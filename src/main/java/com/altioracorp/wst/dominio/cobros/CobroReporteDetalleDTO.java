package com.altioracorp.wst.dominio.cobros;

import java.math.BigDecimal;

public class CobroReporteDetalleDTO {

	private String numeroCobroGP;

	private String formaPago;

	private String banco;

	private String numeroDocumento;

	private String nombreTarjeta;

	private String chequera;

	private String numeroFactura;

	private String estado;

	private BigDecimal valor;

	private boolean aplicado;

	public CobroReporteDetalleDTO() {
	}

	public CobroReporteDetalleDTO(String numeroCobroGP, String formaPago, String banco, String numeroDocumento,
			String nombreTarjeta, String chequera, String numeroFactura, String estado, BigDecimal valor,
			boolean aplicado) {
		super();
		this.numeroCobroGP = numeroCobroGP;
		this.formaPago = formaPago;
		this.banco = banco;
		this.numeroDocumento = numeroDocumento;
		this.nombreTarjeta = nombreTarjeta;
		this.chequera = chequera;
		this.numeroFactura = numeroFactura;
		this.estado = estado;
		this.valor = valor;
		this.aplicado = aplicado;
	}

	public String getNumeroCobroGP() {
		return numeroCobroGP;
	}

	public String getFormaPago() {
		return formaPago;
	}

	public String getBanco() {
		return banco;
	}

	public String getNumeroDocumento() {
		return numeroDocumento;
	}

	public String getNombreTarjeta() {
		return nombreTarjeta;
	}

	public String getChequera() {
		return chequera;
	}

	public String getNumeroFactura() {
		return numeroFactura;
	}

	public String getEstado() {
		return estado;
	}

	public BigDecimal getValor() {
		return valor;
	}

	public boolean isAplicado() {
		return aplicado;
	}

}
