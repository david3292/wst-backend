package com.altioracorp.wst.servicioImpl.ventas;

import static com.altioracorp.wst.util.UtilidadesSeguridad.nombreUsuarioEnSesion;

import java.io.File;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.altioracorp.gpintegrator.client.Customer.Customer;
import com.altioracorp.gpintegrator.client.Item.Item;
import com.altioracorp.wst.dominio.sistema.Bodega;
import com.altioracorp.wst.dominio.sistema.CondicionPago;
import com.altioracorp.wst.dominio.sistema.ConfigSistema;
import com.altioracorp.wst.dominio.sistema.ConfiguracionSistema;
import com.altioracorp.wst.dominio.sistema.PerfilNombre;
import com.altioracorp.wst.dominio.sistema.Secuencial;
import com.altioracorp.wst.dominio.sistema.TipoDocumento;
import com.altioracorp.wst.dominio.sistema.UsuarioPerfil;
import com.altioracorp.wst.dominio.ventas.Cotizacion;
import com.altioracorp.wst.dominio.ventas.CotizacionDetalle;
import com.altioracorp.wst.dominio.ventas.CotizacionEstado;
import com.altioracorp.wst.dominio.ventas.CriterioArticuloDTO;
import com.altioracorp.wst.dominio.ventas.DocumentoEstado;
import com.altioracorp.wst.dominio.ventas.DocumentoReserva;
import com.altioracorp.wst.dominio.ventas.ReservaArticulo;
import com.altioracorp.wst.dominio.ventas.dto.CotizacionReporteDTO;
import com.altioracorp.wst.exception.sistema.ConfiguracionSistemaNoExisteException;
import com.altioracorp.wst.exception.ventas.CotizacionClienteNoEncontradoException;
import com.altioracorp.wst.exception.ventas.CotizacionExcedeLimiteVariacionPrecioException;
import com.altioracorp.wst.exception.ventas.CotizacionNoEncontrada;
import com.altioracorp.wst.exception.ventas.CotizacionNumeroYaExisteException;
import com.altioracorp.wst.exception.ventas.DocumentoRecotizarTiempoExcedido;
import com.altioracorp.wst.exception.ventas.UsuarioNoTieneCodigoVendedorException;
import com.altioracorp.wst.repositorio.sistema.IConfiguracionSistemaRepositorio;
import com.altioracorp.wst.repositorio.sistema.IUsuarioPerfilRepositorio;
import com.altioracorp.wst.repositorio.ventas.ICotizacionDetalleRepositorio;
import com.altioracorp.wst.repositorio.ventas.ICotizacionRepositorio;
import com.altioracorp.wst.repositorio.ventas.IDocumentoReservaRepositorio;
import com.altioracorp.wst.servicio.compras.IComprasServicio;
import com.altioracorp.wst.servicio.sistema.ICondicionPagoServicio;
import com.altioracorp.wst.servicio.sistema.IPuntoVentaBodegaServicio;
import com.altioracorp.wst.servicio.sistema.ISecuencialServicio;
import com.altioracorp.wst.servicio.ventas.IAprobacionCotizacionServicio;
import com.altioracorp.wst.servicio.ventas.IArticuloServicio;
import com.altioracorp.wst.servicio.ventas.IClienteServicio;
import com.altioracorp.wst.servicio.ventas.ICotizacionHistorialEstadoServicio;
import com.altioracorp.wst.servicio.ventas.ICotizacionServicio;
import com.altioracorp.wst.servicio.ventas.IReservaArticuloServicio;
import com.altioracorp.wst.util.UtilidadesCadena;
import com.altioracorp.wst.util.UtilidadesSeguridad;
import com.google.gson.Gson;

import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;

@Service
public class CotizacionServicioImpl implements ICotizacionServicio {

	private static final Log LOG = LogFactory.getLog(CotizacionServicioImpl.class);

	@Autowired
	private ICotizacionRepositorio cotizacionRepositorio;

	@Autowired
	private ICotizacionDetalleRepositorio cotizacionDetalleRepositorio;

