package com.altioracorp.wst.servicioImpl.ventas;

import static com.altioracorp.wst.util.UtilidadesCadena.*;
import static com.altioracorp.wst.util.validacionNumeroIdentificacion.ValidacionNumeroIdentificacion.validar;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpStatusCodeException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import com.altioracorp.gpintegrator.client.Country.Country;
import com.altioracorp.gpintegrator.client.Customer.Customer;
import com.altioracorp.gpintegrator.client.Customer.CustomerRequest;
import com.altioracorp.gpintegrator.client.Sales.SopResponse;
import com.altioracorp.gpintegrator.client.miscellaneous.AltMissInfoAdd;
import com.altioracorp.wst.constantes.integracion.UrlIntegracionCountry;
import com.altioracorp.wst.constantes.integracion.UrlIntegracionCustomer;
import com.altioracorp.wst.dominio.cobros.CondicionCobroEstado;
import com.altioracorp.wst.dominio.cobros.CondicionCobroFactura;
import com.altioracorp.wst.dominio.ventas.Cotizacion;
import com.altioracorp.wst.dominio.ventas.CotizacionEstado;
import com.altioracorp.wst.dominio.ventas.CriterioClienteDTO;
import com.altioracorp.wst.dominio.ventas.DocumentoEstado;
import com.altioracorp.wst.dominio.ventas.DocumentoFactura;
import com.altioracorp.wst.dominio.ventas.DocumentoReserva;
import com.altioracorp.wst.dominio.ventas.TipoIdentificacion;
import com.altioracorp.wst.exception.cliente.ClienteGPException;
import com.altioracorp.wst.exception.cliente.ClienteNumeroIdentificacionInvalidoException;
import com.altioracorp.wst.exception.cliente.ClienteYaExisteException;
import com.altioracorp.wst.repositorio.cobros.ICondicionCobroFacturaRepositorio;
import com.altioracorp.wst.repositorio.ventas.ICotizacionRepositorio;
import com.altioracorp.wst.repositorio.ventas.IDocumentoFacturaRepositorio;
import com.altioracorp.wst.repositorio.ventas.IDocumentoReservaRepositorio;
import com.altioracorp.wst.servicio.sistema.IConstantesAmbienteWstServicio;
import com.altioracorp.wst.servicio.ventas.IClienteServicio;
import com.altioracorp.wst.util.UtilidadesCadena;
import com.google.gson.Gson;

@Service
public class ClienteServicioImpl implements IClienteServicio {

	private static final Log LOG = LogFactory.getLog(ClienteServicioImpl.class);
	
	@Autowired
	private RestTemplate restTemplate;
	
	@Autowired
	private IConstantesAmbienteWstServicio constantesAmbiente;
	
	@Autowired
	private IDocumentoReservaRepositorio reservaRepositorio;
	
	@Autowired
	private IDocumentoFacturaRepositorio factruaRepositorio;
	
	@Autowired
	private ICotizacionRepositorio cotizacionRepositorio;
	
	@Autowired
	private ICondicionCobroFacturaRepositorio condicionCobroFacturaRepositorio;

	@Autowired
	private Gson gsonLog;
	
	@Override
	public Customer obtenerCustomerPorCurstomerNumbre(String customerNumber) {
		String url = constantesAmbiente.getUrlIntegradorGp().concat(UrlIntegracionCustomer.OBTENER_POR_CUSTOMER_NUMBER.getUrl());	
		
		UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(url)
		        .queryParam("customerNumber", customerNumber)
		     ;

		LOG.info("Peticion: " + builder.toUriString());
		try {
			Customer response = restTemplate
					.getForObject(builder.toUriString(), Customer.class);
			return response;
			
		}catch (HttpStatusCodeException e) {
			LOG.error(e.getMessage());
			throw new ClienteGPException(e.getMessage());
		}
		
	}


