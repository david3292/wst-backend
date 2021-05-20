package com.altioracorp.wst.repositorio.cobros;

import java.util.List;

import com.altioracorp.wst.dominio.cobros.ChequePosfechadoControles;
import com.altioracorp.wst.repositorio.RepositorioBase;

public interface IChequePosfechadoControlesRepositorio extends RepositorioBase<ChequePosfechadoControles> {

	List<ChequePosfechadoControles> findByEstadoIsNull();
	
	List<ChequePosfechadoControles> findByCheque_IdOrderByCreadoFechaDesc(long chequeId);
}