	@Autowired
	private IClienteServicio clienteServicio;

	@Autowired
	private IAprobacionCotizacionServicio aprobacionServicio;

	@Autowired
	private ICotizacionHistorialEstadoServicio cotizacionHistorialEstadoServicio;

	@Autowired
	private IConfiguracionSistemaRepositorio sistemaRepositorio;

	@Autowired
	private ISecuencialServicio secuencialServicio;

	@Autowired
	private IArticuloServicio articuloServicio;

	@Autowired
	private IUsuarioPerfilRepositorio usuarioPerfilRepositorio;

	@Autowired
	private ValidarDocumento validaciones;

	@Autowired
	private IComprasServicio comprasServicio;

	@Autowired
	private IDocumentoReservaRepositorio documentoReservaRepositorio;

	@Autowired
	private IPuntoVentaBodegaServicio puntoVentaBodegaServicio;

	@Autowired
	private IReservaArticuloServicio reservaArticuloServicio;
	
	@Autowired
	private Gson gsonLog;
	
	@Autowired
	private ICondicionPagoServicio condicionPagoServicio;
	
	@Autowired
	private IComprasServicio ordenesCompraServicio;

	@Override
	public Cotizacion registrar(Cotizacion obj) {

		obj.setFechaEmision(LocalDateTime.now());

		obj.setCodigoVendedor(obtenerCodigoVendedor());

		this.asegurarCotizacionNumeroUnico(obj.getNumero());

		LOG.info(String.format("Cotización Nueva a Guardar: %s", obj));

		validarDescuentoAdicionalNoExedido(obj.getDetalle());
		return cotizacionRepositorio.save(obj);
	}

	@Override
	@Transactional
	public Cotizacion modificar(Cotizacion obj) {
		Optional<Cotizacion> cotizacionRecargado = cotizacionRepositorio.findById(obj.getId());
		if (cotizacionRecargado.isPresent()) {
			LOG.info(String.format("Cotización a Guardar: %s", obj));
			cotizacionRecargado.get().setCodigoCliente(obj.getCodigoCliente());
			cotizacionRecargado.get().setNombreCliente(obj.getNombreCliente());
			cotizacionRecargado.get().setEstado(obj.getEstado());
			cotizacionRecargado.get().setCodigoVendedor(obtenerCodigoVendedor());
			cotizacionRecargado.get().setOrdenCliente(obj.getOrdenCliente());
			cotizacionRecargado.get().setFormaPago(obj.getFormaPago());
			cotizacionRecargado.get().setCondicionPago(obj.getCondicionPago());
			// cotizacionRecargado.get().setControles(obj.getControles());
			cotizacionRecargado.get().setComentario(obj.getComentario());
		} else {
			throw new CotizacionNoEncontrada();
		}
		return cotizacionRecargado.get();
	}

	@Override
	public List<Cotizacion> listarTodos() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Cotizacion listarPorId(Long id) {
		Optional<Cotizacion> cotizacion = cotizacionRepositorio.findById(id);
		return cotizacion.isPresent() ? cotizacion.get() : null;
	}

	@Override
	public boolean eliminar(Long id) {
		LOG.info(String.format("Cotización id= %s a Eliminar ", id));
		cotizacionRepositorio.deleteById(id);
		return true;
	}

	@Override
	public List<Cotizacion> listarPorUsuarioYPuntoVenta(Long puntoVentaID) {
		String usuarioSesion = nombreUsuarioEnSesion();
		List<Cotizacion> cotizaciones = cotizacionRepositorio
				.findByCreadoPorAndPuntoVenta_idOrderByFechaEmisionDesc(usuarioSesion, puntoVentaID);
		cotizaciones.forEach(x -> x.setDetalle(new ArrayList<>()));
		return cotizaciones;
	}

