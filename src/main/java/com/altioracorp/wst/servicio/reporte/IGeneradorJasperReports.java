package com.altioracorp.wst.servicio.reporte;

import java.util.Collection;
import java.util.Map;

import com.altioracorp.wst.exception.reporte.JasperReportsException;

public interface IGeneradorJasperReports {

	 byte[] generarReporte(final String reporteNombre, final Collection<?> objetos,
			Map<String, Object> parametrosEspecificos) throws JasperReportsException;
}
