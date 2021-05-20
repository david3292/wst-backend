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

import com.altioracorp.gpintegrator.client.CheckBook.CheckBook;
import com.altioracorp.wst.dominio.sistema.FormaPagoPuntoVenta;
import com.altioracorp.wst.dominio.sistema.PuntoVenta;
import com.altioracorp.wst.servicio.sistema.IFPagoPvtaServicio;

@RestController
@RequestMapping("/formasPagoPuntoVenta")
public class FormaPagoPuntoVentaControlador {

	@Autowired
	private IFPagoPvtaServicio servicio;
	
	@GetMapping("/puntoVenta/{id}")
	public ResponseEntity<List<FormaPagoPuntoVenta>> listarPorPuntoVenta(@PathVariable Long id) {
		PuntoVenta puntoVenta = new PuntoVenta();
		puntoVenta.setId(id);
		List<FormaPagoPuntoVenta> formaPagoPuntoVenta = servicio.listarPorPuntoVenta(puntoVenta);
		return new ResponseEntity<List<FormaPagoPuntoVenta>>(formaPagoPuntoVenta, HttpStatus.OK);
	}
	
	@GetMapping("/puntoVentaActivos/{id}")
	public ResponseEntity<List<FormaPagoPvtaDTO>> listarPorPuntoVentaActivos(@PathVariable Long id) {
		PuntoVenta puntoVenta = new PuntoVenta();
		puntoVenta.setId(id);
		List<FormaPagoPvtaDTO> formaPagoPuntoVenta = servicio.listarActivosPorPuntoVenta(puntoVenta);
		return new ResponseEntity<List<FormaPagoPvtaDTO>>(formaPagoPuntoVenta, HttpStatus.OK);
	}
	
	@PostMapping
	public ResponseEntity<FormaPagoPuntoVenta> registrar(@Valid @RequestBody FormaPagoPuntoVenta formaPagoPuntoVenta) {
		FormaPagoPuntoVenta obj = servicio.registrar(formaPagoPuntoVenta);
		return new ResponseEntity<FormaPagoPuntoVenta>(obj, HttpStatus.CREATED);
	}
	
	@PutMapping
	public ResponseEntity<FormaPagoPuntoVenta> modificar(@Valid @RequestBody FormaPagoPuntoVenta formaPagoPuntoVenta) {
		FormaPagoPuntoVenta obj = servicio.modificar(formaPagoPuntoVenta);
		return new ResponseEntity<FormaPagoPuntoVenta>(obj, HttpStatus.OK);
	}
	
	@GetMapping("/chequerasGP")
	public ResponseEntity<List<CheckBook>> listarChequerasGP() {
		List<CheckBook> chequeras = servicio.listarChequerasGP();
		return new ResponseEntity<List<CheckBook>>(chequeras, HttpStatus.OK);
	}
	
	
}
