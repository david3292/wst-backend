package com.altioracorp.wst.repositorio.sistema;

import java.util.Optional;

import com.altioracorp.wst.dominio.sistema.Usuario;
import com.altioracorp.wst.repositorio.RepositorioBase;

public interface IUsuarioRepositorio extends  RepositorioBase<Usuario> {

	Optional<Usuario> findByNombreUsuario(String nombre);
	Optional<Usuario> findByNombreUsuarioAndActivoTrue(String nombre);
}
