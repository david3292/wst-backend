package com.altioracorp.wst.controlador.sistema;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.altioracorp.wst.dominio.sistema.Menu;
import com.altioracorp.wst.dominio.sistema.UsuarioMenuDTO;
import com.altioracorp.wst.dominio.sistema.UsuarioPerfil;
import com.altioracorp.wst.servicio.sistema.IMenuServicio;

@RestController
@RequestMapping("/menus")
public class MenuControlador {

	@Autowired
	private IMenuServicio servicio;
	
	
	@PostMapping(value = "/usuario", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<List<Menu>> listar(@RequestBody String nombre) {
		List<Menu> menus = new ArrayList<>();
		menus = servicio.listarMenuPorUsuario(nombre);
		return new ResponseEntity<List<Menu>>(menus, HttpStatus.OK);
	}
	
	@PostMapping(value = "/usuario_menu", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<UsuarioMenuDTO> listarUsuarioMenu(@RequestBody UsuarioPerfil usuarioPerfil) {
		UsuarioMenuDTO dto = servicio.listarUsuarioMenuPorUsuario(usuarioPerfil);
		return new ResponseEntity<UsuarioMenuDTO>(dto, HttpStatus.OK);
	}
	
}
