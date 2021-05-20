package com.altioracorp.wst.controlador.sistema;

import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.altioracorp.gpintegrator.client.Receivable.SalesPerson;
import com.altioracorp.wst.dominio.sistema.Usuario;
import com.altioracorp.wst.dominio.sistema.UsuarioPerfil;
import com.altioracorp.wst.servicio.sistema.IUsuarioPerfilServicio;

@RestController
@RequestMapping("/usuario-perfiles")
public class UsuarioPerfilControlador {
	
	@Autowired
	private IUsuarioPerfilServicio servicio;
	
	@GetMapping
	public ResponseEntity<List<UsuarioPerfil>> listarUsuarioPerfiles() {
		List<UsuarioPerfil> lista = servicio.listarTodos();
		return new ResponseEntity<List<UsuarioPerfil>>(lista, HttpStatus.OK);
	}
	
	@GetMapping("/usuario/{id}")
	public ResponseEntity<List<UsuarioPerfil>> listarPerfilesPorUsuario(@PathVariable("id") Long id) {
		Usuario usuario = new Usuario();
		usuario.setId(id);
		List<UsuarioPerfil> lista = servicio.listarPorUsuario(usuario);
		return new ResponseEntity<List<UsuarioPerfil>>(lista, HttpStatus.OK);
	}
	
	@GetMapping("/{id}")
	public ResponseEntity<UsuarioPerfil> listarPorId(@PathVariable("id") Long id) {
		UsuarioPerfil perfil = servicio.listarPorId(id);
		return new ResponseEntity<UsuarioPerfil>(perfil, HttpStatus.OK);
	}
	
	@PostMapping
	public ResponseEntity<UsuarioPerfil> registrar(@Valid @RequestBody UsuarioPerfil area) {
		UsuarioPerfil obj = servicio.registrar(area);
		return new ResponseEntity<UsuarioPerfil>(obj, HttpStatus.CREATED);
	}
	
	@PutMapping
	public ResponseEntity<UsuarioPerfil> modificar(@Valid @RequestBody UsuarioPerfil area) {
		UsuarioPerfil obj = servicio.modificar(area);
		return new ResponseEntity<UsuarioPerfil>(obj, HttpStatus.OK);	
	}
	
	@DeleteMapping("/{id}")
	public ResponseEntity<Object> eliminar(@PathVariable("id") Long id) {
		servicio.eliminar(id);
		return new ResponseEntity<Object>(HttpStatus.NO_CONTENT);
	}
	
	@GetMapping("/codigoVendedores")
	public ResponseEntity<List<SalesPerson>> listarCodigoVendedores() {
		List<SalesPerson> salesPerson = servicio.listarCodigosVendedoresGP();
		return new ResponseEntity<List<SalesPerson>>(salesPerson, HttpStatus.OK);
	}

}
