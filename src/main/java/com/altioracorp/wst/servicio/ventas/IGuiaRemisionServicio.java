package com.altioracorp.wst.servicio.ventas;

import com.altioracorp.gpintegrator.client.GuiaRemision.AltGuiaTransaction;
import com.altioracorp.wst.dominio.ventas.DocumentoBase;
import com.altioracorp.wst.dominio.ventas.DocumentoGuiaRemision;
import com.altioracorp.wst.dominio.ventas.dto.GuiaRemisionDTO;
import com.altioracorp.wst.xml.guiaRemision.GuiaRemision;

import java.util.List;
import java.util.Map;
import java.util.Optional;

public interface IGuiaRemisionServicio {

	List<DocumentoGuiaRemision> buscarPorDocumentoPadreId(long documentoPadreId);
	
	DocumentoGuiaRemision save(DocumentoGuiaRemision guiaRemision);
	
	List<GuiaRemisionDTO> listarTodasPorCotizacionID(Long cotizacionID);

	DocumentoBase obtenerGuiaRemisionPorId(long guiaRemisionId);

	Boolean existeGuiasRemisionPorFacturaId(long facturaId);
	
	long countByDocumentoPadreId(long id);
	
	Optional<DocumentoGuiaRemision> findById(long id);
	
	void generarXmlGuiaRemision(AltGuiaTransaction guiaTransaction, DocumentoBase documento);
	
	GuiaRemision obtenerGuiaRemision(AltGuiaTransaction guiaTransaction, DocumentoBase documento);
	
	Map<String, Object> obtenerInfoGuiaRemision(long guiaRemisionId);
	
}
