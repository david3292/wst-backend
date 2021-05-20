package com.altioracorp.wst.dominio.cobros;

import java.math.BigDecimal;
import java.time.LocalDate;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.ManyToOne;
import javax.validation.constraints.NotNull;

import com.altioracorp.wst.dominio.EntidadBase;
import com.altioracorp.wst.dominio.ventas.DocumentoFactura;
import com.altioracorp.wst.util.LocalDateDeserialize;
import com.altioracorp.wst.util.LocalDateSerialize;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

@SuppressWarnings("serial")
@Entity
public class CondicionCobroFactura extends EntidadBase {

	@ManyToOne(fetch = FetchType.EAGER)
	private DocumentoFactura factura;

	@JsonSerialize(using = LocalDateSerialize.class)
	@JsonDeserialize(using = LocalDateDeserialize.class)
	private LocalDate fecha;

	private int numeroCuota;

	private int porcentaje;

	@NotNull
	private int totalDias;

	@NotNull
	private BigDecimal valor;
	
	private BigDecimal saldo = BigDecimal.ZERO;
	
	@Enumerated(EnumType.STRING)
	private CondicionCobroEstado estado;

	public CondicionCobroFactura() {
		super();
	}

	public CondicionCobroFactura(DocumentoFactura factura, LocalDate fecha, int numeroCuota, int porcentaje,
			int totalDias, BigDecimal valor) {
		this.factura = factura;
		this.fecha = fecha;
		this.numeroCuota = numeroCuota;
		this.porcentaje = porcentaje;
		this.totalDias = totalDias;
		this.valor = valor;
		this.saldo = valor;
		this.estado = CondicionCobroEstado.NUEVO;
	}

	
	public CondicionCobroEstado getEstado() {
		return estado;
	}

	public DocumentoFactura getFactura() {
		return factura;
	}

	public LocalDate getFecha() {
		return fecha;
	}

	public int getNumeroCuota() {
		return numeroCuota;
	}

	public int getPorcentaje() {
		return porcentaje;
	}

	public BigDecimal getSaldo() {
		return saldo;
	}

	public int getTotalDias() {
		return totalDias;
	}

	public BigDecimal getValor() {
		return valor;
	}

	public String numeroFactura() {
		return this.factura.getNumero();
	}

	public void setEstado(CondicionCobroEstado estado) {
		this.estado = estado;
	}

	public void setFactura(DocumentoFactura factura) {
		this.factura = factura;
	}

	public void setFecha(LocalDate fecha) {
		this.fecha = fecha;
	}

	public void setNumeroCuota(int numeroCuota) {
		this.numeroCuota = numeroCuota;
	}

	public void setPorcentaje(int porcentaje) {
		this.porcentaje = porcentaje;
	}

	public void setSaldo(BigDecimal saldo) {
		this.saldo = saldo;
	}

	public void setTotalDias(int totalDias) {
		this.totalDias = totalDias;
	}
	
	public void setValor(BigDecimal valor) {
		this.valor = valor;
	}

	@Override
	public String toString() {
		return "CondicionCobroFactura [factura=" + factura.getNumero() + ", fecha=" + fecha + ", numeroCuota=" + numeroCuota
				+ ", porcentaje=" + porcentaje + ", totalDias=" + totalDias + ", valor=" + valor + ", saldo=" + saldo
				+ ", estado=" + estado + "]";
	}
	
}
