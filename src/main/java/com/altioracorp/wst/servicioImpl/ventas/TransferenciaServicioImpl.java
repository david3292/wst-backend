package com.altioracorp.wst.servicioImpl.ventas;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.altioracorp.gpintegrator.client.Item.Item;
import com.altioracorp.gpintegrator.client.Item.ItemReplenishment;
import com.altioracorp.gpintegrator.client.SiteSetup.SiteSetup;
import com.altioracorp.wst.dominio.logistica.dto.ArticuloReposicionDTO;
import com.altioracorp.wst.dominio.logistica.dto.ReposicionArticuloBodegaDTO;
import com.altioracorp.wst.dominio.logistica.dto.ReposicionDTO;
import com.altioracorp.wst.dominio.logistica.dto.ReposicionDetalleDTO;
import com.altioracorp.wst.dominio.logistica.dto.ReposicionRespuestaCambioLineaDTO;
import com.altioracorp.wst.dominio.logistica.dto.ReposicionSugerenciaDTO;
import com.altioracorp.wst.dominio.logistica.dto.TransferenciaConsulta;
import com.altioracorp.wst.dominio.logistica.dto.TransferenciaDTO;
import com.altioracorp.wst.dominio.sistema.Perfil;
import com.altioracorp.wst.dominio.sistema.PuntoVentaBodega;
import com.altioracorp.wst.dominio.sistema.Secuencial;
import com.altioracorp.wst.dominio.sistema.TipoDocumento;
import com.altioracorp.wst.dominio.sistema.Usuario;
import com.altioracorp.wst.dominio.ventas.DocumentoBase;
import com.altioracorp.wst.dominio.ventas.DocumentoDetalle;
import com.altioracorp.wst.dominio.ventas.DocumentoDetalleCompartimiento;
import com.altioracorp.wst.dominio.ventas.DocumentoEstado;
import com.altioracorp.wst.dominio.ventas.DocumentoGuiaRemision;
import com.altioracorp.wst.dominio.ventas.DocumentoReserva;
import com.altioracorp.wst.dominio.ventas.DocumentoTransferencia;
import com.altioracorp.wst.dominio.ventas.DocumentoTransferenciaEntrada;
import com.altioracorp.wst.dominio.ventas.DocumentoTransferenciaSalida;
import com.altioracorp.wst.dominio.ventas.TipoTransferencia;
import com.altioracorp.wst.dominio.ventas.dto.TransferenciaConsultaDTO;
import com.altioracorp.wst.exception.logistica.TransferenciaNoEncontrada;
import com.altioracorp.wst.exception.logistica.TransferenciaSinDetalleException;
import com.altioracorp.wst.exception.reporte.JasperReportsException;
import com.altioracorp.wst.exception.sistema.SecuancialNoExisteException;
import com.altioracorp.wst.repositorio.sistema.IUsuarioRepositorio;
import com.altioracorp.wst.repositorio.ventas.IDocumentoDetalleRepositorio;
import com.altioracorp.wst.repositorio.ventas.IDocumentoReservaRepositorio;
import com.altioracorp.wst.repositorio.ventas.IDocumentoTransferenciaEntradaRepositorio;
import com.altioracorp.wst.repositorio.ventas.IDocumentoTransferenciaRepositorio;
import com.altioracorp.wst.repositorio.ventas.IDocumentoTransferenciaSalidaRepositorio;
import com.altioracorp.wst.servicio.reporte.IGeneradorJasperReports;
import com.altioracorp.wst.servicio.sistema.IBodegaServicio;
import com.altioracorp.wst.servicio.sistema.IPerfilServicio;
import com.altioracorp.wst.servicio.sistema.IPuntoVentaBodegaServicio;
import com.altioracorp.wst.servicio.sistema.ISecuencialServicio;
import com.altioracorp.wst.servicio.ventas.IArticuloServicio;
import com.altioracorp.wst.servicio.ventas.ICompartimientoServicio;
import com.altioracorp.wst.servicio.ventas.IDocumentoServicio;
import com.altioracorp.wst.servicio.ventas.IGuiaRemisionServicio;
import com.altioracorp.wst.servicio.ventas.ITransferenciaServicio;
import com.altioracorp.wst.util.UtilidadesCadena;
import com.google.gson.Gson;

@Service
public class TransferenciaServicioImpl implements ITransferenciaServicio {

	private static final Log LOG = LogFactory.getLog(TransferenciaServicioImpl.class);
	
	@Autowired
	private IDocumentoTransferenciaRepositorio transferenciaRepositorio;
	
	@Autowired
	private IDocumentoTransferenciaSalidaRepositorio transferenciaSalidaRepositorio;
	
	@Autowired
	private IDocumentoTransferenciaEntradaRepositorio transferenciaEntradaRepositorio;
	
	@Autowired
	private IDocumentoServicio documentoServicio;
	
	@Autowired
	private ModelMapper modelMapper;
	
	@Autowired
	private IUsuarioRepositorio usuarioRepositorio;
	
	@Autowired
	private IBodegaServicio bodegaservicio;
	
	@Autowired
	private IDocumentoReservaRepositorio reservaRepositorio;
	
	@Autowired
	private IDocumentoDetalleRepositorio documentoDetalleRepositorio;
	
	@Autowired
	private IArticuloServicio articuloServicio;
	
	@Autowired
	private IGuiaRemisionServicio guiaRemisionServicio;
	
	@Autowired
	private IGeneradorJasperReports generarReporteServicio;
	
	@Autowired
	private ICompartimientoServicio compartimientoServicio;

	@Autowired
	private Gson gsonLog;
	
	@PersistenceContext
	private EntityManager entityManager;
	
	@Autowired
	private IPerfilServicio perfilServicio;
	
	@Autowired
	private IPuntoVentaBodegaServicio puntoVentaBodegaServicio;
	
	@Autowired
	private ISecuencialServicio secuencialServicio;


	
	@Override
	@Transactional
	public DocumentoTransferencia guardar(DocumentoTransferencia transferencia) {
		
		transferencia.setNumero(documentoServicio.secuencialDocumento(transferencia.getCotizacion().getPuntoVenta(), TipoDocumento.TRANSFERENCIA).getNumeroSecuencial());
		
		transferencia.setEstado(DocumentoEstado.EMITIDO);
		
		return transferenciaRepositorio.save(transferencia);
	}
	
	@Override
	@Transactional(readOnly = true)	
	public List<TransferenciaDTO> listarTransferenciasPorUsuarioIdYPuntoVentaId(Map<String, Object> consulta) {
		
		if(consulta.get("perfil") == null)
			return new ArrayList<TransferenciaDTO>();
		
		List<TransferenciaDTO> transferenciasDto;
		long usuarioId = Long.valueOf((int)consulta.get("usuarioId"));
		long puntoVentaId = Long.valueOf((int)consulta.get("puntoVentaId"));
		TipoDocumento tipoDocumento = TipoDocumento.valueOf(consulta.get("tipoConsulta").toString());
		Perfil perfil = perfilServicio.listarTodos().stream().filter(p -> p.getNombre().name().equals(consulta.get("perfil").toString())).findFirst().get();
		
		
		LOG.info(String.format("Consulta de transferencias: %s", gsonLog.toJson(consulta)));
		
		String bodegaPrincipal = bodegaservicio.obtenerCodigoBodegaPrincipalPorUsuarioIdPerfilIdPuntoVentaId(usuarioId, perfil.getId(), puntoVentaId);
		if(StringUtils.isNotBlank(bodegaPrincipal)) {
			transferenciasDto = new ArrayList<TransferenciaDTO>();
			if(tipoDocumento.equals(TipoDocumento.TRANSFERENCIA_SALIDA)) {
				List<DocumentoTransferencia> transferencias;
				transferencias = transferenciaRepositorio.findByBodegaOrigenAndEstadoIn(bodegaPrincipal, Arrays.asList(DocumentoEstado.EMITIDO, DocumentoEstado.EN_PROCESO));
				transferenciasDto = transferencias.stream().map(d -> {
					TransferenciaDTO transferencia = modelMapper.map(d, TransferenciaDTO.class);
					Usuario usuario = usuarioRepositorio.findByNombreUsuario(transferencia.getCotizacionCreadoPor() == null? d.getCreadoPor() : transferencia.getCotizacionCreadoPor()).get();
					BigDecimal peso = d.getDetalle().stream().map(det -> det.getCantidad().multiply(det.getPesoArticulo())).reduce(BigDecimal.ZERO, BigDecimal::add);
					transferencia.setPesoTransferencia(peso);
					transferencia.setNombreusuarioVendedor(usuario.getNombreCompleto());
					return transferencia;
				})
				.collect(Collectors.toList());
			}
			
			if(tipoDocumento.equals(TipoDocumento.TRANSFERENCIA_INGRESO)) {
				List<DocumentoTransferenciaSalida> transferencias;
				transferencias = transferenciaSalidaRepositorio.findByBodegaDestinoAndEstado(bodegaPrincipal, DocumentoEstado.EMITIDO);
				transferenciasDto = transferencias.stream().map(ts -> {
					TransferenciaDTO transferencia = modelMapper.map(ts, TransferenciaDTO.class);
					Usuario usuario = usuarioRepositorio.findByNombreUsuario(transferencia.getCotizacionCreadoPor() == null? ts.getCreadoPor() :transferencia.getCotizacionCreadoPor()).get();
					BigDecimal peso = ts.getDetalle().stream().map(det -> det.getCantidad().multiply(det.getPesoArticulo())).reduce(BigDecimal.ZERO, BigDecimal::add);
					transferencia.setPesoTransferencia(peso);
					transferencia.setNombreusuarioVendedor(usuario.getNombreCompleto());
					transferencia.setTipoTransferencia(ts.isReposicion()? TipoTransferencia.REPOSICION: TipoTransferencia.VENTA);
					return transferencia;
				}).collect(Collectors.toList());
				
			}
			transferenciasDto = transferenciasDto.stream().sorted(Comparator.comparing(TransferenciaDTO::getId).reversed()).collect(Collectors.toList());			
		}else {
			transferenciasDto = new ArrayList<>();
		}
		
		return transferenciasDto;
	}
	
