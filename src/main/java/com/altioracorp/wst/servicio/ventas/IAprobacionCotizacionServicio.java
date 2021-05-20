package com.altioracorp.wst.servicio.ventas;

import com.altioracorp.wst.dominio.ventas.Cotizacion;

public interface IAprobacionCotizacionServicio extends IAprobacionDocumentoBase{

	void lanzarSolictud(Cotizacion cotizacion);
	
}
