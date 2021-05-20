package com.altioracorp.wst.servicio.reporte;

import java.io.File;
import java.io.IOException;
import java.util.Collection;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;

import com.altioracorp.wst.exception.reporte.JasperReportsException;

import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;

@Service
public class GeneradorJasperReportsImpl implements IGeneradorJasperReports {

	private static final Log LOG = LogFactory.getLog(GeneradorJasperReportsImpl.class);

	@Override
	public byte[] generarReporte(String reporteNombre, Collection<?> objetos, Map<String, Object> parametrosEspecificos)
			throws JasperReportsException {

		LOG.info("INICIA creacion de documento JASPER");

		try {
			final String ruta = prepararReporteRuta(reporteNombre);

			JasperPrint print = JasperFillManager.fillReport(ruta, null,
					new JRBeanCollectionDataSource(objetos));

			LOG.info("FINALIZA creacion de documento JASPER");
			return JasperExportManager.exportReportToPdf(print);

		} catch (Exception e) {
			LOG.error("Se produjo un error al generar el reporte: {}", e);
			LOG.info("FINALIZA creacion de documento JASPER");
			throw new JasperReportsException();
		}

	}

	private String prepararReporteRuta(final String reporteNombre) throws IOException {

		final String ruta = "/reporte/" + reporteNombre + ".jasper";
		final File file = new ClassPathResource(ruta).getFile();
		LOG.info("Ruta del reporte: " + ruta);
		return file.getPath();
	}

}
