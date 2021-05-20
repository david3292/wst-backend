package com.altioracorp.wst.repositorio.sistema;

import java.util.List;

import com.altioracorp.wst.dominio.sistema.AsignacionBodega;
import com.altioracorp.wst.dominio.sistema.ConfiguracionUsuarioPerfil;
import com.altioracorp.wst.dominio.sistema.UsuarioPerfil;
import com.altioracorp.wst.repositorio.RepositorioBase;

public interface IAsignacionBodegaRepositorio extends RepositorioBase<AsignacionBodega> {
	
	List<AsignacionBodega> findByConfiguracionUsuarioPerfil(ConfiguracionUsuarioPerfil configuracion);
	
	List<AsignacionBodega> findByConfiguracionUsuarioPerfil_UsuarioPerfilAndAccesoTrue(UsuarioPerfil usuarioPerfil);
}
