package com.altioracorp.wst.util.validacionNumeroIdentificacion;

@SuppressWarnings("serial")
public class ValidacionNumeroIdentificacionDigitoVerificadorInvalidoException extends ValidacionNumeroIdentificacionException {

	private short verificadorDigito;
	private short verificadorCalculado;

	public ValidacionNumeroIdentificacionDigitoVerificadorInvalidoException(short verificadorCalculado, short verificadorDigito) {
		this.verificadorCalculado = verificadorCalculado;
		this.verificadorDigito = verificadorDigito;
	}
	
	@Override
	public String getMessage() {
		return String.format("Se esperaba el dígito verificador %s pero es %s", verificadorCalculado, verificadorDigito);
	}
}
