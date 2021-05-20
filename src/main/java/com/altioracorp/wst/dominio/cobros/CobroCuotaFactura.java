package com.altioracorp.wst.dominio.cobros;

import java.math.BigDecimal;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.ManyToOne;

import com.altioracorp.wst.dominio.EntidadBase;

@SuppressWarnings("serial")
@Entity
public class CobroCuotaFactura extends EntidadBase {
	
	@ManyToOne(fetch = FetchType.EAGER)
	private CondicionCobroFactura cuotaFactura;
	
	private BigDecimal valor = BigDecimal.ZERO;
	
	private String numeroFactura;
	
	private long cobroFormaPagoId;
	
	@Enumerated(EnumType.STRING)
	private CobroCuotaFacturaEstado estado = CobroCuotaFacturaEstado.NUEVO;
	
	private String mensajeError;

	public CobroCuotaFactura() {
		super();
	}

	public long getCobroFormaPagoId() {
		return cobroFormaPagoId;
	}

	public CondicionCobroFactura getCuotaFactura() {
		return cuotaFactura;
	}

	public CobroCuotaFacturaEstado getEstado() {
		return estado;
	}

	public String getMensajeError() {
		return mensajeError;
	}

	public String getNumeroFactura() {
		return numeroFactura;
	}

	public BigDecimal getValor() {
		return valor;
	}

	public void setCobroFormaPagoId(long cobroFormaPagoId) {
		this.cobroFormaPagoId = cobroFormaPagoId;
	}

	public void setCuotaFactura(CondicionCobroFactura cuotaFactura) {
		this.cuotaFactura = cuotaFactura;
	}

	public void setEstado(CobroCuotaFacturaEstado estado) {
		this.estado = estado;
	}

	public void setMensajeError(String mensajeError) {
		this.mensajeError = mensajeError;
	}

	public void setNumeroFactura(String numeroFactura) {
		this.numeroFactura = numeroFactura;
	}

	public void setValor(BigDecimal valor) {
		this.valor = valor;
	}

	@Override
	public String toString() {
		return "CobroCuotaFactura [cuotaFactura=" + cuotaFactura + "]";
	}
}
