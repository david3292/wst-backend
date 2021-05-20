package com.altioracorp.wst.repositorio.sistema;

import java.util.Optional;

import com.altioracorp.wst.dominio.sistema.SecuencialArticulo;
import com.altioracorp.wst.repositorio.RepositorioBase;

public interface ISecuencialArticuloRepositorio extends RepositorioBase<SecuencialArticulo> {

	Optional<SecuencialArticulo> findByClase(String clase);
	
}
