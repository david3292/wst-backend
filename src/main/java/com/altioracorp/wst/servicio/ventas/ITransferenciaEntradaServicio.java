package com.altioracorp.wst.servicio.ventas;

import com.altioracorp.wst.dominio.logistica.dto.TransferenciaResumenDTO;

public interface ITransferenciaEntradaServicio {
	
	void procesarTransferenciaEntrada(long transferenciaEntradaId);
	
	TransferenciaResumenDTO integrarTransferenciaEntradaGp(long transferenciaId);

}
