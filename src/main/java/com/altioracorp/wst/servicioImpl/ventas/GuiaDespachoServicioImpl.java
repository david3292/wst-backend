package com.altioracorp.wst.servicioImpl.ventas;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
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

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.SerializationUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.altioracorp.gpintegrator.client.GuiaRemision.AltGuiaIv10000;
import com.altioracorp.gpintegrator.client.GuiaRemision.AltGuiaIv10000Detalle;
import com.altioracorp.gpintegrator.client.GuiaRemision.AltGuiaTransaction;
import com.altioracorp.gpintegrator.client.SiteSetup.SiteSetup;
import com.altioracorp.gpintegrator.client.inventory.IvResponse;
import com.altioracorp.wst.controlador.MensajesControlador;
import com.altioracorp.wst.dominio.logistica.dto.GuiaDespachoConsulta;
import com.altioracorp.wst.dominio.logistica.dto.GuiaDespachoDTO;
import com.altioracorp.wst.dominio.sistema.Perfil;
import com.altioracorp.wst.dominio.sistema.TipoDocumento;
import com.altioracorp.wst.dominio.ventas.DocumentoBase;
import com.altioracorp.wst.dominio.ventas.DocumentoDetalle;
import com.altioracorp.wst.dominio.ventas.DocumentoDetalleCompartimiento;
import com.altioracorp.wst.dominio.ventas.DocumentoEstado;
import com.altioracorp.wst.dominio.ventas.DocumentoFactura;
import com.altioracorp.wst.dominio.ventas.DocumentoGuiaDespacho;
import com.altioracorp.wst.dominio.ventas.DocumentoGuiaRemision;
import com.altioracorp.wst.dominio.ventas.Entrega;
import com.altioracorp.wst.dominio.ventas.dto.GuiaDespachoDetalleDTO;
import com.altioracorp.wst.dominio.ventas.dto.GuiaDespachoReporteDTO;
import com.altioracorp.wst.exception.logistica.GuiaRemisionException;
import com.altioracorp.wst.exception.reporte.JasperReportsException;
import com.altioracorp.wst.exception.reporte.ReporteExeption;
import com.altioracorp.wst.exception.ventas.CotizacionNoTieneGuiasDespachoException;
import com.altioracorp.wst.exception.ventas.GuiaDespachoNoExisteException;
import com.altioracorp.wst.repositorio.sistema.IBodegaRepositorio;
import com.altioracorp.wst.repositorio.ventas.IDocumentoFacturaRepositorio;
import com.altioracorp.wst.repositorio.ventas.IDocumentoGuiaDespachoRepositorio;
import com.altioracorp.wst.servicio.reporte.IGeneradorJasperReports;
import com.altioracorp.wst.servicio.sistema.IBodegaServicio;
import com.altioracorp.wst.servicio.sistema.IPerfilServicio;
import com.altioracorp.wst.servicio.ventas.IDocumentoServicio;
import com.altioracorp.wst.servicio.ventas.IGuiaDespachoServicio;
import com.altioracorp.wst.servicio.ventas.IGuiaRemisionServicio;
import com.altioracorp.wst.util.UtilidadesCadena;
import com.altioracorp.wst.util.UtilidadesFecha;
import com.altioracorp.wst.xml.guiaRemision.GuiaRemision;
import com.google.gson.Gson;

@Service
public class GuiaDespachoServicioImpl implements IGuiaDespachoServicio {

	private static final Log LOG = LogFactory.getLog(GuiaDespachoServicioImpl.class);

	@Autowired
	private IDocumentoGuiaDespachoRepositorio guiaDespachoRepositorio;

	@Autowired
	private IDocumentoFacturaRepositorio facturaRepositorio;

	@Autowired
	private IDocumentoServicio documentoServicio;

	@Autowired
	private IGeneradorJasperReports reporteServicio;

	@Autowired
	private IBodegaRepositorio bodegaRepositorio;

	@Autowired
	private IGuiaRemisionServicio guiaRemisionServicio;

	@Autowired
	private IBodegaServicio bodegaservicio;

	@Autowired
	private IPerfilServicio perfilServicio;
	
	@Autowired
	private Gson gsonLog;

	@Autowired
	private EntityManager entityManager;

