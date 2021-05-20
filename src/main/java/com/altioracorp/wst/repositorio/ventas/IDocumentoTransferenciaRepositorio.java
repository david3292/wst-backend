package com.altioracorp.wst.repositorio.ventas;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

import com.altioracorp.wst.dominio.ventas.DocumentoEstado;
import com.altioracorp.wst.dominio.ventas.DocumentoTransferencia;
import com.altioracorp.wst.dominio.ventas.TipoTransferencia;

public interface IDocumentoTransferenciaRepositorio extends IDocumentoRepositorio<DocumentoTransferencia>  {

	List<DocumentoTransferencia> findByBodegaOrigenAndEstadoIn(String bodegaOrigen, Collection<DocumentoEstado> estados);
	
	List<DocumentoTransferencia> findByCotizacion_IdOrderByModificadoPorAsc(Long cotizacionID);
	
	List<DocumentoTransferencia> findByBodegaDestinoAndTipoTransferenciaAndEstadoIn(String bodegaDestino, TipoTransferencia tipoTransferencia, Collection<DocumentoEstado> estados);
	
	Optional<DocumentoTransferencia> findByBodegaOrigenAndBodegaDestinoAndTipoTransferenciaAndEstado(String bodegaOrigen, String bodegaDestino, TipoTransferencia tipoTransferencia, DocumentoEstado estado);
	
	List<DocumentoTransferencia> findByTipoTransferenciaAndEstado(TipoTransferencia tipoTransferencia, DocumentoEstado estado);
	
}
