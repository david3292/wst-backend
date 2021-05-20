package com.altioracorp.wst.controlador.ventas;

import com.altioracorp.wst.dominio.ventas.DocumentoDetalle;
import com.altioracorp.wst.dominio.ventas.DocumentoEstado;
import com.altioracorp.wst.dominio.ventas.DocumentoNotaCredito;
import com.altioracorp.wst.dominio.ventas.TipoDevolucion;
import com.altioracorp.wst.dominio.ventas.dto.NotaCreditoConsultaDTO;
import com.altioracorp.wst.dominio.ventas.dto.NotaCreditoDTO;
import com.altioracorp.wst.dominio.ventas.dto.NotaCreditoSolicitudRespuestaDTO;
import com.altioracorp.wst.dominio.ventas.dto.RequiereAprobacionDevolucionDTO;
import com.altioracorp.wst.servicio.ventas.IFacturaServicio;
import com.altioracorp.wst.servicio.ventas.INotaCreditoServicio;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/devoluciones")
public class NotaCreditoControlador {

    @Autowired
    private INotaCreditoServicio servicio;
    
    @Autowired
    private IFacturaServicio facturaServicio;

    @PostMapping("/registrar")
    public ResponseEntity<DocumentoNotaCredito> registrar(@RequestBody DocumentoNotaCredito dto) {
        DocumentoNotaCredito notaCredito = servicio.registrar(dto);
        
        if (notaCredito.getTipoDevolucion().equals(TipoDevolucion.REFACTURACION) && notaCredito.getEstado().equals(DocumentoEstado.EMITIDO))
        	facturaServicio.generarRefacturacion(notaCredito);
        
        return new ResponseEntity<>(notaCredito, HttpStatus.CREATED);
    }

    @GetMapping("/validarRevision/{facturaId}")
    public ResponseEntity<Boolean> validarDevolucionesEnRevisionPorFacturaId(@PathVariable Long facturaId) {
        Boolean respuesta = this.servicio.validarDevolucionesEnRevisionPorFacturaId(facturaId);
        return new ResponseEntity<>(respuesta, HttpStatus.OK);
    }

    @PostMapping("/solicitudesAprobacion")
    public ResponseEntity<List<NotaCreditoDTO>> listarDevolcuionesPorPuntoVentaIdOverview(@RequestBody Map<String, Object> consulta) {
        List<NotaCreditoDTO> devoluciones = servicio.listarDevolucionesEnRevision(consulta);
        return new ResponseEntity<List<NotaCreditoDTO>>(devoluciones, HttpStatus.OK);
    }

    @PostMapping("/solicitudRespuesta")
    public ResponseEntity<Object> responderSolicitudDevolucion(@RequestBody NotaCreditoSolicitudRespuestaDTO respuesta) {
        DocumentoNotaCredito notaCredito = servicio.responderSolicitudDevoluciones(respuesta);
        
        if (notaCredito != null && notaCredito.getTipoDevolucion().equals(TipoDevolucion.REFACTURACION) && notaCredito.getEstado().equals(DocumentoEstado.EMITIDO))
        	facturaServicio.generarRefacturacion(notaCredito);
        
        return new ResponseEntity<Object>(0, HttpStatus.OK);
    }

    @GetMapping("/listarTodasPorCotizacion/{id}")
    public ResponseEntity<List<NotaCreditoConsultaDTO>> obtenerTodosDocumentoFacturaPorCotizacionID(@PathVariable("id") Long cotizacionID) {
        List<NotaCreditoConsultaDTO> notasCredito = servicio.listarTodasPorCotizacionID(cotizacionID);
        return new ResponseEntity<List<NotaCreditoConsultaDTO>>(notasCredito, HttpStatus.OK);
    }

    @PostMapping("actualizarLineaDetalle/{notaCreditoId}")
    public ResponseEntity<DocumentoNotaCredito> modificarLineaDetalle(@PathVariable("notaCreditoId") Long notaCreditoId, @RequestBody DocumentoDetalle linea) {
        DocumentoNotaCredito obj = servicio.actualizarLineaDetalle(notaCreditoId, linea);
        return new ResponseEntity<DocumentoNotaCredito>(obj, HttpStatus.CREATED);
    }

    @GetMapping("/{notaCreditoId}")
    public ResponseEntity<DocumentoNotaCredito> buscarPorId(@PathVariable Long notaCreditoId) {
        DocumentoNotaCredito notaCredito = this.servicio.buscarPorId(notaCreditoId);
        return new ResponseEntity<DocumentoNotaCredito>(notaCredito, HttpStatus.OK);
    }

    @PostMapping(value = "/requiereAprobacion")
    public ResponseEntity<Boolean> requiereaprobacion(@RequestBody RequiereAprobacionDevolucionDTO dto) {
        Boolean respuesta = this.servicio.requiereAprobacion(dto.getDetalles(), dto.getFacturaId(), dto.getTipoDevolucion());
        return new ResponseEntity<>(respuesta, HttpStatus.OK);
    }

    @GetMapping("/reintegrar/{id}")
    public ResponseEntity<NotaCreditoConsultaDTO> reintegrar(@PathVariable Long id) {
        NotaCreditoConsultaDTO notaCreditoDTO = this.servicio.reintegrarNotaCredito(id);
        
        DocumentoNotaCredito notaCredito = servicio.buscarPorId(id);
        
        if (notaCredito.getTipoDevolucion().equals(TipoDevolucion.REFACTURACION) && notaCredito.getEstado().equals(DocumentoEstado.EMITIDO))
        	facturaServicio.generarRefacturacion(notaCredito);
        
        return new ResponseEntity<>(notaCreditoDTO, HttpStatus.OK);
    }

    @GetMapping("/listarNotasCreditoError")
    public ResponseEntity<List<DocumentoNotaCredito>> listarNotasCreditoEstadoError() {
        List<DocumentoNotaCredito> facturas = this.servicio.listarPorEstadoError();
        return new ResponseEntity<>(facturas, HttpStatus.OK);
    }
}
