package com.altioracorp.wst.repositorio.ventas;

import java.util.List;

import com.altioracorp.wst.dominio.ventas.MotivoNotaCredito;
import com.altioracorp.wst.repositorio.RepositorioBase;

public interface IMotivoNotaCreditoRepositorio extends RepositorioBase<MotivoNotaCredito> {
	
	List<MotivoNotaCredito> findByActivoTrue();
}
