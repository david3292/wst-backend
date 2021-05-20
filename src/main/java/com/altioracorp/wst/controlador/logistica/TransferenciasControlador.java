package com.altioracorp.wst.controlador.logistica;

import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.altioracorp.wst.dominio.logistica.dto.TransferenciaConsulta;
import com.altioracorp.wst.dominio.logistica.dto.TransferenciaDTO;
import com.altioracorp.wst.dominio.logistica.dto.TransferenciaResumenDTO;
import com.altioracorp.wst.dominio.sistema.TipoDocumento;
import com.altioracorp.wst.dominio.ventas.ArticuloCompartimientoDTO;
import com.altioracorp.wst.dominio.ventas.DocumentoBase;
import com.altioracorp.wst.dominio.ventas.DocumentoDetalle;
import com.altioracorp.wst.dominio.ventas.DocumentoEstado;
import com.altioracorp.wst.dominio.ventas.DocumentoGuiaRemision;
import com.altioracorp.wst.dominio.ventas.DocumentoTransferenciaEntrada;
import com.altioracorp.wst.dominio.ventas.DocumentoTransferenciaSalida;
import com.altioracorp.wst.dominio.ventas.StockSolicitudDTO;
import com.altioracorp.wst.dominio.ventas.dto.TransferenciaConsultaDTO;
import com.altioracorp.wst.servicio.ventas.IArticuloServicio;
import com.altioracorp.wst.servicio.ventas.ITransferenciaEntradaServicio;
import com.altioracorp.wst.servicio.ventas.ITransferenciaSalidaServicio;
import com.altioracorp.wst.servicio.ventas.ITransferenciaServicio;

@RestController
@RequestMapping("/transferencias")
public class TransferenciasControlador {

	@Autowired
	private ITransferenciaServicio transferenciaServicio;
	
	@Autowired
	private ITransferenciaEntradaServicio transferenciaEntradaServicio;
	
	@Autowired
	private ITransferenciaSalidaServicio transferenciaSalidaServicio; 
	
	@Autowired
	private IArticuloServicio articuloServicio;
	
	
	@GetMapping("/estados")
	public ResponseEntity<List<DocumentoEstado>> listarEntregas() {
		List<DocumentoEstado> lista = Arrays.asList(DocumentoEstado.NUEVO, DocumentoEstado.EMITIDO, DocumentoEstado.COMPLETADO, DocumentoEstado.EN_PROCESO, DocumentoEstado.ERROR );
		return ResponseEntity.ok(lista);
	}
	
	@PostMapping("/overview")
	public ResponseEntity<List<TransferenciaDTO>> listarTransferenciasPorUsuarioIdYPuntoVentaIdOverview(@RequestBody Map<String, Object> consulta) {
		List<TransferenciaDTO> transferencias = transferenciaServicio.listarTransferenciasPorUsuarioIdYPuntoVentaId(consulta);
		return new ResponseEntity<List<TransferenciaDTO>>(transferencias, HttpStatus.OK);
	}
	
	@GetMapping("/{id}")
	public ResponseEntity<DocumentoBase> buscarDocumentoTransferenciaPorId(@PathVariable("id") long id){
		DocumentoBase documentoTransferencia = transferenciaServicio.buscarDocumentoTransferenciaPorId(id);
		return new ResponseEntity<DocumentoBase>(documentoTransferencia, HttpStatus.OK);
	}
	
	@GetMapping("/{transferenciaId}/salidas")
	public ResponseEntity<List<DocumentoBase>> buscarSalidasPorTransferenciaId(@PathVariable("transferenciaId") long transferenciaId){
		List<DocumentoBase> transferenciasSalida = transferenciaServicio.obtenerDocumentosTransferenciaSalidaId(transferenciaId);
		return new ResponseEntity<List<DocumentoBase>>(transferenciasSalida, HttpStatus.OK);
	}
	
	@GetMapping("/{transferenciaSalidaId}/entradas")
	public ResponseEntity<List<DocumentoTransferenciaEntrada>> buscarTransferenciasEntradaPorFransferenciaSalidaId(@PathVariable("transferenciaSalidaId")long transferenciaSalidaId) {
		List<DocumentoTransferenciaEntrada> transferenciasSalida = transferenciaServicio.obtenerDocumentosTransferenciaEntradaId(transferenciaSalidaId);
		return new ResponseEntity<List<DocumentoTransferenciaEntrada>>(transferenciasSalida, HttpStatus.OK);
	}
	
	@GetMapping("/{id}/salida")
	public ResponseEntity<DocumentoBase> buscarTransferenciaSalidaPorId(@PathVariable("id") long transferenciaId){
		DocumentoBase documento = transferenciaServicio.buscarDocumentoTransferenciaSalidaPorId(transferenciaId);
		return new ResponseEntity<DocumentoBase>(documento, HttpStatus.OK);
	}
	
