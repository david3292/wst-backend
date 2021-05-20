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

import com.altioracorp.wst.dominio.sistema.FormaPago;
import com.altioracorp.wst.servicio.sistema.IFormaPagoServicio;

@RestController
@RequestMapping("/formasPago")
public class FormaPagoControlador {
	
	@Autowired
	private IFormaPagoServicio servicio;
	
	@GetMapping("/{id}")
	public ResponseEntity<FormaPago> listarPorId(@PathVariable("id") Long id) {
		FormaPago fpago = servicio.listarPorId(id);
		return new ResponseEntity<FormaPago>(fpago, HttpStatus.OK);
	}
	
	@GetMapping()
	public ResponseEntity<List<FormaPago>> listar() {
		List<FormaPago> fpago = servicio.listarTodos();
		return new ResponseEntity<List<FormaPago>>(fpago, HttpStatus.OK);
	}
	
	@GetMapping("/activos")
	public ResponseEntity<List<FormaPago>> listarActivos() {
		List<FormaPago> fpago = servicio.listarActivos();
		return new ResponseEntity<List<FormaPago>>(fpago, HttpStatus.OK);
	}
	
	@GetMapping("/catalogo")
	public ResponseEntity<List<CatalogoDTO>> listarCatalogoFormaPagoNombre() {
		List<CatalogoDTO> catalogo = servicio.listarCatalogoFormaPagoNombre();
		return new ResponseEntity<List<CatalogoDTO>>(catalogo, HttpStatus.OK);
	}
	
	@PostMapping
	public ResponseEntity<FormaPago> registrar(@Valid @RequestBody FormaPago fpago) {
		FormaPago obj = servicio.registrar(fpago);
		return new ResponseEntity<FormaPago>(obj, HttpStatus.CREATED);
	}
	
	@PutMapping
	public ResponseEntity<FormaPago> modificar(@Valid @RequestBody FormaPago fpago) {
		FormaPago obj = servicio.modificar(fpago);
		return new ResponseEntity<FormaPago>(obj, HttpStatus.OK);
	}
	
}
