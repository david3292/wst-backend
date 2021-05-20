package com.altioracorp.wst.util;

import static com.altioracorp.wst.util.UtilidadesCadena.enmascarar;
import static com.altioracorp.wst.util.UtilidadesCadena.esNuloOBlanco;

import java.nio.file.Files;
import java.nio.file.Paths;

import org.apache.commons.logging.Log;

public class UtilidadesConfiguracion {

	private static final boolean esVariableEntornoNoSustituida(String valor) {
		return valor.startsWith("${");
	}

	public static final void informarValorInvalido(String parametro, String valor, Log log) {
		final String mensaje = String.format("El valor para el parámetro %s es inválido: %s", parametro, valor);
		log.error(mensaje);
		lanzarExcepcion(mensaje);
	}
	
	public static final void informarValorValido(String parametro, String valor, Log log) {
		log.info(String.format("El valor para el parámetro %s es válido: %s", parametro, valor));
	}
	
	private static final void lanzarExcepcion(String mensaje) {
		throw new IllegalArgumentException(mensaje);
	}

	public static final void notificarValorOpcional(String parametro, String valor, Log log) {
		if (valorNoEstaDefinido(valor)) {
			log.warn(String.format("No se definió el valor opcional %s", parametro));			
		} else {
			log.info(String.format("Se definió el valor opcional %s: %s", parametro, valor));
		}
	}

	private static boolean valorNoEstaDefinido(String valor) {
		return esNuloOBlanco(valor) || (esVariableEntornoNoSustituida(valor));
	}
	
	private static final void verificarArchivoExiste(String descripcion, String rutaArchivo, Log log) {
		if (!Files.exists(Paths.get(rutaArchivo))) {
			final String mensaje = String.format("El archivo %s no existe en la ruta %s", descripcion, rutaArchivo);
			log.error(mensaje);
			lanzarExcepcion(mensaje);
		} else {
			log.info("El archivo existe para " + descripcion);
		}
	}
	
	public static final void verificarValor(String parametro, String valor, Log log) {
		verificarValor(parametro, valor, log, false);
	}
	
	public static final void verificarValor(String parametro, String valor, Log log, boolean mostrarValor) {
		verificarValor(parametro, valor, log, mostrarValor, Integer.MAX_VALUE);
	}
	
	public static final void verificarValor(String parametro, String valor, Log log, boolean mostrarValor, int valorEnmascaradoLongitudMaxima) {
		if (valorNoEstaDefinido(valor)) {
			final String mensaje = String.format("No se especificó el valor del parámetro %s: %s", parametro, valor);
			log.error(mensaje);
			lanzarExcepcion(mensaje);
		} else {
			log.info(parametro + " tiene valor" + (mostrarValor ? ": " + enmascarar(valor, valorEnmascaradoLongitudMaxima) : "."));
		}
	}

	public static final void verificarValorNumerico(String parametro, String valor, Log log, boolean mostrarValor) {
		
		verificarValor(parametro, valor, log, mostrarValor);
		
		try {
			Integer.parseInt(valor);
		} catch (NumberFormatException e) {
			informarValorInvalido(parametro, valor, log);
		}
	}
	
	public static final void verificarValorYArchivoExiste(String parametro, String valor, Log log) {
		verificarValor(parametro, valor, log);
		verificarArchivoExiste(parametro, valor, log);
	}
	
	private UtilidadesConfiguracion() {}
}
