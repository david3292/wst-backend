package com.altioracorp.wst.servicio.sistema;

import java.util.List;
import java.util.Optional;

import com.altioracorp.wst.controlador.sistema.CatalogoDTO;
import com.altioracorp.wst.dominio.sistema.CondicionPago;
import com.altioracorp.wst.servicio.ICRUD;

public interface ICondicionPagoServicio extends ICRUD<CondicionPago, Long> {
			
	List<CondicionPago> buscarTodosActivos();
	
	List<CatalogoDTO> crarCatalogoTipoPago();
	
	Optional<CondicionPago> buscarPorTermino(String termino);
}
