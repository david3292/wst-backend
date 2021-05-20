package com.altioracorp.wst.servicioImpl.ventas;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.mail.MessagingException;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.HttpStatusCodeException;

import com.altioracorp.gpintegrator.client.GuiaRemision.AltGuiaIv10000;
import com.altioracorp.gpintegrator.client.GuiaRemision.AltGuiaIv10000Detalle;
import com.altioracorp.gpintegrator.client.GuiaRemision.AltGuiaTransaction;
import com.altioracorp.gpintegrator.client.inventory.IvResponse;
import com.altioracorp.gpintegrator.client.inventory.IvTransferIntegrator;
import com.altioracorp.wst.constantes.integracion.UrlIntegracionGuiaRemision;
import com.altioracorp.wst.dominio.logistica.dto.TransferenciaResumenDTO;
import com.altioracorp.wst.dominio.ventas.DocumentoBase;
import com.altioracorp.wst.dominio.ventas.DocumentoDetalle;
import com.altioracorp.wst.dominio.ventas.DocumentoEstado;
import com.altioracorp.wst.dominio.ventas.DocumentoGuiaRemision;
import com.altioracorp.wst.dominio.ventas.DocumentoReserva;
import com.altioracorp.wst.dominio.ventas.DocumentoTransferencia;
import com.altioracorp.wst.dominio.ventas.DocumentoTransferenciaEntrada;
import com.altioracorp.wst.dominio.ventas.DocumentoTransferenciaSalida;
import com.altioracorp.wst.dominio.ventas.TipoTransferencia;
import com.altioracorp.wst.exception.logistica.GuiaRemisionException;
import com.altioracorp.wst.exception.logistica.GuiaRemisionNoEncontrada;
import com.altioracorp.wst.exception.logistica.TransferenciaCompartimientosIncompletosException;
import com.altioracorp.wst.exception.logistica.TransferenciaNoEncontrada;
import com.altioracorp.wst.exception.reporte.JasperReportsException;
import com.altioracorp.wst.exception.reporte.ReporteExeption;
import com.altioracorp.wst.exception.ventas.DocumentoGPException;
import com.altioracorp.wst.servicio.notificaciones.INotificacionServicio;
import com.altioracorp.wst.servicio.reporte.IGeneradorJasperReports;
import com.altioracorp.wst.servicio.ventas.IArticuloServicio;
import com.altioracorp.wst.servicio.ventas.IDocumentoServicio;
import com.altioracorp.wst.servicio.ventas.IGuiaRemisionServicio;
import com.altioracorp.wst.servicio.ventas.IReservaFacturaServicio;
import com.altioracorp.wst.servicio.ventas.ITransferenciaSalidaServicio;
import com.altioracorp.wst.servicio.ventas.TransferenciaComunes;
import com.altioracorp.wst.util.UtilidadesFecha;
import com.altioracorp.wst.xml.guiaRemision.GuiaRemision;
import com.google.gson.Gson;

@Service
public class TransferenciaSalidaServicioImpl extends TransferenciaComunes implements ITransferenciaSalidaServicio {	

	@Autowired
	private IArticuloServicio articuloServicio;

	@Autowired
	IReservaFacturaServicio reservaFacturaServicio;

	@Autowired
	private INotificacionServicio notificacionServicio;
	
	@Autowired
	private IGuiaRemisionServicio guiaRemisionServicio;
	
	@Autowired
	private IDocumentoServicio documentoServicio;

	@Autowired
	private Gson gsonLog;
	
	@Autowired
	private IGeneradorJasperReports generarReporteServicio;
	
