package com.altioracorp.wst.servicioImpl.compras;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import javax.mail.MessagingException;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.HttpStatusCodeException;
import org.springframework.web.client.RestTemplate;

import com.altioracorp.gpintegrator.client.Item.Item;
import com.altioracorp.gpintegrator.client.Item.ItemResponse;
import com.altioracorp.gpintegrator.client.Item.IvItemPriceListHeader;
import com.altioracorp.gpintegrator.client.Item.IvItemPriceListIntegrator;
import com.altioracorp.gpintegrator.client.Item.IvItemPriceList_ItemPrinceListLine;
import com.altioracorp.gpintegrator.client.Po.PoA40003;
import com.altioracorp.gpintegrator.client.Po.PoHeader;
import com.altioracorp.gpintegrator.client.Po.PoIntegrator;
import com.altioracorp.gpintegrator.client.Po.PoLine;
import com.altioracorp.gpintegrator.client.Po.PoResponse;
import com.altioracorp.gpintegrator.client.PopRcpt.PopRcptHdrInsert;
import com.altioracorp.gpintegrator.client.PopRcpt.PopRcptIntegrator;
import com.altioracorp.gpintegrator.client.PopRcpt.PopRcptLine;
import com.altioracorp.gpintegrator.client.PopRcpt.PopRcptMultiBin;
import com.altioracorp.gpintegrator.client.SiteSetup.SiteSetup;
import com.altioracorp.gpintegrator.client.Vendor.Vendor;
import com.altioracorp.wst.UserDetailsServices;
import com.altioracorp.wst.constantes.integracion.UrlIntegracionItem;
import com.altioracorp.wst.constantes.integracion.UrlIntegracionPo;
import com.altioracorp.wst.constantes.integracion.UrlIntegracionPopRcpt;
import com.altioracorp.wst.controlador.MensajesControlador;
import com.altioracorp.wst.dominio.compras.OrdenCompra;
import com.altioracorp.wst.dominio.compras.OrdenCompraDetalle;
import com.altioracorp.wst.dominio.compras.OrdenCompraEstado;
import com.altioracorp.wst.dominio.compras.RecepcionCompra;
import com.altioracorp.wst.dominio.compras.RecepcionCompraDetalle;
import com.altioracorp.wst.dominio.compras.RecepcionCompraEstado;
import com.altioracorp.wst.dominio.compras.dto.ArticuloCompraDTO;
import com.altioracorp.wst.dominio.sistema.Bodega;
import com.altioracorp.wst.dominio.sistema.ConfigSistema;
import com.altioracorp.wst.dominio.sistema.ConfiguracionSistema;
import com.altioracorp.wst.dominio.sistema.PuntoVenta;
import com.altioracorp.wst.dominio.sistema.Usuario;
import com.altioracorp.wst.dominio.ventas.Cotizacion;
import com.altioracorp.wst.dominio.ventas.CotizacionDetalle;
import com.altioracorp.wst.dominio.ventas.CotizacionEstado;
import com.altioracorp.wst.dominio.ventas.CriterioArticuloDTO;
import com.altioracorp.wst.dominio.ventas.DocumentoDetalle;
import com.altioracorp.wst.dominio.ventas.DocumentoFactura;
import com.altioracorp.wst.dominio.ventas.dto.OrdenCompraDTO;
import com.altioracorp.wst.dominio.ventas.dto.OrdenCompraDetalleDTO;
import com.altioracorp.wst.dominio.ventas.dto.RecepcionCompraDTO;
import com.altioracorp.wst.exception.reporte.JasperReportsException;
import com.altioracorp.wst.exception.ventas.OrdenCompraException;
import com.altioracorp.wst.repositorio.compras.IOrdenCompraRepositorio;
import com.altioracorp.wst.repositorio.compras.IRecepcionCompraRepositorio;
import com.altioracorp.wst.servicio.compras.IComprasServicio;
import com.altioracorp.wst.servicio.notificaciones.INotificacionServicio;
import com.altioracorp.wst.servicio.reporte.IGeneradorJasperReports;
import com.altioracorp.wst.servicio.sistema.IBodegaServicio;
import com.altioracorp.wst.servicio.sistema.IConfiguracionSistemaServicio;
import com.altioracorp.wst.servicio.sistema.IConstantesAmbienteWstServicio;
import com.altioracorp.wst.servicio.sistema.IPuntoVentaBodegaServicio;
import com.altioracorp.wst.servicio.sistema.IUsuarioServicio;
import com.altioracorp.wst.servicio.ventas.IArticuloServicio;
import com.altioracorp.wst.servicio.ventas.ICompartimientoServicio;
import com.altioracorp.wst.servicio.ventas.ICotizacionServicio;
import com.altioracorp.wst.servicio.ventas.IProveedorServicio;
import com.altioracorp.wst.util.UtilidadesFecha;
import com.altioracorp.wst.util.UtilidadesSeguridad;
import com.google.common.collect.Streams;
import com.google.gson.Gson;

@Service
public class ComprasServicio implements IComprasServicio{
	
	private static final Log LOG = LogFactory.getLog(ComprasServicio.class);

	@Autowired
	private IOrdenCompraRepositorio ordenCompraRepositorio;
	
	@Autowired
	private IRecepcionCompraRepositorio recepcionCompraRepositorio;
	
	@Autowired
	private IProveedorServicio proveedorServicio;
	
	@Autowired
	private IBodegaServicio bodegaServicio;
	
	@Autowired
	private ICotizacionServicio cotizacionServicio;
	
	@Autowired
	private IConstantesAmbienteWstServicio constantesAmbiente;
	
	@Autowired
	private IConfiguracionSistemaServicio configuracionSistemaServicio;
	
	@Autowired
	private IGeneradorJasperReports generarReporteServicio;
	
	@Autowired
	private IPuntoVentaBodegaServicio puntoVentaBodegaServicio;
	
	@Autowired
	private IArticuloServicio articuloServicio;
	
	@Autowired
	private RestTemplate restTemplate;
	
	@Autowired
	private Gson gsonLog;
	
	@Autowired
	private ModelMapper modelMapper;
	
	@Autowired
	private IUsuarioServicio usuarioServicio;
	
	@Autowired
	private INotificacionServicio notificacionServicio;

	@Autowired
	private ICompartimientoServicio compartimientoServicio;
	
	@Transactional(readOnly = true)
	public byte[] generarReporteOrdenCompra(long ordenCompraId) {
		byte[] report;
		try {
			
			OrdenCompra ordenCompra = ordenCompraRepositorio.findById(ordenCompraId).get();
			SiteSetup bodega = bodegaServicio.listarBodegaPorLocnCode(ordenCompra.getBodegaEntrega());
			
			ordenCompra.setDireccionDestino(bodega.getAddress1());
			
			report = generarReporteServicio.generarReporte("OrdenCompra", Collections.singleton(ordenCompra), null);
		}catch(JasperReportsException ex) {
			LOG.error(String.format("Error al generar la orden de compra con id %d", ordenCompraId));
			report = null;
		}
		
		return report;
	}
	
	@Transactional(readOnly = true)
	public byte[] generarReporteRecepcionCompra(long recepcionCompraId) {
		byte[] report;
		try {
			
			RecepcionCompra recepcionCompra = recepcionCompraRepositorio.findById(recepcionCompraId).get();
			OrdenCompra ordenCompra = recepcionCompra.getOrdenCompra();
			
			recepcionCompra.getDetalle().forEach(rd -> {
				Optional<OrdenCompraDetalle> detalleOpt = ordenCompra.getDetalle().stream().filter(d -> d.getCodigoArticulo().equals(rd.getCodigoArticulo())).findFirst();
				if(detalleOpt.isPresent())
					rd.setSaldo(detalleOpt.get().getSaldo());
			});
			
			report = generarReporteServicio.generarReporte("RecepcionCompra", Collections.singleton(recepcionCompra), null);
		}catch(JasperReportsException ex) {
			LOG.error(String.format("Error al generar la recepcion de compra con id %d", recepcionCompraId));
			report = null;
		}
		
		return report;
	}
	
