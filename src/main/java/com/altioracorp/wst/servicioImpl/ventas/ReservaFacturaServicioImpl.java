package com.altioracorp.wst.servicioImpl.ventas;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import javax.transaction.Transactional;

import org.apache.commons.lang3.SerializationUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.altioracorp.wst.dominio.sistema.Bodega;
import com.altioracorp.wst.dominio.sistema.TipoDocumento;
import com.altioracorp.wst.dominio.ventas.CotizacionDetalle;
import com.altioracorp.wst.dominio.ventas.CotizacionEstado;
import com.altioracorp.wst.dominio.ventas.DocumentoDetalle;
import com.altioracorp.wst.dominio.ventas.DocumentoEstado;
import com.altioracorp.wst.dominio.ventas.DocumentoFactura;
import com.altioracorp.wst.dominio.ventas.DocumentoReserva;
import com.altioracorp.wst.dominio.ventas.DocumentoTransferencia;
import com.altioracorp.wst.dominio.ventas.FacturaResumenDTO;
import com.altioracorp.wst.dominio.ventas.ReservaArticulo;
import com.altioracorp.wst.dominio.ventas.TipoReserva;
import com.altioracorp.wst.dominio.ventas.dto.FacturaDTO;
import com.altioracorp.wst.dominio.ventas.dto.ReservaConsultaDTO;
import com.altioracorp.wst.dominio.ventas.dto.ReservaDTO;
import com.altioracorp.wst.dominio.ventas.dto.ReservaFacturaDTO;
import com.altioracorp.wst.exception.ventas.FacturaBodegaPrincipalNoExisteException;
import com.altioracorp.wst.exception.ventas.FacturaCantidadExcedeASaldoCotizacionException;
import com.altioracorp.wst.exception.ventas.FacturaDetalleCantidadInvalidaException;
import com.altioracorp.wst.exception.ventas.FacturaDetalleVacioException;
import com.altioracorp.wst.exception.ventas.ProcesoFacturaCerrarExisteReservaPendienteException;
import com.altioracorp.wst.repositorio.ventas.ICotizacionDetalleRepositorio;
import com.altioracorp.wst.repositorio.ventas.IDocumentoReservaRepositorio;
import com.altioracorp.wst.servicio.compras.IComprasServicio;
import com.altioracorp.wst.servicio.sistema.IPuntoVentaBodegaServicio;
import com.altioracorp.wst.servicio.ventas.IArticuloServicio;
import com.altioracorp.wst.servicio.ventas.ICotizacionServicio;
import com.altioracorp.wst.servicio.ventas.IDocumentoServicio;
import com.altioracorp.wst.servicio.ventas.IFacturaServicio;
import com.altioracorp.wst.servicio.ventas.IReservaArticuloServicio;
import com.altioracorp.wst.servicio.ventas.IReservaFacturaServicio;
import com.altioracorp.wst.servicio.ventas.ITransferenciaServicio;
import com.altioracorp.wst.util.UtilidadesCadena;
import com.altioracorp.wst.util.UtilidadesSeguridad;
import com.google.gson.Gson;

@Service
public class ReservaFacturaServicioImpl implements IReservaFacturaServicio {

	private static final Log LOG = LogFactory.getLog(ReservaFacturaServicioImpl.class);

	@Autowired
	private IDocumentoReservaRepositorio reservaRepositorio;

	@Autowired
	private IDocumentoServicio documentoServicio;

	@Autowired
	private IArticuloServicio articuloServicio;

	@Autowired
	private IPuntoVentaBodegaServicio puntoVentaBodegaServicio;

	@Autowired
	private ICotizacionDetalleRepositorio cotizacionDetalleRepositorio;

	@Autowired
	private ICotizacionServicio cotizacionServicio;

	@Autowired
	private ValidarDocumento validaciones;

	@Autowired
	private ITransferenciaServicio transferenciaServicio;

	@Autowired
	IFacturaServicio facturaServicio;

	@Autowired
    private IReservaArticuloServicio reservaArticuloServicio;
	
	@Autowired
	private Gson gsonLog;

	@Autowired
	private IComprasServicio comprasServicio;
	
	@Autowired
    private EntityManager entityManager;
	