	@Override
	@Transactional(readOnly = true)
	public byte[]generarReporteGuiaRemision(long guiaTransferenciaSalidaId) {
		DocumentoGuiaRemision guiaRemision = guiaRemisionServicio.buscarPorDocumentoPadreId(guiaTransferenciaSalidaId).stream().findFirst().get();
		
		if(StringUtils.isBlank(guiaRemision.getNumero()))
			throw new GuiaRemisionException("La Guía de Remisión solicitada, no ha sido procesada");
		
		AltGuiaTransaction guiaTransaction = obtenerDatosIntegracionGuiaRemision(guiaRemision);
		GuiaRemision guiaRemisionReporte = guiaRemisionServicio.obtenerGuiaRemision(guiaTransaction, guiaRemision);
		
		try {

			return generarReporteServicio.generarReporte("GuiaRemision\\guiaRemision",Collections.singleton(guiaRemisionReporte), new HashMap<>());

		} catch (JasperReportsException ex) {
			LOG.error(String.format("Error Guia de Remision Reporte: %s", ex.getMessage()));
			throw new ReporteExeption("GuiaDespacho");
		}
	}

	@Override
	@Transactional(noRollbackFor = { MessagingException.class })
	public void anularTransferencia(long transferenciaSalidaId) {

		DocumentoTransferenciaSalida transferenciaSalida = anularTransferenciaSalida(transferenciaSalidaId);

		DocumentoTransferencia transferencia = transferenciaRepositorio
				.findById(transferenciaSalida.getDocumentoTransferenciaId()).get();

		this.articuloServicio.liberarReservasArticulos(transferencia.getDetalle());

		transferencia.setEstado(DocumentoEstado.ANULADO);

		transferenciaRepositorio.save(transferencia);

		anularDocumentoReserva(transferencia.getDocumentoPadreId());

		if(transferencia.getTipoTransferencia().equals(TipoTransferencia.VENTA))
			notificacionServicio.notificarTransferenciaAnulada(transferencia);

	}

	private void anularDocumentoReserva(Long documentoReservaId) {

		if (documentoReservaId != null && documentoReservaId != 0) {

			DocumentoReserva reserva = reservaFacturaServicio.buscarReserva(documentoReservaId);

			if (reserva.getEstado().equals(DocumentoEstado.EMITIDO)) {

				liberarReservaTransferenciaIngreso(reserva.getId());

				reservaFacturaServicio.anularReserva(reserva);
			}
		}
	}

	private DocumentoTransferenciaSalida anularTransferenciaSalida(long transferenciaSalidaId) {

		DocumentoTransferenciaSalida transferenciaSalida = transferenciaSalidaRepositorio
				.findById(transferenciaSalidaId).get();
		
		transferenciaSalida.getDetalle()
				.forEach(detalle -> this.articuloServicio.liberarReservasCompartimientos(detalle));

		transferenciaSalida.setEstado(DocumentoEstado.ANULADO);

		transferenciaSalidaRepositorio.save(transferenciaSalida);

		return transferenciaSalida;
	}

	private void liberarReservaTransferenciaIngreso(long reservaId) {

		List<DocumentoTransferenciaEntrada> transferenciaEntradas = transferenciaEntradaRepositorio
				.findByDocumentoReservaId(reservaId);

		LOG.info(String.format("Liberando %s Transferencias de entradas: %s ", transferenciaEntradas.size(),
				gsonLog.toJson(transferenciaEntradas)));

		transferenciaEntradas.forEach(x -> {
			if (x.getEstado().equals(DocumentoEstado.EMITIDO)) {
				articuloServicio.liberarReservasArticulos(x.getDetalle());
			}
		});

	}
	
