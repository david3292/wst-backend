package com.altioracorp.wst.repositorio.cobros;

import java.util.List;

import com.altioracorp.wst.dominio.cobros.ChequePosfechado;
import com.altioracorp.wst.dominio.cobros.ChequePosfechadoEstado;
import com.altioracorp.wst.repositorio.RepositorioBase;

public interface IChequePosfechadoRepositorio extends RepositorioBase<ChequePosfechado> {

	List<ChequePosfechado> findByEstadoIn(List<ChequePosfechadoEstado> estados);
}
