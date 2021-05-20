package com.altioracorp.wst.servicioImpl.ventas;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import javax.transaction.Transactional;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.altioracorp.wst.dominio.sistema.ConfigSistema;
import com.altioracorp.wst.dominio.sistema.ConfiguracionSistema;
import com.altioracorp.wst.dominio.sistema.Secuencial;
import com.altioracorp.wst.dominio.sistema.TipoDocumento;
import com.altioracorp.wst.dominio.ventas.DocumentoDetalle;
import com.altioracorp.wst.dominio.ventas.DocumentoEstado;
import com.altioracorp.wst.dominio.ventas.DocumentoReserva;
import com.altioracorp.wst.dominio.ventas.ReservaArticulo;
import com.altioracorp.wst.dominio.ventas.TipoReserva;
import com.altioracorp.wst.dominio.ventas.dto.CotizacionControles;
import com.altioracorp.wst.repositorio.sistema.IConfiguracionSistemaRepositorio;
import com.altioracorp.wst.repositorio.ventas.IDocumentoReservaRepositorio;
import com.altioracorp.wst.servicio.sistema.ISecuencialServicio;
import com.altioracorp.wst.servicio.ventas.IAprobacionReservaServicio;
import com.altioracorp.wst.servicio.ventas.IReservaArticuloServicio;
import com.altioracorp.wst.servicio.ventas.IReservaAbonoServicio;

@Service
public class ReservaAbonoServicioImpl implements IReservaAbonoServicio {

	private static final Log LOG = LogFactory.getLog(ReservaAbonoServicioImpl.class);

	@Autowired
	private IConfiguracionSistemaRepositorio configSistemaRepositorio;

	@Autowired
	private IDocumentoReservaRepositorio reservaRepositorio;

	@Autowired
	private IReservaArticuloServicio reservaArticuloServicio;

	@Autowired
	private IAprobacionReservaServicio reservaAprobacionServicio;

	@Autowired
	private ISecuencialServicio secuencialServicio;

	@Override
	public double calcularPorcentajeAbono() {
		double abono = 0;
		Optional<ConfiguracionSistema> configuracion = configSistemaRepositorio
				.findByNombre(ConfigSistema.PORCENTAJE_PREDETERMINADO_ANTICIPO);
		if (configuracion.isPresent()) {
			abono = configuracion.get().getValor();
		}
		return abono;
	}

	@Override
	public LocalDateTime calcularFechaMaximaReserva() {
		LocalDateTime fechaMaximaReserva = LocalDateTime.now();
		Optional<ConfiguracionSistema> configuracion = configSistemaRepositorio
				.findByNombre(ConfigSistema.MAXIMO_DIAS_RESERVA_STOCK);
		if (configuracion.isPresent()) {
			fechaMaximaReserva = fechaMaximaReserva.plusDays((long) configuracion.get().getValor());
		}

		return fechaMaximaReserva;
	}

	@Override
	public LocalDateTime calcularTiempoMaximoAbono() {
		LocalDateTime fechaVigencia = LocalDateTime.now();
		Optional<ConfiguracionSistema> configuracion = configSistemaRepositorio
				.findByNombre(ConfigSistema.HORA_MAXIMA_ENTREGA_ANTICIPO);
		if (configuracion.isPresent()) {
			fechaVigencia = fechaVigencia.plusHours((long) configuracion.get().getValor());
		}

		return fechaVigencia;
	}

	@Override
	@Transactional
	public DocumentoReserva guardarReserva(DocumentoReserva reserva) {
		Optional<DocumentoReserva> reservaRecargada = reservaRepositorio
				.findByCotizacion_Id(reserva.getCotizacion().getId());
		if (reservaRecargada.isEmpty()) {
			Secuencial secuencial = secuencialServicio.ObtenerSecuencialPorPuntoVentaYTipoDocumento(
					reserva.getCotizacion().getPuntoVenta().getId(), TipoDocumento.RESERVA);
			reserva.setEstado(DocumentoEstado.NUEVO);
			reserva.setTipo(TipoDocumento.RESERVA);
			reserva.setTipoReserva(TipoReserva.ABONO);
			reserva.setNumero(secuencial.getNumeroSecuencial());
			LOG.info(String.format("Reserva a guardar : %s", reserva));
			this.reservarArticulos(reserva.getDetalle());
			return reservaRepositorio.save(reserva);
		} else {
			reserva.getDetalle().forEach(x -> {
				reservaRecargada.get().agregarLineaDetalle(x);
			});
			LOG.info(String.format("Reserva a guardar : %s", reserva));
			this.reservarArticulos(reserva.getDetalle());
			return reservaRepositorio.save(reservaRecargada.get());
		}
	}

	@Override
	@Transactional
	public void enviarAprobarReserva(DocumentoReserva reserva) {
		Optional<DocumentoReserva> reservaRecargada = reservaRepositorio
				.findByCotizacion_Id(reserva.getCotizacion().getId());
		if (reservaRecargada.isPresent()) {
			reservaRecargada.get().setFechaEmision(LocalDateTime.now());
			reservaRecargada.get().setFechaMaximaReserva(reserva.getFechaMaximaReserva());
			reservaRecargada.get().setPorcentajeAbono(reserva.getPorcentajeAbono());
			if (reservaRecargada.get().getCotizacion().getControles() == null) {
				CotizacionControles controles = new CotizacionControles();
				controles.setReservaStock(Boolean.TRUE);
				reservaRecargada.get().getCotizacion().setControles(controles);
			} else {
				reservaRecargada.get().getCotizacion().getControles().setReservaStock(Boolean.TRUE);
			}

			LOG.info(String.format("Solicitud enviada para Reserva: %s", reservaRecargada.get().getNumero()));
			reservaAprobacionServicio.lanzarSolictud(reservaRecargada.get().getId());
		}
	}

	@Override
	public DocumentoReserva listarPorCotizacion(Long cotizacionID) {
		return reservaRepositorio.findByCotizacion_Id(cotizacionID).orElse(null);
	}

	private void reservarArticulos(List<DocumentoDetalle> detalle) {
		detalle.forEach(x -> {
			ReservaArticulo reserva = new ReservaArticulo();
			reserva.setCantidad(x.getCantidad());
			reserva.setCodigoArticulo(x.getCotizacionDetalle().getCodigoArticulo());
			reserva.setCodigoBodega(x.getCodigoBodega());
			reservaArticuloServicio.incremetarReserva(reserva);
		});
	}
	
	

}
