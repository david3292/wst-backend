package com.altioracorp.wst.servicio.sistema;

import java.util.List;

import com.altioracorp.wst.dominio.sistema.Bodega;
import com.altioracorp.wst.dominio.sistema.PuntoVenta;
import com.altioracorp.wst.dominio.sistema.PuntoVentaBodega;
import com.altioracorp.wst.servicio.ICRUD;

public interface IPuntoVentaBodegaServicio extends ICRUD<PuntoVentaBodega, Long>{

	List<Bodega> buscarBodegasPorPuntoVenta(PuntoVenta puntoVenta);
	
	List<PuntoVentaBodega> listarBodegasPorPuntoVenta(PuntoVenta puntoVenta);
	
	Bodega buscarBodegaPrincipalPorPuntoVenta(PuntoVenta puntoVenta);
	
	List<PuntoVentaBodega> listarBodegasPrincipalesPorCodigo(String codigoBodega);
}
