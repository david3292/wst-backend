package com.altioracorp.wst.dominio.cobros;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

import com.altioracorp.wst.dominio.RevisionEstado;

public class ChequePosfechadoReporteDetalleDTO {

	private LocalDate fechaEfectivizacion;

	private String numeroCheque;

	private String banco;

	private String codigoCliente;

	private String nombreCliente;

	private String cobroGP;

	private BigDecimal valor;

	private List<String> facturaAplica;

	private boolean canje;

	private boolean prorroga;

	private LocalDate nuevaFecha;

	private RevisionEstado estado;
	
	private String observacion;
	
	public ChequePosfechadoReporteDetalleDTO(LocalDate fechaEfectivizacion, String numeroCheque, String banco,
			String codigoCliente, String nombreCliente, String cobroGP, BigDecimal valor, List<String> facturaAplica,
			boolean canje, boolean prorroga, LocalDate nuevaFecha, RevisionEstado estado, String observacion) {
		super();
		this.fechaEfectivizacion = fechaEfectivizacion;
		this.numeroCheque = numeroCheque;
		this.banco = banco;
		this.codigoCliente = codigoCliente;
		this.nombreCliente = nombreCliente;
		this.cobroGP = cobroGP;
		this.valor = valor;
		this.facturaAplica = facturaAplica;
		this.canje = canje;
		this.prorroga = prorroga;
		this.nuevaFecha = nuevaFecha;
		this.estado = estado;
		this.observacion = observacion;
	}

	public LocalDate getFechaEfectivizacion() {
		return fechaEfectivizacion;
	}

	public String getNumeroCheque() {
		return numeroCheque;
	}

	public String getBanco() {
		return banco;
	}

	public String getCodigoCliente() {
		return codigoCliente;
	}

	public String getNombreCliente() {
		return nombreCliente;
	}

	public String getCobroGP() {
		return cobroGP;
	}

	public BigDecimal getValor() {
		return valor;
	}

	public String getFacturaAplica() {
		return facturaAplica.stream().collect(Collectors.joining(" ;"));
	}

	public boolean isCanje() {
		return canje;
	}

	public boolean isProrroga() {
		return prorroga;
	}

	public String getTipo() {
		if (isCanje())
			return "CANJE";
		if (isProrroga())
			return "PRORROGA";
		return null;
	}

	public LocalDate getNuevaFecha() {
		return nuevaFecha;
	}

	public String getEstado() {
		return estado == null ? null : estado.toString();
	}

	public String getObservacion() {
		return observacion;
	}

}
