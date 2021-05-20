package com.altioracorp.wst.servicioImpl.ventas;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import com.altioracorp.gpintegrator.client.Vendor.Vendor;
import com.altioracorp.wst.constantes.integracion.UrlIntegracionVendor;
import com.altioracorp.wst.servicio.sistema.IConstantesAmbienteWstServicio;
import com.altioracorp.wst.servicio.ventas.IProveedorServicio;

@Service
public class ProveedorServicioImpl implements IProveedorServicio {

	private static final Log LOG = LogFactory.getLog(ClienteServicioImpl.class);

	@Autowired
	private IConstantesAmbienteWstServicio constantesAmbiente;

	@Autowired
	private RestTemplate restTemplate;

	@Override
	public List<Vendor> ObtenerProveedoresPorCriterio(Map<String, String> criterios) {
		List<Vendor> proveedores = new ArrayList<Vendor>();
		String criterio = criterios.get("criterio");
		if (StringUtils.isNotBlank(criterio)) {
			String url = constantesAmbiente.getUrlIntegradorGp()
					.concat(UrlIntegracionVendor.OBTENER_POR_CRITERIO.getUrl());

			UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(url).queryParam("criteria", criterio);
			LOG.info("Peticion: " + builder.toUriString());

			Vendor[] response = restTemplate.getForObject(builder.toUriString(), Vendor[].class);

			if (response != null)
				proveedores.addAll(Arrays.asList(response));
		}
		return proveedores;
	}
	
	@Override
	public Vendor ObtenerProveedorPorId(String proveedorId) {
		Vendor proveedor = new Vendor();		
		if (StringUtils.isNotBlank(proveedorId)) {
			String url = constantesAmbiente.getUrlIntegradorGp()
					.concat(UrlIntegracionVendor.OBTENER_POR_ID.getUrl());

			UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(url).queryParam("vendorId", proveedorId);
			LOG.info("Peticion: " + builder.toUriString());

			Vendor response = restTemplate.getForObject(builder.toUriString(), Vendor.class);

			if (response != null)
				proveedor = response;
		}
		return proveedor;
	}

	@Override
	public List<String> obtenerCondicionesPago(){
		List<String> condicionesPago = new ArrayList<String>();
		String url = constantesAmbiente.getUrlIntegradorGp().concat(UrlIntegracionVendor.CONDICIONES_PAGO.getUrl());
		
		String[] condicionesPagoResponse = restTemplate.getForObject(url, String[].class);
		condicionesPago.addAll(Arrays.asList(condicionesPagoResponse));
		
		return condicionesPago;
	}
	
}