	@GetMapping("/{id}/entrada")
	public ResponseEntity<DocumentoBase> buscarTransferenciaEntradaPorId(@PathVariable("id") long transferenciaId){
		DocumentoBase documento = transferenciaServicio.buscarDocumentoTransferenciaEntradaPorId(transferenciaId);
		return new ResponseEntity<DocumentoBase>(documento, HttpStatus.OK);
	}
	
	@GetMapping("/detalles/{id}")
	public ResponseEntity<DocumentoDetalle> buscarDocumentoDetallePorId(@PathVariable("id") long id){
		DocumentoDetalle documentoDetalle = transferenciaServicio.obtenerDocumentoDetallePorIdConCompartimientos(id);
		return new ResponseEntity<DocumentoDetalle>(documentoDetalle, HttpStatus.OK);
	}
	
	@GetMapping("/salida/nueva/{id}")
	public ResponseEntity<DocumentoBase> crearNuevaTransferenciaSalida(@PathVariable("id")long transferenciaId){
		DocumentoBase nuevaTransferenciaSalida = transferenciaServicio.crearNuevaTransferenciaSalida(transferenciaId);
		return new ResponseEntity<DocumentoBase>(nuevaTransferenciaSalida, HttpStatus.OK);
	}
	
	@PostMapping("/articulo/compartimientos")
	public ResponseEntity<List<ArticuloCompartimientoDTO>> buscarArticuloContenedores(@RequestBody StockSolicitudDTO dto){
		List<ArticuloCompartimientoDTO> compartimientos = articuloServicio.obtenerStockArticuloCompartimiento(dto);
		return new ResponseEntity<List<ArticuloCompartimientoDTO>>(compartimientos, HttpStatus.OK);
	}
	
	@PostMapping("/compartimientos/{locncode}")
	public ResponseEntity<List<ArticuloCompartimientoDTO>> obtenerCompartimientosPorBodega(@RequestBody StockSolicitudDTO dto){
		List<ArticuloCompartimientoDTO> compartimientos = articuloServicio.obtenerCompartimientosPorBodega(dto);
		compartimientos = compartimientos.stream().sorted(Comparator.comparing(ArticuloCompartimientoDTO::getCompartimiento)).collect(Collectors.toList());
		return new ResponseEntity<List<ArticuloCompartimientoDTO>>(compartimientos, HttpStatus.OK);
	}
	
	@PostMapping("/{id}/detalleDocumento/salida/actualizar")
	public ResponseEntity<DocumentoTransferenciaSalida>actualizarDocumentoDetalleSalida(
			@PathVariable("id") long id,
			@RequestBody DocumentoDetalle detalle) {
		transferenciaServicio.actualizarDetalleTransferenciaSalida(id, detalle);
		return new ResponseEntity<DocumentoTransferenciaSalida>(new DocumentoTransferenciaSalida(), HttpStatus.OK);
	}
	
	@PostMapping("/{id}/detalleDocumento/entrada/actualizar")
	public ResponseEntity<DocumentoTransferenciaSalida>actualizarDocumentoDetalleEntrada(
			@PathVariable("id") long id,
			@RequestBody DocumentoDetalle detalle) {
		transferenciaServicio.actualizarDetalleTransferenciaEntrada(id, detalle);
		return new ResponseEntity<DocumentoTransferenciaSalida>(new DocumentoTransferenciaSalida(), HttpStatus.OK);
	}
	
	@PostMapping("/procesar/{transferenciaId}/{tipo}")
	public ResponseEntity<TransferenciaResumenDTO> procesarTransferencia(@PathVariable("transferenciaId")long transferenciaId, @PathVariable("tipo")String tipo){
		TransferenciaResumenDTO response;
		
		TipoDocumento tipoDocumento = TipoDocumento.valueOf(tipo);
		if(TipoDocumento.TRANSFERENCIA_INGRESO.equals(tipoDocumento))
			response = transferenciaEntradaServicio.integrarTransferenciaEntradaGp(transferenciaId);
		else
			response = transferenciaSalidaServicio.integrarTransferenciaSalidaGp(transferenciaId);
		
		if(tipoDocumento.equals(TipoDocumento.TRANSFERENCIA_SALIDA)) {			
			if(!response.getTranferenciaCreada().equals("ERROR")) {
				TransferenciaResumenDTO resumenGuiaRemision = transferenciaSalidaServicio.integrarGuiaRemisionGp(transferenciaId);
				response.setGuiaRemisionCreada(resumenGuiaRemision.getGuiaRemisionCreada());
				response.setErrorGuiaRemision(resumenGuiaRemision.getErrorGuiaRemision());				
			}
		} else {
			transferenciaEntradaServicio.procesarTransferenciaEntrada(transferenciaId);
		}
		return new ResponseEntity<TransferenciaResumenDTO>(response, HttpStatus.OK);
	}
	
