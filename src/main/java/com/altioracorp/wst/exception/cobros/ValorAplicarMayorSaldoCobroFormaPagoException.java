package com.altioracorp.wst.exception.cobros;

import java.math.BigDecimal;

import com.altioracorp.wst.dominio.sistema.FormaPagoNombre;

@SuppressWarnings("serial")
public class ValorAplicarMayorSaldoCobroFormaPagoException extends CobrosException {

	private BigDecimal valorAplicar;
	private BigDecimal saldo;
	private FormaPagoNombre formaPago;

	public ValorAplicarMayorSaldoCobroFormaPagoException(BigDecimal valorAplicar, BigDecimal saldo,
			FormaPagoNombre formaPago) {
		this.valorAplicar = valorAplicar;
		this.saldo = saldo;
		this.formaPago = formaPago;
	}

	@Override
	public String getMessage() {
		return String.format("Valor aplicar $ %s execede al saldo $ %s de la forma de pago %s", valorAplicar, saldo,
				formaPago.toString());
	}

}
