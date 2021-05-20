package com.altioracorp.wst.exception.sistema;

@SuppressWarnings("serial")
public class SecuencialYaExisteException  extends SistemaException {

	private String puntoVenta;
	private String tipoDocumento;
	
	public SecuencialYaExisteException(String puntoVenta, String tipoDocumento) {
		this.setPuntoVenta(puntoVenta);
		this.setTipoDocumento(tipoDocumento);
	}

	public String getPuntoVenta() {
		return puntoVenta;
	}

	public void setPuntoVenta(String puntoVenta) {
		this.puntoVenta = puntoVenta;
	}

	public String getTipoDocumento() {
		return tipoDocumento;
	}

	public void setTipoDocumento(String tipoDocumento) {
		this.tipoDocumento = tipoDocumento;
	}
	
	@Override
	public String getMessage() {
		return String.format("La secuencia de %s ya existe para el punto de venta", tipoDocumento, puntoVenta);
	}
}
