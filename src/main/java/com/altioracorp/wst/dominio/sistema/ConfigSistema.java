package com.altioracorp.wst.dominio.sistema;

public enum ConfigSistema {

	MAXIMO_PORCENTAJE_VARIACION_PRECIO("Máximo % variación precio"),
	PORCENTAJE_PREDETERMINADO_ANTICIPO("Porcentaje predeterminado Anticipo"),
	HORA_MAXIMA_ENTREGA_ANTICIPO("Hora máxima entrega anticipo"),
	VIGENCIA_COTIZACION("Vigencia cotización"),
	VIGENCIA_COTIZACION_NUEVA("Vigencia cotización nueva"),
	MAXIMO_DIAS_RECOTIZACION("Días máximo de recotización"),
	MAXIMO_DIAS_RESERVA_STOCK("Máximo días reserva stock"),
	PORCENTAJE_MARGEN_UTILIDAD_ARTICULO_REVENTA("% Margen utilidad artículo reventa"),
	MAXIMO_PORCENTAJE_DESCUENTO_FIJO("Máximo % Descuento Fijo"),
	MAXIMO_PORCENTAJE_RETENCION("Máximo Porcentaje Retención");
	
	private String descripcion;
	
	private ConfigSistema(String descripcion) {
		this.descripcion = descripcion;
	}

	public String getDescripcion() {
		return descripcion;
	}
	
	
}
