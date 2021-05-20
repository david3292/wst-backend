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
import com.altioracorp.wst.dominio.ventas.DocumentoFactura;
import com.altioracorp.wst.exception.ventas.SolicitudAprobacionInactivaException;
import com.altioracorp.wst.repositorio.ventas.IAprobacionDocumentoRepositorio;
import com.altioracorp.wst.repositorio.ventas.ICotizacionRepositorio;
import com.altioracorp.wst.repositorio.ventas.IDocumentoFacturaRepositorio;
import com.altioracorp.wst.servicio.ventas.IAprobacionFacturaServicio;
import com.altioracorp.wst.servicio.ventas.IClienteServicio;
import com.altioracorp.wst.servicio.ventas.ICotizacionHistorialEstadoServicio;
import com.altioracorp.wst.servicio.ventas.IFacturaServicio;
import com.altioracorp.wst.util.UtilidadesSeguridad;

@Service
public class AprobacionFacturaServicioImpl implements IAprobacionFacturaServicio{

	private static final Log LOG = LogFactory.getLog(AprobacionFacturaServicioImpl.class);
	
	@Autowired
	private IAprobacionDocumentoRepositorio aprobacionRepositorio;
	
	@Autowired
	private ICotizacionRepositorio cotizacionRepositorio;
	
	@Autowired
	private IDocumentoFacturaRepositorio facturaRepositorio;
	
	@Autowired
	private IFacturaServicio facturaServicio;
	
	@Autowired
	private ICotizacionHistorialEstadoServicio aprobacionHistorialDocumentoServicio;
	
	@Autowired
	private IClienteServicio clienteServicio;

	
	@Override
	@Transactional(readOnly = false, propagation = Propagation.REQUIRED)
	public void procesarSolicitud(AprobacionDocumento aprobacionDocumento) {
		LOG.info(String.format("Respondiendo Solicitud de Aprobacion para Documento: %s , con estado ; %s", aprobacionDocumento.getFactura().getNumero(), aprobacionDocumento.getEstado()));
		
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
	@Transactional
	public void lanzarSolictud(Long facturaID) {
		Optional<DocumentoFactura> factura = facturaRepositorio.findById(facturaID);
		if(factura.isPresent()) {
			LOG.info(String.format("Enviando Solicitud de Aprobacion Factura: %s cotizacion", factura.get().getCotizacion().getNumero()));	
			factura.get().setEstado(DocumentoEstado.REVISION);
			AprobacionDocumento aprobarDocumento = new  AprobacionDocumento();
			aprobarDocumento.setFormaPago(factura.get().getCotizacion().getControles().isFormaPago());
			aprobarDocumento.setPrecioProducto(factura.get().getCotizacion().getControles().isPrecioProducto());
			aprobarDocumento.setDescuentoFijo(factura.get().getCotizacion().getControles().isDescuentoFijo());
			aprobarDocumento.setDescripcionArticulo(factura.get().getCotizacion().getControles().isDescripcionArticulo());
			aprobarDocumento.setDocumentoSoportePendiente(factura.get().getCotizacion().getControles().isDocumentoSoportePendiente());
			aprobarDocumento.setDocumentosVencidos(factura.get().getCotizacion().getControles().isDocumentosVencidos());
			aprobarDocumento.setCreditoCerrado(factura.get().getCotizacion().getControles().isCreditoCerrado());
			aprobarDocumento.setExcesoLimiteCredito(factura.get().getCotizacion().getControles().isExcesoLimiteCredito());
			aprobarDocumento.setReservaStock(factura.get().getCotizacion().getControles().isReservaStock());
			aprobarDocumento.setReservaTransito(factura.get().getCotizacion().getControles().isReservaTransito());
			aprobarDocumento.setCotizacion(factura.get().getCotizacion());
			aprobarDocumento.setCambioCondicionPagoProveedor(factura.get().getCotizacion().getControles().isCambioCondicionPagoProveedor());
			aprobarDocumento.setCambioMargenUtilidad(factura.get().getCotizacion().getControles().isCambioMargenUtilidad());
			aprobarDocumento.setFactura(factura.get());
			
			if(factura.get().getCotizacion().getControles().isExcesoLimiteCredito()) {
				
				Customer cliente = clienteServicio.obtenerCustomerPorCurstomerNumbre(factura.get().getCotizacion().getCodigoCliente());
				
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
					
					if(factura.get().getCotizacion().getControles().soloEsExecesoLimiteCredito())
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
			this.actualizarFactura(solicitud.getFactura(), DocumentoEstado.APROBADO);
			this.actualizarCotizacionControles(solicitud.getFactura().getCotizacion());
			//facturaServicio.procesarFactura(solicitud.getFactura().getId());
			break;
		case REGRESAR:
			this.actualizarFactura(solicitud.getFactura(), DocumentoEstado.NUEVO);
			break;
		case RECHAZAR:
			this.actualizarCotizacion(solicitud.getCotizacion(), CotizacionEstado.RECHAZADO, solicitud.getObservacion());
			this.actualizarFactura(solicitud.getFactura(), DocumentoEstado.RECHAZADO);
			facturaServicio.liberarReservasArticulosYCompartimientos(solicitud.getFactura().getDetalle());
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
	
	private void actualizarFactura(DocumentoFactura factura, DocumentoEstado estado) {
		Optional<DocumentoFactura> facturaRecargado = facturaRepositorio.findById(factura.getId());
		if(facturaRecargado.isPresent()) {
			facturaRecargado.get().setEstado(estado);
			facturaRecargado.get().setFechaVencimiento(factura.getFechaVencimiento());
		}
		
	}

}
