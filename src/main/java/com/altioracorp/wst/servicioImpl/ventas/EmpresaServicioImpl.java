package com.altioracorp.wst.servicioImpl.ventas;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.altioracorp.gpintegrator.client.Company.CompanyInformation;
import com.altioracorp.gpintegrator.client.Company.CompanyInformationRequest;
import com.altioracorp.gpintegrator.client.Company.InfoGuiaCompania;
import com.altioracorp.gpintegrator.electronica.InfoTributaria;
import com.altioracorp.wst.constantes.integracion.UrlIntegracionCompany;
import com.altioracorp.wst.servicio.sistema.IConstantesAmbienteWstServicio;
import com.altioracorp.wst.servicio.ventas.IEmpresaServicio;

@Service
public class EmpresaServicioImpl implements IEmpresaServicio {

	@Autowired
	private RestTemplate restTemplate;
	
	@Autowired
	private IConstantesAmbienteWstServicio constantesAmbiente;
	

	@Override
	public CompanyInformation obtenerInformacionEmpresa() {
		
		CompanyInformationRequest requestInformaction = new CompanyInformationRequest(constantesAmbiente.getNombreBaseDatosGP());
		
		String url = constantesAmbiente.getUrlIntegradorGp().concat(UrlIntegracionCompany.OBTENER_EMPRESA_INFORMACION.getUrl());
		
		HttpEntity<CompanyInformationRequest> request = new HttpEntity<>(requestInformaction);
		ResponseEntity<CompanyInformation> response = restTemplate
		  .exchange(url, HttpMethod.POST, request, CompanyInformation.class);


		return response.getBody();
	}
	
	@Override
	public InfoTributaria obtenerInformacionTributaria() {
		
		final String url = constantesAmbiente.getUrlIntegradorGp().concat(UrlIntegracionCompany.OBTENER_INFORACION_TRIBUTARIA.getUrl());
		
		InfoTributaria infoTributaria = restTemplate.getForObject(url, InfoTributaria.class);
		
		return infoTributaria;
		
	}

	@Override
	public InfoGuiaCompania obtenerInformacionGuiaCompania() {
		
		final String url = constantesAmbiente.getUrlIntegradorGp().concat(UrlIntegracionCompany.OBTENER_COMPANY_LOCATION.getUrl());

		InfoGuiaCompania infoGuiaCompania = restTemplate.getForObject(url, InfoGuiaCompania.class);

		return infoGuiaCompania;
	}
}
