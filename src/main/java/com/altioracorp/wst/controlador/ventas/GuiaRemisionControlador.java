package com.altioracorp.wst.controlador.ventas;

import com.altioracorp.wst.dominio.ventas.DocumentoBase;
import com.altioracorp.wst.dominio.ventas.dto.GuiaRemisionDTO;
import com.altioracorp.wst.servicio.ventas.IGuiaRemisionServicio;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/guiasRemision")
public class GuiaRemisionControlador {
	
	@Autowired
	private IGuiaRemisionServicio servicio;

	@GetMapping("/listarTodasPorCotizacion/{id}")
	public ResponseEntity<List<GuiaRemisionDTO>> obtenerTodosDocumentoFacturaPorCotizacionID(@PathVariable("id") Long cotizacionID) {
		List<GuiaRemisionDTO> facturas = servicio.listarTodasPorCotizacionID(cotizacionID);
		return new ResponseEntity<List<GuiaRemisionDTO>>(facturas, HttpStatus.OK);
	}

	@GetMapping("/{id}")
	public ResponseEntity<DocumentoBase> obtenerGuiaRemisionPorId(@PathVariable("id") long guiaRemisionId) {
		DocumentoBase guiaRemision = servicio.obtenerGuiaRemisionPorId(guiaRemisionId);
		return new ResponseEntity<DocumentoBase>(guiaRemision, HttpStatus.OK);
	}

	@GetMapping("/existeGuiremision/{facturaId}")
	public ResponseEntity<Boolean> existeGuiasRemisionPorFacturaId(@PathVariable Long facturaId) {
		Boolean respuesta = this.servicio.existeGuiasRemisionPorFacturaId(facturaId);
		return new ResponseEntity<>(respuesta, HttpStatus.OK);
	}
	
	@GetMapping("/obtenerInfoGuia/{guiaRemisionId}")
	public ResponseEntity<Map<String, Object>> obtenerInfoGuiaRemision(@PathVariable("guiaRemisionId") long guiaRemisionId){
		Map<String, Object> info = servicio.obtenerInfoGuiaRemision(guiaRemisionId);
		return ResponseEntity.ok(info);
	}
	
}
