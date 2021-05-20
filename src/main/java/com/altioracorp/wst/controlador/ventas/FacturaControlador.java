package com.altioracorp.wst.controlador.ventas;


import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

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

import com.altioracorp.wst.dominio.ventas.DocumentoEstado;
import com.altioracorp.wst.dominio.ventas.DocumentoFactura;
import com.altioracorp.wst.dominio.ventas.Entrega;
import com.altioracorp.wst.dominio.ventas.dto.FacturaConsultaDTO;
import com.altioracorp.wst.dominio.ventas.dto.FacturaCotizacionDTO;
import com.altioracorp.wst.dominio.ventas.dto.FacturaDTO;
import com.altioracorp.wst.servicio.ventas.IFacturaServicio;

@RestController
@RequestMapping("/facturaciones")
public class FacturaControlador {

	@Autowired
	private IFacturaServicio servicio;

	@GetMapping("/entregas")
	public ResponseEntity<List<Entrega>> listarEntregas() {

		List<Entrega> lista = Stream.of(Entrega.values()).collect(Collectors.toList());
		return new ResponseEntity<List<Entrega>>(lista, HttpStatus.OK);
	}

	@GetMapping("/listarPorCotizacion/{id}")
	public ResponseEntity<DocumentoFactura> obtenerDocumentoFacturaPorCotizacionID(
			@PathVariable("id") Long cotizacionID) {
		DocumentoFactura factura = servicio.listarPorCotizacionID(cotizacionID);
		return new ResponseEntity<DocumentoFactura>(factura, HttpStatus.OK);
	}

	@GetMapping("/listarTodasPorCotizacion/{id}")
	public ResponseEntity<List<FacturaDTO>> obtenerTodosDocumentoFacturaPorCotizacionID(
			@PathVariable("id") Long cotizacionID) {
		List<FacturaDTO> facturas = servicio.listarTodasPorCotizacionID(cotizacionID);
		return new ResponseEntity<List<FacturaDTO>>(facturas, HttpStatus.OK);
	}

	@GetMapping("/reintegrar/{id}")
	public ResponseEntity<FacturaDTO> reintegrar(@PathVariable Long id) {
		FacturaDTO facturaDTO = servicio.reintegrarFactura(id);
		return new ResponseEntity<FacturaDTO>(facturaDTO, HttpStatus.OK);
	}

	@GetMapping("/listarFacturasError")
	public ResponseEntity<List<DocumentoFactura>> listarFacturasEstadoError() {
		List<DocumentoFactura> facturas = servicio.listarPorEstadoError();
		return new ResponseEntity<List<DocumentoFactura>>(facturas, HttpStatus.OK);
	}
	
	@GetMapping("/estados")
    public ResponseEntity<List<DocumentoEstado>> listarEstados() {
        final List<DocumentoEstado> lista = Arrays.asList(DocumentoEstado.NUEVO, DocumentoEstado.REVISION, DocumentoEstado.APROBADO,
                DocumentoEstado.RECHAZADO, DocumentoEstado.ERROR, DocumentoEstado.VENCIDO, DocumentoEstado.EMITIDO,
                DocumentoEstado.BLOQUEADO, DocumentoEstado.COMPLETADO, DocumentoEstado.ANULADO, DocumentoEstado.CERRADO);
        return ResponseEntity.ok(lista);
    }

    @PostMapping("/consulta")
    public ResponseEntity<Page<FacturaCotizacionDTO>> consultarFacturas(final Pageable page, @RequestBody final FacturaConsultaDTO consulta) {
        final Page<FacturaCotizacionDTO> resultadoConsulta = this.servicio.consultarFacturasCotizaciones(page, consulta);
        return ResponseEntity.ok(resultadoConsulta);
    }

    @GetMapping("/emitidas/usuario/{puntoVentaID}")
    public ResponseEntity<List<DocumentoFactura>> listarEmitidasPorUsuarioYPuntoVenta(@PathVariable("puntoVentaID") long puntoVentaID) {
        List<DocumentoFactura> facturas = this.servicio.listarEmitidasPorUsuarioYPuntoVenta(puntoVentaID);
        return new ResponseEntity<>(facturas, HttpStatus.OK);
    }

    @GetMapping("/factura/{facturaID}")
    public ResponseEntity<DocumentoFactura> obtenerFacturaPorIdParaDevoluciones(@PathVariable("facturaID") long facturaID) {
        DocumentoFactura factura = this.servicio.obtenerFacturaPorIdParaDevoluciones(facturaID);
        return new ResponseEntity<>(factura, HttpStatus.OK);
    }
    
    @GetMapping(value="/reporte/{numeroFactura}",  produces = MediaType.APPLICATION_OCTET_STREAM_VALUE)
    public ResponseEntity<byte[]> obtenerFacturaPorIdParaDevoluciones(@PathVariable("numeroFactura") String numeroFactura) {
    	byte[] data = this.servicio.generarReporte(numeroFactura);
        return new ResponseEntity<byte[]>(data, HttpStatus.OK);
    }
    
  

}
