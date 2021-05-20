package com.altioracorp.wst.util.validacionNumeroIdentificacion;

@SuppressWarnings("serial")
public class ValidacionNumeroIdentificacionLongitudBaseInvalidaException extends ValidacionNumeroIdentificacionException {

	private short longitudEsperada;
	private int longitudReal;

	public ValidacionNumeroIdentificacionLongitudBaseInvalidaException(short longitudEsperada, int longitudReal) {
		this.longitudEsperada = longitudEsperada;
		this.longitudReal = longitudReal;
	}
	
	@Override
	public String getMessage() {
		return String.format("Se esperaba una base con longitud %s pero es de %s", this.longitudEsperada, this.longitudReal);
	}
}
