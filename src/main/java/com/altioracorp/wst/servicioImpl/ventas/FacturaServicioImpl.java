package com.altioracorp.wst.servicioImpl.ventas;

import static com.altioracorp.wst.util.UtilidadesSeguridad.nombreUsuarioEnSesion;

import java.io.File;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
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

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.json.JSONObject;
import org.json.XML;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.core.io.ClassPathResource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.altioracorp.gpintegrator.client.FE.AltOffXmlDocumento;
import com.altioracorp.gpintegrator.client.Sales.DocumentBin;
import com.altioracorp.gpintegrator.client.Sales.DocumentHeader;
import com.altioracorp.gpintegrator.client.Sales.DocumentLine;
import com.altioracorp.gpintegrator.client.Sales.DocumentResponse;
import com.altioracorp.gpintegrator.client.Sales.DocumentUserDefined;
import com.altioracorp.gpintegrator.client.Sales.SopRequest;
import com.altioracorp.gpintegrator.client.Sales.SopResponse;
import com.altioracorp.wst.dominio.sistema.PerfilNombre;
import com.altioracorp.wst.dominio.sistema.Secuencial;
import com.altioracorp.wst.dominio.sistema.TipoDocumento;
import com.altioracorp.wst.dominio.ventas.Cotizacion;
import com.altioracorp.wst.dominio.ventas.CotizacionDetalle;
import com.altioracorp.wst.dominio.ventas.DocumentoDetalle;
import com.altioracorp.wst.dominio.ventas.DocumentoEstado;
import com.altioracorp.wst.dominio.ventas.DocumentoFactura;
import com.altioracorp.wst.dominio.ventas.DocumentoNotaCredito;
import com.altioracorp.wst.dominio.ventas.DocumentoReserva;
import com.altioracorp.wst.dominio.ventas.Entrega;
import com.altioracorp.wst.dominio.ventas.TipoDevolucion;
import com.altioracorp.wst.dominio.ventas.dto.FacturaConsultaDTO;
import com.altioracorp.wst.dominio.ventas.dto.FacturaCotizacionDTO;
import com.altioracorp.wst.dominio.ventas.dto.FacturaDTO;
import com.altioracorp.wst.repositorio.ventas.IDocumentoFacturaRepositorio;
import com.altioracorp.wst.repositorio.ventas.IDocumentoNotaCreditoRepositorio;
import com.altioracorp.wst.servicio.cobros.ICondicionCobroFacturaServicio;
import com.altioracorp.wst.servicio.notificaciones.INotificacionServicio;
import com.altioracorp.wst.servicio.ventas.IArticuloServicio;
import com.altioracorp.wst.servicio.ventas.ICotizacionServicio;
import com.altioracorp.wst.servicio.ventas.IDocumentoFEServicio;
import com.altioracorp.wst.servicio.ventas.IDocumentoServicio;
import com.altioracorp.wst.servicio.ventas.IFacturaServicio;
import com.altioracorp.wst.servicio.ventas.IGuiaRemisionServicio;
import com.altioracorp.wst.util.JsonUtilities;
import com.altioracorp.wst.util.UtilidadesCadena;
import com.altioracorp.wst.util.UtilidadesFecha;
import com.altioracorp.wst.util.UtilidadesSeguridad;
import com.altioracorp.wst.xml.factura.CampoAdicional;
import com.altioracorp.wst.xml.factura.Converter;
import com.altioracorp.wst.xml.factura.Detalle;
import com.altioracorp.wst.xml.factura.Factura;
import com.altioracorp.wst.xml.factura.Impuesto;
import com.altioracorp.wst.xml.factura.InfoFactura;
import com.altioracorp.wst.xml.factura.InfoTributaria;
import com.altioracorp.wst.xml.factura.Pago;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;

import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;


@Service
public class FacturaServicioImpl implements IFacturaServicio {

	private static final Log LOG = LogFactory.getLog(FacturaServicioImpl.class);

	@Autowired
	private IDocumentoFacturaRepositorio facturaRepositorio;

	@Autowired
	private IArticuloServicio articuloServicio;

	@Autowired
	private ICotizacionServicio cotizacionServicio;

	@Autowired
	private IDocumentoServicio documentoServicio;

	@Autowired
	private Environment environment;

	@Autowired
	private ICondicionCobroFacturaServicio condicionCobroFacturaServicio;

	@Autowired
	private EntityManager entityManager;
	
	@Autowired
	private Gson gsonLog;

	@Autowired
	private IGuiaRemisionServicio guiaRemisionServicio;
	
	@Autowired
	private INotificacionServicio notificacionServicio;
	
	@Autowired
	private IDocumentoNotaCreditoRepositorio notaCreditoRepositorio;
	
	@Autowired
	private IDocumentoFEServicio documentoFeServicio;
	
	

