package com.altioracorp.wst.servicio.ventas;

import com.altioracorp.wst.dominio.ventas.ReservaArticuloCompartimiento;

public interface IReservaArticuloCompartimientoServicio {
	
	ReservaArticuloCompartimiento buscarPorArticuloYBodegaYCompartimiento(ReservaArticuloCompartimiento reserva);

	void incremetarReserva(ReservaArticuloCompartimiento reserva);

	void decrementarReserva(ReservaArticuloCompartimiento reserva);

}
