package com.altioracorp.wst.dominio.ventas.dto;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

import com.altioracorp.wst.dominio.sistema.TipoDocumento;
import com.altioracorp.wst.dominio.ventas.Cotizacion;
import com.altioracorp.wst.dominio.ventas.DocumentoFactura;
import com.altioracorp.wst.dominio.ventas.DocumentoNotaCredito;
import com.altioracorp.wst.util.LocalDateTimeDeserialize;
import com.altioracorp.wst.util.LocalDateTimeSerialize;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

@SuppressWarnings("serial")
public class FacturaCotizacionDTO implements Serializable {

    private Long id;

    private String identificacionCliente;

    private String nombreCliente;

    private String numero;

    @JsonSerialize(using = LocalDateTimeSerialize.class)
    @JsonDeserialize(using = LocalDateTimeDeserialize.class)
    private LocalDateTime fechaEmision;

    private BigDecimal valor;

    private String estado;

    private String puntoVenta;

    private String tipo;

    private Boolean facturaDespachada;

    private Boolean facturaIntegrada;

    private String vendedor;
    
    private Boolean refacturacion;
    
    private Boolean tieneGuiaRemision;

    public FacturaCotizacionDTO(final Cotizacion cotizacion, final List<DocumentoFactura> facturas) {
        this.id = cotizacion.getId();
        this.identificacionCliente = cotizacion.getCodigoCliente();
        this.nombreCliente = cotizacion.getNombreCliente();
        this.numero = cotizacion.getNumero();
        this.fechaEmision = cotizacion.getFechaEmision();
        this.valor = cotizacion.getTotal();
        this.estado = cotizacion.getEstado().toString();
        this.puntoVenta = cotizacion.getPuntoVenta().getNombre();
        this.tipo = TipoDocumento.COTIZACION.getDescripcion();
        this.vendedor = cotizacion.getCreadoPor();
        if (facturas != null && !facturas.isEmpty()) {
            this.facturaDespachada = facturas.get(0).isDespachada();
            this.facturaIntegrada = facturas.get(0).isFacturaIntegrada();
        }
    }

    public FacturaCotizacionDTO(final DocumentoFactura factura) {
        this.id = factura.getId();
        this.identificacionCliente = factura.getCodigoCliente();
        this.nombreCliente = factura.getNombreCliente();
        this.numero = factura.getNumero();
        this.fechaEmision = factura.getFechaEmision();
        this.valor = factura.getTotal();
        this.estado = factura.getEstado().toString();
        this.puntoVenta = factura.getCotizacion().getPuntoVenta().getNombre();
        this.tipo = TipoDocumento.FACTURA.getDescripcion();
        this.facturaDespachada = factura.isDespachada();
        this.facturaIntegrada = factura.isFacturaIntegrada();
        this.vendedor = factura.getCreadoPor();
        this.refacturacion = factura.isRefacturacion();
        this.tieneGuiaRemision = factura.isTieneGuiaRemision();
    }

    public FacturaCotizacionDTO(final DocumentoNotaCredito notaCredito, final List<DocumentoFactura> facturas) {
        this.id = notaCredito.getId();
        this.identificacionCliente = notaCredito.getCotizacion().getCodigoCliente();
        this.nombreCliente = notaCredito.getCotizacion().getNombreCliente();
        this.numero = notaCredito.getNumero();
        this.fechaEmision = notaCredito.getFechaEmision();
        this.valor = notaCredito.getTotal();
        this.estado = notaCredito.getEstado().toString();
        this.puntoVenta = notaCredito.getCotizacion().getPuntoVenta().getNombre();
        this.tipo = TipoDocumento.NOTA_CREDITO.getDescripcion();
        if (facturas != null && !facturas.isEmpty()) {
            this.facturaDespachada = facturas.get(0).isDespachada();
        }
        this.facturaIntegrada = notaCredito.isNotaCreditoIntegrada();
        this.vendedor = notaCredito.getCreadoPor();
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getIdentificacionCliente() {
        return this.identificacionCliente;
    }

    public void setIdentificacionCliente(final String identificacionCliente) {
        this.identificacionCliente = identificacionCliente;
    }

    public String getNombreCliente() {
        return this.nombreCliente;
    }

    public void setNombreCliente(final String nombreCliente) {
        this.nombreCliente = nombreCliente;
    }

    public String getNumero() {
        return this.numero;
    }

    public void setNumero(final String numero) {
        this.numero = numero;
    }

    public LocalDateTime getFechaEmision() {
        return this.fechaEmision;
    }

    public void setFechaEmision(final LocalDateTime fechaEmision) {
        this.fechaEmision = fechaEmision;
    }

    public BigDecimal getValor() {
        return this.valor;
    }

    public void setValor(final BigDecimal valor) {
        this.valor = valor;
    }

    public String getEstado() {
        return this.estado;
    }

    public void setEstado(final String estado) {
        this.estado = estado;
    }

    public String getPuntoVenta() {
        return this.puntoVenta;
    }

    public void setPuntoVenta(final String puntoVenta) {
        this.puntoVenta = puntoVenta;
    }

    public String getTipo() {
        return this.tipo;
    }

    public void setTipo(final String tipo) {
        this.tipo = tipo;
    }

    public Boolean getFacturaDespachada() {
        return this.facturaDespachada;
    }

    public void setFacturaDespachada(final Boolean facturaDespachada) {
        this.facturaDespachada = facturaDespachada;
    }

    public Boolean getFacturaIntegrada() {
        return this.facturaIntegrada;
    }

    public void setFacturaIntegrada(final Boolean facturaIntegrada) {
        this.facturaIntegrada = facturaIntegrada;
    }

    public String getVendedor() {
        return this.vendedor;
    }

    public void setVendedor(final String vendedor) {
        this.vendedor = vendedor;
    }

	public Boolean getRefacturacion() {
		return refacturacion;
	}

	public void setRefacturacion(Boolean refacturacion) {
		this.refacturacion = refacturacion;
	}

	public Boolean getTieneGuiaRemision() {
		return tieneGuiaRemision;
	}

	public void setTieneGuiaRemision(Boolean tieneGuiaRemision) {
		this.tieneGuiaRemision = tieneGuiaRemision;
	}
    
}
