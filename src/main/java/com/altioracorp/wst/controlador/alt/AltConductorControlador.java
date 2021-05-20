package com.altioracorp.wst.controlador.alt;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.altioracorp.gpintegrator.client.miscellaneous.AltConductor;
import com.altioracorp.wst.servicio.alt.IAltConductorServicio;

@RestController()
@RequestMapping("/conductores")
public class AltConductorControlador {

	@Autowired
	private IAltConductorServicio conductorServicio;
	
	@GetMapping
	public ResponseEntity<List<AltConductor>> listarConductores(){
		List<AltConductor> listaConductores = conductorServicio.listarConductores();
		return new ResponseEntity<List<AltConductor>>(listaConductores, HttpStatus.OK);
	}
	
}
