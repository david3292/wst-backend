package com.altioracorp.wst.repositorio.sistema;

import java.util.Optional;

import com.altioracorp.wst.dominio.sistema.Area;
import com.altioracorp.wst.repositorio.RepositorioBase;

public interface IAreaRepositorio extends RepositorioBase<Area> {
	
	Optional<Area> findByCodigo(String codigo);
}
