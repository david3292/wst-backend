package com.altioracorp.wst.servicioImpl.cobros;


import static com.altioracorp.wst.util.UtilidadesCadena.completarCerosIzquierda;

import java.math.BigDecimal;
import java.math.MathContext;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import javax.transaction.Transactional;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpStatusCodeException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import com.altioracorp.gpintegrator.client.Bank.Bank;
import com.altioracorp.gpintegrator.client.CreditCard.CreditCard;
import com.altioracorp.gpintegrator.client.Receivable.Apply;
import com.altioracorp.gpintegrator.client.Receivable.CashReceipt;
import com.altioracorp.gpintegrator.client.Receivable.Response;
import com.altioracorp.gpintegrator.client.ReceivablePostedTransaction.Rm20101Header;
import com.altioracorp.wst.constantes.integracion.UrlIntegracionBankCreditCard;
import com.altioracorp.wst.constantes.integracion.UrlIntegracionReceivable;
import com.altioracorp.wst.dominio.cobros.Cobro;
import com.altioracorp.wst.dominio.cobros.CobroConsultaDTO;
import com.altioracorp.wst.dominio.cobros.CobroCuotaFactura;
import com.altioracorp.wst.dominio.cobros.CobroCuotaFacturaEstado;
import com.altioracorp.wst.dominio.cobros.CobroDTO;
import com.altioracorp.wst.dominio.cobros.CobroEstado;
import com.altioracorp.wst.dominio.cobros.CobroFormaPago;
import com.altioracorp.wst.dominio.cobros.CobroFormaPagoDTO;
import com.altioracorp.wst.dominio.cobros.CobroFormaPagoEstado;
import com.altioracorp.wst.dominio.cobros.CobroReporteDTO;
import com.altioracorp.wst.dominio.cobros.CobroReporteDetalleDTO;
import com.altioracorp.wst.dominio.cobros.CondicionCobroEstado;
import com.altioracorp.wst.dominio.cobros.CondicionCobroFactura;
import com.altioracorp.wst.dominio.sistema.ConfigSistema;
import com.altioracorp.wst.dominio.sistema.ConfiguracionSistema;
import com.altioracorp.wst.dominio.sistema.FormaPagoNombre;
import com.altioracorp.wst.dominio.sistema.PerfilNombre;
import com.altioracorp.wst.dominio.sistema.Secuencial;
import com.altioracorp.wst.dominio.sistema.TipoDocumento;
import com.altioracorp.wst.dominio.sistema.Usuario;
import com.altioracorp.wst.dominio.ventas.DocumentoEstado;
import com.altioracorp.wst.dominio.ventas.DocumentoGuiaDespacho;
import com.altioracorp.wst.exception.cobros.CobroCuotaFacturaYaExisteException;
import com.altioracorp.wst.exception.cobros.CobroFormaPagoYaExisteException;
import com.altioracorp.wst.exception.cobros.CobroNoCumpleCondicionesException;
import com.altioracorp.wst.exception.cobros.CobroNoExisteException;
import com.altioracorp.wst.exception.cobros.CobroNoExisteSinParametrosException;
import com.altioracorp.wst.exception.cobros.FormaPagoNoTieneAplicacionesException;
import com.altioracorp.wst.exception.cobros.FormaPagoValorTotalNoAplicadoException;
import com.altioracorp.wst.exception.cobros.ValorAplicarMayorSaldoCobroFormaPagoException;
import com.altioracorp.wst.exception.cobros.ValorAplicarMayorSaldoCuotaFacturaException;
import com.altioracorp.wst.exception.reporte.JasperReportsException;
import com.altioracorp.wst.exception.reporte.ReporteExeption;
import com.altioracorp.wst.exception.ventas.DocumentoGPException;
import com.altioracorp.wst.repositorio.cobros.ICobroFormaPagoRepositorio;
import com.altioracorp.wst.repositorio.cobros.ICobroRepositorio;
import com.altioracorp.wst.repositorio.cobros.ICondicionCobroFacturaRepositorio;
import com.altioracorp.wst.repositorio.sistema.IConfiguracionSistemaRepositorio;
import com.altioracorp.wst.repositorio.sistema.IUsuarioRepositorio;
import com.altioracorp.wst.servicio.cobros.IChequePosfechadoServicio;
import com.altioracorp.wst.servicio.cobros.ICobroServicio;
import com.altioracorp.wst.servicio.reporte.IGeneradorJasperReports;
import com.altioracorp.wst.servicio.sistema.IConstantesAmbienteWstServicio;
import com.altioracorp.wst.servicio.ventas.IDocumentoServicio;
import com.altioracorp.wst.servicio.ventas.IGuiaDespachoServicio;
import com.altioracorp.wst.servicio.ventas.INotaCreditoServicio;
import com.altioracorp.wst.util.UtilidadesCadena;
import com.altioracorp.wst.util.UtilidadesFecha;
import com.altioracorp.wst.util.UtilidadesSeguridad;

@Service
public class CobroServicioImpl implements ICobroServicio {

    private static final Log LOG = LogFactory.getLog(CobroServicioImpl.class);
    private static final String docDate = UtilidadesFecha.formatear(LocalDateTime.now(), "yyyy-MM-dd");

    @Autowired
    private IConstantesAmbienteWstServicio constantesAmbiente;

    @Autowired
    private ICobroRepositorio cobroRepositorio;

    @Autowired
    private ICobroFormaPagoRepositorio cobroFormaPagoRepositorio;

    @Autowired
    private IDocumentoServicio documentoServicio;

    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    private IGuiaDespachoServicio guiaDespachoServicio;

    @Autowired
    private IGeneradorJasperReports reporteServicio;

    @Autowired
    private IUsuarioRepositorio usuarioRepositorio;

    @Autowired
    private EntityManager entityManager;
    
    @Autowired
    private ICondicionCobroFacturaRepositorio condicionCobroFacturaRepositorio;

    @Autowired
    private IConfiguracionSistemaRepositorio configSistemaRepositorio;
    
    @Autowired
    private IChequePosfechadoServicio chequePosfechadoServicio;
    
    @Autowired
    private INotaCreditoServicio notaCreditoServicio;


