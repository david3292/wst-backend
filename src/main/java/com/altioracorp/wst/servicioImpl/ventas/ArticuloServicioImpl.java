package com.altioracorp.wst.servicioImpl.ventas;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.transaction.Transactional;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.SerializationUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import com.altioracorp.gpintegrator.client.Item.Item;
import com.altioracorp.gpintegrator.client.Item.ItemBinQuantity;
import com.altioracorp.gpintegrator.client.Item.ItemInventory;
import com.altioracorp.gpintegrator.client.Item.ItemReplenishment;
import com.altioracorp.gpintegrator.client.Item.ItemResponse;
import com.altioracorp.gpintegrator.client.Item.ItemSite;
import com.altioracorp.gpintegrator.client.Item.Iv40201;
import com.altioracorp.gpintegrator.client.Item.IvItem40400;
import com.altioracorp.gpintegrator.client.Item.IvItemMasterIntegrator;
import com.altioracorp.gpintegrator.client.Item.IvItemPriceListHeader;
import com.altioracorp.gpintegrator.client.Item.IvItemPriceList_ItemPrinceListLine;
import com.altioracorp.gpintegrator.client.Item.UpdateCreateItemRcd;
import com.altioracorp.gpintegrator.client.miscellaneous.AltMissInfoAdd;
import com.altioracorp.wst.constantes.integracion.UrlIntegracionItem;
import com.altioracorp.wst.constantes.integracion.UrlIntegracionSiteSetup;
import com.altioracorp.wst.controlador.MensajesControlador;
import com.altioracorp.wst.dominio.sistema.AsignacionBodega;
import com.altioracorp.wst.dominio.sistema.Bodega;
import com.altioracorp.wst.dominio.sistema.ConfigSistema;
import com.altioracorp.wst.dominio.sistema.Perfil;
import com.altioracorp.wst.dominio.sistema.PerfilNombre;
import com.altioracorp.wst.dominio.sistema.PuntoVentaBodega;
import com.altioracorp.wst.dominio.sistema.SecuencialArticulo;
import com.altioracorp.wst.dominio.sistema.UsuarioPerfil;
import com.altioracorp.wst.dominio.ventas.ArticuloCompartimientoDTO;
import com.altioracorp.wst.dominio.ventas.CriterioArticuloDTO;
import com.altioracorp.wst.dominio.ventas.DocumentoDetalle;
import com.altioracorp.wst.dominio.ventas.DocumentoDetalleCompartimiento;
import com.altioracorp.wst.dominio.ventas.ReservaArticulo;
import com.altioracorp.wst.dominio.ventas.ReservaArticuloCompartimiento;
import com.altioracorp.wst.dominio.ventas.StockArticuloDTO;
import com.altioracorp.wst.dominio.ventas.StockDetalleDTO;
import com.altioracorp.wst.dominio.ventas.StockSolicitudDTO;
import com.altioracorp.wst.exception.sistema.SecuencialArticuloNoExiste;
import com.altioracorp.wst.repositorio.sistema.IAsignacionBodegaRepositorio;
import com.altioracorp.wst.repositorio.sistema.ICodigoAlternoRepositorio;
import com.altioracorp.wst.repositorio.sistema.IPuntoVentaBodegaRepositorio;
import com.altioracorp.wst.repositorio.sistema.ISecuencialArticuloRepositorio;
import com.altioracorp.wst.servicio.sistema.IConfiguracionSistemaServicio;
import com.altioracorp.wst.servicio.sistema.IConstantesAmbienteWstServicio;
import com.altioracorp.wst.servicio.sistema.IUsuarioPerfilServicio;
import com.altioracorp.wst.servicio.ventas.IArticuloServicio;
import com.altioracorp.wst.servicio.ventas.IReservaArticuloCompartimientoServicio;
import com.altioracorp.wst.servicio.ventas.IReservaArticuloServicio;
import com.altioracorp.wst.util.UtilidadesCadena;
import com.altioracorp.wst.util.UtilidadesSeguridad;
import com.google.gson.Gson;

@Service
public class ArticuloServicioImpl implements IArticuloServicio {

	private static final Log LOG = LogFactory.getLog(ArticuloServicioImpl.class);

	@Autowired
	private RestTemplate restTemplate;

	@Autowired
	private IConstantesAmbienteWstServicio constantesAmbiente;

	@Autowired
	private IUsuarioPerfilServicio usuarioPerfilService;

	@Autowired
	private IAsignacionBodegaRepositorio asignacionesBodegaRepositorio;

	@Autowired
	private IPuntoVentaBodegaRepositorio puntoVentaBodegaRepositorio;