	private BigDecimal consultarTotalFactura(String numeroDocumento) {
		BigDecimal total = BigDecimal.ZERO;
		DocumentResponse documento = documentoServicio.consultarDocumento(numeroDocumento, TipoDocumento.FACTURA.getCodigo());
		if (documento != null) {
			total = documento.getDocumentHeader().getDocamnt();
		}
		return total;
	}

	@Override
	@Transactional
	//synchronized palabra reservada para controlar la concurrencia y garantizar que no se procese la misma solicitud (n) veces
	synchronized public DocumentoFactura generarFactura(DocumentoReserva reserva) {

		if (reserva.getDetalle().stream().count() > 0) {

			DocumentoFactura factura = new DocumentoFactura(reserva);

			factura.setReferencia(reserva.getNumero());

			Map<CotizacionDetalle, BigDecimal> detalleFactura = reserva.getDetalle().stream()
					.collect(Collectors.groupingBy(DocumentoDetalle::getCotizacionDetalle, Collectors.mapping(
							DocumentoDetalle::getCantidad, Collectors.reducing(BigDecimal.ZERO, BigDecimal::add))));

			detalleFactura.forEach((cotizacionDetalle, cantidad) -> {

				DocumentoDetalle detalle = new DocumentoDetalle(cotizacionDetalle, reserva.getBodegaPrincipal(),
						cantidad);

				factura.agregarLineaDetalle(detalle);

			});
			
			agregarCompartimientoAlDetalle(factura);
			
			facturaRepositorio.save(factura);

			procesarFactura(factura);

			return factura;
		}

		return null;
	}


	private void integrarFacturaGP(DocumentoFactura factura) {
		
		if (!factura.esFacturaIntegrada()) {
			
			if (validarCompartimiento(factura)) {
				
				obtenerNumeroFactura(factura);
				
				SopRequest sopRequest = obtenerEntidadIntegracionFactura(factura);
				LOG.info(String.format("Cotizacion %s: Factura a integrar %s", factura.getCotizacion().getNumero(),
						gsonLog.toJson(sopRequest)));
				
				SopResponse sopResponse = documentoServicio.integrarDocumento(sopRequest);
				LOG.info(String.format("Respuesta Integracion Factura: Cotizacion %s => {ErrorCode: %s, ErrorMessage: %s}",
						factura.getCotizacion().getNumero(), sopResponse.getErrorCode(), sopResponse.getErrorMessage()));

				if (sopResponse.getErrorCode() != null && sopResponse.getErrorCode().equals("0")) {
					factura.setEstado(DocumentoEstado.EMITIDO);
					factura.setFacturaIntegrada(true);
					factura.setTotal(consultarTotalFactura(factura.getNumero()));
					liberarReservasArticulosYCompartimientos(factura.getDetalle());
					cotizacionServicio.cambiarEstadoAFacturado(factura.getCotizacion().getId());

				} else {
					factura.setEstado(DocumentoEstado.ERROR);
					factura.setMensajeError(UtilidadesCadena.truncar(sopResponse.getErrorMessage(), 255));
					factura.setFacturaIntegrada(false);
				}
			} else {
				factura.setEstado(DocumentoEstado.ERROR);
				factura.setMensajeError("Error en compartimiento");
				factura.setFacturaIntegrada(false);
			}
		}
	}

	private void obtenerNumeroFactura(DocumentoFactura factura) {
		if (factura.getNumero() == null) {
			Secuencial secuencial = documentoServicio.secuencialDocumento(factura.getCotizacion().getPuntoVenta(),
					factura.getTipo());
			factura.setNumero(secuencial.getNumeroSecuencial());
			factura.setDocIdGP(secuencial.getDocIdGP());
		}
	}

	private boolean validarCompartimiento(DocumentoFactura factura) {
		for (DocumentoDetalle detalle : factura.getDetalle()) {
			if(!detalle.getCotizacionDetalle().getServicio()) {
				if (detalle.getCantidad().compareTo(detalle.getCompartimientos().stream().map(x -> x.getCantidad()).reduce(BigDecimal.ZERO, BigDecimal::add)) != 0) {
					return false;
				}
			}			
		}
		return true;
	}

	@Override
	public void liberarReservasArticulosYCompartimientos(List<DocumentoDetalle> detalles) {

		articuloServicio.liberarReservasArticulosYCompartimientos(detalles);

	}

	@Override
	public DocumentoFactura listarPorCotizacionID(Long cotizacionID) {
		return facturaRepositorio.findByEstadoAndCotizacion_Id(DocumentoEstado.NUEVO, cotizacionID)
				.orElse(facturaRepositorio.findByEstadoAndCotizacion_Id(DocumentoEstado.REVISION, cotizacionID)
						.orElse(facturaRepositorio.findByEstadoAndCotizacion_Id(DocumentoEstado.ERROR, cotizacionID)
								.orElse(null)));
	}

	@Override
	public List<DocumentoFactura> listarPorEstadoError() {
		return facturaRepositorio.findByEstadoIn(Arrays.asList(DocumentoEstado.ERROR));
	}

