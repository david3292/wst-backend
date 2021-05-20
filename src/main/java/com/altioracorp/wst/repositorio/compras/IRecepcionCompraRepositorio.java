package com.altioracorp.wst.repositorio.compras;

import java.util.Collection;
import java.util.List;

import com.altioracorp.wst.dominio.compras.RecepcionCompra;
import com.altioracorp.wst.dominio.compras.RecepcionCompraEstado;
import com.altioracorp.wst.repositorio.RepositorioBase;

public interface IRecepcionCompraRepositorio extends RepositorioBase<RecepcionCompra> {

	List<RecepcionCompra> findByOrdenCompra_Id(long ordenCompraId);
	
	List<RecepcionCompra> findByEstadoInAndOrdenCompra_BodegaEntrega(Collection<RecepcionCompraEstado> estados, String bodegaEntrega);
	
	List<RecepcionCompra> findByEstadoIn(Collection<RecepcionCompraEstado> estados);
	
	List<RecepcionCompra> findByOrdenCompra_Cotizacion_Id(long id);
}