	@Autowired
	private IReservaArticuloServicio reservaArticuloServicio;

	@Autowired
	private IReservaArticuloCompartimientoServicio reservaArticuloCompartimientoServicio;

	@Autowired
	private Gson gsonLog;
	
	@Autowired
	private ICodigoAlternoRepositorio codigoAlternoRepositorio;

	@Autowired 
	private ISecuencialArticuloRepositorio secuencialArticuloRepositorio;
	
	@Autowired
	private IConfiguracionSistemaServicio configuracionSistema;
	
	private BigDecimal consultarCantidadesReserva(String codigoArticulo, String codigoBodega) {
		ReservaArticulo reserva = reservaArticuloServicio.buscarPorArticuloYBodega(codigoArticulo, codigoBodega);
		return reserva == null ? BigDecimal.ZERO
				: reserva.getCantidad().compareTo(BigDecimal.ZERO) == -1 ? BigDecimal.ZERO : reserva.getCantidad();
	}

	private BigDecimal consultarCantidadesReservaCompartimiento(String codigoArticulo, String codigoBodega,
			String compartimiento) {

		ReservaArticuloCompartimiento reserva = reservaArticuloCompartimientoServicio
				.buscarPorArticuloYBodegaYCompartimiento(
						new ReservaArticuloCompartimiento(codigoArticulo, codigoBodega, compartimiento));

		return reserva == null ? BigDecimal.ZERO : reserva.getCantidad();
	}

	@Override
	@Transactional
	public void liberarReservasArticulosYCompartimientos(List<DocumentoDetalle> detalles) {

		detalles.forEach(detalle -> {

			LOG.info(String.format("Liberando reserva y compartimiente del articulo %s y bodega %s",
					detalle.getCodigoArticulo(), detalle.getCodigoBodega()));

			reservaArticuloServicio
					.decrementarReserva(new ReservaArticulo(detalle.getCodigoArticulo(),
							detalle.getCodigoBodega(), detalle.getSaldo()));

			this.liberarReservasCompartimientos(detalle);
		});

	}

	@Override
	@Transactional
	public void liberarReservasCompartimientos(DocumentoDetalle detalle) {
		detalle.getCompartimientos().forEach(x -> {
			ReservaArticuloCompartimiento reservaCompartimiento = new ReservaArticuloCompartimiento(
					detalle.getCodigoArticulo(), detalle.getCodigoBodega(),
					x.getCompartimiento(), x.getCantidad());

			reservaArticuloCompartimientoServicio.decrementarReserva(reservaCompartimiento);
		});
	}

	@Override
	@Transactional
	public void reservarArticulos(List<DocumentoDetalle> detalle) {
		detalle.stream().filter(x -> !x.getServicio()).forEach(x -> {
			ReservaArticulo reserva = new ReservaArticulo();
			reserva.setCantidad(x.getCantidad());
			reserva.setCodigoArticulo(x.getCodigoArticulo());
			reserva.setCodigoBodega(x.getCodigoBodega());
			reservaArticuloServicio.incremetarReserva(reserva);
		});
	}
	
	@Override
	@Transactional
	public void reservarArticulosReposicion(List<DocumentoDetalle> detalles) {
		detalles.stream().forEach(x -> {
			ReservaArticulo reserva = new ReservaArticulo();
			reserva.setCantidad(x.getCantidad());
			reserva.setCodigoArticulo(x.getCodigoArticulo());
			reserva.setCodigoBodega(x.getCodigoBodega());
			reservaArticuloServicio.incremetarReserva(reserva);
		});
	}

	private List<AsignacionBodega> obtenerAccesoBodega(String puntoVentaNombre) {

		List<AsignacionBodega> asignaciones = new ArrayList<>();

		Optional<UsuarioPerfil> usuarioPerfil = usuarioPerfilService.listarPorNombreUsuarioYPerfilNombre(
				UtilidadesSeguridad.nombreUsuarioEnSesion(), PerfilNombre.VENDEDOR);

		if (usuarioPerfil.isPresent()) {
			asignaciones = asignacionesBodegaRepositorio
					.findByConfiguracionUsuarioPerfil_UsuarioPerfilAndAccesoTrue(usuarioPerfil.get()).stream()
					.filter(ab -> ab.getConfiguracionUsuarioPerfil().getPuntoVenta().getNombre()
							.equalsIgnoreCase(puntoVentaNombre))
					.collect(Collectors.toList());

			LOG.info(asignaciones);
		}

		return asignaciones;
	}

