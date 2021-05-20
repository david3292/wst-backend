package com.altioracorp.wst.controlador.cobros;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.altioracorp.gpintegrator.client.Bank.Bank;
import com.altioracorp.gpintegrator.client.CreditCard.CreditCard;
import com.altioracorp.gpintegrator.client.ReceivablePostedTransaction.Rm20101Header;
import com.altioracorp.wst.dominio.cobros.Cobro;
import com.altioracorp.wst.dominio.cobros.CobroConsultaDTO;
import com.altioracorp.wst.dominio.cobros.CobroCuotaFactura;
import com.altioracorp.wst.dominio.cobros.CobroDTO;
import com.altioracorp.wst.dominio.cobros.CobroDocumentoDTO;
import com.altioracorp.wst.dominio.cobros.CobroDocumentoValorDTO;
import com.altioracorp.wst.dominio.cobros.CobroEstado;
import com.altioracorp.wst.servicio.cobros.ICobroServicio;
import com.altioracorp.wst.servicio.cobros.ICondicionCobroFacturaServicio;

@RestController
@RequestMapping("/cobros")
public class CobroControlador {

    @Autowired
    private ICobroServicio servicio;

    @Autowired
    private ICondicionCobroFacturaServicio servicioCondicionCobroFactura;

    @GetMapping("/bancos")
    public ResponseEntity<List<Bank>> listarBancos() {
        List<Bank> bancos = servicio.listarBancos();
        return new ResponseEntity<List<Bank>>(bancos, HttpStatus.OK);
    }

    @GetMapping("/tarjetasCredito")
    public ResponseEntity<List<CreditCard>> listarTarjetasCredito() {
        List<CreditCard> tarjetas = servicio.listarTarjetasCredito();
        return new ResponseEntity<List<CreditCard>>(tarjetas, HttpStatus.OK);
    }

    @GetMapping("/notasCredito/{codigoCliente}")
    public ResponseEntity<List<Rm20101Header>> listarNotasCreditoPorFactura(@PathVariable("codigoCliente") String codigoCliente) {
        List<Rm20101Header> notasCredito = servicio.listarNotasCreditoPorCliente(codigoCliente);
        return new ResponseEntity<List<Rm20101Header>>(notasCredito, HttpStatus.OK);
    }

    @GetMapping("/anticipos/{codigoCliente}")
	public ResponseEntity<List<Rm20101Header>> listarAnticiposGP(@PathVariable("codigoCliente") String codigoCliente) {
		List<Rm20101Header> anticipoDocumentos = servicio.listarCobrosPorClienteGP(codigoCliente);
		return new ResponseEntity<List<Rm20101Header>>(anticipoDocumentos, HttpStatus.OK);
	}

	@GetMapping("/condicionesCobroFactura/{codigoCliente}")
    public ResponseEntity<List<CobroDocumentoValorDTO>> listarCondicionesCobrosFacturaPorCodigoCliente(@PathVariable("codigoCliente") String codigoCliente) {
        List<CobroDocumentoValorDTO> condicionesCobros = servicioCondicionCobroFactura.consultarCobrosPorCliente(codigoCliente);
        return new ResponseEntity<List<CobroDocumentoValorDTO>>(condicionesCobros, HttpStatus.OK);
    }

    @GetMapping("/detalleCobroFactura/{idFactura}")
    public ResponseEntity<CobroDocumentoDTO> obtenerDetalleCobroPorFacturaId(@PathVariable("idFactura") Long idFactura) {
        CobroDocumentoDTO condicionesCobros = servicioCondicionCobroFactura.consultarDetallePagoPorFactura(idFactura);
        return new ResponseEntity<CobroDocumentoDTO>(condicionesCobros, HttpStatus.OK);
    }

    @PostMapping("/registrar")
    public ResponseEntity<Cobro> registrarCobro(@RequestBody Cobro cobro) {
        Cobro cobroGuardado = servicio.registrarCobro(cobro);
        return new ResponseEntity<Cobro>(cobroGuardado, HttpStatus.OK);
    }   
    
    @GetMapping("/procesarCobro/{idCobro}")
    public ResponseEntity<CobroDTO> procesarCobro(@PathVariable("idCobro") Long idCobro) {
        CobroDTO respuesta = servicio.procesarCobro(idCobro);
        return new ResponseEntity<CobroDTO>(respuesta, HttpStatus.OK);
    }
    
    @GetMapping("/codigoCliente/{codigoCliente}")
    public ResponseEntity<Cobro> buscarCobro(@PathVariable("codigoCliente") String codigoCliente) {
        Cobro cobroGuardado = servicio.buscarCobroPorEstadoYCodigoClienteYUsuario(codigoCliente);
        return new ResponseEntity<Cobro>(cobroGuardado, HttpStatus.OK);
    }	
    
