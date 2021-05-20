package com.altioracorp.wst;

public enum AmbienteElectronica {
	
	PRUEBAS(1),
	PRODUCCION(2)
	;
	
	private int ambienteSri;
	
	private AmbienteElectronica(int ambienteSri) {
		this.ambienteSri = ambienteSri;
	}

	public int getAmbienteSri() {
		return ambienteSri;
	}
	
}