	private GuiaDespachoReporteDTO crearReporteDTO(DocumentoFactura factura, DocumentoGuiaDespacho despacho) {
		GuiaDespachoReporteDTO reporteDTO = new GuiaDespachoReporteDTO(factura.getCotizacion().getCodigoCliente(),
				factura.getCotizacion().getDescripcionDireccion(), despacho.getDireccionEntregaDescripcion(),
				despacho.getFechaEmision(), despacho.getCotizacion().getNombreCliente(), despacho.getNumero(),
				factura.getNumero(), despacho.getCotizacion().getPuntoVenta().getNombre(), factura.getRetirarCliente());

		despacho.getDetalle().forEach(x -> {
			reporteDTO.agregarDetalle(new GuiaDespachoDetalleDTO(x.getCotizacionDetalle().getDescripcionArticulo(),
					x.getCotizacionDetalle().getCodigoArticulo(), x.getCotizacionDetalle().getCodigoArticuloAlterno(),
					x.getCantidad(), x.getSaldo(), x.getCodigoBodega(), x.getCompartimientos()));
		});

		return reporteDTO;
	}

	@Override
	public byte[] generarReporte(Long despachoID) {
		Optional<DocumentoGuiaDespacho> guiaDespacho = guiaDespachoRepositorio.findById(despachoID);
		if (guiaDespacho.isPresent()) {

			DocumentoGuiaDespacho despacho = guiaDespacho.get();
			Optional<DocumentoFactura> facturaConsultada = facturaRepositorio
					.findById(despacho.getDocumentoFacturaId());

			if (facturaConsultada.isPresent()) {
				DocumentoFactura factura = facturaConsultada.get();
				try {
					return reporteServicio.generarReporte("GuiaDespacho",
							Collections.singleton(crearReporteDTO(factura, despacho)), new HashMap<>());
				} catch (JasperReportsException e) {
					LOG.error(String.format("Error Guía Despacho Reporte: %s", e.getMessage()));
					throw new ReporteExeption("GuiaDespacho");
				}

			} else {
				throw new GuiaDespachoNoExisteException();
			}

		} else {
			throw new GuiaDespachoNoExisteException();
		}
	}
	
	@Override
	@Transactional(readOnly = true)
	public byte[] generarReporteGuiaRemision(long guiaRemisionId) {
		DocumentoGuiaRemision guiaRemision = guiaRemisionServicio.findById(guiaRemisionId).get();
		DocumentoGuiaDespacho guiaDespacho = guiaDespachoRepositorio.findById(guiaRemision.getDocumentoPadreId()).get();
		
		AltGuiaTransaction guiaTransaction = obtenerDatosIntegracionGuiaRemision(guiaRemision, guiaDespacho);
		GuiaRemision guiaRemisionReporte = guiaRemisionServicio.obtenerGuiaRemision(guiaTransaction, guiaRemision);
		
		if(StringUtils.isBlank(guiaRemision.getNumero()))
			throw new GuiaRemisionException("La Guía de Remisión solicitada no ha sido procesada");
		
		try {
			
			return reporteServicio.generarReporte("GuiaRemision\\guiaRemision", Collections.singleton(guiaRemisionReporte), new HashMap<>());
			
		}catch (JasperReportsException ex) {
			LOG.error(String.format("Error Guia de Remision Reporte: %s", ex.getMessage()));
			throw new ReporteExeption("GuiaDespacho");
		}
		
	}

