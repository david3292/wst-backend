package com.altioracorp.wst.exception.cobros;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import com.altioracorp.wst.dominio.cobros.CobroCuotaFactura;

@SuppressWarnings("serial")
public class CobroCuotaFacturaYaExisteException extends CobrosException{

	private List<CobroCuotaFactura> cuotas = new ArrayList<>();

	public CobroCuotaFacturaYaExisteException(List<CobroCuotaFactura> cuotas) {
		super();
		this.cuotas = cuotas;
	}

	@Override
	public String getMessage() {
		return generarMensaje();
	}
	
	private String generarMensaje() {
		List<String> numeroFacturas = new ArrayList<>();
		
		 Map<String, List<CobroCuotaFactura>> bodegas = cuotas.stream().collect(Collectors.groupingBy(CobroCuotaFactura::getNumeroFactura, Collectors.toList()));

		 bodegas.forEach((numeroFactura, cuotas)->{
			 numeroFacturas.add(numeroFactura);
			 
		 });

		return String.format("Para los documentos %s ya se tiene valores aplicados a la misma forma de pago", String.join(",", numeroFacturas));
	}
	
	
}
