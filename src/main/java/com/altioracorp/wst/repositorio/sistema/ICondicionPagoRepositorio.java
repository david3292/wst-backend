package com.altioracorp.wst.repositorio.sistema;

import java.util.List;
import java.util.Optional;

import com.altioracorp.wst.dominio.sistema.CondicionPago;
import com.altioracorp.wst.repositorio.RepositorioBase;

public interface ICondicionPagoRepositorio extends RepositorioBase<CondicionPago> {
	
	List<CondicionPago> findByActivoTrue();
	
	Optional<CondicionPago> findByTermino(String termino);
}
