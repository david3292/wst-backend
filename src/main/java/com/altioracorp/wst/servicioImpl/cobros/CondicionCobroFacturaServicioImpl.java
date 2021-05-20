package com.altioracorp.wst.servicioImpl.cobros;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.altioracorp.wst.dominio.cobros.Cobro;
import com.altioracorp.wst.dominio.cobros.CobroDocumentoDTO;
import com.altioracorp.wst.dominio.cobros.CobroDocumentoValorDTO;
import com.altioracorp.wst.dominio.cobros.CobroEstado;
import com.altioracorp.wst.dominio.cobros.CondicionCobroEstado;
import com.altioracorp.wst.dominio.cobros.CondicionCobroFactura;
import com.altioracorp.wst.dominio.cobros.CuotaDTO;
import com.altioracorp.wst.dominio.sistema.CondicionPago;
import com.altioracorp.wst.dominio.sistema.TipoDocumento;
import com.altioracorp.wst.dominio.sistema.TipoPago;
import com.altioracorp.wst.dominio.ventas.Cotizacion;
import com.altioracorp.wst.dominio.ventas.DocumentoFactura;
import com.altioracorp.wst.dominio.ventas.DocumentoGuiaDespacho;
import com.altioracorp.wst.repositorio.cobros.ICobroRepositorio;
import com.altioracorp.wst.repositorio.cobros.ICondicionCobroFacturaRepositorio;
import com.altioracorp.wst.repositorio.sistema.ICondicionPagoRepositorio;
import com.altioracorp.wst.servicio.cobros.ICondicionCobroFacturaServicio;
import com.altioracorp.wst.servicio.ventas.IDocumentoServicio;
import com.altioracorp.wst.servicio.ventas.IGuiaDespachoServicio;
import com.altioracorp.wst.util.UtilidadesCadena;
import com.altioracorp.wst.util.UtilidadesSeguridad;

@Service
public class CondicionCobroFacturaServicioImpl implements ICondicionCobroFacturaServicio {

	private static final Log LOG = LogFactory.getLog(CondicionCobroFacturaServicioImpl.class);

	@Autowired
	private ICondicionCobroFacturaRepositorio condicionCobroFacturaRepositorio;

	@Autowired
	private ICondicionPagoRepositorio condicionPagoRepositorio;

	@Autowired
	private IGuiaDespachoServicio guiaDespachoServicio;
	
	@Autowired
	private ICobroRepositorio cobroRepositorio;
	
	@Autowired
	private IDocumentoServicio documentoServicio;


	private BigDecimal calcularCuotaPorcentaje(BigDecimal totalFactura, Double porcentaje) {
		return totalFactura.multiply(new BigDecimal(porcentaje).divide(new BigDecimal(100))).setScale(2,
				RoundingMode.HALF_UP);
	}

	private LocalDate calcularFechaPago(LocalDate fechaEmision, int numeroDias, int periodoGracia, TipoPago tipoPago) {
		if (tipoPago.equals(TipoPago.CONTADO)) {
			return fechaEmision.plusDays(numeroDias);
		} else {
			if (numeroDias == 0) {
				return fechaEmision.plusDays(numeroDias);
			}else {
				return fechaEmision.plusDays(numeroDias + periodoGracia);
			}
		}
	}

	@Override
	public List<CobroDocumentoValorDTO> consultarCobrosPorCliente(String codigoCliente) {

		List<CobroDocumentoValorDTO> valores = new ArrayList<>();

		Map<DocumentoFactura, List<CondicionCobroFactura>> cobros = condicionCobroFacturaRepositorio
				.findByEstadoInAndFacturaCodigoClienteOrderByFechaAsc(
						Arrays.asList(CondicionCobroEstado.VENCIDO, CondicionCobroEstado.NUEVO ), codigoCliente)
				.stream().collect(Collectors.groupingBy(CondicionCobroFactura::getFactura, Collectors.toList()));

		cobros.forEach((factura, lista) -> {
			LocalDate fechaAntigua = lista.stream().map(x -> x.getFecha()).min(Comparator.comparing(LocalDate::toEpochDay)).get();
			BigDecimal valorCalculado = lista.stream().map(x -> x.getSaldo()).reduce(BigDecimal.ZERO, BigDecimal::add);
			
			boolean activo = facturaEstaEnProcesoCobro(factura);
			
			valores.add(new CobroDocumentoValorDTO(factura.getId(), TipoDocumento.FACTURA, factura.getNumero(),
					valorCalculado, factura.getCotizacion().getNombreCliente(), fechaAntigua, !activo));
		});

		LOG.info(String.format("Para el cliente %s , se obtiene %s cobros.", codigoCliente, valores.size()));

		Collections.sort(valores, Comparator.comparing(CobroDocumentoValorDTO::getFechaMasAntigua));
		return valores;
	}