	@Override
	public List<Item> obtenerArticulosPorCriterio(CriterioArticuloDTO criterio) {
		String url = constantesAmbiente.getUrlIntegradorGp().concat(UrlIntegracionItem.OBTENER_POR_CRITERIO.getUrl());
		Bodega bodegaPrincipal = null;

		if (criterio.getPuntoVenta() != null) {
			bodegaPrincipal = obtenerBodegaPrincipal(criterio.getPuntoVenta().getNombre());
		}

		String bodega = bodegaPrincipal == null ? "" : bodegaPrincipal.getCodigo();
		UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(url)
				.queryParam("criterio", criterio.getCriterio()).queryParam("locncode", bodega);

		LOG.info("Peticion: " + builder.toUriString());
		Item[] response = restTemplate.getForObject(builder.toUriString(), Item[].class);
		return Arrays.asList(response);
	}
	
	private List<ItemBinQuantity> obtenerBinsByLocncode(String locncode) {

		String url = constantesAmbiente.getUrlIntegradorGp()
				.concat(UrlIntegracionItem.OBTENER_BINS_POR_LOCNCODE.getUrl().replace("${locncode}", locncode));

		LOG.info("Peticion obtener Bins por Locncode: " + url);

		ItemBinQuantity[] response = restTemplate.getForObject(url, ItemBinQuantity[].class);

		return Arrays.asList(response);
	}

	private Bodega obtenerBodegaPrincipal(String puntoVentaNombre) {
		Optional<PuntoVentaBodega> bodegaPrincipal = puntoVentaBodegaRepositorio
				.findByPrincipalTrueAndPuntoVenta_Nombre(puntoVentaNombre);
		if (bodegaPrincipal.isPresent()) {
			return bodegaPrincipal.get().getBodega();
		} else {
			return null;
		}
	}

	@Override
	public List<ArticuloCompartimientoDTO> obtenerCompartimientosPorBodega(StockSolicitudDTO dto) {
		List<ItemBinQuantity> compartimientos = obtenerBinsByLocncode(dto.getBodega());
		List<ArticuloCompartimientoDTO> compartimientosDTOBodega = new ArrayList<>();
		if (CollectionUtils.isNotEmpty(compartimientos)) {
			compartimientosDTOBodega.addAll(compartimientos.stream().map(c -> new ArticuloCompartimientoDTO("",
					c.getLocncode(), c.getBin(), BigDecimal.ZERO, BigDecimal.ZERO)).collect(Collectors.toList()));
			List<ArticuloCompartimientoDTO> compartimientosdStock = obtenerStockArticuloCompartimiento(dto);
			compartimientosdStock.forEach(cs -> {
				compartimientosDTOBodega
						.removeIf(dtoC -> dtoC.getCompartimiento().trim().equals(cs.getCompartimiento().trim()));
				compartimientosDTOBodega.add(cs);
			});
		}
		return compartimientosDTOBodega;
	}

	@Override
	public List<ArticuloCompartimientoDTO> obtenerStockArticuloCompartimiento(StockSolicitudDTO dto) {

		List<ItemBinQuantity> compartimientos = obtenerStockBinGP(dto.getItemnmbr(), dto.getBodega());

		List<ArticuloCompartimientoDTO> compartimientosDTO = new ArrayList<>();

		for (ItemBinQuantity item : compartimientos) {

			BigDecimal cantidadReservada = consultarCantidadesReservaCompartimiento(item.getItemnmbr(),
					item.getLocncode(), item.getBin());

			compartimientosDTO.add(new ArticuloCompartimientoDTO(item.getItemnmbr(), item.getLocncode(), item.getBin(),
					cantidadReservada, new BigDecimal(item.getQuantity() - item.getAtyalloc())));
		}

		LOG.info(String.format("Stock Compartimientos obtenidos: %s", gsonLog.toJson(compartimientosDTO)));

		return compartimientosDTO;
	}

	@Override
	public StockArticuloDTO obtenerStockArticuloPorItemnmbr(StockSolicitudDTO dto) {

		StockArticuloDTO stockPorBodega = new StockArticuloDTO();
		final ItemInventory stockGP = obtenerStockGP(dto.getItemnmbr());
		final List<AsignacionBodega> accesosBodega = this.obtenerAccesoBodega(dto.getPuntoVenta());
		stockPorBodega.setPuntoVenta(dto.getPuntoVenta());
		stockPorBodega.setCodigoArticulo(dto.getItemnmbr());
		Bodega principal = obtenerBodegaPrincipal(dto.getPuntoVenta());

		accesosBodega.forEach(ab -> {
			stockGP.getQuantities().forEach(q -> {
				if (ab.getBodega().getCodigo().equalsIgnoreCase(q.getLocncode())) {
					BigDecimal stockExistente = q.getQtyonhnd().subtract(q.getAtyalloc());
					boolean esPrincipal = principal == null ? Boolean.FALSE
							: principal.getCodigo().equalsIgnoreCase(ab.getBodega().getCodigo()) ? Boolean.TRUE
									: Boolean.FALSE;
					stockPorBodega.agregarStockDetalle(new StockDetalleDTO(ab.getBodega(), stockExistente,
							consultarCantidadesReserva(dto.getItemnmbr(), ab.getBodega().getCodigo()), esPrincipal));
				}
			});
		});
		return stockPorBodega;
	}

