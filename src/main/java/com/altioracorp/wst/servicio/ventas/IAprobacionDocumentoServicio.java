package com.altioracorp.wst.servicio.ventas;

import java.util.List;

import com.altioracorp.wst.dominio.sistema.PerfilNombre;
import com.altioracorp.wst.dominio.ventas.AprobacionDocumento;
import com.altioracorp.wst.dominio.ventas.dto.CatalogoAprobacionEstadoDTO;

public interface IAprobacionDocumentoServicio {
	
	List<AprobacionDocumento> listarSolicitudesPendientesPorPerfil(PerfilNombre perfil);
	
	List<CatalogoAprobacionEstadoDTO> catalogoAprobacionEstado();
	
}
