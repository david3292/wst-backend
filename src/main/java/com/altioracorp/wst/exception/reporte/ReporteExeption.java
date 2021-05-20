package com.altioracorp.wst.exception.reporte;

@SuppressWarnings("serial")
public class ReporteExeption extends RuntimeException {
	private String nombreReporte;

	public ReporteExeption(String nombreReporte) {
		this.nombreReporte = nombreReporte;
	}

	@Override
	public String getMessage() {
		return "Error en la generación del Reporte " + nombreReporte;
	}

}
