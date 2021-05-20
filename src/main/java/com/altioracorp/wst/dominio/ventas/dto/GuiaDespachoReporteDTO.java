package com.altioracorp.wst.dominio.ventas.dto;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class GuiaDespachoReporteDTO {

	private String codigoCliente;

	private List<GuiaDespachoDetalleDTO> detalle;

	private String direccionCliente;

	private String direccionEntrega;

	private LocalDateTime fechaEmision;

	private String nombreCliente;

	private String numero;

	private String numeroFactura;
	
	private String puntoVenta;
	
	private String retirarCliente;

	public GuiaDespachoReporteDTO() {
		super();
	}

	public GuiaDespachoReporteDTO(String codigoCliente, String direccionCliente, String direccionEntrega,
			LocalDateTime fechaEmision, String nombreCliente, String numero, String numeroFactura, String puntoVenta, String retirarCliente) {
		this.codigoCliente = codigoCliente;
		this.direccionCliente = direccionCliente;
		this.direccionEntrega = direccionEntrega;
		this.fechaEmision = fechaEmision;
		this.nombreCliente = nombreCliente;
		this.numero = numero;
		this.numeroFactura = numeroFactura;
		this.retirarCliente = retirarCliente;
		this.puntoVenta = puntoVenta;
		this.detalle = new ArrayList<>();
	}

	public void agregarDetalle(GuiaDespachoDetalleDTO detalleDTO) {
		this.detalle.add(detalleDTO);
	}

	public String getCodigoCliente() {
		return codigoCliente;
	}

	public List<GuiaDespachoDetalleDTO> getDetalle() {
		return detalle;
	}

	public String getDireccionCliente() {
		return direccionCliente;
	}

	public String getDireccionEntrega() {
		return direccionEntrega;
	}

	public LocalDateTime getFechaEmision() {
		return fechaEmision;
	}

	public String getNombreCliente() {
		return nombreCliente;
	}

	public String getNumero() {
		return numero;
	}

	public String getNumeroFactura() {
		return numeroFactura;
	}

	public void setCodigoCliente(String codigoCliente) {
		this.codigoCliente = codigoCliente;
	}

	public void setDetalle(List<GuiaDespachoDetalleDTO> detalle) {
		this.detalle = detalle;
	}

	public void setDireccionCliente(String direccionCliente) {
		this.direccionCliente = direccionCliente;
	}

	public void setDireccionEntrega(String direccionEntrega) {
		this.direccionEntrega = direccionEntrega;
	}

	public void setFechaEmision(LocalDateTime fechaEmision) {
		this.fechaEmision = fechaEmision;
	}

	public void setNombreCliente(String nombreCliente) {
		this.nombreCliente = nombreCliente;
	}

	public void setNumero(String numero) {
		this.numero = numero;
	}

	public void setNumeroFactura(String numeroFactura) {
		this.numeroFactura = numeroFactura;
	}

	public String getPuntoVenta() {
		return puntoVenta;
	}

	public void setPuntoVenta(String puntoVenta) {
		this.puntoVenta = puntoVenta;
	}

	public String getRetirarCliente() {
		return retirarCliente;
	}

	public void setRetirarCliente(String retirarCliente) {
		this.retirarCliente = retirarCliente;
	}
		
}
