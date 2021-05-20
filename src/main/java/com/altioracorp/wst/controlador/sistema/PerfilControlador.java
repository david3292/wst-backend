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

import com.altioracorp.wst.dominio.sistema.Perfil;
import com.altioracorp.wst.servicio.sistema.IPerfilServicio;

@RestController
@RequestMapping("/perfiles")
public class PerfilControlador {
	
	@Autowired
	private IPerfilServicio servicio;
	
	@GetMapping
	public ResponseEntity<List<Perfil>> listarPerfiles() {
		List<Perfil> lista = servicio.listarTodos();
		return new ResponseEntity<List<Perfil>>(lista, HttpStatus.OK);
	}

	
	@GetMapping("/{id}")
	public ResponseEntity<Perfil> listarPorId(@PathVariable("id") Long id) {
		Perfil perfil = servicio.listarPorId(id);
		return new ResponseEntity<Perfil>(perfil, HttpStatus.OK);
	}
	
	@PostMapping
	public ResponseEntity<Perfil> registrar(@Valid @RequestBody Perfil area) {
		Perfil obj = servicio.registrar(area);
		return new ResponseEntity<Perfil>(obj, HttpStatus.CREATED);
	}
	
	@PutMapping
	public ResponseEntity<Perfil> modificar(@Valid @RequestBody Perfil area) {
		Perfil obj = servicio.modificar(area);
		return new ResponseEntity<Perfil>(obj, HttpStatus.OK);	
	}

}
