package com.altioracorp.wst.repositorio.ventas;

import java.util.List;

import com.altioracorp.wst.dominio.ventas.DocumentoTransferenciaEntrada;

public interface IDocumentoTransferenciaEntradaRepositorio extends IDocumentoRepositorio<DocumentoTransferenciaEntrada>{

	List<DocumentoTransferenciaEntrada> findByDocumentoTransferenciaSalidaId(long documentoTransferenciaSalidaId);
	
	List<DocumentoTransferenciaEntrada> findByDocumentoReservaId(long documentoReservaId);
	
}