	@Override
	public byte[] cotizarDocumento(Cotizacion obj) {
		Cotizacion cotizacionRecargada = null;
		obj.setEstado(CotizacionEstado.EMITIDO);

		if (obj.getId() == 0) {
			cotizacionRecargada = this.registrar(obj);
		} else {
			cotizacionRecargada = this.modificar(obj);
		}

		cotizacionHistorialEstadoServicio.registrar(cotizacionRecargada, null);

		if (cotizacionRecargada != null) {
			return this.crearReportePdf(cotizacionRecargada);
		} else {
			return null;
		}
	}

	@Override
	@Transactional(readOnly = false, propagation = Propagation.REQUIRED)
	public void enviarDocumentoAprobar(Cotizacion cotizacion) {
		Optional<Cotizacion> cotizacionRecargado = cotizacionRepositorio.findById(cotizacion.getId());
		if (cotizacionRecargado.isPresent()) {
			cotizacionRecargado.get().setEstado(CotizacionEstado.REVISION);
			LOG.info(String.format("Documento por aprobar: %s", cotizacionRecargado.get()));
			aprobacionServicio.lanzarSolictud(cotizacionRecargado.get());
			cotizacionHistorialEstadoServicio.registrar(cotizacionRecargado.get(),
					cotizacionRecargado.get().getComentario());
		}

	}

	private byte[] crearReportePdf(Cotizacion cabecera) {
		byte[] data = null;
		HashMap<String, String> params = new HashMap<String, String>();
		params.put("imgLogo", "/public/img/logo.png");
		;
		try {
			File file = new ClassPathResource("/reporte/cotizacion.jasper").getFile();
			JasperPrint print = JasperFillManager.fillReport(file.getPath(), null,
					new JRBeanCollectionDataSource(Collections.singleton(this.crearCotizacionReporteDTO(cabecera))));
			data = JasperExportManager.exportReportToPdf(print);

		} catch (Exception e) {
			e.printStackTrace();
		}
		return data;
	}

	private CotizacionReporteDTO crearCotizacionReporteDTO(Cotizacion cabecera) {
		CotizacionReporteDTO dto = new CotizacionReporteDTO();

		Optional<ConfiguracionSistema> config = this.sistemaRepositorio.findByNombre(ConfigSistema.VIGENCIA_COTIZACION);
		if (config.isPresent()) {
			int vigencia = (int) config.get().getValor();
			dto.setTiempoVigencia(String.format("%s %s", vigencia, config.get().getUnidadMedida().getDescripcion()));
		}
		
		Optional<CondicionPago> condicionPago = condicionPagoServicio.buscarPorTermino(cabecera.getCondicionPago());
		if( condicionPago.isPresent()) {
			dto.setDiasPlazo(condicionPago.get().getTotalDias());
		}
		
		dto.setCabecera(cabecera);
		Customer cliente = clienteServicio.obtenerCustomerPorCurstomerNumbre(cabecera.getCodigoCliente());
		dto.setCliente(cliente);
		dto.ordenarDetalle();
		return dto;
	}

	private void asegurarCotizacionNumeroUnico(String numero) {
		Optional<Cotizacion> cotizacion = cotizacionRepositorio.findByNumero(numero);
		if (cotizacion.isPresent()) {
			throw new CotizacionNumeroYaExisteException(numero);
		}
	}

	@Override
	public byte[] imprimirDocumento(Long id) {
		Optional<Cotizacion> cotizacion = cotizacionRepositorio.findById(id);
		if (cotizacion.isPresent()) {
			/*
			 * if (cotizacion.get().getEstado().equals(CotizacionEstado.APROBADO)) {
			 * cotizacion.get().setEstado(CotizacionEstado.EMITIDO);
			 * cotizacionRepositorio.save(cotizacion.get());
			 * aprobacionHistorialDocumentoServicio.registrar(cotizacion.get(), ""); }
			 */
			return this.crearReportePdf(cotizacion.get());
		} else {
			throw new CotizacionNoEncontrada();
		}
	}

	@Override
	@Transactional
	public boolean anularDocumento(Cotizacion documento) {
		Optional<Cotizacion> cotizacionRecargada = cotizacionRepositorio.findById(documento.getId());
		if (cotizacionRecargada.isPresent()) {
			LOG.info(String.format("Anulando Documento No %s", documento.getNumero()));
			cotizacionRecargada.get().setEstado(CotizacionEstado.ANULADO);
			
			comprasServicio.cerrarOrdenesCompraPorCotizacionId(cotizacionRecargada.get().getId());
		}
		return true;
	}

