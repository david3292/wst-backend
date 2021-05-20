package com.altioracorp.wst.util;

import java.text.Collator;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;

public class UtilidadesCadena {
	
	public static final String ND_CORTO = "N/D";
	public static final String ND_LARGO = "No Disponible";
	
	public static final boolean algunoEsNuloOBlanco(String... cadenas) {
		
		for (String cadena : cadenas) {
			if (esNuloOBlanco(cadena)) {
				return true;
			}
		}
		
		return false;
	}
	
	public static final boolean algunoNoEsNuloNiBlanco(String... cadenas) {
		
		for (String cadena : cadenas) {
			if (noEsNuloNiBlanco(cadena)) {
				return true;
			}
		}
		
		return false;
	}
	
	public static final String blancoONDCorto(boolean usarND) {
		return usarND ? ND_CORTO : "";
	}
	
	public static final String blancoONDLargo(boolean usarND) {
		return usarND ? ND_LARGO : "";
	}
	
	public static final String cadenaAlfabeticaAleatoria(int longitud) {
		return RandomStringUtils.randomAlphabetic(longitud);
	}
	
	public static final String cadenaDeLongitudMayorQue(int longitud) {
		return StringUtils.repeat("X", longitud + 1);
	}
	
	public static final int cambiarStringAInt(final String valor) {
		return Integer.parseInt(valor);
	}
	
	public static final Collator collator() {
		return Collator.getInstance(new Locale("es_EC"));
	}

	public static final String completarCerosIzquierda(long numero, int totalCaracteres) {
		return completarCerosIzquierda(String.valueOf(numero), totalCaracteres);
	}
	
	public static final String completarCerosIzquierda(String cadena, int totalCaracteres) {
		return StringUtils.leftPad(cadena, totalCaracteres, "0");
	}
	
	public static final String concatenarNombres(String... nombres) {
		return Stream.of(nombres)
				.filter((nombre) -> noEsNuloNiBlanco(nombre))
				.map((nombre) -> normalizarEspacios(nombre))
				.collect(Collectors.joining(" "));
	}
	
	public static final int contarOcurrencias(final String cadena, final String subcadena) {
		return StringUtils.countMatches(cadena, subcadena);
	}
	
	public static final boolean contieneCadena(String plantilla, String constante) {
		return plantilla.contains(constante);
	}
	
	public static final List<String> descomponerPorLongitud(String cadena, int longitud) {
		
		final List<String> componentes = new ArrayList<>();
		
		String restante = cadena;
		
		while (true) {
			final String izquierda = StringUtils.left(restante, longitud);
			componentes.add(izquierda);
			restante = StringUtils.substring(restante, izquierda.length());
			if (esNuloOBlanco(restante)) break;
		}
		
		return componentes;
	}
	
	public static final String eliminarEspaciosSufijoPrefijo(String cadena) {
		return StringUtils.trim(cadena);
	}
	
	public static final String enmascarar(String cadena, int longitudMaxima) {
		return StringUtils.abbreviateMiddle(cadena, "***", longitudMaxima);
	}
	
	public static final String enmascararDireccionCorreo(String direccionCorreo) {
		
		if (noEsNuloNiBlanco(direccionCorreo)) {
		
			final String[] partes = direccionCorreo.trim().split("@");
			
			if (partes.length == 2) {
				
				final String buzon = partes[0];
				final String dominio = partes[1];
				
				final String buzonEnmascarado;
				
				if (buzon.length() > 5) {		
					buzonEnmascarado = StringUtils.abbreviateMiddle(buzon, "***", 5);
				} else {
					buzonEnmascarado = "***";
				}
				
				return buzonEnmascarado + "@" + dominio;
			}
		}
			
		return "***";
	}
	
	public static final boolean esCadenaAlfabeticaOEspacios(final String cadena) {
		return StringUtils.isAlphaSpace(cadena);
	}
	
	public static final boolean esNuloOBlanco(String cadena) {
		return StringUtils.isBlank(cadena);
	}
	
	public static final boolean esNumero(String cadena) {
		return NumberUtils.isDigits(cadena);
	}
	
	public static final String generarCadenaAlfanumericaAleatoriaDeLogitud(int longitud) {
        return RandomStringUtils.randomAlphanumeric(longitud);
	}
	
