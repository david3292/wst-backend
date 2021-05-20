package com.altioracorp.wst.repositorio.sistema;

import java.util.List;
import java.util.Optional;

import com.altioracorp.wst.dominio.sistema.ConfiguracionUsuarioPerfil;
import com.altioracorp.wst.dominio.sistema.PerfilNombre;
import com.altioracorp.wst.dominio.sistema.PuntoVenta;
import com.altioracorp.wst.dominio.sistema.Usuario;
import com.altioracorp.wst.dominio.sistema.UsuarioPerfil;
import com.altioracorp.wst.repositorio.RepositorioBase;

public interface IConfiguracionUsuarioPerfilRepositorio extends RepositorioBase<ConfiguracionUsuarioPerfil> {
	
	List<ConfiguracionUsuarioPerfil> findByUsuarioPerfil(UsuarioPerfil usuarioPerfil);
	
	Optional<ConfiguracionUsuarioPerfil> findByUsuarioPerfilAndPuntoVenta(UsuarioPerfil usuarioPerfil, PuntoVenta puntoVenta);
	
	List<ConfiguracionUsuarioPerfil> findByUsuarioPerfil_Usuario(Usuario usuario);
	
	List<ConfiguracionUsuarioPerfil> findByUsuarioPerfil_Usuario_NombreUsuario(String nombreUsuario);
	
	Optional<ConfiguracionUsuarioPerfil> findByPuntoVentaAndUsuarioPerfil_Usuario(PuntoVenta puntoVenta, Usuario usuario);
	
	List<ConfiguracionUsuarioPerfil> findByUsuarioPerfil_Usuario_NombreUsuarioAndUsuarioPerfil_Perfil_nombre(String nombreUsuario, PerfilNombre perfil);
	
}
