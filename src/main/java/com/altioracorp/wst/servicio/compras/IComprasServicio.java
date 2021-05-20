package com.altioracorp.wst.servicio.compras;

import java.util.List;
import java.util.Map;

import com.altioracorp.wst.dominio.compras.OrdenCompra;
import com.altioracorp.wst.dominio.compras.RecepcionCompra;
import com.altioracorp.wst.dominio.compras.RecepcionCompraDetalle;
import com.altioracorp.wst.dominio.compras.dto.ArticuloCompraDTO;
import com.altioracorp.wst.dominio.sistema.PuntoVenta;
import com.altioracorp.wst.dominio.ventas.Cotizacion;
import com.altioracorp.wst.dominio.ventas.DocumentoFactura;
import com.altioracorp.wst.dominio.ventas.dto.OrdenCompraDTO;
import com.altioracorp.wst.dominio.ventas.dto.OrdenCompraDetalleDTO;
import com.altioracorp.wst.dominio.ventas.dto.RecepcionCompraDTO;

public interface IComprasServicio {

	ArticuloCompraDTO verificarOrdenCompra(ArticuloCompraDTO articuloCompra);
	
	Map<String, Object> crearActualizarArticuloCompra(ArticuloCompraDTO articuloCompra);
	
	void aprobarOrdenesCompraPorCotizacionId(long cotizacionId);
	
	OrdenCompra integrarOrdenCompra(long ordenCompraId);
	
	Map<String, Object> integrarRecepcionOrdenCompra(long ordenCompraId);
	
	byte[] generarReporteOrdenCompra(long ordenCompraId);
	
	byte[] generarReporteRecepcionCompra(long recepcionCompraId);
	
	Map<String, String> lanzarIntegracionesOrdenCompra(Cotizacion cotizacion);
	
	void eliminarDetalleOrdenCompraPorDetalleCotizacion(long cotizacionId, String codigoArticulo);
	
	List<OrdenCompraDTO> listarOrdenesCompraDtoPorCotizacionId(long cotizacionId);
	
	List<OrdenCompraDTO> listarComprasError(PuntoVenta puntoVenta);
	
	List<RecepcionCompraDTO> listarRecepcionesError(PuntoVenta puntoVenta);
	
	List<OrdenCompraDTO> listarOrdenesCompraEmitidas(Map<String, Long> consulta);
	
	public OrdenCompra obtenerOrdenCompraPorId(long id);
	
	public Map<String, String> lanzarIntegracionesRecepcionOrdenCompra(Cotizacion cotizacion);
	
	RecepcionCompra actualizarCantidadRecepcion(RecepcionCompraDetalle detalle);
	
	boolean validarArticuloEstaIngresado(DocumentoFactura factura);
	
	List<RecepcionCompra> obtenerRecepcionesOrdenesCompraPorId(long ordenCompraId);
	
	RecepcionCompra obtenerRecepcionComopraPorId(long id);
	
	RecepcionCompra crearNuevaRecepcionCompra(long ordenCompraId);
	
	RecepcionCompra cerrarProcesoCompra(long recepcionCompraId);
	
	boolean existeCambioCondicionPagoProveedorPorCotizacionid(long cotizacionId);
	
	boolean existeCambioMargenUtilidadDetallePorCotizacionId(long cotizacionId);
	
	List<OrdenCompraDTO> ordenesCompraCambioCondicionPagoProveedor(long cotizacionId);
	
	List<OrdenCompraDetalleDTO> detallesOrdenCompraCambioMargenUtilidad(long cotizacionId);
	
	Map<String, Object> obtenerOrdenesCompraDetallesAprobacion(long cotizacionId);
	
	List<RecepcionCompraDTO> listarRecepcionesPorCotizacionId(long cotizacionId);
	
	Map<String, Object> validarCantidadesOrdenesCompraPorCotizacionId(long idCotizacion);
	
	List<OrdenCompra> listarOrdenesCompraPorCotizacionId(long cotizacionId);
	
	void cerrarOrdenesCompraPorCotizacionId(long cotizacionId);
	
	boolean existeOrdenesCompraPorCotizacionId(long cotizacionId);
}
