package com.altioracorp.wst.servicio.ventas;

public interface IAprobacionFacturaServicio extends IAprobacionDocumentoBase {
	
	void lanzarSolictud(Long facturaID);
}