    @Override
    public Cobro buscarCobroPorEstadoYCodigoClienteYUsuario(String codigoCliente) {
        return cobroRepositorio.findByCodigoClienteAndEstadoAndModificadoPor(codigoCliente, CobroEstado.NUEVO,
                UtilidadesSeguridad.usuarioEnSesion()).orElse(null);
    }

    private void generarAplicacion(CobroFormaPago cobroFormaPago, List<CobroCuotaFactura> cobroCuotaFactura) {

    	 if (cobroFormaPago.getEstado().equals(CobroFormaPagoEstado.COBRADO)) {
			 
    		 if (cobroFormaPago.getFormaPago().isIntegrarAplicacion()) {
    			 
        		 Map<String, List<CobroCuotaFactura>> cuotasAgrupadas = cobroCuotaFactura.stream()
        	         		.collect(Collectors.groupingBy(CobroCuotaFactura::getNumeroFactura, Collectors.toList()));
        	    	 
        	    	 cuotasAgrupadas.forEach((numeroFactura, cuotas) ->{
        	    		 
        	    		 Response response = integrarAplicacion(ObtenerEntidadIntegracionAplicacion(cobroFormaPago, numeroFactura,cuotas));
        	    		 
        	    		 if (response.getErrorCode() != null && response.getErrorCode().equals("0")) {
        	    			 
        	    			 cuotas.forEach(x->x.setEstado(CobroCuotaFacturaEstado.APLICADO));
        	                 LOG.info(String.format("Se aplicó con exito: %s", cobroFormaPago.getNumeroCobroGP()));
        	                    
        	                } else {
        	                	cuotas.forEach(x -> {
        	                		x.setEstado(CobroCuotaFacturaEstado.ERROR_APLICACION);
        	                		x.setMensajeError(UtilidadesCadena.truncar(response.getErrorMessage(), 255));
        	                	});       	                	

        	                    LOG.error(String.format("Error al hacer la aplicación cobro : %s", cobroFormaPago.getNumeroCobroGP()));
        	                    LOG.error(String.format("Error GPIntegrador : %s", response.getErrorMessage()));

        	                }
        	    	 });
        		 
    		 }else {
    			 cobroCuotaFactura.stream().forEach(x->x.setEstado(CobroCuotaFacturaEstado.APLICADO));
             }
        	
		 }  	

    }

    private CobroFormaPagoDTO generarCobro(CobroFormaPago cobroFormaPago, Cobro cobro) {

        CobroFormaPagoDTO cobroFormaPagoDTO = new CobroFormaPagoDTO();

        if (cobroFormaPago.getEstado().equals(CobroFormaPagoEstado.NUEVO)
                || cobroFormaPago.getEstado().equals(CobroFormaPagoEstado.ERROR_COBRO)) {

            if (cobroFormaPago.getFormaPago().isIntegrarCobro()) {
            	
            	if (UtilidadesCadena.esNuloOBlanco(cobroFormaPago.getNumeroCobroGP())) {
            		
            		 String numeroCobroGPTruncado = crearNumeroCobroGP(cobro.getNumero(), Integer.toString(cobroFormaPago.getSecuencial()));
            		 
                     cobroFormaPago.setNumeroCobroGP(numeroCobroGPTruncado);

                     cobroFormaPagoDTO.setNumero(cobroFormaPago.getNumeroCobroGP());
            	}

                Response response = integrarCobro(ObtenerEntidadIntegracionCobro(cobroFormaPago, cobro));

                if (response.getErrorCode() != null && response.getErrorCode().equals("0")) {
                    cobroFormaPago.setEstado(CobroFormaPagoEstado.COBRADO);

                    LOG.info(String.format("Se integró con exito: %s", cobroFormaPago.getNumeroCobroGP()));

                } else {

                    cobroFormaPago.setEstado(CobroFormaPagoEstado.ERROR_COBRO);
                    cobroFormaPago.setMensajeError(UtilidadesCadena.truncar(response.getErrorMessage(), 255));

                    LOG.error(String.format("Error al integrar cobro : %s", cobroFormaPago.getNumeroCobroGP()));
                    LOG.error(String.format("Error GPIntegrador : %s", response.getErrorMessage()));

                }

            } else {

                cobroFormaPagoDTO.setNumero(cobroFormaPago.getNumeroDocumento());
                cobroFormaPago.setEstado(CobroFormaPagoEstado.COBRADO);
            }

            cobroFormaPagoDTO.setEstado(cobroFormaPago.getEstado());

        }
        
        //TODO: Solo para el caso de reintegracion de cobro
        if(cobroFormaPagoDTO.getEstado() == null && cobroFormaPagoDTO.getNumero() == null) {
        	cobroFormaPagoDTO.setNumero(cobroFormaPago.getNumeroCobroGP());
        	cobroFormaPagoDTO.setEstado(cobroFormaPago.getEstado());
        }
        	
        return cobroFormaPagoDTO;

    }

    private String crearNumeroCobroGP(String cobroNumero, String secuencial) {

        String[] numeroCobro = cobroNumero.split("-");
        String regex = "^0+(?!$)";
        String numero = numeroCobro[1].replaceAll(regex, "");
        return numeroCobro[0].concat("-").concat(numero).concat("-").concat(secuencial);

    }

    private Response integrarAplicacion(Apply aplicacion) {

        try {
            return restTemplate.postForObject(prepararUrl(UrlIntegracionReceivable.INTEGRAR_APLICACION.getUrl()),
                    aplicacion, Response.class);
        } catch (HttpStatusCodeException e) {
            LOG.error(e.getMessage());
            throw new DocumentoGPException(e.getMessage());
        }
    }

    private Response integrarCobro(CashReceipt cobro) {

        try {
            return restTemplate.postForObject(prepararUrl(UrlIntegracionReceivable.INTEGRAR_COBRO.getUrl()), cobro,
                    Response.class);
        } catch (HttpStatusCodeException e) {
            LOG.error(e.getMessage());
            throw new DocumentoGPException(e.getMessage());
        }
    }

