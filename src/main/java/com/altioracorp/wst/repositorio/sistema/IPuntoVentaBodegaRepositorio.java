package com.altioracorp.wst.repositorio.sistema;

import java.util.List;
import java.util.Optional;

import com.altioracorp.wst.dominio.sistema.Bodega;
import com.altioracorp.wst.dominio.sistema.PuntoVenta;
import com.altioracorp.wst.dominio.sistema.PuntoVentaBodega;
import com.altioracorp.wst.repositorio.RepositorioBase;

public interface IPuntoVentaBodegaRepositorio extends RepositorioBase<PuntoVentaBodega> {	

	List<PuntoVentaBodega> findByPuntoVenta(PuntoVenta puntoVenta);
	
	List<PuntoVentaBodega> findByPuntoVenta_Nombre(String puntoVenta);
	
	Optional<PuntoVentaBodega> findByPrincipalTrueAndPuntoVenta_Nombre(String puntoVentaNombre);
	
	Optional<PuntoVentaBodega> findByPuntoVentaAndBodega(PuntoVenta puntoVenta, Bodega bodega);
	
	Optional<PuntoVentaBodega> findByPrincipalTrueAndPuntoVenta(PuntoVenta puntoVenta);
	
	List<PuntoVentaBodega> findByActivoTrueAndPrincipalTrueAndBodega_Codigo(String codigoBodega);
}
