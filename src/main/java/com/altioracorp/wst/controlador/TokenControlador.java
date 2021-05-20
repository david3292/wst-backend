package com.altioracorp.wst.controlador;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.oauth2.provider.token.ConsumerTokenServices;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/tokens")
public class TokenControlador {

	@Autowired
	private ConsumerTokenServices tokenServices;
	
	
	//Se usa una expresión irregular tokenId:.*, para poder enviar el token en la uri
		@GetMapping("/anular/{tokenId:.*}")
		public void  revocarToken(@PathVariable("tokenId") String token) {
			//Elimina el token es decir elmina de la tabla de la bbdd si lo usaran
			tokenServices.revokeToken(token);
		}
	
}
