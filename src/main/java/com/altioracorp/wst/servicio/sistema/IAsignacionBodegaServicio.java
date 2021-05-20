package com.altioracorp.wst.servicio.sistema;

import java.util.List;

import com.altioracorp.wst.dominio.sistema.AsignacionBodega;
import com.altioracorp.wst.dominio.sistema.ConfiguracionUsuarioPerfil;
import com.altioracorp.wst.servicio.ICRUD;

public interface IAsignacionBodegaServicio extends ICRUD<AsignacionBodega, Long> {
			
	List<AsignacionBodega> buscarPorConfiguracionUsuarioPerfil(ConfiguracionUsuarioPerfil ConfiguracionUsuarioPerfil);
	
	List<AsignacionBodega> modificarLista(List<AsignacionBodega> asignacionesBodega);
}
