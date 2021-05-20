package com.altioracorp.wst.exception.ventas;

@SuppressWarnings("serial")
public class CotizacionDetalleArticuloPrecioCeroException extends VentasException {

	private String codigoArticulo;
	private String descripcionArticulo;

	public CotizacionDetalleArticuloPrecioCeroException(String codigoArticulo, String descripcionArticulo) {
		super();
		this.codigoArticulo = codigoArticulo;
		this.descripcionArticulo = descripcionArticulo;
	}

	@Override
	public String getMessage() {
		return String.format("No se puede agregar el artículo %s %s , debido a que su precio es $0.00", codigoArticulo,
				descripcionArticulo);
	}
}