	@Override
	@Transactional(readOnly = true)
	public List<OrdenCompraDTO> listarOrdenesCompraEmitidas(Map<String, Long> consulta){
		List<OrdenCompraDTO> ordenesCompra = new ArrayList<OrdenCompraDTO>();
		long usuarioId = consulta.get("usuarioId");
		long puntoVentaId = consulta.get("puntoVentaId");
		
		String bodegaPrincipal = bodegaServicio.obtenerCodigoBodegaPrincipalPorUsuarioIdPerfilIdPuntoVentaId(usuarioId, 7, puntoVentaId);
		if(StringUtils.isNotBlank(bodegaPrincipal)) {
			List<OrdenCompra> ordenesEmitidas = ordenCompraRepositorio.findByBodegaEntregaAndEstadoIn(bodegaPrincipal, Arrays.asList(OrdenCompraEstado.EMITIDO));
			if(!CollectionUtils.isEmpty(ordenesEmitidas)) {
				ordenesCompra.addAll(
				ordenesEmitidas.stream().map(oc -> {
					OrdenCompraDTO dto = modelMapper.map(oc, OrdenCompraDTO.class);
					Optional<Usuario> usuarioOpt = usuarioServicio.listarPorNombreUsuario(oc.getCotizacion().getCreadoPor());
					if(usuarioOpt.isPresent())
						dto.setNombreVendedor(usuarioOpt.get().getNombreCompleto());
					
					return dto;
				})
				.collect(Collectors.toList()));
				ordenesCompra = ordenesCompra.stream().sorted(Comparator.comparing(OrdenCompraDTO::getFechaEmision).reversed()).collect(Collectors.toList());
			}
		}
		
		return ordenesCompra;
	}
	
	@Override
	@Transactional(readOnly = true)
	public List<OrdenCompraDTO> listarComprasError(PuntoVenta puntoVenta){
		boolean esSuperAdmin = UtilidadesSeguridad.usuarioEnSesion().toLowerCase().equalsIgnoreCase(UserDetailsServices.SUPERADMIN);
		
		List<OrdenCompraDTO> comprasError = new ArrayList<OrdenCompraDTO>();
		
		UtilidadesSeguridad.usuarioEnSesion();
		
		List<OrdenCompraEstado> estadosError = Arrays.asList(OrdenCompraEstado.ERROR_ORDEN_COMPRA);
		List<OrdenCompra> compras = new ArrayList<OrdenCompra>();
		
		if(esSuperAdmin)
			compras = ordenCompraRepositorio.findByEstadoIn(estadosError);
		else {
			Bodega bodega = puntoVentaBodegaServicio.buscarBodegaPrincipalPorPuntoVenta(puntoVenta);
			compras = ordenCompraRepositorio.findByBodegaEntregaAndEstadoIn(bodega.getCodigo(), estadosError);
		}
		
		if(!CollectionUtils.isEmpty(compras))
			comprasError.addAll(compras.stream().map(c -> {
				OrdenCompraDTO compra = modelMapper.map(c, OrdenCompraDTO.class);
				return compra;
			}).collect(Collectors.toList()));
		
		comprasError = comprasError.stream().sorted(Comparator.comparing(OrdenCompraDTO::getId).reversed()).collect(Collectors.toList());
		
		return comprasError;
	}
	
	@Override
	@Transactional(readOnly = true)
	public List<RecepcionCompraDTO> listarRecepcionesError(PuntoVenta puntoVenta) {
		boolean esSuperAdmin = UtilidadesSeguridad.usuarioEnSesion().toLowerCase().equalsIgnoreCase(UserDetailsServices.SUPERADMIN);
		
		List<RecepcionCompraDTO> recepcionesError = new ArrayList<RecepcionCompraDTO>();
		
		List<RecepcionCompraEstado> estadosError = Arrays.asList(RecepcionCompraEstado.ERROR_RECEPCION);
		List<RecepcionCompra> recepciones = new ArrayList<RecepcionCompra>();
		
		if(esSuperAdmin)
			recepciones = recepcionCompraRepositorio.findByEstadoIn(estadosError);
		else {
			Bodega bodega = puntoVentaBodegaServicio.buscarBodegaPrincipalPorPuntoVenta(puntoVenta);
			recepciones = recepcionCompraRepositorio.findByEstadoInAndOrdenCompra_BodegaEntrega(estadosError, bodega.getCodigo());
		}
		
		if(!CollectionUtils.isEmpty(recepciones))
			recepcionesError.addAll(recepciones.stream().map(r -> {
				RecepcionCompraDTO recepcion = modelMapper.map(r, RecepcionCompraDTO.class);
				return recepcion;
			}).collect(Collectors.toList()));
		
		recepcionesError = recepcionesError.stream().sorted(Comparator.comparing(RecepcionCompraDTO::getId).reversed()).collect(Collectors.toList());
		
		return recepcionesError;
	}
	
	@Override
	@Transactional(readOnly = true)
	public List<OrdenCompraDTO> listarOrdenesCompraDtoPorCotizacionId(long cotizacionId){
		List<OrdenCompraDTO> ordenesCompraDto = new ArrayList<OrdenCompraDTO>();
		List<OrdenCompra> ordenes = ordenCompraRepositorio.findByCotizacion_Id(cotizacionId);
		if(!CollectionUtils.isEmpty(ordenes))
			ordenes.forEach(o -> {
				OrdenCompraDTO dto = modelMapper.map(o, OrdenCompraDTO.class);
				ordenesCompraDto.add(dto);
			});
		return ordenesCompraDto;
	}
	
	@Override
	@Transactional(readOnly = true)
	public List<OrdenCompra> listarOrdenesCompraPorCotizacionId(long cotizacionId){
		return ordenCompraRepositorio.findByCotizacion_Id(cotizacionId);
	}
	
	@Override
	@Transactional(readOnly = true)
	public List<RecepcionCompraDTO> listarRecepcionesPorCotizacionId(long cotizacionId){
		List<RecepcionCompraDTO> recepcionesDto = new ArrayList<RecepcionCompraDTO>();
		List<RecepcionCompra> recepciones = recepcionCompraRepositorio.findByOrdenCompra_Cotizacion_Id(cotizacionId);
		if(!CollectionUtils.isEmpty(recepciones)) {
			recepciones.forEach(r -> {
				RecepcionCompraDTO rDto = modelMapper.map(r, RecepcionCompraDTO.class);
				recepcionesDto.add(rDto);
			});
		}
		
		return recepcionesDto;
	}
	