	@Override
	public DocumentoBase buscarDocumentoTransferenciaPorId(long id) {		
		Optional<DocumentoTransferencia> optionalDocumento = transferenciaRepositorio.findById(id);
		if(optionalDocumento.isPresent()) {
			DocumentoTransferencia transferencia = optionalDocumento.get();
			Optional<DocumentoReserva> reservaOptional = reservaRepositorio.findById(transferencia.getDocumentoPadreId());
			if(reservaOptional.isPresent())
				transferencia.setEntregaMercaderia(reservaOptional.get().getEntrega().toString());
			
			SiteSetup siteSetupOrigen = bodegaservicio.listarBodegaPorLocnCode(transferencia.getBodegaOrigen().trim()); 
			SiteSetup siteSetupDestino = bodegaservicio.listarBodegaPorLocnCode(transferencia.getBodegaDestino().trim());
			if(siteSetupOrigen != null) {
				transferencia.setDescBodegaOrigen(siteSetupOrigen.getLocnDscr().trim());
				transferencia.setDireccionBodegaOrigen(siteSetupOrigen.getAddress1().trim());
			}
			if(siteSetupDestino != null) {
				transferencia.setDescBodegaDestino(siteSetupDestino.getLocnDscr().trim());
				transferencia.setDireccionBodegaDestino(siteSetupDestino.getAddress1().trim());
			}
			return transferencia;
		}
		else
			return new DocumentoTransferencia();
	}
	
	@Override
	@Transactional(readOnly = true)
	public DocumentoBase buscarDocumentoTransferenciaSalidaPorId(long id) {
		Optional<DocumentoTransferenciaSalida> transferenciaSalidaOpt = transferenciaSalidaRepositorio.findById(id);
		if(transferenciaSalidaOpt.isPresent()) {
			DocumentoTransferenciaSalida transferenciaSalida = transferenciaSalidaOpt.get();
			DocumentoTransferencia transferencia = transferenciaRepositorio.findById(transferenciaSalida.getDocumentoTransferenciaId()).get();
			unirSaldos(transferencia, transferenciaSalida);
			SiteSetup siteSetupOrigen = bodegaservicio.listarBodegaPorLocnCode(transferenciaSalida.getBodegaOrigen().trim()); 
			SiteSetup siteSetupDestino = bodegaservicio.listarBodegaPorLocnCode(transferenciaSalida.getBodegaDestino().trim());
			if(siteSetupOrigen != null) {
				transferenciaSalida.setDescBodegaOrigen(siteSetupOrigen.getLocnDscr().trim());
				transferenciaSalida.setDireccionBodegaOrigen(siteSetupOrigen.getAddress1().trim());
			}
			if(siteSetupDestino != null) {
				transferenciaSalida.setDescBodegaDestino(siteSetupDestino.getLocnDscr().trim());
				transferenciaSalida.setDireccionBodegaDestino(siteSetupDestino.getAddress1().trim());
			}
			return transferenciaSalida;
		}else {
			return new DocumentoTransferenciaSalida();
		}
	}
	
	private void unirSaldos(final DocumentoBase transferencia, DocumentoBase transferenciaSalida) {
		transferenciaSalida.getDetalle().parallelStream().forEach(d -> {
			Optional<DocumentoDetalle> detalleOpt = transferencia.getDetalle()
					.stream().filter(dts -> dts.getCodigoArticulo().equals(d.getCodigoArticulo()))
					.findFirst();
			if(detalleOpt.isPresent())
				d.setTotal(detalleOpt.get().getSaldo());
		});
	}
	
	@Override
	@Transactional(readOnly = true)
	public DocumentoBase buscarDocumentoTransferenciaEntradaPorId(long id) {
		Optional<DocumentoTransferenciaEntrada> transferenciaEntradaOpt = transferenciaEntradaRepositorio.findById(id);
		if(transferenciaEntradaOpt.isPresent()) {
			DocumentoTransferenciaEntrada transferenciaEntrada = transferenciaEntradaOpt.get();
			DocumentoTransferenciaSalida transferenciaSalida = transferenciaSalidaRepositorio.findById(transferenciaEntrada.getDocumentoTransferenciaSalidaId()).get();
			unirSaldos(transferenciaSalida, transferenciaEntrada);
			SiteSetup siteSetupOrigen = bodegaservicio.listarBodegaPorLocnCode(transferenciaEntrada.getBodegaOrigen().trim()); 
			SiteSetup siteSetupDestino = bodegaservicio.listarBodegaPorLocnCode(transferenciaEntrada.getBodegaDestino().trim());
			if(siteSetupOrigen != null) {
				transferenciaEntrada.setDescBodegaOrigen(siteSetupOrigen.getLocnDscr().trim());
				transferenciaEntrada.setDireccionBodegaOrigen(siteSetupOrigen.getAddress1().trim());
			}
			if(siteSetupDestino != null) {
				transferenciaEntrada.setDescBodegaDestino(siteSetupDestino.getLocnDscr().trim());
				transferenciaEntrada.setDireccionBodegaDestino(siteSetupDestino.getAddress1().trim());
			}
			
			List<DocumentoGuiaRemision> guias = guiaRemisionServicio.buscarPorDocumentoPadreId(transferenciaEntrada.getDocumentoTransferenciaSalidaId());
			Optional<DocumentoGuiaRemision> guiaRemisionOtp = guias.stream().findFirst();
			if(guiaRemisionOtp.isPresent())
				transferenciaEntrada.setGuiaRemision(guiaRemisionOtp.get().getNumero());
			
			return transferenciaEntrada;
		}else {
			return new DocumentoTransferenciaSalida();
		}
	}
	
	@Override
	public List<DocumentoBase> obtenerDocumentosTransferenciaSalidaId(long transferenciaId){
		List<DocumentoBase> transferenciasSalidaFinal = new ArrayList<DocumentoBase>();
		DocumentoTransferencia transferencia = transferenciaRepositorio.findById(transferenciaId).get();
		List<DocumentoTransferenciaSalida> transferenciasSalida = transferenciaSalidaRepositorio.findByDocumentoTransferenciaId(transferenciaId);
		 
		if(CollectionUtils.isEmpty(transferenciasSalida))
			transferenciasSalidaFinal.add(generarDocumentoTransferenciaSalida(transferencia));
		else
			transferenciasSalidaFinal.addAll(transferenciasSalida);
		
		transferenciasSalidaFinal.forEach(t -> {
			Optional<DocumentoGuiaRemision> guiaOpt =  guiaRemisionServicio.buscarPorDocumentoPadreId(transferenciaId).stream().findFirst();
			if(guiaOpt.isPresent()) {
				DocumentoGuiaRemision guia = guiaOpt.get();
				if(guia.getEstado().equals(DocumentoEstado.ERROR))
					t.setEstado(guia.getEstado());
			}
			
		});
		
		transferenciasSalidaFinal.parallelStream().forEach(t -> {
			t.setCotizacion(null);
			t.setDetalle(null);
		});
		
		return transferenciasSalidaFinal;
	}
	