	@Override
	@Transactional(readOnly = false)
	public TransferenciaResumenDTO integrarTransferenciaSalidaGp(long transferenciaId) {
		TransferenciaResumenDTO transferenciaResumen = new TransferenciaResumenDTO();
		Optional<DocumentoTransferenciaSalida> transferenciaOpt = transferenciaSalidaRepositorio.findById(transferenciaId);
		if(transferenciaOpt.isEmpty())
			throw new TransferenciaNoEncontrada();		
		
		DocumentoBase transferencia =  transferenciaOpt.get();
		LOG.info(String.format("Inicio de integracion de transferencia de salida: %s", gsonLog.toJson(transferenciaOpt.get())));
		List<DocumentoEstado> estadosNoIntegrar = Arrays.asList(DocumentoEstado.COMPLETADO, DocumentoEstado.EMITIDO);
		
		if(estadosNoIntegrar.contains(transferencia.getEstado())) {
			transferenciaResumen.setTranferenciaCreada(transferencia.getNumero());
			
			DocumentoTransferencia transf = validarTransferenciaCompleta(transferencia);
			transferenciaRepositorio.save(transf);			
		}else {
			generarCompartimientosTransferenciaSalida(transferencia.getDetalle());
			LOG.info(String.format("Compartimientos generados: %s", gsonLog.toJson(transferencia.getDetalle())));
			if(!validaTransferenciaComplartimientosIguales(transferencia))
				throw new TransferenciaCompartimientosIncompletosException("Las cantidades de los compartimientos no coinciden con las cantidades a transferir");
			
			if(StringUtils.isBlank(transferencia.getNumero())) {
				String numeroDocumento = obtenerNumeroDocumentoTransferencia(transferenciaOpt.get());
				if(StringUtils.isBlank(numeroDocumento))
					throw new DocumentoGPException("Error al obtener el número de documento");
				
				transferencia.setNumero(numeroDocumento);
			}
			
			if(CollectionUtils.isNotEmpty(transferenciaOpt.get().getDetalle()))
				transferenciaOpt.get().limpiarCantidadesCero();
			
			IvTransferIntegrator inventoryIntegrator = obtenerDatosParaIntegracionTransferencia(transferenciaOpt.get());
			LOG.info(String.format("Transferencia Salida Integracion datos: %s", gsonLog.toJson(inventoryIntegrator)));
			IvResponse response = llamarApiIntegracionTransferencia(inventoryIntegrator);
			
			String mensajeError = StringUtils.EMPTY;
			if(StringUtils.isNotBlank(response.getErrorCode()) && response.getErrorCode().equals("0")) {
				if(transferencia instanceof DocumentoTransferenciaSalida) {
					this.articuloServicio.liberarReservasArticulosYCompartimientos(transferencia.getDetalle());
					DocumentoTransferencia transf = validarTransferenciaCompleta(transferencia);
					transf.limpiarCantidadesCero();
					transferenciaRepositorio.save(transf);
				}
				
				if(transferencia instanceof DocumentoTransferenciaEntrada) {
					completarTransferenciaIngreso(transferencia);
				}
					
				transferenciaResumen.setTranferenciaCreada(transferencia.getNumero());
				transferencia.setEstado(DocumentoEstado.EMITIDO);
				
			}else {			
				transferencia.setEstado(DocumentoEstado.ERROR);	
				mensajeError = response.getErrorMessage().length() > 255 ? response.getErrorMessage().substring(0, 255) : response.getErrorMessage();			
				transferenciaResumen.setTranferenciaCreada("ERROR");
				transferenciaResumen.setErrorTransferencia(mensajeError);
			}
			guardarTranferencia(transferencia, mensajeError);
		}
		
		return transferenciaResumen;
	}

