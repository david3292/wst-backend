package com.altioracorp.wst.servicioImpl.ventas;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpStatusCodeException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import com.altioracorp.gpintegrator.client.GuiaRemision.AltGuiaTransaction;
import com.altioracorp.gpintegrator.client.ReceivablePostedTransaction.Rm20101Header;
import com.altioracorp.gpintegrator.client.Sales.DocumentNumber;
import com.altioracorp.gpintegrator.client.Sales.DocumentResponse;
import com.altioracorp.gpintegrator.client.Sales.SopRequest;
import com.altioracorp.gpintegrator.client.Sales.SopResponse;
import com.altioracorp.gpintegrator.client.inventory.IvResponse;
import com.altioracorp.wst.constantes.integracion.UrlIntegracionGuiaRemision;
import com.altioracorp.wst.constantes.integracion.UrlIntegracionSop;
import com.altioracorp.wst.dominio.sistema.PuntoVenta;
import com.altioracorp.wst.dominio.sistema.Secuencial;
import com.altioracorp.wst.dominio.sistema.TipoDocumento;
import com.altioracorp.wst.dominio.ventas.DocumentoBase;
import com.altioracorp.wst.dominio.ventas.DocumentoGuiaRemision;
import com.altioracorp.wst.exception.sistema.SecuancialNoExisteException;
import com.altioracorp.wst.exception.ventas.DocumentoGPException;
import com.altioracorp.wst.servicio.sistema.IConstantesAmbienteWstServicio;
import com.altioracorp.wst.servicio.sistema.ISecuencialServicio;
import com.altioracorp.wst.servicio.ventas.IDocumentoServicio;
import com.altioracorp.wst.util.UtilidadesCadena;

@Service
public class DocumentoServicioImpl implements IDocumentoServicio{

	private static final Log LOG = LogFactory.getLog(DocumentoServicioImpl.class);

	@Autowired
	private ISecuencialServicio secuencialServicio;

	@Autowired
	private IConstantesAmbienteWstServicio constantesAmbiente;

	@Autowired
	private RestTemplate restTemplate;
	
	@Autowired
	private IConstantesAmbienteWstServicio constantesAmbienteServicio;
	
	@Override
	public void insertarLocalizacionSop(DocumentNumber documentNumber) {
		
		try {

			final String url = prepararUrl(UrlIntegracionSop.INSERTAR_LOCALIZACION_SOP.getUrl());

			restTemplate.postForLocation(url, documentNumber);

		} catch (HttpStatusCodeException e) {

			LOG.error(e.getMessage());
			throw new DocumentoGPException(e.getMessage());
		}
		
	}
	
	@Override
	public SopResponse integrarDocumento(SopRequest request) {

		try {
			return restTemplate.postForObject(prepararUrl(UrlIntegracionSop.INTEGRAR_TRNASACCION_SOP.getUrl()), request,
					SopResponse.class);
		} catch (HttpStatusCodeException e) {
			LOG.error(e.getMessage());
			throw new DocumentoGPException(e.getMessage());
		}
	}
	
	private String numeroDocumentoGP(Secuencial secuencial) {

		final String url = prepararUrl(UrlIntegracionSop.OBTENER_NUMERO_DOCUMENTO.getUrl());

		DocumentNumber documentNumber = new DocumentNumber(secuencial.getTipoDocumento().getCodigo(),
				secuencial.getDocIdGP());

		try {

			ResponseEntity<String> response = restTemplate.postForEntity(url, documentNumber, String.class);

			return response.getBody().toString();

		} catch (HttpStatusCodeException e) {

			LOG.error(e.getMessage());
			throw new DocumentoGPException(e.getMessage());
		}
	}

	private String prepararUrl(final String urlIntegracion) {

		final String url = UriComponentsBuilder
				.fromHttpUrl(constantesAmbiente.getUrlIntegradorGp().concat(urlIntegracion)).build().toUriString();

		LOG.info("URL Información: " + url);

		return url;
	}
	
	@Override
	public Secuencial secuencialDocumento(PuntoVenta puntoVenta, TipoDocumento tipoDocumento) {

		Secuencial secuencial = secuencialServicio.ObtenerSecuencialPorPuntoVentaYTipoDocumento(puntoVenta.getId(),
				tipoDocumento);

		if (secuencial == null) {
			LOG.error(String.format("No existe secuencial para el punto de venta %s y tipo documento %s",
					puntoVenta.getNombre(), tipoDocumento.getDescripcion()));
			throw new SecuancialNoExisteException(puntoVenta.getNombre(), tipoDocumento.getDescripcion());
		}

		if (tipoDocumento.esDocumentoGP()) {
			secuencial.setNumeroSecuencial(numeroDocumentoGP(secuencial));
		} 
		
		return secuencial;
	}

