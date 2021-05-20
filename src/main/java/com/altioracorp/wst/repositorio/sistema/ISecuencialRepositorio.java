package com.altioracorp.wst.repositorio.sistema;

import com.altioracorp.wst.dominio.sistema.PuntoVenta;
import com.altioracorp.wst.dominio.sistema.Secuencial;
import com.altioracorp.wst.dominio.sistema.TipoDocumento;
import com.altioracorp.wst.repositorio.RepositorioBase;

public interface ISecuencialRepositorio extends RepositorioBase<Secuencial> {

	Secuencial findByPuntoVentaAndTipoDocumento(PuntoVenta puntoVenta, TipoDocumento tipoDocumento);

}
