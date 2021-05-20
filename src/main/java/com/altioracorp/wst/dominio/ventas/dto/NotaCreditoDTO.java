package com.altioracorp.wst.dominio.ventas.dto;

import com.altioracorp.wst.dominio.ventas.TipoDevolucion;
import com.altioracorp.wst.util.LocalDateTimeDeserialize;
import com.altioracorp.wst.util.LocalDateTimeSerialize;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

import java.time.LocalDateTime;

public class NotaCreditoDTO {

	private long id;
	
	private TipoDevolucion tipoDevolucion;
	
	private String motivo;
	
	private String codigoCliente;
	
	private String nombreCliente;

	private String numeroFactura;

	private String nombreUsuarioVendedor;

	private String puntoVentaNombre;

	@JsonSerialize(using = LocalDateTimeSerialize.class)
	@JsonDeserialize(using = LocalDateTimeDeserialize.class)
	private LocalDateTime facturaFechaEmision;

	private String revisionTecnica;
	
	private String codigoClienteNuevo;
	
	private String nombreClienteNuevo;

	public NotaCreditoDTO() {
	}

	public NotaCreditoDTO(long id, TipoDevolucion tipoDevolucion, String motivo, String codigoCliente,
						  String nombreCliente, String numeroFactura, String nombreUsuarioVendedor, String puntoVentaNombre,
						  LocalDateTime facturaFechaEmision, Boolean revisionTecnica, String codigoClienteNuevo, String nombreClienteNuevo) {
		super();
		this.id = id;
		this.tipoDevolucion = tipoDevolucion;
		this.motivo = motivo;
		this.codigoCliente = codigoCliente;
		this.nombreCliente = nombreCliente;
		this.numeroFactura = numeroFactura;
		this.nombreUsuarioVendedor = nombreUsuarioVendedor;
		this.puntoVentaNombre = puntoVentaNombre;
		this.facturaFechaEmision = facturaFechaEmision;
		this.revisionTecnica = revisionTecnica ? "Sí" : "No";
		this.nombreClienteNuevo = nombreClienteNuevo;
		this.codigoClienteNuevo = codigoClienteNuevo;
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public TipoDevolucion getTipoDevolucion() {
		return tipoDevolucion;
	}

	public void setTipoDevolucion(TipoDevolucion tipoDevolucion) {
		this.tipoDevolucion = tipoDevolucion;
	}

	public String getMotivo() {
		return motivo;
	}

	public void setMotivo(String motivo) {
		this.motivo = motivo;
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

	public String getNumeroFactura() {
		return numeroFactura;
	}

	public void setNumeroFactura(String numeroFactura) {
		this.numeroFactura = numeroFactura;
	}

	public String getNombreUsuarioVendedor() {
		return nombreUsuarioVendedor;
	}

	public void setNombreUsuarioVendedor(String nombreUsuarioVendedor) {
		this.nombreUsuarioVendedor = nombreUsuarioVendedor;
	}

	public String getPuntoVentaNombre() {
		return puntoVentaNombre;
	}

	public void setPuntoVentaNombre(String puntoVentaNombre) {
		this.puntoVentaNombre = puntoVentaNombre;
	}

	public LocalDateTime getFacturaFechaEmision() {
		return facturaFechaEmision;
	}

	public void setFacturaFechaEmision(LocalDateTime facturaFechaEmision) {
		this.facturaFechaEmision = facturaFechaEmision;
	}

	public String getRevisionTecnica() {
		return revisionTecnica;
	}

	public void setRevisionTecnica(String revisionTecnica) {
		this.revisionTecnica = revisionTecnica;
	}

	public String getCodigoClienteNuevo() {
		return codigoClienteNuevo;
	}

	public void setCodigoClienteNuevo(String codigoClienteNuevo) {
		this.codigoClienteNuevo = codigoClienteNuevo;
	}

	public String getNombreClienteNuevo() {
		return nombreClienteNuevo;
	}

	public void setNombreClienteNuevo(String nombreClienteNuevo) {
		this.nombreClienteNuevo = nombreClienteNuevo;
	}
	
}
