package com.altioracorp.wst.servicio.ventas;

import com.altioracorp.wst.dominio.logistica.dto.TransferenciaResumenDTO;

public interface ITransferenciaSalidaServicio {
	
	void anularTransferencia(long transferenciaSalidaId);
	
	TransferenciaResumenDTO integrarTransferenciaSalidaGp(long transferenciaId);
	
	TransferenciaResumenDTO integrarGuiaRemisionGp(long transferenciaSalidaId);
	
	byte[]generarReporteGuiaRemision(long guiaTransferenciaSalidaId);

}