    @Override
    public List<Bank> listarBancos() {
        String url = constantesAmbiente.getUrlIntegradorGp()
                .concat(UrlIntegracionBankCreditCard.LISTAR_BANCOS.getUrl());
        Bank[] response = restTemplate.getForObject(url, Bank[].class);
        return Arrays.asList(response);
    }

    @Override
    public List<Rm20101Header> listarNotasCreditoPorCliente(String codigoCliente) {
        return documentoServicio.consultarNotasDeCreditoPorCliente(codigoCliente);
    }

	@Override
	public List<Rm20101Header> listarCobrosPorClienteGP(String codigoCliente) {
		//Falta validar que los cobros de WST tengan todas las aplicaciones integradas
		return documentoServicio.consultarCobrosPorCliente(codigoCliente);
	}

    @Override
    public List<CreditCard> listarTarjetasCredito() {
        String url = constantesAmbiente.getUrlIntegradorGp()
                .concat(UrlIntegracionBankCreditCard.LISTAR_TARJETAS_CREDITO.getUrl());
        CreditCard[] response = restTemplate.getForObject(url, CreditCard[].class);
        return Arrays.asList(response);

    }

    private Apply ObtenerEntidadIntegracionAplicacion(CobroFormaPago cobroFormaPago,String numeroFactura, List<CobroCuotaFactura> cobroCuotaFactura) {

        Apply apply = new Apply();
        apply.setAptodcnm(numeroFactura);
        BigDecimal montoTotalAplicar = cobroCuotaFactura.stream().map(x->x.getValor()).reduce(BigDecimal.ZERO, BigDecimal::add);
        apply.setApptoamt(montoTotalAplicar);

        apply.setAptodcty(1);
        apply.setApplydate(docDate);
        apply.setGlpostdt(docDate);
        apply.setDistknam(BigDecimal.ZERO);

        if (!cobroFormaPago.getFormaPago().isIntegrarCobro()) {
            apply.setApfrdcnm(cobroFormaPago.getNumeroDocumento());
            apply.setApfrdcty(cobroFormaPago.getFormaPago().getCodigo());
		}
         else {
            apply.setApfrdcnm(cobroFormaPago.getNumeroCobroGP());
            apply.setApfrdcty(9);
        }

        return apply;
    }

    private CashReceipt ObtenerEntidadIntegracionCobro(CobroFormaPago cobroFormaPago, Cobro cobro) {

        String[] partesNumeroCobro = cobro.getNumero().split("-");
        List<String> cademas = Arrays.asList("WST", partesNumeroCobro[0], Long.toString(cobroFormaPago.getId()));
        String bachnumb = UtilidadesCadena.juntar(cademas, "-");

        CashReceipt cashReceipt = new CashReceipt();
        cashReceipt.setCustnmbr(cobro.getCodigoCliente());
        cashReceipt.setDocnumbr(cobroFormaPago.getNumeroCobroGP());
        cashReceipt.setDocdate(docDate);
        cashReceipt.setOrtrxamt(cobroFormaPago.getValor());
        cashReceipt.setGlpostdt(docDate);
        cashReceipt.setBachnumb(bachnumb);
        cashReceipt.setCshrctyp(cobroFormaPago.getFormaPago().getCodigo());

        if (cobroFormaPago.getFormaPago().equals(FormaPagoNombre.TARJETA_CREDITO)) {
            cashReceipt.setCrcardid(cobroFormaPago.getNombreTarjeta());
            cashReceipt.setCheknmbr(cobroFormaPago.getNumeroDocumento());
        } else {
            cashReceipt.setChekbkid(cobroFormaPago.getChequera());

            if (cobroFormaPago.getFormaPago().equals(FormaPagoNombre.CHEQUE)) {
                cashReceipt.setCheknmbr(UtilidadesCadena.truncar(cobroFormaPago.getNumeroDocumento(), 21));
            }
        }

        return cashReceipt;
    }

    private String prepararUrl(final String urlIntegracion) {

        final String url = UriComponentsBuilder
                .fromHttpUrl(constantesAmbiente.getUrlIntegradorGp().concat(urlIntegracion)).build().toUriString();

        LOG.info("URL Información: " + url);

        return url;
    }

	@Override
	@Transactional
	public CobroDTO procesarCobro(long cobroId) {

		CobroDTO cobroDTO = new CobroDTO();

		Cobro cobro = cobroRepositorio.findById(cobroId).get();

		if (cobro != null && cobro.getEstado().equals(CobroEstado.NUEVO)) {

			if (!validarCondicionCobro(cobro))
				throw new CobroNoCumpleCondicionesException(cobro.getNumero());
			
			validarAplicacionesDeFormaPago(cobro);
			
			validarFormasPagoValorTotalAplicado(cobro);
			
			cobroDTO.setNumero(cobro.getNumero());

			int i = 1;

			for (CobroFormaPago formaPago : cobro.getCobroFormaPagos()) {
				
				formaPago.setSecuencial(i);

				integrarCobros(cobroDTO, cobro, formaPago);

				i++;

			}

			cobro.getCuotaFacturas().forEach(cuotaFactura -> {

				if (cuotaFactura.getCuotaFactura().getSaldo().compareTo(BigDecimal.ZERO) == 0)
					cuotaFactura.getCuotaFactura().setEstado(CondicionCobroEstado.COBRADO);

				
				if (puedeGenerarGuiaDespacho(cuotaFactura)) {

					DocumentoGuiaDespacho guiaDespacho = guiaDespachoServicio
							.guardarDespachoDeFactura(cuotaFactura.getCuotaFactura().getFactura());

					cuotaFactura.getCuotaFactura().getFactura().setDespachada(true);

					if (guiaDespacho != null)
						cobroDTO.getGuiaDespachoIds().add(guiaDespacho.getId());

				}
				
			});

			cambiarEstadoCobro(cobro);

			cobroRepositorio.save(cobro);

		}

		cobroDTO.setEstado(cobro.getEstado());

		chequePosfechadoServicio.registrar(cobro.getCobroFormaPagos().stream()
				.filter(x -> x.getFormaPago().equals(FormaPagoNombre.CHEQUE_A_FECHA)).collect(Collectors.toList()), cobro.getCodigoCliente(), cobro.getNombreCliente(), cobro.getFecha());
		return cobroDTO;
	}

