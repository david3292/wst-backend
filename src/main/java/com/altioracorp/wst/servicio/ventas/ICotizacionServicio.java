package com.altioracorp.wst.servicio.ventas;

import java.time.LocalDateTime;
import java.util.List;

import com.altioracorp.gpintegrator.client.Item.Item;
import com.altioracorp.wst.dominio.ventas.Cotizacion;
import com.altioracorp.wst.dominio.ventas.CotizacionDetalle;
import com.altioracorp.wst.dominio.ventas.CotizacionEstado;
import com.altioracorp.wst.servicio.ICRUD;

public interface ICotizacionServicio extends ICRUD<Cotizacion,Long> {

	List<Cotizacion> listarPorUsuarioYPuntoVenta(Long puntoVentaID);
	
	byte[] cotizarDocumento(Cotizacion documento);
	
	void enviarDocumentoAprobar(Cotizacion documento);
	
	byte[] imprimirDocumento(Long id);
	
	Cotizacion recotizarDocumento(Long idDocumento);
	
	boolean anularDocumento(Cotizacion documento);
	
	void cambiarEstadoCotizacion(long cotizacionID, CotizacionEstado estado, String observacion);
	
	Cotizacion agregarLineaDetalle(Cotizacion cotizacion, List<Item> articulo );
	
	CotizacionDetalle modificarLineaDetalle(long cotizacionID, CotizacionDetalle detalle);
	
	boolean eliminarLineaDetalle(long cotizacionID, long detalleID);
	
	LocalDateTime calcularFechaVigenciaCotizacion();
	
	List<String> validarCotizacion(long cotizaiconID);
	
	void cambiarEstadoAFacturado(long cotizacionid);
	
	void liberarReservaComprasL(long cotizacionId);
	
	void editarOrdenCliente(long cotizacionId, String valor);
	
}