	@Override
	public StockArticuloDTO obtenerStockArticuloPorItemnmbrNoVendedor(StockSolicitudDTO dto) {

		StockArticuloDTO stockPorBodega = new StockArticuloDTO();

		final ItemInventory stockGP = obtenerStockGP(dto.getItemnmbr());

		final List<PuntoVentaBodega> bodegas = this.puntoVentaBodegaRepositorio
				.findByPuntoVenta_Nombre(dto.getPuntoVenta());

		stockPorBodega.setPuntoVenta(dto.getPuntoVenta());

		stockPorBodega.setCodigoArticulo(dto.getItemnmbr());

		bodegas.forEach(x -> {

			stockGP.getQuantities().forEach(q -> {

				if (x.getBodega().getCodigo().equals(q.getLocncode())) {

					BigDecimal stockExistente = q.getQtyonhnd().subtract(q.getAtyalloc());

					stockPorBodega.agregarStockDetalle(new StockDetalleDTO(x.getBodega(), stockExistente,
							consultarCantidadesReserva(dto.getItemnmbr(), q.getLocncode()), x.isPrincipal()));
				}

			});
		});

		return stockPorBodega;
	}

	private List<ItemBinQuantity> obtenerStockBinGP(String itemnmbr, String locncode) {

		String url = constantesAmbiente.getUrlIntegradorGp()
				.concat(UrlIntegracionItem.OBTENER_STOCK_BIN_POR_ITEMNMBR_AND_LOCNCODE.getUrl());

		UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(url).queryParam("itemnmbr", itemnmbr)
				.queryParam("locncode", locncode);

		LOG.info("Peticion obtener Stock Bin: " + builder.encode().toUriString());

		ItemBinQuantity[] response = restTemplate.getForObject(builder.toUriString(), ItemBinQuantity[].class);

		return Arrays.asList(response);
	}

	@Override
	public StockArticuloDTO obtenerStockBodegas(StockSolicitudDTO dto) {
		StockArticuloDTO stock = this.obtenerStockArticuloPorItemnmbr(dto);
		ordernarBodegas(stock);
		return stock;
	}

	private ItemInventory obtenerStockGP(String itemnmbr) {
		String url = constantesAmbiente.getUrlIntegradorGp()
				.concat(UrlIntegracionItem.OBTENER_STOCK_POR_ITEMNMBR.getUrl());
		UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(url).queryParam("itemnmbr", itemnmbr);

		LOG.info("Peticion obtener Stock: " + builder.encode().toUriString());
		ItemInventory response = restTemplate.getForObject(builder.encode().toUriString(), ItemInventory.class);
		return response;
	}

	private void ordernarBodegas(StockArticuloDTO dto) {
		List<StockDetalleDTO> detalle1 = new ArrayList<>();
		List<StockDetalleDTO> detalle2 = new ArrayList<>();
		dto.getStockDetalle().forEach(x -> {
			if (x.isBodegaPrincipal()) {
				detalle1.add(x);
			} else {
				detalle2.add(x);
			}
		});
		detalle1.addAll(detalle2);
		dto.setStockDetalle(detalle1);
	}

	@Override
	@Transactional
	public void reservarArticuloCompartimientos(DocumentoDetalle detalle) {

		detalle.getCompartimientos().forEach(x -> {

			ReservaArticuloCompartimiento reservaCompartimiento = new ReservaArticuloCompartimiento(
					detalle.getCodigoArticulo(), detalle.getCodigoBodega(),
					x.getCompartimiento(), x.getCantidad());

			reservaArticuloCompartimientoServicio.incremetarReserva(reservaCompartimiento);

		});
	}
	
	private void reservarArticuloCompartimiento(DocumentoDetalle detalle, DocumentoDetalleCompartimiento detalleCompartimiento) {
		
		ReservaArticuloCompartimiento reservaCompartimiento = new ReservaArticuloCompartimiento(
				detalle.getCodigoArticulo(), detalle.getCodigoBodega(),
				detalleCompartimiento.getCompartimiento(), detalleCompartimiento.getCantidad());

		reservaArticuloCompartimientoServicio.incremetarReserva(reservaCompartimiento);
	}