	@Transactional(readOnly = true)
	@Override
	public ArticuloCompraDTO verificarOrdenCompra(ArticuloCompraDTO articuloCompra) { 
		ConfiguracionSistema configSistema = configuracionSistemaServicio.obetenerConfiguracionPorNombre(ConfigSistema.PORCENTAJE_MARGEN_UTILIDAD_ARTICULO_REVENTA).get();
		
		ArticuloCompraDTO articuloCompraR = new ArticuloCompraDTO(articuloCompra.getCotizacionDetalle());
		BigDecimal margenUtilidad = obtenerMargenUtilidadPorArticulo(articuloCompra);
		
		if(margenUtilidad.compareTo(BigDecimal.ZERO) == 0) {
			articuloCompraR.setMargenUtilidad(new BigDecimal(configSistema.getValor() / 100));
			articuloCompraR.setMargenUtilidadOriginal(new BigDecimal(configSistema.getValor() / 100));
		}else {
			articuloCompraR.setMargenUtilidad(margenUtilidad.divide(new BigDecimal(100)));
			articuloCompraR.setMargenUtilidadOriginal(margenUtilidad.divide(new BigDecimal(100)));
		}
		
		List<OrdenCompra> ordenesCompra = this.ordenCompraRepositorio.findByCotizacion_Id(articuloCompra.getCotizacionId());
		if(CollectionUtils.isNotEmpty(ordenesCompra)) {
			Optional<OrdenCompraDetalle> detalleOpt = buscarArticuloCompra(ordenesCompra, articuloCompra);
			if(detalleOpt.isPresent()) {
				OrdenCompraDetalle detalle = detalleOpt.get();
				articuloCompra.setCantidad(detalle.getCantidad());
				articuloCompra.setCostoUnitarioCompra(detalle.getCostoUnitario());
				articuloCompra.setPrecioVenta(detalle.getPrecioVenta());
				articuloCompra.setMargenUtilidadOriginal(detalle.getMargenUtilidadOriginal() == null ? articuloCompraR.getMargenUtilidadOriginal() : detalle.getMargenUtilidadOriginal());
				articuloCompra.setMargenUtilidad(detalle.getMargenUtilidad() == null ? articuloCompraR.getMargenUtilidad() : detalle.getMargenUtilidad());
				
				OrdenCompra ordenCompra = ordenesCompra.stream().filter(oc -> oc.getId() == detalle.getOrdenCompraId()).findFirst().get();
				Vendor proveedor = new Vendor();
				proveedor.setVendorId(ordenCompra.getCodigoProveedor());
				proveedor.setVendName(ordenCompra.getNombreProveedor());
				proveedor.setPhnumbr1(ordenCompra.getTelefonoProveedor());
				proveedor.setPymtrmid(ordenCompra.getCondicionPago());
				
				articuloCompra.setCondicionPago(ordenCompra.getCondicionPago());
				articuloCompra.setProveedor(proveedor);
				
				
				articuloCompraR = articuloCompra;
			}
		}else {
			articuloCompraR.setPrecioVenta(calcularPrecioVentaSinIva(articuloCompraR));
		}
				
		return articuloCompraR;
	}
	
	private BigDecimal calcularPrecioVentaSinIva(ArticuloCompraDTO articuloCompra) {
		BigDecimal valor1 = new BigDecimal(1).setScale(5, RoundingMode.HALF_UP);
		BigDecimal precioVenta;
		BigDecimal costoUnitario = articuloCompra.getCostoUnitarioCompra().setScale(5, RoundingMode.HALF_UP);
		
		BigDecimal dividendo1 = valor1.subtract(articuloCompra.getMargenUtilidad()).setScale(5, RoundingMode.HALF_UP);
		BigDecimal dividendo2 = valor1.subtract(articuloCompra.getCotizacionDetalle().getDescuentoFijo().divide(new BigDecimal(100).setScale(5, RoundingMode.HALF_UP))).setScale(5, RoundingMode.HALF_UP);
		precioVenta = costoUnitario.divide(dividendo1, 5, RoundingMode.HALF_UP).divide(dividendo2, 5, RoundingMode.HALF_UP);		
		return precioVenta;
	}
	
	private BigDecimal obtenerMargenUtilidadPorArticulo(ArticuloCompraDTO articuloCompra) {
		CriterioArticuloDTO criterioArticulo = new CriterioArticuloDTO(articuloCompra.getCotizacionDetalle().getCodigoArticulo());
		criterioArticulo.setPuntoVenta(articuloCompra.getPuntoVenta());
		List<Item> articulos = articuloServicio.obtenerArticulosPorCriterio(criterioArticulo);
		if(CollectionUtils.isEmpty(articulos)) {
			return BigDecimal.ZERO;
		}else {
			return articulos.stream().findFirst().get().getUscatvls_5();
		}
	}
	
	@Transactional(readOnly = true)
	@Override
	public OrdenCompra obtenerOrdenCompraPorId(long id) {
		OrdenCompra ordenCompra = new OrdenCompra();
		Optional<OrdenCompra> ordenOpt = ordenCompraRepositorio.findById(id);
		if(ordenOpt.isPresent())
			ordenCompra = ordenOpt.get();
		
		return ordenCompra;
	}
	
	@Transactional(readOnly = true)
	public RecepcionCompra obtenerRecepcionComopraPorId(long id) {
		RecepcionCompra recepcionCompra = new RecepcionCompra();
		Optional<RecepcionCompra> recepcionOpt = recepcionCompraRepositorio.findById(id);
		if(recepcionOpt.isPresent())
			recepcionCompra = recepcionOpt.get();
		
		return recepcionCompra;
	}
	
	@Transactional
	@Override
	public Map<String, Object> crearActualizarArticuloCompra(ArticuloCompraDTO articuloCompra) {
		Map<String, Object> respuestaTransaccion = new HashMap<String, Object>();
		LOG.info("Inicio de integracion de lista de precios");
		IvItemPriceListIntegrator itemPriceIntegrator = this.obtenerListaPrecioAIntegrar(articuloCompra);
		LOG.info(String.format("Precio de aticulo a actualizar: %s", gsonLog.toJson(itemPriceIntegrator)));
		ItemResponse response = llamarApiIntegracionListaPrecios(itemPriceIntegrator);
		if(response.getErrorCode() == null || !response.getErrorCode().equals("0")) {
			respuestaTransaccion.put(MensajesControlador.MENSAJE_CODIGO, "ERROR");
			respuestaTransaccion.put(MensajesControlador.MENSAJE, response.getErrorMessage());
		}else {			
			Cotizacion cotizacion = cotizacionServicio.listarPorId(articuloCompra.getCotizacionId());
			crearActualizarOrdenCompra(articuloCompra, cotizacion);
			actualizarDetalleCotizacion(articuloCompra);
			respuestaTransaccion.put(MensajesControlador.MENSAJE_CODIGO, "OK");
			respuestaTransaccion.put(MensajesControlador.MENSAJE, "Actualización correcta");
			respuestaTransaccion.put("cotizacion", cotizacion);
		}
		LOG.info("Finaliza integracion de lista de precios");
		return respuestaTransaccion;
	}
	
