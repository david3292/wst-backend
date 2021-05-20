package com.altioracorp.wst.dominio.cobros;

import java.math.BigDecimal;
import java.time.LocalDate;

import com.altioracorp.wst.util.LocalDateDeserialize;
import com.altioracorp.wst.util.LocalDateSerialize;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

public class CuotaDTO {

	private Long idCondicionCobroFactura;
	
	private int numero;
	
	private CondicionCobroEstado estado;
	
	private BigDecimal valor;
	
	private BigDecimal saldo;
	
	private boolean seleccionado;
	
	@JsonSerialize(using = LocalDateSerialize.class)
	@JsonDeserialize(using = LocalDateDeserialize.class)
	private LocalDate fechaPago;

	public CuotaDTO() {}
	
	public CuotaDTO(Long idCondicionCobroFactura, int numero, CondicionCobroEstado estado, BigDecimal valor, BigDecimal saldo, LocalDate fechaPago, boolean seleccionado) {
		this.idCondicionCobroFactura = idCondicionCobroFactura;
		this.numero = numero;
		this.estado = estado;
		this.valor = valor;
		this.saldo = saldo;
		this.fechaPago = fechaPago;
		this.seleccionado = seleccionado;
	}

	public Long getIdCondicionCobroFactura() {
		return idCondicionCobroFactura;
	}

	public void setIdCondicionCobroFactura(Long idCondicionCobroFactura) {
		this.idCondicionCobroFactura = idCondicionCobroFactura;
	}

	public int getNumero() {
		return numero;
	}

	public void setNumero(int numero) {
		this.numero = numero;
	}

	public CondicionCobroEstado getEstado() {
		return estado;
	}

	public void setEstado(CondicionCobroEstado estado) {
		this.estado = estado;
	}

	public BigDecimal getValor() {
		return valor;
	}

	public void setValor(BigDecimal valor) {
		this.valor = valor;
	}

	public boolean isSeleccionado() {
		return seleccionado;
	}

	public void setSeleccionado(boolean seleccionado) {
		this.seleccionado = seleccionado;
	}

	public LocalDate getFechaPago() {
		return fechaPago;
	}

	public void setFechaPago(LocalDate fechaPago) {
		this.fechaPago = fechaPago;
	}

	public BigDecimal getSaldo() {
		return saldo;
	}

	public void setSaldo(BigDecimal saldo) {
		this.saldo = saldo;
	}
}