	@Transactional(readOnly = false )
	private DocumentoTransferencia validarTransferenciaCompleta(DocumentoBase documento) {
		DocumentoTransferenciaSalida transferenciaSalida = (DocumentoTransferenciaSalida)documento;		
		
		DocumentoTransferencia transferencia = this.transferenciaRepositorio.findById(transferenciaSalida.getDocumentoTransferenciaId()).get();
		LOG.info(String.format("Validando transferencia de salida completa: %s - %s", gsonLog.toJson(transferenciaSalida), gsonLog.toJson(transferencia)));
		List<DocumentoEstado> estadosRestar = Arrays.asList(DocumentoEstado.NUEVO, DocumentoEstado.ERROR);
		if(estadosRestar.contains(documento.getEstado())) {
			transferencia.getDetalle().parallelStream().forEach(dt -> {
				Optional<DocumentoDetalle> detalleOpt = documento.getDetalle().stream()
						.filter(detalleT -> detalleT.getCodigoArticulo().equals(dt.getCodigoArticulo()))
						.findFirst();
				
				if(detalleOpt.isPresent()) {
					DocumentoDetalle dd = detalleOpt.get();
					dt.setSaldo(dt.getSaldo().subtract(dd.getCantidad()));
				}
				
			});
		}
		
		BigDecimal saldoTransferencia = transferencia.getDetalle().stream().map(DocumentoDetalle::getSaldo).reduce(BigDecimal.ZERO, BigDecimal::add);
		
		if((saldoTransferencia.compareTo(BigDecimal.ZERO) == 0)) {			
			transferencia.setEstado(DocumentoEstado.COMPLETADO);			
		}
		return transferencia;
	}
	
	@Transactional
	private void generarCompartimientosTransferenciaSalida(List<DocumentoDetalle> detalles) {
		detalles.parallelStream().forEach(d -> d.getCompartimientos().removeIf(c -> c.getCantidad().compareTo(BigDecimal.ZERO) == 0));
		detalles.forEach(d -> articuloServicio.liberarReservasCompartimientos(d));
		detalles.forEach(d -> d.getCompartimientos().clear());
		
		detalles.forEach(d -> this.articuloServicio.agregarCompartimientoADetalle(d));
	}	
	
	@Transactional
	private void completarTransferenciaIngreso(DocumentoBase documento) {
		DocumentoTransferenciaEntrada transferenciaEntrada = (DocumentoTransferenciaEntrada)documento;
		DocumentoTransferenciaSalida transferenciaSalida = transferenciaSalidaRepositorio.findById(transferenciaEntrada.getDocumentoTransferenciaSalidaId()).get();
		transferenciaEntrada.setEstado(DocumentoEstado.COMPLETADO);
		transferenciaSalida.setEstado(DocumentoEstado.COMPLETADO);
		
		LOG.info(String.format("Completando transferencia de ingreso %s - %s", gsonLog.toJson(transferenciaEntrada), gsonLog.toJson(transferenciaSalida)));
		
		transferenciaSalida.getDetalle().parallelStream().forEach(d -> {
			Optional<DocumentoDetalle> detalleOpt = transferenciaEntrada.getDetalle().stream()
					.filter(det -> det.getCotizacionDetalle().getId() == d.getCotizacionDetalle().getId())
					.findFirst();
			if(detalleOpt.isPresent())
				d.setSaldo(d.getSaldo().subtract(detalleOpt.get().getCantidad()));
		});
		
		transferenciaSalidaRepositorio.save(transferenciaSalida);

	}

