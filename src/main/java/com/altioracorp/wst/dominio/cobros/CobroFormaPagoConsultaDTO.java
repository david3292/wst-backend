package com.altioracorp.wst.dominio.cobros;

import com.altioracorp.wst.util.LocalDateTimeDeserialize;
import com.altioracorp.wst.util.LocalDateTimeSerialize;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

import java.io.Serializable;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class CobroFormaPagoConsultaDTO implements Serializable {

    private String identificacionCliente;

    private String numeroCobro;

    private String estadoCobro;

    private BigDecimal valorCobro;

    @JsonSerialize(using = LocalDateTimeSerialize.class)
    @JsonDeserialize(using = LocalDateTimeDeserialize.class)
    private LocalDateTime fecha;

    private String puntoVenta;

    private String chequera;

    private String estadoFormaPago;

    private String formaPago;

    private String numeroFactura;

    private String numeroCobroGp;

    private BigDecimal valorCuota;

    public static List<CobroFormaPagoConsultaDTO> parseCobroToContultaDTO(final Cobro cobro) {
        if (cobro.getCobroFormaPagos() == null || cobro.getCobroFormaPagos().isEmpty()) {
            return null;
        }

        final List<CobroFormaPagoConsultaDTO> lista = new ArrayList<>();

        cobro.getCobroFormaPagos().forEach(pago -> {
            final CobroFormaPagoConsultaDTO obj = new CobroFormaPagoConsultaDTO();
            obj.setIdentificacionCliente(cobro.getCodigoCliente());
            obj.setNumeroCobro(cobro.getNumero());
            obj.setEstadoCobro(cobro.getEstado().toString());
            obj.setValorCobro(cobro.getValor().setScale(2, RoundingMode.HALF_UP));
            obj.setFecha(cobro.getFecha());
            obj.setPuntoVenta(cobro.getPuntoVenta().getNombre());
            obj.setChequera(pago.getChequera());
            obj.setEstadoFormaPago(pago.getEstadoCadena());
            obj.setFormaPago(pago.getFormaPagoCadena());
            obj.setNumeroFactura(pago.getNumeroFactura());
            obj.setNumeroCobroGp(pago.getNumeroCobroGP());
            obj.setValorCuota(pago.getValor().setScale(2, RoundingMode.HALF_UP));
            lista.add(obj);
        });

        return lista;
    }

    public String getIdentificacionCliente() {
        return this.identificacionCliente;
    }

    public void setIdentificacionCliente(final String identificacionCliente) {
        this.identificacionCliente = identificacionCliente;
    }

    public String getNumeroCobro() {
        return this.numeroCobro;
    }

    public void setNumeroCobro(final String numeroCobro) {
        this.numeroCobro = numeroCobro;
    }

    public String getEstadoCobro() {
        return this.estadoCobro;
    }

    public void setEstadoCobro(final String estadoCobro) {
        this.estadoCobro = estadoCobro;
    }

    public BigDecimal getValorCobro() {
        return this.valorCobro;
    }

    public void setValorCobro(final BigDecimal valorCobro) {
        this.valorCobro = valorCobro;
    }

    public LocalDateTime getFecha() {
        return this.fecha;
    }

    public void setFecha(final LocalDateTime fecha) {
        this.fecha = fecha;
    }

    public String getPuntoVenta() {
        return this.puntoVenta;
    }

    public void setPuntoVenta(final String puntoVenta) {
        this.puntoVenta = puntoVenta;
    }

    public String getChequera() {
        return this.chequera;
    }

    public void setChequera(final String chequera) {
        this.chequera = chequera;
    }

    public String getEstadoFormaPago() {
        return this.estadoFormaPago;
    }

    public void setEstadoFormaPago(final String estadoFormaPago) {
        this.estadoFormaPago = estadoFormaPago;
    }

    public String getFormaPago() {
        return this.formaPago;
    }

    public void setFormaPago(final String formaPago) {
        this.formaPago = formaPago;
    }

    public String getNumeroFactura() {
        return this.numeroFactura;
    }

    public void setNumeroFactura(final String numeroFactura) {
        this.numeroFactura = numeroFactura;
    }

    public String getNumeroCobroGp() {
        return this.numeroCobroGp;
    }

    public void setNumeroCobroGp(final String numeroCobroGp) {
        this.numeroCobroGp = numeroCobroGp;
    }

    public BigDecimal getValorCuota() {
        return this.valorCuota;
    }

    public void setValorCuota(final BigDecimal valorCuota) {
        this.valorCuota = valorCuota;
    }
}
