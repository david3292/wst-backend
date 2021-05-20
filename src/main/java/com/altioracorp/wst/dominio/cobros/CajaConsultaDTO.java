package com.altioracorp.wst.dominio.cobros;

import com.altioracorp.wst.dominio.sistema.PerfilNombre;
import com.altioracorp.wst.util.LocalDateTimeDeserialize;
import com.altioracorp.wst.util.LocalDateTimeSerialize;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

import java.io.Serializable;
import java.time.LocalDateTime;

@SuppressWarnings("serial")
public class CajaConsultaDTO implements Serializable {

    @JsonSerialize(using = LocalDateTimeSerialize.class)
    @JsonDeserialize(using = LocalDateTimeDeserialize.class)
    private LocalDateTime fechaInicio;

    @JsonSerialize(using = LocalDateTimeSerialize.class)
    @JsonDeserialize(using = LocalDateTimeDeserialize.class)
    private LocalDateTime fechaFin;

    private long idPuntoVenta;

    private PerfilNombre rol;

    private String usuario;

	public LocalDateTime getFechaInicio() {
		return fechaInicio;
	}

	public void setFechaInicio(LocalDateTime fechaInicio) {
		this.fechaInicio = fechaInicio;
	}

	public LocalDateTime getFechaFin() {
		return fechaFin;
	}

	public void setFechaFin(LocalDateTime fechaFin) {
		this.fechaFin = fechaFin;
	}

	public long getIdPuntoVenta() {
		return idPuntoVenta;
	}

	public void setIdPuntoVenta(long idPuntoVenta) {
		this.idPuntoVenta = idPuntoVenta;
	}

	public PerfilNombre getRol() {
		return rol;
	}

	public void setRol(PerfilNombre rol) {
		this.rol = rol;
	}

	public String getUsuario() {
		return usuario;
	}

	public void setUsuario(String usuario) {
		this.usuario = usuario;
	}

}