	@Override
	@Transactional
	public TransferenciaResumenDTO integrarGuiaRemisionGp(long transferenciaSalidaId) {
		boolean continuar = true;
		TransferenciaResumenDTO transferenciaResumen = new TransferenciaResumenDTO();
		List<DocumentoGuiaRemision> guias = guiaRemisionServicio.buscarPorDocumentoPadreId(transferenciaSalidaId);
		Optional<DocumentoGuiaRemision> guiaRemisionOtp = guias.stream().findFirst();
		if(guiaRemisionOtp.isEmpty())
			throw new GuiaRemisionNoEncontrada();
		
		DocumentoGuiaRemision guiaRemision = guiaRemisionOtp.get();
		if(guiaRemision.getEstado().equals(DocumentoEstado.COMPLETADO)) {
			transferenciaResumen.setGuiaRemisionCreada(guiaRemision.getNumero());
		}else {
			if(CollectionUtils.isEmpty(guiaRemision.getDetalle()))
				generarDetalleGuiaRemision(guiaRemision);
			
			if(StringUtils.isBlank(guiaRemision.getNumero())) {
				String numeroDocumento = obtenerNumeroDocumentoGuiaRemision(guiaRemision);
				if(StringUtils.isBlank(numeroDocumento)) {
					String mensajeError = "Error al obtener el número de documento";
					guiaRemision.setEstado(DocumentoEstado.ERROR);
					guiaRemision.setMensajeError(mensajeError);
					DocumentoTransferenciaSalida transferenciaSalida = this.transferenciaSalidaRepositorio.findById(transferenciaSalidaId).get();					
					DocumentoTransferencia transferencia = this.transferenciaRepositorio.findById(transferenciaSalida.getDocumentoTransferenciaId()).get();
					transferencia.setEstado(DocumentoEstado.EMITIDO);
					this.transferenciaRepositorio.save(transferencia);
					
					transferenciaResumen.setGuiaRemisionCreada("ERROR");
					transferenciaResumen.setErrorGuiaRemision(mensajeError);
					continuar = false;
				}
				guiaRemision.setNumero(numeroDocumento);
			}		
			if(continuar) {
				AltGuiaTransaction guiaTransaction = obtenerDatosIntegracionGuiaRemision(guiaRemision);
				LOG.info(String.format("Integracion guia de remision: %s", gsonLog.toJson(guiaTransaction)));
				
				guiaRemisionServicio.generarXmlGuiaRemision(guiaTransaction, guiaRemision);
				
				IvResponse response = llamarApiIntegracionGuiaRemision(guiaTransaction);
				
				String mensajeError = StringUtils.EMPTY;
				if(StringUtils.isNotBlank(response.getErrorCode()) && response.getErrorCode().equals("0")) {
					guiaRemision.setEstado(DocumentoEstado.COMPLETADO);
					transferenciaResumen.setGuiaRemisionCreada(guiaRemision.getNumero());
					guiaRemision.setFechaEmision(LocalDateTime.now());
				}else {
					guiaRemision.setEstado(DocumentoEstado.ERROR);	
					mensajeError = response.getErrorMessage().length() > 255 ? response.getErrorMessage().substring(0, 255) : response.getErrorMessage();
					transferenciaResumen.setGuiaRemisionCreada("ERROR");
					transferenciaResumen.setErrorGuiaRemision(mensajeError);
					DocumentoTransferenciaSalida transferenciaSalida = this.transferenciaSalidaRepositorio.findById(transferenciaSalidaId).get();					
					DocumentoTransferencia transferencia = this.transferenciaRepositorio.findById(transferenciaSalida.getDocumentoTransferenciaId()).get();
					transferencia.setEstado(DocumentoEstado.EMITIDO);
					this.transferenciaRepositorio.save(transferencia);
					
				}
				guiaRemision.setMensajeError(mensajeError);
				guiaRemisionServicio.save(guiaRemision);			
				
				transferenciaResumen.setObjeto(guiaRemision);
			}
		}
		return transferenciaResumen;		
	}
	
	@Transactional(readOnly = true)
	private void generarDetalleGuiaRemision(DocumentoGuiaRemision guiaRemision) {
		DocumentoTransferenciaSalida transferenciaSalida = transferenciaSalidaRepositorio
				.findById(guiaRemision.getDocumentoPadreId()).get();
		List<DocumentoDetalle> detalleActualizado = transferenciaSalida.getDetalle().stream()
				.filter(g -> !(g.getCantidad().compareTo(BigDecimal.ZERO) == 0)).map(d -> {
					
					DocumentoDetalle detalle = new DocumentoDetalle(d.getCodigoBodega(), d.getCantidad(),
							d.getCodigoArticulo(), d.getCodigoArticuloAlterno(), d.getClaseArticulo(),
							d.getDescripcionArticulo(), d.getPesoArticulo(), d.getUnidadMedida());
					
					return detalle;
				}).collect(Collectors.toList());

		guiaRemision.getDetalle().clear();
		guiaRemision.getDetalle().addAll(detalleActualizado);
	}
	
