package com.altioracorp.wst.repositorio.sistema;

import java.util.Optional;

import com.altioracorp.wst.dominio.sistema.ConfigSistema;
import com.altioracorp.wst.dominio.sistema.ConfiguracionSistema;
import com.altioracorp.wst.repositorio.RepositorioBase;

public interface IConfiguracionSistemaRepositorio extends RepositorioBase<ConfiguracionSistema> {
	
	Optional<ConfiguracionSistema> findByNombre(ConfigSistema nombre);
}
