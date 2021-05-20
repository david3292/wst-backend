package com.altioracorp.wst.controlador.ldap;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.altioracorp.wst.dominio.ldap.UsuarioLdap;
import com.altioracorp.wst.servicioImpl.ldap.ILdapServicio;

@RestController
@RequestMapping("/ldap")
public class LdapControlador {

	@Autowired
	private ILdapServicio servicio;
	
	@GetMapping
	public ResponseEntity<List<UsuarioLdap>> listarUsuariosLdap() {
		List<UsuarioLdap> lista = servicio.findAll();
		return new ResponseEntity<List<UsuarioLdap>>(lista, HttpStatus.OK);
	}
}
