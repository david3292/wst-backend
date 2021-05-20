package com.altioracorp.wst.servicio.sistema;

import java.util.List;

import com.altioracorp.gpintegrator.client.CheckBook.CheckBook;
import com.altioracorp.wst.controlador.sistema.FormaPagoPvtaDTO;
import com.altioracorp.wst.dominio.sistema.FormaPagoPuntoVenta;
import com.altioracorp.wst.dominio.sistema.PuntoVenta;
import com.altioracorp.wst.servicio.ICRUD;

public interface IFPagoPvtaServicio extends ICRUD<FormaPagoPuntoVenta, Long> {

	List<FormaPagoPuntoVenta> listarPorPuntoVenta(PuntoVenta puntoVenta);
	
	List<CheckBook> listarChequerasGP();
	
	List<FormaPagoPvtaDTO> listarActivosPorPuntoVenta(PuntoVenta puntoVenta);
	
}