	@Override
	public byte[] generarReporteDespachoCliente(Long despachoID, DocumentoGuiaRemision guiaRemision) {
		Optional<DocumentoGuiaDespacho> guiaDespachoOpt = guiaDespachoRepositorio.findById(despachoID);
		if (guiaDespachoOpt.isPresent()) {
			DocumentoGuiaDespacho guiaDespacho = guiaDespachoOpt.get();
			DocumentoBase documentoCalculo;
			if (CollectionUtils.isEmpty(guiaRemision.getDetalle())) {
				guiaDespacho.getDetalle().forEach(DocumentoDetalle::getCotizacionDetalle);
				documentoCalculo = guiaDespacho;
			} else
				documentoCalculo = guiaRemision;

			boolean calcularCantidad = CollectionUtils.isEmpty(guiaRemision.getDetalle());

			final DocumentoBase guiaRemisionFinal = SerializationUtils.clone(documentoCalculo);

			guiaDespacho.getDetalle().parallelStream().forEach(d -> {
				Optional<DocumentoDetalle> detalleGuiaOpt = guiaRemisionFinal.getDetalle().stream()
						.filter(dg -> dg.getCotizacionDetalle().getId() == d.getCotizacionDetalle().getId())
						.findFirst();
				if (detalleGuiaOpt.isPresent()) {
					DocumentoDetalle detallaGuiaR = detalleGuiaOpt.get();
					if (calcularCantidad)
						d.setCantidad(d.getCantidad());
//						d.setCantidad(d.getCantidad().subtract(detallaGuiaR.getSaldo()));
					else
						d.setCantidad(detallaGuiaR.getCantidad());
				}
			});

//			java.util.function.Predicate<DocumentoDetalle> predicadoDetalle = (d) -> (d.getCantidad().compareTo(BigDecimal.ZERO) == 0 && d.getSaldo().compareTo(BigDecimal.ZERO) == 0);
//			guiaDespacho.getDetalle().removeIf(predicadoDetalle);

			Optional<DocumentoFactura> facturaConsultada = facturaRepositorio
					.findById(guiaDespacho.getDocumentoFacturaId());

			if (facturaConsultada.isPresent()) {
				DocumentoFactura factura = facturaConsultada.get();
				try {
					return reporteServicio.generarReporte("GuiaDespachoCliente",
							Collections.singleton(crearReporteDTO(factura, guiaDespacho)), new HashMap<>());
				} catch (JasperReportsException e) {
					LOG.error(String.format("Error Guía Despacho Reporte: %s", e.getMessage()));
					throw new ReporteExeption("GuiaDespacho");
				}

			} else {
				throw new GuiaDespachoNoExisteException();
			}

		} else {
			throw new GuiaDespachoNoExisteException();
		}
	}

	@Override
	public byte[] generarReportePorFacturaID(Long facturaID) {

		Optional<DocumentoGuiaDespacho> guiaDespacho = guiaDespachoRepositorio.findByDocumentoFacturaId(facturaID);
		Optional<DocumentoFactura> facturaConsultada = facturaRepositorio.findById(facturaID);
		if (guiaDespacho.isPresent() && facturaConsultada.isPresent()) {

			DocumentoGuiaDespacho despacho = guiaDespacho.get();
			DocumentoFactura factura = facturaConsultada.get();

			try {
				return reporteServicio.generarReporte("GuiaDespacho",
						Collections.singleton(crearReporteDTO(factura, despacho)), new HashMap<>());
			} catch (JasperReportsException e) {
				LOG.error(String.format("Error Guía Despacho Reporte: %s", e.getMessage()));
				throw new ReporteExeption("GuiaDespacho");
			}

		} else {
			throw new GuiaDespachoNoExisteException();
		}
	}

	@Override
	public DocumentoGuiaDespacho guardarDespachoDeFactura(DocumentoFactura factura) {

		DocumentoGuiaDespacho guiaDespacho = new DocumentoGuiaDespacho(factura, factura.getBodegaPrincipal(),
				factura.getEntrega(), factura.getDireccionEntrega(), factura.getDireccionEntregaDescripcion());

		guiaDespacho.setReferencia(factura.getNumero());

		List<DocumentoDetalle> detalles = factura.getDetalle().stream()
				.filter(x -> x.getCodigoBodega().equalsIgnoreCase(factura.getBodegaPrincipal()))
				.collect(Collectors.toList());

		if (factura.getEstado().equals(DocumentoEstado.ANULADO)) {
			detalles.forEach(x -> {
				agregarDetalle(guiaDespacho, x, x.getCantidad());
			});

		} else {

			detalles.forEach(x -> {
				if (x.getSaldo().compareTo(BigDecimal.ZERO) != 0) {
					agregarDetalle(guiaDespacho, x, x.getSaldo());
				}

			});
		}

		if (detalles.isEmpty()) {
			return null;
		}

		guiaDespacho.setNumero(documentoServicio
				.secuencialDocumento(guiaDespacho.getCotizacion().getPuntoVenta(), TipoDocumento.GUIA_DESPACHO)
				.getNumeroSecuencial());

		BigDecimal saldo = guiaDespacho.getDetalle().stream().map(det -> det.getSaldo()).reduce(BigDecimal.ZERO,
				BigDecimal::add);

		if (saldo.compareTo(BigDecimal.ZERO) == 0)
			guiaDespacho.setEstado(DocumentoEstado.COMPLETADO);
		else
			guiaDespacho.setEstado(DocumentoEstado.EMITIDO);

		return guiaDespachoRepositorio.save(guiaDespacho);
	}

