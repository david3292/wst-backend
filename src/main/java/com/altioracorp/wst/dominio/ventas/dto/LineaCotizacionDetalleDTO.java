package com.altioracorp.wst.dominio.ventas.dto;

import java.util.List;

import com.altioracorp.gpintegrator.client.Item.Item;
import com.altioracorp.wst.dominio.ventas.Cotizacion;
import com.altioracorp.wst.dominio.ventas.CotizacionDetalle;

public class LineaCotizacionDetalleDTO {

	private Cotizacion cotizacion; 
	
	private CotizacionDetalle linea;
	
	private List<Item> articulos;

	public Cotizacion getCotizacion() {
		return cotizacion;
	}

	public void setCotizacion(Cotizacion cotizacion) {
		this.cotizacion = cotizacion;
	}

	public CotizacionDetalle getLinea() {
		return linea;
	}

	public void setLinea(CotizacionDetalle linea) {
		this.linea = linea;
	}

	public List<Item> getArticulos() {
		return articulos;
	}

	public void setArticulos(List<Item> articulos) {
		this.articulos = articulos;
	}

	
}
