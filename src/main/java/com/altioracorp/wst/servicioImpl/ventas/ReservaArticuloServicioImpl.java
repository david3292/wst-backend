package com.altioracorp.wst.servicioImpl.ventas;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.altioracorp.wst.dominio.sistema.Bodega;
import com.altioracorp.wst.dominio.sistema.PuntoVenta;
import com.altioracorp.wst.dominio.ventas.ReservaArticulo;
import com.altioracorp.wst.repositorio.ventas.IReservaArticuloRepositorio;
import com.altioracorp.wst.servicio.sistema.IPuntoVentaBodegaServicio;
import com.altioracorp.wst.servicio.ventas.IReservaArticuloServicio;

@Service
public class ReservaArticuloServicioImpl implements IReservaArticuloServicio {

	@Autowired
	private IReservaArticuloRepositorio reservaArticuloRepositorio;

	@Autowired
	private IPuntoVentaBodegaServicio puntoVentaBodegaServicio;

	@Override
	public BigDecimal obtenerCantidadReservadaPorArticuloYPuntoVenta(String codigoArticulo, PuntoVenta puntoVenta) {

		List<Bodega> bodegas = puntoVentaBodegaServicio.buscarBodegasPorPuntoVenta(puntoVenta);

		BigDecimal cantidadReservada = BigDecimal.ZERO;

		for (Bodega bodega : bodegas) {
			Optional<ReservaArticulo> reserva = reservaArticuloRepositorio
					.findByCodigoArticuloAndCodigoBodega(codigoArticulo, bodega.getCodigo());
			if(reserva.isPresent()) {
				cantidadReservada.add(reserva.get().getCantidad());	
			}
			
		}

		return cantidadReservada;
	}

	@Override
	public ReservaArticulo buscarPorArticuloYBodega(String codigoArticulo, String codigoBodega) {

		Optional<ReservaArticulo> reservaOP = reservaArticuloRepositorio
				.findByCodigoArticuloAndCodigoBodega(codigoArticulo, codigoBodega);

		return reservaOP.isPresent() ? reservaOP.get() : null;	
	}

	@Override
	public void incremetarReserva(ReservaArticulo reservaArticulo) {

		Optional<ReservaArticulo> reservaOP = reservaArticuloRepositorio
				.findByCodigoArticuloAndCodigoBodega(reservaArticulo.getCodigoArticulo(),
						reservaArticulo.getCodigoBodega());

		if (reservaOP.isPresent()) {
			ReservaArticulo reserva = reservaOP.get();
			reserva.setCantidad(reserva.getCantidad().add(reservaArticulo.getCantidad()));
			reservaArticuloRepositorio.save(reserva);
		} else {
			reservaArticuloRepositorio.save(reservaArticulo);
		}

	}

	@Override
	public void decrementarReserva(ReservaArticulo reservaArticulo) {

		Optional<ReservaArticulo> reservaOP = reservaArticuloRepositorio.findByCodigoArticuloAndCodigoBodega(
				reservaArticulo.getCodigoArticulo(), reservaArticulo.getCodigoBodega());

		if (reservaOP.isPresent()) {
			ReservaArticulo reserva = reservaOP.get();
			reserva.setCantidad(reserva.getCantidad().subtract(reservaArticulo.getCantidad()));
			reservaArticuloRepositorio.save(reserva);
		}

	}

}