	private void cambiarEstadoCobro(Cobro cobro) {
		if (cobro.getCuotaFacturas().stream()
				.allMatch(x -> x.getEstado().equals(CobroCuotaFacturaEstado.APLICADO))) {

			cobro.setEstado(CobroEstado.CERRADO);
			cobro.setFecha(LocalDateTime.now());

		} else {

			cobro.setEstado(CobroEstado.PENDIENTE);
			cobro.setFecha(LocalDateTime.now());

		}
	}
	
	private boolean puedeGenerarGuiaDespacho(CobroCuotaFactura cuotaFactura) {

		if (!cuotaFactura.getCuotaFactura().getFactura().getEstado().equals(DocumentoEstado.ANULADO)
				&& !cuotaFactura.getCuotaFactura().getFactura().isDespachada()
				&& validarSaldoFacturaPuedeLiberarGuiaDespacho(cuotaFactura.getCuotaFactura().getFactura().getId()))
			return true;

		if (cuotaFactura.getCuotaFactura().getFactura().getEstado().equals(DocumentoEstado.ANULADO)
				&& motivoNotaCreditoEsRefacturacion(cuotaFactura.getCobroFormaPagoId())
				&& !cuotaFactura.getCuotaFactura().getFactura().isDespachada())
			return true;

		return false;
	}

    @Override
    public Cobro registrarCobro(Cobro cobro) {
        Optional<Cobro> cobroExistente = cobroRepositorio.findByCodigoClienteAndEstadoAndModificadoPor(cobro.getCodigoCliente(),
                CobroEstado.NUEVO, UtilidadesSeguridad.usuarioEnSesion());
        if (cobroExistente.isPresent()) {

            cobro.getCobroFormaPagos().forEach(x -> {
                validarFormaPagoUnicaParaDocumento(x.getFormaPago(), x.getNumeroFactura());
                cobroExistente.get().agregarCobroFormaPago(x);
            });

            if (cobro.getCuotaFacturas() != null)
                cobroExistente.get().agregarCuotaFactura(cobro.getCuotaFacturas());

            LOG.info(String.format("Cobro a guardar: %s", cobro));
            return cobroRepositorio.save(cobroExistente.get());

        } else {
            Secuencial secuencial = documentoServicio.secuencialDocumento(cobro.getPuntoVenta(), TipoDocumento.COBRO);
            cobro.setActivo(Boolean.TRUE);
            cobro.setNumero(secuencial.getNumeroSecuencial());
            cobro.setFecha(LocalDateTime.now());
            cobro.setValor(cobro.getCobroFormaPagos().stream().map(x -> x.getValor()).reduce(BigDecimal.ZERO,
                    BigDecimal::add));
            LOG.info(String.format("Cobro a guardar: %s", cobro));
            return cobroRepositorio.save(cobro);
        }
    }

    private boolean validarCondicionCobro(Cobro cobro) {

    	BigDecimal valorCobroFormaPago = cobro.getCobroFormaPagos().stream().map(x -> x.getValor().subtract(x.getSaldo()))
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    	
    	BigDecimal valorCondicionesCobro = cobro.getCuotaFacturas().stream().map(x -> x.getValor())
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        if (valorCondicionesCobro.compareTo(valorCobroFormaPago) == 0)
            return true;

        return false;
    }

    private void validarFormaPagoUnicaParaDocumento(FormaPagoNombre formaPago, String numeroFactura) {
        if (formaPago.equals(FormaPagoNombre.EFECTIVO)) {

            Optional<CobroFormaPago> formaPagoRecargado = cobroFormaPagoRepositorio
                    .findByFormaPagoAndNumeroFacturaAndEstado(formaPago, numeroFactura, CobroFormaPagoEstado.NUEVO);

            if (formaPagoRecargado.isPresent()) {
                throw new CobroFormaPagoYaExisteException(formaPago.getDescripcion(), numeroFactura);
            }
        }
    }

	@Override
	public byte[] generarReportePorNumero(String numero) {

		Optional<Cobro> cobroRecargado = cobroRepositorio.findByNumero(numero);

		if (cobroRecargado.isPresent()) {

			Usuario usuario = usuarioRepositorio.findByNombreUsuario(cobroRecargado.get().getModificadoPor()).get();

			String direccionCliente = "";

			Optional<CobroCuotaFactura> cuota = cobroRecargado.get().getCuotaFacturas().stream().findFirst();
			if (cuota.isPresent()) {
				direccionCliente = cuota.get().getCuotaFactura().getFactura().getCotizacion().getDescripcionDireccion();
			}

			CobroReporteDTO dto = new CobroReporteDTO(usuario.getNombreCompleto(), usuario.getNombreUsuario(),
					cobroRecargado.get().getPuntoVenta().getNombre(), cobroRecargado.get().getFecha(),
					cobroRecargado.get().getNumero(), cobroRecargado.get().getEstado().toString(),
					crearDetalleReporteCobro(cobroRecargado.get()), cobroRecargado.get().getCodigoCliente(),
					cobroRecargado.get().getNombreCliente(), direccionCliente, cobroRecargado.get().getValor());

			try {

				return reporteServicio.generarReporte("Cobro", Collections.singleton(dto), new HashMap<>());

			} catch (JasperReportsException e) {
				LOG.error(String.format("Error Cobro Reporte: %s", e.getMessage()));
				throw new ReporteExeption("Cobro");
			}
		} else {
			throw new CobroNoExisteException(numero);
		}
	}
    
