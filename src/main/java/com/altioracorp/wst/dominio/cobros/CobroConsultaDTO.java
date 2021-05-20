package com.altioracorp.wst.dominio.cobros;

import com.altioracorp.wst.dominio.sistema.PerfilNombre;
import com.altioracorp.wst.util.LocalDateTimeDeserialize;
import com.altioracorp.wst.util.LocalDateTimeSerialize;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

import java.io.Serializable;
import java.time.LocalDateTime;

public class CobroConsultaDTO implements Serializable {

    private String identificacionCliente;

    @JsonSerialize(using = LocalDateTimeSerialize.class)
    @JsonDeserialize(using = LocalDateTimeDeserialize.class)
    private LocalDateTime fechaInicio;

    @JsonSerialize(using = LocalDateTimeSerialize.class)
    @JsonDeserialize(using = LocalDateTimeDeserialize.class)
    private LocalDateTime fechaFin;

    private CobroEstado estado;

    private String numeroCobro;

    private long idPuntoVenta;

    private String nombreCliente;

    private PerfilNombre rol;

    private String usuario;

    public String getIdentificacionCliente() {
        return this.identificacionCliente;
    }

    public void setIdentificacionCliente(final String identificacionCliente) {
        this.identificacionCliente = identificacionCliente;
    }

    public LocalDateTime getFechaInicio() {
        return this.fechaInicio;
    }

    public void setFechaInicio(final LocalDateTime fechaInicio) {
        this.fechaInicio = fechaInicio;
    }

    public LocalDateTime getFechaFin() {
        return this.fechaFin;
    }

    public void setFechaFin(final LocalDateTime fechaFin) {
        this.fechaFin = fechaFin;
    }

    public CobroEstado getEstado() {
        return this.estado;
    }

    public void setEstado(final CobroEstado estado) {
        this.estado = estado;
    }

    public String getNumeroCobro() {
        return this.numeroCobro;
    }

    public void setNumeroCobro(final String numeroCobro) {
        this.numeroCobro = numeroCobro;
    }

    public long getIdPuntoVenta() {
        return this.idPuntoVenta;
    }

    public void setIdPuntoVenta(final long idPuntoVenta) {
        this.idPuntoVenta = idPuntoVenta;
    }

    public String getNombreCliente() {
        return nombreCliente;
    }

    public void setNombreCliente(String nombreCliente) {
        this.nombreCliente = nombreCliente;
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
