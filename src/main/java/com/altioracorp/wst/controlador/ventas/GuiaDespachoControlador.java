package com.altioracorp.wst.controlador.ventas;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

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

import com.altioracorp.wst.controlador.MensajesControlador;
import com.altioracorp.wst.dominio.logistica.dto.GuiaDespachoConsulta;
import com.altioracorp.wst.dominio.logistica.dto.GuiaDespachoDTO;
import com.altioracorp.wst.dominio.ventas.DocumentoBase;
import com.altioracorp.wst.dominio.ventas.DocumentoEstado;
import com.altioracorp.wst.dominio.ventas.DocumentoFactura;
import com.altioracorp.wst.dominio.ventas.DocumentoGuiaDespacho;
import com.altioracorp.wst.dominio.ventas.DocumentoGuiaRemision;
import com.altioracorp.wst.servicio.ventas.IGuiaDespachoServicio;

@RestController
@RequestMapping("/despachos")
public class GuiaDespachoControlador {

	@Autowired
	private IGuiaDespachoServicio servicio;
	
	@GetMapping("/estados")
	public ResponseEntity<List<DocumentoEstado>> listarEntregas() {
		List<DocumentoEstado> lista = Arrays.asList(DocumentoEstado.EMITIDO, DocumentoEstado.COMPLETADO);
		return ResponseEntity.ok(lista);
	}
	
	@GetMapping(value="/reportePorFactura/{idFactura}", produces = MediaType.APPLICATION_OCTET_STREAM_VALUE)
	public ResponseEntity<byte[]> generarReportePorFacturaID(@PathVariable("idFactura") Long idFactura) {
		byte [] data = null;
		data= servicio.generarReportePorFacturaID(idFactura);
		return new ResponseEntity<byte[]>(data, HttpStatus.CREATED);
	}
	
	@GetMapping("/generarReporteGR/{guiaRemisionId}")
	public ResponseEntity<byte[]> generarReporteGuiaRemision(@PathVariable("guiaRemisionId") long guiaRemisionId){
		byte[] data = servicio.generarReporteGuiaRemision(guiaRemisionId);
		return new ResponseEntity<byte[]>(data, HttpStatus.CREATED);
	}
	
	@GetMapping(value="/reporte/{id}", produces = MediaType.APPLICATION_OCTET_STREAM_VALUE)
	public ResponseEntity<byte[]> generarReporte(@PathVariable("id") Long id) {
		byte [] data = null;
		data= servicio.generarReporte(id);
		return new ResponseEntity<byte[]>(data, HttpStatus.CREATED);
	}
	
	@PostMapping(value="/reporte/cliente/{id}", produces = MediaType.APPLICATION_OCTET_STREAM_VALUE)
	public ResponseEntity<byte[]> generarReporteDespachoCliente(@PathVariable("id") Long id, @RequestBody DocumentoGuiaRemision guiaRemision) {
		byte [] data = null;
		data= servicio.generarReporteDespachoCliente(id, guiaRemision);
		return new ResponseEntity<byte[]>(data, HttpStatus.CREATED);
	}
	
	@GetMapping("/listarPorCotizacion/{cotizacionID}")
	public ResponseEntity<List<Long>> listarPorUsuario(@PathVariable("cotizacionID") Long cotizacionID) {
		List<Long> ids = servicio.listarGuiasPorCotizacionID(cotizacionID);
		return new ResponseEntity<List<Long>>(ids, HttpStatus.OK);
	}
	
	@PostMapping("/overview")
	public ResponseEntity<List<GuiaDespachoDTO>> obtenerGuiasDespachoOverView(@RequestBody Map<String, Object> consulta){
		List<GuiaDespachoDTO> guiasDespacho = this.servicio.obtenerGuiasDespachoPorUsuarioIdyPuntoVentaId(consulta);
		return new ResponseEntity<List<GuiaDespachoDTO>>(guiasDespacho, HttpStatus.OK);
	}
	
	@GetMapping("/guiasRemision/{despachoId}")
	public ResponseEntity<List<DocumentoGuiaRemision>> obtenerGuiasRemisionPorDespachoId(@PathVariable("despachoId")long despachoId){
		List<DocumentoGuiaRemision> guiasRemision = this.servicio.obtenerGuiasRemisionPorDespachoId(despachoId);
		return new ResponseEntity<List<DocumentoGuiaRemision>>(guiasRemision, HttpStatus.OK);
	}
	
	@GetMapping("/{guiaDespachoId}")
	public ResponseEntity<Map<String, DocumentoBase>> obtenerGuiaDespachoPorId(@PathVariable("guiaDespachoId")long guiaDespachoId){
		Map<String, DocumentoBase> guiaDespacho = this.servicio.obtenerGuiaDespachoPorId(guiaDespachoId);
		return new ResponseEntity<Map<String, DocumentoBase>>(guiaDespacho, HttpStatus.OK);
	}
	
	@PostMapping("/procesarDespacho/{guiaDespachoId}")
	public ResponseEntity<Map<String, Object>> integrarGuiaRemision(@PathVariable("guiaDespachoId")long guiaDespachoId, @RequestBody DocumentoGuiaRemision guiaRemision){
		Map<String, Object> despachoResumen = this.servicio.procesarDespacho(guiaDespachoId, guiaRemision);
		HttpStatus statusResponse = HttpStatus.OK;
		String msgCode = String.valueOf(despachoResumen.get(MensajesControlador.MENSAJE_CODIGO));
		if(msgCode.equals("ERROR"))
			statusResponse = HttpStatus.INTERNAL_SERVER_ERROR;
		return new ResponseEntity<Map<String,Object>>(despachoResumen, statusResponse);
	}
	
	@PostMapping("/consulta")
	public ResponseEntity<Page<GuiaDespachoDTO>> consultarGuiasDespacho(Pageable page , @RequestBody GuiaDespachoConsulta consulta){
		Page<GuiaDespachoDTO> resultadoConsulta = this.servicio.consultarGuiasDespacho(page, consulta);
		return ResponseEntity.ok(resultadoConsulta);
	}
	
	@GetMapping("/listarTodasPorCotizacionId/{id}")
	public ResponseEntity<List<GuiaDespachoDTO>> obtenerTodssGuiaDespachoPorCotizacionID(
			@PathVariable("id") Long cotizacionID) {
		List<GuiaDespachoDTO> guias = servicio.listarTodasPorCotizacionID(cotizacionID);
		return new ResponseEntity<List<GuiaDespachoDTO>>(guias, HttpStatus.OK);
	}
	
	@GetMapping("/facturasEmitidasSinGuiaDespacho")
	public ResponseEntity<List<DocumentoFactura>> obtenerFacturasEmitadasSinGuiasDespacho(){
		List<DocumentoFactura> facturas = this.servicio.facturasSinGuiasDespacho();
		return new ResponseEntity<List<DocumentoFactura>>(facturas, HttpStatus.OK);
	}
	
	@GetMapping("/generarGuiaDespacho/{facturaID}")
	public ResponseEntity<DocumentoGuiaDespacho> generarGuiaApartirFactura(@PathVariable("facturaID") long facturaID){
		DocumentoGuiaDespacho guiaDespacho = this.servicio.generarGuiaDespacho(facturaID);
		return new ResponseEntity<DocumentoGuiaDespacho>(guiaDespacho, HttpStatus.OK);
	}
	
}
