package com.altioracorp.wst.controlador.compras;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.altioracorp.wst.dominio.compras.OrdenCompra;
import com.altioracorp.wst.dominio.compras.RecepcionCompra;
import com.altioracorp.wst.dominio.compras.RecepcionCompraDetalle;
import com.altioracorp.wst.dominio.compras.dto.ArticuloCompraDTO;
import com.altioracorp.wst.dominio.sistema.PuntoVenta;
import com.altioracorp.wst.dominio.ventas.Cotizacion;
import com.altioracorp.wst.dominio.ventas.dto.OrdenCompraDTO;
import com.altioracorp.wst.dominio.ventas.dto.RecepcionCompraDTO;
import com.altioracorp.wst.servicio.compras.IComprasServicio;

@RestController
@RequestMapping("/compras")
public class OrdenCompraControlador {

	@Autowired
	private IComprasServicio comprasServicio;
	
	@PostMapping("/validarCompra")
	ResponseEntity<ArticuloCompraDTO> verificarOrdenCompra(@RequestBody ArticuloCompraDTO articuloCompra){
		ArticuloCompraDTO articuloCompraR = this.comprasServicio.verificarOrdenCompra(articuloCompra);
		return ResponseEntity.ok(articuloCompraR);
	}
	
	@PostMapping("/crearActualizarArticuloCompra")
	public ResponseEntity<Map<String, Object>> crearActualizarArticuloCompra(@RequestBody ArticuloCompraDTO articuloCompra){
		Map<String, Object> response = comprasServicio.crearActualizarArticuloCompra(articuloCompra);
		return ResponseEntity.ok(response);
	}
	
	@GetMapping("/aprobarCompras/{cotizacionId}")
	public ResponseEntity<Map<String, String>> aprobarComprasPorCotizacionId(@PathVariable("cotizacionId")long cotizacionId){
		comprasServicio.aprobarOrdenesCompraPorCotizacionId(cotizacionId);
		Map<String, String> respuesta = new HashMap<String, String>();
		respuesta.put("mensaje", "OK");
		return ResponseEntity.ok(respuesta);
	}
	
	@PostMapping("/integrar/{ordenCompraId}")
	public ResponseEntity<OrdenCompra> integrarOrdenCompra(@PathVariable long ordenCompraId){
		OrdenCompra ordenCompra = comprasServicio.integrarOrdenCompra(ordenCompraId);
		return ResponseEntity.ok(ordenCompra);
	}
	
	@PostMapping("/integrar/recepcion/{recepcionCompraId}")
	public ResponseEntity<Map<String, Object>> integrarRecepcionOrdenCompra(@PathVariable long recepcionCompraId){
		Map<String, Object> recepcionCompra = comprasServicio.integrarRecepcionOrdenCompra(recepcionCompraId);
		return ResponseEntity.ok(recepcionCompra);
	}
	
	@PostMapping("/integrarPorCotizacion/{cotizacionId}")
	public ResponseEntity<Map<String, String>> integrarOrdenCompraPorCotizacion(@PathVariable long cotizacionId){
		Cotizacion cotizacion = new Cotizacion();
		cotizacion.setId(cotizacionId);
		Map<String, String> respuesta = comprasServicio.lanzarIntegracionesOrdenCompra(cotizacion);
		return ResponseEntity.ok(respuesta);
	}
	
	@GetMapping("/reporte/{ordenCompraId}")
	public ResponseEntity<byte[]> reporteOrdenCompra(@PathVariable long ordenCompraId){
		byte[] reporte = comprasServicio.generarReporteOrdenCompra(ordenCompraId);
		return ResponseEntity.ok(reporte);
	}
	
	@GetMapping("/reporteRecepcion/{recepcionCompraId}")
	public ResponseEntity<byte[]> reporteRecepcionCompra(@PathVariable long recepcionCompraId){
		byte[] reporte = comprasServicio.generarReporteRecepcionCompra(recepcionCompraId);
		return ResponseEntity.ok(reporte);
	}
	
	@GetMapping("/listarPorCotizacion/{cotizacionId}")
	public ResponseEntity<List<OrdenCompraDTO>> listarOrdenesCompraPorCotizacionId(@PathVariable("cotizacionId")long cotizacionId){
		List<OrdenCompraDTO> ordenes = comprasServicio.listarOrdenesCompraDtoPorCotizacionId(cotizacionId);
		return ResponseEntity.ok(ordenes);
	}
	