	private List<CobroReporteDetalleDTO> crearDetalleReporteCobro(Cobro cobro) {
		List<CobroReporteDetalleDTO> detalle = new ArrayList<>();
		Map<String, List<CobroCuotaFactura>> detalleAgrupado = cobro.getCuotaFacturas().stream()
				.collect(Collectors.groupingBy(CobroCuotaFactura::getNumeroFactura, Collectors.toList()));

		detalleAgrupado.forEach((numeroFactura, cuotas) -> {

			cuotas.forEach(y -> {
				Optional<CobroFormaPago> cobroFormaPago = cobro.getCobroFormaPagos().stream()
						.filter(x -> x.getId() == y.getCobroFormaPagoId()).findFirst();
				
				if(cobroFormaPago.isPresent()) {
					detalle.add(new CobroReporteDetalleDTO(cobroFormaPago.get().getNumeroCobroGP(),
							cobroFormaPago.get().getFormaPago().toString(), cobroFormaPago.get().getBanco(),
							cobroFormaPago.get().getNumeroDocumento(), cobroFormaPago.get().getNombreTarjeta(),
							cobroFormaPago.get().getChequera(), numeroFactura, cobroFormaPago.get().getEstado().toString(),
							y.getValor(), Boolean.TRUE));
				}				
			});
		});

		cobro.getCobroFormaPagos().forEach(x -> {
			if (!cobro.getCuotaFacturas().stream().anyMatch(y -> y.getCobroFormaPagoId() == x.getId())) {
				detalle.add(new CobroReporteDetalleDTO(x.getNumeroCobroGP(), x.getFormaPago().toString(), x.getBanco(),
						x.getNumeroDocumento(), x.getNombreTarjeta(), x.getChequera(), "",
						x.getEstado().toString(), x.getValor(), Boolean.FALSE));
			}
		});

		return detalle;
	}

    @Override
    @Transactional
    public boolean eliminarCobroFormaPago(Long cobroFormaPagoID, Long cobroID) {
        LOG.info(String.format("Eliminado Forma de Pago %s de cobro %s", cobroFormaPagoID, cobroID));

        actualizarValorCobro(cobroFormaPagoID, cobroID);

        eliminarCobroCabecera(cobroID);

        return true;
    }

    private void actualizarValorCobro(Long cobroFormaPagoID, Long cobroID) {
        CobroFormaPago formaPago = cobroFormaPagoRepositorio.findById(cobroFormaPagoID).get();

        Cobro cobro = cobroRepositorio.findById(cobroID).get();

        BigDecimal valor = cobro.getValor().subtract(formaPago.getValor());

        LOG.info(String.format("Actualizando Valor %s de Cobro %s", valor, cobro.getNumero()));

        cobro.setValor(valor);

        eliminarCobroCuotaFactura(formaPago, cobro);

        cobro.getCobroFormaPagos().remove(formaPago);

        cobroRepositorio.save(cobro);
    }

    private void eliminarCobroCuotaFactura(CobroFormaPago formaPago, Cobro cobro) {
        
        List<CobroCuotaFactura> objs = cobro.getCuotaFacturas().stream()
                .filter(x -> x.getCobroFormaPagoId() == formaPago.getId())
                .collect(Collectors.toList());
        
        actualizarSaldoCuotaPorAplicacionEliminada(objs);
        LOG.info(String.format("Eliminando Cobro Cuotas Factura %s de Cobro %s", objs.size(), cobro.getNumero()));

        cobro.getCuotaFacturas().removeAll(objs);
    }

    private void eliminarCobroCabecera(Long cobroID) {
        Optional<Cobro> cobroRecargado = cobroRepositorio.findById(cobroID);

        if (cobroRecargado.isPresent()) {

            LOG.info(String.format("Cobro %s tiene %s Forma(s) Pago registrado. ", cobroRecargado.get().getNumero(), cobroRecargado.get().getCobroFormaPagos().size()));

            if (cobroRecargado.get().getCobroFormaPagos().isEmpty()) {

                LOG.info(String.format("Eliminado Cobro %s ", cobroRecargado.get().getNumero()));

                cobroRepositorio.delete(cobroRecargado.get());
            }
        }
    }

    @Override
    public Page<Cobro> consultarCobros(final Pageable pageable, final CobroConsultaDTO consulta) {
        try {
            final CriteriaBuilder criteriaBuilder = this.entityManager.getCriteriaBuilder();
            final CriteriaQuery<Cobro> query = criteriaBuilder.createQuery(Cobro.class);

            final Root<Cobro> cobroRoot = query.from(Cobro.class);
            final List<Predicate> predicadosConsulta = new ArrayList<Predicate>();

            if (consulta.getRol().getDescripcion().equals(PerfilNombre.CAJERO.getDescripcion())) {
                predicadosConsulta.add(criteriaBuilder.equal(cobroRoot.get("creadoPor"), UtilidadesSeguridad.usuarioEnSesion()));
            }
//            else {
//                if (!StringUtils.isBlank(consulta.getUsuario())) {
//                    predicadosConsulta.add(criteriaBuilder.equal(cobroRoot.get("creadoPor"), consulta.getUsuario()));
//                }
//            }

            if (!StringUtils.isBlank(consulta.getIdentificacionCliente())) {
                predicadosConsulta.add(criteriaBuilder.equal(cobroRoot.get("codigoCliente"), consulta.getIdentificacionCliente()));
            }

            if (!StringUtils.isBlank(consulta.getNombreCliente())) {
                predicadosConsulta.add(criteriaBuilder.equal(cobroRoot.get("nombreCliente"), consulta.getNombreCliente()));
            }

            if (consulta.getIdPuntoVenta() != 0) {
                predicadosConsulta.add(criteriaBuilder.equal(cobroRoot.get("puntoVenta").get("id"), consulta.getIdPuntoVenta()));
            }

            if (consulta.getEstado() != null) {
                predicadosConsulta.add(criteriaBuilder.equal(cobroRoot.get("estado"), consulta.getEstado()));
            }

            if (!StringUtils.isBlank(consulta.getNumeroCobro())) {
                predicadosConsulta.add(criteriaBuilder.equal(cobroRoot.get("numero"), consulta.getNumeroCobro()));
            }

            if (consulta.getFechaInicio() != null && consulta.getFechaFin() != null) {
                predicadosConsulta.add(criteriaBuilder.between(cobroRoot.get("fecha"),
                        consulta.getFechaInicio().withHour(0).withMinute(0).withSecond(0),
                        consulta.getFechaFin().withHour(23).withMinute(59).withSecond(59)));
            }

            if (consulta.getFechaInicio() != null && consulta.getFechaFin() == null) {
                predicadosConsulta.add(criteriaBuilder.between(cobroRoot.get("fecha"),
                        consulta.getFechaInicio().withHour(0).withMinute(0).withSecond(0),
                        consulta.getFechaInicio().withHour(23).withMinute(59).withSecond(59)));
            }

            query.where(predicadosConsulta.toArray(new Predicate[predicadosConsulta.size()]))
                    .orderBy(criteriaBuilder.desc(cobroRoot.get("fecha")));

            final TypedQuery<Cobro> statement = this.entityManager.createQuery(query);

            List<Cobro> cobrosResult = statement.getResultList();

            List<Cobro> cobrosTemp = null;

            if (!StringUtils.isBlank(consulta.getUsuario())) {
                cobrosTemp = cobrosResult.stream().filter(c -> (
                        (!c.getCuotaFacturas().isEmpty() && consulta.getUsuario().equalsIgnoreCase(c.getCuotaFacturas()
                                .get(0).getCuotaFactura().getFactura().getCotizacion().getCreadoPor()))
                )).collect(Collectors.toList());
            }

            if (cobrosTemp != null) {
                cobrosResult.clear();
                cobrosResult.addAll(cobrosTemp);
            }

            cobrosResult.forEach(c -> {
                try {
                    if (c.getCuotaFacturas() != null && !c.getCuotaFacturas().isEmpty()) {
                        String nombreCliente = c.getCuotaFacturas().get(0).getCuotaFactura()
                                .getFactura().getCotizacion().getNombreCliente();
                        c.setNombreCliente(nombreCliente);
                        Optional<Usuario> usuario = usuarioRepositorio.findByNombreUsuario(c.getCuotaFacturas().get(0).getCuotaFactura().getFactura().getCotizacion().getCreadoPor());
                        c.setNombreVendedor(usuario.isPresent() ? usuario.get().getNombreCompleto() : null);
                    }
                } catch (Exception ex) {
                    LOG.error(ex.getMessage());
                }
            });

            final int sizeTotal = cobrosResult.size();

            final int start = (int) pageable.getOffset();
            final int end = (start + pageable.getPageSize()) > cobrosResult.size() ? cobrosResult.size() : (start + pageable.getPageSize());

            cobrosResult = cobrosResult.subList(start, end);

            final Page<Cobro> pageResut = new PageImpl<Cobro>(cobrosResult, pageable, sizeTotal);

            return pageResut;

        } catch (final Exception ex) {
            final Page<Cobro> pageResult = new PageImpl<Cobro>(new ArrayList<Cobro>(), pageable, 0);
            return pageResult;
        }
    }

