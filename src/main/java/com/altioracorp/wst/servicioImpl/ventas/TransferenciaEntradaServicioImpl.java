package com.altioracorp.wst.servicioImpl.ventas;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.altioracorp.gpintegrator.client.inventory.IvResponse;
import com.altioracorp.gpintegrator.client.inventory.IvTransferIntegrator;
import com.altioracorp.wst.dominio.logistica.dto.TransferenciaResumenDTO;
import com.altioracorp.wst.dominio.ventas.DocumentoBase;
import com.altioracorp.wst.dominio.ventas.DocumentoDetalle;
import com.altioracorp.wst.dominio.ventas.DocumentoDetalleCompartimiento;
import com.altioracorp.wst.dominio.ventas.DocumentoEstado;
import com.altioracorp.wst.dominio.ventas.DocumentoReserva;
import com.altioracorp.wst.dominio.ventas.DocumentoTransferencia;
import com.altioracorp.wst.dominio.ventas.DocumentoTransferenciaEntrada;
import com.altioracorp.wst.dominio.ventas.DocumentoTransferenciaSalida;
import com.altioracorp.wst.exception.logistica.TransferenciaCompartimientosIncompletosException;
import com.altioracorp.wst.exception.logistica.TransferenciaNoEncontrada;
import com.altioracorp.wst.exception.ventas.DocumentoGPException;
import com.altioracorp.wst.repositorio.ventas.IDocumentoTransferenciaSalidaRepositorio;
import com.altioracorp.wst.servicio.notificaciones.INotificacionServicio;
import com.altioracorp.wst.servicio.ventas.IArticuloServicio;
import com.altioracorp.wst.servicio.ventas.ICompartimientoServicio;
import com.altioracorp.wst.servicio.ventas.IReservaFacturaServicio;
import com.altioracorp.wst.servicio.ventas.ITransferenciaEntradaServicio;
import com.altioracorp.wst.servicio.ventas.TransferenciaComunes;

@Service
public class TransferenciaEntradaServicioImpl extends TransferenciaComunes implements ITransferenciaEntradaServicio {

	private static final Log LOG = LogFactory.getLog(TransferenciaEntradaServicioImpl.class);

	@Autowired
	private IDocumentoTransferenciaSalidaRepositorio transferenciaSalidaRepositorio;	

	@Autowired
	IReservaFacturaServicio reservaFacturaServicio;

	@Autowired
	private IArticuloServicio articuloServicio;

	@Autowired
	private INotificacionServicio notificacionServicio;
	
	@Autowired
	private ICompartimientoServicio compartimientoServicio;
	

	private void procesarReserva(long transferenciaId, DocumentoReserva reserva) {

		DocumentoTransferencia transferencia = transferenciaRepositorio.findById(transferenciaId).get();
		
		LOG.info(String.format("Inicio Procesar Factura a partir de Transferencia %s con estado %s", transferencia.getNumero(), transferencia.getEstado() ));
		
		List<DocumentoTransferenciaEntrada> transferenciaEntradas = transferenciaEntradaRepositorio
				.findByDocumentoReservaId(reserva.getId());
		
		List<DocumentoTransferenciaSalida> transferenciaSalidas = transferenciaSalidaRepositorio
				.findByDocumentoReservaId(reserva.getId()).stream().filter(x -> !x.getEstado().equals(DocumentoEstado.ANULADO)).collect(Collectors.toList());

		if (transferencia.getEstado().equals(DocumentoEstado.COMPLETADO) && transferenciaSalidas.stream().allMatch(x -> x.getEstado().equals(DocumentoEstado.COMPLETADO)) &&
				transferenciaEntradas.stream().allMatch(x -> x.getEstado().equals(DocumentoEstado.EMITIDO))) {
			
			LOG.info(String.format("Cambiando de estado de Reserva %s", reserva.getNumero()));
			
			reservaFacturaServicio.cambiarEstado(reserva, DocumentoEstado.EN_PROCESO);
			
			//Enviar notificación a vendedor de que la reserva está lista para facturar
			notificacionServicio.notificarReservaListaParaFaturar(reserva);
			
//			DocumentoFactura factura = reservaFacturaServicio.procesarFacturaEnTransferencia(reserva);
//
//			if (factura.getEstado().equals(DocumentoEstado.EMITIDO)) {
//				// enviar correo de factura generada con exito
//				notificacionServicio.notificarFacturaGenerada(factura);
//
//			} else if (factura.getEstado().equals(DocumentoEstado.ERROR)) {
//				// enviar correo de factura generada con error favor comunicarse con sistemas
//				notificacionServicio.notificarFacturaGeneradaError(factura);
//			}
		}
	}

