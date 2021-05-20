package com.altioracorp.wst.servicioImpl.ventas;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.altioracorp.wst.dominio.sistema.PerfilNombre;
import com.altioracorp.wst.dominio.ventas.AprobacionDocumento;
import com.altioracorp.wst.dominio.ventas.AprobacionEstado;
import com.altioracorp.wst.dominio.ventas.dto.CatalogoAprobacionEstadoDTO;
import com.altioracorp.wst.repositorio.ventas.IAprobacionDocumentoRepositorio;
import com.altioracorp.wst.servicio.ventas.IAprobacionDocumentoServicio;

@Service
public class AprobacionDocumentoServicioImpl implements IAprobacionDocumentoServicio {

	@Autowired
	private IAprobacionDocumentoRepositorio aprobacionRepositorio;


	@Override
	public List<AprobacionDocumento> listarSolicitudesPendientesPorPerfil(PerfilNombre perfil) {
		return aprobacionRepositorio.findByPerfilAndActivoTrueOrderByCreadoFechaDesc(perfil);
	}
	
	@Override
	public List<CatalogoAprobacionEstadoDTO> catalogoAprobacionEstado() {
		AprobacionEstado[] opciones = AprobacionEstado.values();
		List<CatalogoAprobacionEstadoDTO> catalogo = new ArrayList<>();
		for (AprobacionEstado ae : opciones) {
			if (!ae.equals(AprobacionEstado.REGRESAR))
				catalogo.add(new CatalogoAprobacionEstadoDTO(ae.toString(), ae.toString()));
		}
		return catalogo;
	}
}