	@Override
	public boolean exsiteNumeroChequeEnOtrosCobros(String codigoCliente, String numeroCheque) {

		boolean respuesta = Boolean.FALSE;

		List<Object[]> cheques = cobroFormaPagoRepositorio.existeChuequeEnOtrosCobrosPorCliente(numeroCheque,
				codigoCliente);

		if (cheques.isEmpty()) {
			respuesta = Boolean.FALSE;
		} else {
			respuesta = Boolean.TRUE;
		}

		LOG.info(String.format("Cobros: Validando CHEQUE %s del cliente %s, existe en otros cobros %s ", numeroCheque,
				codigoCliente, respuesta ? "SI" : "NO"));
		return respuesta;

	}
	
	private boolean motivoNotaCreditoEsRefacturacion(long cobroFormaPagoId) {
		Optional<CobroFormaPago> cobroFormaPago = cobroFormaPagoRepositorio.findById(cobroFormaPagoId);
		if(cobroFormaPago.isPresent()) {
			if(cobroFormaPago.get().getFormaPago().equals(FormaPagoNombre.NOTA_CREDITO)) {
				return notaCreditoServicio.esGeneradaPorMotivoRefacturacion(cobroFormaPago.get().getNumeroDocumento());					
			}			
			return false;
		}
		return false;
	}
	

	@Override
	public Cobro registrarCobroGeneral(Cobro cobro) {
		Optional<Cobro> cobroExistente = cobroRepositorio.findByCodigoClienteAndEstadoAndModificadoPor(cobro.getCodigoCliente(),
                CobroEstado.NUEVO, UtilidadesSeguridad.usuarioEnSesion());
        if (cobroExistente.isPresent()) {

            cobro.getCobroFormaPagos().forEach(x -> {
                //validarFormaPagoUnicaParaDocumento(x.getFormaPago(), x.getNumeroFactura());
                cobroExistente.get().agregarCobroFormaPago(x);
            });

            LOG.info(String.format("Cobro a guardar: %s", cobro));
            return cobroRepositorio.save(cobroExistente.get());

        } else {
            Secuencial secuencial = documentoServicio.secuencialDocumento(cobro.getPuntoVenta(), TipoDocumento.COBRO);
            cobro.setActivo(Boolean.TRUE);
            cobro.setNumero(secuencial.getNumeroSecuencial());
            cobro.setFecha(LocalDateTime.now());
            cobro.setValor(cobro.getCobroFormaPagos().stream().map(x -> x.getValor()).reduce(BigDecimal.ZERO,
                    BigDecimal::add));
            LOG.info(String.format("Cobro a guardar: %s", cobro));
            return cobroRepositorio.save(cobro);
        }
	}

	@Override
	public Cobro listarCobroPorId(long id) {
		Optional<Cobro> cobro = cobroRepositorio.findById(id);
		return cobro.isEmpty() ? null : cobro.get();
	}

	@Override
	@Transactional
	public Cobro registrarCobroCuotaFactura(long cobroId, List<CobroCuotaFactura> cuotas) {
		Optional<Cobro> cobro = cobroRepositorio.findById(cobroId);
		
		if (cobro.isPresent()) {
			
			validarCobroCuotaFacturaDuplicada(cobro.get(), cuotas);
			
			actualizarSaldoFormaPago(cobro.get(), cuotas);
			
			actualizarSaldoCuotas(cuotas);
			
			cobro.get().agregarCuotaFactura(cuotas);
			
			return cobroRepositorio.save(cobro.get());
		}		
		throw new CobroNoExisteSinParametrosException();
	}
	
