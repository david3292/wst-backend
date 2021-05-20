package com.altioracorp.wst.dominio.ventas.dto;

import java.util.Comparator;

import com.altioracorp.gpintegrator.client.Customer.Customer;
import com.altioracorp.wst.dominio.ventas.Cotizacion;
import com.altioracorp.wst.dominio.ventas.CotizacionDetalle;

public class CotizacionReporteDTO {

	private Cotizacion cabecera;
	
	private Customer cliente;
	
	private String tiempoVigencia;
	
	private Integer diasPlazo;
	
	public Cotizacion getCabecera() {
		return cabecera;
	}
	
	public void setCabecera(Cotizacion cabecera) {
		this.cabecera = cabecera;
	}
	
	public Customer getCliente() {
		return cliente;
	}
	
	public void setCliente(Customer cliente) {
		this.cliente = cliente;
	}
	
	public String getTiempoVigencia() {
		return tiempoVigencia;
	}
	
	public void setTiempoVigencia(String tiempoVigencia) {
		this.tiempoVigencia = tiempoVigencia;
	}
	
	public void ordenarDetalle() {
		cabecera.getDetalle().sort(Comparator.comparing(CotizacionDetalle::getId));
	}

	public Integer getDiasPlazo() {
		return diasPlazo;
	}

	public void setDiasPlazo(Integer diasPlazo) {
		this.diasPlazo = diasPlazo;
	}

}
