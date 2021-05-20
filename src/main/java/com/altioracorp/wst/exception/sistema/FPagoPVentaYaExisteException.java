package com.altioracorp.wst.exception.sistema;

@SuppressWarnings("serial")
public class FPagoPVentaYaExisteException extends SistemaException{

	private String formaPago;
	
	private String puntoVenta;

	public FPagoPVentaYaExisteException(String formaPago, String puntoVenta) {
		this.formaPago = formaPago;
		this.puntoVenta = puntoVenta;
	}

	@Override
	public String getMessage() {
		return String.format("Forma Pago %s ya esta asociado a Punto Venta: %s", formaPago, puntoVenta);
	}

}