	@Transactional
	private void crearActualizarOrdenCompra(ArticuloCompraDTO articuloCompra, Cotizacion cotizacion) {
		Optional<OrdenCompra> ordenCompraOpt = ordenCompraRepositorio.findByCodigoProveedorAndCotizacion_Id(articuloCompra.getProveedor().getVendorId(), articuloCompra.getCotizacionId());
		Optional<OrdenCompra> ordenCompraCambioOpt = Optional.empty();
		Optional<OrdenCompra> ordenCompraEliminarOpt = Optional.empty();
		Vendor proveedor = articuloCompra.getProveedor();
		if(articuloCompra.getProveedorCambio() != null) {
			ordenCompraEliminarOpt = ordenCompraOpt;
			ordenCompraCambioOpt = ordenCompraRepositorio.findByCodigoProveedorAndCotizacion_Id(articuloCompra.getProveedorCambio().getVendorId(), articuloCompra.getCotizacionId());
			ordenCompraOpt = ordenCompraCambioOpt;
			proveedor = articuloCompra.getProveedorCambio();
		}
		OrdenCompraDetalle detalle = new OrdenCompraDetalle();
		
		detalle.setCodigoArticulo(articuloCompra.getCotizacionDetalle().getCodigoArticulo());		
		detalle.setCantidad(articuloCompra.getCantidad().setScale(2, RoundingMode.HALF_UP));
		detalle.setSaldo(articuloCompra.getCantidad().setScale(2, RoundingMode.HALF_UP));
		detalle.setCostoUnitario(articuloCompra.getCostoUnitarioCompra().setScale(2, RoundingMode.HALF_UP));
		detalle.setPrecioVenta(articuloCompra.getPrecioVenta().setScale(2, RoundingMode.HALF_UP));
		detalle.setCotizacionDetalle(articuloCompra.getCotizacionDetalle());
		detalle.setMargenUtilidadOriginal(articuloCompra.getMargenUtilidadOriginal());
		detalle.setMargenUtilidad(articuloCompra.getMargenUtilidad());
		detalle.calcularTotal();
		
		OrdenCompra ordenCompra;
		if(ordenCompraOpt.isPresent()) {
			ordenCompra = ordenCompraOpt.get();
		}else {
			
			Vendor vendor = proveedorServicio.ObtenerProveedorPorId(proveedor.getVendorId());
			proveedor.setEmail(vendor.getEmail());
			
			ordenCompra = new OrdenCompra();
			ordenCompra.setNumeroRecepcion(StringUtils.EMPTY);
			ordenCompra.setEstado(OrdenCompraEstado.NUEVO);
			ordenCompra.setCodigoProveedor(proveedor.getVendorId());
			ordenCompra.setNombreProveedor(proveedor.getVendName());
			ordenCompra.setTelefonoProveedor(proveedor.getPhnumbr1());
			ordenCompra.setEmailProveedor(proveedor.getEmail());
			ordenCompra.setBodegaEntrega(articuloCompra.getBodegaEntrega());
			ordenCompra.setReferencia(cotizacion.getNumero());
			ordenCompra.setCotizacion(cotizacion);
			ordenCompra.setCodigoVendedor(cotizacion.getCodigoVendedor());
			ordenCompra.setCondicionPagoGp(articuloCompra.getCondicionPagoGp());
		}
		ordenCompra.setCondicionPago(articuloCompra.getCondicionPago());
		ordenCompra.actualizarDetalle(detalle);
		ordenCompra.calcularTotales();
		LOG.info(String.format("Orden de compra a actualizar: %s", gsonLog.toJson(ordenCompra)));
		ordenCompraRepositorio.save(ordenCompra);
		
		if(ordenCompraEliminarOpt.isPresent()) {
			OrdenCompra ordemCompraActualizar = ordenCompraEliminarOpt.get();
			ordemCompraActualizar.eliminarDetalle(detalle);
			if(CollectionUtils.isEmpty(ordemCompraActualizar.getDetalle())) {
				ordenCompraRepositorio.delete(ordemCompraActualizar);
				LOG.info(String.format("Se elimina la orden de compra con ID: %d puesto que su detalle se encuentra vacio", ordemCompraActualizar.getId()));
			}else {
				ordenCompraRepositorio.save(ordemCompraActualizar);
				LOG.info(String.format("Se elimina el articulo: %s de la orden de compra con ID: %d", detalle.getCodigoArticulo(), ordemCompraActualizar.getId()));
			}
		}
	}
	
	@Transactional
	private void actualizarDetalleCotizacion(ArticuloCompraDTO articuloCompra) {
		CotizacionDetalle cotizacionDetalle = articuloCompra.getCotizacionDetalle();
		cotizacionDetalle.setGeneraCompra(true);
		cotizacionDetalle.setPrecio(articuloCompra.getPrecioVenta().setScale(2, RoundingMode.HALF_UP));
		//cotizacionDetalle.setPrecioGP(articuloCompra.getPrecioVenta().setScale(2, RoundingMode.HALF_UP));		
		cotizacionServicio.modificarLineaDetalle(articuloCompra.getCotizacionId(), cotizacionDetalle);
	}
	
	@Override
	@Transactional
	public RecepcionCompra actualizarCantidadRecepcion(RecepcionCompraDetalle detalle) {
		
		RecepcionCompra recepcionCompra = recepcionCompraRepositorio.findById(detalle.getRecepcionCompraId()).get();
		recepcionCompra.actualizarDetalle(detalle);
		
		recepcionCompraRepositorio.save(recepcionCompra);
		
		return recepcionCompra;
	}
	
	@Override
	@Transactional
	public void eliminarDetalleOrdenCompraPorDetalleCotizacion(long cotizacionId, String codigoArticulo) {
		List<OrdenCompra> ordenesCompra = ordenCompraRepositorio.findByCotizacion_Id(cotizacionId);
		if(!CollectionUtils.isEmpty(ordenesCompra)) {
			ordenesCompra.forEach(c -> {
				boolean eliminado = c.eliminarDetalle(codigoArticulo);
				if(eliminado) {
					if(CollectionUtils.isEmpty(c.getDetalle()))
						ordenCompraRepositorio.delete(c);
					else
						ordenCompraRepositorio.save(c);
				}
			});
		}
	}
	
	private Optional<OrdenCompraDetalle> buscarArticuloCompra(List<OrdenCompra> ordenesCompra, ArticuloCompraDTO articuloCompra) {
		List<OrdenCompraDetalle> detallesConsolidatos = ordenesCompra.stream()
				.map(OrdenCompra::getDetalle)
				.flatMap(Collection::stream)
				.collect(Collectors.toList());
		return detallesConsolidatos.stream()
				.filter(f -> f.getCodigoArticulo().equals(articuloCompra.getCotizacionDetalle().getCodigoArticulo()))
				.findFirst();
	}
	
	private IvItemPriceListIntegrator obtenerListaPrecioAIntegrar(ArticuloCompraDTO articuloCompra) {
		IvItemPriceListIntegrator integrator = new IvItemPriceListIntegrator();
		
		IvItemPriceListHeader header = new IvItemPriceListHeader();
		header.setItemNmbr(articuloCompra.getCotizacionDetalle().getCodigoArticulo());
		header.setPricmthdSpecified(false);
		header.setPriceGroup("GENERAL");
		header.setPrcLevel("GENERAL");
		header.setUofm(articuloCompra.getCotizacionDetalle().getUnidadMedida());
		header.setCurncyId("USD");
		header.setUpdateIfExists(1);
		
		List<IvItemPriceList_ItemPrinceListLine> lista = new ArrayList<IvItemPriceList_ItemPrinceListLine>();
		
		IvItemPriceList_ItemPrinceListLine itemPrecio = new IvItemPriceList_ItemPrinceListLine();
		itemPrecio.setItemNmbr(articuloCompra.getCotizacionDetalle().getCodigoArticulo());
		itemPrecio.setCurncyId("USD");
		itemPrecio.setPrcLevel("GENERAL");
		itemPrecio.setUofm(articuloCompra.getCotizacionDetalle().getUnidadMedida());
		itemPrecio.setUomprice(articuloCompra.getPrecioVenta().setScale(2, RoundingMode.HALF_UP));
		itemPrecio.setUpdateIfExists(1);
		lista.add(itemPrecio);
		
		integrator.setItemPriceListHeader(header);
		integrator.setItemPrinceListLines(lista);
		
		return integrator;
	}
	
	private ItemResponse llamarApiIntegracionListaPrecios(IvItemPriceListIntegrator itemPrecio) {
		try {
			final String url = constantesAmbiente.getUrlIntegradorGp().concat(UrlIntegracionItem.INTEGRAR_ITEM_LISTA_DE_PRECIO.getUrl());
			return restTemplate.postForObject(url, itemPrecio, ItemResponse.class);
		} catch (HttpStatusCodeException e) {
			LOG.error(String.format("Error en integracion de lista de precios: %s", e.getMessage()));
			ItemResponse response = new ItemResponse();
			response.setErrorCode("-1");
			response.setErrorMessage(e.getMessage());
			return response;
		}
	}
	
