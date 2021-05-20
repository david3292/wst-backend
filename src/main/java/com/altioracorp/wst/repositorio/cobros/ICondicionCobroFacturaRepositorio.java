package com.altioracorp.wst.repositorio.cobros;

import java.util.Collection;
import java.util.List;

import com.altioracorp.wst.dominio.cobros.CondicionCobroEstado;
import com.altioracorp.wst.dominio.cobros.CondicionCobroFactura;
import com.altioracorp.wst.repositorio.RepositorioBase;

public interface ICondicionCobroFacturaRepositorio extends RepositorioBase<CondicionCobroFactura> {

	List<CondicionCobroFactura> findByEstadoInAndFacturaCodigoClienteOrderByFechaAsc(
			Collection<CondicionCobroEstado> estados, String codigoCliente);

	List<CondicionCobroFactura> findByEstadoInAndFactura_IdOrderByNumeroCuotaAsc(Collection<CondicionCobroEstado> estados, Long id);
	
	List<CondicionCobroFactura> findByEstadoAndFechaNotNull(CondicionCobroEstado estado);
	
	List<CondicionCobroFactura> findByFactura_CodigoCliente(String codigoCliente);
	
	boolean existsByFactura_Cotizacion_CodigoClienteAndEstado(String codigoCliente, CondicionCobroEstado estado);
	
	List<CondicionCobroFactura> findByFactura_Id(Long facturaId);
}