	private void actualizarSaldosDetalleCotizacion(List<DocumentoDetalle> detalle) {

		if (detalle.size() > 0) {
			Optional<CotizacionDetalle> lineaDetalle = cotizacionDetalleRepositorio
					.findById(detalle.get(0).getCotizacionDetalle().getId());
			if (lineaDetalle.isPresent()) {

				detalle.forEach(x -> {
					LOG.info(String.format("Factura: Actualizando saldo detalle Cotizacion: %s", lineaDetalle.get()));
					BigDecimal saldo = lineaDetalle.get().getSaldo().subtract(x.getCantidad());
					lineaDetalle.get().setSaldo(saldo);
					BigDecimal saldoCantidadReserva = lineaDetalle.get().getCantidadReserva().subtract(x.getCantidadReserva());
					lineaDetalle.get().setCantidadReserva(saldoCantidadReserva);
				});
			}
		}
	}

	@Override
	@Transactional
	public void eliminarReservaDetallesPorCotizacionDetalle(long reservaId, long cotizacionDetalleId) {

		Optional<DocumentoReserva> documentoFacturaOP = reservaRepositorio.findById(reservaId);

		if (documentoFacturaOP.isPresent()) {

			DocumentoReserva reserva = documentoFacturaOP.get();

			List<DocumentoDetalle> documentoDetalles = reserva.getDetalle().stream()
					.filter(x -> x.cotizacionDetalle.getId() == cotizacionDetalleId).collect(Collectors.toList());

			if (documentoDetalles.size() > 0) {

				List<DocumentoDetalle> detalleSinComprasL = filtrarArticulosPorReservar(documentoDetalles);
				LOG.info(String.format("Se filtra detalle de Reserva %s para liberar lo faltante detalle = %s",
						reserva.getNumero(), gsonLog.toJson(detalleSinComprasL)));
				articuloServicio.liberarReservasArticulosYCompartimientos(detalleSinComprasL);

				restablecerSaldoDetalleCotizacion(documentoDetalles);

				documentoDetalles.stream().forEach(detalle -> {
					reserva.eliminarLineaDetalle(detalle);
				});

				reservaRepositorio.save(reserva);

			}
		}
	}

	@Override
	@Transactional
	public DocumentoReserva guardarCabecera(DocumentoReserva reserva) {
		Optional<DocumentoReserva> optional = reservaRepositorio.findByEstadoAndCotizacion_Id(DocumentoEstado.NUEVO,
				reserva.getCotizacion().getId());
		if (optional.isPresent()) {
			LOG.info(String.format("Reserva a guardar : %s", reserva));
			validarDetalleReserva(optional.get());
			optional.get().setEntrega(reserva.getEntrega());
			optional.get().setPorcentajeAbono(0);
			optional.get().setDireccionEntrega(reserva.getDireccionEntrega());
			optional.get().setDireccionEntregaDescripcion(reserva.getDireccionEntregaDescripcion());
			optional.get().setFechaEmision(LocalDateTime.now());
			optional.get().setPeriodoGracia(reserva.getPeriodoGracia());
			optional.get().setRetirarCliente(reserva.getRetirarCliente());
			return optional.get();
		}
		return null;
	}

