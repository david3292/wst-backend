package com.altioracorp.wst.exception.sistema;

@SuppressWarnings("serial")
public class ConfiguracionYaExistenteException  extends SistemaException{

	private String nombreConfiguracion;
	
	public ConfiguracionYaExistenteException(String nombre) {
		this.nombreConfiguracion = nombre;
	}
	
	@Override
	public String getMessage() {		
		return String.format("La configuración ya existe: %s", nombreConfiguracion);
	}

}
