package com.altioracorp.wst.controlador.sistema;

import com.altioracorp.wst.dominio.sistema.FormaPagoNombre;

public class FormaPagoPvtaDTO {

	private FormaPagoNombre formaPago;
	
	private String chequera;

	public FormaPagoPvtaDTO(FormaPagoNombre formaPago, String chequera) {
		super();
		this.formaPago = formaPago;
		this.chequera = chequera;
	}

	public FormaPagoNombre getFormaPago() {
		return formaPago;
	}

	public void setFormaPago(FormaPagoNombre formaPago) {
		this.formaPago = formaPago;
	}

	public String getChequera() {
		return chequera;
	}

	public void setChequera(String chequera) {
		this.chequera = chequera;
	}
	
}
