package com.altioracorp.wst.servicioImpl.ventas;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpStatusCodeException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import com.altioracorp.gpintegrator.client.FE.AltOffXmlDocumento;
import com.altioracorp.wst.constantes.integracion.UrlIntegracionFE;
import com.altioracorp.wst.exception.ventas.DocumentoGPException;
import com.altioracorp.wst.servicio.sistema.IConstantesAmbienteWstServicio;
import com.altioracorp.wst.servicio.ventas.IDocumentoFEServicio;

@Service
public class DocumentoFEServicioImpl implements IDocumentoFEServicio {
	
	private static final Log LOG = LogFactory.getLog(DocumentoFEServicioImpl.class);
	
	@Autowired
	private IConstantesAmbienteWstServicio constantesAmbiente;

	@Autowired
	private RestTemplate restTemplate;
	
	@Override
	public AltOffXmlDocumento consultarDocumentoFE(String numeroDocumento) {
		
		final String url = prepararUrl(UrlIntegracionFE.CONSULTAR_DOCUMENTO_FE.getUrl());

		UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(url).queryParam("documentNumber",
				numeroDocumento);

		try {

			AltOffXmlDocumento response = restTemplate.getForObject(builder.toUriString(), AltOffXmlDocumento.class);

			return response;

		} catch (HttpStatusCodeException e) {

			throw new DocumentoGPException(e.getMessage());
		}

	}
	
	private String prepararUrl(final String urlIntegracion) {

		final String url = UriComponentsBuilder
				.fromHttpUrl(constantesAmbiente.getUrlIntegradorGp().concat(urlIntegracion)).build().toUriString();

		LOG.info("URL Información: " + url);

		return url;
	}

}
