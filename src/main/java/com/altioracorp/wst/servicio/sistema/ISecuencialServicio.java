package com.altioracorp.wst.servicio.sistema;

import com.altioracorp.wst.dominio.sistema.Secuencial;
import com.altioracorp.wst.dominio.sistema.TipoDocumento;
import com.altioracorp.wst.servicio.ICRUD;

public interface ISecuencialServicio extends ICRUD<Secuencial, Long> {
	
	Secuencial ObtenerSecuencialPorPuntoVentaYTipoDocumento(long puntoVentaId, TipoDocumento tipoDocumento);
}