	@Override
	public Map<String, String> lanzarIntegracionesOrdenCompra(Cotizacion cotizacion) {
		Map<String, String> respuesta = new HashMap<String, String>();
		List<OrdenCompra> ordenesCompra = ordenCompraRepositorio.findByCotizacion_Id(cotizacion.getId());
		if(!CollectionUtils.isEmpty(ordenesCompra)) {
			for(OrdenCompra compra : ordenesCompra) {
				try {
					integrarOrdenCompra(compra.getId());
					respuesta.put(MensajesControlador.MENSAJE_CODIGO, "OK");
				}catch (Exception e) {
					LOG.error(String.format("Error en integracion de orden de Compra: %d ::: mensaje: %s", compra.getId(), e.getMessage()));
					respuesta.put(MensajesControlador.MENSAJE_CODIGO, "ERROR");
					respuesta.put(MensajesControlador.MENSAJE, e.getMessage());
				}
			}
		}
		return respuesta;
	}
	
	@Override
	public Map<String, String> lanzarIntegracionesRecepcionOrdenCompra(Cotizacion cotizacion) {
		Map<String, String> respuesta = new HashMap<String, String>();
		List<OrdenCompra> ordenesCompra = ordenCompraRepositorio.findByCotizacion_Id(cotizacion.getId());
		for(OrdenCompra compra : ordenesCompra) {
			try {
				integrarRecepcionOrdenCompra(compra.getId());
				respuesta.put(MensajesControlador.MENSAJE_CODIGO, "OK");
			}catch (Exception e) {
				LOG.error(String.format("Error en integracion de recepcion de la orden de Compra: %d ::: mensaje: %s", compra.getId(), e.getMessage()));
				respuesta.put(MensajesControlador.MENSAJE_CODIGO, "ERROR");
				respuesta.put(MensajesControlador.MENSAJE, e.getMessage());
			}
		}
		return respuesta;
	}
	
	@Override
	public void aprobarOrdenesCompraPorCotizacionId(long cotizacionId) {
		List<OrdenCompra> compras =  ordenCompraRepositorio.findByCotizacion_Id(cotizacionId);
		compras.parallelStream().forEach(c -> c.setEstado(OrdenCompraEstado.APROBADO));
		compras.forEach(c -> ordenCompraRepositorio.save(c));
	}
	
	@Override
	public OrdenCompra integrarOrdenCompra(long ordenCompraId) {
		List<CotizacionEstado> estadosCotizacionIntegrar = Arrays.asList(CotizacionEstado.EMITIDO, CotizacionEstado.APROBADO, CotizacionEstado.POR_FACTURAR);
		List<OrdenCompraEstado> estadosIntegrar = Arrays.asList(OrdenCompraEstado.APROBADO, OrdenCompraEstado.ERROR_ORDEN_COMPRA);
		
		OrdenCompra ordenCompra = ordenCompraRepositorio.findById(ordenCompraId).get();
		LOG.info(String.format("Inicia la integracion de la orden de compra con ID: %d", ordenCompraId));
		
		if(estadosCotizacionIntegrar.contains(ordenCompra.getCotizacion().getEstado())) {
			
			if(estadosIntegrar.contains(ordenCompra.getEstado())) {
				boolean continuar = true;
				if(StringUtils.isBlank(ordenCompra.getNumero())) {
					String numeroDocumento = this.obtenerNumeroDocumentoOrdenCompra(ordenCompra);
					ordenCompra.setFechaEmision(LocalDateTime.now());
					if(StringUtils.isBlank(numeroDocumento)) {
						String mensaje = String.format("Error al obtener el secuencial para la orden de compra con ID: %d", ordenCompraId);
						ordenCompra.setEstado(OrdenCompraEstado.ERROR_ORDEN_COMPRA);
						ordenCompra.setMensajeError(mensaje);
						continuar = false;
					}
					ordenCompra.setNumero(numeroDocumento);
				}
				if(continuar) {
					ordenCompra.establecerSecuenciales();
					PoIntegrator poIntegrator = obtenerDatosParaIntegracionOrdenCompra(ordenCompra);
					LOG.info(String.format("Orden de comrpa a integrar: %s", gsonLog.toJson(poIntegrator)));
					PoResponse response = llamarApiIntegracionPo(poIntegrator);
					
					if(StringUtils.isNotBlank(response.getErrorCode()) && response.getErrorCode().equals("0")) {
						ordenCompra.setEstado(OrdenCompraEstado.EMITIDO);
						ordenCompra.setMensajeError(StringUtils.EMPTY);
					}else {
						ordenCompra.setEstado(OrdenCompraEstado.ERROR_ORDEN_COMPRA);
						ordenCompra.setMensajeError(response.getErrorMessage().length() > 255 ? response.getErrorMessage().substring(0, 255) : response.getErrorMessage());
					}
				}
				ordenCompraRepositorio.save(ordenCompra);
				LOG.info(String.format("Finaliza la integracion de la orden de compra con ID: %d", ordenCompraId));
			}
		}
		
		return ordenCompra;
	}
	
	@Transactional(noRollbackFor = { MessagingException.class })
	public Map<String, Object> integrarRecepcionOrdenCompra(long recepcionCompraId) {
		Map<String, Object> resumenIntegracion = new HashMap<String, Object>();
		List<RecepcionCompraEstado> estadosIntegrar = Arrays.asList(RecepcionCompraEstado.NUEVO, RecepcionCompraEstado.ERROR_RECEPCION);
		RecepcionCompra recepcionCompra = recepcionCompraRepositorio.findById(recepcionCompraId).get();
		LOG.info(String.format("Inicia la integracion de la recepcion de orden de compra con ID: %d", recepcionCompraId));
		if(estadosIntegrar.contains(recepcionCompra.getEstado())) {
			boolean continuar = true;
			if(StringUtils.isBlank(recepcionCompra.getNumeroRecepcion())) {
				String numeroDocumento = this.obtenerNumeroDocumentoRecepcionOrdenCompra();
				if(StringUtils.isBlank(numeroDocumento)) {
					String mensaje = String.format("Error al obtener el secuencial para la recepcion de orden de compra con ID: %d", recepcionCompraId);
					recepcionCompra.setEstado(RecepcionCompraEstado.ERROR_RECEPCION);
					recepcionCompra.setMensajeError(mensaje);
					resumenIntegracion.put(MensajesControlador.MENSAJE_CODIGO, "ERROR");
					resumenIntegracion.put(MensajesControlador.MENSAJE, mensaje);
					continuar = false;
				}
				recepcionCompra.setNumeroRecepcion(numeroDocumento);
			}
			if(continuar) {
				PopRcptIntegrator popRcptIntegrator = obtenerDatosParaIntegracionRecepcionOrdenCompra(recepcionCompra);
				LOG.info(String.format("Recepcion de orden de comrpa a integrar: %s", gsonLog.toJson(popRcptIntegrator)));
				PoResponse response = llamarApiIntegracionPopRcpt(popRcptIntegrator);
				
				if(StringUtils.isNotBlank(response.getErrorCode()) && response.getErrorCode().equals("0")) {
					recepcionCompra.setEstado(RecepcionCompraEstado.COMPLETADO);
					recepcionCompra.setFechaRecepcion(LocalDateTime.now());
					recepcionCompra.setMensajeError(StringUtils.EMPTY);
					validarSaldosOrdenCompra(recepcionCompra);
					reservarRecepciones(recepcionCompra);
					
					notificacionServicio.notificarRecepcionOrdenCompra(recepcionCompra);
					
					resumenIntegracion.put(MensajesControlador.MENSAJE_CODIGO, "OK");
					resumenIntegracion.put("recepcionCompra", recepcionCompra);
				}else {
					recepcionCompra.setEstado(RecepcionCompraEstado.ERROR_RECEPCION);
					recepcionCompra.setMensajeError(response.getErrorMessage().length() > 255 ? response.getErrorMessage().substring(0, 255) : response.getErrorMessage());
					
					resumenIntegracion.put(MensajesControlador.MENSAJE_CODIGO, "ERROR");
					resumenIntegracion.put(MensajesControlador.MENSAJE, recepcionCompra.getMensajeError());
				}
				
				LOG.info(String.format("Finaliza la integracion de la recepcion de la orden de compra con ID: %d", recepcionCompraId));
			}
			
			recepcionCompraRepositorio.save(recepcionCompra);
		}else {
			resumenIntegracion.put(MensajesControlador.MENSAJE_CODIGO, "OK");			
		}
		resumenIntegracion.put("recepcionCompra", recepcionCompra);
		return resumenIntegracion;
	}
	