	public static final String juntar(Collection<String> cadenas, String separador) {
		return StringUtils.join(cadenas, separador);
	}
	
	public static final String juntarNombres(final String... nombres) {
		return Stream.of(nombres).map(UtilidadesCadena::normalizarEspacios).collect(Collectors.joining(" "));
	}
	
	public static final boolean longitudInvalida(String cadena, int longitudMinima, int longitudMaxima) {
		return (cadena.length() < longitudMinima) || (cadena.length() > longitudMaxima);
	}
	
	public static final boolean longitudMaximaInvalida(String cadena, int longitudMaxima) {
		return cadena.length() > longitudMaxima;
	}

	public static final boolean longitudNormalizadaMayorOIgualQue(String cadena, int longitud) {
		return normalizarEspacios(cadena).length() >= longitud;
	}
	
	public static final boolean ningunoEsNuloNiBlanco(String... cadenas) {
		
		for (String cadena : cadenas) {
			if (esNuloOBlanco(cadena)) {
				return false;
			}
		}
		
		return true;
	}
	
	public static final boolean noEsNulo(Object objeto) {
		return objeto != null;
	}
	
	public static final boolean noEsNuloNiBlanco(String cadena) {
		return StringUtils.isNotBlank(cadena);
	}
	
	public static final String normalizarCodigo(String codigo) {
		return StringUtils.trim(codigo).toUpperCase();
	}
	
	public static final String normalizarDireccion(String direccion) {
		return normalizarEspacios(direccion).toUpperCase();
	}
	
	public static final String normalizarDireccionCorreoElectronico(final String direccionCorreoElectronico) {
		return normalizarEspacios(direccionCorreoElectronico).toLowerCase();
	}
	
	public static final String normalizarEspacios(String cadena) {
		return StringUtils.normalizeSpace(cadena);
	}
	
	public static final String normalizarNombre(String nombre) {
		return normalizarEspacios(nombre).toUpperCase();
	}
	
	public static final String normalizarNumeroIdentificacion(String cadena) {
		return StringUtils.normalizeSpace(cadena).toUpperCase();
	}
	
	public static final String normalizarTelefono(String telefono) {
		return normalizarEspacios(telefono);
	}
	
	public static final String removerCaracteresEspeciales(String cadena) {
		return StringUtils.stripAccents(cadena);
	}
	
	public static final String removerEspacios(String cadena) {
		return StringUtils.deleteWhitespace(cadena);
	}
	
	public static final String removerPrefijoSiExiste(String valor, String prefijo) {
		return StringUtils.removeStart(valor, prefijo);
	}
	
	public static final String removerSufijoSiExiste(String valor, String sufijo) {
		return StringUtils.removeEnd(valor, sufijo);
	}
	
	public static final String siNo(boolean valor) {
		return valor ? "Sí" : "No";
	}
	
	public static final String sustituirCaracteresEspanolPotencialesPor(final String cadenaOriginal, final String sustituto) {
		return StringUtils.normalizeSpace(cadenaOriginal).replaceAll("[AEIOUaeiouÁÉÍÓÚÜáéíóúü]", sustituto);
	}
	
	public static final String truncar(String cadena, int longitudMaxima) {
		return StringUtils.left(cadena, longitudMaxima);
	}
	
	public static final String valorOBlanco(String valor) {
		return noEsNuloNiBlanco(valor) ? valor : "";
	}
	
	public static final String valorONDCorto(String valor) {
		return noEsNuloNiBlanco(valor) ? valor : ND_CORTO;
	}
	
	public static final String valorONDLargo(String valor) {
		return noEsNuloNiBlanco(valor) ? valor : ND_LARGO;
	}
	
	public static final String valorONulo(String valor) {
		return noEsNuloNiBlanco(valor) ? valor : null;
	}
	
	public static final String valorOPorDefecto(String valor, String porDefecto) {
		return noEsNuloNiBlanco(valor) ? valor : porDefecto;
	}
	
	public static final String valorTruncadoONulo(String valor, int longitud) {
		return noEsNuloNiBlanco(valor) ? truncar(valor, longitud) : null;
	}
	
	private UtilidadesCadena() {}
}
