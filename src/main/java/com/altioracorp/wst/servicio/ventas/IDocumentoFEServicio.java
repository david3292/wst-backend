package com.altioracorp.wst.servicio.ventas;

import com.altioracorp.gpintegrator.client.FE.AltOffXmlDocumento;

public interface IDocumentoFEServicio {

	AltOffXmlDocumento consultarDocumentoFE(String numeroDocumento);
}
