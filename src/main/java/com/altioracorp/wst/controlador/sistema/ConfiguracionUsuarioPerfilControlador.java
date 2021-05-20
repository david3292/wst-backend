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

import com.altioracorp.wst.dominio.sistema.ConfiguracionUsuarioPerfil;
import com.altioracorp.wst.dominio.sistema.PerfilNombre;
import com.altioracorp.wst.dominio.sistema.PuntoVenta;
import com.altioracorp.wst.dominio.sistema.Usuario;
import com.altioracorp.wst.dominio.sistema.UsuarioPerfil;
import com.altioracorp.wst.servicio.sistema.IConfiguracionUsuarioPerfilServicio;

@RestController
@RequestMapping("/configuraciones-usuario-perfil")
public class ConfiguracionUsuarioPerfilControlador {
	
	@Autowired
	private IConfiguracionUsuarioPerfilServicio servicio;
	
	@GetMapping("/usuarioperfil/{id}")
	public ResponseEntity<List<ConfiguracionUsuarioPerfil>> listarConfiguracionesPorUsuarioPerfil(@PathVariable("id") Long id) {
		UsuarioPerfil usuarioPerfil = new UsuarioPerfil();
		usuarioPerfil.setId(id);
		List<ConfiguracionUsuarioPerfil> lista = servicio.buscarPorUsuarioPerfil(usuarioPerfil);
		return new ResponseEntity<List<ConfiguracionUsuarioPerfil>>(lista, HttpStatus.OK);
	}
	
	@PostMapping("/usuario")
	public ResponseEntity<List<ConfiguracionUsuarioPerfil>> listarConfiguracionesPorUsuario(@RequestBody Usuario usuario) {
		List<ConfiguracionUsuarioPerfil> lista = servicio.buscarPorUsuario(usuario);
		return new ResponseEntity<List<ConfiguracionUsuarioPerfil>>(lista, HttpStatus.OK);
	}
	
	@GetMapping("/{id}")
	public ResponseEntity<ConfiguracionUsuarioPerfil> listarPorId(@PathVariable("id") Long id) {
		ConfiguracionUsuarioPerfil configuracion = servicio.listarPorId(id);
		return new ResponseEntity<ConfiguracionUsuarioPerfil>(configuracion, HttpStatus.OK);
	}
	
	@PostMapping
	public ResponseEntity<ConfiguracionUsuarioPerfil> registrar(@Valid @RequestBody ConfiguracionUsuarioPerfil configuracionUsuarioPerfil) {
		ConfiguracionUsuarioPerfil obj = servicio.registrar(configuracionUsuarioPerfil);
		return new ResponseEntity<ConfiguracionUsuarioPerfil>(obj, HttpStatus.CREATED);
	}
	
	@PutMapping
	public ResponseEntity<ConfiguracionUsuarioPerfil> modificar(@Valid @RequestBody ConfiguracionUsuarioPerfil configuracionUsuarioPerfil) {
		ConfiguracionUsuarioPerfil obj = servicio.modificar(configuracionUsuarioPerfil);
		return new ResponseEntity<ConfiguracionUsuarioPerfil>(obj, HttpStatus.OK);
	}
	
	@DeleteMapping("/{id}")
	public ResponseEntity<Object> eliminar(@PathVariable("id") Long id) {
		servicio.eliminar(id);
		return new ResponseEntity<Object>(HttpStatus.NO_CONTENT);
	}
	
	@GetMapping("/puntosVentas/{perfil}")
	public ResponseEntity<List<PuntoVenta>> listarPuntosDeVentaPorUsuarioEnSesionYPerfil(@PathVariable("perfil") PerfilNombre perfil ) {
		List<PuntoVenta> lista = servicio.buscarPuntosDeVentasPorUsuarioEnSesionYPerfil(perfil);
		return new ResponseEntity<List<PuntoVenta>>(lista, HttpStatus.OK);
	}
	
	@GetMapping("/puntosVentasPorUsuario/{usuario}/{perfil}")
	public ResponseEntity<List<PuntoVenta>> listarPuntosDeVentaPorUsuarioEnSesionYPerfil(@PathVariable("usuario") String usuario, @PathVariable("perfil") PerfilNombre perfil ) {
		List<PuntoVenta> lista = servicio.buscarPuntosDeVentasPorUsuarioYPerfil(usuario, perfil);
		return new ResponseEntity<List<PuntoVenta>>(lista, HttpStatus.OK);
	}

}
