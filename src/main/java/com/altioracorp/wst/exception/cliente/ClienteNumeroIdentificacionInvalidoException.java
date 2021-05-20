package com.altioracorp.wst.exception.cliente;

@SuppressWarnings("serial")
public class ClienteNumeroIdentificacionInvalidoException extends ClientesException{

	private String codigoCliente;

	public ClienteNumeroIdentificacionInvalidoException(String codigoCliente) {
		super();
		this.codigoCliente = codigoCliente;
	}



	@Override
	public String getMessage() {
		return String.format("Número de identificación %s es inválido.", codigoCliente);
	}

}
