package com.altioracorp.wst.servicio.ventas;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.altioracorp.wst.dominio.ventas.DocumentoDetalle;
import com.altioracorp.wst.dominio.ventas.DocumentoEstado;
import com.altioracorp.wst.dominio.ventas.DocumentoReserva;
import com.altioracorp.wst.dominio.ventas.FacturaResumenDTO;
import com.altioracorp.wst.dominio.ventas.dto.FacturaDTO;
import com.altioracorp.wst.dominio.ventas.dto.ReservaConsultaDTO;
import com.altioracorp.wst.dominio.ventas.dto.ReservaDTO;
import com.altioracorp.wst.dominio.ventas.dto.ReservaFacturaDTO;

public interface IReservaFacturaServicio {
	
	DocumentoReserva guardarCabecera(DocumentoReserva reserva);
	
	DocumentoReserva guardarDetalle(DocumentoReserva reserva);
	
	void eliminarReservaDetallesPorCotizacionDetalle(long reservaId, long cotizacionDetalleId);
	
	FacturaResumenDTO crearResumenFactura(Long reservaId);
	
	DocumentoReserva listarPorCotizacionID(long cotizacionID);
	
	List<ReservaFacturaDTO> listarTodasPorCotizacionID(long cotizacionID);
	
	List<String> listarNumeroReservasEmitidasPorCotizacionID(long cotizacionID);
	
	List<String> validarReservaParaFacturar(long reservaId);
	
	FacturaDTO procesarReservaFactura(long reservaId);
	
	DocumentoReserva buscarReserva(long reservaId);
	
	void anularReserva(DocumentoReserva reserva);
	
	//DocumentoFactura procesarFacturaEnTransferencia(DocumentoReserva reserva);
	
	boolean cerrar(long cotizacionId);
	
	void cambiarEstado(DocumentoReserva reserva, DocumentoEstado estado);
	
	List<DocumentoReserva> listarReservasPorFacturarPorUsuarioYPuntoVenta(long puntoVentaId);
	
	void anularReservaPorId(Long reservaId);
	
	Page<ReservaDTO> consultaReservas(final Pageable pageable, final ReservaConsultaDTO consulta);
	
	List<DocumentoDetalle> obtenerDetallePorReservaID(long reservaId);

}
