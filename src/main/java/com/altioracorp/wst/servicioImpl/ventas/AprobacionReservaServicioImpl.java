package com.altioracorp.wst.servicioImpl.ventas;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Optional;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.altioracorp.gpintegrator.client.Customer.Customer;
import com.altioracorp.wst.dominio.sistema.PerfilNombre;
import com.altioracorp.wst.dominio.ventas.AprobacionDocumento;
import com.altioracorp.wst.dominio.ventas.Cotizacion;
import com.altioracorp.wst.dominio.ventas.CotizacionEstado;
import com.altioracorp.wst.dominio.ventas.DocumentoEstado;
import com.altioracorp.wst.dominio.ventas.DocumentoReserva;
import com.altioracorp.wst.exception.ventas.SolicitudAprobacionInactivaException;
import com.altioracorp.wst.repositorio.ventas.IAprobacionDocumentoRepositorio;
import com.altioracorp.wst.repositorio.ventas.ICotizacionRepositorio;
import com.altioracorp.wst.repositorio.ventas.IDocumentoReservaRepositorio;
import com.altioracorp.wst.servicio.ventas.IAprobacionReservaServicio;

import com.altioracorp.wst.servicio.ventas.IClienteServicio;
import com.altioracorp.wst.servicio.ventas.ICotizacionHistorialEstadoServicio;
import com.altioracorp.wst.servicio.ventas.IFacturaServicio;
import com.altioracorp.wst.servicio.ventas.IReservaFacturaServicio;
import com.altioracorp.wst.util.UtilidadesSeguridad;

@Service
public class AprobacionReservaServicioImpl implements IAprobacionReservaServicio {

	private static final Log LOG = LogFactory.getLog(AprobacionReservaServicioImpl.class);
	
	@Autowired
	private IAprobacionDocumentoRepositorio aprobacionRepositorio;
	
	@Autowired
	private ICotizacionRepositorio cotizacionRepositorio;
	
	@Autowired
	private IDocumentoReservaRepositorio reservaRepositorio;
	
	@Autowired
	private ICotizacionHistorialEstadoServicio aprobacionHistorialDocumentoServicio;

	
	@Autowired
	private IReservaFacturaServicio reservaFacturaServicio;
	
	@Autowired
	private IFacturaServicio facturaServicio;
	
	@Autowired
	private IClienteServicio clienteServicio;
	
	@Override
	@Transactional(readOnly = false, propagation = Propagation.REQUIRED)
	public void procesarSolicitud(AprobacionDocumento aprobacionDocumento) {
		LOG.info(String.format("Respondiendo Solicitud de Aprobacion para Reserva: %s , con estado ; %s", aprobacionDocumento.getReserva().getNumero(), aprobacionDocumento.getEstado()));
		
		Optional<AprobacionDocumento> solicitud = aprobacionRepositorio.findByIdAndActivoTrue(aprobacionDocumento.getId());
		if(solicitud.isEmpty()) {
			throw new SolicitudAprobacionInactivaException();
		}else {
			
			solicitud.get().setEstado(aprobacionDocumento.getEstado());
			solicitud.get().setActivo(Boolean.FALSE);
			solicitud.get().setUsuario(UtilidadesSeguridad.usuarioEnSesion());
			//aprobacionRepositorio.save(solicitud.get());			
			this.actualizarDocumentos(solicitud.get());
		}		
	}