	@Override
	public BigDecimal calcularCreditoDisponible(String customerNumber) {
		LOG.info(String.format("Calculando Crédito disponoble de cliente: %s", customerNumber));
		
		Customer cliente = obtenerCustomerPorCurstomerNumbre(customerNumber);
		
		if(cliente != null) {
			
			BigDecimal totalReservas = calcularTotalReservasWST(cliente.getCustnmbr());
			
			BigDecimal totalPorFacturar = calcularTotalDocumentosPorFacturarWST(cliente.getCustnmbr());
			
			BigDecimal totalPendienteCobro = calcularTotalDocumentosFacturadosWST(cliente.getCustnmbr());
			
			return cliente.getCrlmtamt().subtract(totalReservas.add(totalPorFacturar).add(totalPendienteCobro));
		}
		
		return BigDecimal.ZERO;
	}
	
	@Override
	public BigDecimal calcularCreditoDisponibleWST( String customerNumber, BigDecimal crlmtamt ) {
		LOG.info(String.format("Calculando Crédito disponoble WST de cliente: %s", customerNumber));
		
		if(customerNumber != null && crlmtamt != null) {
			BigDecimal totalReservas = calcularTotalReservasWST(customerNumber);
			
			BigDecimal totalPorFacturar = calcularTotalDocumentosPorFacturarWST(customerNumber);
			
			BigDecimal totalPendienteCobro = calcularTotalDocumentosFacturadosWST(customerNumber);
			
			return crlmtamt.subtract(totalReservas.add(totalPorFacturar.add(totalPendienteCobro)));
		}
		return BigDecimal.ZERO;
	}
	
	private BigDecimal calcularTotalReservasWST(String codigoCliente) {
		
		BigDecimal total = BigDecimal.ZERO;
		
		List<DocumentoReserva> reservas = reservaRepositorio
				.findByEstadoInAndCotizacion_CodigoCliente(Arrays.asList(DocumentoEstado.EMITIDO,
						DocumentoEstado.APROBADO, DocumentoEstado.REVISION, DocumentoEstado.COMPLETADO), codigoCliente);
		
		reservas.forEach(x -> {
			
			BigDecimal abono = (x.getCotizacion().getTotal()).multiply(BigDecimal.valueOf(x.getPorcentajeAbono()));
			
			total.add((x.getCotizacion().getTotal()).subtract(abono));
		});
		
		LOG.info(String.format("Valor Reservas WST: %s de cliente: %s", total, codigoCliente));
		
		return total;
	}
	
	private BigDecimal calcularTotalDocumentosPorFacturarWST(String codigoCliente) {
		BigDecimal total = BigDecimal.ZERO;
		
		List<Cotizacion> cotizaciones = cotizacionRepositorio.findByCodigoClienteAndEstadoInAndActivoTrue(codigoCliente,Arrays.asList(CotizacionEstado.POR_FACTURAR));
		
		List<DocumentoFactura> facturasEmitidas = factruaRepositorio.findByCotizacionInAndEstadoIn(cotizaciones, Arrays.asList(DocumentoEstado.EMITIDO));
		
		BigDecimal totalFE = facturasEmitidas.stream().filter(x->x.getTotal()!= null).map(x -> x.getTotal()).reduce(BigDecimal::add).orElse(BigDecimal.ZERO);
		
		BigDecimal totalC = cotizaciones.stream().map(x -> x.getTotal()).reduce(BigDecimal::add).orElse(BigDecimal.ZERO);
		
		total = totalC.subtract(totalFE);
		
		LOG.info(String.format("Valor Documentos Por Facturar WST: %s de cliente: %s", total, codigoCliente));
		
		return total;
	}
	
	private BigDecimal calcularTotalDocumentosFacturadosWST(String codigoCliente) {
		BigDecimal total = BigDecimal.ZERO;
		
		List<CondicionCobroFactura> cobrosPendientes = condicionCobroFacturaRepositorio.findByEstadoInAndFacturaCodigoClienteOrderByFechaAsc(Arrays.asList(CondicionCobroEstado.NUEVO, CondicionCobroEstado.VENCIDO), codigoCliente);
		
		total = cobrosPendientes.stream().map(x -> x.getValor()).reduce(BigDecimal::add).orElse(BigDecimal.ZERO);
		
		LOG.info(String.format("Valor Documentos Pendientes de Cobro WST: %s de cliente: %s", total, codigoCliente));
		
		return total;
	}


