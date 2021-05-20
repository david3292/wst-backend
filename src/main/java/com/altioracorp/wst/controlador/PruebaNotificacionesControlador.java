package com.altioracorp.wst.controlador;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.altioracorp.wst.servicio.notificaciones.INotificacionServicio;

@RestController
@RequestMapping("/pruebas")
public class PruebaNotificacionesControlador {

	@Autowired
	private INotificacionServicio servicio;
	
	@GetMapping("/anularTransferencia")
	public ResponseEntity<Object> anularTransferencia() {
		servicio.probarNotificacion();
		return new ResponseEntity<Object>(Boolean.TRUE,HttpStatus.OK);
	}
	
	
}