	private void agregarDetalle(DocumentoGuiaDespacho guiaDespacho, DocumentoDetalle x, BigDecimal cantidad) {
		DocumentoDetalle detalle = new DocumentoDetalle(x.getCotizacionDetalle(), x.getCodigoBodega(),
				cantidad);

		x.getCompartimientos().forEach(y -> {

			DocumentoDetalleCompartimiento compartimiento = new DocumentoDetalleCompartimiento(
					y.getCantidad(), y.getCompartimiento());

			detalle.agregarCompartimiento(compartimiento);

		});

		if (detalle.cotizacionDetalle.getServicio())
			detalle.setSaldo(BigDecimal.ZERO);

		guiaDespacho.agregarLineaDetalle(detalle);
	}

	@Override
	public List<Long> listarGuiasPorCotizacionID(Long cotizacionID) {
		List<Long> ids = guiaDespachoRepositorio.findByCotizacion_IdOrderByModificadoFechaAsc(cotizacionID).stream()
				.map(x -> x.getId()).collect(Collectors.toList());
		if (ids.isEmpty()) {
			throw new CotizacionNoTieneGuiasDespachoException();
		}
		return ids;
	}

	private List<GuiaDespachoDTO> obtenerGuiasDespachoOverviewPorBodega(String bodega, String estado) {
		List<GuiaDespachoDTO> guiasDto = new ArrayList<GuiaDespachoDTO>();
		List<Object[]> guiasLinea = guiaDespachoRepositorio.obtenerGuiasDespachoOverviewByBodegaAndEstado(bodega,
				estado);
		if (CollectionUtils.isNotEmpty(guiasLinea)) {
			guiasDto.addAll(guiasLinea.stream().map(g -> {
				GuiaDespachoDTO guiaDto = new GuiaDespachoDTO();
				guiaDto.setIdGuiaDespacho(new BigInteger(g[0].toString()).longValue());
				guiaDto.setIdentificacionCliente(g[1].toString());
				guiaDto.setNombreCliente(g[2].toString());
				guiaDto.setNumeroFactura(g[3].toString());
				guiaDto.setFechaFactura(((Timestamp) g[4]).toLocalDateTime());
				guiaDto.setNumeroGuiaDespacho(g[5].toString());
				guiaDto.setFechaGuiaDespacho(((Timestamp) g[6]).toLocalDateTime());
				guiaDto.setValorTotalFactura(new BigDecimal(g[7].toString()));
				guiaDto.setEstadoGuiaRemision(DocumentoEstado.valueOf(g[8].toString()));
				guiaDto.setEntrega(Entrega.valueOf(g[9].toString()));
				guiaDto.setPesoGuiaDespacho(new BigDecimal(g[10].toString()));
				return guiaDto;
			}).collect(Collectors.toList()));
		}
		return guiasDto;
	}

	@Override
	@Transactional(readOnly = true)
	public List<DocumentoGuiaRemision> obtenerGuiasRemisionPorDespachoId(long despachoId){
		List<DocumentoGuiaRemision> guiasRemision = guiaRemisionServicio.buscarPorDocumentoPadreId(despachoId);
		if(CollectionUtils.isEmpty(guiasRemision))
			return new ArrayList<DocumentoGuiaRemision>();
		else
			return guiasRemision;
	}

	@Override
	@Transactional
	public Map<String, DocumentoBase> obtenerGuiaDespachoPorId(long guiaDespachoId) {
		Map<String, DocumentoBase> documentos = new HashMap<String, DocumentoBase>();
		Optional<DocumentoGuiaDespacho> guiaDespachoOpt = guiaDespachoRepositorio.findById(guiaDespachoId);
		if (guiaDespachoOpt.isEmpty())
			return documentos;
		else {
			DocumentoGuiaDespacho guiaDespacho = guiaDespachoOpt.get();
			DocumentoFactura factura = facturaRepositorio.findById(guiaDespacho.getDocumentoFacturaId()).get();
			guiaDespacho.setNumeroFactura(factura.getNumero());
			guiaDespacho.setNumeroCotizacion(guiaDespacho.getCotizacion().getNumero());
			guiaDespacho.setRetirarCliente(factura.getRetirarCliente());
			documentos.put("guiaDespacho", guiaDespacho);
			documentos.put("factura", factura);
			return documentos;
		}
	}
	