	@Override
	public List<FacturaDTO> listarTodasPorCotizacionID(Long cotizacionID) {
		List<FacturaDTO> dto = new ArrayList<>();

		List<DocumentoFactura> facturas = facturaRepositorio.findByCotizacion_IdOrderByModificadoPorAsc(cotizacionID);

		facturas.forEach(x -> {
			
			if(x.getCodigoCliente().equals(x.getCotizacion().getCodigoCliente())) {
				dto.add(new FacturaDTO(x.getId(), x.getNumero(), x.isDespachada(), x.getEstado(), ""));
			}
			
		});

		return dto;
	}

	private SopRequest obtenerEntidadIntegracionFactura(DocumentoFactura factura) {

		String docDate = UtilidadesFecha.formatear(LocalDateTime.now(), "yyyy-MM-dd");

		DocumentHeader documentHeader = new DocumentHeader();
		documentHeader.setSoptype(factura.getTipo().getCodigo());
		documentHeader.setSopnumbe(factura.getNumero());
		documentHeader.setDocid(factura.getDocIdGP());
		documentHeader.setCstponbr(factura.getCotizacion().getNumero());
		documentHeader.setLocncode(factura.getBodegaPrincipal());
		documentHeader.setDocdate(docDate);
		documentHeader.setCustnmbr(factura.getCodigoCliente());
		documentHeader.setBachnumb("FAC".concat(Long.toString(factura.getId())));
		documentHeader.setPrbtadcd(factura.getCodigoDireccion());
		documentHeader.setPymtrmid(factura.getCotizacion().getCondicionPago());
		documentHeader.setSlprsnid(factura.getCotizacion().getCodigoVendedor());
		if (factura.getEntrega().equals(Entrega.CLIENTE))
			documentHeader.setPrstadcd(factura.getCodigoDireccion());
		else {
			documentHeader.setPrstadcd(factura.getDireccionEntrega());
		}
		documentHeader.setCmmttext(factura.getCotizacion().getComentario());
		documentHeader.setPrclevel(environment.getRequiredProperty("integrador.listaPrecio"));
		documentHeader.setUsingheaderleveltaxes(0);
		documentHeader.setCreatedist(1);
		documentHeader.setCreatetaxes(1);
		documentHeader.setDefpricing(1);
		documentHeader.setLocation(true);

		DocumentUserDefined documentUserDefined = new DocumentUserDefined();
		documentUserDefined.setSoptype(factura.getTipo().getCodigo());
		documentUserDefined.setSopnumbe(factura.getNumero());
		documentUserDefined.setUserdef1(factura.getCotizacion().getOrdenCliente());
		documentUserDefined.setUserdef2(factura.getCotizacion().getTotalKilos().toString());
		documentUserDefined.setUsrdef03(factura.getEntrega().getDescripcion());

		List<DocumentLine> documentLines = new ArrayList<DocumentLine>();
		List<DocumentBin> documentBins = new ArrayList<DocumentBin>();

		int i = 1;

		for (DocumentoDetalle detalle : factura.getDetalle()) {

			int secuencia = i * 16384;

			DocumentLine documentLine = new DocumentLine();
			documentLine.setSoptype(factura.getTipo().getCodigo());
			documentLine.setSopnumbe(factura.getNumero());
			documentLine.setDocid(factura.getDocIdGP());
			documentLine.setCustnmbr(factura.getCotizacion().getCodigoCliente());
			documentLine.setDocdate(docDate);
			documentLine.setLnitmseq(secuencia);
			documentLine.setItemnmbr(detalle.getCotizacionDetalle().getCodigoArticulo());
			documentLine.setItemdesc(detalle.getCotizacionDetalle().getDescripcionArticulo());
			documentLine.setLocncode(detalle.getCodigoBodega());
			documentLine.setUnitprce(detalle.getCotizacionDetalle().getPrecioUnitario());
			documentLine.setQuantity(detalle.getCantidad());
			documentLine.setMrkdnpct((detalle.getCotizacionDetalle().getDescuentoFijo()));
			documentLine.setUofm(detalle.getCotizacionDetalle().getUnidadMedida());
			documentLine.setPrclevel(environment.getRequiredProperty("integrador.listaPrecio"));
			documentLine.setDefpricing(0);
			documentLine.setAutoallocateserial(0);
			documentLine.setAutoallocatelot(0);
			documentLine.setAutoAssignBin(0);
			documentLines.add(documentLine);

			detalle.getCompartimientos().forEach(x -> {
				DocumentBin documentBin = new DocumentBin();
				documentBin.setSoptype((short) factura.getTipo().getCodigo());
				documentBin.setSopnumbe(factura.getNumero());
				documentBin.setItemnmbr(detalle.getCotizacionDetalle().getCodigoArticulo());
				documentBin.setLnitmseq(secuencia);
				documentBin.setBin(x.getCompartimiento());
				documentBin.setQuantity(x.getCantidad());
				documentBin.setUofm(detalle.getCotizacionDetalle().getUnidadMedida());
				documentBins.add(documentBin);
			});

			i++;
		}

		SopRequest sopRequest = new SopRequest();
		sopRequest.setDocumentHeader(documentHeader);
		sopRequest.setDocumentLines(documentLines);
		sopRequest.setDocumentBins(documentBins);
		sopRequest.setDocumentUserDefined(documentUserDefined);

		return sopRequest;
	}

