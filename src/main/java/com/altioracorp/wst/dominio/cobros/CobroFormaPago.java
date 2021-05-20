package com.altioracorp.wst.dominio.cobros;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;

import com.altioracorp.wst.dominio.EntidadBase;
import com.altioracorp.wst.dominio.sistema.FormaPagoNombre;
import com.altioracorp.wst.util.LocalDateTimeDeserialize;
import com.altioracorp.wst.util.LocalDateTimeSerialize;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

@SuppressWarnings("serial")
@Entity
public class CobroFormaPago extends EntidadBase {

	@Enumerated(EnumType.STRING)
	private CobroFormaPagoEstado estado;

	@Enumerated(EnumType.STRING)
	private FormaPagoNombre formaPago;

	private BigDecimal valor;

	private BigDecimal saldo;

	private String banco;

	private String numeroDocumento;

	private String numeroAutorizacion;

	private String chequera;

	private String nombreTarjeta;

	private String numeroFactura;

	private String numeroCobroGP;

	private String mensajeError;

	@JsonSerialize(using = LocalDateTimeSerialize.class)
	@JsonDeserialize(using = LocalDateTimeDeserialize.class)
	private LocalDateTime fechaEfectivizacion;

	private int secuencial = 0;

	public CobroFormaPago() {
		super();
		// TODO Auto-generated constructor stub
	}

	public String getBanco() {
		return banco;
	}

	public String getChequera() {
		return chequera;
	}

	public CobroFormaPagoEstado getEstado() {
		return estado;
	}

	@JsonIgnore
	public String getEstadoCadena() {
		return estado.toString();
	}

	public LocalDateTime getFechaEfectivizacion() {
		return fechaEfectivizacion;
	}

	public FormaPagoNombre getFormaPago() {
		return formaPago;
	}

	@JsonIgnore
	public String getFormaPagoCadena() {
		return formaPago.getDescripcion().toUpperCase();
	}

	public String getMensajeError() {
		return mensajeError;
	}

	public String getNombreTarjeta() {
		return nombreTarjeta;
	}

	public String getNumeroAutorizacion() {
		return numeroAutorizacion;
	}

	public String getNumeroCobroGP() {
		return numeroCobroGP;
	}

	public String getNumeroDocumento() {
		return numeroDocumento;
	}

	public String getNumeroFactura() {
		return numeroFactura;
	}

	public BigDecimal getSaldo() {
		return saldo;
	}

	public int getSecuencial() {
		return secuencial;
	}

	public BigDecimal getValor() {
		return valor;
	}

	public void setBanco(String banco) {
		this.banco = banco;
	}

	public void setChequera(String chequera) {
		this.chequera = chequera;
	}

	public void setEstado(CobroFormaPagoEstado estado) {
		this.estado = estado;
	}

	public void setFechaEfectivizacion(LocalDateTime fechaEfectivizacion) {
		this.fechaEfectivizacion = fechaEfectivizacion;
	}

	public void setFormaPago(FormaPagoNombre formaPago) {
		this.formaPago = formaPago;
	}

	public void setMensajeError(String mensajeError) {
		this.mensajeError = mensajeError;
	}

	public void setNombreTarjeta(String nombreTarjeta) {
		this.nombreTarjeta = nombreTarjeta;
	}

	public void setNumeroAutorizacion(String numeroAutorizacion) {
		this.numeroAutorizacion = numeroAutorizacion;
	}

	public void setNumeroCobroGP(String numeroCobroGP) {
		this.numeroCobroGP = numeroCobroGP;
	}

	public void setNumeroDocumento(String numeroDocumento) {
		this.numeroDocumento = numeroDocumento;
	}

	public void setNumeroFactura(String numeroFactura) {
		this.numeroFactura = numeroFactura;
	}

	public void setSaldo(BigDecimal saldo) {
		this.saldo = saldo;
	}

	public void setSecuencial(int secuencial) {
		this.secuencial = secuencial;
	}

	public void setValor(BigDecimal valor) {
		this.valor = valor;
	}

	@Override
	public String toString() {
		return "CobroFormaPago [estado=" + estado + ", formaPago=" + formaPago + ", valor=" + valor + ", banco=" + banco
				+ ", numeroDocumento=" + numeroDocumento + ", numeroAutorizacion=" + numeroAutorizacion + ", chequera="
				+ chequera + ", nombreTarjeta=" + nombreTarjeta + ", numeroFactura=" + numeroFactura
				+ ", numeroCobroGP=" + numeroCobroGP + ", mensajeError=" + mensajeError + ", fechaEfectivizacion="
				+ fechaEfectivizacion + "]";
	}

}