	@Override
	public Cotizacion recotizarDocumento(Long idDocumento) {
		LOG.info(String.format("Iniciando RECOTIZACION"));
		Optional<Cotizacion> documentoVencido = cotizacionRepositorio.findById(idDocumento);
		if (documentoVencido.isPresent()) {
			LOG.info(String.format("Recotizando Documento N: %s", documentoVencido.get().getNumero()));

			if (documentoVencido.get().getEstado().equals(CotizacionEstado.VENCIDO)) {
				asegurarPuedeRecotizar(documentoVencido.get());
			}

			Cotizacion documentoNuevo = new Cotizacion();
			documentoNuevo.setActivo(Boolean.TRUE);
			documentoNuevo.setCodigoCliente(documentoVencido.get().getCodigoCliente());
			documentoNuevo.setCondicionPago(documentoVencido.get().getCondicionPago());
			documentoNuevo.setDetalle(actualizarPreciosArticulos(documentoVencido.get().getDetalle()));
			documentoNuevo.setEmpresa(documentoVencido.get().getEmpresa());
			documentoNuevo.setEstado(CotizacionEstado.NUEVO);
			documentoNuevo.setFechaEmision(LocalDateTime.now());
			documentoNuevo.setNombreCliente(documentoVencido.get().getNombreCliente());
			documentoNuevo.setFormaPago(documentoVencido.get().getFormaPago());
			documentoNuevo.setPuntoVenta(documentoVencido.get().getPuntoVenta());
			actualizarDatosCliente(documentoNuevo);
			documentoNuevo.setOrdenCliente(documentoVencido.get().getOrdenCliente());
			documentoNuevo.setComentario(documentoVencido.get().getComentario());
			documentoNuevo.setCodigoVendedor(documentoVencido.get().getCodigoVendedor());
			Secuencial secuencial = this.secuencialServicio.ObtenerSecuencialPorPuntoVentaYTipoDocumento(
					documentoVencido.get().getPuntoVenta().getId(), TipoDocumento.COTIZACION);
			documentoNuevo.setNumero(secuencial.getNumeroSecuencial());
			documentoNuevo.calcularTotales();

			LOG.info(String.format("RECOTIZACION creando una nueva cotizacion : %s", documentoNuevo));

			cotizacionRepositorio.save(documentoNuevo);

			if (documentoVencido.get().getEstado().equals(CotizacionEstado.EMITIDO)) {
				cambiarEstadoCotizacion(idDocumento, CotizacionEstado.ANULADO, "RECOTIZADA");
			}

			return documentoNuevo;
		} else {
			throw new CotizacionNoEncontrada();
		}

	}

