package com.altioracorp.wst.controlador.sistema;

import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.altioracorp.wst.dominio.sistema.AsignacionBodega;
import com.altioracorp.wst.dominio.sistema.ConfiguracionUsuarioPerfil;
import com.altioracorp.wst.servicio.sistema.IAsignacionBodegaServicio;

@RestController
@RequestMapping("/asignacion-bodega")
public class AsignacionBodegaControlador {
	
	@Autowired
	private IAsignacionBodegaServicio servicio;
	
	
	@PostMapping("/listar-config-perfil")
	public ResponseEntity<List<AsignacionBodega>> listarPorConfiguracionUsuarioPerfil(@RequestBody ConfiguracionUsuarioPerfil config) {
		List<AsignacionBodega> configuracion = servicio.buscarPorConfiguracionUsuarioPerfil(config);
		return new ResponseEntity<List<AsignacionBodega>>(configuracion, HttpStatus.OK);
	}
	
	@PostMapping
	public ResponseEntity<AsignacionBodega> registrar(@Valid @RequestBody AsignacionBodega area) {
		AsignacionBodega obj = servicio.registrar(area);
		return new ResponseEntity<AsignacionBodega>(obj, HttpStatus.CREATED);
	}
	
	@PutMapping
	public ResponseEntity<List<AsignacionBodega>> modificar(@Valid @RequestBody List<AsignacionBodega> asignacionesBodega) {
		List<AsignacionBodega> obj = servicio.modificarLista(asignacionesBodega);
		return new ResponseEntity<List<AsignacionBodega>>(obj, HttpStatus.OK);
	}

}
