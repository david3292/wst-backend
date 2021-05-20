package com.altioracorp.wst.util.validacionNumeroIdentificacion;

@SuppressWarnings("serial")
public class ValidacionNumeroIdentificacionLongitudCoeficientesInvalidaException extends ValidacionNumeroIdentificacionException {

	private int longitudBase;
	private int longitudCoeficientes;

	public ValidacionNumeroIdentificacionLongitudCoeficientesInvalidaException(int longitudBase, int longitudCoeficientes) {
		this.longitudBase = longitudBase;
		this.longitudCoeficientes = longitudCoeficientes;
	}
	
	@Override
	public String getMessage() {
		return String.format("Se esperaban coeficientes con longitud %s pero es de %s", this.longitudBase, this.longitudCoeficientes);
	}
}
