package com.altioracorp.wst.exception.ventas;

@SuppressWarnings("serial")
public class DocumentoGPException extends VentasException {
	
	private String mensaje;

	public DocumentoGPException(String mensaje) {
		super();
		this.mensaje = mensaje;
	}

	@Override
	public String getMessage() {
		return mensaje;
	}

}
