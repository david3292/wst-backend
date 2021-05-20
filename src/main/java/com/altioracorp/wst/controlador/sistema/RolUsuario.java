package com.altioracorp.wst.controlador.sistema;

public enum RolUsuario {

	SUPERADMIN("SUPERADMIN"), ADMIN("Administrador");

	private String etiqueta;

	private RolUsuario(String etiqueta) {
		this.etiqueta = etiqueta;
	}

	public String getEtiqueta() {
		return etiqueta;
	}
	
}
