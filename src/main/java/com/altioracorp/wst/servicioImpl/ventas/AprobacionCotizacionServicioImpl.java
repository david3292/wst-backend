package com.altioracorp.wst.servicioImpl.ventas;

import java.util.Optional;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.altioracorp.wst.dominio.sistema.PerfilNombre;
import com.altioracorp.wst.dominio.ventas.AprobacionDocumento;
import com.altioracorp.wst.dominio.ventas.Cotizacion;
import com.altioracorp.wst.dominio.ventas.CotizacionEstado;
import com.altioracorp.wst.exception.ventas.SolicitudAprobacionInactivaException;
import com.altioracorp.wst.repositorio.ventas.IAprobacionDocumentoRepositorio;
import com.altioracorp.wst.repositorio.ventas.ICotizacionRepositorio;
import com.altioracorp.wst.servicio.ventas.IAprobacionCotizacionServicio;
import com.altioracorp.wst.servicio.ventas.ICotizacionHistorialEstadoServicio;
import com.altioracorp.wst.servicio.ventas.ICotizacionServicio;
import com.altioracorp.wst.util.UtilidadesSeguridad;

@Service
public class AprobacionCotizacionServicioImpl implements IAprobacionCotizacionServicio{

	private static final Log LOG = LogFactory.getLog(AprobacionCotizacionServicioImpl.class);
	
	@Autowired
	private IAprobacionDocumentoRepositorio aprobacionRepositorio;
	
	@Autowired
	private ICotizacionRepositorio cotizacionRepositorio;
	
	@Autowired
	private ICotizacionServicio cotizacionServicio;
	
	@Autowired
	private ICotizacionHistorialEstadoServicio aprobacionHistorialDocumentoServicio;
	
	@Override
	@Transactional(readOnly = false, propagation = Propagation.REQUIRED)
	public void procesarSolicitud(AprobacionDocumento aprobacionDocumento) {
		LOG.info(String.format("Respondiendo Solicitud de Aprobacion para Documento: %s , con estado ; %s", aprobacionDocumento.getCotizacion().getNumero(), aprobacionDocumento.getEstado()));
		
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
	public void lanzarSolictud(Cotizacion cotizacion) {
		LOG.info(String.format("Enviando Solicitud de Aprobacion para Cotizacion: %s", cotizacion.getNumero()));	
		
		AprobacionDocumento aprobarDocumento = new  AprobacionDocumento();
		aprobarDocumento.setFormaPago(cotizacion.getControles().isFormaPago());
		aprobarDocumento.setPrecioProducto(cotizacion.getControles().isPrecioProducto());
		aprobarDocumento.setDescuentoFijo(cotizacion.getControles().isDescuentoFijo());
		aprobarDocumento.setDescripcionArticulo(cotizacion.getControles().isDescripcionArticulo());
		aprobarDocumento.setDocumentoSoportePendiente(cotizacion.getControles().isDocumentoSoportePendiente());
		aprobarDocumento.setDocumentosVencidos(cotizacion.getControles().isDocumentosVencidos());
		aprobarDocumento.setCreditoCerrado(cotizacion.getControles().isCreditoCerrado());
		aprobarDocumento.setExcesoLimiteCredito(cotizacion.getControles().isExcesoLimiteCredito());
		aprobarDocumento.setReservaStock(cotizacion.getControles().isReservaStock());
		aprobarDocumento.setReservaTransito(cotizacion.getControles().isReservaTransito());
		aprobarDocumento.setCotizacion(cotizacion);
		aprobarDocumento.setCambioCondicionPagoProveedor(cotizacion.getControles().isCambioCondicionPagoProveedor());
		aprobarDocumento.setCambioMargenUtilidad(cotizacion.getControles().isCambioMargenUtilidad());
		aprobarDocumento.setPerfil(PerfilNombre.APROBADOR_COMERCIAL);
		LOG.info(String.format("Enviando la solicitud  %s", aprobarDocumento));
		aprobacionRepositorio.save(aprobarDocumento);		
	}
	
	private void actualizarDocumentos(AprobacionDocumento solicitud) {
		switch (solicitud.getEstado()) {
		case APROBAR:
			solicitud.getCotizacion().setFechaVencimiento(cotizacionServicio.calcularFechaVigenciaCotizacion());
			this.actualizarCotizacion(solicitud.getCotizacion(), CotizacionEstado.APROBADO, solicitud.getObservacion());
			//comprasServicio.lanzarIntegracionesOrdenCompra(solicitud.getCotizacion());
			break;
		case REGRESAR:
			this.actualizarCotizacion(solicitud.getCotizacion(), CotizacionEstado.NUEVO, solicitud.getObservacion());
			break;
		case RECHAZAR:	
			this.actualizarCotizacion(solicitud.getCotizacion(), CotizacionEstado.RECHAZADO, solicitud.getObservacion());
			break;
		default:
			break;
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
	
	

}
