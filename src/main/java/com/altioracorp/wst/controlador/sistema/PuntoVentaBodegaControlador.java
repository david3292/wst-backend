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

import com.altioracorp.wst.dominio.sistema.Bodega;
import com.altioracorp.wst.dominio.sistema.PuntoVenta;
import com.altioracorp.wst.dominio.sistema.PuntoVentaBodega;
import com.altioracorp.wst.servicio.sistema.IPuntoVentaBodegaServicio;

@RestController
@RequestMapping("/pvta-bodegas")
public class PuntoVentaBodegaControlador {

	@Autowired
	private IPuntoVentaBodegaServicio servicio;
	
	@GetMapping("/pvta/{id}")
	public ResponseEntity<List<Bodega>> listarBodegasPorPuntoVenta( @PathVariable Long id){
		PuntoVenta ptv= new PuntoVenta();
		ptv.setId(id);
		List<Bodega> puntosVenta = servicio.buscarBodegasPorPuntoVenta(ptv);
		return new ResponseEntity<List<Bodega>>(puntosVenta, HttpStatus.OK);
	}
	
	@GetMapping("/listarPorPvta/{id}")
	public ResponseEntity<List<PuntoVentaBodega>> listarBodegasConfiguradas( @PathVariable Long id){
		PuntoVenta ptv= new PuntoVenta();
		ptv.setId(id);
		List<PuntoVentaBodega> bodegasConfiguradas = servicio.listarBodegasPorPuntoVenta(ptv);
		return new ResponseEntity<List<PuntoVentaBodega>>(bodegasConfiguradas, HttpStatus.OK);
	}
	
	@PostMapping
	public ResponseEntity<PuntoVentaBodega> registrar(@RequestBody PuntoVentaBodega puntoVenta){
		PuntoVentaBodega puntoVentaBodegaCreado = servicio.registrar(puntoVenta);
		return new ResponseEntity<PuntoVentaBodega>(puntoVentaBodegaCreado, HttpStatus.CREATED);
	}
	
	@PutMapping
	public ResponseEntity<PuntoVentaBodega> modificar(@Valid @RequestBody PuntoVentaBodega puntoVenta){
		PuntoVentaBodega puntoVentaBodegaActualizado = servicio.modificar(puntoVenta);
		return new ResponseEntity<PuntoVentaBodega>(puntoVentaBodegaActualizado, HttpStatus.OK);
	}
	
	@PostMapping("/buscarBodegaPrincipal")
	public ResponseEntity<Bodega> buscarBodegasPrincipal( @RequestBody PuntoVenta puntoVenta){
		Bodega bodega = servicio.buscarBodegaPrincipalPorPuntoVenta(puntoVenta);
		return new ResponseEntity<Bodega>(bodega, HttpStatus.OK);
	}
}