	@Override
	public List<Customer> obtenerCustomersPorCriterio(CriterioClienteDTO dto) {
		String url = constantesAmbiente.getUrlIntegradorGp()
				.concat(UrlIntegracionCustomer.OBTENER_POR_CRITERIO_TODOS.getUrl());

		UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(url).queryParam("criteria",
				dto.getCriterio());

		LOG.info("Peticion: " + builder.toUriString());
		
		Customer[] response = restTemplate.getForObject(builder.toUriString(), Customer[].class);
		
		return response == null? new ArrayList<>() : Arrays.asList(response);
	}
	
	@Override
	public List<Customer> obtenerCustomersActivosPorCriterio(CriterioClienteDTO dto) {
		String url = constantesAmbiente.getUrlIntegradorGp()
				.concat(UrlIntegracionCustomer.OBTENER_POR_CRITERIO_ACTIVOS.getUrl());

		UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(url).queryParam("criteria",
				dto.getCriterio());

		LOG.info("Peticion: " + builder.toUriString());
		
		Customer[] response = restTemplate.getForObject(builder.toUriString(), Customer[].class);
		
		return response == null? new ArrayList<>() : Arrays.asList(response);
	}


	@Override
	public boolean registrarCliente(Customer cliente) {
		
		String numeroIdentificacionNormalizada = normalizarNumeroIdentificacion(cliente.getCustnmbr());
		cliente.setCustnmbr(numeroIdentificacionNormalizada);
		String nombreNormalizado = normalizarNumeroIdentificacion(cliente.getCustname());
		cliente.setCustname(nombreNormalizado);
		
		validarNumeroIdentificacion(cliente.getCustnmbr(), cliente.getiIAT_GL00020().getDocType());
		
		cliente.setUpadteIfExists(Boolean.FALSE);
		cliente.setCrlmtamt(BigDecimal.ZERO);
		cliente.setPymtrmid("CONTADO");
		cliente.setAdrscode("PRINCIPAL");
		cliente.setInactive(0);
		cliente.setComment1("0");
		cliente.setComment2("0");
		cliente.setShipcomplete(Boolean.FALSE);
		cliente.getiIAT_GL00010().setCod("20");
		
		validarExisteCliente(cliente.getCustnmbr());
		
		CustomerRequest obj = obtenerEntidadIntegrarCliente(cliente);
		LOG.info(String.format("Cliente a Registrar : %s", gsonLog.toJson(obj)));
		SopResponse resultado = integrarCustomer(obj);
		
		if(resultado.getErrorCode().equals("0"))
			return Boolean.TRUE;
		
		return Boolean.FALSE;
	}

	@Override
	public boolean modificarCliente(Customer cliente) {
		cliente.setUpadteIfExists(Boolean.TRUE);
		CustomerRequest obj = obtenerEntidadIntegrarCliente(cliente);
		LOG.info(String.format("Cliente a Modificar : %s", gsonLog.toJson(obj)));
		SopResponse resultado = integrarCustomer(obj);
		
		if(resultado.getErrorCode().equals("0"))
			return Boolean.TRUE;
		
		return Boolean.FALSE;
	}
	
	private SopResponse integrarCustomer(CustomerRequest customer) {
		final String url = UriComponentsBuilder.fromHttpUrl(
				constantesAmbiente.getUrlIntegradorGp().concat(UrlIntegracionCustomer.INTEGRAR_CUSTOMER.getUrl()))
				.build().toUriString();

		try {
			LOG.info(String.format("Integrando Cliente a GP: %s -> %s", customer.getCustnmbr(), customer.getCustname()));
			return restTemplate.postForObject(url, customer, SopResponse.class);
			
		} catch (HttpStatusCodeException e) {
			LOG.error(e.getMessage());
			throw new ClienteGPException(e.getMessage());
		}
	}
	
