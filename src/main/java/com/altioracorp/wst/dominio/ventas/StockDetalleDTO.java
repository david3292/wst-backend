package com.altioracorp.wst.dominio.ventas;

import java.math.BigDecimal;

import com.altioracorp.wst.dominio.sistema.Bodega;

public class StockDetalleDTO {

	private Bodega bodega;
	
	private BigDecimal cantidadExistente;
	
	private BigDecimal cantidadReservada;
	
	private BigDecimal cantidadTotal;
	
	private boolean bodegaPrincipal;

	public StockDetalleDTO() {}
	
	public StockDetalleDTO(Bodega bodega, BigDecimal cantidadExistente, BigDecimal cantidadReservada, boolean bodegaPrincipal) {
		this.bodega = bodega;
		this.cantidadExistente = cantidadExistente;
		this.cantidadReservada = cantidadReservada;
		this.cantidadTotal = cantidadExistente.subtract(cantidadReservada);
		this.bodegaPrincipal = bodegaPrincipal;
	}	

	public Bodega getBodega() {
		return bodega;
	}

	public void setBodega(Bodega bodega) {
		this.bodega = bodega;
	}

	public BigDecimal getCantidadExistente() {
		return cantidadExistente;
	}

	public void setCantidadExistente(BigDecimal cantidadExistente) {
		this.cantidadExistente = cantidadExistente;
	}

	public BigDecimal getCantidadReservada() {
		return cantidadReservada;
	}

	public void setCantidadReservada(BigDecimal cantidadReservada) {
		this.cantidadReservada = cantidadReservada;
	}

	public BigDecimal getCantidadTotal() {
		return cantidadTotal;
	}

	public void setCantidadTotal(BigDecimal cantidadTotal) {
		this.cantidadTotal = cantidadTotal;
	}

	public boolean isBodegaPrincipal() {
		return bodegaPrincipal;
	}

	public void setBodegaPrincipal(boolean bodegaPrincipal) {
		this.bodegaPrincipal = bodegaPrincipal;
	}	
}
