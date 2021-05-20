package com.altioracorp.wst.dominio.sistema;

import java.util.List;

public class UsuarioMenuDTO {

	private String nombreCompleto;
	
	private String nombreUsuario;
	
	private List<Menu> menus;
	
	public UsuarioMenuDTO() {};	

	public UsuarioMenuDTO(String nombreCompleto, String nombreUsuario, List<Menu> menus) {
		this.nombreCompleto = nombreCompleto;
		this.nombreUsuario = nombreUsuario;
		this.menus = menus;
	}

	public String getNombreCompleto() {
		return nombreCompleto;
	}

	public void setNombreCompleto(String nombreCompleto) {
		this.nombreCompleto = nombreCompleto;
	}

	public String getNombreUsuario() {
		return nombreUsuario;
	}

	public void setNombreUsuario(String nombreUsuario) {
		this.nombreUsuario = nombreUsuario;
	}

	public List<Menu> getMenus() {
		return menus;
	}

	public void setMenus(List<Menu> menus) {
		this.menus = menus;
	}
	
}