	@Override
	public void procesarTransferenciaEntrada(long transferenciaEntradaId) {

		DocumentoTransferenciaEntrada transferenciaEntrada = transferenciaEntradaRepositorio
				.findById(transferenciaEntradaId).get();

		LOG.info(String.format("Procesando Transferencia de Entrada %s con estado %s", transferenciaEntrada.getNumero(), transferenciaEntrada.getEstado()));
		
		if (transferenciaEntrada.getEstado().equals(DocumentoEstado.EMITIDO)) {
			DocumentoTransferenciaSalida transferenciaSalida = transferenciaSalidaRepositorio
					.findById(transferenciaEntrada.getDocumentoTransferenciaSalidaId()).get();

			DocumentoReserva reserva = reservaFacturaServicio
					.buscarReserva(transferenciaEntrada.getDocumentoReservaId());

			if (reserva != null && reserva.getEstado().equals(DocumentoEstado.EMITIDO)) {

				if (transferenciaSalida.getDetalle().stream()
						.allMatch(x -> x.getSaldo().compareTo(BigDecimal.ZERO) == 0)) {
					
					LOG.info(String.format("Reservando Artículos %s ", gsonLog.toJson(transferenciaEntrada.getDetalle())));
					this.articuloServicio.reservarArticulos(transferenciaEntrada.getDetalle());

					procesarReserva(transferenciaSalida.getDocumentoTransferenciaId(), reserva);

				} else {

					List<DocumentoTransferenciaEntrada> transferenciaEntradas = transferenciaEntradaRepositorio
							.findByDocumentoReservaId(reserva.getId());

					LOG.info(String.format("Liberando %s Transferencias de entradas: %s ",transferenciaEntradas.size(), gsonLog.toJson(transferenciaEntradas)));
					
					transferenciaEntradas.forEach(x -> {
						if (x.getId() != transferenciaEntradaId && x.getEstado().equals(DocumentoEstado.EMITIDO)) {
							articuloServicio.liberarReservasArticulos(x.getDetalle());
						}
					});

					reservaFacturaServicio.anularReserva(reserva);
					
					// Enviar correo porque la transfenrecia de salida no se ingresó completa
					notificacionServicio.notificarTransferenciaIncompleta(transferenciaSalida,reserva);
				}
			}
		}
	}

	@Override
	@Transactional(readOnly = false)
	public TransferenciaResumenDTO integrarTransferenciaEntradaGp(long transferenciaId) {
		TransferenciaResumenDTO transferenciaResumen = new TransferenciaResumenDTO();
		Optional<DocumentoTransferenciaEntrada> transferenciaOpt = transferenciaEntradaRepositorio.findById(transferenciaId);
		if(transferenciaOpt.isEmpty())
			throw new TransferenciaNoEncontrada();		
		
		DocumentoBase transferencia =  transferenciaOpt.get();
		LOG.info(String.format("Inicio de integracion de transferencia de entrada: %s", gsonLog.toJson(transferenciaOpt.get())));
		List<DocumentoEstado> estadosNoIntegrar = Arrays.asList(DocumentoEstado.COMPLETADO, DocumentoEstado.EMITIDO);
		
		if(estadosNoIntegrar.contains(transferencia.getEstado())) {
			transferenciaResumen.setTranferenciaCreada(transferencia.getNumero());			
		}else {
			generarCompartimientosTransferenciaEntrada(transferencia.getDetalle());
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
			LOG.info(String.format("Transferencia Integracion datos: %s", gsonLog.toJson(inventoryIntegrator)));
			IvResponse response = llamarApiIntegracionTransferencia(inventoryIntegrator);
			
			String mensajeError = StringUtils.EMPTY;
			if(StringUtils.isNotBlank(response.getErrorCode()) && response.getErrorCode().equals("0")) {				
				completarTransferenciaIngreso(transferencia);
					
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
	
	@Transactional
	private void generarCompartimientosTransferenciaEntrada(List<DocumentoDetalle> detalles) {
		detalles.parallelStream().forEach(d -> {
			if(CollectionUtils.isEmpty(d.getCompartimientos()))
				d.getCompartimientos().clear();
		});
		
		detalles.forEach(d -> {
			String nombreBin = compartimientoServicio.ObtenerCompartimientoPorCodigoArticuloYBodega(d.getCodigoArticulo(), d.getCodigoBodega());
			DocumentoDetalleCompartimiento compartimiento = new DocumentoDetalleCompartimiento(d.getCantidad(), nombreBin);
			d.agregarCompartimiento(compartimiento);
		});
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
					.filter(det -> det.getCodigoArticulo().equals(d.getCodigoArticulo()))
					.findFirst();
			if(detalleOpt.isPresent())
				d.setSaldo(d.getSaldo().subtract(detalleOpt.get().getCantidad()));
		});
		
		transferenciaSalidaRepositorio.save(transferenciaSalida);

	}
}