	private List<CotizacionDetalle> actualizarPreciosArticulos(List<CotizacionDetalle> detalle) {
		LOG.info(String.format("RECOTIZACION: Actualizando Precios a %s articulos", detalle.size()));
		List<CotizacionDetalle> detalleActualizado = new ArrayList<>();
		detalle.forEach(item -> {
			Optional<Item> articulo = this.articuloServicio
					.obtenerArticulosPorCriterio(new CriterioArticuloDTO(item.getCodigoArticulo())).stream()
					.findFirst();
			CotizacionDetalle detalleNuevo = new CotizacionDetalle();
			detalleNuevo.setCantidad(item.getCantidad());
			detalleNuevo.setClaseArticulo(item.getClaseArticulo());
			detalleNuevo.setCodigoArticulo(item.getCodigoArticulo());
			detalleNuevo.setDescripcionArticulo(item.getDescripcionArticulo());
			detalleNuevo.setDescuentoAdicional(item.getDescuentoAdicional());
			detalleNuevo.setDescuentoFijo(item.getDescuentoFijo());
			detalleNuevo.setImpuestoDetalle(item.getImpuestoDetalle());
			detalleNuevo.setImpuestoValor(item.getImpuestoValor());
			detalleNuevo.setPesoArticulo(item.getPesoArticulo());
			detalleNuevo.setCostoUnitario(item.getCostoUnitario());
			detalleNuevo.setPrecio(item.getPrecio());
			detalleNuevo.setSaldo(item.getCantidad());
			detalleNuevo.setCodigoArticuloAlterno(item.getCodigoArticuloAlterno());
			detalleNuevo.setServicio(item.getServicio());
			detalleNuevo.setGeneraCompra(false);
			detalleNuevo.setUnidadMedida(item.getUnidadMedida());
			detalleNuevo.setUnidadMedidaCompra(item.getUnidadMedidaCompra());
			if (articulo.isPresent()) {
				detalleNuevo.setPrecio(articulo.get().getUomprice());
				detalleNuevo.setPrecioGP(articulo.get().getUomprice());
				detalleNuevo.setDescuentoFijo(articulo.get().getUscatvls_6());
				detalleNuevo.setDescuentoFijoGP(articulo.get().getUscatvls_6());
				detalleNuevo.setDescripcionArticuloGP(articulo.get().getItemdesc());
				detalleNuevo.calcularPrecioUnitario();
				detalleNuevo.calcularValorDescuento();
				detalleNuevo.calcularSubtotal();
				detalleNuevo.calcularTotal();
			}
			detalleActualizado.add(detalleNuevo);
		});

		return detalleActualizado;
	}

	private void actualizarDatosCliente(Cotizacion documentoNuevo) {
		LOG.info(String.format("RECOTIZACION: Actualizando Cliente: %s", documentoNuevo.getCodigoCliente()));
		Customer clienteRecuperado = this.clienteServicio
				.obtenerCustomerPorCurstomerNumbre(documentoNuevo.getCodigoCliente());
		if (clienteRecuperado != null) {
			documentoNuevo.setCondicionPago(clienteRecuperado.getPymtrmid());
			documentoNuevo.setCodigoDireccion(clienteRecuperado.getAdrscode());
			documentoNuevo.setDescripcionDireccion(clienteRecuperado.getAddress1());
		}
	}

	private void asegurarPuedeRecotizar(Cotizacion documento) {
		Optional<ConfiguracionSistema> config = this.sistemaRepositorio
				.findByNombre(ConfigSistema.MAXIMO_DIAS_RECOTIZACION);

		if (config.isPresent()) {

			LocalDateTime fechaLimite = LocalDateTime.now();
			switch (config.get().getUnidadMedida()) {
			case HORAS:
				fechaLimite.plusHours((long) config.get().getValor());
				break;
			case DIAS:
				fechaLimite.plusDays((long) config.get().getValor());
				break;
			default:
				break;
			}

			if (documento.getFechaVencimiento().isAfter(fechaLimite)) {
				throw new DocumentoRecotizarTiempoExcedido(documento.getNumero());
			}

		} else {
			throw new ConfiguracionSistemaNoExisteException(ConfigSistema.MAXIMO_DIAS_RECOTIZACION.getDescripcion());
		}
	}

	@Override
	public void cambiarEstadoCotizacion(long cotizacionID, CotizacionEstado estado, String observacion) {
		Optional<Cotizacion> cotizacionRecargada = cotizacionRepositorio.findById(cotizacionID);
		if (cotizacionRecargada.isPresent()) {
			LOG.info(String.format("Se cambia Cotización No %s a estado %s", cotizacionRecargada.get().getNumero(),
					estado.toString()));
			cotizacionRecargada.get().setEstado(estado);
			cotizacionRepositorio.save(cotizacionRecargada.get());
			cotizacionHistorialEstadoServicio.registrar(cotizacionRecargada.get(), observacion);
		}
	}