	@Override
	public DocumentResponse consultarDocumento(String numeroDocumento, Integer soptype) {
	
		final String url = prepararUrl(UrlIntegracionSop.CONSULTAR_DOCUMENTO.getUrl());

		UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(url).queryParam("documentNumber",
				numeroDocumento). queryParam("soptype", soptype);

		try {

			DocumentResponse response = restTemplate.getForObject(builder.toUriString(), DocumentResponse.class);

			return response;

		} catch (HttpStatusCodeException e) {

			throw new DocumentoGPException(e.getMessage());
		}
	}

	@Override
	public List<Rm20101Header> consultarNotasDeCreditoPorCliente(String codigoCliente) {
		
		final String url = prepararUrl(UrlIntegracionSop.CONSULTAR_DOCUMENTO_NC.getUrl());

		UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(url).queryParam("custnmbr",
				codigoCliente);

		try {

			Rm20101Header[] response = restTemplate.getForObject(builder.toUriString(), Rm20101Header[].class);

			return response == null? new ArrayList<Rm20101Header>(): Arrays.asList(response);

		} catch (HttpStatusCodeException e) {

			throw new DocumentoGPException(e.getMessage());
		}
	}
	
	@Override
	public List<Rm20101Header> consultarCobrosPorCliente(String codigoCliente) {
		
		final String url = prepararUrl(UrlIntegracionSop.CONSULTAR_DOCUMENTO_COBRO.getUrl());

		UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(url).queryParam("custnmbr",
				codigoCliente);

		try {

			Rm20101Header[] response = restTemplate.getForObject(builder.toUriString(), Rm20101Header[].class);
			
			List<Rm20101Header> cobros = response == null? new ArrayList<Rm20101Header>(): Arrays.asList(response);
			
			//cobros.stream().filter(x -> !x.getBachnumb().startsWith("WST")).collect(Collectors.toList());

			return cobros;

		} catch (HttpStatusCodeException e) {

			throw new DocumentoGPException(e.getMessage());
		}
	}
	
	@Override
	public String obtenerNumeroDocumentoGuiaRemision(DocumentoGuiaRemision guiaRemision) {
		String numeroDocumento = StringUtils.EMPTY;
		final String url = constantesAmbienteServicio.getUrlIntegradorGp().concat(UrlIntegracionGuiaRemision.OBTENER_NUMERO_GP.getUrl().replace("${locnCode}", guiaRemision.getBodegaPartida()));
		try {
			numeroDocumento = restTemplate.getForObject(url, String.class);	
			numeroDocumento = numeroDocumento.replace("\"", StringUtils.EMPTY);
		} catch (Exception e) {
			LOG.error(String.format("Error al obtener el numero de documento: error: %s", e.getMessage()));
		}
		return numeroDocumento;
	}

	@Override
	public String obtenerBachNumber(DocumentoBase documento) {
		String[] partesNumero = documento.getNumero().split("-");
        List<String> cademas = Arrays.asList("WST", partesNumero[0], Long.toString(documento.getId()));
        String bachnumb = UtilidadesCadena.juntar(cademas,"-");
        return bachnumb;
	}
	
	@Override
	public IvResponse llamarApiIntegracionGuiaRemision(AltGuiaTransaction guiaTransaction) {
		try {
			final String url = constantesAmbienteServicio.getUrlIntegradorGp().concat(UrlIntegracionGuiaRemision.INTEGRAR_GUIA_REMISION.getUrl()); 
			return restTemplate.postForObject(url, guiaTransaction, IvResponse.class);
		}catch(HttpStatusCodeException e) {
			LOG.error(e.getMessage());
			IvResponse response = new IvResponse(); 
			response.setErrorCode("-1");
			response.setErrorMessage(e.getMessage());
			return response;
		}
	}

	@Override
	public boolean documentoEstaContabilizado(String numeroDocumento) {
		final String url = prepararUrl(UrlIntegracionSop.CONSULTAR_DOCUMENTO_CONTABILIZADO.getUrl());

		UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(url).queryParam("documentNumber",
				numeroDocumento);

		try {
			DocumentResponse response = restTemplate.getForObject(builder.toUriString(), DocumentResponse.class);
			LOG.info(String.format("Consultando DocumentoGP %s está contabilizado: %s", numeroDocumento,
					response.getDocumentHeader() == null ? Boolean.FALSE : Boolean.TRUE));
			
			return response.getDocumentHeader() == null ? Boolean.FALSE : Boolean.TRUE;

		} catch (HttpStatusCodeException e) {
			LOG.error(e.getMessage());
			throw new DocumentoGPException(e.getMessage());
		}
	}
}