	@Override
	@Transactional
	public DocumentoReserva guardarDetalle(DocumentoReserva reserva) {

		valdiarCantidadDetalleAGuardar(reserva.getDetalle());

		Optional<DocumentoReserva> optional = reservaRepositorio.findByEstadoAndCotizacion_Id(DocumentoEstado.NUEVO,
				reserva.getCotizacion().getId());
		if (optional.isEmpty()) {
			reserva.setNumero(documentoServicio
					.secuencialDocumento(reserva.getCotizacion().getPuntoVenta(), TipoDocumento.RESERVA)
					.getNumeroSecuencial());
			reserva.inicializarResarva();
			Bodega bodegaPrincipal = puntoVentaBodegaServicio
					.buscarBodegaPrincipalPorPuntoVenta(reserva.getCotizacion().getPuntoVenta());
			if (bodegaPrincipal != null) {
				reserva.setBodegaPrincipal(bodegaPrincipal.getCodigo());
			} else {
				throw new FacturaBodegaPrincipalNoExisteException(reserva.getCotizacion().getPuntoVenta().getNombre());
			}
			LOG.info(String.format("Reserva a guardar : %s", reserva));

			List<DocumentoDetalle> articulosAReservar = filtrarArticulosPorReservar(reserva.getDetalle());
			LOG.info(String.format("Se filtra detalle de Reserva %s para reservar lo faltante detalle = %s",
					reserva.getNumero(), gsonLog.toJson(articulosAReservar)));
			articuloServicio.reservarArticulos(articulosAReservar);

			this.actualizarSaldosDetalleCotizacion(reserva.getDetalle());
			cotizacionServicio.cambiarEstadoCotizacion(reserva.getCotizacion().getId(), CotizacionEstado.POR_FACTURAR,
					"");
			return reservaRepositorio.save(reserva);
		} else {
			validarPickingCantidades(reserva, optional.get().getDetalle());

			reserva.getDetalle().forEach(x -> {

				boolean cantidadSeteada = false;

				for (DocumentoDetalle y : optional.get().getDetalle()) {

					if (x.getCodigoBodega().equals(y.getCodigoBodega())
							&& x.getCotizacionDetalle().getId() == (y.getCotizacionDetalle().getId())) {
						BigDecimal cantidad = y.getCantidad().add(x.getCantidad());
						y.setCantidad(cantidad);
						y.setSaldo(cantidad);
						cantidadSeteada = true;
					}
				}

				if (!cantidadSeteada) {
					optional.get().agregarLineaDetalle(x);
				}

			});
			LOG.info(String.format("Reserva a guardar : %s", reserva));

			List<DocumentoDetalle> articulosAReservar = filtrarArticulosPorReservar(reserva.getDetalle());
			LOG.info(String.format("Se filtra detalle de Reserva %s para reservar lo faltante detalle = %s",
					optional.get().getNumero(), gsonLog.toJson(articulosAReservar)));
			articuloServicio.reservarArticulos(articulosAReservar);

			this.actualizarSaldosDetalleCotizacion(reserva.getDetalle());
			return reservaRepositorio.save(optional.get());
		}
	}

	private void restablecerSaldoDetalleCotizacion(List<DocumentoDetalle> detalle) {

		if (detalle.size() > 0) {

			Optional<CotizacionDetalle> lineaDetalle = cotizacionDetalleRepositorio
					.findById(detalle.get(0).getCotizacionDetalle().getId());

			if (lineaDetalle.isPresent()) {

				CotizacionDetalle cotizacionDetalle = lineaDetalle.get();

				detalle.forEach(x -> {
					LOG.info(String.format("Factura: Restablecer saldo detalle Cotizacion: %s", cotizacionDetalle));
					BigDecimal saldo = cotizacionDetalle.getSaldo().add(x.getCantidad());
					BigDecimal saldoCantidadReserva = cotizacionDetalle.getCantidadReserva().add(x.getCantidadReserva());
					if (saldo.compareTo(cotizacionDetalle.getCantidad()) == -1
							|| saldo.compareTo(cotizacionDetalle.getCantidad()) == 0) {
						cotizacionDetalle.setSaldo(saldo);
						cotizacionDetalle.setCantidadReserva(saldoCantidadReserva);
					}
				});

				cotizacionDetalleRepositorio.save(cotizacionDetalle);
			}
		}
	}

	private void valdiarCantidadDetalleAGuardar(List<DocumentoDetalle> detalle) {
		if (detalle.stream().anyMatch(x -> x.getCantidad().compareTo(BigDecimal.ZERO) == 0)) {
			throw new FacturaDetalleCantidadInvalidaException();
		}
	}

	private void validarDetalleReserva(DocumentoReserva reserva) {
		if (reserva.getDetalle().isEmpty()) {
			throw new FacturaDetalleVacioException();
		}
	}

	private void validarPickingCantidades(DocumentoReserva reserva, List<DocumentoDetalle> detalleRegistrado) {
		CotizacionDetalle detalleItem = reserva.getDetalle().stream().findFirst().get().getCotizacionDetalle();
		BigDecimal cantidadRegistrada = detalleRegistrado.stream()
				.filter(x -> x.getCotizacionDetalle().getId() == detalleItem.getId()).map(x -> x.getCantidad())
				.reduce(BigDecimal.ZERO, BigDecimal::add);
		BigDecimal cantidadPorRegistrar = reserva.getDetalle().stream().map(x -> x.getCantidad())
				.reduce(BigDecimal.ZERO, BigDecimal::add);

		if ((cantidadRegistrada.add(cantidadPorRegistrar)).compareTo(detalleItem.getCantidad()) == 1) {
			throw new FacturaCantidadExcedeASaldoCotizacionException(detalleItem.getCodigoArticulo());
		}

	}

