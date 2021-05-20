package com.altioracorp.wst.controlador.sistema;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import javax.validation.Valid;

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

import com.altioracorp.wst.dominio.sistema.Secuencial;
import com.altioracorp.wst.dominio.sistema.TipoDocumento;
import com.altioracorp.wst.servicio.sistema.ISecuencialServicio;

@RestController
@RequestMapping("/secuenciales")
public class SecuencialControlador {
	@Autowired
	private ISecuencialServicio secuencialServicio;
	
	@GetMapping
	public ResponseEntity<List<SecuencialDTO>> listarSecuenciales() {
		ArrayList<SecuencialDTO> lista = new ArrayList<SecuencialDTO>();
		
		for (Secuencial scl : secuencialServicio.listarTodos()) {
			lista.add(new SecuencialDTO(scl));
		}
		
		return new ResponseEntity<List<SecuencialDTO>>(lista, HttpStatus.OK);
	}
	
	@GetMapping("/{id}")
	public ResponseEntity<SecuencialDTO> listarPorId(@PathVariable("id") Long id) {
		
		SecuencialDTO secuencial = new SecuencialDTO(secuencialServicio.listarPorId(id));
		
		return new ResponseEntity<SecuencialDTO>(secuencial, HttpStatus.OK);
	}

	@PostMapping
	public ResponseEntity<Secuencial> registrar(@Valid @RequestBody SecuencialDTO secuencial) {
		
		Secuencial obj = secuencialServicio.registrar(new Secuencial(secuencial.getPuntoVenta(), secuencial.getTipoDocumento().getTipoDocumento(), secuencial.getDocIdGP(), secuencial.getAbreviatura()));
		return new ResponseEntity<Secuencial>(obj, HttpStatus.CREATED);
	}
	
	@GetMapping("/tipoDocumentos")
	public ResponseEntity<List<TipoDocumentoDTO>> listarTipoDocumentos() {
		
		ArrayList<TipoDocumentoDTO> lista = new ArrayList<TipoDocumentoDTO>();
		
		 for (TipoDocumento tmp: TipoDocumento.values() ) {
			 lista.add(new TipoDocumentoDTO(tmp));
		 }
		
		return new ResponseEntity<List<TipoDocumentoDTO>>(lista, HttpStatus.OK);
	}
	
	@GetMapping("/tipoDocumentos/{esDocumentoGP}")
	public ResponseEntity<List<TipoDocumento>> listarTipoDocumentos(@PathVariable("esDocumentoGP") Boolean esDocumentoGP) {
		
		List<TipoDocumento> lista = Stream.of(TipoDocumento.values())
                .filter(e -> e.esDocumentoGP() == esDocumentoGP)
                .collect(Collectors.toList());
		return new ResponseEntity<List<TipoDocumento>>(lista, HttpStatus.OK);
	}
	
	@GetMapping(value="/numeroSecuencial/{puntoVentaId}/{tipoDocumento}", produces = MediaType.TEXT_PLAIN_VALUE)
	public ResponseEntity<Object> numeroSecuencial(@PathVariable("puntoVentaId") long puntoVentaId, @PathVariable("tipoDocumento") TipoDocumento tipoDocumento) {
		Secuencial secuencial = secuencialServicio.ObtenerSecuencialPorPuntoVentaYTipoDocumento(puntoVentaId, tipoDocumento);
		if (secuencial != null)
			secuencial.getNumeroSecuencial();
		
		return new ResponseEntity<Object>(secuencial.getNumeroSecuencial(), HttpStatus.OK);
	}
	
}