	@Override
	@Transactional(readOnly = false, propagation = Propagation.REQUIRED)
	public void lanzarSolictud(Long reservaID) {		
		Optional<DocumentoReserva> reserva = reservaRepositorio.findById(reservaID);
		if(reserva.isPresent()) {
			LOG.info(String.format("Enviando Solicitud de Aprobacion ReservaFactura: %s cotizacion", reserva.get().getCotizacion().getNumero()));	
			reserva.get().setEstado(DocumentoEstado.REVISION);
			AprobacionDocumento aprobarDocumento = new  AprobacionDocumento();
			aprobarDocumento.setFormaPago(reserva.get().getCotizacion().getControles().isFormaPago());
			aprobarDocumento.setPrecioProducto(reserva.get().getCotizacion().getControles().isPrecioProducto());
			aprobarDocumento.setDescuentoFijo(reserva.get().getCotizacion().getControles().isDescuentoFijo());
			aprobarDocumento.setDescripcionArticulo(reserva.get().getCotizacion().getControles().isDescripcionArticulo());
			aprobarDocumento.setDocumentoSoportePendiente(reserva.get().getCotizacion().getControles().isDocumentoSoportePendiente());
			aprobarDocumento.setDocumentosVencidos(reserva.get().getCotizacion().getControles().isDocumentosVencidos());
			aprobarDocumento.setCreditoCerrado(reserva.get().getCotizacion().getControles().isCreditoCerrado());
			aprobarDocumento.setExcesoLimiteCredito(reserva.get().getCotizacion().getControles().isExcesoLimiteCredito());
			aprobarDocumento.setReservaStock(reserva.get().getCotizacion().getControles().isReservaStock());
			aprobarDocumento.setReservaTransito(reserva.get().getCotizacion().getControles().isReservaTransito());
			aprobarDocumento.setCotizacion(reserva.get().getCotizacion());
			aprobarDocumento.setCambioCondicionPagoProveedor(reserva.get().getCotizacion().getControles().isCambioCondicionPagoProveedor());
			aprobarDocumento.setCambioMargenUtilidad(reserva.get().getCotizacion().getControles().isCambioMargenUtilidad());
			aprobarDocumento.setReserva(reserva.get());
			
			if(reserva.get().getCotizacion().getControles().isExcesoLimiteCredito()) {
				
				Customer cliente = clienteServicio.obtenerCustomerPorCurstomerNumbre(reserva.get().getCotizacion().getCodigoCliente());
				
				BigDecimal creditoDisponible = clienteServicio.calcularCreditoDisponibleWST(cliente.getCustnmbr(), cliente.getCrlmtamt());
				
				BigDecimal exesoCredito = BigDecimal.ZERO;
				if(cliente.getCrlmtamt().compareTo(BigDecimal.ZERO) == 0) {
					exesoCredito = new BigDecimal(100);
				}else {
					exesoCredito = (creditoDisponible.abs().divide(cliente.getCrlmtamt(), RoundingMode.HALF_UP))
							.multiply(new BigDecimal(100));
				}
				
				LOG.info(String.format("Exceso de limite de credito: %s", exesoCredito));
				
				if(exesoCredito.compareTo(new BigDecimal(30)) == 1) {
					aprobarDocumento.setPerfil(PerfilNombre.APROBADOR_COMERCIAL);
				}
				if(exesoCredito.compareTo(new BigDecimal(30)) == 0 || exesoCredito.compareTo(new BigDecimal(30)) == -1  ) {
					
					if(reserva.get().getCotizacion().getControles().soloEsExecesoLimiteCredito())
						aprobarDocumento.setPerfil(PerfilNombre.APROBADOR_CREDITO_Y_COBRANZAS);
					else
						aprobarDocumento.setPerfil(PerfilNombre.APROBADOR_COMERCIAL);
				}
				
			}else {
				aprobarDocumento.setPerfil(PerfilNombre.APROBADOR_COMERCIAL);
			}
			
			LOG.info(String.format("Enviando la solicitud  %s", aprobarDocumento));
			aprobacionRepositorio.save(aprobarDocumento);	
		}	
	}
	
	private void actualizarDocumentos(AprobacionDocumento solicitud) {
		switch (solicitud.getEstado()) {
		case APROBAR:
			this.actualizarReserva(solicitud.getReserva(), DocumentoEstado.APROBADO);
			this.actualizarCotizacionControles(solicitud.getReserva().getCotizacion());
			reservaFacturaServicio.procesarReservaFactura(solicitud.getReserva().getId());
			break;
		case REGRESAR:
			this.actualizarReserva(solicitud.getReserva(), DocumentoEstado.NUEVO);
			break;
		case RECHAZAR:
			this.actualizarCotizacion(solicitud.getCotizacion(), CotizacionEstado.RECHAZADO, solicitud.getObservacion());
			this.actualizarReserva(solicitud.getReserva(), DocumentoEstado.RECHAZADO);
			facturaServicio.liberarReservasArticulosYCompartimientos(solicitud.getReserva().getDetalle());
			break;
		default:
			break;
		}
	}
	
	private void actualizarCotizacionControles(Cotizacion cotizacion) {
		Optional<Cotizacion> cotizacionRecargado = cotizacionRepositorio.findById(cotizacion.getId());
		if(cotizacionRecargado.isPresent()) {
			cotizacionRecargado.get().setFechaVencimiento(cotizacion.getFechaVencimiento());
			cotizacionRecargado.get().getControles().aprobarControles();
		}
	}
	
	private void actualizarCotizacion(Cotizacion cotizacion, CotizacionEstado estado, String observacion) {
		Optional<Cotizacion> cotizacionRecargado = cotizacionRepositorio.findById(cotizacion.getId());
		if(cotizacionRecargado.isPresent()) {
			cotizacionRecargado.get().setEstado(estado);
			cotizacionRecargado.get().setFechaVencimiento(cotizacion.getFechaVencimiento());
			cotizacionRecargado.get().getControles().aprobarControles();
			aprobacionHistorialDocumentoServicio.registrar(cotizacion, observacion);
		}
	}
	
	private void actualizarReserva(DocumentoReserva reserva, DocumentoEstado estado) {
		Optional<DocumentoReserva> reservaRecargado = reservaRepositorio.findById(reserva.getId());
		if(reservaRecargado.isPresent()) {
			reservaRecargado.get().setEstado(estado);
			reservaRecargado.get().setFechaVencimiento(reserva.getFechaVencimiento());
		}
		
	}

}
