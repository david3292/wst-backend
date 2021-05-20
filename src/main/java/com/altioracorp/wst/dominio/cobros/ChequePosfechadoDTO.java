package com.altioracorp.wst.dominio.cobros;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import com.altioracorp.wst.util.LocalDateDeserialize;
import com.altioracorp.wst.util.LocalDateSerialize;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

public class ChequePosfechadoDTO {

	private Long chequePosfechadoId;

	private Long cobroFormaPagoId;

	private Long chequePosfechadoControlesId;

	private String numeroCheque;

	private String banco;

	private BigDecimal monto;

	@JsonSerialize(using = LocalDateSerialize.class)
	@JsonDeserialize(using = LocalDateDeserialize.class)
	private LocalDate fechaCheque;

	private List<String> documentosAplicados = new ArrayList<String>();

	private boolean canje;

	private int diasProrroga;

	private String observacion;

	private boolean revision;

	@JsonSerialize(using = LocalDateSerialize.class)
	@JsonDeserialize(using = LocalDateDeserialize.class)
	private LocalDate nuevaFecha;

	private boolean fechaDiferente;

	private String codigoCliente;

	private String nombreCliente;

	@JsonSerialize(using = LocalDateSerialize.class)
	@JsonDeserialize(using = LocalDateDeserialize.class)
	private LocalDate cobroFecha;
	
	private String usuarioAnalista;

	public ChequePosfechadoDTO() {
	}

	public ChequePosfechadoDTO(Long chequePosfechadoId, Long cobroFormaPagoId, String numeroCheque, String banco,
			BigDecimal monto, LocalDate fechaCheque, List<String> documentoAplicado, String observacion,
			String codigoCliente, String nombreCliente, LocalDate cobroFecha, String usuarioAnalista) {
		super();
		this.chequePosfechadoId = chequePosfechadoId;
		this.cobroFormaPagoId = cobroFormaPagoId;
		this.numeroCheque = numeroCheque;
		this.banco = banco;
		this.monto = monto;
		this.fechaCheque = fechaCheque;
		this.documentosAplicados = documentoAplicado;
		this.canje = Boolean.FALSE;
		this.observacion = observacion;
		this.revision = false;
		this.nombreCliente = nombreCliente;
		this.codigoCliente = codigoCliente;
		this.cobroFecha = cobroFecha;
		this.usuarioAnalista = usuarioAnalista;
	}

	public ChequePosfechadoDTO(Long chequePosfechadoControlesId, Long chequePosfechadoId, Long cobroFormaPagoId,
			String numeroCheque, String banco, BigDecimal monto, LocalDate fechaCheque, List<String> documentoAplicado,
			int diasProrroga, boolean canje, boolean fechaDiferente, String codigoCliente, String nombreCliente,
			LocalDate cobroFecha, String usuarioAnalista) {
		super();
		this.chequePosfechadoControlesId = chequePosfechadoControlesId;
		this.chequePosfechadoId = chequePosfechadoId;
		this.cobroFormaPagoId = cobroFormaPagoId;
		this.numeroCheque = numeroCheque;
		this.banco = banco;
		this.monto = monto;
		this.fechaCheque = fechaCheque;
		this.documentosAplicados = documentoAplicado;
		this.canje = canje;
		this.diasProrroga = diasProrroga;
		this.fechaDiferente = fechaDiferente;
		this.nombreCliente = nombreCliente;
		this.codigoCliente = codigoCliente;
		this.cobroFecha = cobroFecha;
		this.usuarioAnalista = usuarioAnalista;
		this.calcularNuevaFecha();
	}

	public void calcularNuevaFecha() {
		if (this.diasProrroga > 0) {
			this.nuevaFecha = this.fechaCheque.plusDays(diasProrroga);
		}
	}

	public boolean esFechaDiferente() {
		return isCanje() || diasProrroga > 0 ? Boolean.FALSE : Boolean.TRUE;
	}

	public Long getChequePosfechadoId() {
		return chequePosfechadoId;
	}

	public void setChequePosfechadoId(Long chequePosfechadoId) {
		this.chequePosfechadoId = chequePosfechadoId;
	}

	public String getNumeroCheque() {
		return numeroCheque;
	}

	public void setNumeroCheque(String numeroCheque) {
		this.numeroCheque = numeroCheque;
	}

	public String getBanco() {
		return banco;
	}

	public void setBanco(String banco) {
		this.banco = banco;
	}

	public BigDecimal getMonto() {
		return monto;
	}

	public void setMonto(BigDecimal monto) {
		this.monto = monto;
	}

	public LocalDate getFechaCheque() {
		return fechaCheque;
	}

	public void setFechaCheque(LocalDate fechaCheque) {
		this.fechaCheque = fechaCheque;
	}

	public List<String> getDocumentosAplicados() {
		return documentosAplicados;
	}

	public void setDocumentosAplicados(List<String> documentoAplicado) {
		this.documentosAplicados = documentoAplicado;
	}

	public boolean isCanje() {
		return canje;
	}

	public void setCanje(boolean canje) {
		this.canje = canje;
	}

	public int getDiasProrroga() {
		return diasProrroga;
	}

	public void setDiasProrroga(int diasProrroga) {
		this.diasProrroga = diasProrroga;
	}

	public String getObservacion() {
		return observacion;
	}

	public void setObservacion(String observacion) {
		this.observacion = observacion;
	}

	public LocalDate getNuevaFecha() {
		return nuevaFecha;
	}

	public void setNuevaFecha(LocalDate nuevaFecha) {
		this.nuevaFecha = nuevaFecha;
	}

	public boolean isRevision() {
		return revision;
	}

	public void setRevision(boolean revision) {
		this.revision = revision;
	}

	public Long getCobroFormaPagoId() {
		return cobroFormaPagoId;
	}

	public void setCobroFormaPagoId(Long cobroFormaPagoId) {
		this.cobroFormaPagoId = cobroFormaPagoId;
	}

	public boolean isFechaDiferente() {
		return fechaDiferente;
	}

	public void setFechaDiferente(boolean fechaDiferente) {
		this.fechaDiferente = fechaDiferente;
	}

	public Long getChequePosfechadoControlesId() {
		return chequePosfechadoControlesId;
	}

	public void setChequePosfechadoControlesId(Long chequePosfechadoControlesId) {
		this.chequePosfechadoControlesId = chequePosfechadoControlesId;
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

	public LocalDate getCobroFecha() {
		return cobroFecha;
	}

	public void setCobroFecha(LocalDate cobroFecha) {
		this.cobroFecha = cobroFecha;
	}

	public String getUsuarioAnalista() {
		return usuarioAnalista;
	}

	public void setUsuarioAnalista(String usuarioAnalista) {
		this.usuarioAnalista = usuarioAnalista;
	}

}
