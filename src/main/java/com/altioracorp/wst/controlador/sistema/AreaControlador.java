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
import com.altioracorp.wst.servicio.sistema.IAreaServicio;

@RestController
@RequestMapping("/areas")
public class AreaControlador {
	
	@Autowired
	private IAreaServicio servicio;
	
	@GetMapping
	public ResponseEntity<List<Area>> listarAreas() {
		List<Area> lista = servicio.listarTodos();
		return new ResponseEntity<List<Area>>(lista, HttpStatus.OK);
	}
	
	@GetMapping("/activos")
	public ResponseEntity<List<Area>> listarAreasActivos() {
		List<Area> lista = servicio.buscarTodosActivos();
		return new ResponseEntity<List<Area>>(lista, HttpStatus.OK);
	}
	
	@GetMapping("/{id}")
	public ResponseEntity<Area> listarPorId(@PathVariable("id") Long id) {
		Area configuracion = servicio.listarPorId(id);
		return new ResponseEntity<Area>(configuracion, HttpStatus.OK);
	}
	
	@PostMapping
	public ResponseEntity<Area> registrar(@Valid @RequestBody Area area) {
		Area obj = servicio.registrar(area);
		return new ResponseEntity<Area>(obj, HttpStatus.CREATED);
	}
	
	@PutMapping
	public ResponseEntity<Area> modificar(@Valid @RequestBody Area area) {
		Area obj = servicio.modificar(area);
		return new ResponseEntity<Area>(obj, HttpStatus.OK);
	}

}