	@Override
	@Transactional
	public Map<String, Object> procesarDespacho(long guiaDespachoId, DocumentoGuiaRemision guiaRemision) {
		Map<String, Object> resumenIntegracion = new HashMap<String, Object>();
		String mensaje = StringUtils.EMPTY;
		DocumentoGuiaDespacho guiaDespacho = this.guiaDespachoRepositorio.findById(guiaDespachoId).get();
		LOG.info(String.format("Procesando despacho %s", gsonLog.toJson(guiaDespacho)));
		LOG.info(String.format("Procesando guia remision %s", gsonLog.toJson(guiaRemision)));

		if(!guiaDespacho.getEstado().equals(DocumentoEstado.COMPLETADO)) {
			
			if(guiaRemision.getDocumentoPadreId() != 0 ) {
				if(guiaRemision.getId() != ((long) 0))
					guiaRemision = guiaRemisionServicio.findById(guiaRemision.getId()).get();
				else
				{
					SiteSetup siteSetupOrigen = bodegaservicio.listarBodegaPorLocnCode(guiaRemision.getBodegaPartida().trim());
					guiaRemision.setDireccionPartida(siteSetupOrigen.getLocnDscr().trim());
					if(StringUtils.isBlank(guiaRemision.getCedulaConductor()) && StringUtils.isBlank(guiaRemision.getNombreConductor())) {
						guiaDespacho.setEntrega(Entrega.CLIENTE);
						guiaRemision.setCedulaConductor(guiaDespacho.getCotizacion().getCodigoCliente());
						guiaRemision.setNombreConductor(guiaDespacho.getCotizacion().getNombreCliente());
					} else {
						guiaDespacho.setEntrega(Entrega.DESPACHO);
					}
				}

				guiaRemision.setCotizacion(guiaDespacho.getCotizacion());

				if (StringUtils.isBlank(guiaRemision.getNumero())) {
					String numeroDocumento = documentoServicio.obtenerNumeroDocumentoGuiaRemision(guiaRemision);
					if (StringUtils.isBlank(numeroDocumento)) {
						mensaje = "Error al obtener el numero de guia de remision";
						LOG.error(mensaje);
						guiaRemision.setMensajeError(mensaje);
						guiaRemision.setEstado(DocumentoEstado.ERROR);
						resumenIntegracion.put(MensajesControlador.MENSAJE_CODIGO, "ERROR");
						resumenIntegracion.put(MensajesControlador.MENSAJE, mensaje);
					}
					guiaRemision.setNumero(numeroDocumento);
				}

				if (!guiaRemision.getEstado().equals(DocumentoEstado.COMPLETADO)) {
					if (guiaRemision.getEstado().equals(DocumentoEstado.NUEVO)) {
						calcularSaldos(guiaDespacho, guiaRemision);
					}
					
					if(!CollectionUtils.isEmpty(guiaRemision.getDetalle()))
						guiaRemision.limpiarCantidadesCero();
					
					AltGuiaTransaction guiaTransaction = obtenerDatosIntegracionGuiaRemision(guiaRemision, guiaDespacho);
					guiaRemisionServicio.generarXmlGuiaRemision(guiaTransaction, guiaRemision);
					
					LOG.info(String.format("Guia de remision a integrar: %s", gsonLog.toJson(guiaTransaction)));
					
					IvResponse response = documentoServicio.llamarApiIntegracionGuiaRemision(guiaTransaction);
					guiaDespacho.setDireccionEntrega(guiaRemision.getDireccionEntregaCodigo());
					guiaDespacho.setDireccionEntregaDescripcion(guiaRemision.getDireccionEntega());
					guiaRemision.setReferencia(guiaDespacho.getNumero());
					if (StringUtils.isNotBlank(response.getErrorCode()) && response.getErrorCode().equals("0")) {
						guiaRemision.setEstado(DocumentoEstado.COMPLETADO);
						guiaRemision.setFechaEmision(LocalDateTime.now());
						if (validarGuiaDespachoCompleta(guiaDespacho))
							guiaDespacho.setEstado(DocumentoEstado.COMPLETADO);
						resumenIntegracion.put(MensajesControlador.MENSAJE_CODIGO, "OK");
					} else {
						guiaRemision.setEstado(DocumentoEstado.ERROR);
						guiaRemision.setMensajeError(
								response.getErrorMessage().length() > 255 ? response.getErrorMessage().substring(0, 255)
										: response.getErrorMessage());
						resumenIntegracion.put(MensajesControlador.MENSAJE_CODIGO, "ERROR");
						resumenIntegracion.put(MensajesControlador.MENSAJE, guiaRemision.getMensajeError());
					}
				}
				
				this.guiaRemisionServicio.save(guiaRemision);			
				
				resumenIntegracion.put("guiaRemision", guiaRemision);
			} else {
				calcularSaldos(guiaDespacho, guiaRemision);
				if (validarGuiaDespachoCompleta(guiaDespacho))
					guiaDespacho.setEstado(DocumentoEstado.COMPLETADO);
				guiaDespacho.setDireccionEntrega(guiaDespacho.getCotizacion().getCodigoDireccion());
				guiaDespacho.setDireccionEntregaDescripcion(guiaDespacho.getCotizacion().getDescripcionDireccion());

				resumenIntegracion.put(MensajesControlador.MENSAJE_CODIGO, "OK");
				resumenIntegracion.put("guiaRemision", guiaRemision);
				
			}
			this.guiaDespachoRepositorio.save(guiaDespacho);
		}else {
			resumenIntegracion.put("guiaRemision", guiaRemision);
		}
		
		return resumenIntegracion;
	}