	public List<DocumentoTransferenciaEntrada> obtenerDocumentosTransferenciaEntradaId(long transferenciaSalidaId){
		List<DocumentoTransferenciaEntrada> transferenciasEntradaFinal = new ArrayList<DocumentoTransferenciaEntrada>();
		DocumentoTransferenciaSalida transferenciaSalida = transferenciaSalidaRepositorio.findById(transferenciaSalidaId).get();		
		List<DocumentoTransferenciaEntrada> transferenciasEntrada = transferenciaEntradaRepositorio.findByDocumentoTransferenciaSalidaId(transferenciaSalidaId);
		if(CollectionUtils.isEmpty(transferenciasEntrada))
			transferenciasEntradaFinal.add(generarDocumentoTransferenciaEntrada(transferenciaSalida));
		else
			transferenciasEntradaFinal.addAll(transferenciasEntrada);
			
					
		transferenciasEntradaFinal.parallelStream().forEach(t -> {
			t.setCotizacion(null);
			t.setDetalle(null);			
		});
		return transferenciasEntradaFinal;
	}
	
	@Override
	public DocumentoBase crearNuevaTransferenciaSalida(long transferenciaId) {
		DocumentoBase nuevaTransferenciasalida = new DocumentoTransferencia();		
		DocumentoTransferencia transferencia = transferenciaRepositorio.findById(transferenciaId).get();
		
		if(transferencia.getDetalle().stream().allMatch(d -> d.getSaldo().compareTo(BigDecimal.ZERO) == 0)) 
			throw new TransferenciaSinDetalleException();
			
		nuevaTransferenciasalida = generarDocumentoTransferenciaSalida(transferencia);
		nuevaTransferenciasalida.setCotizacion(null);
		nuevaTransferenciasalida.setDetalle(null);
		LOG.info(String.format("Se crea transferencia de salida: %s", gsonLog.toJson(nuevaTransferenciasalida)));
		
		return nuevaTransferenciasalida;
	}
	
	private DocumentoBase generarDocumentoTransferenciaSalida(DocumentoTransferencia transferencia) {				
		DocumentoTransferenciaSalida tSalida = modelMapper.map(transferencia, DocumentoTransferenciaSalida.class);;
		tSalida.setId((long)0);
		tSalida.setTipo(TipoDocumento.TRANSFERENCIA_SALIDA);
		tSalida.setNumero(null);
		tSalida.setEstado(DocumentoEstado.NUEVO);
		tSalida.setReferencia(transferencia.getNumero());
		tSalida.setDocumentoTransferenciaId(transferencia.getId());
		tSalida.setDetalle(new ArrayList<DocumentoDetalle>());
		
		if (transferencia.getTipoTransferencia().equals(TipoTransferencia.VENTA)) {
			tSalida.setDocumentoReservaId(transferencia.getDocumentoPadreId());
			transferencia.getDetalle().stream().filter(d -> d.getSaldo().compareTo(BigDecimal.ZERO) > 0)
			.forEach(d -> {
				DocumentoDetalle docDetalle = new DocumentoDetalle(d.getCotizacionDetalle(), d.getCodigoBodega(), BigDecimal.ZERO);
				//detalle.add(docDetalle);
				tSalida.agregarLineaDetalle(docDetalle);
				tSalida.setReposicion(false);
			});
		}else {
			transferencia.getDetalle().stream().filter(d -> d.getSaldo().compareTo(BigDecimal.ZERO) > 0).forEach(d -> {
				DocumentoDetalle docDetalle = new DocumentoDetalle(d.getCodigoBodega(), BigDecimal.ZERO, d.getCodigoArticulo(), d.getCodigoArticuloAlterno(), d.getClaseArticulo(), d.getDescripcionArticulo(), d.getPesoArticulo(), d.getUnidadMedida());
				//detalle.add(docDetalle);
				tSalida.agregarLineaDetalle(docDetalle);
				tSalida.setReposicion(true);
			});
		}

		//tSalida.setDetalle(detalle);
		transferenciaSalidaRepositorio.save(tSalida);
		transferencia.setEstado(DocumentoEstado.EN_PROCESO);
		transferenciaRepositorio.save((DocumentoTransferencia)transferencia);
		
		LOG.info(String.format("Se crea la transferencia de salida %s", gsonLog.toJson(tSalida)));
		return tSalida;
	}
	
	private DocumentoTransferenciaEntrada generarDocumentoTransferenciaEntrada(DocumentoBase documento) {
		DocumentoTransferenciaSalida transferenciaSalida = (DocumentoTransferenciaSalida) documento;
		DocumentoTransferenciaEntrada transferenciaEntrada = modelMapper.map(transferenciaSalida,
				DocumentoTransferenciaEntrada.class);
		transferenciaEntrada.setId((long) 0);
		transferenciaEntrada.setTipo(TipoDocumento.TRANSFERENCIA_INGRESO);
		transferenciaEntrada.setEstado(DocumentoEstado.NUEVO);
		transferenciaEntrada.setNumero(null);
		transferenciaEntrada.setReferencia(transferenciaSalida.getNumero());
		transferenciaEntrada.setDocumentoTransferenciaSalidaId(transferenciaSalida.getId());
		transferenciaEntrada.setDocumentoReservaId(transferenciaSalida.getDocumentoReservaId());
		transferenciaEntrada.setFechaEmision(LocalDateTime.now());
		List<DocumentoDetalle> detalles = new ArrayList<DocumentoDetalle>();
		transferenciaSalida.getDetalle().stream().filter(d -> d.getCantidad().compareTo(BigDecimal.ZERO) == 1)
				.forEach(d -> {
					
					DocumentoDetalle detalle = new DocumentoDetalle(transferenciaSalida.getBodegaDestino(), d.getCantidad(),
							d.getCodigoArticulo(), d.getCodigoArticuloAlterno(), d.getClaseArticulo(),
							d.getDescripcionArticulo(), d.getPesoArticulo(), d.getUnidadMedida());
					
					detalles.add(detalle);
				});
		transferenciaEntrada.setDetalle(detalles);
		transferenciaEntradaRepositorio.save(transferenciaEntrada);
		LOG.info(String.format("Se crea la transferencia de entrada %s", gsonLog.toJson(transferenciaEntrada)));
		return transferenciaEntrada;
	}
	
	@Transactional()
	public DocumentoGuiaRemision crearActualizarDocumentoGuiaRemision(long transferenciaSalidaId, DocumentoGuiaRemision guiaRemision) {
		Optional<DocumentoTransferenciaSalida> transferenciaSalidaOpt = transferenciaSalidaRepositorio.findById(transferenciaSalidaId);
		if(transferenciaSalidaOpt.isEmpty()) 
			throw new TransferenciaNoEncontrada();
		
		DocumentoTransferenciaSalida transferenciaSalida = transferenciaSalidaOpt.get();
		DocumentoGuiaRemision guiaRemisionActualizar;
		List<DocumentoGuiaRemision> guias = guiaRemisionServicio.buscarPorDocumentoPadreId(transferenciaSalida.getId());
		Optional<DocumentoGuiaRemision> guiaRemisionOptional = guias.stream().findFirst();
		if(guiaRemisionOptional.isEmpty()) {
			guiaRemisionActualizar = new DocumentoGuiaRemision();
			
			guiaRemisionActualizar.setTipo(TipoDocumento.GUIA_REMISION);
			guiaRemisionActualizar.setCotizacion(transferenciaSalida.getCotizacion());
			guiaRemisionActualizar.setBodegaPartida(transferenciaSalida.getBodegaOrigen());
			guiaRemisionActualizar.setDocumentoPadreId(transferenciaSalida.getId());			
		}else {
			guiaRemisionActualizar = guiaRemisionOptional.get();
		}
		
		if(transferenciaSalida.getEstado() == DocumentoEstado.COMPLETADO)
			guiaRemisionActualizar.setEstado(DocumentoEstado.EMITIDO);
		else
			guiaRemisionActualizar.setEstado(DocumentoEstado.NUEVO);
		
		guiaRemisionActualizar.setMotivo(guiaRemision.getMotivo());
		guiaRemisionActualizar.setPlaca(guiaRemision.getPlaca());
		guiaRemisionActualizar.setRuta(guiaRemision.getRuta());
		guiaRemisionActualizar.setCorreo(guiaRemision.getCorreo());
		guiaRemisionActualizar.setCedulaConductor(guiaRemision.getCedulaConductor());
		guiaRemisionActualizar.setDireccionPartida(guiaRemision.getDireccionPartida());
		guiaRemisionActualizar.setDireccionEntega(guiaRemision.getDireccionEntega());
		guiaRemisionActualizar.setFechaInicioTraslado(guiaRemision.getFechaInicioTraslado());
		guiaRemisionActualizar.setFechaFinTraslado(guiaRemision.getFechaFinTraslado());
		guiaRemisionActualizar.setNombreConductor(guiaRemision.getNombreConductor());
		if(transferenciaSalida.getEstado() == DocumentoEstado.COMPLETADO) {
			if(CollectionUtils.isNotEmpty(guiaRemisionActualizar.getDetalle()))
				guiaRemisionActualizar.getDetalle().clear();
			else
				guiaRemisionActualizar.setDetalle(new ArrayList<DocumentoDetalle>());
			
			guiaRemisionActualizar.getDetalle().addAll(crearDetalleGuiaRemision(transferenciaSalida));
		}
		guiaRemisionServicio.save(guiaRemisionActualizar);
		LOG.info(String.format("Se crea, actualiza la guia de remision %s", gsonLog.toJson(guiaRemisionActualizar)));
		return guiaRemisionActualizar;
	}
	