	@PostMapping("/crearActualizarGuiaRemision/{transferenciaSalidaId}")
	public ResponseEntity<DocumentoBase> crearActualizarGuiaRemision(@PathVariable("transferenciaSalidaId")long transferenciaSalidaId, @Valid @RequestBody DocumentoGuiaRemision guiaRemision){
		DocumentoBase guiaRemisionCreada = transferenciaServicio.crearActualizarDocumentoGuiaRemision(transferenciaSalidaId, guiaRemision);
		return new ResponseEntity<DocumentoBase>(guiaRemisionCreada, HttpStatus.OK);
	}
	
	@GetMapping("/guiaRemision/{transferenciaSalidaId}")
	public ResponseEntity<DocumentoBase> obtenerGuiaRemisionPorTransferenciaSalidaId(@PathVariable("transferenciaSalidaId")long transferenciaSalidaId){
		DocumentoBase guiaremision = transferenciaServicio.obtenerDocumentoGuiaRemisionPorTransferenciaSalidaId(transferenciaSalidaId);
		return new ResponseEntity<DocumentoBase>(guiaremision, HttpStatus.OK);
	}	
	
	@PostMapping("/guiaRemision/integrar/{transferenciaSalidaId}")
	public ResponseEntity<DocumentoBase> integrarGuiaRemision(@PathVariable("transferenciaSalidaId")long transferenciaSalidaId){
		TransferenciaResumenDTO resumenGuiaRemisionIntegrada = transferenciaSalidaServicio.integrarTransferenciaSalidaGp(transferenciaSalidaId);
		DocumentoGuiaRemision guiaRemision = (DocumentoGuiaRemision)resumenGuiaRemisionIntegrada.getObjeto();
		return new ResponseEntity<DocumentoBase>(guiaRemision, HttpStatus.OK);
	}
	
	@GetMapping("/transferencia/reporte/{transferenciaId}")
	public ResponseEntity<byte[]> generarReporteGuiaTransferencia(@PathVariable("transferenciaId")long transferenciaId){
		byte[] reporte = transferenciaServicio.generarReporteTransferencia(transferenciaId);
		return new ResponseEntity<byte[]>(reporte, HttpStatus.OK);
	}
	
	@GetMapping("/transferenciaSalida/reporte/{tranferenciaSalidaId}")
	public ResponseEntity<byte[]> generarReporteGuiaTransferenciaSalida(@PathVariable("tranferenciaSalidaId")long tranferenciaSalidaId){
		byte[] reporte = transferenciaServicio.generarReporteTransferenciaSalida(tranferenciaSalidaId);
		return new ResponseEntity<byte[]>(reporte, HttpStatus.OK);
	}
	
	@GetMapping("/transferenciaEntrada/reporte/{tranferenciaEntradaId}")
	public ResponseEntity<byte[]> generarReporteGuiaTransferenciaEntrada(@PathVariable("tranferenciaEntradaId")long tranferenciaEntradaId){
		byte[] reporte = transferenciaServicio.generarReporteTransferenciaEntrada(tranferenciaEntradaId);
		return new ResponseEntity<byte[]>(reporte, HttpStatus.OK);
	}
	
	@GetMapping("/generarReporteGR/{guiaTransferenciaSalidaId}")
	public ResponseEntity<byte[]> generarReporteGuiaRemision(@PathVariable("guiaTransferenciaSalidaId") long guiaTransferenciaSalidaId){
		byte[] data = transferenciaSalidaServicio.generarReporteGuiaRemision(guiaTransferenciaSalidaId);
		return new ResponseEntity<byte[]>(data, HttpStatus.CREATED);
	}
	
	@PostMapping("/salida/anular/{transferenciaSalidaId}")
	public ResponseEntity<String> anularTransferenciaSalida(@PathVariable("transferenciaSalidaId")long transferenciaSalidaId){
		transferenciaSalidaServicio.anularTransferencia(transferenciaSalidaId);
		return new ResponseEntity<String>("OK",HttpStatus.OK);
	}
	
	
	@PostMapping("/consulta")
	public ResponseEntity<Page<TransferenciaDTO>> consultarTrasferencias(Pageable page, @RequestBody TransferenciaConsulta consulta){
		Page<TransferenciaDTO> resultadoConsulta = this.transferenciaServicio.consultarTransferencia(page, consulta);
		return ResponseEntity.ok(resultadoConsulta);
	}
	
	@GetMapping("/listarTodasPorCotizacion/{id}")
	public ResponseEntity<List<TransferenciaConsultaDTO>> obtenerTodosDocumentoFacturaPorCotizacionID(@PathVariable("id") Long cotizacionID) {
		List<TransferenciaConsultaDTO> transferencias = transferenciaServicio.listarTodasPorCotizacionID(cotizacionID);
		return new ResponseEntity<List<TransferenciaConsultaDTO>>(transferencias, HttpStatus.OK);
	}
}