	private void validarDescuentoAdicionalNoExedido(List<CotizacionDetalle> detalle) {

		Optional<ConfiguracionSistema> maximoVariacionPrecio = sistemaRepositorio
				.findByNombre(ConfigSistema.MAXIMO_PORCENTAJE_VARIACION_PRECIO);
		if (maximoVariacionPrecio.isPresent()) {

			if (detalle.stream()
					.anyMatch(x -> x.getDescuentoAdicional().doubleValue() > maximoVariacionPrecio.get().getValor())) {

				throw new CotizacionExcedeLimiteVariacionPrecioException(
						String.valueOf(maximoVariacionPrecio.get().getValor()));
			}

		} else {
			throw new ConfiguracionSistemaNoExisteException(
					ConfigSistema.MAXIMO_PORCENTAJE_VARIACION_PRECIO.getDescripcion());
		}
	}

	private String obtenerCodigoVendedor() {
		String usuario = UtilidadesSeguridad.usuarioEnSesion();

		Optional<UsuarioPerfil> usPer = usuarioPerfilRepositorio.findByUsuario_NombreUsuarioAndPerfil_Nombre(usuario,
				PerfilNombre.VENDEDOR);
		if (usPer.isPresent()) {
			return usPer.get().getCodigoVendedor();
		} else {
			throw new UsuarioNoTieneCodigoVendedorException(usuario);
		}
	}

	@Override
	@Transactional
	public Cotizacion agregarLineaDetalle(Cotizacion cotizacion, List<Item> articulo) {
		Optional<Cotizacion> cotizacionRecargada = cotizacionRepositorio.findById(cotizacion.getId());

		if (cotizacionRecargada.isPresent()) {

			articulo.forEach(x -> {
				CotizacionDetalle detalle = crearDetalle(x);
				cotizacionRecargada.get().agregarDetalle(detalle);
			});

			return cotizacionRecargada.get();
		} else {
			validarCliente(cotizacion);
			Secuencial secuencial = secuencialServicio.ObtenerSecuencialPorPuntoVentaYTipoDocumento(
					cotizacion.getPuntoVenta().getId(), TipoDocumento.COTIZACION);
			cotizacion.setNumero(secuencial.getNumeroSecuencial());
			cotizacion.setFechaEmision(LocalDateTime.now());
			cotizacion.setCodigoVendedor(obtenerCodigoVendedor());
			cotizacion.setFechaVencimiento(calcularFechaVigenciaCotizacion());

			cotizacion = cotizacionRepositorio.save(cotizacion);

			for (Item item : articulo) {
				CotizacionDetalle detalle = crearDetalle(item);
				cotizacion.agregarDetalle(detalle);
			}

			return cotizacion;
		}
	}

	private void validarCliente(Cotizacion cotizacion) {
		if (UtilidadesCadena.esNuloOBlanco(cotizacion.getCodigoCliente())) {
			throw new CotizacionClienteNoEncontradoException();
		}
	}

	private CotizacionDetalle crearDetalle(Item item) {
//		if (item.getUomprice().compareTo(BigDecimal.ZERO) == 0) {
//			throw new CotizacionDetalleArticuloPrecioCeroException(item.getItemnmbr(), item.getItemdesc());
//		}
		CotizacionDetalle detalle = new CotizacionDetalle();
		detalle.setCantidad(BigDecimal.ONE);
		detalle.setSaldo(BigDecimal.ONE);
		detalle.setCodigoArticulo(item.getItemnmbr());
		detalle.setCodigoArticuloAlterno(item.getItmshnam());
		detalle.setDescripcionArticulo(item.getItemdesc());
		detalle.setDescripcionArticuloGP(item.getItemdesc());
		detalle.setPesoArticulo(item.getItemshwt());
		detalle.setClaseArticulo(item.getUscatvls_2());
		detalle.setPrecio(item.getUomprice());
		detalle.setCostoUnitario(item.getCurrCost());
		detalle.setPrecioGP(item.getUomprice());
		detalle.setDescuentoAdicional(BigDecimal.ZERO);
		detalle.setDescuentoFijo(item.getUscatvls_6());
		detalle.setDescuentoFijoGP(item.getUscatvls_6());
		detalle.setUnidadMedida(item.getUomschdl());
		detalle.setUnidadMedidaCompra(item.getPrchsuom());
		detalle.setImpuestoDetalle(item.getTaxdtlid());
		detalle.setImpuestoValor(item.getTxdtlpct());
		detalle.setServicio(item.isServicio());
		detalle.calcularPrecioUnitario();
		detalle.calcularValorDescuento();
		detalle.calcularSubtotal();
		detalle.calcularTotal();

		return detalle;
	}

