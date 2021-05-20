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

import com.altioracorp.wst.dominio.ldap.UserDetailsVO;
import com.altioracorp.wst.dominio.sistema.PerfilNombre;
import com.altioracorp.wst.dominio.sistema.PuntoVenta;
import com.altioracorp.wst.dominio.sistema.Usuario;
import com.altioracorp.wst.servicio.sistema.IUsuarioServicio;

@RestController
@RequestMapping("/usuarios")
public class UsuarioControlador {

	@Autowired
	private IUsuarioServicio servicio;
	
	@GetMapping
	public ResponseEntity<List<Usuario>> listarUsuarios() {
		List<Usuario> lista = servicio.listarTodos();
		return new ResponseEntity<List<Usuario>>(lista, HttpStatus.OK);
	}
	
	@GetMapping("/{id}")
	public ResponseEntity<Usuario> listarPorId(@PathVariable("id") Long id) {
		Usuario usuario = servicio.listarPorId(id);
		return new ResponseEntity<Usuario>(usuario, HttpStatus.OK);
	}
	
	@PostMapping
	public ResponseEntity<Usuario> registrar(@Valid @RequestBody Usuario usuarioSistema) {
		Usuario obj = servicio.registrar(usuarioSistema);
		return new ResponseEntity<Usuario>(obj, HttpStatus.CREATED);
	}
	
	@PutMapping
	public ResponseEntity<Usuario> modificar(@Valid @RequestBody Usuario usuarioSistema) {
		Usuario obj = servicio.modificar(usuarioSistema);
		return new ResponseEntity<Usuario>(obj, HttpStatus.OK);
	}
	
	@GetMapping("/usuarioSesion")
	public ResponseEntity<UserDetailsVO> obtenerUsuarioDetalleEnSesion() {
		UserDetailsVO usuarioDetalle = servicio.obtenerDetalleUsuarioEnSesion();
		return new ResponseEntity<UserDetailsVO>(usuarioDetalle, HttpStatus.OK);
	}

	@PostMapping("/seleccionarPV")
	public ResponseEntity<Object> registrar(@Valid @RequestBody PuntoVenta puntoVenta) {
		servicio.seleccionarPuntoVenta(puntoVenta);
		return new ResponseEntity<Object>("ok", HttpStatus.CREATED);
	}

	@GetMapping("/perfilVendedor")
	public ResponseEntity<List<Usuario>> listarPorPerfilVendedor() {
		final List<Usuario> usuarios = this.servicio.listarPorPerfilVendedor();
		return new ResponseEntity<>(usuarios, HttpStatus.OK);
	}
	
	@GetMapping("/porPerfiil/{perfil}")
	public ResponseEntity<List<Usuario>> listarPorPerfil(@PathVariable("perfil") PerfilNombre perfil) {
		final List<Usuario> usuarios = this.servicio.listarPorPerfil(perfil);
		return new ResponseEntity<>(usuarios, HttpStatus.OK);
	}

}
