package com.altioracorp.wst.controlador.ventas;


import java.util.List;
import java.util.Map;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.altioracorp.wst.dominio.ventas.Cotizacion;
import com.altioracorp.wst.dominio.ventas.CotizacionDetalle;
import com.altioracorp.wst.dominio.ventas.dto.LineaCotizacionDetalleDTO;
import com.altioracorp.wst.servicio.ventas.ICotizacionServicio;

@RestController
@RequestMapping("/cotizaciones")
public class CotizacionControlador {

	@Autowired
	private ICotizacionServicio servicio;
	
	@GetMapping("/{id}")
	public ResponseEntity<Cotizacion> listarPorId(@PathVariable("id") Long id) {
		Cotizacion cotizacion = servicio.listarPorId(id);
		return new ResponseEntity<Cotizacion>(cotizacion, HttpStatus.OK);
	}
	
	@GetMapping("/usuario/{puntoVentaID}")
	public ResponseEntity<List<Cotizacion>> listarPorUsuarioYPuntoVenta(@PathVariable("puntoVentaID") Long puntoVentaID) {
		List<Cotizacion> cotizacion = servicio.listarPorUsuarioYPuntoVenta(puntoVentaID);
		return new ResponseEntity<List<Cotizacion>>(cotizacion, HttpStatus.OK);
	}
	
	@PostMapping
	public ResponseEntity<Cotizacion> registrar(@Valid @RequestBody Cotizacion cotizacion) {
		Cotizacion obj = servicio.registrar(cotizacion);
		return new ResponseEntity<Cotizacion>(obj, HttpStatus.CREATED);
	}
	
	@PutMapping
	public ResponseEntity<Cotizacion> modificar(@Valid @RequestBody Cotizacion cotizacion) {
		Cotizacion obj = servicio.modificar(cotizacion);
		return new ResponseEntity<Cotizacion>(obj, HttpStatus.OK);
	}
	
	@PostMapping("/agregarLineaDetalle")
	public ResponseEntity<Cotizacion> agregarLineaDetalle( @RequestBody LineaCotizacionDetalleDTO linea) {
		Cotizacion obj = servicio.agregarLineaDetalle(linea.getCotizacion(), linea.getArticulos());
		return new ResponseEntity<Cotizacion>(obj, HttpStatus.CREATED);
	}
	
	@PostMapping("modificarLineaDetalle")
	public ResponseEntity<CotizacionDetalle> modificarLineaDetalle( @RequestBody LineaCotizacionDetalleDTO linea) {
		CotizacionDetalle obj = servicio.modificarLineaDetalle(linea.getCotizacion().getId(), linea.getLinea());
		return new ResponseEntity<CotizacionDetalle>(obj, HttpStatus.CREATED);
	}
	
	@DeleteMapping("/eliminarLineaDetalle/{cotizacionID}/{detalleID}")
	public ResponseEntity<Object> eliminarLineaDetalle(@PathVariable("cotizacionID") Long cotizacionID, @PathVariable("detalleID") Long detalleID) {
		Boolean obj = servicio.eliminarLineaDetalle(cotizacionID, detalleID);
		return new ResponseEntity<Object>(obj, HttpStatus.CREATED);
	}
	
	@PostMapping(value="/cotizarDocumento", produces = MediaType.APPLICATION_OCTET_STREAM_VALUE)
	public ResponseEntity<byte[]> cotizarDocumento(@Valid @RequestBody Cotizacion cotizacion) {
		byte [] data = null;
		data= servicio.cotizarDocumento(cotizacion);
		return new ResponseEntity<byte[]>(data, HttpStatus.CREATED);
	}
	
	@GetMapping(value="/imprimir/{id}", produces = MediaType.APPLICATION_OCTET_STREAM_VALUE)
	public ResponseEntity<byte[]> cotizarDocumento(@PathVariable("id") Long id) {
		byte [] data = null;
		data= servicio.imprimirDocumento(id);
		return new ResponseEntity<byte[]>(data, HttpStatus.CREATED);
	}
	
	@PostMapping("/solicitudAprobar")
	public ResponseEntity<Object> enviarDocumentoAprobar(@Valid @RequestBody Cotizacion documento) {
		servicio.enviarDocumentoAprobar(documento);
		return new ResponseEntity<Object>(Boolean.TRUE, HttpStatus.CREATED);
	}
	
	@GetMapping("/recotizarDocumento/{id}")
	public ResponseEntity<Cotizacion> listarPorUsuario(@PathVariable("id") Long id) {
		Cotizacion cotizacion = servicio.recotizarDocumento(id);
		return new ResponseEntity<Cotizacion>(cotizacion, HttpStatus.OK);
	}
	
	@PostMapping(value="/anularDocumento")
	public ResponseEntity<Object> anularDocumento(@Valid @RequestBody Cotizacion cotizacion) {
		boolean respuesta= servicio.anularDocumento(cotizacion);
		return new ResponseEntity<Object>(respuesta, HttpStatus.OK);
	}
	
	@GetMapping("/validarCotizacion/{id}")
	public ResponseEntity<List<String>> validarCotizacion(@PathVariable("id") Long cotizacionID) {
		List<String> resumen = servicio.validarCotizacion(cotizacionID);
		return new ResponseEntity<List<String>>(resumen, HttpStatus.OK);
	}
	
	@GetMapping("/obtenerInfoGuiaRemision/{guiaRemisionId}")
	public ResponseEntity<Map<String, Object>> obtenerInfoGuiaRemision(@PathVariable("guiaRemisionId")long guiaRemisionId){
		
		return null;
	}
	
	@PutMapping("/editar/oc")
	public ResponseEntity<Object> editarOrdenCliente(@RequestBody Cotizacion cotizacion) {
		servicio.editarOrdenCliente(cotizacion.getId(), cotizacion.getOrdenCliente());
		return new ResponseEntity<>( HttpStatus.OK);
	}
}
