package com.altioracorp.wst.servicioImpl.sistema;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.altioracorp.wst.servicio.sistema.ConfiguracionIntegradorGP;
import com.altioracorp.wst.servicio.sistema.IConstantesAmbienteWstServicio;

@Service
public class ConstantesAmbienteWstServicioImpl implements IConstantesAmbienteWstServicio{

	@Autowired
	ConfiguracionIntegradorGP configuracion;
	
	@Override
	public String getUrlIntegradorGp() {		
		return configuracion.getUrl();
	}

	@Override
	public String getNombreBaseDatosGP() {
		return configuracion.getNombreBaseDatosGP();
	}

	@Override
	public String getBodegaTemporalLocnCode() {
		return configuracion.getBodegaTemporalLocncode();
	}

	@Override
	public String getBodegaTemporalBin() {
		return configuracion.getBodegaTemporalBin();
	}

	@Override
	public String getCompartimientoGeneral() {
		return configuracion.getCompartimientoGeneral();
	}
	
	@Override
	public String getAmbienteSri() {
		return configuracion.getAmbienteSri();
	}

	@Override
	public String getBodegaReposicionLocnCode() {
		return configuracion.getBodegaReposicionLocncode();
	}
	
	@Override
	public String getBinRecepcionOrdenCompra() {
		return configuracion.getBinRecepcionOrdenCompran();
	}
	
}