	@Override
	@Transactional
	public CotizacionDetalle modificarLineaDetalle(long CotizacionID, CotizacionDetalle detalle) {
		Optional<Cotizacion> cotizacionRecargada = cotizacionRepositorio.findById(CotizacionID);
		Optional<CotizacionDetalle> cotizacionDetalleRecargada = cotizacionDetalleRepositorio.findById(detalle.getId());
		if (cotizacionRecargada.isPresent() && cotizacionDetalleRecargada.isPresent()) {
			Cotizacion cotizacion = cotizacionRecargada.get();
			CotizacionDetalle cotizacionDetalle = cotizacionDetalleRecargada.get();
			cotizacionDetalle.setPrecio(detalle.getPrecio());
			cotizacionDetalle.setDescripcionArticulo(detalle.getDescripcionArticulo());
			cotizacionDetalle.setDescuentoFijo(detalle.getDescuentoFijo());
			cotizacionDetalle.setDescuentoAdicional(detalle.getDescuentoAdicional());
			cotizacionDetalle.setCantidad(detalle.getCantidad());
			cotizacionDetalle.setGeneraCompra(detalle.getGeneraCompra());
			cotizacionDetalle.calcularPrecioUnitario();
			cotizacionDetalle.calcularValorDescuento();
			cotizacionDetalle.calcularSubtotal();
			cotizacionDetalle.calcularTotal();

			cotizacion.calcularTotales();

			LOG.info(String.format("Cotizacion %s: Modificando linea detalle: %s ", cotizacion.getNumero(),
					gsonLog.toJson(cotizacionDetalle)));
			return cotizacionDetalle;
		} else {
			return null;
		}
	}

	@Override
	@Transactional
	public boolean eliminarLineaDetalle(long CotizacionID, long detalleID) {
		Optional<Cotizacion> cotizacionRecargada = cotizacionRepositorio.findById(CotizacionID);

		if (cotizacionRecargada.isPresent()) {
			LOG.info(String.format("Cotizacion %s: Eliminando linea detalle id: %s ",
					cotizacionRecargada.get().getNumero(), detalleID));
			cotizacionRecargada.get().buscarDetallePorId(detalleID).ifPresent(d -> comprasServicio
					.eliminarDetalleOrdenCompraPorDetalleCotizacion(CotizacionID, d.getCodigoArticulo()));

			cotizacionRecargada.get().eliminarLineaDetalle(detalleID);
			return true;
		}
		return false;
	}

	@Override
	public LocalDateTime calcularFechaVigenciaCotizacion() {
		LocalDateTime fechaVigencia = LocalDateTime.now();
		Optional<ConfiguracionSistema> configuracion = sistemaRepositorio
				.findByNombre(ConfigSistema.VIGENCIA_COTIZACION);
		if (configuracion.isPresent()) {

			switch (configuracion.get().getUnidadMedida()) {
			case DIAS:
				fechaVigencia = fechaVigencia.plusDays((long) configuracion.get().getValor());
				break;
			case HORAS:
				fechaVigencia = fechaVigencia.plusHours((long) configuracion.get().getValor());
				break;
			default:
				break;
			}

		}

		return fechaVigencia;
	}

	@Override
	public List<String> validarCotizacion(long cotizaiconID) {
		List<String> inconsistencias = new ArrayList<>();
		Optional<Cotizacion> cotizacionRecargada = cotizacionRepositorio.findById(cotizaiconID);
		if (cotizacionRecargada.isPresent()) {
			inconsistencias = validaciones.validarCotizacion(cotizacionRecargada.get());
			return inconsistencias;
		}
		return inconsistencias;
	}

