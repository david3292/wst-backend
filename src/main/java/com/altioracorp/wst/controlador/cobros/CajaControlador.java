package com.altioracorp.wst.controlador.cobros;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.altioracorp.wst.dominio.cobros.Caja;
import com.altioracorp.wst.dominio.cobros.CajaConsultaDTO;
import com.altioracorp.wst.dominio.cobros.CajaDetalle;
import com.altioracorp.wst.dominio.cobros.CajaDetalleConsultaDTO;
import com.altioracorp.wst.dominio.cobros.CierreCajaDetalleDTO;
import com.altioracorp.wst.servicio.cobros.ICajaService;

@RestController
@RequestMapping("/cierreCajas")
public class CajaControlador {

	@Autowired
	private ICajaService servicio;
	
	@GetMapping("/{puntoVentaID}")
	public ResponseEntity<Caja> listarBancos(@PathVariable("puntoVentaID") Long puntoVentaID) {
		Caja preCierreCaja = servicio.consultarCobrosPorPuntoVenta(puntoVentaID);
		return new ResponseEntity<Caja>(preCierreCaja, HttpStatus.OK);
	}
	
	@PostMapping(value="/cerrar", produces = MediaType.APPLICATION_OCTET_STREAM_VALUE)
	public ResponseEntity<byte[]> cerrarCaja(@RequestBody Caja caja) {
		byte[] data = servicio.cerrarCaja(caja);
		return new ResponseEntity<byte[]>(data, HttpStatus.OK);
	}
	
	@PostMapping("/consulta")
	public ResponseEntity<Page<CajaDetalle>> consultarCobros(final Pageable page,
			@RequestBody final CajaConsultaDTO consulta) {
		final Page<CajaDetalle> resultadoConsulta = this.servicio.consultarCajaCierre(page, consulta);
		return ResponseEntity.ok(resultadoConsulta);
	}
	
	@PostMapping("/cierreCajaDetalle")
	public ResponseEntity<List<CierreCajaDetalleDTO>> consultarCobros(@RequestBody final CajaDetalleConsultaDTO consulta) {
		final List<CierreCajaDetalleDTO> resultadoConsulta = this.servicio.consultarDetallaCierreCaje(consulta);
		return ResponseEntity.ok(resultadoConsulta);
	}
	
	@GetMapping(value="/reporte/{cajaId}", produces = MediaType.APPLICATION_OCTET_STREAM_VALUE)
	public ResponseEntity<byte[]> cerrarCaja(@PathVariable("cajaId") Long cajaId) {
		byte[] data = servicio.generarReporteCierreCaja(cajaId);
		return new ResponseEntity<byte[]>(data, HttpStatus.OK);
	}
}
