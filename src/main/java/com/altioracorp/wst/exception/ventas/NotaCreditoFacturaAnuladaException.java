package com.altioracorp.wst.exception.ventas;

@SuppressWarnings("serial")
public class NotaCreditoFacturaAnuladaException extends VentasException {

	private String numeroFactura;
	
    public NotaCreditoFacturaAnuladaException(String numeroFactura) {
		this.numeroFactura = numeroFactura;
	}

	@Override
    public String getMessage() {
        return String.format("Error al generar la Nota Crédito Factura %s Anulada ", numeroFactura);
    }
}
