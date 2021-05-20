package com.altioracorp.wst.servicioImpl.ventas;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.altioracorp.gpintegrator.client.Customer.Customer;
import com.altioracorp.wst.dominio.ventas.AprobacionDocumento;
import com.altioracorp.wst.dominio.ventas.Cotizacion;
import com.altioracorp.wst.dominio.ventas.DocumentoBase;
import com.altioracorp.wst.dominio.ventas.DocumentoFactura;
import com.altioracorp.wst.dominio.ventas.DocumentoReserva;
import com.altioracorp.wst.dominio.ventas.dto.CotizacionControles;
import com.altioracorp.wst.repositorio.ventas.IAprobacionDocumentoRepositorio;
import com.altioracorp.wst.repositorio.ventas.ICotizacionRepositorio;
import com.altioracorp.wst.servicio.cobros.ICondicionCobroFacturaServicio;
import com.altioracorp.wst.servicio.compras.IComprasServicio;
import com.altioracorp.wst.servicio.ventas.IClienteServicio;

@Component
public class ValidarDocumento {

	@Autowired
	private IClienteServicio clienteServicio;

	@Autowired
	private ICotizacionRepositorio cotizacionRepo;
	
	@Autowired
	private ICondicionCobroFacturaServicio condicionFacturaRepo;
	
	@Autowired
	private IAprobacionDocumentoRepositorio aprobacionDocumentoRepo;
	
	@Autowired
	private IComprasServicio comprasServicio;

	private static final Log LOG = LogFactory.getLog(ValidarDocumento.class);

	private DocumentoBase documento;
	private Customer cliente;
	private List<String> validaciones;

	//TODO: Eliminar si no se usa.
	public List<String> validarFactura(DocumentoFactura factura) {
		LOG.info(String.format("Inicia Proceso de Validación Documento %s", factura.getId()));
		documento = factura;
		validaciones = new ArrayList<>();
		obtenerCliente(factura.getCotizacion().getCodigoCliente());
		validar();
		return validaciones;

	}
	
	public List<String> validarReserva(DocumentoReserva reserva) {
		LOG.info(String.format("Inicia Proceso de Validación Documento %s", reserva.getNumero()));
		documento = reserva;
		validaciones = new ArrayList<>();
		obtenerCliente(reserva.getCotizacion().getCodigoCliente());
		validar();
		return validaciones;

	}
	
	public List<String> validarCotizacion(Cotizacion cotizacion) {
		LOG.info(String.format("Inicia Proceso de Validación Cotizacion %s", cotizacion.getNumero()));
		validaciones = new ArrayList<>();
		obtenerCliente(cotizacion.getCodigoCliente());
		validarControlesCotizacion(cotizacion);
		crearListaValidaciones(cotizacion.getControles());
		return validaciones;

	}

	private void validarControlesCotizacion(Cotizacion cotizacionValidar) {
		LOG.info(String.format("Validar controles cotización %s", cotizacionValidar.getNumero()));
		boolean precioProducto = cotizacionValidar.getDetalle().stream()
				.anyMatch(x -> x.getPrecio().compareTo(x.getPrecioGP()) != 0);
		
		boolean descuentoFijo = cotizacionValidar.getDetalle().stream()
				.anyMatch(x -> x.getDescuentoFijo().compareTo(x.getDescuentoFijoGP()) != 0);
		
		boolean descripcionArticulo = cotizacionValidar.getDetalle().stream()
				.anyMatch(x -> !x.getDescripcionArticulo().equalsIgnoreCase(x.getDescripcionArticuloGP()));
		
		boolean cambioCondicionPagoProveedor = comprasServicio.existeCambioCondicionPagoProveedorPorCotizacionid(cotizacionValidar.getId());
		
		boolean cambioMargenUtilidad = comprasServicio.existeCambioMargenUtilidadDetallePorCotizacionId(cotizacionValidar.getId());
		
		boolean condicionPago = Boolean.FALSE;
		if(!cotizacionValidar.getCondicionPago().equalsIgnoreCase("CONTADO")) {
			condicionPago = cotizacionValidar.getCondicionPago() != null ? !cotizacionValidar.getCondicionPago().equalsIgnoreCase(cliente.getPymtrmid()) : false;
		}				
		
		CotizacionControles controles =cotizacionValidar.getControles();
		if(controles == null) {
			controles = new CotizacionControles();
			controles.setPrecioProducto(precioProducto);
			controles.setFormaPago(condicionPago);
			controles.setDescuentoFijo(descuentoFijo);
			controles.setDescripcionArticulo(descripcionArticulo);
			controles.setCambioCondicionPagoProveedor(cambioCondicionPagoProveedor);
			controles.setCambioMargenUtilidad(cambioMargenUtilidad);
		}else {
			controles.setPrecioProducto(precioProducto);
			controles.setFormaPago(condicionPago);
			controles.setDescuentoFijo(descuentoFijo);
			controles.setDescripcionArticulo(descripcionArticulo);
			controles.setCambioCondicionPagoProveedor(cambioCondicionPagoProveedor);
			controles.setCambioMargenUtilidad(cambioMargenUtilidad);
		}
		actualizarControlesCotizacion(cotizacionValidar,controles);
	}

