package com.altioracorp.wst.dominio.cobros;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;

import com.altioracorp.wst.util.LocalDateTimeDeserialize;
import com.altioracorp.wst.util.LocalDateTimeSerialize;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

@SuppressWarnings("serial")
public class CierreCajaDetalleDTO implements Serializable {

	private String numeroCobroGP;
	
	private String banco;
	
	private String chequerea;
	
	private String numeroDocumento;
	
	private String nombreCliente;
	
	private String codigoCliente;
	
	private BigDecimal valor;
	
	@JsonSerialize(using = LocalDateTimeSerialize.class)
	@JsonDeserialize(using = LocalDateTimeDeserialize.class)
	private LocalDateTime fechaCobro;

	public CierreCajaDetalleDTO() {}
			
	public CierreCajaDetalleDTO(String numeroCobroGP, String banco, String chequerea, String numeroDocumento,
			String nombreCliente, String codigoCliente, BigDecimal valor, LocalDateTime fechaCobro) {
		super();
		this.numeroCobroGP = numeroCobroGP;
		this.banco = banco;
		this.chequerea = chequerea;
		this.numeroDocumento = numeroDocumento;
		this.nombreCliente = nombreCliente;
		this.codigoCliente = codigoCliente;
		this.valor = valor;
		this.fechaCobro = fechaCobro;
	}

	public String getNumeroCobroGP() {
		return numeroCobroGP;
	}

	public String getBanco() {
		return banco;
	}

	public String getChequerea() {
		return chequerea;
	}

	public String getNumeroDocumento() {
		return numeroDocumento;
	}

	public String getNombreCliente() {
		return nombreCliente;
	}

	public String getCodigoCliente() {
		return codigoCliente;
	}

	public BigDecimal getValor() {
		return valor;
	}

	public LocalDateTime getFechaCobro() {
		return fechaCobro;
	}
	
}
