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

import com.altioracorp.wst.dominio.sistema.Area;
import com.altioracorp.wst.dominio.sistema.Cargo;
import com.altioracorp.wst.servicio.sistema.ICargoServicio;

@RestController
@RequestMapping("/cargos")
public class CargoControlador {
	
	@Autowired
	private ICargoServicio servicio;
	
	@GetMapping
	public ResponseEntity<List<Cargo>> listarCargos() {
		List<Cargo> lista = servicio.listarTodos();
		return new ResponseEntity<List<Cargo>>(lista, HttpStatus.OK);
	}
	
	@GetMapping("/activos")
	public ResponseEntity<List<Cargo>> listarCargosActivos() {
		List<Cargo> lista = servicio.buscarTodosActivos();
		return new ResponseEntity<List<Cargo>>(lista, HttpStatus.OK);
	}
	
	@GetMapping("/{id}")
	public ResponseEntity<Cargo> listarPorId(@PathVariable("id") Long id) {
		Cargo configuracion = servicio.listarPorId(id);
		return new ResponseEntity<Cargo>(configuracion, HttpStatus.OK);
	}
	
	@GetMapping("/area/{id}")
	public ResponseEntity<List<Cargo>> listarPorAreaIdYActivo(@PathVariable("id") Long id) {
		Area area = new Area();
		area.setId(id);
		List<Cargo> cargos = servicio.buscarPorAreaYActivos(area);
		return new ResponseEntity<List<Cargo>>(cargos, HttpStatus.OK);
	}
	
	@PostMapping
	public ResponseEntity<Cargo> registrar(@Valid @RequestBody Cargo area) {
		Cargo obj = servicio.registrar(area);
		return new ResponseEntity<Cargo>(obj, HttpStatus.CREATED);
	}
	
	@PutMapping
	public ResponseEntity<Cargo> modificar(@Valid @RequestBody Cargo area) {
		Cargo obj = servicio.modificar(area);
		return new ResponseEntity<Cargo>(obj, HttpStatus.OK);
	}

}