	private CustomerRequest obtenerEntidadIntegrarCliente(Customer customer) {
		
		CustomerRequest customerNew =  new CustomerRequest();
		customerNew.setAddress1(customer.getAddress1());
		customerNew.setAddress2(customer.getAddress2());
		customerNew.setAddress3(customer.getAddress3());
		customerNew.setAdrscode(customer.getAdrscode());
		customerNew.setCrlmtamt(customer.getCrlmtamt());
		customerNew.setCustclas(customer.getCustclas());
		customerNew.setCustname(customer.getCustname());
		customerNew.setCustnmbr(customer.getCustnmbr());
		customerNew.setState(customer.getState());
		
		customerNew.setPhone1(customer.getPhone1());
		customerNew.setPhone2(customer.getPhone2());
		customerNew.setPhone3(customer.getPhone3());
		
		Optional<AltMissInfoAdd> canton = customer.getInfoMiscelaneos().stream().filter(x->x.getField().equalsIgnoreCase("Canton")).findAny();
		if(canton.isPresent()) {
			if(UtilidadesCadena.noEsNuloNiBlanco(canton.get().getData())) {
				customerNew.setCity(canton.get().getData());
			}
		}
		if(!customer.isUpadteIfExists()) {
			customerNew.setShipcomplete(customer.isShipcomplete()? 1:0);
			customerNew.setCountry("ECUADOR");
			customerNew.setSlprsnid("9");
			customerNew.setPrclevel("GENERAL");
			customerNew.setInactive(customer.getInactive());
			customerNew.setComment1(customer.getComment1());
			customerNew.setComment2(customer.getComment2());
		}		
		
		customerNew.setCcode(customer.getCcode());
		customerNew.setPymtrmid(customer.getPymtrmid());
		customerNew.setUpadteIfExists(customer.isUpadteIfExists()? 1:0);
		if(customer.getAddressList().isEmpty()) {
			customerNew.setAddressList(new ArrayList<>());
		}else {
			customer.getAddressList().forEach(x -> x.setCustnmbr(customerNew.getCustnmbr()));
			customerNew.setAddressList(customer.getAddressList());
		}
		
		customerNew.setiIAT_GL00010(customer.getiIAT_GL00010());
		customerNew.setiIAT_GL00020(customer.getiIAT_GL00020());
		customerNew.setInfoMiscelaneos(customer.getInfoMiscelaneos());
		return customerNew;
	}
	
	private void validarExisteCliente(String codigoCliente) {
		Customer cliente = obtenerCustomerPorCurstomerNumbre(codigoCliente);
		if (cliente.getCustnmbr() != null) {
			throw new ClienteYaExisteException(cliente.getCustnmbr(), cliente.getCustname());
		}
	}


	@Override
	public List<Country> obtenerPaisesTodos() {
		String url = constantesAmbiente.getUrlIntegradorGp()
				.concat(UrlIntegracionCountry.LISTAR_PAISES_TODO.getUrl());
		Country[] response = restTemplate.getForObject(url, Country[].class);
		return Arrays.asList(response);
	}
	
	private void validarNumeroIdentificacion(String numeroIdentificacion, int tipoIdentificacionCodigo) {

		if (!saltarValidacionIdentificacion(numeroIdentificacion, tipoIdentificacionCodigo)) {

			TipoIdentificacion tipoIdentificacion = null;
			switch (tipoIdentificacionCodigo) {
			case 1:
				tipoIdentificacion = TipoIdentificacion.CEDULA;
				break;
			case 2:
				tipoIdentificacion = TipoIdentificacion.PASAPORTE;
				break;
			case 3:
				tipoIdentificacion = TipoIdentificacion.RUC;
				break;
			default:
				tipoIdentificacion = TipoIdentificacion.DESCONOCIDO;
				break;
			}

			if (!validar(tipoIdentificacion, numeroIdentificacion)) {
				throw new ClienteNumeroIdentificacionInvalidoException(numeroIdentificacion);
			}
		}

	}
	
	private boolean saltarValidacionIdentificacion(String numeroIdentificacion, int tipoIdentificacionCodigo) {
		if(tipoIdentificacionCodigo == 1 || tipoIdentificacionCodigo == 3) {
			
			String[] descomponer = numeroIdentificacion.split("");
			if (Integer.parseInt(descomponer[0] + descomponer[1]) >= 30) {
				return true;
			}

			if (Integer.parseInt(descomponer[2]) == 6) {
				return true;
			}
		}
		return false;
	}

}