	private List<DocumentoDetalle> crearDetalleGuiaRemision(DocumentoBase documento){
		return documento.getDetalle().stream().map(d -> {
			DocumentoDetalle detalle = new DocumentoDetalle(d.getCotizacionDetalle(), d.getCodigoBodega(), d.getCantidad());
			detalle.setSaldo(BigDecimal.ZERO);
			return d;
		})
		.collect(Collectors.toList());
	}
	
	@Override
	public DocumentoDetalle obtenerDocumentoDetallePorIdConCompartimientos(long documentoDetalleId) {
		DocumentoDetalle documentodetalle;		
		Optional<DocumentoDetalle> optionalDetalle = this.documentoDetalleRepositorio.findById(documentoDetalleId);
		if(optionalDetalle.isPresent()) {
			documentodetalle = optionalDetalle.get();
			documentodetalle.getCompartimientos();
		} else
			documentodetalle = new DocumentoDetalle();
		return documentodetalle;
	}

	@Override
	@Transactional
	public void actualizarDetalleTransferenciaSalida(long transferenciaSalidaId, DocumentoDetalle documentoDetalle) {
		Optional<DocumentoTransferenciaSalida> transferenciaSalidaOpt = transferenciaSalidaRepositorio.findById(transferenciaSalidaId);
		if(transferenciaSalidaOpt.isPresent()) {
			LOG.info(String.format("Se actualiza el detalle de transferencia de salida  %d datos: %s", transferenciaSalidaId, gsonLog.toJson(documentoDetalle)));
			DocumentoTransferenciaSalida transferenciaSalida = transferenciaSalidaOpt.get();
//			if(CollectionUtils.isNotEmpty(transferenciaSalida.getDetalle())) 
//				transferenciaSalida.getDetalle().stream()
//						.filter(d -> d.getId() == documentoDetalle.getId()).findFirst()
//						.ifPresent(d -> this.articuloServicio.liberarReservasCompartimientos(d));
			
			transferenciaSalida.actualizarLineaDetalle(documentoDetalle);
			transferenciaSalidaRepositorio.save(transferenciaSalida);
			LOG.info(String.format("Transferencia actualizada: %s", gsonLog.toJson(transferenciaSalida)));
//			articuloServicio.reservarArticuloCompartimiento(documentoDetalle);
		}
		
	}
	
	@Override
	@Transactional
	public void actualizarDetalleTransferenciaEntrada(long transferenciaEntradaId, DocumentoDetalle documentoDetalle) {
		Optional<DocumentoTransferenciaEntrada> transferenciaEntradaOpt = transferenciaEntradaRepositorio.findById(transferenciaEntradaId);
		if(transferenciaEntradaOpt.isPresent()) {
			LOG.info(String.format("Se actualiza el detalle de transferencia de salida  %d datos: ", transferenciaEntradaId, gsonLog.toJson(documentoDetalle)));
			DocumentoTransferenciaEntrada transferenciaEntrada = transferenciaEntradaOpt.get();
			
			transferenciaEntrada.actualizarLineaDetalle(documentoDetalle);
			transferenciaEntradaRepositorio.save(transferenciaEntrada);	
			LOG.info(String.format("Transferencia actualizada: %s", gsonLog.toJson(transferenciaEntrada)));
		}
	}	
	
	@Transactional
	private void generarCompartimientosTransferencia(DocumentoBase transferencia) {
		if(transferencia instanceof DocumentoTransferenciaEntrada)
			generarCompartimientosTransferenciaEntrada(transferencia.getDetalle());
		
		if(transferencia instanceof DocumentoTransferenciaSalida)
			generarCompartimientosTransferenciaSalida(transferencia.getDetalle());
	}
	
	@Transactional
	private void generarCompartimientosTransferenciaEntrada(List<DocumentoDetalle> detalles) {
		detalles.parallelStream().forEach(d -> {			
			if(!CollectionUtils.isEmpty(d.getCompartimientos()))
				d.getCompartimientos().clear();
				
		});
		
		detalles.forEach(d -> {
			String nombreBin = compartimientoServicio.ObtenerCompartimientoPorCodigoArticuloYBodega(d.getCotizacionDetalle().getCodigoArticulo(), d.getCodigoBodega());
			DocumentoDetalleCompartimiento compartimiento = new DocumentoDetalleCompartimiento(d.getCantidad(), nombreBin);
			d.agregarCompartimiento(compartimiento);
		});
	}
	
	@Transactional
	private void generarCompartimientosTransferenciaSalida(List<DocumentoDetalle> detalles) {
		detalles.parallelStream().forEach(d -> d.getCompartimientos().removeIf(c -> c.getCantidad().compareTo(BigDecimal.ZERO) == 0));
		detalles.forEach(d -> articuloServicio.liberarReservasCompartimientos(d));
		detalles.forEach(d -> d.getCompartimientos().clear());
		
		detalles.forEach(d -> this.articuloServicio.agregarCompartimientoADetalle(d));
	}
	
	@Transactional(readOnly = false )
	private DocumentoTransferencia validarTransferenciaCompleta(DocumentoBase documento) {
		DocumentoTransferenciaSalida transferenciaSalida = (DocumentoTransferenciaSalida)documento;		
		
		DocumentoTransferencia transferencia = this.transferenciaRepositorio.findById(transferenciaSalida.getDocumentoTransferenciaId()).get();
		LOG.info(String.format("Validando transferencia de salida completa: %s - %s", gsonLog.toJson(transferenciaSalida), gsonLog.toJson(transferencia)));
		List<DocumentoEstado> estadosRestar = Arrays.asList(DocumentoEstado.NUEVO, DocumentoEstado.ERROR);
		if(estadosRestar.contains(documento.getEstado())) {
			transferencia.getDetalle().parallelStream().forEach(dt -> {
				Optional<DocumentoDetalle> detalleOpt = documento.getDetalle().stream()
						.filter(detalleT -> detalleT.getCotizacionDetalle().getId() == dt.getCotizacionDetalle().getId())
						.findFirst();
				
				if(detalleOpt.isPresent()) {
					DocumentoDetalle dd = detalleOpt.get();
					dt.setSaldo(dt.getSaldo().subtract(dd.getCantidad()));
				}
				
			});
		}
		
		BigDecimal saldoTransferencia = transferencia.getDetalle().stream().map(DocumentoDetalle::getSaldo).reduce(BigDecimal.ZERO, BigDecimal::add);
		
		if((saldoTransferencia.compareTo(BigDecimal.ZERO) == 0)) {			
			transferencia.setEstado(DocumentoEstado.COMPLETADO);			
		}
		return transferencia;
	}
	
	@Transactional
	private void completarTransferenciaIngreso(DocumentoBase documento) {
		DocumentoTransferenciaEntrada transferenciaEntrada = (DocumentoTransferenciaEntrada)documento;
		DocumentoTransferenciaSalida transferenciaSalida = transferenciaSalidaRepositorio.findById(transferenciaEntrada.getDocumentoTransferenciaSalidaId()).get();
		transferenciaEntrada.setEstado(DocumentoEstado.COMPLETADO);
		transferenciaSalida.setEstado(DocumentoEstado.COMPLETADO);
		
		LOG.info(String.format("Completando transferencia de ingreso %s - %s", gsonLog.toJson(transferenciaEntrada), gsonLog.toJson(transferenciaSalida)));
		
		transferenciaSalida.getDetalle().parallelStream().forEach(d -> {
			Optional<DocumentoDetalle> detalleOpt = transferenciaEntrada.getDetalle().stream()
					.filter(det -> det.getCotizacionDetalle().getId() == d.getCotizacionDetalle().getId())
					.findFirst();
			if(detalleOpt.isPresent())
				d.setSaldo(d.getSaldo().subtract(detalleOpt.get().getCantidad()));
		});
		
		transferenciaSalidaRepositorio.save(transferenciaSalida);

	}

