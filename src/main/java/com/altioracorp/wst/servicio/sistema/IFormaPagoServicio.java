package com.altioracorp.wst.servicio.sistema;

import java.util.List;

import com.altioracorp.wst.controlador.sistema.CatalogoDTO;
import com.altioracorp.wst.dominio.sistema.FormaPago;
import com.altioracorp.wst.servicio.ICRUD;

public interface IFormaPagoServicio extends ICRUD<FormaPago, Long> {
	
	List<FormaPago> listarActivos();
	
	List<CatalogoDTO> listarCatalogoFormaPagoNombre();
}
