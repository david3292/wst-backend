package com.altioracorp.wst.controlador.logistica;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.altioracorp.wst.dominio.logistica.dto.ArticuloReposicionDTO;
import com.altioracorp.wst.dominio.logistica.dto.ReposicionArticuloBodegaDTO;
import com.altioracorp.wst.dominio.logistica.dto.ReposicionDTO;
import com.altioracorp.wst.dominio.logistica.dto.ReposicionDetalleDTO;
import com.altioracorp.wst.dominio.logistica.dto.ReposicionRespuestaCambioLineaDTO;
import com.altioracorp.wst.dominio.logistica.dto.ReposicionSugerenciaDTO;
import com.altioracorp.wst.dominio.sistema.Bodega;
import com.altioracorp.wst.servicio.sistema.IBodegaServicio;
import com.altioracorp.wst.servicio.ventas.ITransferenciaServicio;

@RestController
@RequestMapping("/reposiciones")
public class ReposicionControlador {

	@Autowired
	private IBodegaServicio bodegaServicio;
	
	@Autowired
	private ITransferenciaServicio transferenciaServicio;
	
	@GetMapping("/centroDistribucion")
	public ResponseEntity<List<Bodega>> listarBodegasCentroDistribucion(){
		return ResponseEntity.ok(bodegaServicio.listarBodegaCentroDistribucion());
	}
	
	@GetMapping("/reposicionInventario")
	public ResponseEntity<List<Bodega>> listarBodegasReposicionInventario(){
		return ResponseEntity.ok(bodegaServicio.listarBodegaReposicionInventario());
	}
	
	@PostMapping("/sugerencia")
	public ResponseEntity<ReposicionDTO> sugerenciaReposicion(@RequestBody ReposicionSugerenciaDTO dto){
		return ResponseEntity.ok(transferenciaServicio.sugerirReposicion(dto));
	}
	
	@PostMapping("/actualizarLinea")
	public ResponseEntity<ReposicionRespuestaCambioLineaDTO> actualizarLineaDetalle(@RequestBody ReposicionDetalleDTO dto){
		ReposicionRespuestaCambioLineaDTO actualizado = transferenciaServicio.actualizarCantidadReposicionDetalle(dto);
		return ResponseEntity.ok(actualizado);
	}
	
	@GetMapping("/anular/{id}")
	public ResponseEntity<Object> anularReposicion(@PathVariable("id") long id){
		transferenciaServicio.anularTransferenciaReposicion(id);
		return ResponseEntity.ok(Boolean.TRUE);
	}
	
	@GetMapping("/emitir/{id}")
	public ResponseEntity<Object> emitirReposicion(@PathVariable("id") long id){
		transferenciaServicio.emitirTransferenciaReposicion(id);
		return ResponseEntity.ok(Boolean.TRUE);
	}
	
	@GetMapping("/eliminarDetalle/{id}")
	public ResponseEntity<Object> eliminarReposicionDetalle(@PathVariable("id") long id){
		transferenciaServicio.eliminarDetalleReposicion(id);
		return ResponseEntity.ok(Boolean.TRUE);
	}
	
	@PostMapping("/reposicionArticulo")
	public ResponseEntity<ArticuloReposicionDTO> reposicionArticulo(@RequestBody ReposicionArticuloBodegaDTO dto){
		ArticuloReposicionDTO reposicion = transferenciaServicio.obtenerReposicionArticulo(dto);
		return ResponseEntity.ok(reposicion);
	}
	
}
