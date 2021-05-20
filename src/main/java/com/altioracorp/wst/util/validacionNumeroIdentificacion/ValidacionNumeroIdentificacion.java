package com.altioracorp.wst.util.validacionNumeroIdentificacion;

import static com.altioracorp.wst.util.UtilidadesCadena.esNuloOBlanco;

import com.altioracorp.wst.dominio.ventas.TipoIdentificacion;



public class ValidacionNumeroIdentificacion {

	public static final boolean validar(final TipoIdentificacion tipo, final String numeroIdentificacion) {
		try {
			validarConDescripcion(tipo, numeroIdentificacion);
			return true;
		} catch (ValidacionNumeroIdentificacionException e) {
			return false;
		}
	}
	
	public static final void validarConDescripcion(final TipoIdentificacion tipo, final String numero) throws ValidacionNumeroIdentificacionException {
		
		if (tipo == null) {
			throw new ValidacionNumeroIdentificacionTipoInvalidoException(null);
		}
		
		if (esNuloOBlanco(numero)) {
			throw new ValidacionNumeroIdentificacionNumeroEnBlancoException();
		}
		
		switch (tipo) {
		case CEDULA:
			ValidadorCedula.validar(numero);
			break;
		case RUC:
			ValidadorRuc.validar(numero);
			break;
		case PASAPORTE:
			break;
		default:
			throw new ValidacionNumeroIdentificacionTipoInvalidoException(tipo);
		}
	}
	
	private ValidacionNumeroIdentificacion() {}
}
