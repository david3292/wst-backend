package com.altioracorp.wst.servicio.ventas;

import java.math.BigDecimal;

import com.altioracorp.wst.dominio.sistema.PuntoVenta;
import com.altioracorp.wst.dominio.ventas.ReservaArticulo;

public interface IReservaArticuloServicio {

	BigDecimal obtenerCantidadReservadaPorArticuloYPuntoVenta(String codigoArticulo, PuntoVenta puntoVenta);

	ReservaArticulo buscarPorArticuloYBodega(String codigoArticulo, String codigoBodega);

	void incremetarReserva(ReservaArticulo reservaArticulo);

	void decrementarReserva(ReservaArticulo reservaArticulo);
}
