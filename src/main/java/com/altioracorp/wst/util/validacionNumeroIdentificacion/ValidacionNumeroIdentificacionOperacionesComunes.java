package com.altioracorp.wst.util.validacionNumeroIdentificacion;

public class ValidacionNumeroIdentificacionOperacionesComunes {
	
	public static short calcularModulo10(String base, short[] coeficientes) {
		
		int sumaMultiplicaciones = 0;
		
		for (int i = 0; i < base.length(); i++) {
			final int digito = Short.parseShort(base.substring(i, i + 1));
			final int multiplicacion = digito * coeficientes[i];
			final int sumaDigitos = (multiplicacion / 10) + (multiplicacion % 10);
			sumaMultiplicaciones += sumaDigitos;
		}
		
		final short remanente = (short)(sumaMultiplicaciones % 10);
		
		return (short)((remanente == 0) ? 0 : 10 - remanente);
	}
	
	public static short calcularModulo11(String base, short[] coeficientes) {

		int sumaMultiplicaciones = 0;
		
		for (int i = 0; i < base.length(); i++) {
			final int digito = Short.parseShort(base.substring(i, i + 1));
			final int multiplicacion = digito * coeficientes[i];
			sumaMultiplicaciones += multiplicacion;
		}
		
		final short remanente = (short)(sumaMultiplicaciones % 11);
		
		return (short)((remanente == 0) ? 0 : 11 - remanente);
	}
	
	public static void validarProvincia(short digitosProvincia) throws ValidacionNumeroIdentificacionProvinciaInvalidaException {
		
		if ((digitosProvincia < 1) 
				|| ((digitosProvincia > 24) && (digitosProvincia != 30))) {
			
			throw new ValidacionNumeroIdentificacionProvinciaInvalidaException(digitosProvincia);
		}
	}
}