	private String obtenerNumeroDocumentoGuiaRemision(DocumentoGuiaRemision guiaRemision) {
		String numeroDocumento = StringUtils.EMPTY;
		final String url = constantesAmbienteServicio.getUrlIntegradorGp().concat(UrlIntegracionGuiaRemision.OBTENER_NUMERO_GP.getUrl().replace("${locnCode}", guiaRemision.getBodegaPartida()));
		try {
			numeroDocumento = restTemplate.getForObject(url, String.class);	
			numeroDocumento = numeroDocumento.replace("\"", StringUtils.EMPTY);
		} catch (Exception e) {
			LOG.error(String.format("Error al obtener el numero de documento: error: %s", e.getMessage()));
		}
		return numeroDocumento;
	}
	
	@Transactional(readOnly = true)
	private AltGuiaTransaction obtenerDatosIntegracionGuiaRemision(DocumentoGuiaRemision guiaRemision) {		
		final String formatoFecha = "yyyy-MM-dd HH:mm:ss";
		Optional<DocumentoTransferenciaSalida> transferenciaSalidaOpt = transferenciaSalidaRepositorio.findById(guiaRemision.getDocumentoPadreId());
		
		DocumentoTransferencia transferencia = new DocumentoTransferencia();
		transferencia.setNumero(transferenciaSalidaOpt.get().getReferencia());
		transferencia.setId(transferenciaSalidaOpt.get().getId());
		
		String bachNumb = documentoServicio.obtenerBachNumber(transferencia);
		
		AltGuiaTransaction guiaIntegracion = new AltGuiaTransaction();
		//cabecera
		AltGuiaIv10000 cabecera = new AltGuiaIv10000();
		cabecera.setBachNumb(bachNumb);
		cabecera.setBchSourc("IV_Trans");
		cabecera.setCustNmbr("0990281866001");
		cabecera.setCustomerVendorName("TUVAL S.A.");
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
		cabecera.setUserDeleted(" ");
		cabecera.setIsDelete(false);
		cabecera.setUserDeleted(" ");
		cabecera.setDateDeleted("1900-01-01 00:00:00.000");
		cabecera.setMotivoDeleted(" ");
		cabecera.setTipAfecta("06");
		cabecera.setFacAfecta(transferenciaSalidaOpt.get().getNumero());
		cabecera.setDocAduana(" ");
		cabecera.setAutFacAfecta(" ");
		cabecera.setComentario(" ");
		cabecera.setEmail(guiaRemision.getCorreo());
		guiaIntegracion.setCabeceraGuiaRemision(cabecera);
		//detalle
		guiaIntegracion.setDetalleGuiaRemision(guiaRemision.getDetalle().stream().map(d -> {
			AltGuiaIv10000Detalle detalle = new AltGuiaIv10000Detalle();
			detalle.setIvDocNbr(guiaRemision.getNumero());
			detalle.setTipAfecta("06");
			detalle.setItemNmbr(d.getCodigoArticulo());			
			detalle.setTrxQty(d.getCantidad());
			detalle.setDescripcionArticulo(d.getDescripcionArticulo());
			return detalle;
		})
		.collect(Collectors.toList()));
		
		return guiaIntegracion;
	}
	
	private IvResponse llamarApiIntegracionGuiaRemision(AltGuiaTransaction guiaTransaction) {
		try {
			final String url = constantesAmbienteServicio.getUrlIntegradorGp().concat(UrlIntegracionGuiaRemision.INTEGRAR_GUIA_REMISION.getUrl()); 
			return restTemplate.postForObject(url, guiaTransaction, IvResponse.class);
		}catch(HttpStatusCodeException e) {
			LOG.error(e.getMessage());
			IvResponse response = new IvResponse(); 
			response.setErrorCode("-1");
			response.setErrorMessage(e.getMessage());
			return response;
		}
	}
}
