package com.altioracorp.wst.controlador.sistema;

import com.altioracorp.wst.dominio.sistema.PuntoVenta;
import com.altioracorp.wst.servicio.sistema.IPuntoVentaServicio;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/puntos-venta")
public class PuntoVentaControlador {

	@Autowired
	private IPuntoVentaServicio puntoVentaServicio;
	
	@GetMapping
	public ResponseEntity<List<PuntoVenta>> listarPuntosVenta(){
		List<PuntoVenta> puntosVenta = puntoVentaServicio.listarTodos();
		return new ResponseEntity<List<PuntoVenta>>(puntosVenta, HttpStatus.OK);
	}
	
	@GetMapping("/activos")
	public ResponseEntity<List<PuntoVenta>> listarPuntosVentaActivos(){
		List<PuntoVenta> puntosVenta = puntoVentaServicio.listarTodosActivos();
		return new ResponseEntity<List<PuntoVenta>>(puntosVenta, HttpStatus.OK);
	}
	
	@GetMapping("/{id}")
	public ResponseEntity<PuntoVenta> buscarPorId(@PathVariable Long id){
		PuntoVenta puntoVenta = puntoVentaServicio.listarPorId(id);
		return new ResponseEntity<PuntoVenta>(puntoVenta, HttpStatus.OK);
	}
	
	@PostMapping
	public ResponseEntity<PuntoVenta> registrar(@RequestBody PuntoVenta puntoVenta) {
		PuntoVenta puntoVentaCreado = puntoVentaServicio.registrar(puntoVenta);
		return new ResponseEntity<PuntoVenta>(puntoVentaCreado, HttpStatus.CREATED);
	}

	@PutMapping
	public ResponseEntity<PuntoVenta> modificar(@Valid @RequestBody PuntoVenta puntoVenta) {
		PuntoVenta puntoVentaActualizado = puntoVentaServicio.modificar(puntoVenta);
		return new ResponseEntity<PuntoVenta>(puntoVentaActualizado, HttpStatus.OK);
	}

	/**
	 * M\u00e9todo que consulta los puntos de venta de acuerdo al id de perfil y al usuario en sesi\u00f3n.
	 *
	 * @param idPerfil
	 * @return
	 */
	@GetMapping("/perfil/{idPerfil}")
	public ResponseEntity<List<PuntoVenta>> buscarPorPerfilIdYUsuarioSesion(@PathVariable final Long idPerfil) {
		final List<PuntoVenta> puntosVenta = this.puntoVentaServicio.buscarPorPerfilIdYUsuarioSesion(idPerfil);
		return new ResponseEntity<List<PuntoVenta>>(puntosVenta, HttpStatus.OK);
	}
}