	private void actualizarSaldoFormaPago(Cobro cobro, List<CobroCuotaFactura> cuotas) {
		Optional<CobroFormaPago> formaPago = cobro.getCobroFormaPagos().stream()
				.filter(x -> x.getId() == cuotas.stream().findFirst().get().getCobroFormaPagoId()).findFirst();

		if (formaPago.isPresent()) {
			BigDecimal valorAplicar = cuotas.stream().map(x -> x.getValor()).reduce(BigDecimal.ZERO, BigDecimal::add);
			BigDecimal saldo = formaPago.get().getSaldo().subtract(valorAplicar);

			if (saldo.compareTo(BigDecimal.ZERO) == -1) {
				throw new ValorAplicarMayorSaldoCobroFormaPagoException(valorAplicar, formaPago.get().getSaldo(),
						formaPago.get().getFormaPago());
			}

			LOG.info(String.format("Cobro Aplicacion : Actualizando Forma Pago %s saldo = %s",
					formaPago.get().getFormaPago().toString(), saldo));
			formaPago.get().setSaldo(saldo);
		}
	}
	
	private void actualizarSaldoCuotas(List<CobroCuotaFactura> cuotas) {
		cuotas.forEach(x -> {

			Optional<CondicionCobroFactura> condicion = condicionCobroFacturaRepositorio
					.findById(x.getCuotaFactura().getId());

			if (condicion.isPresent()) {
				if ((x.getValor().compareTo(condicion.get().getSaldo()) == 0)
						|| (x.getValor().compareTo(condicion.get().getSaldo()) == -1)) {

					BigDecimal saldo = condicion.get().getSaldo().subtract(x.getValor());
					LOG.info(String.format("Cobro Aplicacion : Actualizando saldo = %s de la cuota %s en la factura %s",
							saldo, condicion.get().getNumeroCuota(), x.getNumeroFactura()));
					condicion.get().setSaldo(saldo);					

				} else {
					throw new ValorAplicarMayorSaldoCuotaFacturaException(x.getValor(), condicion.get().getSaldo(),
							x.getNumeroFactura(), condicion.get().getNumeroCuota());
				}
			}
		});
	}
	
	private void actualizarSaldoCuotaPorAplicacionEliminada(List<CobroCuotaFactura> cuotas) {
		cuotas.forEach(x -> {

			Optional<CondicionCobroFactura> condicion = condicionCobroFacturaRepositorio
					.findById(x.getCuotaFactura().getId());

			if (condicion.isPresent()) {

				BigDecimal saldo = condicion.get().getSaldo().add(x.getValor());
				LOG.info(String.format("Aplicacion Eliminada: Actualizando saldo = %s de la cuota %s en la factura %s",
						saldo, condicion.get().getNumeroCuota(), x.getNumeroFactura()));
				condicion.get().setSaldo(saldo);
				condicionCobroFacturaRepositorio.save(condicion.get());

			}
		});
	}
	
	private void validarCobroCuotaFacturaDuplicada(Cobro cobro, List<CobroCuotaFactura> cuotasARegistrar) {
		List<CobroCuotaFactura> duplicadas = new ArrayList<>();

		cuotasARegistrar.forEach(x -> {
			cobro.getCuotaFacturas().forEach(y -> {
				if ((x.getCuotaFactura().getId() == y.getCuotaFactura().getId())
						&& (x.getCobroFormaPagoId() == y.getCobroFormaPagoId())) {
					duplicadas.add(y);
				}
			});
		});

		if (!duplicadas.isEmpty()) {
			throw new CobroCuotaFacturaYaExisteException(duplicadas);
		}
	}
	
	private boolean validarSaldoFacturaPuedeLiberarGuiaDespacho(long facturaId) {

		Optional<ConfiguracionSistema> configuracion = configSistemaRepositorio
				.findByNombre(ConfigSistema.MAXIMO_PORCENTAJE_RETENCION);
		if (configuracion.isPresent()) {

			List<CondicionCobroFactura> cuotasFactura = condicionCobroFacturaRepositorio
					.findByFactura_Id(facturaId);
			BigDecimal saldoPendienteFactura = cuotasFactura.stream().map(x -> x.getSaldo()).reduce(BigDecimal.ZERO,
					BigDecimal::add);
			BigDecimal valorFactura = cuotasFactura.stream().map(x -> x.getValor()).reduce(BigDecimal.ZERO,
					BigDecimal::add);
			BigDecimal porcentajeDeuda = (saldoPendienteFactura.divide(valorFactura, MathContext.DECIMAL32)).multiply(new BigDecimal(100))
					.setScale(2, RoundingMode.HALF_UP);

			if (porcentajeDeuda.doubleValue() <= configuracion.get().getValor()) {
				LOG.info(String.format(
						"Para la FacturaId: %s su porcentaje de deuda: %s | Config Max Retencion:%s => Liberear Guia Despacho? SI",
						facturaId, porcentajeDeuda, configuracion.get().getValor()));
				return Boolean.TRUE;
			} else {
				LOG.info(String.format(
						"Para la FacturaId: %s su porcentaje de deuda: %s | Config Max Retencion:%s => Liberear Guia Despacho? NO",
						facturaId, porcentajeDeuda, configuracion.get().getValor()));
				return Boolean.FALSE;
			}
		}

		LOG.error(String.format("No se encontro configuracion %s para validarSaldoFacturaPuedeLiberarGuiaDespacho",
				ConfigSistema.MAXIMO_PORCENTAJE_RETENCION.toString()));
		return Boolean.FALSE;

	}

	@Override
	public boolean validarAnticipoTieneAplicacionesConEstadoError(String numeroAnticipo) {
		String[] numeroSeparado = numeroAnticipo.split("-");
		String numeroCobro = numeroSeparado[0].concat("-").concat(completarCerosIzquierda(numeroSeparado[1], 10));

		Optional<Cobro> cobro = cobroRepositorio.findByNumero(numeroCobro);
		LOG.info(String.format("Anticipo %s: Buscando cobro %s ", numeroAnticipo, numeroCobro));
		
		if (cobro.isPresent()) {

			LOG.info(String.format("Anticipo %s: validando %s aplicaciones en estado aplicado del cobro %s ",
					numeroAnticipo, cobro.get().getCuotaFacturas().size(), numeroCobro));
			
			return !cobro.get().getCuotaFacturas().stream()
					.allMatch(x -> x.getEstado().equals(CobroCuotaFacturaEstado.APLICADO));
		}
		
		LOG.info(String.format("Anticipo %s: Cobro %s no existe en WST", numeroAnticipo, numeroCobro));
		return false;
	}
	
