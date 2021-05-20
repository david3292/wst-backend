package com.altioracorp.wst.xml.factura;

import java.math.BigDecimal;

public class ComprobanteAumentadoFormaPago {

	private FormaPagoXML formaPago;
	private BigDecimal total;

	public ComprobanteAumentadoFormaPago(FormaPagoXML formaPago, BigDecimal total) {
		this.formaPago = formaPago;
		this.total = total;
	}

	public FormaPagoXML getFormaPago() {
		return formaPago;
	}

	public String getFormaPagoDescripcion() {
		return formaPago.getDescripcion();
	}
	
	public BigDecimal getTotal() {
		return total;
	}
}
