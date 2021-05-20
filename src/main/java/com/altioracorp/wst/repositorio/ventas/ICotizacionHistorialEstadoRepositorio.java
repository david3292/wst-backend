package com.altioracorp.wst.repositorio.ventas;

import java.util.List;

import com.altioracorp.wst.dominio.ventas.CotizacionHistorialEstado;
import com.altioracorp.wst.dominio.ventas.Cotizacion;
import com.altioracorp.wst.repositorio.RepositorioBase;

public interface ICotizacionHistorialEstadoRepositorio extends RepositorioBase<CotizacionHistorialEstado>  {

	List<CotizacionHistorialEstado> findByCotizacionOrderByCreadoFechaAsc(Cotizacion documento);
	
	List<CotizacionHistorialEstado> findByCotizacion_idOrderByCreadoFechaAsc(Long cotizacionID);
		
}
