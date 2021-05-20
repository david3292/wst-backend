package com.altioracorp.wst.dominio.cobros;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;

import com.altioracorp.wst.dominio.EntidadBase;
import com.altioracorp.wst.dominio.sistema.PuntoVenta;
import com.altioracorp.wst.util.LocalDateTimeDeserialize;
import com.altioracorp.wst.util.LocalDateTimeSerialize;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

@SuppressWarnings("serial")
@Entity
public class Caja extends EntidadBase {

	@ManyToOne(fetch = FetchType.EAGER)
	private PuntoVenta puntoVenta;

	@JsonSerialize(using = LocalDateTimeSerialize.class)
	@JsonDeserialize(using = LocalDateTimeDeserialize.class)
	private LocalDateTime fechaInicio;

	@JsonSerialize(using = LocalDateTimeSerialize.class)
	@JsonDeserialize(using = LocalDateTimeDeserialize.class)
	private LocalDateTime fechaCierre;

	@OneToMany(cascade = { CascadeType.ALL }, fetch = FetchType.LAZY)
	@JoinColumn(name = "caja_id", nullable = false, insertable = true)
	private List<CajaDetalle> cajaDetalles = new ArrayList<>();

	public Caja() {	}

	public void agregarDetalle(CajaDetalle detalle) {
		if (!cajaDetalles.stream().anyMatch(x -> x.getFormaPago().equals(detalle.getFormaPago()))) {
			cajaDetalles.add(detalle);
		}
	}

	public List<CajaDetalle> getCajaDetalles() {
		return cajaDetalles;
	}

	public LocalDateTime getFechaCierre() {
		return fechaCierre;
	}

	public LocalDateTime getFechaInicio() {
		return fechaInicio;
	}

	public PuntoVenta getPuntoVenta() {
		return puntoVenta;
	}

	public void setFechaCierre(LocalDateTime fechaCierre) {
		this.fechaCierre = fechaCierre;
	}

	public void setFechaInicio(LocalDateTime fechaInicio) {
		this.fechaInicio = fechaInicio;
	}

	public void setPuntoVenta(PuntoVenta puntoVenta) {
		this.puntoVenta = puntoVenta;
	}
	
	public BigDecimal getTotal() {
		return cajaDetalles.stream().map(x ->x.getValorCobrado()).reduce(BigDecimal.ZERO,BigDecimal::add);
	}

	public void setCajaDetalles(List<CajaDetalle> cajaDetalles) {
		this.cajaDetalles = cajaDetalles;
	}
	

}