	@GetMapping("/validarChequeEnOtrosCobros/{codigoCliente}/{numeroCheque}")
	public ResponseEntity<Object> buscarCobro(@PathVariable("codigoCliente") String codigoCliente,
			@PathVariable("numeroCheque") String numeroCheque) {
		Boolean respuesta = servicio.exsiteNumeroChequeEnOtrosCobros(codigoCliente, numeroCheque);
		return new ResponseEntity<Object>(respuesta, HttpStatus.OK);
	}
    
    @GetMapping(value = "/generarReporte/{numero}", produces = MediaType.APPLICATION_OCTET_STREAM_VALUE)
    public ResponseEntity<byte[]> cerrarCaja(@PathVariable("numero") String numero) {
        byte[] data = servicio.generarReportePorNumero(numero);
        return new ResponseEntity<byte[]>(data, HttpStatus.OK);
    }    

    @DeleteMapping("cobroFormaPago/{id}/{cobroID}")
    public ResponseEntity<Object> eliminar(@PathVariable("id") Long id, @PathVariable("cobroID") Long cobroID) {
        servicio.eliminarCobroFormaPago(id, cobroID);
        return new ResponseEntity<Object>(HttpStatus.NO_CONTENT);
    }    
    
    @GetMapping("/estados")
    public ResponseEntity<List<CobroEstado>> listarEstados() {
        final List<CobroEstado> lista = Arrays.asList(CobroEstado.NUEVO, CobroEstado.PENDIENTE, CobroEstado.CERRADO);
        return ResponseEntity.ok(lista);
    }

    @PostMapping("/consulta")
    public ResponseEntity<Page<Cobro>> consultarCobros(final Pageable page, @RequestBody final CobroConsultaDTO consulta) {
        final Page<Cobro> resultadoConsulta = this.servicio.consultarCobros(page, consulta);
        return ResponseEntity.ok(resultadoConsulta);
    }
    
    @PostMapping("/registrarCobroGeneral")
    public ResponseEntity<Cobro> registrarCobroGeneral(@RequestBody Cobro cobro) {
        Cobro cobroGuardado = servicio.registrarCobroGeneral(cobro);
        return new ResponseEntity<Cobro>(cobroGuardado, HttpStatus.OK);
    }
    
    @GetMapping("/{cobroId}")
    public ResponseEntity<Cobro> buscarCobroPorId(@PathVariable("cobroId") long cobroId) {
        Cobro cobro = servicio.listarCobroPorId(cobroId);
        return new ResponseEntity<Cobro>(cobro, HttpStatus.OK);
    }
    
	@PostMapping("/registrarCobroCuotas/{cobroId}")
	public ResponseEntity<Cobro> registrarCobroGeneral(@PathVariable("cobroId") long cobroId,
			@RequestBody List<CobroCuotaFactura> cuotas) {
		Cobro cobroGuardado = servicio.registrarCobroCuotaFactura(cobroId, cuotas);
		return new ResponseEntity<Cobro>(cobroGuardado, HttpStatus.OK);
	}
	
	@GetMapping("/deudaCliente/{codigoCliente}")
    public ResponseEntity<BigDecimal> obtenerDedudaCliente(@PathVariable("codigoCliente") String codigoCliente) {
        BigDecimal deuda = servicioCondicionCobroFactura.deudaTotalPorCliente(codigoCliente);
        return new ResponseEntity<BigDecimal>(deuda, HttpStatus.OK);
    }

	@GetMapping("/validarAnticipoTieneAplicacionesEstadoError/{numeroAnticipo}")
	public ResponseEntity<Object> validarAnticipo(@PathVariable("numeroAnticipo") String numeroAnticipo) {
		Boolean respuesta = servicio.validarAnticipoTieneAplicacionesConEstadoError(numeroAnticipo);
		return new ResponseEntity<Object>(respuesta, HttpStatus.OK);
	}

	@GetMapping("/pendiente")
	public ResponseEntity<List<Cobro>> buscarCobroEstadoPendiente() {
		List<Cobro>cobroGuardado = servicio.listarCobrosEstadoPendiente();
		return new ResponseEntity<List<Cobro>>(cobroGuardado, HttpStatus.OK);
	}
	
    @GetMapping("/reintegrarCobro/{idCobro}")
    public ResponseEntity<CobroDTO> reintegrarCobro(@PathVariable("idCobro") Long idCobro) {
        CobroDTO respuesta = servicio.reintegrarCobro(idCobro);
        return new ResponseEntity<CobroDTO>(respuesta, HttpStatus.OK);
    }

}
