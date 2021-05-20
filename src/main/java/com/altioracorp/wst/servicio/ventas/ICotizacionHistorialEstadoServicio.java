package com.altioracorp.wst.servicio.ventas;

import com.altioracorp.wst.dominio.ventas.Cotizacion;

public interface ICotizacionHistorialEstadoServicio {

	void registrar(Cotizacion documento, String observacion);
}
