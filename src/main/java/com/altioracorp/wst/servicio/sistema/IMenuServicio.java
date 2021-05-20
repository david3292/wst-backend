package com.altioracorp.wst.servicio.sistema;

import java.util.List;

import com.altioracorp.wst.dominio.sistema.Menu;
import com.altioracorp.wst.dominio.sistema.UsuarioMenuDTO;
import com.altioracorp.wst.dominio.sistema.UsuarioPerfil;

public interface IMenuServicio {

	List<Menu> listarMenuPorUsuario(String usuario);
	
	UsuarioMenuDTO listarUsuarioMenuPorUsuario(UsuarioPerfil usuarioPerfil);
	
}
