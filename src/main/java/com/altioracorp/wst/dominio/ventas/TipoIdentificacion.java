package com.altioracorp.wst.dominio.ventas;

public enum TipoIdentificacion {
	
	CEDULA("Cédula", 1, true, true),
	DESCONOCIDO("Desconocido", 4, false, false),
	PASAPORTE("Pasaporte", 3, true, true), 
	RUC("RUC", 2, true, false);

	private String descripcion;
	
	private int orden;

	private boolean personaNatural;

	private boolean visibleExternamente;

	private TipoIdentificacion(String descripcion, int orden, boolean visibleExternamente, boolean personaNatural) {
		this.descripcion = descripcion;
		this.orden = orden;
		this.visibleExternamente = visibleExternamente;
		this.personaNatural = personaNatural;
	}

	public String getDescripcion() {
		return descripcion;
	}

	public int getOrden() {
		return orden;
	}

	public boolean isPersonaNatural() {
		return personaNatural;
	}
	
	public boolean isPersonaNaturalVisibleExternamente() {
		return personaNatural && visibleExternamente;
	}

	public boolean isVisibleExternamente() {
		return visibleExternamente;
	}
}
