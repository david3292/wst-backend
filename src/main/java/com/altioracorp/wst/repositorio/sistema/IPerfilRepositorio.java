package com.altioracorp.wst.repositorio.sistema;

import java.util.Optional;

import com.altioracorp.wst.dominio.sistema.Perfil;
import com.altioracorp.wst.dominio.sistema.PerfilNombre;
import com.altioracorp.wst.repositorio.RepositorioBase;

public interface IPerfilRepositorio extends RepositorioBase<Perfil> {

	Optional<Perfil> findByNombre(PerfilNombre nombre);

}