	@Override
	@Transactional
	public void liberarReservasArticulos(List<DocumentoDetalle> detalles) {

		detalles.forEach(detalle -> {

			LOG.info(String.format("Liberando reserva del articulo %s y bodega %s",
					detalle.getCodigoArticulo(), detalle.getCodigoBodega()));

			reservaArticuloServicio
					.decrementarReserva(new ReservaArticulo(detalle.getCodigoArticulo(),
							detalle.getCodigoBodega(), detalle.getSaldo()));

		});

	}
	
	@Override
	@Transactional
	public void liberarReservasArticulosReposicion(List<DocumentoDetalle> detalles) {

		detalles.forEach(detalle -> {

			LOG.info(String.format("Liberando reserva del articulo %s y bodega %s",
					detalle.getCodigoArticulo(), detalle.getCodigoBodega()));

			reservaArticuloServicio
					.decrementarReserva(new ReservaArticulo(detalle.getCodigoArticulo(),
							detalle.getCodigoBodega(), detalle.getSaldo()));

		});

	}

	@Override
	@Transactional
	public void agregarCompartimientoADetalle(DocumentoDetalle detalle) {

		List<ArticuloCompartimientoDTO> compartimientos = obtenerStockArticuloCompartimiento(
				new StockSolicitudDTO(detalle.getCodigoArticulo(), detalle.getCodigoBodega()));

		compartimientos = compartimientos.stream()
				.sorted(Comparator.comparing(ArticuloCompartimientoDTO::getCantidadTotal).reversed())
				.collect(Collectors.toList());
		
		BigDecimal sumaCompartimiento = detalle.getCompartimientos().stream().map(x -> x.getCantidad()).reduce(BigDecimal.ZERO, BigDecimal::add);

		BigDecimal cantidad = detalle.getCantidad().subtract(sumaCompartimiento);
		
		if (cantidad.compareTo(BigDecimal.ZERO) > 0) {
			
			for (ArticuloCompartimientoDTO compartimiento : compartimientos) {

				if (cantidad.compareTo(BigDecimal.ZERO) > 0) {

					BigDecimal cantidadCompartimiento = BigDecimal.ZERO;

					DocumentoDetalleCompartimiento detalleCompartimiento = new DocumentoDetalleCompartimiento();

					detalleCompartimiento.setCompartimiento(compartimiento.getCompartimiento());

					if (cantidad.compareTo(compartimiento.getCantidadTotal()) == 0
							|| cantidad.compareTo(compartimiento.getCantidadTotal()) == -1)
						cantidadCompartimiento = cantidadCompartimiento.add(cantidad);
					else
						cantidadCompartimiento = cantidadCompartimiento.add(compartimiento.getCantidadTotal());

					if (cantidadCompartimiento.compareTo(BigDecimal.ZERO) == 1) {
						
						detalleCompartimiento.setCantidad(cantidadCompartimiento);

						detalle.agregarCompartimiento(detalleCompartimiento);

						cantidad = cantidad.subtract(cantidadCompartimiento);
						
						reservarArticuloCompartimiento(detalle, detalleCompartimiento);
					}
					
				} else
					break;
			}
		}
	}

	@org.springframework.transaction.annotation.Transactional
	public String obtenerCodigoAlterno() {
		
		String codigoAlterno = codigoAlternoRepositorio.getCodigoAlterno(); 
		if(StringUtils.isNotBlank(codigoAlterno))
			deshabilitarCodigoAlterno(codigoAlterno);
		
		return codigoAlterno;
	}
	
	@org.springframework.transaction.annotation.Transactional
	public void deshabilitarCodigoAlterno(String codigoAlterno) {
		codigoAlternoRepositorio.disableCodigoAlterno(codigoAlterno);
	}
	
	private List<IvItem40400> obtenerClasesArticulo(){
		List<IvItem40400> clases = new ArrayList<>();
		
		String url = constantesAmbiente.getUrlIntegradorGp().concat(UrlIntegracionItem.OBTENER_CLASES_ARTICULO.getUrl());
		
		try {
			IvItem40400[] clasesConsulta = restTemplate.getForObject(url, IvItem40400[].class);
			clases.addAll(Arrays.asList(clasesConsulta));
		} catch (Exception e) {
			LOG.info("Error al obtener la lista de clases de articulos");
		}
		
		return clases;
	}
	
