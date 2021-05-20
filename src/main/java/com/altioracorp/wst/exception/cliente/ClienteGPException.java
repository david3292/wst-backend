package com.altioracorp.wst.exception.cliente;

@SuppressWarnings("serial")
public class ClienteGPException extends ClientesException{

	private String mensaje;
	
	public ClienteGPException(String mensaje) {
		this.mensaje = mensaje;
	}

	@Override
	public String getMessage() {
		return mensaje;
	}

}