	@Transactional
	private void validarSaldosOrdenCompra(RecepcionCompra recepcionCompra) {
		OrdenCompra ordenCompra = recepcionCompra.getOrdenCompra();
		ordenCompra.getDetalle().stream().forEach(x -> {
			Optional<RecepcionCompraDetalle> recepcionDetalleOpt = recepcionCompra.getDetalle().stream().filter(f -> f.getCodigoArticulo().equals(x.getCodigoArticulo())).findFirst();
			if(recepcionDetalleOpt.isPresent())
				x.setSaldo(x.getSaldo().subtract(recepcionDetalleOpt.get().getCantidadRecepcion()));
			
		});
		
		BigDecimal saldoTotal = ordenCompra.getDetalle().stream().map(OrdenCompraDetalle::getSaldo).reduce(BigDecimal.ZERO, BigDecimal::add);
		if(saldoTotal.compareTo(BigDecimal.ZERO) == 0)
			ordenCompra.setEstado(OrdenCompraEstado.CERRADO);
		
		ordenCompraRepositorio.save(ordenCompra);
	}
	
	@Transactional
	private void reservarRecepciones(RecepcionCompra recepcion) {
		List<CotizacionEstado> estadosCotizacionReservar = Arrays.asList(CotizacionEstado.EMITIDO, CotizacionEstado.POR_FACTURAR, CotizacionEstado.APROBADO);
		
		Cotizacion cotizacion = recepcion.getOrdenCompra().getCotizacion();
		if(estadosCotizacionReservar.contains(cotizacion.getEstado())) {
			
			LOG.info(String.format("Se realiza la reserva de la recepcion: %s", gsonLog.toJson(recepcion)));
			
			recepcion.getDetalle().forEach(rd -> {
				Optional<CotizacionDetalle> detalleOpt = cotizacion.getDetalle().stream().filter(cd -> cd.getId() == rd.getCotizacionDetalle().getId()).findFirst();
				if(detalleOpt.isPresent()) {
					CotizacionDetalle cotizacionDetalle = detalleOpt.get();
					BigDecimal cantidadReserva = cotizacionDetalle.getCantidadReserva() == null ? BigDecimal.ZERO : cotizacionDetalle.getCantidadReserva();
					detalleOpt.get().setCantidadReserva(cantidadReserva.add(rd.getCantidadRecepcion()));
				}
			});
			
			List<DocumentoDetalle> detallesReserva = recepcion.getDetalle().stream().map(d -> {
				DocumentoDetalle documentoDetalle = new DocumentoDetalle();
				documentoDetalle.setCantidad(d.getCantidadRecepcion());
				documentoDetalle.setCotizacionDetalle(d.getCotizacionDetalle());
				documentoDetalle.setCodigoBodega(recepcion.getOrdenCompra().getBodegaEntrega());
				return documentoDetalle;
			}).collect(Collectors.toList());
			
			articuloServicio.reservarArticulos(detallesReserva);
		}		
		
	}
	
	private String obtenerNumeroDocumentoOrdenCompra(OrdenCompra ordenCompra) {
		String numeroDocumento = StringUtils.EMPTY;
		final String url = constantesAmbiente.getUrlIntegradorGp().concat(UrlIntegracionPo.OBTENER_NUMERO_GP.getUrl());
		try {
			numeroDocumento = restTemplate.getForObject(url, String.class);
			numeroDocumento = numeroDocumento.replace("\"", StringUtils.EMPTY);
		}catch (Exception e) {
			LOG.error(String.format("Error al obtener el numero de documento para orde de compra: error: %s", e.getMessage()));
		}
		return numeroDocumento;
	}
	
	
	private String obtenerNumeroDocumentoRecepcionOrdenCompra() {
		String numeroDocumento = StringUtils.EMPTY;
		final String url = constantesAmbiente.getUrlIntegradorGp().concat(UrlIntegracionPopRcpt.OBTENER_NUMERO_GP.getUrl());
		try {
			numeroDocumento = restTemplate.getForObject(url, String.class);
			numeroDocumento = numeroDocumento.replace("\"", StringUtils.EMPTY);
		}catch (Exception e) {
			LOG.error(String.format("Error al obtener el numero de documento para recepcion de orden de compra: error: %s", e.getMessage()));
		}
		return numeroDocumento;
	}
	
	private PoResponse llamarApiIntegracionPo(PoIntegrator poIntegrator) {
		try {
			final String url = constantesAmbiente.getUrlIntegradorGp().concat(UrlIntegracionPo.INTEGRAR_PO.getUrl());
			return restTemplate.postForObject(url, poIntegrator, PoResponse.class);
		} catch (HttpStatusCodeException e) {
			LOG.error(e.getMessage());
			PoResponse response = new PoResponse();
			response.setErrorCode("-1");
			response.setErrorMessage(e.getMessage());
			return response;
		}
	}
	
	private PoResponse llamarApiIntegracionPopRcpt(PopRcptIntegrator popRcptIntegrator) {
		try {
			final String url = constantesAmbiente.getUrlIntegradorGp().concat(UrlIntegracionPopRcpt.INTEGRAR_POP_RCPT.getUrl());
			return restTemplate.postForObject(url, popRcptIntegrator, PoResponse.class);
		} catch (HttpStatusCodeException e) {
			LOG.error(e.getMessage());
			PoResponse response = new PoResponse();
			response.setErrorCode("-1");
			response.setErrorMessage(e.getMessage());
			return response;
		}
	}
	
