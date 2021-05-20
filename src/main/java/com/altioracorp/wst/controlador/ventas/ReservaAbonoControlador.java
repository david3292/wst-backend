package com.altioracorp.wst.controlador.ventas;

import java.time.LocalDateTime;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.altioracorp.wst.dominio.ventas.DocumentoReserva;
import com.altioracorp.wst.servicio.ventas.IReservaAbonoServicio;

@RestController
@RequestMapping("/reservaciones/abono")
public class ReservaAbonoControlador {

	@Autowired
	private IReservaAbonoServicio servicio;
	
	@GetMapping(value="/abono", produces = MediaType.TEXT_PLAIN_VALUE)
	public ResponseEntity<Object> obtenerPorcentajeAbono() {
		double abono = servicio.calcularPorcentajeAbono();
		return new ResponseEntity<Object>(String.valueOf(abono), HttpStatus.OK);
	}
	
	@GetMapping(value="/fechaMaximaReserva", produces = MediaType.TEXT_PLAIN_VALUE)
	public ResponseEntity<Object> obtenerFechaMaximaReserva() {
		LocalDateTime fecha = servicio.calcularFechaMaximaReserva();
		return new ResponseEntity<Object>(fecha.toString(), HttpStatus.OK);
	}	
	
	@PostMapping("/registrar")
	public ResponseEntity<DocumentoReserva> registrar(@RequestBody DocumentoReserva dto) {
		DocumentoReserva reserva = servicio.guardarReserva(dto);
		return new ResponseEntity<DocumentoReserva>(reserva, HttpStatus.CREATED);
	}
	
	@PostMapping("/enviarAprobar")
	public ResponseEntity<Object> enviarAporbar(@RequestBody DocumentoReserva dto) {
		servicio.enviarAprobarReserva(dto);
		return new ResponseEntity<Object>(Boolean.TRUE, HttpStatus.CREATED);
	}
	
	@GetMapping("/listarPorCotizacion/{id}")
	public ResponseEntity<DocumentoReserva> obtenerFechaMaximaReserva(@PathVariable("id") Long cotizacionID) {
		DocumentoReserva reserva = servicio.listarPorCotizacion(cotizacionID);
		return new ResponseEntity<DocumentoReserva>(reserva, HttpStatus.OK);
	}	
	
}
