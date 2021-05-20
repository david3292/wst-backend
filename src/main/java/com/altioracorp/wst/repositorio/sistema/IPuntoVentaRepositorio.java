package com.altioracorp.wst.repositorio.sistema;

import java.util.Optional;

import com.altioracorp.wst.dominio.sistema.PuntoVenta;
import com.altioracorp.wst.repositorio.RepositorioBase;

public interface IPuntoVentaRepositorio extends RepositorioBase<PuntoVenta> {	
	
	Optional<PuntoVenta> findByNombre(String nombre);
}