	private void obtenerCliente(String codigoCliente) {
		cliente = clienteServicio.obtenerCustomerPorCurstomerNumbre(codigoCliente);
	}

	private void validarControlesFactura() {
		LOG.info(String.format("Validar controles Factura de la cotización %s",
				this.documento.getCotizacion().getNumero()));
		boolean documentoSoportePendienteEntrega = Boolean.FALSE;
		boolean documentosVencidos = condicionFacturaRepo.existenCodicionesCobroVencidasPorCliente(cliente.getCustnmbr());
		
		boolean excesoLimiteCredito = Boolean.FALSE;		
		if(!this.documento.getCotizacion().getCondicionPago().equalsIgnoreCase("CONTADO")) {
			excesoLimiteCredito = clienteServicio.calcularCreditoDisponible(cliente.getCustnmbr())
			.subtract(documento.getCotizacion().getTotal()).compareTo(BigDecimal.ZERO) <= 0;
		}
		
		boolean creditoCerrado = cliente.isShipcomplete();
		CotizacionControles controles = documento.getCotizacion().getControles();
		if(controles == null) {
			controles = new CotizacionControles();
			controles.setDocumentoSoportePendiente(documentoSoportePendienteEntrega);
			controles.setDocumentosVencidos(documentosVencidos);
			controles.setExcesoLimiteCredito(excesoLimiteCredito);
			controles.setCreditoCerrado(creditoCerrado);
			
		}else {
			controles.setDocumentoSoportePendiente(documentoSoportePendienteEntrega);
			controles.setDocumentosVencidos(documentosVencidos);
			controles.setExcesoLimiteCredito(excesoLimiteCredito);
			controles.setCreditoCerrado(creditoCerrado);
		}
		actualizarControlesCotizacion(documento.getCotizacion(),controles);
	}

	private void validar() {

//		switch (documento.getCotizacion().getEstado()) {
//		case NUEVO:
//			validarControlesCotizacion();
//			validarControlesFactura();
//			break;
//
//		case EMITIDO:
//			validarControlesFactura();
//			break;
//
//		case APROBADO:
//			validarControlesFactura();
//			break;
//		case POR_FACTURAR:
//			validarControlesFactura();
//		default:
//			break;
//		}
		
		if(verificarCotizacionPasoPorAprobacion()) {
			validarControlesFactura();
		}else {
			validarControlesCotizacion(documento.getCotizacion());
			validarControlesFactura();
		}
		
		this.crearListaValidaciones(documento.getCotizacion().getControles());
	}
	
	private boolean verificarCotizacionPasoPorAprobacion() {
		List<AprobacionDocumento> solicitudesAprobacion = aprobacionDocumentoRepo.findByCotizacion_id(this.documento.getCotizacion().getId());
		if(solicitudesAprobacion.stream().count() > 0) {
			LOG.info(String.format("Verificando Cotizacion %s paso por aprobacion: SI", documento.getCotizacion().getNumero()));
			return true;
		}else {
			LOG.info(String.format("Verificando Cotizacion %s paso por aprobacion: NO", documento.getCotizacion().getNumero()));
			return false;
		}
	}

	private void actualizarControlesCotizacion(Cotizacion cotizacionValidar ,CotizacionControles controles) {

		Cotizacion cotizacion = cotizacionValidar;
		if (cotizacion.getControles() == null) {
			cotizacion.setControles(controles);
		} else {
			long idControles = cotizacion.getControles().getId();
			cotizacion.setControles(controles);
			cotizacion.getControles().setId(idControles);
		}
		cotizacionValidar = cotizacionRepo.save(cotizacion);
	}

	private void crearListaValidaciones(CotizacionControles controles) {
		if (controles.isFormaPago())
			validaciones.add("cambio en la condición de pago");
		if (controles.isPrecioProducto())
			validaciones.add("cambio en el precio de los artículos");
		if (controles.isDescuentoFijo())
			validaciones.add("cambio en el descuento fijo de los artículos");
		if (controles.isDescripcionArticulo())
			validaciones.add("cambio en la descripción de los artículos");
		if (controles.isDocumentoSoportePendiente())
			validaciones.add("tiene documento(s) soporte pendiente de entrega");
		if (controles.isDocumentosVencidos())
			validaciones.add("existen facturas anteriores sin documento soporte entregado");
		if (controles.isExcesoLimiteCredito())
			validaciones.add("no se tiene un crédito disponible");
		if (controles.isCreditoCerrado())
			validaciones.add("crédito cerrado");
		if (controles.isCambioMargenUtilidad())
			validaciones.add("cambio en margen de utilidad");
		if (controles.isCambioCondicionPagoProveedor())
			validaciones.add("cambio en condición de pago a proveedor");

	}

}
