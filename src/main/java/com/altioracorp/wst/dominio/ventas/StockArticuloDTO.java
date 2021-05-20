package com.altioracorp.wst.dominio.ventas;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public class StockArticuloDTO {

	private String codigoArticulo;
	
	private String puntoVenta;
	
	private List<StockDetalleDTO> stockDetalle = new ArrayList<>();
	
	public StockArticuloDTO() {}

	public StockArticuloDTO(String codigoArticulo, String puntoVenta) {
		this.codigoArticulo = codigoArticulo;
		this.puntoVenta = puntoVenta;
	}
	
	public void agregarStockDetalle(StockDetalleDTO detalle) {
		this.stockDetalle.add(detalle);
	}

	public String getCodigoArticulo() {
		return codigoArticulo;
	}

	public void setCodigoArticulo(String codigoArticulo) {
		this.codigoArticulo = codigoArticulo;
	}

	public String getPuntoVenta() {
		return puntoVenta;
	}

	public void setPuntoVenta(String puntoVenta) {
		this.puntoVenta = puntoVenta;
	}

	public List<StockDetalleDTO> getStockDetalle() {
		return stockDetalle;
	}

	public void setStockDetalle(List<StockDetalleDTO> stockDetalle) {
		this.stockDetalle = stockDetalle;
	}

	public BigDecimal getTotal() {
		return this.stockDetalle.stream().map(x -> x.getCantidadTotal()).reduce(BigDecimal.ZERO, BigDecimal::add);
	}
	
}