	@Override
	@Transactional(readOnly = true)
	public DocumentoBase obtenerDocumentoGuiaRemisionPorTransferenciaSalidaId(long transferenciaSalidaId) {
		List<DocumentoGuiaRemision> guias = guiaRemisionServicio.buscarPorDocumentoPadreId(transferenciaSalidaId);
		Optional<DocumentoGuiaRemision> guiaRemisionOpt = guias.stream().findFirst();
		if(guiaRemisionOpt.isEmpty())
			return new DocumentoGuiaRemision();
		guiaRemisionOpt.get().setCotizacion(null);
		return guiaRemisionOpt.get();
	}
	
	@Override
	@Transactional(readOnly = true)
	public byte[] generarReporteTransferenciaSalida(long transferenciaSalidaId) {
		byte[] report;
		try {
			DocumentoTransferenciaSalida transferencia = transferenciaSalidaRepositorio.findById(transferenciaSalidaId).get();
			DocumentoGuiaRemision guiaRemision = guiaRemisionServicio.buscarPorDocumentoPadreId(transferenciaSalidaId).stream().findFirst().get();			
			
			guiaRemision.setNumero(transferencia.getNumero());
			guiaRemision.setFechaEmision(transferencia.getFechaEmision());
			guiaRemision.setDetalle(transferencia.getDetalle());
			
			report = generarReporteServicio.generarReporte("GuiaTransferenciaSalida", Collections.singleton(guiaRemision), null);
		} catch (JasperReportsException e) {
			LOG.error(String.format("Error al generar el documento de transferencia de salida con id %d", transferenciaSalidaId));
			report = null;
		}
		return report;
	}
	
	@Override
	@Transactional(readOnly = true)
	public byte[] generarReporteTransferenciaEntrada(long transferenciaEntradaId) {
		byte[] report;
		try {
			DocumentoTransferenciaEntrada tEntrada = transferenciaEntradaRepositorio.findById(transferenciaEntradaId).get();
			DocumentoTransferenciaSalida transferencia = transferenciaSalidaRepositorio.findById(tEntrada.getDocumentoTransferenciaSalidaId()).get();
			
			SiteSetup siteOrigen = bodegaservicio.listarBodegaPorLocnCode(tEntrada.getBodegaOrigen());
			SiteSetup siteDestino = bodegaservicio.listarBodegaPorLocnCode(tEntrada.getBodegaDestino());
			tEntrada.setDescBodegaOrigen(siteOrigen.getLocnDscr());
			tEntrada.setDescBodegaDestino(siteDestino.getLocnDscr());
			
			tEntrada.getDetalle().parallelStream().forEach(dte -> {
				Optional<DocumentoDetalle> detalleOpt = transferencia.getDetalle().stream().filter(d -> d.getCodigoArticulo().equals(dte.getCodigoArticulo())).findFirst();
				if(detalleOpt.isPresent())
					dte.setSaldo(detalleOpt.get().getSaldo());
				else
					dte.setSaldo(BigDecimal.ZERO);				
			});;
			
			report = generarReporteServicio.generarReporte("GuiaTransferenciaEntrada", Collections.singleton(tEntrada), null);
		} catch (JasperReportsException e) {
			LOG.error(String.format("Error al generar el documento de transferencia de entrada con id %d", transferenciaEntradaId));
			report = null;
		}
		return report;
	}
	
	@Override
	@Transactional(readOnly = true)
	public byte[] generarReporteTransferencia(long transferenciaId) {
		byte[] report;
		try {
			DocumentoTransferencia transferencia = transferenciaRepositorio.findById(transferenciaId).get();
			
			SiteSetup siteOrigen = bodegaservicio.listarBodegaPorLocnCode(transferencia.getBodegaOrigen());
			SiteSetup siteDestino = bodegaservicio.listarBodegaPorLocnCode(transferencia.getBodegaDestino());
			transferencia.setDescBodegaOrigen(siteOrigen.getLocnDscr());
			transferencia.setDescBodegaDestino(siteDestino.getLocnDscr());
			
			report = generarReporteServicio.generarReporte("GuiaTransferencia", Collections.singleton(transferencia), null);
		} catch (JasperReportsException e) {
			LOG.error(String.format("Error al generar el documento de transferencia %d", transferenciaId));
			report = null;
		}
		return report;
	}
	
	@Override
	public List<TransferenciaConsultaDTO> listarTodasPorCotizacionID(long cotizacionID) {
		List<TransferenciaConsultaDTO> dto = new ArrayList<>();
		
		List<DocumentoTransferencia> transferencias = transferenciaRepositorio.findByCotizacion_IdOrderByModificadoPorAsc(cotizacionID);
		
		transferencias.forEach(x -> {
			dto.add(new TransferenciaConsultaDTO(x.getId(), x.getNumero(), x.getEstado(),""));
		});
		
		return dto;
	}

	@Override
	public Page<TransferenciaDTO> consultarTransferencia(Pageable pageable, TransferenciaConsulta consulta){
		
		switch (consulta.getTipoTransferencia()) {
		case TRANSFERENCIA:
			return consultarTransferenciaPadre(pageable, consulta);			
			
		case TRANSFERENCIA_INGRESO:
			return consultarTransferenciaEntrada(pageable, consulta);
			
		case TRANSFERENCIA_SALIDA:
			return consultarTransferenciaSalida(pageable, consulta);

		default:
			return new PageImpl<TransferenciaDTO>(new ArrayList<TransferenciaDTO>(), pageable, 0);
		}
		
	}
	
	
	
	@Transactional(readOnly = true)
	private Page<TransferenciaDTO> consultarTransferenciaPadre(Pageable pageable, TransferenciaConsulta consulta){
		
		String bodegaPrincipal = bodegaservicio.obtenerCodigoBodegaPrincipalPorUsuarioIdPerfilIdPuntoVentaId(consulta.getUsuarioId(), consulta.getPerfil().getId(), consulta.getPuntoVentaId());
		if(StringUtils.isBlank(bodegaPrincipal)) {
			Page<TransferenciaDTO> pageResut = new PageImpl<TransferenciaDTO>(new ArrayList<TransferenciaDTO>(), pageable, 0);
			return pageResut;
		}else {
			consulta.setBodega(bodegaPrincipal);
			
			CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
			CriteriaQuery<DocumentoTransferencia> query = criteriaBuilder.createQuery(DocumentoTransferencia.class);
			Root<DocumentoTransferencia> transferencia = query.from(DocumentoTransferencia.class);
			List<Predicate> predicadosConsulta = new ArrayList<Predicate>();
			
			if(!StringUtils.isBlank(consulta.getBodega()))
				predicadosConsulta.add(criteriaBuilder.equal(transferencia.get("bodegaOrigen"), consulta.getBodega()));
			
			if(!StringUtils.isBlank(consulta.getBodegaTransaccion()))
				predicadosConsulta.add(criteriaBuilder.equal(transferencia.get("bodegaDestino"), consulta.getBodegaTransaccion()));
			
			if(!StringUtils.isEmpty(consulta.getNumeroDocumento()))
				predicadosConsulta.add(criteriaBuilder.like(transferencia.get("numero"), String.format("%%%s%%", consulta.getNumeroDocumento())));
			
			if(!StringUtils.isEmpty(consulta.getNumeroCotizacion()))
				predicadosConsulta.add(criteriaBuilder.like(transferencia.get("cotizacion").get("numero"), String.format("%%%s%%", consulta.getNumeroCotizacion())));
			
			if(!StringUtils.isEmpty(consulta.getNumeroReferencia()))
				predicadosConsulta.add(criteriaBuilder.like(transferencia.get("referencia"), String.format("%%%s%%", consulta.getNumeroReferencia())));
			
			if(consulta.getEstado() != null)
				predicadosConsulta.add(criteriaBuilder.equal(transferencia.get("estado"), consulta.getEstado()));
			
			if (consulta.getFechaInicio() != null && consulta.getFechaFin() == null)
				consulta.setFechaFin(consulta.getFechaInicio().plusDays(0));
			
			if (consulta.getFechaInicio() == null && consulta.getFechaFin() != null) {
				consulta.setFechaInicio(consulta.getFechaFin());
				consulta.setFechaFin(consulta.getFechaInicio().plusDays(0));
			}
			
			if(consulta.getFechaInicio() != null && consulta.getFechaFin() != null) {
				consulta.setFechaFin(consulta.getFechaFin().plusDays(1));
				predicadosConsulta.add(criteriaBuilder.between(transferencia.get("fechaEmision"), consulta.getFechaInicio(), consulta.getFechaFin()));
			}
			
			Predicate[] predicados = new Predicate[predicadosConsulta.size()];
			predicados = predicadosConsulta.toArray(predicados);
			query.where(predicados).orderBy(criteriaBuilder.desc(transferencia.get("id")));
			
			TypedQuery<DocumentoTransferencia> statement = entityManager.createQuery(query);
			
			List<DocumentoTransferencia> transferencias = statement.getResultList();
			
			int sizeTotal = transferencias.size();
			
			int start = (int)pageable.getOffset();
			int end = (start + pageable.getPageSize()) > transferencias.size() ? transferencias.size() : (start + pageable.getPageSize());
			
			transferencias = transferencias.subList(start, end);
			
			List<TransferenciaDTO> resultadoConsulta = transferencias.stream().map(t -> {
				TransferenciaDTO transferenciaDto = new TransferenciaDTO();
				transferenciaDto.setId(t.getId());
				transferenciaDto.setNumero(t.getNumero());
				transferenciaDto.setCotizacionNumero(t.getCotizacion() != null? t.getCotizacion().getNumero(): "");
				transferenciaDto.setFechaEmision(t.getFechaEmision());
				transferenciaDto.setBodegaOrigen(t.getBodegaOrigen());
				transferenciaDto.setBodegaDestino(t.getBodegaDestino());
				transferenciaDto.setEstado(t.getEstado());
				transferenciaDto.setTipo(t.getTipo());
				return transferenciaDto;
			}).collect(Collectors.toList());
			
			Page<TransferenciaDTO> pageResut = new PageImpl<TransferenciaDTO>(resultadoConsulta, pageable, sizeTotal);
			
			return pageResut;
			
		}
		
	}
	
