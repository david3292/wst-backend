package com.altioracorp.wst.servicioImpl.alt;

import java.util.Arrays;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.altioracorp.gpintegrator.client.miscellaneous.AltConductor;
import com.altioracorp.wst.constantes.integracion.UrlIntegradorConductor;
import com.altioracorp.wst.servicio.alt.IAltConductorServicio;
import com.altioracorp.wst.servicio.sistema.IConstantesAmbienteWstServicio;

@Service
public class AltConductorServicio implements IAltConductorServicio {

	@Autowired
	private RestTemplate restTemplate;
	
	@Autowired
	private IConstantesAmbienteWstServicio constantesAmbiente;

	@Override
	public List<AltConductor> listarConductores() {
		String url = constantesAmbiente.getUrlIntegradorGp().concat(UrlIntegradorConductor.LISTAR_CONDUSTORES.getUrl());
		AltConductor[] response = restTemplate.getForObject(url, AltConductor[].class);
		return Arrays.asList(response);
	}
	
}