	private List<IvItem40400> obtenerClasesArticuloPorLinea(List<String> lineas){
		List<IvItem40400> clases = obtenerClasesArticulo();
		
		if(CollectionUtils.isNotEmpty(clases))
			clases = clases.stream().filter(c -> lineas.contains(c.getUscatvls_2())).collect(Collectors.toList()); 
		
		return clases;
	}
	
	@Override
	public List<IvItem40400> obtenerClasesArticuloPorPerfil(Perfil perfil){
		
		switch (perfil.getNombre()) {
		case VENDEDOR:
			return obtenerClasesArticuloPorLinea(Arrays.asList("L"));

		default:
			return new ArrayList<IvItem40400>();
		}
	}
	
	@Override
	public List<Item> obtenerArticulosPorCriterioPerfil(CriterioArticuloDTO criterio) {
		
		List<String> categorias = obtenerClasesArticuloPorPerfil(criterio.getPerfil()).stream().map(IvItem40400::getUscatvls_2).collect(Collectors.toList());
		
		String url = constantesAmbiente.getUrlIntegradorGp().concat(UrlIntegracionItem.OBTENER_POR_CRITERIO.getUrl());
		Bodega bodegaPrincipal =null;
		
		if(criterio.getPuntoVenta() != null) {
			bodegaPrincipal = obtenerBodegaPrincipal(criterio.getPuntoVenta().getNombre());
		}
		
		String bodega = bodegaPrincipal == null? "":bodegaPrincipal.getCodigo();
		UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(url).queryParam("criterio", criterio.getCriterio()).queryParam("locncode", bodega);

		LOG.info("Peticion: " + builder.toUriString());
		Item[] response = restTemplate.getForObject(builder.toUriString(), Item[].class);
		
		List<Item> articulosBusqueda = Arrays.asList(response).stream().filter(f -> categorias.contains(f.getUscatvls_2())).collect(Collectors.toList()); 
		
		return articulosBusqueda;
	}
	
	@Override
	@org.springframework.transaction.annotation.Transactional
	public Map<String, Object> crearActualizarArticulo(ItemInventory articulo){
		Map<String, Object> resumenIntegracion = new HashMap<String, Object>();
		LOG.info(String.format("Inicia integracion del articulo: %s", gsonLog.toJson(articulo)));
		
		IvItemMasterIntegrator itemMaster = obtenerDatosIntegracionArticulo(articulo);
		LOG.info(String.format("Articulo a integrar: %s", gsonLog.toJson(itemMaster)));
		
		ItemResponse response = llamarApiIntegracionArticulo(itemMaster);
		
		if(StringUtils.isNotBlank(response.getErrorCode()) && response.getErrorCode().equals("0")) {
			articulo = obtenerArticuloPorCodigo(itemMaster.getUpdateCreateItemRcd().getItemnmbr());
			resumenIntegracion.put(MensajesControlador.MENSAJE_CODIGO, "OK");
			resumenIntegracion.put("articulo", articulo);
		}else {
			resumenIntegracion.put(MensajesControlador.MENSAJE_CODIGO, "ERROR");
			resumenIntegracion.put(MensajesControlador.MENSAJE, response.getErrorMessage());
		}
		
		return resumenIntegracion;
	}
	
