package com.altioracorp.wst.servicio.ventas;

import java.util.List;
import java.util.Map;

import com.altioracorp.gpintegrator.client.Vendor.Vendor;

public interface IProveedorServicio {

	List<Vendor> ObtenerProveedoresPorCriterio(Map<String, String> criterios);
	
	Vendor ObtenerProveedorPorId(String proveedorId);
	
	public List<String> obtenerCondicionesPago();
	
}
