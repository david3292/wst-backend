package com.wst;

import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import com.altioracorp.wst.dominio.sistema.Usuario;
import com.altioracorp.wst.repositorio.sistema.IUsuarioRepositorio;

@SpringBootTest
class WstBackendApplicationTests {

	@Autowired
	private BCryptPasswordEncoder bcrypt;
	
	@Autowired
	private IUsuarioRepositorio repo;
	
	@Test
	void contextLoads() {
		
		Usuario us = new Usuario();
		us.setId((long) 3);
//		us.setUsuarioNombre("test");
//		us.setContrasena(bcrypt.encode("123"));
//		us.setActivo(true);
		
		Usuario retorno = repo.save(us);
		assertTrue(retorno.getContrasena().equalsIgnoreCase(retorno.getContrasena()));
	}

}