	private IvItemMasterIntegrator obtenerDatosIntegracionArticulo(ItemInventory articulo) {
		
		final boolean esActualizacion = StringUtils.isNotBlank(articulo.getItemnmbr());
		
		IvItemMasterIntegrator masterIntegrator = new IvItemMasterIntegrator();
		List<String> bodegas = obtenerBodegasParaIntegracionArticulo();
		
		String codigoArticulo = StringUtils.EMPTY;
		String codigoAlterno = StringUtils.EMPTY;
		String secuencial = StringUtils.EMPTY;
		String linea = StringUtils.EMPTY;
		
		if(esActualizacion) {
			codigoArticulo = articulo.getItemnmbr();
			codigoAlterno = articulo.getItmshnam();
			secuencial = articulo.getItmgedsc();
			linea = articulo.getUscatvls_2();
		}else {
			SecuencialArticulo secuencialArticulo = this.obtenerSecuencialPorClase(articulo.getItmclscd());
			codigoArticulo = secuencialArticulo.getCodigoArticulo();
			
			codigoAlterno = codigoArticulo;
			
			secuencial = String.valueOf(secuencialArticulo.getSucesion() * 1000);
			linea = secuencialArticulo.getLinea();
		}
		
		UpdateCreateItemRcd itemRcd = new UpdateCreateItemRcd();
		itemRcd.setItemnmbr(codigoArticulo);
		itemRcd.setItemdesc(articulo.getItemdesc());
		itemRcd.setItmshnam(codigoAlterno);
		itemRcd.setItmclscd(articulo.getItmclscd());
		itemRcd.setItmgedsc(secuencial);
		itemRcd.setUomschdl(articulo.getUomschdl());
		itemRcd.setPrchsuom(articulo.getPrchsuom());
		itemRcd.setLocncode(articulo.getLocncode());
		
		if(linea.equals("L")) {
			if(esActualizacion) {
				itemRcd.setUscatvls_5(articulo.getUscatvls_5());
			}else {
				Double porcentajeUtilidad = configuracionSistema.obetenerConfiguracionPorNombre(ConfigSistema.PORCENTAJE_MARGEN_UTILIDAD_ARTICULO_REVENTA).get().getValor();
				itemRcd.setUscatvls_5(String.valueOf(porcentajeUtilidad));
			}
		}
		
		itemRcd.setUseItemClass(1);
		itemRcd.setUpdateIfExists(1);
		
		masterIntegrator.setUpdateCreateItemRcd(itemRcd);
		
		IvItemPriceListHeader priceListHeader = new IvItemPriceListHeader();
		priceListHeader.setItemNmbr(codigoArticulo);
		priceListHeader.setCurncyId("USD");
		priceListHeader.setPriceGroup(articulo.getPrclevel());
		priceListHeader.setPrcLevel(articulo.getPrclevel());
		priceListHeader.setUofm(articulo.getUomschdl());
		priceListHeader.setUpdateIfExists(1);
		//priceListHeader.setPricMthd(1);
		
		masterIntegrator.setItemPriceListHeader(priceListHeader);
		
		IvItemPriceList_ItemPrinceListLine priceListline = new IvItemPriceList_ItemPrinceListLine();
		priceListline.setItemNmbr(codigoArticulo);
		priceListline.setCurncyId("USD");
		priceListline.setPrcLevel(articulo.getPrclevel());
		priceListline.setUofm(articulo.getUomschdl());
		priceListline.setUomprice(BigDecimal.ZERO);
		priceListline.setUpdateIfExists(1);
		
		masterIntegrator.setItemPriceListLines(Arrays.asList(priceListline));
		
		ItemSite itemSite = new ItemSite();
		itemSite.setItemNmbr(codigoArticulo);
		itemSite.setUpdateIfExists(1);
		
		masterIntegrator.setItemsSite(
		bodegas.stream().map(b -> {
			ItemSite is = SerializationUtils.clone(itemSite);
			is.setLocnCode(b);
			return is;
		}).collect(Collectors.toList()));
		
		AltMissInfoAdd pesoArticulo = new AltMissInfoAdd();
		pesoArticulo.setMasterId(codigoArticulo);
		pesoArticulo.setType("INV");
		pesoArticulo.setField("Peso");
		pesoArticulo.setSequency(1);
		pesoArticulo.setData(articulo.getPeso().setScale(5, RoundingMode.HALF_UP).toString());
		pesoArticulo.setIsEnabled(true);
		pesoArticulo.setIsInvElectronic(false);
		pesoArticulo.setIsMain(false);
		pesoArticulo.setMasterId2(articulo.getItemdesc());
		
		masterIntegrator.setPesoArticulo(pesoArticulo);
		
		return masterIntegrator;
	}
	
	private List<String> obtenerBodegasParaIntegracionArticulo(){
		List<String> bodegas = new ArrayList<String>();
		
		String url = constantesAmbiente.getUrlIntegradorGp().concat(UrlIntegracionSiteSetup.SITES_FOR_CREATE_UPDATE_ITEM.getUrl());
		
		try {
			
			String[] bodegasConsulta = restTemplate.getForObject(url, String[].class);
			bodegas.addAll(Arrays.asList(bodegasConsulta));
			
		} catch (Exception e) {
			LOG.error(String.format("Error al obtener la lista de bodegas %s", e.getMessage()));
		} 
		
		return bodegas;
	}
	
	@Override
	public List<Iv40201> obtenerUnidadesDeMedida(){
		List<Iv40201> unidadesMedida = new ArrayList<Iv40201>();
		
		String url = constantesAmbiente.getUrlIntegradorGp().concat(UrlIntegracionItem.OBTENER_UNIDADES_MEDIDA.getUrl());
		
		try {
			
			Iv40201[] unidadesConsulta = restTemplate.getForObject(url, Iv40201[].class);
			unidadesMedida.addAll(Arrays.asList(unidadesConsulta));
			
		} catch (Exception e) {
			LOG.error(String.format("Error al obtener la lista de bodegas: %s", e.getMessage()));
		}
		
		return unidadesMedida;
	}
	
