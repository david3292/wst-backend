package com.altioracorp.wst.servicio.ventas;

public interface ICompartimientoServicio {

	String ObtenerCompartimientoPorCodigoArticuloYBodega(String codigoArticulo, String codigoBodega);
	
	String obtenerCompartimientoMayorCantidadPorCodigoArticuloYBodega(String codigoArticulo, String codigoBodega);
	
	String obtenerCompartimientoConUltimoMovimientoPorCodigoArticuloYBodega(String codigoArticulo, String codigoBodega);
	
	String obtenerCompartimientoPredeterminado();

}