	private void validarAplicacionesDeFormaPago(Cobro cobro) {
		cobro.getCobroFormaPagos().forEach(x -> {
			if (!x.getFormaPago().isIntegrarCobro() && x.getFormaPago().isIntegrarAplicacion()) {

				if (!cobro.getCuotaFacturas().stream().anyMatch(a -> a.getCobroFormaPagoId() == x.getId())) {
					throw new FormaPagoNoTieneAplicacionesException(x.getFormaPagoCadena(), x.getNumeroDocumento());
				}
			}

		});
	}
	
	private void validarFormasPagoValorTotalAplicado(Cobro cobro) {
		cobro.getCobroFormaPagos().stream().filter(x -> (x.getFormaPago().equals(FormaPagoNombre.ANTICIPO)
				|| x.getFormaPago().equals(FormaPagoNombre.NOTA_CREDITO))).forEach(y -> {
					
					if (y.getSaldo().compareTo(BigDecimal.ZERO) != 0) {
						throw new FormaPagoValorTotalNoAplicadoException(y.getFormaPagoCadena(), y.getNumeroDocumento(),
								y.getSaldo());
					}

				});

	}
	
	
	@Override
	@Transactional
	public CobroDTO efectivizarChequePosFechado (long cobroFormaPagoId) {
		
		CobroDTO cobroDTO = new CobroDTO();
		
		Cobro cobro = cobroRepositorio.findBycobroFormaPagos_Id(cobroFormaPagoId);
		
		if (cobro != null) {
			
			cobroDTO.setNumero(cobro.getNumero());
			
			CobroFormaPago cobroFormaPago = cobro.getCobroFormaPagos().stream().filter(x -> x.getId() == cobroFormaPagoId).findFirst().get();
			
			if (cobroFormaPago.getFormaPago().equals(FormaPagoNombre.CHEQUE_A_FECHA)) {
				
				cobroFormaPago.setFechaEfectivizacion(LocalDateTime.now());
				
				cobroFormaPago.setFormaPago(FormaPagoNombre.CHEQUE);
				
				cobroFormaPago.setEstado(CobroFormaPagoEstado.NUEVO);
				
				CobroFormaPagoDTO formaPagoDTO = generarCobro(cobroFormaPago, cobro);

				cobroDTO.agregarFormaPago(formaPagoDTO);
				
				List<CobroCuotaFactura> cobroCuotasFacturas = cobro.getCuotaFacturas().stream().filter(x -> x.getCobroFormaPagoId() ==  cobroFormaPagoId).collect(Collectors.toList());
				
				generarAplicacion(cobroFormaPago, cobroCuotasFacturas);
				
				cobroRepositorio.save(cobro);
				
			}
		}
		
		return cobroDTO;
	}
	
	@Override
	@Transactional
    public void canjearChequePosFechado (long cobroFormaPagoId) {
		
		Cobro cobro = cobroRepositorio.findBycobroFormaPagos_Id(cobroFormaPagoId);
		
		if (cobro != null) {			
			
			CobroFormaPago cobroFormaPago = cobro.getCobroFormaPagos().stream().filter(x -> x.getId() == cobroFormaPagoId).findFirst().get();
			
			if (cobroFormaPago.getFormaPago().equals(FormaPagoNombre.CHEQUE_A_FECHA)) {
				
				List<CobroCuotaFactura> cobroCuotasFacturas = cobro.getCuotaFacturas().stream().filter(x -> x.getCobroFormaPagoId() ==  cobroFormaPagoId).collect(Collectors.toList());
				
				for (CobroCuotaFactura cuotaFactura : cobroCuotasFacturas) {
					
					CondicionCobroFactura condicionCobroFactura = cuotaFactura.getCuotaFactura();
					
					condicionCobroFactura.setEstado(CondicionCobroEstado.VENCIDO);
					
					condicionCobroFactura.setSaldo(condicionCobroFactura.getSaldo().add(cuotaFactura.getValor()));
					
					condicionCobroFacturaRepositorio.save(condicionCobroFactura);
					
					 LOG.info(String.format("Se actualiza la cuota  #%s de la factura #%s por canje cheque posfechado", condicionCobroFactura.getNumeroCuota(),  cuotaFactura.getNumeroFactura()));
				}
				
			}
		}
	}

	@Override
	public List<Cobro> listarCobrosEstadoPendiente() {
		return cobroRepositorio.findByEstadoIn(Arrays.asList(CobroEstado.PENDIENTE));
	}
	
	@Override
	@Transactional
	public CobroDTO reintegrarCobro(long cobroId) {
		CobroDTO cobroDTO = new CobroDTO();

		Cobro cobro = cobroRepositorio.findById(cobroId).get();

		if (cobro != null && cobro.getEstado().equals(CobroEstado.PENDIENTE)) {
			
			cobroDTO.setNumero(cobro.getNumero());
			
			for (CobroFormaPago formaPago : cobro.getCobroFormaPagos()) {
				
				
				integrarCobros(cobroDTO, cobro, formaPago);

			}
			
			cambiarEstadoCobro(cobro);

			cobroRepositorio.save(cobro);
		
		}
		cobroDTO.setEstado(cobro.getEstado());
		return cobroDTO;
	}

	private void integrarCobros(CobroDTO cobroDTO, Cobro cobro, CobroFormaPago formaPago) {
		CobroFormaPagoDTO formaPagoDTO = generarCobro(formaPago, cobro);

		cobroDTO.agregarFormaPago(formaPagoDTO);
		
		List<CobroCuotaFactura> cobroCuotasFacturas = cobro.getCuotaFacturas().stream().filter(x -> x.getCobroFormaPagoId() ==  formaPago.getId()).collect(Collectors.toList());
		
		generarAplicacion(formaPago, cobroCuotasFacturas);
	}
	
}