	@Override
	public ItemInventory obtenerArticuloPorCodigo(String codigoArticulo) {
		
		if(codigoArticulo.equals("undefined"))
			return null;
		
		ItemInventory articulo = this.obtenerStockGP(codigoArticulo);
		if(articulo != null)
			articulo.setQuantities(null);
		
		return articulo;
	}
	
	@Override
	public List<String> obtenerCategoriasPorTipo(int tipo){
		List<String> categorias = new ArrayList<String>();
		
		try {
			String url = constantesAmbiente.getUrlIntegradorGp().concat(UrlIntegracionItem.OBTENER_CATEGORIAS_POR_TIPO.getUrl().replace("${type}", Integer.toString(tipo)));
			String[] categoriasConsulta = restTemplate.getForObject(url, String[].class);
			if(categoriasConsulta != null && categoriasConsulta.length > 0)
				categorias.addAll(Arrays.asList(categoriasConsulta));
			
		}catch (Exception e) {
			LOG.error(String.format("Error al obtener las categorias del tipo: %d", tipo));
			
		}
		
		return categorias;
	}
	
	@Override
	public List<String> obtenerListasDePrecios(){
		List<String> listaPrecios = new ArrayList<String>();
		
		try {
			String url = constantesAmbiente.getUrlIntegradorGp().concat(UrlIntegracionItem.OBTENER_LISTA_PRECIOS.getUrl());
			String[] listaPreciosConsulta = restTemplate.getForObject(url, String[].class);
			if(listaPreciosConsulta != null && listaPreciosConsulta.length > 0)
				listaPrecios.addAll(Arrays.asList(listaPreciosConsulta));
			
		}catch (Exception e) {
			LOG.info("Error al obtener la lista de precios");
		}
		
		return listaPrecios;
	}
	
	@org.springframework.transaction.annotation.Transactional
	private SecuencialArticulo obtenerSecuencialPorClase(String clase) {
		Optional<SecuencialArticulo> secuencialArticuloOpt = this.secuencialArticuloRepositorio.findByClase(clase);
		
		if(secuencialArticuloOpt.isEmpty())
			throw new SecuencialArticuloNoExiste();
		
		SecuencialArticulo secuencialArticulo = secuencialArticuloOpt.get();
		secuencialArticulo.incrementar();
		
		this.secuencialArticuloRepositorio.save(secuencialArticulo);
		
		StringBuilder secuencialBuilder = new StringBuilder().append(secuencialArticulo.getTipoCompra()).append("-");
		secuencialBuilder.append(UtilidadesCadena.completarCerosIzquierda(secuencialArticulo.getSucesion(), 6)).append("-").append(secuencialArticulo.getLinea());
		
		secuencialArticulo.setCodigoArticulo(secuencialBuilder.toString());
		
		return secuencialArticulo;
	}
	
	private ItemResponse llamarApiIntegracionArticulo(IvItemMasterIntegrator itemMaster) {
		ItemResponse response = new ItemResponse();
		try {
			final String url =constantesAmbiente.getUrlIntegradorGp().concat(UrlIntegracionItem.INTEGRAR_ARTICULO.getUrl());
			response = restTemplate.postForObject(url, itemMaster, ItemResponse.class);
		} catch (Exception e) {
			LOG.error(e.getMessage());
			response.setErrorCode("-1");
			response.setErrorMessage(e.getMessage());			
		}
		
		return response;
	}
	
	@Override
	public List<ItemReplenishment> obtenerReposicion(String bodegaOrigen, String bodegaDestino) {

		String url = constantesAmbiente.getUrlIntegradorGp()
				.concat(UrlIntegracionItem.OBTENER_REPOSICION.getUrl());
		
		UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(url)
				.queryParam("locncodeOrigin", bodegaOrigen).queryParam("locncodeDestination", bodegaDestino);
				

		LOG.info("Peticion obtener reposición: " + url);

		ItemReplenishment[] response = restTemplate.getForObject(builder.toUriString(), ItemReplenishment[].class);
		
		return Arrays.asList(response);
		
	}
	
	@Override
	public Item obtenerReposicionPorArticuloYBodega(String codigoArticulo, String codigoBodega) {

		String url = constantesAmbiente.getUrlIntegradorGp()
				.concat(UrlIntegracionItem.OBTENER_REPOSICION_POR_ARTICULO_Y_BODEGA.getUrl());
		
		UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(url)
				.queryParam("itemnmbr", codigoArticulo).queryParam("locncode", codigoBodega);
				

		LOG.info("Peticion obtener item reposición: " + url);

		Item response = restTemplate.getForObject(builder.toUriString(), Item.class);

		return response;
	}
}