	private void calcularSaldos(DocumentoGuiaDespacho guiaDespacho, DocumentoGuiaRemision guiaRemision) {
		final DocumentoGuiaRemision guiaRemisionFinal = SerializationUtils.clone(guiaRemision);
		guiaDespacho.getDetalle().parallelStream().forEach(d -> {
			Optional<DocumentoDetalle> detalleGuiaOpt = guiaRemisionFinal.getDetalle().stream()
					.filter(dg -> dg.getCotizacionDetalle().getId() == d.getCotizacionDetalle().getId()).findFirst();
			if (detalleGuiaOpt.isPresent()) {
				DocumentoDetalle detallaGuiaR = detalleGuiaOpt.get();
				d.setSaldo(d.getSaldo().subtract(detallaGuiaR.getCantidad()));
			}
		});
	}

	private boolean validarGuiaDespachoCompleta(DocumentoGuiaDespacho guiaDespacho) {
		boolean esCompleta = false;
		BigDecimal saldo = guiaDespacho.getDetalle().stream().map(DocumentoDetalle::getSaldo).reduce(BigDecimal.ZERO,
				BigDecimal::add);
		if (saldo.compareTo(BigDecimal.ZERO) == 0)
			esCompleta = true;
		return esCompleta;
	}

	private AltGuiaTransaction obtenerDatosIntegracionGuiaRemision(DocumentoGuiaRemision guiaRemision,
			DocumentoGuiaDespacho guiaDespacho) {
		final String formatoFecha = "yyyy-MM-dd HH:mm:ss";
		
		long conteo = guiaRemisionServicio.countByDocumentoPadreId(guiaDespacho.getId());
		
		DocumentoGuiaDespacho docGuiaDespacho = new DocumentoGuiaDespacho();
		docGuiaDespacho.setId(++conteo);
		docGuiaDespacho.setNumero(guiaDespacho.getNumero());

		String bachnumb = documentoServicio.obtenerBachNumber(docGuiaDespacho);
		AltGuiaTransaction guiaIntegracion = new AltGuiaTransaction();

		DocumentoFactura factura = facturaRepositorio.findById(guiaDespacho.getDocumentoFacturaId()).get();

		// cabecera
		AltGuiaIv10000 cabecera = new AltGuiaIv10000();
		cabecera.setBachNumb(bachnumb);
		cabecera.setBchSourc("IV_Trans");
		cabecera.setCustNmbr(guiaDespacho.getCotizacion().getCodigoCliente());
		cabecera.setCustomerVendorName(guiaDespacho.getCotizacion().getNombreCliente());
		cabecera.setIvDocNbr(guiaRemision.getNumero());
		cabecera.setIvDocTyp("06");
		cabecera.setNumAutoriza("000-000-000000000");
		cabecera.setDocDate(UtilidadesFecha.formatear(LocalDateTime.now(), formatoFecha));
		cabecera.setIdConductor(guiaRemision.getCedulaConductor());
		cabecera.setConductor(guiaRemision.getNombreConductor());
		cabecera.setMotTraslado(guiaRemision.getMotivo());
		cabecera.setIniTraslado(UtilidadesFecha.formatear(guiaRemision.getFechaInicioTraslado(), formatoFecha));
		cabecera.setFinTraslado(UtilidadesFecha.formatear(guiaRemision.getFechaFinTraslado(), formatoFecha));
		cabecera.setDirPartida(guiaRemision.getDireccionPartida());
		cabecera.setDirLlegada(guiaRemision.getDireccionEntega());
		cabecera.setNumPlaca(guiaRemision.getPlaca());
		cabecera.setRuta(guiaRemision.getRuta());
		cabecera.setIsDelete(false);
		cabecera.setUserDeleted(StringUtils.SPACE);
		cabecera.setDateDeleted("1900-01-01 00:00:00.000");
		cabecera.setMotivoDeleted(StringUtils.SPACE);
		cabecera.setTipAfecta("01");
		cabecera.setFacAfecta(factura.getNumero());
		cabecera.setDocAduana(StringUtils.SPACE);
		cabecera.setAutFacAfecta(StringUtils.SPACE);
		cabecera.setComentario(StringUtils.SPACE);
		cabecera.setEmail(guiaRemision.getCorreo());
		guiaIntegracion.setCabeceraGuiaRemision(cabecera);
		// detalle
		guiaIntegracion.setDetalleGuiaRemision(guiaRemision.getDetalle().stream().map(d -> {
			AltGuiaIv10000Detalle detalle = new AltGuiaIv10000Detalle();
			detalle.setIvDocNbr(guiaRemision.getNumero());
			detalle.setTipAfecta("06");
			detalle.setItemNmbr(d.getCotizacionDetalle().getCodigoArticulo());
			detalle.setTrxQty(d.getCantidad());
			return detalle;
		}).collect(Collectors.toList()));
		return guiaIntegracion;
	}

