package com.altioracorp.wst.repositorio.sistema;

import java.util.List;
import java.util.Optional;

import com.altioracorp.wst.dominio.sistema.Area;
import com.altioracorp.wst.dominio.sistema.Cargo;
import com.altioracorp.wst.repositorio.RepositorioBase;

public interface ICargoRepositorio extends RepositorioBase<Cargo> {
	
	Optional<Cargo> findByNombre(String nombre);
	
	List<Cargo> findByAreaAndActivoTrue(Area area);
}
