package com.altioracorp.wst.util.validacionNumeroIdentificacion;

import static com.altioracorp.wst.util.validacionNumeroIdentificacion.ValidacionNumeroIdentificacionOperacionesComunes.calcularModulo10;

import java.util.regex.Pattern;

public class ValidadorCedula {
	
	private static final short[] COEFICIENTES = new short[] {2, 1, 2, 1, 2, 1, 2, 1, 2};
	private static final String PATRON = "^\\d{10}$";

	private static short extraerTipo(String numeroIdentificacion) {
		final String tipoCadena = numeroIdentificacion.substring(2, 3);
		return Short.parseShort(tipoCadena);
	}

	public static void validar(String numeroIdentificacion) throws ValidacionNumeroIdentificacionException {
		
		validarPatron(numeroIdentificacion);		
		validarProvincia(numeroIdentificacion);
		
		final short tipoDigito = extraerTipo(numeroIdentificacion);
		
		// TODO Arreglo temporal hasta saber de forma oficial cómo validar las cédulas de tipo 6.
		if (tipoDigito != 6) {
			validarTipo(tipoDigito);
			validarIdentificador(numeroIdentificacion);			
		}
	}

	private static void validarIdentificador(String numeroIdentificacion) throws ValidacionNumeroIdentificacionDigitoVerificadorInvalidoException {
		
		final String base = numeroIdentificacion.substring(0, 9);
		final short verificadorExtraido = Short.parseShort(numeroIdentificacion.substring(9, 10));
		final short verificadorCalculado = calcularModulo10(base, COEFICIENTES);
		
		if (verificadorExtraido != verificadorCalculado) {
			throw new ValidacionNumeroIdentificacionDigitoVerificadorInvalidoException(verificadorCalculado, verificadorExtraido);
		}
	}

	private static void validarPatron(String numeroIdentificacion) throws ValidacionNumeroIdentificacionPatronInvalidoException {
		if (!Pattern.compile(PATRON).matcher(numeroIdentificacion).matches()) {
			throw new ValidacionNumeroIdentificacionPatronInvalidoException();
		}
	}

	private static void validarProvincia(String numeroIdentificacion) throws ValidacionNumeroIdentificacionProvinciaInvalidaException {
		final String provinciaCadena = numeroIdentificacion.substring(0, 2);
		final short digitosProvincia = Short.parseShort(provinciaCadena);
		ValidacionNumeroIdentificacionOperacionesComunes.validarProvincia(digitosProvincia);
	}

	private static void validarTipo(short tipoDigito) throws ValidacionNumeroIdentificacionCedulaTipoInvalidoException {
		if ((tipoDigito > 5)) {
			throw new ValidacionNumeroIdentificacionCedulaTipoInvalidoException(tipoDigito);
		}
	}
}