	@Transactional(readOnly = true)
	private Page<TransferenciaDTO> consultarTransferenciaEntrada(Pageable pageable, TransferenciaConsulta consulta){
		
		String bodegaPrincipal = bodegaservicio.obtenerCodigoBodegaPrincipalPorUsuarioIdPerfilIdPuntoVentaId(consulta.getUsuarioId(), consulta.getPerfil().getId(), consulta.getPuntoVentaId());
		if(StringUtils.isBlank(bodegaPrincipal)) {
			Page<TransferenciaDTO> pageResut = new PageImpl<TransferenciaDTO>(new ArrayList<TransferenciaDTO>(), pageable, 0);
			return pageResut;
		}else {
			consulta.setBodega(bodegaPrincipal);
			
			CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
			CriteriaQuery<DocumentoTransferenciaEntrada> query = criteriaBuilder.createQuery(DocumentoTransferenciaEntrada.class);
			Root<DocumentoTransferenciaEntrada> transferencia = query.from(DocumentoTransferenciaEntrada.class);
			List<Predicate> predicadosConsulta = new ArrayList<Predicate>();
			
			if(!StringUtils.isBlank(consulta.getBodega()))
				predicadosConsulta.add(criteriaBuilder.equal(transferencia.get("bodegaDestino"), consulta.getBodega()));
			
			if(!StringUtils.isBlank(consulta.getBodegaTransaccion()))
				predicadosConsulta.add(criteriaBuilder.equal(transferencia.get("bodegaOrigen"), consulta.getBodegaTransaccion()));
			
			if(!StringUtils.isEmpty(consulta.getNumeroDocumento()))
				predicadosConsulta.add(criteriaBuilder.like(transferencia.get("numero"), String.format("%%%s%%", consulta.getNumeroDocumento())));
			
			if(!StringUtils.isEmpty(consulta.getNumeroCotizacion()))
				predicadosConsulta.add(criteriaBuilder.like(transferencia.get("cotizacion").get("numero"), String.format("%%%s%%", consulta.getNumeroCotizacion())));
			
			if(!StringUtils.isEmpty(consulta.getNumeroReferencia()))
				predicadosConsulta.add(criteriaBuilder.like(transferencia.get("referencia"), String.format("%%%s%%", consulta.getNumeroReferencia())));
			
			if(consulta.getEstado() != null)
				predicadosConsulta.add(criteriaBuilder.equal(transferencia.get("estado"), consulta.getEstado()));
			
			if (consulta.getFechaInicio() != null && consulta.getFechaFin() == null)
				consulta.setFechaFin(consulta.getFechaInicio().plusDays(0));
			
			if (consulta.getFechaInicio() == null && consulta.getFechaFin() != null) {
				consulta.setFechaInicio(consulta.getFechaFin());
				consulta.setFechaFin(consulta.getFechaInicio().plusDays(0));
			}
			
			if(consulta.getFechaInicio() != null && consulta.getFechaFin() != null) {
				consulta.setFechaFin(consulta.getFechaFin().plusDays(1));
				predicadosConsulta.add(criteriaBuilder.between(transferencia.get("fechaEmision"), consulta.getFechaInicio(), consulta.getFechaFin()));
			}
			
			Predicate[] predicados = new Predicate[predicadosConsulta.size()];
			predicados = predicadosConsulta.toArray(predicados);
			query.where(predicados).orderBy(criteriaBuilder.desc(transferencia.get("id")));
			
			TypedQuery<DocumentoTransferenciaEntrada> statement = entityManager.createQuery(query);
			
			List<DocumentoTransferenciaEntrada> transferencias = statement.getResultList();
			
			int sizeTotal = transferencias.size();
			
			int start = (int)pageable.getOffset();
			int end = (start + pageable.getPageSize()) > transferencias.size() ? transferencias.size() : (start + pageable.getPageSize());
			
			transferencias = transferencias.subList(start, end);
			
			List<TransferenciaDTO> resultadoConsulta = transferencias.stream().map(t -> {
				DocumentoTransferenciaSalida transferenciaPadre = transferenciaSalidaRepositorio.findById(t.getDocumentoTransferenciaSalidaId()).orElse(null);
				TransferenciaDTO transferenciaDto = new TransferenciaDTO();
				transferenciaDto.setId(t.getId());
				transferenciaDto.setNumero(t.getNumero());
				transferenciaDto.setCotizacionNumero(t.getCotizacion() != null ? t.getCotizacion().getNumero() : "");
				transferenciaDto.setFechaEmision(t.getFechaEmision());
				transferenciaDto.setBodegaOrigen(t.getBodegaOrigen());
				transferenciaDto.setBodegaDestino(t.getBodegaDestino());
				transferenciaDto.setEstado(t.getEstado());
				transferenciaDto.setTipo(t.getTipo());
				transferenciaDto.setTipoTransferencia(transferenciaPadre != null
						? transferenciaPadre.isReposicion() ? TipoTransferencia.REPOSICION : TipoTransferencia.VENTA
						: null);
				transferenciaDto.setNumeroTransferenciaPadre(transferenciaPadre != null ? transferenciaPadre.getNumero() : "");
				return transferenciaDto;
			}).collect(Collectors.toList());
			
			Page<TransferenciaDTO> pageResut = new PageImpl<TransferenciaDTO>(resultadoConsulta, pageable, sizeTotal);
			
			return pageResut;
			
		}
		
	}
	
	
	@Transactional(readOnly = true)
	private Page<TransferenciaDTO> consultarTransferenciaSalida(Pageable pageable, TransferenciaConsulta consulta){
		
		String bodegaPrincipal = bodegaservicio.obtenerCodigoBodegaPrincipalPorUsuarioIdPerfilIdPuntoVentaId(consulta.getUsuarioId(), consulta.getPerfil().getId(), consulta.getPuntoVentaId());
		if(StringUtils.isBlank(bodegaPrincipal)) {
			Page<TransferenciaDTO> pageResut = new PageImpl<TransferenciaDTO>(new ArrayList<TransferenciaDTO>(), pageable, 0);
			return pageResut;
		}else {
			consulta.setBodega(bodegaPrincipal);
			
			CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
			CriteriaQuery<DocumentoTransferenciaSalida> query = criteriaBuilder.createQuery(DocumentoTransferenciaSalida.class);
			Root<DocumentoTransferenciaSalida> transferencia = query.from(DocumentoTransferenciaSalida.class);
			List<Predicate> predicadosConsulta = new ArrayList<Predicate>();
			
			if(!StringUtils.isBlank(consulta.getBodega()))
				predicadosConsulta.add(criteriaBuilder.equal(transferencia.get("bodegaOrigen"), consulta.getBodega()));
			
			if(!StringUtils.isBlank(consulta.getBodegaTransaccion()))
				predicadosConsulta.add(criteriaBuilder.equal(transferencia.get("bodegaDestino"), consulta.getBodegaTransaccion()));
			
			if(!StringUtils.isEmpty(consulta.getNumeroDocumento()))
				predicadosConsulta.add(criteriaBuilder.like(transferencia.get("numero"), String.format("%%%s%%", consulta.getNumeroDocumento())));
			
			if(!StringUtils.isEmpty(consulta.getNumeroCotizacion()))
				predicadosConsulta.add(criteriaBuilder.like(transferencia.get("cotizacion").get("numero"), String.format("%%%s%%", consulta.getNumeroCotizacion())));
			
			if(!StringUtils.isEmpty(consulta.getNumeroReferencia()))
				predicadosConsulta.add(criteriaBuilder.like(transferencia.get("referencia"), String.format("%%%s%%", consulta.getNumeroReferencia())));
			
			if(consulta.getEstado() != null)
				predicadosConsulta.add(criteriaBuilder.equal(transferencia.get("estado"), consulta.getEstado()));
			
			if (consulta.getFechaInicio() != null && consulta.getFechaFin() == null)
				consulta.setFechaFin(consulta.getFechaInicio().plusDays(0));
			
			if (consulta.getFechaInicio() == null && consulta.getFechaFin() != null) {
				consulta.setFechaInicio(consulta.getFechaFin());
				consulta.setFechaFin(consulta.getFechaInicio().plusDays(0));
			}
			
			if(consulta.getFechaInicio() != null && consulta.getFechaFin() != null) {
				consulta.setFechaFin(consulta.getFechaFin().plusDays(1));
				predicadosConsulta.add(criteriaBuilder.between(transferencia.get("fechaEmision"), consulta.getFechaInicio(), consulta.getFechaFin()));
			}
			
			Predicate[] predicados = new Predicate[predicadosConsulta.size()];
			predicados = predicadosConsulta.toArray(predicados);
			query.where(predicados).orderBy(criteriaBuilder.desc(transferencia.get("id")));
			
			TypedQuery<DocumentoTransferenciaSalida> statement = entityManager.createQuery(query);
			
			List<DocumentoTransferenciaSalida> transferencias = statement.getResultList();
			
			int sizeTotal = transferencias.size();
			
			int start = (int)pageable.getOffset();
			int end = (start + pageable.getPageSize()) > transferencias.size() ? transferencias.size() : (start + pageable.getPageSize());
			
			transferencias = transferencias.subList(start, end);
			
			List<TransferenciaDTO> resultadoConsulta = transferencias.stream().map(t -> {
				DocumentoTransferencia transferenciaPadre = transferenciaRepositorio.findById(t.getDocumentoTransferenciaId()).orElse(null);
				TransferenciaDTO transferenciaDto = new TransferenciaDTO();
				transferenciaDto.setId(t.getId());
				transferenciaDto.setNumero(t.getNumero());
				transferenciaDto.setCotizacionNumero(t.getCotizacion() != null ? t.getCotizacion().getNumero() : "");
				transferenciaDto.setFechaEmision(t.getFechaEmision());
				transferenciaDto.setBodegaOrigen(t.getBodegaOrigen());
				transferenciaDto.setBodegaDestino(t.getBodegaDestino());
				transferenciaDto.setEstado(t.getEstado());
				transferenciaDto.setTipo(t.getTipo());
				transferenciaDto.setTipoTransferencia(t.isReposicion()? TipoTransferencia.REPOSICION: TipoTransferencia.VENTA);
				transferenciaDto.setNumeroTransferenciaPadre(transferenciaPadre != null ? transferenciaPadre.getNumero(): "");
				return transferenciaDto;
			}).collect(Collectors.toList());
			
			Page<TransferenciaDTO> pageResut = new PageImpl<TransferenciaDTO>(resultadoConsulta, pageable, sizeTotal);
			
			return pageResut;
			
		}
		
	}

