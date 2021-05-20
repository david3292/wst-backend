package com.altioracorp.wst.servicio.cobros;

import java.time.LocalDateTime;
import java.util.List;

import com.altioracorp.wst.dominio.cobros.ChequePosfechadoAprobacionDTO;
import com.altioracorp.wst.dominio.cobros.ChequePosfechadoDTO;
import com.altioracorp.wst.dominio.cobros.CobroChequePosfechadoAprobacionDTO;
import com.altioracorp.wst.dominio.cobros.CobroChequePosfechadoDTO;
import com.altioracorp.wst.dominio.cobros.CobroFormaPago;
import com.altioracorp.wst.dominio.cobros.SolicitarReporteChequePosfechadoDTO;

public interface IChequePosfechadoServicio {

	void registrar(List<CobroFormaPago> chequesAFecha, String codigoCliente, String nombreCliente, LocalDateTime fechaCobro);
	
	List<ChequePosfechadoDTO> obtenerChequesPorEstadoRecibido();
	
	List<ChequePosfechadoDTO> obtenerChequesPorEstadoRevision();
	
	List<CobroChequePosfechadoDTO> procesar(List<ChequePosfechadoDTO> chequesAProcesar);
	
	List<CobroChequePosfechadoAprobacionDTO> procesarAprobacion(ChequePosfechadoAprobacionDTO aprobacionDTO);
	
	byte[] generarReporte(SolicitarReporteChequePosfechadoDTO solicitudReporte);	
	
}
