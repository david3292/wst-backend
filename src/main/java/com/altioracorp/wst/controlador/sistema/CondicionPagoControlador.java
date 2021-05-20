package com.altioracorp.wst.controlador.sistema;

import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.altioracorp.wst.dominio.sistema.CondicionPago;
import com.altioracorp.wst.servicio.sistema.ICondicionPagoServicio;

@RestController
@RequestMapping("/condiciones-pago")
public class CondicionPagoControlador {
	
	@Autowired
	private ICondicionPagoServicio servicio;
	
	@GetMapping
	public ResponseEntity<List<CondicionPago>> listarCondicionesPago() {
		List<CondicionPago> lista = servicio.listarTodos();
		return new ResponseEntity<List<CondicionPago>>(lista, HttpStatus.OK);
	}
	
	@GetMapping("/activos")
	public ResponseEntity<List<CondicionPago>> listarCondicionesPagoActivos() {
		List<CondicionPago> lista = servicio.buscarTodosActivos();
		return new ResponseEntity<List<CondicionPago>>(lista, HttpStatus.OK);
	}
	
	@GetMapping("/{id}")
	public ResponseEntity<CondicionPago> listarPorId(@PathVariable("id") Long id) {
		CondicionPago configuracion = servicio.listarPorId(id);
		return new ResponseEntity<CondicionPago>(configuracion, HttpStatus.OK);
	}
	
	@GetMapping("/catalogoTipoPago")
	public ResponseEntity<List<CatalogoDTO>> listarCatalogoTipoPago() {
		List<CatalogoDTO> catalogo = servicio.crarCatalogoTipoPago();
		return new ResponseEntity<List<CatalogoDTO>>(catalogo, HttpStatus.OK);
	}
	
	@PostMapping
	public ResponseEntity<CondicionPago> registrar(@Valid @RequestBody CondicionPago area) {
		CondicionPago obj = servicio.registrar(area);
		return new ResponseEntity<CondicionPago>(obj, HttpStatus.CREATED);
	}
	
	@PutMapping
	public ResponseEntity<CondicionPago> modificar(@Valid @RequestBody CondicionPago area) {
		CondicionPago obj = servicio.modificar(area);
		return new ResponseEntity<CondicionPago>(obj, HttpStatus.OK);
	}

}
