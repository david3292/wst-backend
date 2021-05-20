package com.altioracorp.wst.servicio.sistema;

import java.util.List;

import com.altioracorp.wst.dominio.sistema.Area;
import com.altioracorp.wst.dominio.sistema.Cargo;
import com.altioracorp.wst.servicio.ICRUD;

public interface ICargoServicio extends ICRUD<Cargo, Long> {
			
	List<Cargo> buscarTodosActivos();
	List<Cargo> buscarPorAreaYActivos(Area area);
}