	@Override
	public FacturaResumenDTO crearResumenFactura(Long reservaId) {
		Optional<DocumentoReserva> reserva = reservaRepositorio.findById(reservaId);
		FacturaResumenDTO dto = new FacturaResumenDTO();
		if (reserva.isPresent()) {
			// TODO: Se agrupa por cotizacionDetalle_id
			reserva.get().getDetalle().forEach(x -> x.setCompartimientos(null));
			DocumentoReserva reservaClonado = SerializationUtils.clone(reserva.get());

			Map<Long, List<DocumentoDetalle>> detalleAgrupado = reserva.get().getDetalle().stream()
					.collect(Collectors.groupingBy(x -> x.getCotizacionDetalle().getId()));

			detalleAgrupado.forEach((id, detalleItem) -> {
				BigDecimal cantidadTotal = detalleItem.stream().map(x -> x.getCantidad()).reduce(BigDecimal.ZERO,
						BigDecimal::add);
				DocumentoDetalle detalleTemp = detalleItem.stream().findFirst().get();
				detalleTemp.setCantidad(cantidadTotal);
				dto.agregarLineaDetalle(detalleTemp);

			});

			reservaClonado.getDetalle().forEach(x -> {
				// dto.agregarLineaDetalle(x);
				if (!x.getCodigoBodega().equalsIgnoreCase(reserva.get().getBodegaPrincipal())) {
					dto.agregarLineaTransferencia(x);
				}

			});
			return dto;
		}
		return dto;
	}

	@Override
	public DocumentoReserva listarPorCotizacionID(long cotizacionID) {
		return reservaRepositorio
				.findByEstadoAndTipoReservaAndCotizacion_Id(DocumentoEstado.NUEVO, TipoReserva.FACTURA, cotizacionID)
				.orElse(reservaRepositorio.findByEstadoAndTipoReservaAndCotizacion_Id(DocumentoEstado.REVISION,
						TipoReserva.FACTURA, cotizacionID).orElse(null));
	}

	@Override
	public List<String> validarReservaParaFacturar(long reservaId) {
		List<String> inconsistencias = new ArrayList<>();
		Optional<DocumentoReserva> reserva = reservaRepositorio.findById(reservaId);

		if (reserva.isPresent()) {
			inconsistencias = validaciones.validarReserva(reserva.get());
			cotizacionServicio.cambiarEstadoCotizacion(reserva.get().getCotizacion().getId(),
					CotizacionEstado.POR_FACTURAR, null);
			return inconsistencias;
		}
		return inconsistencias;
	}

	private FacturaDTO generarTransferencias(DocumentoReserva reserva, FacturaDTO facturaDTO) {

		Map<String, List<DocumentoDetalle>> bodegas = reserva.getDetalle().stream()
				.filter(x -> !x.getCodigoBodega().equalsIgnoreCase(reserva.getBodegaPrincipal())
						&& !x.getCotizacionDetalle().getServicio())
				.collect(Collectors.groupingBy(DocumentoDetalle::getCodigoBodega, Collectors.toList()));

		bodegas.forEach((bodega, lista) -> {
			DocumentoTransferencia transferencia = new DocumentoTransferencia(bodega, reserva.getBodegaPrincipal(),
					reserva);
			transferencia.setReferencia(reserva.getNumero());

			lista.forEach(x -> {
				DocumentoDetalle detalle = new DocumentoDetalle(x.getCotizacionDetalle(), x.getCodigoBodega(),
						x.getCantidad());
				transferencia.agregarLineaDetalle(detalle);
			});

			transferenciaServicio.guardar(transferencia);

			facturaDTO.agregarNumeroTransferencia(transferencia.getNumero());

		});

		return facturaDTO;

	}

	@Override
	@Transactional
	public FacturaDTO procesarReservaFactura(long reservaId) {
		
		Optional<DocumentoReserva> optional = reservaRepositorio.findById(reservaId);

		FacturaDTO facturaDTO = new FacturaDTO();

		if (optional.isPresent()) {
			
			DocumentoReserva reserva = optional.get();
			
			if ((reserva.getEstado().equals(DocumentoEstado.NUEVO)) || (reserva.getEstado().equals(DocumentoEstado.APROBADO))) {
				
				if (reserva.getDetalle().stream().allMatch(x -> x.getCodigoBodega().equals(reserva.getBodegaPrincipal()))) {
					
					integrarFactura(facturaDTO, reserva);
					
				} else {
					
					generarTransferencias(reserva, facturaDTO);
					
					reserva.setEstado(DocumentoEstado.EMITIDO);
					
				}
				
			} else if (reserva.getEstado().equals(DocumentoEstado.EN_PROCESO)) {
				
				integrarFactura(facturaDTO, reserva);
			}

			reservaRepositorio.save(reserva);

		}
		
		return facturaDTO;
		
	}

