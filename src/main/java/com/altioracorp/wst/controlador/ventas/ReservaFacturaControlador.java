package com.altioracorp.wst.controlador.ventas;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.altioracorp.wst.dominio.ventas.DocumentoDetalle;
import com.altioracorp.wst.dominio.ventas.DocumentoReserva;
import com.altioracorp.wst.dominio.ventas.Entrega;
import com.altioracorp.wst.dominio.ventas.FacturaResumenDTO;
import com.altioracorp.wst.dominio.ventas.dto.FacturaDTO;
import com.altioracorp.wst.dominio.ventas.dto.ReservaConsultaDTO;
import com.altioracorp.wst.dominio.ventas.dto.ReservaDTO;
import com.altioracorp.wst.dominio.ventas.dto.ReservaFacturaDTO;
import com.altioracorp.wst.servicio.ventas.IReservaFacturaServicio;

@RestController
@RequestMapping("/reservaciones/factura")
public class ReservaFacturaControlador {

	@Autowired
	private IReservaFacturaServicio servicio;

	@GetMapping("/entregas")
	public ResponseEntity<List<Entrega>> listarEntregas() {
		List<Entrega> lista = Stream.of(Entrega.values()).collect(Collectors.toList());
		return new ResponseEntity<List<Entrega>>(lista, HttpStatus.OK);
	}

	@PostMapping("/registrarDetalle")
	public ResponseEntity<DocumentoReserva> registrarDetalle(@RequestBody DocumentoReserva dto) {
		DocumentoReserva reserva = servicio.guardarDetalle(dto);
		return new ResponseEntity<DocumentoReserva>(reserva, HttpStatus.CREATED);
	}

	@PostMapping("/registrarCabecera")
	public ResponseEntity<DocumentoReserva> registrarCabecera(@RequestBody DocumentoReserva dto) {
		DocumentoReserva reserva = servicio.guardarCabecera(dto);
		return new ResponseEntity<DocumentoReserva>(reserva, HttpStatus.CREATED);
	}

	@GetMapping("/resumen/{id}")
	public ResponseEntity<FacturaResumenDTO> obtenerFacturaResumen(@PathVariable("id") Long reservaId) {
		FacturaResumenDTO resumen = servicio.crearResumenFactura(reservaId);
		return new ResponseEntity<FacturaResumenDTO>(resumen, HttpStatus.OK);
	}

	@DeleteMapping("/{reservaId}/{cotizacionDetalleId}")
	public ResponseEntity<Object> eliminarFacturaDetalle(@PathVariable("reservaId") Long reservaId,
			@PathVariable("cotizacionDetalleId") Long cotizacionDetalleId) {
		servicio.eliminarReservaDetallesPorCotizacionDetalle(reservaId, cotizacionDetalleId);
		return new ResponseEntity<Object>(HttpStatus.NO_CONTENT);
	}

	@GetMapping("/listarPorCotizacion/{id}")
	public ResponseEntity<DocumentoReserva> obtenerDocumentoReservaPorTipoYCotizacionID(
			@PathVariable("id") Long cotizacionID) {
		DocumentoReserva reserva = servicio.listarPorCotizacionID(cotizacionID);
		return new ResponseEntity<DocumentoReserva>(reserva, HttpStatus.OK);
	}

	@GetMapping("/validarReserva/{id}")
	public ResponseEntity<List<String>> validarReservaParaFacturar(@PathVariable("id") Long reservaID) {
		List<String> resumen = servicio.validarReservaParaFacturar(reservaID);
		return new ResponseEntity<List<String>>(resumen, HttpStatus.OK);
	}

	@GetMapping("/procesar/{id}")
	public ResponseEntity<FacturaDTO> procesar(@PathVariable Long id) {
		FacturaDTO facturaDTO = servicio.procesarReservaFactura(id);
		return new ResponseEntity<FacturaDTO>(facturaDTO, HttpStatus.OK);
	}
	
	@GetMapping("/listarTodasPorCotizacionId/{id}")
	public ResponseEntity<List<ReservaFacturaDTO>> obtenerTodosDocumentoReservaPorCotizacionID(
			@PathVariable("id") Long cotizacionID) {
		List<ReservaFacturaDTO> reservas = servicio.listarTodasPorCotizacionID(cotizacionID);
		return new ResponseEntity<List<ReservaFacturaDTO>>(reservas, HttpStatus.OK);
	}
	
	@GetMapping("/listarEmitidasPorCotizacionId/{id}")
	public ResponseEntity<List<String>> obtenerDocumentosReservaEmitidosPorCotizacionID(
			@PathVariable("id") Long cotizacionID) {
		List<String> reservas = servicio.listarNumeroReservasEmitidasPorCotizacionID(cotizacionID);
		return new ResponseEntity<List<String>>(reservas, HttpStatus.OK);
	}
	
	@GetMapping("/cerrar/{cotizacionID}")
	public ResponseEntity<Object> cerrar(@PathVariable("cotizacionID") Long cotizacionID) {
		servicio.cerrar(cotizacionID);
		return new ResponseEntity<Object>(Boolean.TRUE, HttpStatus.OK);
	}
	
	@GetMapping("/reservasPorFacturar/{puntoVentaId}")
	public ResponseEntity<List<DocumentoReserva>> listarReservasPorFacturar(@PathVariable("puntoVentaId") Long puntoVentaId) {
		List<DocumentoReserva> reservas = servicio.listarReservasPorFacturarPorUsuarioYPuntoVenta(puntoVentaId);
		return new ResponseEntity<List<DocumentoReserva>>(reservas, HttpStatus.OK);
	}
	
	@GetMapping("/anular/{reservaId}")
	public ResponseEntity<Object> anularReserva(@PathVariable("reservaId") Long reservaId) {
		servicio.anularReservaPorId(reservaId);
		return new ResponseEntity<Object>(true, HttpStatus.OK);
	}
	
	@PostMapping("/consulta")
    public ResponseEntity<Page<ReservaDTO>> consultarReservas(final Pageable page, @RequestBody final ReservaConsultaDTO consulta) {
        final Page<ReservaDTO> resultadoConsulta = this.servicio.consultaReservas(page, consulta);
        return ResponseEntity.ok(resultadoConsulta);
    }
	
	@GetMapping("/detalle/{reservaId}")
	public ResponseEntity<List<DocumentoDetalle>> listarDetallePorReservaId(@PathVariable("reservaId") Long reservaId) {
		List<DocumentoDetalle> reservas = servicio.obtenerDetallePorReservaID(reservaId);
		return new ResponseEntity<List<DocumentoDetalle>>(reservas, HttpStatus.OK);
	}

}
