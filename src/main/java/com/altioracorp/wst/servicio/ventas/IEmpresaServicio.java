package com.altioracorp.wst.servicio.ventas;

import com.altioracorp.gpintegrator.client.Company.CompanyInformation;
import com.altioracorp.gpintegrator.client.Company.InfoGuiaCompania;
import com.altioracorp.gpintegrator.electronica.InfoTributaria;

public interface IEmpresaServicio {

	CompanyInformation obtenerInformacionEmpresa();
	
	InfoTributaria obtenerInformacionTributaria();
	
	InfoGuiaCompania obtenerInformacionGuiaCompania();
}
