package com.altioracorp.wst.exception.sistema;

@SuppressWarnings("serial")
public class ConfiguracionUsuarioPerfilPuntoVentaYaExisteException  extends SistemaException{

	private String perfil;
	
	private String puntoVenta;
	
	public ConfiguracionUsuarioPerfilPuntoVentaYaExisteException(String perfil, String puntoVenta) {
		this.perfil = perfil;
		this.puntoVenta = puntoVenta;
	}
	
	@Override
	public String getMessage() {		
		return String.format("La configuración para el perfil %s y el Punto Venta %s ya existe.", this.perfil, this.puntoVenta);
	}

}
