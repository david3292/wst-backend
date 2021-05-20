package com.altioracorp.wst.controlador.sistema;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.altioracorp.gpintegrator.client.Company.CompanyInformation;
import com.altioracorp.wst.servicio.ventas.IEmpresaServicio;

@RestController
@RequestMapping("/empresa")
public class EmpresaControlador {

	@Autowired
	private IEmpresaServicio servicio;
	
	@GetMapping("/informacion")
	public ResponseEntity<CompanyInformation> listarAreasActivos() {
		CompanyInformation lista = servicio.obtenerInformacionEmpresa();
		return new ResponseEntity<CompanyInformation>(lista, HttpStatus.OK);
	}
	
}
