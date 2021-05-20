package com.altioracorp.wst.exception.sistema;

@SuppressWarnings("serial")
public class UsuarioYaExistenteException  extends SistemaException{

	private String nombre;
	
	public UsuarioYaExistenteException(String codigo) {
		this.nombre = codigo;
	}
	
	@Override
	public String getMessage() {		
		return String.format("Usuario %s ya existe", nombre);
	}

}
