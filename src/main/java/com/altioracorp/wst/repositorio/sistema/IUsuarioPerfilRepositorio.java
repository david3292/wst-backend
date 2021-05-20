package com.altioracorp.wst.repositorio.sistema;

import com.altioracorp.wst.dominio.sistema.Perfil;
import com.altioracorp.wst.dominio.sistema.PerfilNombre;
import com.altioracorp.wst.dominio.sistema.Usuario;
import com.altioracorp.wst.dominio.sistema.UsuarioPerfil;
import com.altioracorp.wst.repositorio.RepositorioBase;

import java.util.List;
import java.util.Optional;

public interface IUsuarioPerfilRepositorio extends RepositorioBase<UsuarioPerfil> {

	List<UsuarioPerfil> findByUsuario(Usuario usuario);

	List<UsuarioPerfil> findByUsuario_NombreUsuario(String usuarioNombre);

	List<UsuarioPerfil> findByPerfil_Nombre(PerfilNombre perfilNombre);

	Optional<UsuarioPerfil> findByUsuarioAndPerfil(Usuario usuario, Perfil perfil);

	Optional<UsuarioPerfil> findByUsuario_NombreUsuarioAndPerfil_Nombre(String usuarioNombre, PerfilNombre perfilNombre);

	Optional<UsuarioPerfil> findByUsuario_NombreUsuarioAndPerfil_Id(String nombreUsuario, Long idPerfil);
}
