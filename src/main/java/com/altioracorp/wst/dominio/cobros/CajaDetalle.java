package com.altioracorp.wst.dominio.cobros;

import java.math.BigDecimal;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import com.altioracorp.wst.dominio.EntidadBase;
import com.altioracorp.wst.dominio.sistema.FormaPagoNombre;
import com.fasterxml.jackson.annotation.JsonIgnore;

@SuppressWarnings("serial")
@Entity
public class CajaDetalle extends EntidadBase {

	@ManyToOne
    @JoinColumn(name ="caja_id", nullable = false, insertable = false ,updatable = false)
    private Caja caja;
	
	@Enumerated(EnumType.STRING)
	private FormaPagoNombre formaPago;

	private BigDecimal valorCobrado;

	private BigDecimal valorContado;

	private BigDecimal diferencia;

	public CajaDetalle() { }

	public CajaDetalle(FormaPagoNombre formaPago, BigDecimal valorCobrado) {
		super();
		this.formaPago = formaPago;
		this.valorCobrado = valorCobrado;
	}

	public BigDecimal getDiferencia() {
		return diferencia;
	}

	public FormaPagoNombre getFormaPago() {
		return formaPago;
	}

	public BigDecimal getValorCobrado() {
		return valorCobrado;
	}

	public BigDecimal getValorContado() {
		return valorContado;
	}

	public void setDiferencia(BigDecimal diferencia) {
		this.diferencia = diferencia;
	}

	public void setFormaPago(FormaPagoNombre formaPago) {
		this.formaPago = formaPago;
	}

	public void setValorCobrado(BigDecimal valorCobrado) {
		this.valorCobrado = valorCobrado;
	}

	public void setValorContado(BigDecimal valorContado) {
		this.valorContado = valorContado;
	}
	
	@JsonIgnore
	public String getFormaPagoCadena() {
		return this.formaPago.getDescripcion().toUpperCase();
	}

	public Caja getCaja() {
		return caja;
	}

	public void setCaja(Caja caja) {
		this.caja = caja;
	}

}
