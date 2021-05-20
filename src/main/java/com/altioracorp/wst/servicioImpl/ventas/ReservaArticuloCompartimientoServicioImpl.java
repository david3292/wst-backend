package com.altioracorp.wst.servicioImpl.ventas;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.altioracorp.wst.dominio.ventas.ReservaArticuloCompartimiento;
import com.altioracorp.wst.repositorio.ventas.IReservaArticuloCompartimientoRepositorio;
import com.altioracorp.wst.servicio.ventas.IReservaArticuloCompartimientoServicio;

@Service
public class ReservaArticuloCompartimientoServicioImpl implements IReservaArticuloCompartimientoServicio {

	@Autowired
	private IReservaArticuloCompartimientoRepositorio reservaArticuloCompartimientoRepositorio;

	@Override
	public ReservaArticuloCompartimiento buscarPorArticuloYBodegaYCompartimiento(
			ReservaArticuloCompartimiento reserva) {

		Optional<ReservaArticuloCompartimiento> reservaOP = reservaArticuloCompartimientoRepositorio
				.findByCodigoArticuloAndCodigoBodegaAndCompartimiento(reserva.getCodigoArticulo(),
						reserva.getCodigoBodega(), reserva.getCompartimiento());

		return reservaOP.isPresent() ? reservaOP.get() : null;
	}

	@Override
	public void incremetarReserva(ReservaArticuloCompartimiento reserva) {

		Optional<ReservaArticuloCompartimiento> reservaOP = reservaArticuloCompartimientoRepositorio
				.findByCodigoArticuloAndCodigoBodegaAndCompartimiento(reserva.getCodigoArticulo(),
						reserva.getCodigoBodega(), reserva.getCompartimiento());

		if (reservaOP.isPresent()) {
			ReservaArticuloCompartimiento reservaCompartimiento = reservaOP.get();
			reservaCompartimiento.setCantidad(reservaCompartimiento.getCantidad().add(reserva.getCantidad()));
			reservaArticuloCompartimientoRepositorio.save(reservaCompartimiento);
		} else {
			reservaArticuloCompartimientoRepositorio.save(reserva);
		}

	}

	@Override
	public void decrementarReserva(ReservaArticuloCompartimiento reserva) {

		Optional<ReservaArticuloCompartimiento> reservaOP = reservaArticuloCompartimientoRepositorio
				.findByCodigoArticuloAndCodigoBodegaAndCompartimiento(reserva.getCodigoArticulo(),
						reserva.getCodigoBodega(), reserva.getCompartimiento());

		if (reservaOP.isPresent()) {
			ReservaArticuloCompartimiento reservaCompartimiento = reservaOP.get();
			reservaCompartimiento.setCantidad(reservaCompartimiento.getCantidad().subtract(reserva.getCantidad()));
			reservaArticuloCompartimientoRepositorio.save(reservaCompartimiento);
		}

	}

}
