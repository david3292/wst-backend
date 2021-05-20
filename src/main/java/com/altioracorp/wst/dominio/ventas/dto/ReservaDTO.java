package com.altioracorp.wst.dominio.ventas.dto;

import java.time.LocalDate;
import java.time.LocalDateTime;

import com.altioracorp.wst.dominio.ventas.DocumentoEstado;
import com.altioracorp.wst.util.LocalDateDeserialize;
import com.altioracorp.wst.util.LocalDateSerialize;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

public class ReservaDTO {

	private long reservaId;
	
	private String numero;
	
	private String numeroCotizacion;
	
	private DocumentoEstado estado;
	
	private String usuario;
	
	private String usuarioNombreCompleto;
	
	@JsonSerialize(using = LocalDateSerialize.class)
    @JsonDeserialize(using = LocalDateDeserialize.class)
    private LocalDate fechaEmision;
	
	private String nombreCliente;
	
	private String codigoCliente;
	
	private String puntoVenta;

	
	public ReservaDTO() {	}

	public ReservaDTO(long reservaId, String numero, String numeroCotizacion, DocumentoEstado estado, String usuario,
			String usuarioNombreCompleto, LocalDate fechaEmision, String nombreCliente, String codigoCliente, String puntoVenta) {
		super();
		this.reservaId = reservaId;
		this.numero = numero;
		this.numeroCotizacion = numeroCotizacion;
		this.estado = estado;
		this.usuario = usuario;
		this.usuarioNombreCompleto = usuarioNombreCompleto;
		this.fechaEmision = fechaEmision;
		this.nombreCliente = nombreCliente;
		this.codigoCliente = codigoCliente;
		this.puntoVenta = puntoVenta;
	}

	public long getReservaId() {
		return reservaId;
	}

	public String getNumero() {
		return numero;
	}

	public String getNumeroCotizacion() {
		return numeroCotizacion;
	}

	public DocumentoEstado getEstado() {
		return estado;
	}

	public String getUsuario() {
		return usuario;
	}

	public String getUsuarioNombreCompleto() {
		return usuarioNombreCompleto;
	}

	public LocalDate getFechaEmision() {
		return fechaEmision;
	}

	public String getNombreCliente() {
		return nombreCliente;
	}

	public String getCodigoCliente() {
		return codigoCliente;
	}

	public String getPuntoVenta() {
		return puntoVenta;
	}
	
	
}
