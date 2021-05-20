package com.altioracorp.wst.util.validacionNumeroIdentificacion;

@SuppressWarnings("serial")
public class ValidacionNumeroIdentificacionRucTipoInvalidoException extends ValidacionNumeroIdentificacionException {

	private RucTipo tipo;
	
	public ValidacionNumeroIdentificacionRucTipoInvalidoException(RucTipo tipo) {
		this.tipo = tipo;
	}

	@Override
	public String getMessage() {
		return "El tipo es inválido: " + tipo;
	}
}