	@Override
	@Transactional( readOnly = false, propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
	//synchronized palabra reservada para controlar la concurrencia y garantizar que no se procese la misma solicitud (n) veces
	synchronized public FacturaDTO reintegrarFactura(long id) {

		Optional<DocumentoFactura> documentoFacturaOP = facturaRepositorio.findById(id);

		if (documentoFacturaOP.isPresent()) {

			DocumentoFactura factura = documentoFacturaOP.get();

			if (factura.getEstado().equals(DocumentoEstado.ERROR)) {
				
				agregarCompartimientoAlDetalle(factura);

				procesarFactura(factura);

				return new FacturaDTO(factura.getId(), factura.getNumero(), factura.isDespachada(), factura.getEstado(),
						factura.getMensajeError());

			}
		}

		return new FacturaDTO(id, "", false, DocumentoEstado.ERROR, "No existe factura para reintegrar");
	}

	private void agregarCompartimientoAlDetalle(DocumentoFactura factura) {
		factura.getDetalle().forEach(x -> {
			if (!x.getCotizacionDetalle().getServicio())
				articuloServicio.agregarCompartimientoADetalle(x);
		});
	}


	private void procesarFactura(DocumentoFactura factura) {
		
		integrarFacturaGP(factura);

		facturaRepositorio.save(factura);
		
		if(generarDesgloseCuotas(factura))
			condicionCobroFacturaServicio.registrarDesgloseCobrosFactura(factura);
		
		//Si es refacturación se debe enviar correo
		if(factura.isRefacturacion()) {
			try {
				notificacionServicio.notificarFacturaGeneradaRefacturacion(factura);
			}catch (Exception e) {
				LOG.error(e.getMessage());
			}
		}
			
	}


	public List<FacturaCotizacionDTO> obtenerFacturasConsulta(final FacturaConsultaDTO consulta) {
		try {
			final CriteriaBuilder criteriaBuilder = this.entityManager.getCriteriaBuilder();
			final CriteriaQuery<DocumentoFactura> query = criteriaBuilder.createQuery(DocumentoFactura.class);

			final Root<DocumentoFactura> root = query.from(DocumentoFactura.class);
			final List<Predicate> predicadosConsulta = new ArrayList<>();

			if (consulta.getRol().getDescripcion().equals(PerfilNombre.VENDEDOR.getDescripcion())) {
				predicadosConsulta
						.add(criteriaBuilder.equal(root.get("cotizacion").get("creadoPor"), UtilidadesSeguridad.usuarioEnSesion()));
			} else {
				if (!StringUtils.isBlank(consulta.getVendedor())) {
					predicadosConsulta.add(criteriaBuilder.equal(root.get("cotizacion").get("creadoPor"), consulta.getVendedor()));
				}
			}

			if (!StringUtils.isBlank(consulta.getNumeroFactura())) {
				predicadosConsulta.add(criteriaBuilder.equal(root.get("numero"), consulta.getNumeroFactura()));
			}

			if (consulta.getEstado() != null) {
				predicadosConsulta.add(criteriaBuilder.equal(root.get("estado"), consulta.getEstado()));
			}

			if (consulta.getFechaInicio() != null && consulta.getFechaFin() != null) {
				predicadosConsulta.add(criteriaBuilder.between(root.get("fechaEmision"),
						consulta.getFechaInicio().withHour(0).withMinute(0).withSecond(0),
						consulta.getFechaFin().withHour(23).withMinute(59).withSecond(59)));
			}

			if (consulta.getFechaInicio() != null && consulta.getFechaFin() == null) {
				predicadosConsulta.add(criteriaBuilder.between(root.get("fechaEmision"),
						consulta.getFechaInicio().withHour(0).withMinute(0).withSecond(0),
						consulta.getFechaInicio().withHour(23).withMinute(59).withSecond(59)));
			}

			if (consulta.getIdPuntoVenta() != null) {
				predicadosConsulta.add(criteriaBuilder.equal(root.get("cotizacion").get("puntoVenta").get("id"),
						consulta.getIdPuntoVenta()));
			}

			if (!StringUtils.isBlank(consulta.getIdentificacionCliente())) {
				predicadosConsulta.add(criteriaBuilder.equal(root.get("codigoCliente"),
						consulta.getIdentificacionCliente()));
			}

			if (!StringUtils.isBlank(consulta.getNombreCliente())) {
				predicadosConsulta.add(criteriaBuilder.like(root.get("nombreCliente"), "%" + consulta.getNombreCliente() + "%"));
			}

			query.where(predicadosConsulta.toArray(new Predicate[predicadosConsulta.size()]))
					.orderBy(criteriaBuilder.desc(root.get("fechaEmision")));

			final TypedQuery<DocumentoFactura> statement = this.entityManager.createQuery(query);

			final List<DocumentoFactura> facturasResult = statement.getResultList();

			return facturasResult.stream().map(f -> {
				f.setTieneGuiaRemision(this.guiaRemisionServicio.existeGuiasRemisionPorFacturaId(f.getId()));
				return new FacturaCotizacionDTO(f);
			}).collect(Collectors.toList());
		} catch (final Exception ex) {
			LOG.error(String.format("Error al consultar facturas %s", ex.getMessage()));
			return new ArrayList<>();
		}
	}

	public List<FacturaCotizacionDTO> obtenerCotizacionesConsulta(final FacturaConsultaDTO consulta) {
		try {
			final CriteriaBuilder criteriaBuilder = this.entityManager.getCriteriaBuilder();
			final CriteriaQuery<Cotizacion> query = criteriaBuilder.createQuery(Cotizacion.class);

			final Root<Cotizacion> root = query.from(Cotizacion.class);
			final List<Predicate> predicadosConsulta = new ArrayList<>();

			if (consulta.getRol().getDescripcion().equals(PerfilNombre.VENDEDOR.getDescripcion())) {
				predicadosConsulta
						.add(criteriaBuilder.equal(root.get("creadoPor"), UtilidadesSeguridad.usuarioEnSesion()));
			} else {
				if (!StringUtils.isBlank(consulta.getVendedor())) {
					predicadosConsulta.add(criteriaBuilder.equal(root.get("creadoPor"), consulta.getVendedor()));
				}
			}

			if (!StringUtils.isBlank(consulta.getNumeroFactura())) {
				predicadosConsulta.add(criteriaBuilder.equal(root.get("numero"), consulta.getNumeroFactura()));
			}

			if (consulta.getEstado() != null) {
				predicadosConsulta.add(criteriaBuilder.equal(root.get("estado"), consulta.getEstado()));
			}

			if (consulta.getFechaInicio() != null && consulta.getFechaFin() != null) {
				predicadosConsulta.add(criteriaBuilder.between(root.get("fechaEmision"),
						consulta.getFechaInicio().withHour(0).withMinute(0).withSecond(0),
						consulta.getFechaFin().withHour(23).withMinute(59).withSecond(59)));
			}

			if (consulta.getFechaInicio() != null && consulta.getFechaFin() == null) {
				predicadosConsulta.add(criteriaBuilder.between(root.get("fechaEmision"),
						consulta.getFechaInicio().withHour(0).withMinute(0).withSecond(0),
						consulta.getFechaInicio().withHour(23).withMinute(59).withSecond(59)));
			}

			if (consulta.getIdPuntoVenta() != null) {
				predicadosConsulta
						.add(criteriaBuilder.equal(root.get("puntoVenta").get("id"), consulta.getIdPuntoVenta()));
			}

			if (!StringUtils.isBlank(consulta.getIdentificacionCliente())) {
				predicadosConsulta
						.add(criteriaBuilder.equal(root.get("codigoCliente"), consulta.getIdentificacionCliente()));
			}

			if (!StringUtils.isBlank(consulta.getNombreCliente())) {
				predicadosConsulta.add(criteriaBuilder.like(root.get("nombreCliente"), "%" + consulta.getNombreCliente() + "%"));
			}

			query.where(predicadosConsulta.toArray(new Predicate[predicadosConsulta.size()]))
					.orderBy(criteriaBuilder.desc(root.get("fechaEmision")));

			final TypedQuery<Cotizacion> statement = this.entityManager.createQuery(query);

			final List<Cotizacion> cotizacionesResult = statement.getResultList();

			return cotizacionesResult.stream().map(c -> {
				List<DocumentoFactura> facturas = this.facturaRepositorio.findByCotizacionAndTipo(c,
						TipoDocumento.FACTURA);
				return new FacturaCotizacionDTO(c, facturas);
			}).collect(Collectors.toList());
		} catch (final Exception ex) {
			LOG.error(String.format("Error al consultar cotizaciones %s", ex.getMessage()));
			return new ArrayList<>();
		}
	}

	@Override
	public Page<FacturaCotizacionDTO> consultarFacturasCotizaciones(final Pageable pageable,
																	final FacturaConsultaDTO consulta) {
		try {

			List<FacturaCotizacionDTO> respuesta = new ArrayList<>();

			if (StringUtils.isBlank(consulta.getTipo())) {

				final List<FacturaCotizacionDTO> cotizaciones = this.obtenerCotizacionesConsulta(consulta);
				final List<FacturaCotizacionDTO> facturas = this.obtenerFacturasConsulta(consulta);
				final List<FacturaCotizacionDTO> notasCredito = this.obtenerNotasCreditoConsulta(consulta);

				if (!cotizaciones.isEmpty()) {
					respuesta.addAll(cotizaciones);
				}

				if (!facturas.isEmpty()) {
					respuesta.addAll(facturas);
				}

				if (!notasCredito.isEmpty()) {
					respuesta.addAll(notasCredito);
				}
			} else {

				if (consulta.getTipo().equals(TipoDocumento.FACTURA.getDescripcion())) {
					respuesta.addAll(this.obtenerFacturasConsulta(consulta));
				}

				if (consulta.getTipo().equals(TipoDocumento.COTIZACION.getDescripcion())) {
					respuesta.addAll(this.obtenerCotizacionesConsulta(consulta));
				}

				if (consulta.getTipo().equalsIgnoreCase(TipoDocumento.NOTA_CREDITO.getDescripcion())) {
					respuesta.addAll(this.obtenerNotasCreditoConsulta(consulta));
				}
			}

			final int sizeTotal = respuesta.size();

			final int start = (int) pageable.getOffset();
			final int end = (start + pageable.getPageSize()) > respuesta.size() ? respuesta.size()
					: (start + pageable.getPageSize());

			respuesta = respuesta.subList(start, end);

			final Page<FacturaCotizacionDTO> pageResut = new PageImpl<>(respuesta, pageable, sizeTotal);

			return pageResut;

		} catch (final Exception ex) {
			final Page<FacturaCotizacionDTO> pageResult = new PageImpl<>(new ArrayList<>(), pageable, 0);
			return pageResult;
		}
	}

	@Override
	public List<DocumentoFactura> listarEmitidasPorUsuarioYPuntoVenta(long puntoVentaID) {
		String usuarioSesion = nombreUsuarioEnSesion();
		List<DocumentoFactura> facturas = this.facturaRepositorio
				.findByEstadoAndCotizacion_PuntoVenta_IdAndCotizacion_CreadoPorOrderByFechaEmisionDesc(
						DocumentoEstado.EMITIDO, puntoVentaID, usuarioSesion)
				.stream().filter(x -> !x.isRefacturacion()).collect(Collectors.toList());
		
		facturas.parallelStream().forEach(x -> {
			x.getCotizacion().setDetalle(null);
			x.setTieneGuiaRemision(this.guiaRemisionServicio.existeGuiasRemisionPorFacturaId(x.getId()));
		});
		return facturas;
	}

	@Override
	@Transactional(readOnly = true)
	public DocumentoFactura obtenerFacturaPorIdParaDevoluciones(long facturaID) {
		Optional<DocumentoFactura> factura = this.facturaRepositorio.findById(facturaID);
		if (factura.isEmpty())
			return null;

		factura.get()
				.setDetalle(factura.get().getDetalle().stream()
						.filter(x -> x.getCodigoBodega().equals(factura.get().getBodegaPrincipal()))
						.collect(Collectors.toList()));

		return factura.get();
	}

	public List<FacturaCotizacionDTO> obtenerNotasCreditoConsulta(final FacturaConsultaDTO consulta) {
		try {
			final CriteriaBuilder criteriaBuilder = this.entityManager.getCriteriaBuilder();
			final CriteriaQuery<DocumentoNotaCredito> query = criteriaBuilder.createQuery(DocumentoNotaCredito.class);

			final Root<DocumentoNotaCredito> root = query.from(DocumentoNotaCredito.class);
			final List<Predicate> predicadosConsulta = new ArrayList<>();

			if (consulta.getRol().getDescripcion().equals(PerfilNombre.VENDEDOR.getDescripcion())) {
				predicadosConsulta.add(criteriaBuilder.equal(root.get("cotizacion").get("creadoPor"), UtilidadesSeguridad.usuarioEnSesion()));
			} else {
				if (!StringUtils.isBlank(consulta.getVendedor())) {
					predicadosConsulta.add(criteriaBuilder.equal(root.get("cotizacion").get("creadoPor"), consulta.getVendedor()));
				}
			}

			if (!StringUtils.isBlank(consulta.getNumeroFactura())) {
				predicadosConsulta.add(criteriaBuilder.equal(root.get("numero"), consulta.getNumeroFactura()));
			}

			if (consulta.getEstado() != null) {
				predicadosConsulta.add(criteriaBuilder.equal(root.get("estado"), consulta.getEstado()));
			}

			if (consulta.getFechaInicio() != null && consulta.getFechaFin() != null) {
				predicadosConsulta.add(criteriaBuilder.between(root.get("fechaEmision"),
						consulta.getFechaInicio().withHour(0).withMinute(0).withSecond(0),
						consulta.getFechaFin().withHour(23).withMinute(59).withSecond(59)));
			}

			if (consulta.getFechaInicio() != null && consulta.getFechaFin() == null) {
				predicadosConsulta.add(criteriaBuilder.between(root.get("fechaEmision"),
						consulta.getFechaInicio().withHour(0).withMinute(0).withSecond(0),
						consulta.getFechaInicio().withHour(23).withMinute(59).withSecond(59)));
			}

			if (consulta.getIdPuntoVenta() != null) {
				predicadosConsulta.add(criteriaBuilder.equal(root.get("cotizacion").get("puntoVenta").get("id"), consulta.getIdPuntoVenta()));
			}

			if (!StringUtils.isBlank(consulta.getIdentificacionCliente())) {
				predicadosConsulta.add(criteriaBuilder.equal(root.get("cotizacion").get("codigoCliente"), consulta.getIdentificacionCliente()));
			}

			if (!StringUtils.isBlank(consulta.getNombreCliente())) {
				predicadosConsulta.add(criteriaBuilder.like(root.get("cotizacion").get("nombreCliente"), "%" + consulta.getNombreCliente() + "%"));
			}

			query.where(predicadosConsulta.toArray(new Predicate[predicadosConsulta.size()]))
					.orderBy(criteriaBuilder.desc(root.get("fechaEmision")));

			final TypedQuery<DocumentoNotaCredito> statement = this.entityManager.createQuery(query);

			final List<DocumentoNotaCredito> resultList = statement.getResultList();

			return resultList.stream().map(n -> {
				List<DocumentoFactura> facturas = this.facturaRepositorio.findByCotizacionAndTipo(n.getCotizacion(), TipoDocumento.FACTURA);
				return new FacturaCotizacionDTO(n, facturas);
			}).collect(Collectors.toList());
		} catch (final Exception ex) {
			LOG.error(String.format("Error al consultar facturas %s", ex.getMessage()));
			return new ArrayList<>();
		}
	}
	
	@Override
	@Transactional
	//synchronized palabra reservada para controlar la concurrencia y garantizar que no se procese la misma solicitud (n) veces
	synchronized public DocumentoFactura generarRefacturacion(DocumentoNotaCredito notaCredito) {
		
		if (notaCredito.getTipoDevolucion().equals(TipoDevolucion.REFACTURACION) && notaCredito.getEstado().equals(DocumentoEstado.EMITIDO)) {
			
			if (notaCredito.getDetalle().stream().count() > 0) {
				
				DocumentoFactura facturaAntigua = facturaRepositorio.findById(notaCredito.getDocumentoFacturaId()).get();

				DocumentoFactura factura = new DocumentoFactura(notaCredito, facturaAntigua.getPeriodoGracia());

				factura.setReferencia(notaCredito.getNumero());

				notaCredito.getDetalle().stream().forEach(x -> {

					DocumentoDetalle detalle = new DocumentoDetalle(x.getCotizacionDetalle(), x.getCodigoBodega(),
							x.getCantidad());

					factura.agregarLineaDetalle(detalle);

				});
				
				articuloServicio.reservarArticulos(factura.getDetalle());

				factura.getDetalle().forEach(x -> {
					articuloServicio.agregarCompartimientoADetalle(x);
				});
				
				facturaRepositorio.save(factura);

				procesarFactura(factura);

				return factura;
			}
		}
		
		return null;
	}
	
	private boolean generarDesgloseCuotas(DocumentoFactura factura) {
		
		boolean generarCuotas = false;
		
		if (factura.getEstado().equals(DocumentoEstado.EMITIDO))
			generarCuotas = true;
		
		if (factura.isRefacturacion()) {
			
			DocumentoNotaCredito notaCredito = notaCreditoRepositorio.findById(factura.getDocumentoReservaId()).get();
			
			if (notaCredito.getMotivoNotaCredito().isCambioRazonSocial()) 
				generarCuotas = false;

		}
		
		return generarCuotas;
	}

	@Override
	public byte[] generarReporte(String numeroFactura) {
		
		AltOffXmlDocumento dtoXml = documentoFeServicio.consultarDocumentoFE(numeroFactura);

		if (dtoXml != null) {

			if (dtoXml.getXml() != null) {
				
				try {
					Factura factura = xmlToFactura(dtoXml.getXml(), numeroFactura);
					factura.setNumeroAutorizacion(dtoXml.getNumeroAutorizacion_SRI());

					try {
						File file = new ClassPathResource("/reporte/factura.jasper").getFile();
						JasperPrint print = JasperFillManager.fillReport(file.getPath(), null,
								new JRBeanCollectionDataSource(Collections.singleton(factura)));

						return JasperExportManager.exportReportToPdf(print);

					} catch (Exception e) {
						e.printStackTrace();
					}

				} catch (JsonProcessingException e) {
					e.printStackTrace();
				}
			}

		}

		return null;
	}
	
	private Factura xmlToFactura(final String xml, final String numeroFactura)  throws JsonProcessingException{
		if (UtilidadesCadena.esNuloOBlanco(xml)) {
            return null;
        }

        final ObjectMapper objectMapper = new ObjectMapper();
        final JSONObject xmlJSONObj = XML.toJSONObject(xml);

        final Factura factura = new Factura();
        final JSONObject facturaJson = JsonUtilities.getObjectFromJson(xmlJSONObj, "factura");

        final JSONObject infoTributariaJson = JsonUtilities.getObjectFromJson(facturaJson, "infoTributaria");
        final InfoTributaria infoTributaria = objectMapper.readValue(infoTributariaJson.toString(), InfoTributaria.class);

        final JSONObject infoFacturaJson = (JSONObject) facturaJson.get("infoFactura");
        final InfoFactura infoFactura = new InfoFactura();
        infoFactura.setDirEstablecimiento(JsonUtilities.getStringFromJson(infoFacturaJson, "dirEstablecimiento"));
        infoFactura.setDireccionComprador(JsonUtilities.getStringFromJson(infoFacturaJson, "direccionComprador"));
        infoFactura.setFechaEmision(JsonUtilities.getStringFromJson(infoFacturaJson, "fechaEmision"));
        infoFactura.setGuiaRemision(JsonUtilities.getStringFromJson(infoFacturaJson, "guiaRemision"));
        infoFactura.setIdentificacionComprador(JsonUtilities.getStringFromJson(infoFacturaJson, "identificacionComprador"));
        infoFactura.setImporteTotal(JsonUtilities.getBigDecimalFromJson(infoFacturaJson, "importeTotal"));
        infoFactura.setMoneda(JsonUtilities.getStringFromJson(infoFacturaJson, "moneda"));
        infoFactura.setPropina(JsonUtilities.getBigDecimalFromJson(infoFacturaJson, "propina"));
        infoFactura.setRazonSocialComprador(JsonUtilities.getStringFromJson(infoFacturaJson, "razonSocialComprador"));
        infoFactura.setTipoIdentificacionComprador(JsonUtilities.getStringFromJson(infoFacturaJson, "tipoIdentificacionComprador"));
        infoFactura.setTotalDescuento(JsonUtilities.getBigDecimalFromJson(infoFacturaJson, "totalDescuento"));
        infoFactura.setTotalSinImpuestos(JsonUtilities.getBigDecimalFromJson(infoFacturaJson, "totalSinImpuestos"));
        infoFactura.setValorRetIva(JsonUtilities.getBigDecimalFromJson(infoFacturaJson, "valorRetIva"));
        infoFactura.setValorRetRenta(JsonUtilities.getBigDecimalFromJson(infoFacturaJson, "valorRetRenta"));
        infoFactura.setObligadoContabilidadString(JsonUtilities.getStringFromJson(infoFacturaJson, "obligadoContabilidad"));
        infoFactura.setContribuyenteEspecial(JsonUtilities.getStringFromJson(infoFacturaJson, "contribuyenteEspecial"));

        final JSONObject pagosJson = JsonUtilities.getObjectFromJson(infoFacturaJson, "pagos");
        final List<Pago> pagos = Converter.getPagosFromJson(pagosJson);
        infoFactura.setPagos(pagos);

        final JSONObject totalConImpuestosJson = JsonUtilities.getObjectFromJson(infoFacturaJson, "totalConImpuestos");
        final List<Impuesto> totalConImpuestos = Converter.getImpuestosFromJson(totalConImpuestosJson, "totalImpuesto");
        infoFactura.setTotalConImpuestos(totalConImpuestos);

        final JSONObject detallesJson = JsonUtilities.getObjectFromJson(facturaJson, "detalles");
        final List<Detalle> detalles = Converter.getDetallesFromJson(detallesJson);

        final JSONObject infoAdicionalJson = JsonUtilities.getObjectFromJson(facturaJson, "infoAdicional");
        final List<CampoAdicional> infoAdicional = Converter.getInfoAdicionalFromJson(infoAdicionalJson);
        
        factura.setInfoTributaria(infoTributaria);
        factura.setInfoFactura(infoFactura);
        factura.setDetalles(detalles);
        factura.setInfoAdicional(infoAdicional);
        Optional<DocumentoFactura> facturaOptional= facturaRepositorio.findByNumero(numeroFactura);
        if(facturaOptional.isPresent()) {
        	factura.setObservacion(facturaOptional.get().getCotizacion().getComentario());
        }

        return factura;
	}
	
	@Scheduled(cron = "0 0/15 * 1/1 * ?")
	@Transactional
	public void reintegrarFacturasErrorRefacturacion() {
		final List<Long> facturasError = listarFaturasRefacturacionEstadoError();
		LOG.info(String.format("Iniciando Tarea Programada: REINTEGRACION FACTURAS CON ERROR TIPO REFACTURACION para %s documentos", facturasError.size()));
		facturasError.forEach(x -> reintegrarFactura(x));
		LOG.info("Finalizando Tarea Programada: REINTEGRACION FACTURAS CON ERROR TIPO REFACTURACION");
	}
	
	private List<Long> listarFaturasRefacturacionEstadoError(){
		return facturaRepositorio.findByEstadoInAndRefacturacionTrue(Arrays.asList(DocumentoEstado.ERROR)).stream().map(x -> x.getId()).collect(Collectors.toList());
	}
}