	private void integrarFactura(FacturaDTO facturaDTO, DocumentoReserva reserva) {
		reserva.setEstado(DocumentoEstado.COMPLETADO);
		DocumentoFactura factura = facturaServicio.generarFactura(reserva);
		facturaDTO.setIdFactura(factura.getId());
		facturaDTO.setNumeroFactura(factura.getNumero());
		facturaDTO.setEstado(factura.getEstado());
		facturaDTO.setFacturaDespachada(factura.isDespachada());
	}

//	@Override
//	@Transactional
//	public DocumentoFactura procesarFacturaEnTransferencia(DocumentoReserva reserva) {
//		
//		reserva.setEstado(DocumentoEstado.COMPLETADO);
//		
//		DocumentoFactura factura = facturaServicio.generarFactura(reserva);
//		
//		reservaRepositorio.save(reserva);
//		
//		return factura;
//	}

	@Override
	public List<ReservaFacturaDTO> listarTodasPorCotizacionID(long cotizacionID) {
		List<ReservaFacturaDTO> dto = new ArrayList<>();

		List<DocumentoReserva> reservas = reservaRepositorio.findByCotizacion_IdOrderByModificadoPorAsc(cotizacionID);

		reservas.forEach(x -> {
			dto.add(new ReservaFacturaDTO(x.getId(), x.getNumero(), x.getEstado(), x.getTipoReserva()));
		});

		return dto;
	}

	@Override
	public List<String> listarNumeroReservasEmitidasPorCotizacionID(long cotizacionID) {

		List<DocumentoReserva> reservas = reservaRepositorio.findByEstadoInAndTipoReservaInAndCotizacion_Id(
				Arrays.asList(DocumentoEstado.EMITIDO), Arrays.asList(TipoReserva.FACTURA), cotizacionID);

		return reservas.stream().map(x -> x.getNumero()).collect(Collectors.toList());
	}

	@Override
	public DocumentoReserva buscarReserva(long reservaId) {

		Optional<DocumentoReserva> reservaOpcional = reservaRepositorio.findById(reservaId);

		if (reservaOpcional.isPresent()) {
			return reservaOpcional.get();
		}

		return null;
	}

	@Override
	@Transactional
	public void anularReserva(DocumentoReserva reserva) {
		// Al anular la reserva es solo de la bodega principal
		if (reserva != null) {

			if (reserva.getEstado().equals(DocumentoEstado.EMITIDO)
					|| reserva.getEstado().equals(DocumentoEstado.EN_PROCESO)) {

				List<DocumentoDetalle> detalle = new ArrayList<DocumentoDetalle>();

				if (reserva.getEstado().equals(DocumentoEstado.EMITIDO)) {

					detalle = reserva.getDetalle().stream()
							.filter(x -> x.getCodigoBodega().equalsIgnoreCase(reserva.getBodegaPrincipal()))
							.collect(Collectors.toList());

				}

				if (reserva.getEstado().equals(DocumentoEstado.EN_PROCESO)) {

					detalle = reserva.getDetalle().stream().collect(Collectors.toList());
					detalle.stream().forEach(x -> x.setCodigoBodega(reserva.getBodegaPrincipal()));

				}

				this.articuloServicio.liberarReservasArticulos(detalle);

				reserva.setEstado(DocumentoEstado.ANULADO);

				reservaRepositorio.save(reserva);
				
				LOG.info(String.format("Reserva %s ANULADA",reserva.getNumero()));

				try {
					cerrar(reserva.getCotizacion().getId());
				} catch (Exception e) {
					LOG.info(
							String.format("No se puede cerrar la cotización: %s", reserva.getCotizacion().getNumero()));
				}
			}
		}
	}

