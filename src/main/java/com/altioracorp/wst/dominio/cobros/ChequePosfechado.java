package com.altioracorp.wst.dominio.cobros;

import java.time.LocalDateTime;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.OneToOne;

import com.altioracorp.wst.dominio.EntidadBase;
import com.altioracorp.wst.util.LocalDateTimeDeserialize;
import com.altioracorp.wst.util.LocalDateTimeSerialize;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

@SuppressWarnings("serial")
@Entity
public class ChequePosfechado extends EntidadBase {

	@OneToOne(fetch = FetchType.EAGER)
	private CobroFormaPago cobroFormaPago;

	private int diasProrroga;

	@JsonSerialize(using = LocalDateTimeSerialize.class)
	@JsonDeserialize(using = LocalDateTimeDeserialize.class)
	private LocalDateTime fechaEfectivizacion;

	private boolean canje;

	@Enumerated(EnumType.STRING)
	private ChequePosfechadoEstado estado;

	private String observacion;
	
	private String codigoCliente;
	
	private String nombreCliente;
	
	@JsonSerialize(using = LocalDateTimeSerialize.class)
	@JsonDeserialize(using = LocalDateTimeDeserialize.class)
	private LocalDateTime fechaCobro;

	public ChequePosfechado() {	}

	public ChequePosfechado(CobroFormaPago cobroFormaPago, String codigoCliente, String nombreCliente, LocalDateTime fechaCobro) {
		this.cobroFormaPago = cobroFormaPago;
		this.fechaEfectivizacion = cobroFormaPago.getFechaEfectivizacion();
		this.canje = Boolean.FALSE;
		this.diasProrroga = 0;
		this.estado = ChequePosfechadoEstado.RECIBIDO;
		this.codigoCliente = codigoCliente;
		this.nombreCliente = nombreCliente;
		this.fechaCobro = fechaCobro;
	}

	public CobroFormaPago getCobroFormaPago() {
		return cobroFormaPago;
	}

	public int getDiasProrroga() {
		return diasProrroga;
	}

	public ChequePosfechadoEstado getEstado() {
		return estado;
	}

	public LocalDateTime getFechaEfectivizacion() {
		return fechaEfectivizacion;
	}

	public String getObservacion() {
		return observacion;
	}

	public boolean isCanje() {
		return canje;
	}

	public void setCanje(boolean canje) {
		this.canje = canje;
	}

	public void setCobroFormaPago(CobroFormaPago cobroFormaPago) {
		this.cobroFormaPago = cobroFormaPago;
	}

	public void setDiasProrroga(int diasProrroga) {
		this.diasProrroga = diasProrroga;
	}

	public void setEstado(ChequePosfechadoEstado estado) {
		this.estado = estado;
	}

	public void setFechaEfectivizacion(LocalDateTime fechaEfectivizacion) {
		this.fechaEfectivizacion = fechaEfectivizacion;
	}

	public void setObservacion(String observacion) {
		this.observacion = observacion;
	}

	public String getCodigoCliente() {
		return codigoCliente;
	}

	public void setCodigoCliente(String codigoCliente) {
		this.codigoCliente = codigoCliente;
	}

	public String getNombreCliente() {
		return nombreCliente;
	}

	public void setNombreCliente(String nombreCliente) {
		this.nombreCliente = nombreCliente;
	}

	public LocalDateTime getFechaCobro() {
		return fechaCobro;
	}

	public void setFechaCobro(LocalDateTime fechaCobro) {
		this.fechaCobro = fechaCobro;
	}

	@Override
	public String toString() {
		return "ChequePosfechado [cobroFormaPago=" + cobroFormaPago + ", diasProrroga=" + diasProrroga
				+ ", fechaEfectivizacion=" + fechaEfectivizacion + ", canje=" + canje + ", estado=" + estado
				+ ", observacion=" + observacion + "]";
	}

}
