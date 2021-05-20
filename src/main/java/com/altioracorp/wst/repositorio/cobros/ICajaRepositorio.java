package com.altioracorp.wst.repositorio.cobros;

import java.util.List;

import com.altioracorp.wst.dominio.cobros.Caja;
import com.altioracorp.wst.repositorio.RepositorioBase;

public interface ICajaRepositorio extends RepositorioBase<Caja>{

	List<Caja> findByCreadoPorAndPuntoVenta_IdOrderByFechaCierreDesc(String usuario, Long puntoVentaID);
}
