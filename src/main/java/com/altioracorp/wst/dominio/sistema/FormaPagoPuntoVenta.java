package com.altioracorp.wst.dominio.sistema;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.ManyToOne;
import javax.validation.constraints.Size;

import com.altioracorp.wst.dominio.EntidadBase;

@SuppressWarnings("serial")
@Entity
public class FormaPagoPuntoVenta extends EntidadBase{

	@ManyToOne(fetch = FetchType.EAGER)
	private PuntoVenta puntoVenta;
	
	@ManyToOne(fetch = FetchType.EAGER)
	private FormaPago formaPago;
	
	private boolean activo;
	
	@Size(max = 512, message = "El campo de chequera no puede tener mas de 512 caractéres")
	private String chequera;

	public PuntoVenta getPuntoVenta() {
		return puntoVenta;
	}

	public void setPuntoVenta(PuntoVenta puntoVenta) {
		this.puntoVenta = puntoVenta;
	}

	public FormaPago getFormaPago() {
		return formaPago;
	}

	public void setFormaPago(FormaPago formaPago) {
		this.formaPago = formaPago;
	}

	public boolean isActivo() {
		return activo;
	}

	public void setActivo(boolean activo) {
		this.activo = activo;
	}

	public String getChequera() {
		return chequera;
	}

	public void setChequera(String chequerea) {
		this.chequera = chequerea;
	}

	@Override
	public String toString() {
		return "FormaPagoPuntoVenta [puntoVenta=" + puntoVenta + ", formaPago=" + formaPago + ", activo=" + activo
				+ ", chequera=" + chequera + "]";
	}
	
}
