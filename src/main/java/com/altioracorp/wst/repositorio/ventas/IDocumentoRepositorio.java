package com.altioracorp.wst.repositorio.ventas;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

import com.altioracorp.wst.dominio.ventas.DocumentoBase;
import com.altioracorp.wst.dominio.ventas.DocumentoEstado;
import com.altioracorp.wst.repositorio.RepositorioBase;

public interface IDocumentoRepositorio<T extends DocumentoBase> extends RepositorioBase<T> {
	
	Optional<T> findByCotizacion_Id(Long cotizacionID);
	
	Optional<T> findByEstadoAndCotizacion_Id(DocumentoEstado estado, Long cotizacionID);

	List<T> findByEstadoInAndCotizacion_Id(Collection<DocumentoEstado> estados, Long cotizacionID);
	
	Optional<T> findByNumero(String numero);
}
