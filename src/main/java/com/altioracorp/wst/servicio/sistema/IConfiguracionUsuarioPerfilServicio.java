package com.altioracorp.wst.servicio.sistema;

import java.util.List;
import java.util.Optional;

import com.altioracorp.wst.dominio.sistema.ConfiguracionUsuarioPerfil;
import com.altioracorp.wst.dominio.sistema.PerfilNombre;
import com.altioracorp.wst.dominio.sistema.PuntoVenta;
import com.altioracorp.wst.dominio.sistema.Usuario;
import com.altioracorp.wst.dominio.sistema.UsuarioPerfil;
import com.altioracorp.wst.servicio.ICRUD;

public interface IConfiguracionUsuarioPerfilServicio extends ICRUD<ConfiguracionUsuarioPerfil, Long> {
			
	Optional<ConfiguracionUsuarioPerfil> buscarPorUsuarioPerfilYPuntoVenta(UsuarioPerfil usuarioPerfil, PuntoVenta puntoVenta);
	
	List<ConfiguracionUsuarioPerfil> buscarPorUsuarioPerfil(UsuarioPerfil usuarioPerfil);
	
	List<ConfiguracionUsuarioPerfil> buscarPorUsuario(Usuario usuario);
	
	List<PuntoVenta> buscarPuntosDeVentasPorUsuarioEnSesionYPerfil(PerfilNombre perfil);
	
	List<PuntoVenta> buscarPuntosDeVentasPorUsuarioYPerfil(String usuario, PerfilNombre perfil);

}
