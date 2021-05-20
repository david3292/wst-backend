package com.altioracorp.wst.exception.ventas;

@SuppressWarnings("serial")
public class DocumentoRecotizarTiempoExcedido extends VentasException {

	private String numero;
	
	public DocumentoRecotizarTiempoExcedido(String numero) {
		this.numero = numero;
	}
	
	@Override
	public String getMessage() {
		return String.format("No es posible recotizar documento %s, ya que se encuentra fuera del tiempo permitido.", numero);
	}
}
