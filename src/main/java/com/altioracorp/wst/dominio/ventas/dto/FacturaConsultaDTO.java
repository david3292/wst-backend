package com.altioracorp.wst.dominio.ventas.dto;

import com.altioracorp.wst.dominio.sistema.PerfilNombre;
import com.altioracorp.wst.dominio.ventas.DocumentoEstado;
import com.altioracorp.wst.util.LocalDateTimeDeserialize;
import com.altioracorp.wst.util.LocalDateTimeSerialize;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

import java.io.Serializable;
import java.time.LocalDateTime;

public class FacturaConsultaDTO implements Serializable {

    private String numeroFactura;

    private String identificacionCliente;

    private DocumentoEstado estado;

    @JsonSerialize(using = LocalDateTimeSerialize.class)
    @JsonDeserialize(using = LocalDateTimeDeserialize.class)
    private LocalDateTime fechaInicio;

    @JsonSerialize(using = LocalDateTimeSerialize.class)
    @JsonDeserialize(using = LocalDateTimeDeserialize.class)
    private LocalDateTime fechaFin;

    private String tipo;

    private Long idPuntoVenta;

    private String vendedor;

    private PerfilNombre rol;

    private String nombreCliente;

    public String getNumeroFactura() {
        return this.numeroFactura;
    }

    public void setNumeroFactura(final String numeroFactura) {
        this.numeroFactura = numeroFactura;
    }

    public DocumentoEstado getEstado() {
        return this.estado;
    }

    public void setEstado(final DocumentoEstado estado) {
        this.estado = estado;
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

    public String getIdentificacionCliente() {
        return this.identificacionCliente;
    }

    public void setIdentificacionCliente(final String identificacionCliente) {
        this.identificacionCliente = identificacionCliente;
    }

    public String getTipo() {
        return this.tipo;
    }

    public void setTipo(final String tipo) {
        this.tipo = tipo;
    }

    public Long getIdPuntoVenta() {
        return this.idPuntoVenta;
    }

    public void setIdPuntoVenta(final Long idPuntoVenta) {
        this.idPuntoVenta = idPuntoVenta;
    }

    public String getVendedor() {
        return this.vendedor;
    }

    public void setVendedor(final String vendedor) {
        this.vendedor = vendedor;
    }

    public PerfilNombre getRol() {
        return this.rol;
    }

    public void setRol(final PerfilNombre rol) {
        this.rol = rol;
    }

    public String getNombreCliente() {
        return nombreCliente;
    }

    public void setNombreCliente(String nombreCliente) {
        this.nombreCliente = nombreCliente;
    }
}