	@Override
	@Transactional
	public boolean cerrar(long cotizacionId) {
		verificarReservaEmitidas(cotizacionId);
		cotizacionServicio.liberarReservaComprasL(cotizacionId);
		cotizacionServicio.cambiarEstadoCotizacion(cotizacionId, CotizacionEstado.CERRADO, "");
		Optional<DocumentoReserva> reserva = reservaRepositorio.findByEstadoAndCotizacion_Id(DocumentoEstado.NUEVO,
				cotizacionId);

		if (reserva.isPresent()) {
			LOG.info(String.format("Anulando Reserva %s de la Cotización %s", reserva.get().getNumero(),
					reserva.get().getCotizacion().getNumero()));

			if (reserva.get().getEstado().equals(DocumentoEstado.NUEVO)) {

				reserva.get().getDetalle().forEach(x -> {
					reservaArticuloServicio.decrementarReserva(new ReservaArticulo(
							x.getCotizacionDetalle().getCodigoArticulo(), x.getCodigoBodega(), x.getCantidad()));

					BigDecimal saldo = x.getCotizacionDetalle().getSaldo().add(x.getCantidad());
					x.getCotizacionDetalle().setSaldo(saldo);
				});

				reserva.get().setEstado(DocumentoEstado.CERRADO);
				reservaRepositorio.save(reserva.get());
				
			}
		}
		
		comprasServicio.cerrarOrdenesCompraPorCotizacionId(cotizacionId);

		return true;
	}
		

	private void verificarReservaEmitidas(long cotizacionID) {
		List<DocumentoReserva> reservas = reservaRepositorio.findByEstadoInAndTipoReservaInAndCotizacion_Id(
				Arrays.asList(DocumentoEstado.EMITIDO, DocumentoEstado.EN_PROCESO), Arrays.asList(TipoReserva.FACTURA),
				cotizacionID);
		if (!reservas.isEmpty()) {
			throw new ProcesoFacturaCerrarExisteReservaPendienteException(reservas.get(0).getCotizacion().getNumero());
		}
	}

	@Override
	@Transactional
	public void cambiarEstado(DocumentoReserva reserva, DocumentoEstado estado) {

		reserva.setEstado(estado);

		reservaRepositorio.save(reserva);
	}

	@Override
	public List<DocumentoReserva> listarReservasPorFacturarPorUsuarioYPuntoVenta(long puntoVentaId) {

		return reservaRepositorio
				.findByCreadoPorAndEstadoAndTipoReservaAndCotizacion_PuntoVenta_IdOrderByModificadoFechaDesc(
						UtilidadesSeguridad.usuarioEnSesion(), DocumentoEstado.EN_PROCESO, TipoReserva.FACTURA,
						puntoVentaId);
	}

	@Override
	public void anularReservaPorId(Long reservaId) {
		Optional<DocumentoReserva> reserva = reservaRepositorio.findById(reservaId);
		if (reserva.isPresent()) {
			LOG.info(String.format("Anulando Reserva %s", reserva.get().getNumero()));
			this.anularReserva(reserva.get());
		}
	}

	private List<DocumentoDetalle> filtrarArticulosPorReservar(List<DocumentoDetalle> detalle) {
		List<DocumentoDetalle> nuevoDetalle = new ArrayList<>();
		
		detalle.forEach(x ->{
			if(x.getCantidadReserva().compareTo(BigDecimal.ZERO) == 0) {
				nuevoDetalle.add(x);
			}else {
				if( x.getCantidad().compareTo(x.getCantidadReserva()) == 1) {
					BigDecimal diferencia = x.getCantidad().subtract(x.getCantidadReserva());
					nuevoDetalle.add(new DocumentoDetalle(x.getCotizacionDetalle(),x.getCodigoBodega(), diferencia, x.getCantidadReserva()));
				}
			}			
		});
		
		return nuevoDetalle;
	}