	@Override
	public CobroDocumentoDTO consultarDetallePagoPorFactura(Long idFactura) {
		CobroDocumentoDTO cobroDocumento = new CobroDocumentoDTO();
		List<CondicionCobroFactura> cobros = condicionCobroFacturaRepositorio
				.findByEstadoInAndFactura_IdOrderByNumeroCuotaAsc(Arrays.asList(CondicionCobroEstado.NUEVO, CondicionCobroEstado.VENCIDO),
						idFactura);
		;
		cobros.forEach(x -> {
			cobroDocumento.setIdPuntoVenta(x.getFactura().getCotizacion().getPuntoVenta().getId());
			cobroDocumento.setTipoCredito(x.getFactura().getCotizacion().getFormaPago().getDescripcion());
			cobroDocumento.setCondicionPago(x.getFactura().getCotizacion().getCondicionPago());
			cobroDocumento.agregarCuota(new CuotaDTO(x.getId(), x.getNumeroCuota(), x.getEstado(), x.getSaldo(), x.getSaldo(),
					x.getFecha(), Boolean.TRUE));
		});
		
		String numeroFactura = cobros.stream().findAny().get().getFactura().getNumero();
		Cotizacion cotizacion =  cobros.stream().findAny().get().getFactura().getCotizacion();
		if(cotizacion.isDatoInicial())
			cobroDocumento.setFacturaContabilizada(Boolean.TRUE);
		else
			cobroDocumento.setFacturaContabilizada(documentoServicio.documentoEstaContabilizado(numeroFactura));
		
		return cobroDocumento;
	}

	private void emitirDespacho(DocumentoFactura factura, List<CondicionCobroFactura> cuotas) {

		CondicionCobroFactura cuotaInicial = cuotas.stream().filter(x -> x.getNumeroCuota() == 1).findAny().get();
		
		boolean despachar = false;
		
		if (!factura.isDespachada()) {
			if (factura.getCotizacion().getFormaPago().equals(TipoPago.CREDITO_SOPORTE)) {
				if (factura.getPeriodoGracia() > 0 && cuotaInicial.getFecha().isAfter(LocalDateTime.now().toLocalDate())) {
					despachar = true;
				}
			}else {
				if (cuotaInicial.getFecha().isAfter(LocalDateTime.now().toLocalDate())) {
					despachar = true;
				}
			}
		}
		
		if (despachar) {

			DocumentoGuiaDespacho guia = guiaDespachoServicio.guardarDespachoDeFactura(factura);
			
			if (guia != null) {

				factura.setDespachada(true);

				LOG.info(String.format("Se emite guia de despacho para Factura: %s", factura.getNumero()));
			}
			

		}

	}

	private void establecerCondicionCobroFacturasVencidas() {

		List<CondicionCobroFactura> condicionCobroFacturas = condicionCobroFacturaRepositorio
				.findByEstadoAndFechaNotNull(CondicionCobroEstado.NUEVO);

		LocalDate fechaActual = LocalDateTime.now().toLocalDate();

		condicionCobroFacturas.forEach(cuota -> {

			if (fechaActual.isAfter(cuota.getFecha())) {

				LOG.info(String.format("Cuota N°: %s VENCIDAD, de la factura %s", cuota.getNumeroCuota(),
						cuota.getFactura().getNumero()));

				vencerCondicionCobroFactura(cuota);

			}

		});

	}

	@Override
	public boolean existenCodicionesCobroVencidasPorCliente(String codigoCliente) {
		
//		List<CondicionCobroFactura> condicionCobroFacturas = condicionCobroFacturaRepositorio
//				.findByFactura_Cotizacion_CodigoCliente(codigoCliente);
//		
//		return condicionCobroFacturas.stream().anyMatch(x -> x.getEstado().equals(CondicionCobroEstado.VENCIDO));
		
		return condicionCobroFacturaRepositorio.existsByFactura_Cotizacion_CodigoClienteAndEstado(codigoCliente, CondicionCobroEstado.VENCIDO);
	
	}

	private CondicionPago obtenerCondicionPago(String condicionPago) {
		return condicionPagoRepositorio.findByTermino(condicionPago).orElse(null);
	}

	@Override
	public void registrarDesgloseCobrosFactura(DocumentoFactura factura) {

		CondicionPago condicion = obtenerCondicionPago(factura.getCotizacion().getCondicionPago());
		if (condicion != null) {

			List<CondicionCobroFactura> pagos = new ArrayList<>();

			condicion.getDetalle().forEach(x -> {
				LocalDate fechaPago = calcularFechaPago(factura.getFechaEmision().toLocalDate(), x.getNumeroDias(),
						factura.getPeriodoGracia(), factura.getCotizacion().getFormaPago());
				BigDecimal valorCuota = calcularCuotaPorcentaje(factura.getTotal(), x.getPorcentaje());
				pagos.add(new CondicionCobroFactura(factura, fechaPago, x.getNumeroCuota(),
						x.getPorcentaje().intValue(), x.getNumeroDias(), valorCuota));
			});
			
			correccionValoresCuotas(pagos, factura);
			
			LOG.info(String.format("Guardando %s pago(s) para Factura: %s", pagos.size(), factura.getNumero()));
			condicionCobroFacturaRepositorio.saveAll(pagos);

			emitirDespacho(factura, pagos);

		} else {
			LOG.error(String.format(
					"No se realizó el desglose de pagos para la factura %s => Causa: No se enontró condición de pago",
					factura.getNumero()));
		}
	}