	@Override
	@Transactional
	public void cambiarEstadoAFacturado(long cotizacionId) {

		Optional<Cotizacion> cotizacionRecargada = cotizacionRepositorio.findById(cotizacionId);

		if (cotizacionRecargada.isPresent()) {

			Cotizacion cotizacion = cotizacionRecargada.get();

			if (cotizacion.getEstado().equals(CotizacionEstado.POR_FACTURAR)) {

				List<DocumentoReserva> reservas = documentoReservaRepositorio
						.findByCotizacion_IdOrderByModificadoPorAsc(cotizacionId);

				if (cotizacion.getDetalle().stream().allMatch(x -> x.getSaldo().compareTo(BigDecimal.ZERO) == 0)
						&& reservas.stream().allMatch(x -> x.getEstado().equals(DocumentoEstado.COMPLETADO))) {
					
					liberarReservaComprasL(cotizacionId);

					cotizacion.setEstado(CotizacionEstado.FACTURADO);

					LOG.info(String.format("Se cambia Cotización No %s a estado %s", cotizacion.getNumero(),
							cotizacion.getEstado().toString()));

					cotizacionHistorialEstadoServicio.registrar(cotizacion, "");

				}
			}

		}
	}

	@Override
	@Transactional
	public void liberarReservaComprasL(long cotizacionId) {

		Optional<Cotizacion> cotizacionRecargada = cotizacionRepositorio.findById(cotizacionId);

		if (cotizacionRecargada.isPresent()) {

			Cotizacion cotizacion = cotizacionRecargada.get();

			Bodega bodegaPrincipal = puntoVentaBodegaServicio
					.buscarBodegaPrincipalPorPuntoVenta(cotizacion.getPuntoVenta());

			if (cotizacion.getEstado().equals(CotizacionEstado.EMITIDO)
					|| cotizacion.getEstado().equals(CotizacionEstado.POR_FACTURAR)) {

				List<CotizacionDetalle> detalleComprasL = cotizacion.getDetalle().stream()
						.filter(x -> x.getClaseArticulo().equals("L"))
						.filter(x -> x.getCantidadReserva() != null)
						.collect(Collectors.toList());

				detalleComprasL.forEach(x -> {
					reservaArticuloServicio.decrementarReserva(new ReservaArticulo(x.getCodigoArticulo(),
							bodegaPrincipal.getCodigo(), x.getCantidadReserva()));

				});
			}

		}

	}
	
	//0 0/5 * 1/1 * ?
	@Scheduled(cron = "0 0/5 * 1/1 * ?")
	public void vigenciaCotizacion() {
		LOG.info("Iniciando Tarea Programada: VIGENCIA COTIZACION");
		this.determinarCotizacionVencidas();
		LOG.info("Finalizando Tarea Programada: VIGENCIA COTIZACION");
	}
	
	private void determinarCotizacionVencidas() {
		List<Cotizacion> cotizaciones = cotizacionRepositorio
				.findByEstadoAndActivoTrueAndFechaVencimientoNotNull(CotizacionEstado.APROBADO).stream()
				.filter(x -> !ordenesCompraServicio.existeOrdenesCompraPorCotizacionId(x.getId()))
				.collect(Collectors.toList());
		LocalDateTime fechaActual = LocalDateTime.now();

		cotizaciones.forEach(cotizacion -> {
			if (fechaActual.isAfter(cotizacion.getFechaVencimiento())) {

				liberarReservaComprasL(cotizacion.getId());
				LOG.info(String.format("Documento %s cambiado a estado VENCIDO", cotizacion.getNumero()));
				cambiarEstadoCotizacion(cotizacion.getId(), CotizacionEstado.VENCIDO, "Tarea Programada");
			}
		});
	}

	@Override
	@Transactional
	public void editarOrdenCliente(long cotizacionId, String valor) {
		Optional<Cotizacion> cotizacion = cotizacionRepositorio.findById(cotizacionId);
		if (cotizacion.isPresent()) {
			cotizacion.get().setOrdenCliente(valor);
		}
	}

}
