package com.altioracorp.wst.repositorio.cobros;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

import com.altioracorp.wst.dominio.cobros.Cobro;
import com.altioracorp.wst.dominio.cobros.CobroEstado;
import com.altioracorp.wst.repositorio.RepositorioBase;

public interface ICobroRepositorio extends RepositorioBase<Cobro>{

	Optional<Cobro> findByCodigoClienteAndEstadoAndModificadoPor(String codigoCliente, CobroEstado estado, String usuario);

	List<Cobro> findByModificadoPorAndEstadoInAndPuntoVenta_Id(String usuario, Collection<CobroEstado> estados, Long id);
	
	List<Cobro> findByCodigoClienteAndEstadoIn(String codigoCliente, Collection<CobroEstado> estados);
	
	Optional<Cobro> findByNumero(String numero);
	
	List<Cobro> findByCreadoPorAndFechaBetween(String usuario, LocalDateTime fechaInicio, LocalDateTime fechaFin);

	Cobro findBycobroFormaPagos_Id(Long id);
	
	List<Cobro> findByEstadoIn(Collection<CobroEstado> estados);

}
