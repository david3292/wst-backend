package com.altioracorp.wst.exception.sistema;

@SuppressWarnings("serial")
public class CargoYaExistenteException  extends SistemaException{

	private String nombre;
	
	public CargoYaExistenteException(String codigo) {
		this.nombre = codigo;
	}
	
	@Override
	public String getMessage() {		
		return String.format("Cargo %s ya existe", nombre);
	}

}
