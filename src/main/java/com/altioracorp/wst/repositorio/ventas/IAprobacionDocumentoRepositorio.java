package com.altioracorp.wst.repositorio.ventas;

import java.util.List;
import java.util.Optional;

import com.altioracorp.wst.dominio.sistema.PerfilNombre;
import com.altioracorp.wst.dominio.ventas.AprobacionDocumento;
import com.altioracorp.wst.repositorio.RepositorioBase;

public interface IAprobacionDocumentoRepositorio extends RepositorioBase<AprobacionDocumento>  {

	List<AprobacionDocumento> findByUsuarioAndActivoTrue(String usuario);
	
	List<AprobacionDocumento> findByPerfilAndActivoTrueOrderByCreadoFechaDesc(PerfilNombre perfil);
	
	Optional<AprobacionDocumento> findByIdAndActivoTrue(Long id);
	
	List<AprobacionDocumento> findByCotizacion_id(Long cotizacionID);
	
}