	private PoIntegrator obtenerDatosParaIntegracionOrdenCompra(OrdenCompra ordenCompra) {
		PoIntegrator poIntegrator = new PoIntegrator();
		String fechaIntegracion = UtilidadesFecha.formatear(LocalDateTime.now(), "yyyy-MM-dd");
		PoHeader poHeader = new PoHeader();
		poHeader.setPonumber(ordenCompra.getNumero());
		poHeader.setVendorid(ordenCompra.getCodigoProveedor());
		poHeader.setVendname(ordenCompra.getNombreProveedor());
		poHeader.setPymtrmid(ordenCompra.getCondicionPago());
		poHeader.setDocdate(fechaIntegracion);
		poHeader.setSubtotal(ordenCompra.getSubTotal());
		poHeader.setReqDate(fechaIntegracion);
		poHeader.setReqtndt(fechaIntegracion);
		poHeader.setPotype(1);
		poHeader.setPostatus(1);
		poHeader.setComment1(ordenCompra.getCotizacion().getNumero());
		poHeader.setUsingHeaderLevelTaxes(1);
		
		poIntegrator.setPoHeader(poHeader);
		poIntegrator.setPoLines(
			 ordenCompra.getDetalle().stream().map(d -> {
				PoLine poLine = new PoLine();
				poLine.setPotype(1);
				poLine.setPonumber(ordenCompra.getNumero());
				poLine.setVendorId(ordenCompra.getCodigoProveedor());
				poLine.setLocnCode(ordenCompra.getBodegaEntrega());
				poLine.setItemNmbr(d.getCodigoArticulo());
				poLine.setUofm(d.getCotizacionDetalle().getUnidadMedidaCompra());
				poLine.setPolnesta(1);
				poLine.setItemDesc(d.getCotizacionDetalle().getDescripcionArticulo());
				poLine.setQuantity(d.getCantidad());
				poLine.setUnitCost(d.getCostoUnitario());
				poLine.setOrd(d.getNumeroSecuencia());
				return poLine;
			}).collect(Collectors.toList())
		 );
		
		PoA40003 poA = new PoA40003();
		poA.setPoNumber(ordenCompra.getNumero());
		poA.setVendorId(ordenCompra.getCodigoProveedor());
		poA.setStatgrp(1);
		poA.setDocDate(fechaIntegracion);
		poA.setPoaCreatedBy("sa");
		poA.setPoapoApprovalStatus(2);
		poA.setRemsubto(ordenCompra.getSubTotal());
		poA.setPoaApprovedBy("sa");
		poA.setApprvlDt(fechaIntegracion);
		poA.setPoaApprovalTime("1900-01-01");
		
		poIntegrator.setPoA40003(poA);
		return poIntegrator;
	}
	
	private PopRcptIntegrator obtenerDatosParaIntegracionRecepcionOrdenCompra(RecepcionCompra recepcionCompra) {
		PopRcptIntegrator popRcpIntegrator = new PopRcptIntegrator();
		String fechaIntegracion = UtilidadesFecha.formatear(LocalDateTime.now(), "yyyy-MM-dd");
		
		PopRcptHdrInsert popRcpHeader = new PopRcptHdrInsert();
		popRcpHeader.setPoprctnm(recepcionCompra.getNumeroRecepcion());
		popRcpHeader.setPopType(1);
		popRcpHeader.setVndDocnm(recepcionCompra.getOrdenCompra().getCotizacion().getNumero());
		popRcpHeader.setReceiptdate(fechaIntegracion);
		popRcpHeader.setBachNumb(String.format("WST-%d", recepcionCompra.getId()));
		popRcpHeader.setVendorId(recepcionCompra.getOrdenCompra().getCodigoProveedor());
		popRcpHeader.setPymtrmId(recepcionCompra.getOrdenCompra().getCotizacion().getCondicionPago());
		popRcpHeader.setUsingHeaderLevelTaxes(1);
		popRcpHeader.setCreateDist(1);
		popRcpIntegrator.setPopRcptHdrInsert(popRcpHeader);
		
		popRcpIntegrator.setPopRcptLines(
		Streams.mapWithIndex(recepcionCompra.getDetalle().stream(), (detalle, index) -> {
			PopRcptLine line = new PopRcptLine();
			line.setPopType(1);
			line.setPoprctnm(recepcionCompra.getNumeroRecepcion());
			line.setPoNumber(recepcionCompra.getOrdenCompra().getNumero());
			line.setItemNmbr(detalle.getCodigoArticulo());
			line.setItemDesc(detalle.getCotizacionDetalle().getDescripcionArticulo());
			line.setVnditNum(detalle.getCodigoArticulo());
			line.setVendorId(recepcionCompra.getOrdenCompra().getCodigoProveedor());
			line.setQtyShppd(detalle.getCantidadRecepcion());
			line.setAutocost(1);
			line.setLocnCode(recepcionCompra.getOrdenCompra().getBodegaEntrega());
			line.setPolneNum(detalle.getNumeroSecuencial());
			line.setReceiptDate(fechaIntegracion);
			line.setAutoAssignBin(0);
			return line;
		}).collect(Collectors.toList()));
		
		final String codigoBodega = recepcionCompra.getOrdenCompra().getBodegaEntrega(); 
		popRcpIntegrator.setPopRcptMultiBins(
		Streams.mapWithIndex(recepcionCompra.getDetalle().stream(), (detalle, index) -> {
			String compartimiento = compartimientoServicio.ObtenerCompartimientoPorCodigoArticuloYBodega(detalle.getCodigoArticulo(), codigoBodega);
			PopRcptMultiBin bin = new PopRcptMultiBin();
			bin.setPoprctnm(recepcionCompra.getNumeroRecepcion());
			bin.setRcptlnnm(detalle.getNumeroSecuencial());
			bin.setItemNmbr(detalle.getCodigoArticulo());
			bin.setBin(compartimiento);
			bin.setCreateBin(0);
			bin.setQuantity(detalle.getCantidadRecepcion());
			return bin;
		}).collect(Collectors.toList()));
				
		popRcpIntegrator.getPopRcptLines().removeIf(d -> d.getQtyShppd().compareTo(BigDecimal.ZERO) == 0);
		popRcpIntegrator.getPopRcptMultiBins().removeIf(d -> d.getQuantity().compareTo(BigDecimal.ZERO) == 0);
		
		return popRcpIntegrator;
	}
	
	@Transactional(readOnly = true)
	public boolean validarArticuloEstaIngresado(DocumentoFactura factura) {
		AtomicBoolean estaIngresado = new AtomicBoolean(true);
		List<OrdenCompraEstado> estadosPermitir = Arrays.asList(OrdenCompraEstado.RECIBIDA);
		List<OrdenCompra> ordenesCompra = ordenCompraRepositorio.findByCotizacion_Id(factura.getCotizacion().getId());
		
		if(!CollectionUtils.isEmpty(ordenesCompra)) {
			Stream<OrdenCompraDetalle> detallesAplanados = ordenesCompra.stream()
					.map(OrdenCompra::getDetalle)
					.flatMap(Collection::stream);
					
			
			factura.getDetalle().parallelStream().forEach(df -> {
				Optional<OrdenCompraDetalle> ocDetalleOpt = detallesAplanados
						.filter(d -> d.getCotizacionDetalle().getCodigoArticulo().equals(df.getCotizacionDetalle().getCodigoArticulo()))
						.findFirst();
				
				if(ocDetalleOpt.isPresent()) {
					OrdenCompra ordenCompra = ordenesCompra.stream().filter(oc -> oc.getId() == ocDetalleOpt.get().getOrdenCompraId()).findFirst().get();
					if(!estadosPermitir.contains(ordenCompra.getEstado()))
						estaIngresado.set(false);
				}
			});
		}
		
		return estaIngresado.get();
	}
	
	@Override
	public List<RecepcionCompra> obtenerRecepcionesOrdenesCompraPorId(long ordenCompraId){
		List<RecepcionCompra> recepcionesFinal = new ArrayList<>();
		List<RecepcionCompra> recepcionesConsulta = recepcionCompraRepositorio.findByOrdenCompra_Id(ordenCompraId);
		if(CollectionUtils.isEmpty(recepcionesConsulta))
			recepcionesFinal.add(generarRecepcionCompra(ordenCompraRepositorio.findById(ordenCompraId).get()));
		else
			recepcionesFinal.addAll(recepcionesConsulta);
			
		recepcionesFinal.parallelStream().forEach(r -> {
			r.getOrdenCompra().setDetalle(null);
			r.setDetalle(null);
		});
		
		return recepcionesFinal;
	}
	