	@Override
	public ReposicionDTO sugerirReposicion(ReposicionSugerenciaDTO dto) {
		
		DocumentoTransferencia transferencia = null;
		
		Optional<DocumentoTransferencia> transferenciaOP = transferenciaRepositorio.findByBodegaOrigenAndBodegaDestinoAndTipoTransferenciaAndEstado(dto.getBodegaOrigen(), dto.getBodegaDestino(), TipoTransferencia.REPOSICION, DocumentoEstado.NUEVO);
		
		if (transferenciaOP.isPresent()) {
			
			transferencia = transferenciaOP.get();
			
			eliminarDetalleReposicion(transferencia);
			
		} else {
			
			transferencia = new DocumentoTransferencia(dto.getBodegaOrigen(), dto.getBodegaDestino());
		}
		
		ReposicionDTO reposicionDTO = new ReposicionDTO(transferencia.getId(),transferencia.getBodegaOrigen(), transferencia.getBodegaDestino(), transferencia.getNumero(),
				transferencia.getEstado(), LocalDate.now());
		
		List<ItemReplenishment> articulosReposicion = articuloServicio.obtenerReposicion(dto.getBodegaOrigen(), dto.getBodegaDestino());
		
		for (ItemReplenishment item : articulosReposicion) {
			
			agregarItemReposicion(transferencia, reposicionDTO, item);
		}
		
		if (transferencia.getDetalle().size() > 0) {
			
			if (!UtilidadesCadena.noEsNuloNiBlanco(transferencia.getNumero())) {
				
				transferencia.setNumero(obtenerSecuencialReposicion(dto.getBodegaDestino()));
				
				reposicionDTO.setNumero(transferencia.getNumero());
				
			}
			
			transferenciaRepositorio.save(transferencia);
			
			articuloServicio.reservarArticulosReposicion(transferencia.getDetalle());
			
			for (ReposicionDetalleDTO item : reposicionDTO.getDetalle()) {
				transferencia.getDetalle().forEach(x ->{
					if(item.getCodigoArticulo().equals(x.getCodigoArticulo()))
						item.setIdDetalle(x.getId());
				});
			}
			reposicionDTO.setIdReposicion(transferencia.getId());
			return reposicionDTO;
		}
		
		return null;
		
	}

	private void agregarItemReposicion(DocumentoTransferencia transferencia, ReposicionDTO reposicionDTO, ItemReplenishment item) {
		
		BigDecimal disponibleDestino = item.getStockDestination().subtract(item.getReserveDestination());
		
		disponibleDestino = disponibleDestino.add(calcularReposicionProceso(item.getItemnmbr(), transferencia.getBodegaDestino()));
		
		if (disponibleDestino.compareTo(item.getMinDestination()) <= 0) {
			
			BigDecimal cantidadReponer = item.getMaxDestination().subtract(disponibleDestino);
			
			BigDecimal disponibleOrigen = item.getStockOrigin().subtract(item.getReserveOrigin());
			
			if (disponibleOrigen.compareTo(item.getMinOrigin()) > 0) {
				
				BigDecimal cantidadReponerOrigen =  disponibleOrigen.subtract(item.getMinOrigin());
				
				if (cantidadReponerOrigen.compareTo(cantidadReponer) < 0)
					cantidadReponer = cantidadReponerOrigen;
				
				transferencia.agregarLineaDetalle(new DocumentoDetalle(transferencia.getBodegaOrigen(), cantidadReponer, item.getItemnmbr(), item.getItmshnam(), item.getUscatvls_2(), item.getItemdesc(), item.getItemshwt(), item.getUomschdl()));
				
				reposicionDTO.agregarLineaDetalle(new ReposicionDetalleDTO(item.getItemnmbr(), item.getItemdesc(), item.getItmshnam(), item.getUomschdl(), transferencia.getBodegaOrigen(), transferencia.getBodegaDestino(), disponibleOrigen, disponibleDestino, cantidadReponer, item.getItemshwt(), item.getBinOrigin()));
				
			}
		}
	}
	
	private void eliminarDetalleReposicion (DocumentoTransferencia transferencia) {
		
		if (transferencia.getTipoTransferencia().equals(TipoTransferencia.REPOSICION) && transferencia.getEstado().equals(DocumentoEstado.NUEVO)) {
			
			articuloServicio.liberarReservasArticulosReposicion(transferencia.getDetalle());
			
			List<DocumentoDetalle> tmp = new ArrayList<DocumentoDetalle>();
			tmp.addAll(transferencia.getDetalle());
			
			for (DocumentoDetalle detalle : tmp) {
				transferencia.eliminarLineaDetalle(detalle);
			}
			
			transferenciaRepositorio.save(transferencia);
		}
	}
	
