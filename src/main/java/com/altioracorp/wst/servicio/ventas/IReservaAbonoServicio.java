package com.altioracorp.wst.servicio.ventas;

import java.time.LocalDateTime;

import com.altioracorp.wst.dominio.ventas.DocumentoReserva;

public interface IReservaAbonoServicio {

	double calcularPorcentajeAbono();
	
	LocalDateTime calcularFechaMaximaReserva();
	
	LocalDateTime calcularTiempoMaximoAbono();
	
	DocumentoReserva guardarReserva(DocumentoReserva reserva);
	
	DocumentoReserva listarPorCotizacion(Long cotizacionID);
	
	void enviarAprobarReserva(DocumentoReserva reserva);
	
}
