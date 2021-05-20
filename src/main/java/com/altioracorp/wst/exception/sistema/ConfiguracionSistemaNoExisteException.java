package com.altioracorp.wst.exception.sistema;

@SuppressWarnings("serial")
public class ConfiguracionSistemaNoExisteException  extends SistemaException{

	private String nombreConfiguracion;
	
	public ConfiguracionSistemaNoExisteException(String nombre) {
		this.nombreConfiguracion = nombre;
	}
	
	@Override
	public String getMessage() {		
		return String.format("La configuración %s existe", nombreConfiguracion);
	}

}
