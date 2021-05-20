package com.altioracorp.wst.controlador.ventas;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.altioracorp.wst.dominio.sistema.PerfilNombre;
import com.altioracorp.wst.dominio.ventas.AprobacionDocumento;
import com.altioracorp.wst.dominio.ventas.dto.CatalogoAprobacionEstadoDTO;
import com.altioracorp.wst.servicio.ventas.IAprobacionCotizacionServicio;
import com.altioracorp.wst.servicio.ventas.IAprobacionDocumentoServicio;
import com.altioracorp.wst.servicio.ventas.IAprobacionFacturaServicio;
import com.altioracorp.wst.servicio.ventas.IAprobacionReservaServicio;

@RestController
@RequestMapping("/aprobacionesDocumento")
public class AprobacionDocumentoControlador {

	@Autowired
	private IAprobacionDocumentoServicio servicio;

	@Autowired
	private IAprobacionFacturaServicio servicioFactura;

	@Autowired
	private IAprobacionCotizacionServicio servicioCotizacion;

	@Autowired
	private IAprobacionReservaServicio servicioReserva;

	@GetMapping("/catalogoEstados")
	public ResponseEntity<List<CatalogoAprobacionEstadoDTO>> obtenerCatalogoAprobacionEstado() {
		List<CatalogoAprobacionEstadoDTO> catalogo = servicio.catalogoAprobacionEstado();
		return new ResponseEntity<List<CatalogoAprobacionEstadoDTO>>(catalogo, HttpStatus.OK);
	}

	@GetMapping("/{perfil}")
	public ResponseEntity<List<AprobacionDocumento>> listarSolicitudesPorPerfil(
			@PathVariable("perfil") PerfilNombre perfil) {
		List<AprobacionDocumento> solicitudes = servicio.listarSolicitudesPendientesPorPerfil(perfil);
		return new ResponseEntity<List<AprobacionDocumento>>(solicitudes, HttpStatus.OK);
	}

	@GetMapping("/lanzarSolicitudFactura/{facturaID}")
	public void lanzarSolicitudFactura(@PathVariable("facturaID") Long facturaID) {
		servicioFactura.lanzarSolictud(facturaID);
	}
	
	@GetMapping("/lanzarSolicitudReservaFactura/{reservaID}")
	public void lanzarSolicitudReservaFactura(@PathVariable("reservaID") Long reservaID) {
		servicioReserva.lanzarSolictud(reservaID);
	}

	@PostMapping("/procesarSolicitudFactura")
	public ResponseEntity<Object> procesarSolicitudFactura(@RequestBody AprobacionDocumento aprobacionDocumento) {
		servicioFactura.procesarSolicitud(aprobacionDocumento);
		return new ResponseEntity<Object>(Boolean.TRUE, HttpStatus.OK);
	}

	@PostMapping("/procesarSolicitudCotizacion")
	public ResponseEntity<Object> procesarSolicitudCotizacion(@RequestBody AprobacionDocumento aprobacionDocumento) {
		servicioCotizacion.procesarSolicitud(aprobacionDocumento);
		return new ResponseEntity<Object>(Boolean.TRUE, HttpStatus.OK);
	}
	
	@PostMapping("/procesarSolicitudReserva")
	public ResponseEntity<Object> procesarSolicitudReserva(@RequestBody AprobacionDocumento aprobacionDocumento) {
		servicioReserva.procesarSolicitud(aprobacionDocumento);
		return new ResponseEntity<Object>(Boolean.TRUE, HttpStatus.OK);
	}
}
