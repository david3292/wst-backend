package com.altioracorp.wst.controlador.cobros;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.altioracorp.wst.dominio.cobros.ChequePosfechadoAprobacionDTO;
import com.altioracorp.wst.dominio.cobros.ChequePosfechadoDTO;
import com.altioracorp.wst.dominio.cobros.CobroChequePosfechadoAprobacionDTO;
import com.altioracorp.wst.dominio.cobros.CobroChequePosfechadoDTO;
import com.altioracorp.wst.dominio.cobros.SolicitarReporteChequePosfechadoDTO;
import com.altioracorp.wst.servicio.cobros.IChequePosfechadoServicio;

@RestController
@RequestMapping("/chequesPosfechados")
public class ChequePosfechadoControlador {
	
	@Autowired
    private IChequePosfechadoServicio servicio;
	
	@GetMapping("/estadoRecibido")
    public ResponseEntity<List<ChequePosfechadoDTO>> listarChequesEstadoRecibido() {
        List<ChequePosfechadoDTO> cheques = servicio.obtenerChequesPorEstadoRecibido();
        return new ResponseEntity<List<ChequePosfechadoDTO>>(cheques, HttpStatus.OK);
    }
	
	@GetMapping("/estadoRevision")
    public ResponseEntity<List<ChequePosfechadoDTO>> listarChequesEstadoRevision() {
        List<ChequePosfechadoDTO> cheques = servicio.obtenerChequesPorEstadoRevision();
        return new ResponseEntity<List<ChequePosfechadoDTO>>(cheques, HttpStatus.OK);
    }
	
	@PostMapping("/procesar")
    public ResponseEntity<List<CobroChequePosfechadoDTO>> procesar(@RequestBody List<ChequePosfechadoDTO> chequesAProcesar) {
        List<CobroChequePosfechadoDTO> cheques = servicio.procesar(chequesAProcesar);
        return new ResponseEntity<List<CobroChequePosfechadoDTO>>(cheques, HttpStatus.OK);
    }
	
	@PostMapping("/procesarAprobacion")
    public ResponseEntity<List<CobroChequePosfechadoAprobacionDTO>> procesarAprobacion(@RequestBody ChequePosfechadoAprobacionDTO dto) {
        List<CobroChequePosfechadoAprobacionDTO> cheques = servicio.procesarAprobacion(dto);
        return new ResponseEntity<List<CobroChequePosfechadoAprobacionDTO>>(cheques, HttpStatus.OK);
    }

	@PostMapping(value="/reporte", produces = MediaType.APPLICATION_OCTET_STREAM_VALUE)
	public ResponseEntity<byte[]> generarReporte(@RequestBody SolicitarReporteChequePosfechadoDTO dto) {
		byte [] data = null;
		data= servicio.generarReporte(dto);
		return new ResponseEntity<byte[]>(data, HttpStatus.CREATED);
	}
	
}
