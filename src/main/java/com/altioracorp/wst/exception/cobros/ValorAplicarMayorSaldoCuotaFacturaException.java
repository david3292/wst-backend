package com.altioracorp.wst.exception.cobros;

import java.math.BigDecimal;

@SuppressWarnings("serial")
public class ValorAplicarMayorSaldoCuotaFacturaException extends CobrosException {

	private BigDecimal valorAplicar;
	private BigDecimal saldo;
	private String numeroFactura;
	private int cuota;

	public ValorAplicarMayorSaldoCuotaFacturaException(BigDecimal valorAplicar, BigDecimal saldo, String numeroFactura,
			int cuota) {
		super();
		this.valorAplicar = valorAplicar;
		this.saldo = saldo;
		this.numeroFactura = numeroFactura;
		this.cuota = cuota;
	}

	@Override
	public String getMessage() {
		return String.format("Valor aplicar $ %s execede al saldo $ %s de la cuota %s en la factura %s",
				valorAplicar, saldo, cuota, numeroFactura);
	}

}
