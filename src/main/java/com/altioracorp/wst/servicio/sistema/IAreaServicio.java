package com.altioracorp.wst.servicio.sistema;

import java.util.List;

import com.altioracorp.wst.dominio.sistema.Area;
import com.altioracorp.wst.servicio.ICRUD;

public interface IAreaServicio extends ICRUD<Area, Long> {
			
	List<Area> buscarTodosActivos();
}
