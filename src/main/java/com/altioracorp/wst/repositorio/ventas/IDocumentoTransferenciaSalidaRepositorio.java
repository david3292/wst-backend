package com.altioracorp.wst.repositorio.ventas;

import java.util.List;

import com.altioracorp.wst.dominio.ventas.DocumentoEstado;
import com.altioracorp.wst.dominio.ventas.DocumentoTransferenciaSalida;

public interface IDocumentoTransferenciaSalidaRepositorio extends IDocumentoRepositorio<DocumentoTransferenciaSalida>{

	List<DocumentoTransferenciaSalida> findByDocumentoTransferenciaId(long documentoTransferenciaId);
	
	List<DocumentoTransferenciaSalida> findByBodegaDestinoAndEstado(String bodegaDestino, DocumentoEstado estado);
	
	long countByDocumentoTransferenciaIdAndEstado(long documentoTransferenciaId, DocumentoEstado estado);
	
	List<DocumentoTransferenciaSalida> findByDocumentoReservaId(long documentoReservaId);
	
	List<DocumentoTransferenciaSalida> findByBodegaDestinoAndEstadoAndReposicionTrue(String bodegaDestino, DocumentoEstado estado);
}
