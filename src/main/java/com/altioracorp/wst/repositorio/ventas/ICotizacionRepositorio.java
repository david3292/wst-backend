package com.altioracorp.wst.repositorio.ventas;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

import com.altioracorp.wst.dominio.ventas.Cotizacion;
import com.altioracorp.wst.dominio.ventas.CotizacionEstado;
import com.altioracorp.wst.repositorio.RepositorioBase;

public interface ICotizacionRepositorio extends RepositorioBase<Cotizacion>  {

	List<Cotizacion> findByCreadoPorAndPuntoVenta_idOrderByFechaEmisionDesc(String usuario, Long id);
	
	List<Cotizacion> findByEstadoAndActivoTrueAndFechaVencimientoNotNull(CotizacionEstado estado);
	
	Optional<Cotizacion> findByNumero(String numero);
	
	List<Cotizacion> findByCodigoClienteAndEstadoInAndActivoTrue(String codigoCliente, Collection<CotizacionEstado> estados);
}
