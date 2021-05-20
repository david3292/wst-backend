package com.altioracorp.wst.controlador.ventas;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.altioracorp.gpintegrator.client.Vendor.Vendor;
import com.altioracorp.wst.servicio.ventas.IProveedorServicio;

@RestController
@RequestMapping("/proveedores")
public class ProveedorControlador {
	
	@Autowired
	private IProveedorServicio proveedorServicio;

	@PostMapping("/buscarPorCriterio")
	public ResponseEntity<List<Vendor>> buscarPorCriterio(@RequestBody Map<String, String> criterios){
		List<Vendor> vendedores = this.proveedorServicio.ObtenerProveedoresPorCriterio(criterios);
		return ResponseEntity.ok(vendedores);
	}
	
	@GetMapping("/condicionesPago")
	public ResponseEntity<List<String>> obtenerCondicionesPago(){
		List<String> condicionesPago = proveedorServicio.obtenerCondicionesPago();
		return ResponseEntity.ok(condicionesPago);
	}
	
}