	@Override
	@Transactional(readOnly = true)
	public List<GuiaDespachoDTO> obtenerGuiasDespachoPorUsuarioIdyPuntoVentaId(Map<String, Object> consulta) {
		
		if(consulta.get("perfil") == null)
			return new ArrayList<GuiaDespachoDTO>();
		
		long usuarioId = Long.valueOf((int)consulta.get("usuarioId"));
		long puntoVentaId = Long.valueOf((int)consulta.get("puntoVentaId"));
		Perfil perfil = perfilServicio.listarTodos().stream().filter(p -> p.getNombre().name().equals(consulta.get("perfil").toString())).findFirst().get();
		
		List<GuiaDespachoDTO> guiasDespachoDto = new ArrayList<GuiaDespachoDTO>();
		String bodegaPrincipal = bodegaRepositorio
				.obtenerCodigoBodegaPrincipalPorUsuarioIdPerfilIdPuntoVentaId(usuarioId, perfil.getId(), puntoVentaId);
		if (StringUtils.isNotBlank(bodegaPrincipal)) {
			guiasDespachoDto
					.addAll(obtenerGuiasDespachoOverviewPorBodega(bodegaPrincipal, DocumentoEstado.EMITIDO.toString()));
		}
		guiasDespachoDto = guiasDespachoDto.stream()
				.sorted(Comparator.comparing(GuiaDespachoDTO::getFechaGuiaDespacho).reversed())
				.collect(Collectors.toList());
		return guiasDespachoDto;
	}