	private RecepcionCompra generarRecepcionCompra(OrdenCompra ordenCompra) {
		RecepcionCompra rc = new RecepcionCompra();
		rc.setOrdenCompra(ordenCompra);
		rc.setEstado(RecepcionCompraEstado.NUEVO);
		List<RecepcionCompraDetalle> rds = new ArrayList<>();
		ordenCompra.getDetalle().stream().filter(d -> d.getSaldo().compareTo(BigDecimal.ZERO) > 0).forEach(d -> {
			RecepcionCompraDetalle rd = new RecepcionCompraDetalle();
			rd.setCodigoArticulo(d.getCodigoArticulo());
			rd.setCantidadCompra(d.getCantidad());
			rd.setCantidadRecepcion(d.getSaldo());
			rd.setCotizacionDetalle(d.getCotizacionDetalle());
			rd.setNumeroSecuencial(d.getNumeroSecuencia());
			rds.add(rd);
		});
		rc.setDetalle(rds);
		
		recepcionCompraRepositorio.save(rc);
		LOG.info(String.format("Se crea la recepcion: %s", gsonLog.toJson(rc)));
		
		return rc;
	}
	
	@Override
	public RecepcionCompra crearNuevaRecepcionCompra(long ordenCompraId) {
		OrdenCompra ordenCompra = ordenCompraRepositorio.findById(ordenCompraId).get();
		return generarRecepcionCompra(ordenCompra);
	}
	
	@Transactional
	@Override
	public RecepcionCompra cerrarProcesoCompra(long recepcionCompraId) {
		RecepcionCompra recepcioncompra = recepcionCompraRepositorio.findById(recepcionCompraId).get();
		
		PoResponse response = cerrarOrdenCompraGP(recepcioncompra.getOrdenCompra().getNumero());
		if(response != null && !response.getErrorCode().equals("0"))
			throw new OrdenCompraException(response.getErrorMessage());
		
		recepcioncompra.setEstado(RecepcionCompraEstado.CERRADO);
		OrdenCompra ordenCompra = recepcioncompra.getOrdenCompra();
		ordenCompra.setEstado(OrdenCompraEstado.CERRADO);
		recepcionCompraRepositorio.save(recepcioncompra);
		ordenCompraRepositorio.save(ordenCompra);
		return recepcioncompra;
	}
		
	private PoResponse cerrarOrdenCompraGP(String numeroOrdenCompra) {
		final String url = constantesAmbiente.getUrlIntegradorGp().concat(UrlIntegracionPo.CERRAR_ORDEN_COMPRA.getUrl().replace("${poNumber}", numeroOrdenCompra));
		PoResponse response;
		
		try {
			response = restTemplate.getForObject(url, PoResponse.class);
		} catch (Exception e) {
			response = new PoResponse();
			response.setErrorCode("-1");
			response.setErrorMessage(e.getMessage());
		}
				
		return response;
	}
	
	@Override
	public boolean existeCambioCondicionPagoProveedorPorCotizacionid(long cotizacionId) {
		return ordenCompraRepositorio.countCambioCondicionPagoProveedorPorCotizacionid(cotizacionId) > 0;
	}
	
	@Override
	public boolean existeCambioMargenUtilidadDetallePorCotizacionId(long cotizacionId) {
		return ordenCompraRepositorio.countCambioMargenUtilidadDetallePorCotizacionId(cotizacionId) > 0;
	}
	
	@Override
	public List<OrdenCompraDTO> ordenesCompraCambioCondicionPagoProveedor(long cotizacionId){
		List<OrdenCompraDTO> ordenes = new ArrayList<OrdenCompraDTO>();
		List<Object[]> ordenesConsulta = ordenCompraRepositorio.ordenesCompraCambioCondicionPagoProveedorPorCotizacionid(cotizacionId);
		
		if(!CollectionUtils.isEmpty(ordenesConsulta))
			ordenesConsulta.forEach(o -> {
				OrdenCompraDTO ordenCompra = new OrdenCompraDTO();
				ordenCompra.setCodigoProveedor(o[0].toString());
				ordenCompra.setNombreProveedor(o[1].toString());
				ordenCompra.setCondicionPagoGp(o[2].toString());
				ordenCompra.setCondicionPago(o[3].toString());
				ordenes.add(ordenCompra);
			});
		
		return ordenes;
	}
	
	@Override
	public List<OrdenCompraDetalleDTO> detallesOrdenCompraCambioMargenUtilidad(long cotizacionId) {
		List<OrdenCompraDetalleDTO> detalles = new ArrayList<>();
		List<Object[]> detallesConsulta = ordenCompraRepositorio.detallesCompraCambioMargenUtilidadDetallePorCotizacionId(cotizacionId);
		
		NumberFormat formatoPorcentaje = new DecimalFormat("###.##%");
		
		if(!CollectionUtils.isEmpty(detallesConsulta))
			detallesConsulta.forEach(d -> {
				OrdenCompraDetalleDTO detalle = new OrdenCompraDetalleDTO();
				detalle.setCodigoArticulo(d[0].toString());
				detalle.setDescripcionArticulo(d[1].toString());
				detalle.setMargenUtilidadOriginal(formatoPorcentaje.format(new BigDecimal(d[2].toString())));
				detalle.setMargenUtilidad(formatoPorcentaje.format(new BigDecimal(d[3].toString())));
				detalle.setNombreProveedor(d[4].toString());
				detalles.add(detalle);
			});
			
		return detalles;
	}
	
	@Override
	public Map<String, Object> obtenerOrdenesCompraDetallesAprobacion(long cotizacionId) {
		Map<String, Object> detalles = new HashMap<String, Object>();
		
		detalles.put("ordenesCompra", ordenesCompraCambioCondicionPagoProveedor(cotizacionId));
		detalles.put("detallesCompra", detallesOrdenCompraCambioMargenUtilidad(cotizacionId));
		
		return detalles;
	}
	
	@Transactional(readOnly = true)
	public Map<String, Object> validarCantidadesOrdenesCompraPorCotizacionId(long idCotizacion) {
		Map<String, Object> respuesta = new HashMap<String, Object>();
		respuesta.put("respuesta", true);
		
		List<OrdenCompra> ordenesCompra = ordenCompraRepositorio.findByCotizacion_Id(idCotizacion);
		if(CollectionUtils.isNotEmpty(ordenesCompra)) {
			for(OrdenCompra compra : ordenesCompra) {
				 for(OrdenCompraDetalle detalle : compra.getDetalle()) {
					 if(detalle.esCantidadCompraMayorACotizacion())
						 throw new OrdenCompraException(String.format("La cantidad de compra del artículo %s es mayor a la cotizada", detalle.getCodigoArticulo()));
				 }
			 }
		}
		
		return respuesta;
	}
	
	@Override
	@Transactional
	public void cerrarOrdenesCompraPorCotizacionId(long cotizacionId) {
		List<OrdenCompra> ordenesCompra = ordenCompraRepositorio.findByCotizacion_Id(cotizacionId);
		if(CollectionUtils.isNotEmpty(ordenesCompra)) {
			ordenesCompra.parallelStream().forEach(oc -> {
				if(StringUtils.isNotBlank(oc.getNumero()))
					cerrarOrdenCompraGP(oc.getNumero());
					
				oc.setEstado(OrdenCompraEstado.CERRADO);
			});
			ordenCompraRepositorio.saveAll(ordenesCompra);
		}
	}

	@Override
	public boolean existeOrdenesCompraPorCotizacionId(long cotizacionId) {
		return ordenCompraRepositorio.existsByCotizacion_id(cotizacionId) ;
	}
}