	@GetMapping("/listarPorCotizacion/recepciones/{cotizacionId}")
	public ResponseEntity<List<RecepcionCompraDTO>> listarRecepcionesCompraPorCotizacionId(@PathVariable("cotizacionId")long cotizacionId){
		List<RecepcionCompraDTO> recepciones = comprasServicio.listarRecepcionesPorCotizacionId(cotizacionId);
		return ResponseEntity.ok(recepciones);
	}
	
	@PostMapping("/comprasError")
	public ResponseEntity<List<OrdenCompraDTO>> listarComprasError(@RequestBody PuntoVenta puntoVenta){
		List<OrdenCompraDTO> ordenes = comprasServicio.listarComprasError(puntoVenta);
		return ResponseEntity.ok(ordenes);
	}
	
	@PostMapping("/recepcionesError")
	public ResponseEntity<List<RecepcionCompraDTO>> listarRecepcionesError(@RequestBody PuntoVenta puntoVenta){
		List<RecepcionCompraDTO> recepciones = comprasServicio.listarRecepcionesError(puntoVenta);
		return ResponseEntity.ok(recepciones);
	}
	
	@PostMapping("/recepciones/overview")
	public ResponseEntity<List<OrdenCompraDTO>> listarOrdenesCompraParaRecepcion(@RequestBody Map<String, Long> consulta){
		List<OrdenCompraDTO> ordenesCompra = comprasServicio.listarOrdenesCompraEmitidas(consulta);
		return ResponseEntity.ok(ordenesCompra);
	}
	
	@GetMapping("/ordenCompra/{id}")
	public ResponseEntity<OrdenCompra> obtenerOrdenCompraPorId(@PathVariable("id") long id){
		OrdenCompra ordenCompra = comprasServicio.obtenerOrdenCompraPorId(id);
		return ResponseEntity.ok(ordenCompra);
	}
	
	@PostMapping("/actualizar/recepcionDetalle")
	public ResponseEntity<RecepcionCompra> actualizarCantidadRecepcion(@RequestBody RecepcionCompraDetalle detalle) {
		RecepcionCompra recepcionCompra = comprasServicio.actualizarCantidadRecepcion(detalle);
		return ResponseEntity.ok(recepcionCompra);
	}
	
	@GetMapping("/listarRecepciones/{ordenCompraId}")
	public ResponseEntity<List<RecepcionCompra>> listarRecepcionesCompras(@PathVariable("ordenCompraId") long ordenCompraId){
		List<RecepcionCompra> recepciones = comprasServicio.obtenerRecepcionesOrdenesCompraPorId(ordenCompraId);
		return ResponseEntity.ok(recepciones);
	}
	
	@GetMapping("/recepcionCompra/{recepcionCompraId}")
	public ResponseEntity<RecepcionCompra> obtenerRecepcionCompraPorId(@PathVariable("recepcionCompraId") long recepcionCompraId){
		RecepcionCompra recepcionCompra = comprasServicio.obtenerRecepcionComopraPorId(recepcionCompraId);
		return ResponseEntity.ok(recepcionCompra);
	}
	
	@GetMapping("/nuevaRecepcionCompra/{ordenCompraId}")
	public ResponseEntity<RecepcionCompra> crearNuevaRecepcion(@PathVariable("ordenCompraId") long ordenCompraId){
		RecepcionCompra recepcionCompra = comprasServicio.crearNuevaRecepcionCompra(ordenCompraId);
		return ResponseEntity.ok(recepcionCompra);
	}
	
	@GetMapping("/cerrarProcesoCompra/{recepcionCompraId}")
	public ResponseEntity<RecepcionCompra> cerrarProcesoCompra(@PathVariable("recepcionCompraId") long recepcionCompraId){
		RecepcionCompra recepcioncompra = comprasServicio.cerrarProcesoCompra(recepcionCompraId);
		return ResponseEntity.ok(recepcioncompra);
	}
	
	@GetMapping("/detallesAprobacion/{cotizacionId}")
	public ResponseEntity<Map<String, Object>> obtenerOrdenesCompraDetallesAprobacion(@PathVariable("cotizacionId") long cotizacionId){
		Map<String, Object> detalles = comprasServicio.obtenerOrdenesCompraDetallesAprobacion(cotizacionId);
		return ResponseEntity.ok(detalles);
	}
	
	@GetMapping("/validarCantidadesOrdenCompra/{cotizacionId}")
	public ResponseEntity<Map<String, Object>> validarCantidadesOrdenCompra(@PathVariable("cotizacionId") long cotizacionId){
		Map<String, Object> respuesta = comprasServicio.validarCantidadesOrdenesCompraPorCotizacionId(cotizacionId);
		return ResponseEntity.ok(respuesta);
	}
}