	@Override
	@Transactional(readOnly = true)
	public Page<GuiaDespachoDTO> consultarGuiasDespacho(Pageable pageable, GuiaDespachoConsulta consulta){
		
		String bodegaPrincipal = bodegaRepositorio.obtenerCodigoBodegaPrincipalPorUsuarioIdPerfilIdPuntoVentaId(consulta.getUsuarioId(), consulta.getPerfil().getId(), consulta.getPuntoVentaId());
		if(StringUtils.isBlank(bodegaPrincipal)) {
			Page<GuiaDespachoDTO> pageResut = new PageImpl<GuiaDespachoDTO>(new ArrayList<GuiaDespachoDTO>(), pageable, 0);
			return pageResut;
			
		} else {

			consulta.setBodega(bodegaPrincipal);

			CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
			CriteriaQuery<DocumentoGuiaDespacho> query = criteriaBuilder.createQuery(DocumentoGuiaDespacho.class);

			Root<DocumentoGuiaDespacho> guiaDespacho = query.from(DocumentoGuiaDespacho.class);
			List<Predicate> predicadosConsulta = new ArrayList<Predicate>();

			if (!StringUtils.isBlank(consulta.getBodega()))
				predicadosConsulta.add(criteriaBuilder.equal(guiaDespacho.get("bodega"), consulta.getBodega()));

			if (!StringUtils.isEmpty(consulta.getNumeroDocumento()))
				predicadosConsulta.add(criteriaBuilder.like(guiaDespacho.get("numero"), consulta.getNumeroDocumento()));

			if (consulta.getEstado() != null)
				predicadosConsulta.add(criteriaBuilder.equal(guiaDespacho.get("estado"), consulta.getEstado()));

			if (consulta.getFechaInicio() != null && consulta.getFechaFin() == null)
				consulta.setFechaFin(consulta.getFechaInicio().plusDays(0));

			if (consulta.getFechaInicio() == null && consulta.getFechaFin() != null) {
				consulta.setFechaInicio(consulta.getFechaFin());
				consulta.setFechaFin(consulta.getFechaInicio().plusDays(0));
			}

			if (consulta.getFechaInicio() != null && consulta.getFechaFin() != null) {
				consulta.setFechaFin(consulta.getFechaFin().plusDays(1));
				predicadosConsulta.add(criteriaBuilder.between(guiaDespacho.get("fechaEmision"),
						consulta.getFechaInicio(), consulta.getFechaFin()));
			}

			if (consulta.getTipoEntrega() != null)
				predicadosConsulta.add(criteriaBuilder.equal(guiaDespacho.get("entrega"), consulta.getTipoEntrega()));

			Predicate[] predicados = new Predicate[predicadosConsulta.size()];
			predicados = predicadosConsulta.toArray(predicados);
			query.where(predicados).orderBy(criteriaBuilder.desc(guiaDespacho.get("id")));

			TypedQuery<DocumentoGuiaDespacho> statement = entityManager.createQuery(query);

			List<DocumentoGuiaDespacho> despachosResult = statement.getResultList();

			int sizeTotal = despachosResult.size();

			int start = (int) pageable.getOffset();
			int end = (start + pageable.getPageSize()) > despachosResult.size() ? despachosResult.size()
					: (start + pageable.getPageSize());

			despachosResult = despachosResult.subList(start, end);

			List<GuiaDespachoDTO> resultadoConsulta = despachosResult.stream().map(c -> {
				DocumentoFactura factura = this.facturaRepositorio.findById(c.getDocumentoFacturaId()).get();

				GuiaDespachoDTO guia = new GuiaDespachoDTO();
				guia.setIdGuiaDespacho(c.getId());
				guia.setNumeroGuiaDespacho(c.getNumero());
				guia.setFechaGuiaDespacho(c.getFechaEmision());
				guia.setEntrega(c.getEntrega());
				guia.setEstadoGuiaRemision(c.getEstado());
				guia.setIdentificacionCliente(c.getCotizacion().getCodigoCliente());
				guia.setNombreCliente(c.getCotizacion().getNombreCliente());
				guia.setNumeroFactura(factura.getNumero());
				guia.setFechaFactura(factura.getFechaEmision());
				return guia;
			}).collect(Collectors.toList());

			Page<GuiaDespachoDTO> pageResut = new PageImpl<GuiaDespachoDTO>(resultadoConsulta, pageable, sizeTotal);

			return pageResut;
		}

	}

	@Override
	public List<GuiaDespachoDTO> listarTodasPorCotizacionID(long cotizacionID) {
		List<GuiaDespachoDTO> dto = new ArrayList<>();
		List<DocumentoGuiaDespacho> guias = guiaDespachoRepositorio
				.findByCotizacion_IdOrderByModificadoFechaAsc(cotizacionID);
		guias.forEach(x -> {
			dto.add(new GuiaDespachoDTO(x.getId(), x.getNumero(), x.getEstado()));
		});

		return dto;
	}

	@Transactional
	public DocumentoGuiaDespacho generarGuiaDespacho(long facturaId) {

		Optional<DocumentoFactura> facturaOp = facturaRepositorio.findById(facturaId);

		if (facturaOp.isPresent()) {

			DocumentoFactura factura = facturaOp.get();

			if (!factura.isDespachada()) {

				DocumentoGuiaDespacho guiaDespacho = guardarDespachoDeFactura(facturaOp.get());

				factura.setDespachada(true);

				return guiaDespacho;
			}
		}

		return null;
	}

	public List<DocumentoFactura> facturasSinGuiasDespacho() {

		return facturaRepositorio.findByEstadoAndDespachada(DocumentoEstado.EMITIDO, false).stream()
				.filter(x -> UtilidadesCadena.noEsNuloNiBlanco(x.getNumero())).collect(Collectors.toList());

	}

}
