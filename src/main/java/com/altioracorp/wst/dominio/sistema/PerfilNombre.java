package com.altioracorp.wst.dominio.sistema;

public enum PerfilNombre {

	SUPERADMIN("SUPERADMIN"),
	ADMINISTRADOR("ADMINISTRADOR"),
	VENDEDOR("VENDEDOR"),
	APROBADOR_COMERCIAL("APROBADOR COMERCIAL"),
	APROBADOR_CREDITO_Y_COBRANZAS("APROBADOR CREDITO Y COBRANZAS"),
	CAJERO("CAJERO"),
	ANALISTA_TESORERIA("ANALISTA TESORERÍA"),
	JEFE_TESORERIA("JEFE TESORERÍA"),
	JEFE_BODEGA("JEFE BODEGA"),
	JEFE_COBRANZAS("JEFE COBRANZAS"),
	BODEGUERO("Bodeguero"),
	JEFE_VENTAS("JEFE VENTAS");

	private String descripcion;

	private PerfilNombre(String descripcion) {
		this.descripcion = descripcion;
	}

	public String getDescripcion() {
		return descripcion;
	}
}