	private BigDecimal calcularReposicionProceso(String articulo, String bodega) {
		
		List<DocumentoDetalle> transferencias = transferenciaRepositorio.findByBodegaDestinoAndTipoTransferenciaAndEstadoIn(bodega, TipoTransferencia.REPOSICION, Arrays.asList(DocumentoEstado.EMITIDO, DocumentoEstado.EN_PROCESO)).stream()
	              .flatMap(x -> x.getDetalle().stream())
	              .collect(Collectors.toList());
		
		List<DocumentoDetalle> transferenciasSalidas = transferenciaSalidaRepositorio.findByBodegaDestinoAndEstadoAndReposicionTrue(bodega, DocumentoEstado.EMITIDO).stream()
	              .flatMap(x -> x.getDetalle().stream())
	              .collect(Collectors.toList());
		
		BigDecimal saldoTransferencia = transferencias.stream().filter(x -> x.getCodigoArticulo().equals(articulo)).map(x -> x.getSaldo()).reduce(BigDecimal.ZERO, BigDecimal::add);
		
		BigDecimal cantidadTransferenciaSalida = transferenciasSalidas.stream().filter(x -> x.getCodigoArticulo().equals(articulo)).map(x -> x.getCantidad()).reduce(BigDecimal.ZERO, BigDecimal::add);
		
		return saldoTransferencia.add(cantidadTransferenciaSalida);
	}
	
	
	private String obtenerSecuencialReposicion(String codigoBodega) {
		String numero = "";
		List<PuntoVentaBodega> bodegasPrincipales = puntoVentaBodegaServicio
				.listarBodegasPrincipalesPorCodigo(codigoBodega);

		for (PuntoVentaBodega puntoVentaBodega : bodegasPrincipales) {
			Secuencial secuencial = secuencialServicio.ObtenerSecuencialPorPuntoVentaYTipoDocumento(
					puntoVentaBodega.getPuntoVenta().getId(), TipoDocumento.REPOSICION);
			if (secuencial != null) {
				numero = secuencial.getNumeroSecuencial();
				break;
			}
		}

		if (UtilidadesCadena.esNuloOBlanco(numero))
			throw new SecuancialNoExisteException(bodegasPrincipales.stream().map(x -> x.getPuntoVenta().getNombre())
					.collect(Collectors.joining(",")), TipoDocumento.REPOSICION.getDescripcion());

		return numero;
	}
	
	@Override
	@Transactional
	public void anularTransferenciaReposicion(long transferenciaId) {

		DocumentoTransferencia transferencia = transferenciaRepositorio.findById(transferenciaId).get();
		
		anularReposicion(transferencia);
	}
	
	@Transactional(readOnly = false, propagation = Propagation.REQUIRED)
	private void anularReposicion(DocumentoTransferencia transferencia) {
		
		if (transferencia.getTipoTransferencia().equals(TipoTransferencia.REPOSICION) && transferencia.getEstado().equals(DocumentoEstado.NUEVO)) {
			
			this.articuloServicio.liberarReservasArticulosReposicion(transferencia.getDetalle());

			transferencia.setEstado(DocumentoEstado.ANULADO);

			transferenciaRepositorio.save(transferencia);
			
		}
	}
	
	@Override
	@Transactional
	public void emitirTransferenciaReposicion(long transferenciaId) {
		
		DocumentoTransferencia transferencia = transferenciaRepositorio.findById(transferenciaId).get();
		
		if (transferencia.getTipoTransferencia().equals(TipoTransferencia.REPOSICION) && transferencia.getEstado().equals(DocumentoEstado.NUEVO)) {

			transferencia.setEstado(DocumentoEstado.EMITIDO);

			transferenciaRepositorio.save(transferencia);
			
		}
		
	}
	
	@Override
	public ArticuloReposicionDTO obtenerReposicionArticulo(ReposicionArticuloBodegaDTO articuloBodegaDTO) {
		
		Item item = articuloServicio.obtenerReposicionPorArticuloYBodega(articuloBodegaDTO.getCodigoArticulo(), articuloBodegaDTO.getCodigoBodega());
		
		if (item != null) {
			
			ArticuloReposicionDTO articuloReposicionDTO = new ArticuloReposicionDTO(item.getItemnmbr(), item.getItmgedsc(), item.getItmshnam(), item.getUomschdl(), item.getItemshwt(), item.getDisponible(), item.getReserva(), item.getMinimo(), item.getMaximo());
			
			if (articuloBodegaDTO.isBodegaOrigen())
				articuloReposicionDTO.setCantidadReserva(item.getReserva().subtract(articuloBodegaDTO.getCantidadReponer()));
			else
				articuloReposicionDTO.setCantidadProceso(calcularReposicionProceso(articuloBodegaDTO.getCodigoArticulo(), articuloBodegaDTO.getCodigoBodega()));
			
			articuloReposicionDTO.setCantidadTotal((articuloReposicionDTO.getCantidadDisponible().add(articuloReposicionDTO.getCantidadProceso()).subtract(articuloReposicionDTO.getCantidadReserva())));
				
			return articuloReposicionDTO;
		}
		
		return null;
		
	}
	
	@Scheduled(cron = "0 55 7 1/1 * ?")
	public void anularTransferenciasReposiciones() {
		
		LOG.info("Iniciando Tarea Programada: ANULAR TRANSFERENCIA REPOSICIÓN NUEVAS");
		
		List<DocumentoTransferencia> reposiciones = transferenciaRepositorio.findByTipoTransferenciaAndEstado(TipoTransferencia.REPOSICION, DocumentoEstado.NUEVO);
		
		for (DocumentoTransferencia transferencia : reposiciones) {
			anularReposicion(transferencia);
		}
		
		LOG.info("Finalizando Tarea Programada: ANULAR TRANSFERENCIA REPOSICIÓN NUEVAS");
	}

	@Override
	@Transactional
	public ReposicionRespuestaCambioLineaDTO actualizarCantidadReposicionDetalle(ReposicionDetalleDTO detalleDTO) {

		Optional<DocumentoDetalle> detalleRecargado = documentoDetalleRepositorio.findById(detalleDTO.getIdDetalle());
		if (detalleRecargado.isPresent()) {
			DocumentoDetalle detalle = detalleRecargado.get();
			ReposicionRespuestaCambioLineaDTO respuesta = new ReposicionRespuestaCambioLineaDTO();
			
			BigDecimal diferencia = detalleDTO.getCantidadReponer().subtract(detalle.getCantidad());

			DocumentoDetalle detalleTMP = new DocumentoDetalle(detalle.getCodigoBodega(), diferencia.abs(),
					detalle.getCodigoArticulo(), detalle.getCodigoArticuloAlterno(), detalle.getClaseArticulo(),
					detalle.getDescripcionArticulo(), detalle.getPesoArticulo(), detalle.getUnidadMedida());

			if (diferencia.compareTo(BigDecimal.ZERO) == -1) {
				validarCantidadDisponibleOrigen(respuesta, detalleDTO, detalle.getCantidad());	
				articuloServicio.liberarReservasArticulosReposicion(Arrays.asList(detalleTMP));
			}

			if (diferencia.compareTo(BigDecimal.ZERO) == 1) {
				
				validarCantidadDisponibleOrigen(respuesta, detalleDTO, detalle.getCantidad());				
				if(!respuesta.isError())
					articuloServicio.reservarArticulosReposicion(Arrays.asList(detalleTMP));
			}
			
			if(!respuesta.isError()) {
				
				detalle.setCantidad(detalleDTO.getCantidadReponer());
				detalle.setSaldo(detalleDTO.getCantidadReponer());
				LOG.info(String.format("Acualizando Linea Detalle Reposicion %s", gsonLog.toJson(detalle)));
			}else {
				LOG.info(String.format("No se pudo actualizar Linea Detalle Reposicion %s", gsonLog.toJson(detalle)));
				respuesta.setCantidadReponerOriginal(detalle.getCantidad());
			}
			
			return respuesta;
		}
		return null;
	}

	@Override
	public void eliminarDetalleReposicion(long detalleId) {
		Optional<DocumentoDetalle> detalle = documentoDetalleRepositorio.findById(detalleId);
		if (detalle.isPresent())
			articuloServicio.liberarReservasArticulosReposicion(Arrays.asList(detalle.get()));

		documentoDetalleRepositorio.deleteById(detalleId);
		LOG.info(String.format("Detalle Reposicion id= %s Eliminado", detalleId));
	}
	
	private void validarCantidadDisponibleOrigen(ReposicionRespuestaCambioLineaDTO respuesta,
			ReposicionDetalleDTO detalleDTO, BigDecimal cantidadReponer) {

		ArticuloReposicionDTO resultado = obtenerReposicionArticulo(
				new ReposicionArticuloBodegaDTO(detalleDTO.getCodigoArticulo(), detalleDTO.getBodegaOrigen(),
						Boolean.TRUE, cantidadReponer));
		
		if (resultado != null) {
			
			if (resultado.getCantidadTotal().compareTo(detalleDTO.getCantidadReponer()) == -1) {
				LOG.info(String.format(
						"Reposición: Cantidad a reponer %s no disponible para el artículo %s en Bodega %s cant. Disponible:%s",
						detalleDTO.getCantidadReponer(), detalleDTO.getCodigoArticulo(), detalleDTO.getBodegaOrigen(),
						resultado.getCantidadTotal()));
				respuesta.setError(Boolean.TRUE);				
			}
			respuesta.setNuevaCantidadDisponible(resultado.getCantidadTotal());
		}
	}

}
