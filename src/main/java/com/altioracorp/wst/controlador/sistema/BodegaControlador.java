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

import com.altioracorp.gpintegrator.client.SiteSetup.SiteSetup;
import com.altioracorp.wst.dominio.sistema.Bodega;
import com.altioracorp.wst.servicio.sistema.IBodegaServicio;

@RestController
@RequestMapping("/bodegas")
public class BodegaControlador {

	@Autowired
	private IBodegaServicio bodegaServicio; 
	
	@GetMapping
	public ResponseEntity<List<Bodega>> listarTodos(){
		List<Bodega> bodegas = bodegaServicio.listarTodos();
		return new ResponseEntity<List<Bodega>>(bodegas, HttpStatus.OK);
	}
	
	@GetMapping("/listargp")
	public ResponseEntity<List<SiteSetup>> listarTodosGp(){
		List<SiteSetup> bodegas = bodegaServicio.listarBodegasGP();
		return new ResponseEntity<List<SiteSetup>>(bodegas, HttpStatus.OK);
	}
	
	@GetMapping("/activos")
	public ResponseEntity<List<Bodega>> listarTodosActivos(){
		List<Bodega> bodegas = bodegaServicio.listarTodosActivos();
		return new ResponseEntity<List<Bodega>>(bodegas, HttpStatus.OK);
	}
	
	@GetMapping("/{id}")
	public ResponseEntity<Bodega> listarPorId(@PathVariable("id") Long id){
		Bodega bodega = bodegaServicio.listarPorId(id);
		return new ResponseEntity<Bodega>(bodega, HttpStatus.OK);
	}
	
	@PostMapping
	public ResponseEntity<Bodega> registrarBodega(@Valid @RequestBody Bodega bodega){
		Bodega bodegaCreada = bodegaServicio.registrar(bodega);
		return new ResponseEntity<Bodega>(bodegaCreada, HttpStatus.CREATED);
	}
	
	@PutMapping
	public ResponseEntity<Bodega> modificarBodega(@Valid @RequestBody Bodega bodega){
		Bodega bodegaModificada = bodegaServicio.modificar(bodega);
		return new ResponseEntity<Bodega>(bodegaModificada, HttpStatus.OK);
	}
	
	@GetMapping("/SiteSetup/{locnCode}")
	public ResponseEntity<SiteSetup> listarPorLocnCode(@PathVariable("locnCode")String locnCode){
		SiteSetup siteSetup = bodegaServicio.listarBodegaPorLocnCode(locnCode);
		return new ResponseEntity<SiteSetup>(siteSetup, HttpStatus.OK);
	}
	
}
