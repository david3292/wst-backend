package com.altioracorp.wst.exception.cliente;

@SuppressWarnings("serial")
public class ClienteYaExisteException extends ClientesException{

	private String codigoCliente;
	private String nombreCliente;

	public ClienteYaExisteException(String codigoCliente, String nombreCliente) {
		super();
		this.codigoCliente = codigoCliente;
		this.nombreCliente = nombreCliente;
	}



	@Override
	public String getMessage() {
		return String.format("El Cliente %s %s ya existe.", codigoCliente, nombreCliente);
	}

}