	@Transactional(readOnly = false, propagation = Propagation.REQUIRED)
	private void vencerCondicionCobroFactura(CondicionCobroFactura condicionCobroFactura) {

		if (condicionCobroFactura.getEstado().equals(CondicionCobroEstado.NUEVO)) {

			condicionCobroFactura.setEstado(CondicionCobroEstado.VENCIDO);

			condicionCobroFacturaRepositorio.save(condicionCobroFactura);
		}
		
	}

	@Scheduled(cron = "0 55 7 1/1 * ?")
	public void vigenciaCondicionCobroFacturas() {
		LOG.info("Iniciando Tarea Programada: VIGENCIA CONDICIÓN COBRO FACTURA");

		establecerCondicionCobroFacturasVencidas();

		LOG.info("Finalizando Tarea Programada: VIGENCIA CONDICIÓN COBRO FACTURA");
	}	
	
	private boolean facturaEstaEnProcesoCobro(DocumentoFactura factura) {
		List<Cobro> cobros = cobroRepositorio.findByCodigoClienteAndEstadoIn(factura.getCotizacion().getCodigoCliente(), Arrays.asList(CobroEstado.NUEVO));
		
		String usuarioEnSesion = UtilidadesSeguridad.usuarioEnSesion();
		
		if(cobros.isEmpty()) {
			LOG.info(String.format("Verificando Factura %s en proceso de cobro NO", factura.getNumero()));
			return Boolean.FALSE;
		}else {
			
			List<Cobro> cobrosAux = new ArrayList<>();
			for (Cobro cobro : cobros) {
				
				cobro.getCobroFormaPagos().forEach(x ->{
					if(UtilidadesCadena.noEsNuloNiBlanco(x.getNumeroFactura())) {
						if(x.getNumeroFactura().equals(factura.getNumero()) && !x.getModificadoPor().equals(usuarioEnSesion)) {
							cobrosAux.add(cobro);
						}
					}
					
				});
			
			}
			LOG.info(String.format("Verificando Factura %s en proceso de cobro %s", factura.getNumero(), cobrosAux.isEmpty()? "NO":"SI"));
			return cobrosAux.isEmpty()? Boolean.FALSE: Boolean.TRUE;
		}
	}

	@Override
	public BigDecimal deudaTotalPorCliente(String codigoCliente) {
		BigDecimal deuda = BigDecimal.ZERO;
		List<CondicionCobroFactura> condicionesValores = condicionCobroFacturaRepositorio
				.findByEstadoInAndFacturaCodigoClienteOrderByFechaAsc(
						Arrays.asList(CondicionCobroEstado.VENCIDO, CondicionCobroEstado.NUEVO), codigoCliente);

		deuda = condicionesValores.stream().map(x -> x.getSaldo()).reduce(BigDecimal.ZERO, BigDecimal::add);
		LOG.info(String.format("Calculando Deuda Total:  %s para el Cliente %s", deuda, codigoCliente));

		return deuda;
	}
	
	private void correccionValoresCuotas(List<CondicionCobroFactura> pagos, DocumentoFactura factura) {
		BigDecimal cuotasValorTotal = pagos.stream().map(x -> x.getValor()).reduce(BigDecimal.ZERO, BigDecimal::add);

		if (factura.getTotal().compareTo(cuotasValorTotal) != 0) {

			LOG.info(String.format("Necesario Correcion de valores cuotas? : %s", Boolean.TRUE));

			BigDecimal totalMenosUltimaCuota = BigDecimal.ZERO;

			for (int i = 0; i < pagos.size() - 1; i++) {
				totalMenosUltimaCuota = totalMenosUltimaCuota.add(pagos.get(i).getValor());
			}

			pagos.get(pagos.size() - 1).setValor(factura.getTotal().subtract(totalMenosUltimaCuota));
			pagos.get(pagos.size() - 1).setSaldo(factura.getTotal().subtract(totalMenosUltimaCuota));

			LOG.info(String.format("Valores rectificados para la factura %s con total : $%s => cuotas %s",
					factura.getNumero(), factura.getTotal(), pagos));
		} else {
			LOG.info(String.format("Necesario Correcion de valores cuotas? : %s", Boolean.FALSE));
		}
	}

}
