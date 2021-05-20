package com.altioracorp.wst.exception.sistema;

@SuppressWarnings("serial")
public class PerfilAsignadoYaExisteException  extends SistemaException{

	private String nombre;
	
	public PerfilAsignadoYaExisteException(String codigo) {
		this.nombre = codigo;
	}
	
	@Override
	public String getMessage() {		
		return String.format("Perfil %s ya está asignado", nombre);
	}

}
