package com.altioracorp.wst.repositorio.sistema;

import java.util.List;
import java.util.Optional;

import com.altioracorp.wst.dominio.sistema.FormaPago;
import com.altioracorp.wst.dominio.sistema.FormaPagoNombre;
import com.altioracorp.wst.repositorio.RepositorioBase;

public interface IFormaPagoRepositorio extends RepositorioBase<FormaPago> {

	Optional<FormaPago> findByNombre(FormaPagoNombre nombre);
	
	List<FormaPago> findByActivoTrue();
	
}