	@Override
	public  Page<ReservaDTO> consultaReservas(final Pageable pageable, final ReservaConsultaDTO consulta) {
		try {
			final CriteriaBuilder criteriaBuilder = this.entityManager.getCriteriaBuilder();
            final CriteriaQuery<DocumentoReserva> query = criteriaBuilder.createQuery(DocumentoReserva.class);

            final Root<DocumentoReserva> cobroRoot = query.from(DocumentoReserva.class);
            final List<Predicate> predicadosConsulta = new ArrayList<Predicate>();
            
            Predicate predicateEstadoNuevo = criteriaBuilder.equal(cobroRoot.get("estado"), DocumentoEstado.NUEVO);            
            Predicate predicateEstadoRevision = criteriaBuilder.equal(cobroRoot.get("estado"), DocumentoEstado.REVISION);
            Predicate predicateEstadoAprobado = criteriaBuilder.equal(cobroRoot.get("estado"), DocumentoEstado.APROBADO);
            Predicate predicateEstadoEmitido = criteriaBuilder.equal(cobroRoot.get("estado"), DocumentoEstado.EMITIDO);
            Predicate predicateEstadoEnProceso = criteriaBuilder.equal(cobroRoot.get("estado"), DocumentoEstado.EN_PROCESO);
            predicadosConsulta.add(criteriaBuilder.or(predicateEstadoNuevo, predicateEstadoRevision, predicateEstadoAprobado, predicateEstadoEmitido, predicateEstadoEnProceso));
            
            if (UtilidadesCadena.noEsNuloNiBlanco(consulta.getVendedor())) {
				predicadosConsulta.add(criteriaBuilder.equal(cobroRoot.get("creadoPor"),consulta.getVendedor()));
			}
            
            if (consulta.getIdPuntoVenta() != null) {
                predicadosConsulta.add(criteriaBuilder.equal(cobroRoot.get("cotizacion").get("puntoVenta").get("id"), consulta.getIdPuntoVenta()));
            }
            
            if (consulta.getFechaInicio() != null && consulta.getFechaFin() != null) {
            	Date startDate = Date.from(consulta.getFechaInicio().withHour(0).withMinute(0).withSecond(0).atZone(ZoneId.systemDefault()).toInstant());
            	Date endDate = Date.from( consulta.getFechaFin().withHour(23).withMinute(59).withSecond(59).atZone(ZoneId.systemDefault()).toInstant());
                predicadosConsulta.add(criteriaBuilder.between(cobroRoot.get("creadoFecha"),
                		startDate,endDate ));
            }

            if (consulta.getFechaInicio() != null && consulta.getFechaFin() == null) {
            	Date startDate = Date.from(consulta.getFechaInicio().withHour(0).withMinute(0).withSecond(0).atZone(ZoneId.systemDefault()).toInstant());
            	Date endDate = Date.from(consulta.getFechaInicio().withHour(23).withMinute(59).withSecond(59).atZone(ZoneId.systemDefault()).toInstant());
            	predicadosConsulta.add(criteriaBuilder.between(cobroRoot.get("creadoFecha"),
            			startDate, endDate));
            }
            
            query.where(predicadosConsulta.toArray(new Predicate[predicadosConsulta.size()]))
            .orderBy(criteriaBuilder.desc(cobroRoot.get("creadoFecha")));

		    final TypedQuery<DocumentoReserva> statement = this.entityManager.createQuery(query);
		
		    List<DocumentoReserva> cobrosResult = statement.getResultList();
		    
		    final int sizeTotal = cobrosResult.size();

            final int start = (int) pageable.getOffset();
            final int end = (start + pageable.getPageSize()) > cobrosResult.size() ? cobrosResult.size() : (start + pageable.getPageSize());

            cobrosResult = cobrosResult.subList(start, end);
            
			List<ReservaDTO> resultadoConsulta = cobrosResult.stream()
					.map(c -> {
						return new ReservaDTO(c.getId(), c.getNumero(), c.getCotizacion().getNumero(), c.getEstado(),
								c.getCreadoPor(), c.getCreadoPor(),
								c.getCreadoFecha().toInstant().atZone(ZoneId.systemDefault()).toLocalDate(),
								c.getCotizacion().getNombreCliente(), c.getCotizacion().getCodigoCliente(),
								c.getCotizacion().getPuntoVenta().getNombre());
					}).collect(Collectors.toList());

		    final Page<ReservaDTO> pageResut = new PageImpl<ReservaDTO>(resultadoConsulta, pageable, sizeTotal);
		    
		    return pageResut;
			
		}catch (final Exception ex) {
			
			LOG.error(ex);
            final Page<ReservaDTO> pageResult = new PageImpl<ReservaDTO>(new ArrayList<ReservaDTO>(), pageable, 0);
            return pageResult;
        }
		
	}

	@Override
	public List<DocumentoDetalle> obtenerDetallePorReservaID(long reservaId) {
		Optional<DocumentoReserva> optional = reservaRepositorio.findById(reservaId);
		if (optional.isPresent()) {
			return optional.get().getDetalle();
		}
		return new ArrayList<DocumentoDetalle>();
	}
}
