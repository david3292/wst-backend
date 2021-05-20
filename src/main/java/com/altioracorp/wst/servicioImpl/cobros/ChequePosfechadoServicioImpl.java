package com.altioracorp.wst.servicioImpl.cobros;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import javax.mail.MessagingException;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.altioracorp.wst.dominio.RevisionEstado;
import com.altioracorp.wst.dominio.cobros.ChequePosfechado;
import com.altioracorp.wst.dominio.cobros.ChequePosfechadoAprobacionDTO;
import com.altioracorp.wst.dominio.cobros.ChequePosfechadoControles;
import com.altioracorp.wst.dominio.cobros.ChequePosfechadoDTO;
import com.altioracorp.wst.dominio.cobros.ChequePosfechadoEstado;
import com.altioracorp.wst.dominio.cobros.ChequePosfechadoReporteDTO;
import com.altioracorp.wst.dominio.cobros.ChequePosfechadoReporteDetalleDTO;
import com.altioracorp.wst.dominio.cobros.CobroChequePosfechadoAprobacionDTO;
import com.altioracorp.wst.dominio.cobros.CobroChequePosfechadoDTO;
import com.altioracorp.wst.dominio.cobros.CobroCuotaFactura;
import com.altioracorp.wst.dominio.cobros.CobroDTO;
import com.altioracorp.wst.dominio.cobros.CobroFormaPago;
import com.altioracorp.wst.dominio.cobros.SolicitarReporteChequePosfechadoDTO;
import com.altioracorp.wst.dominio.sistema.PerfilNombre;
import com.altioracorp.wst.dominio.sistema.Usuario;
import com.altioracorp.wst.exception.reporte.JasperReportsException;
import com.altioracorp.wst.exception.reporte.ReporteExeption;
import com.altioracorp.wst.repositorio.cobros.IChequePosfechadoControlesRepositorio;
import com.altioracorp.wst.repositorio.cobros.IChequePosfechadoRepositorio;
import com.altioracorp.wst.repositorio.cobros.ICobroCuotaFacturaRepositorio;
import com.altioracorp.wst.repositorio.sistema.IUsuarioRepositorio;
import com.altioracorp.wst.servicio.cobros.IChequePosfechadoServicio;
import com.altioracorp.wst.servicio.cobros.ICobroServicio;
import com.altioracorp.wst.servicio.notificaciones.INotificacionServicio;
import com.altioracorp.wst.servicio.reporte.IGeneradorJasperReports;
import com.altioracorp.wst.util.UtilidadesSeguridad;
import com.google.gson.Gson;

@Service
public class ChequePosfechadoServicioImpl implements IChequePosfechadoServicio {

	private static final Log LOG = LogFactory.getLog(ChequePosfechadoServicioImpl.class);

	@Autowired
	private IChequePosfechadoRepositorio chequePosfechadoRepositorio;
	
	@Autowired
	private ICobroCuotaFacturaRepositorio cobroCuotaFacturaRepositorio;
	
	@Autowired
	private Gson gsonLog;
	
	@Autowired
	private ICobroServicio cobroServicio;
	
	@Autowired
	private IChequePosfechadoControlesRepositorio chequeControlesRepositorio;
	
	@Autowired
	private INotificacionServicio notificacionServicio;
	
	@Autowired
	private IUsuarioRepositorio usuarioRepositorio;
	
	@Autowired
    private IGeneradorJasperReports reporteServicio;
	
	@Override
	public void registrar(List<CobroFormaPago> chequesAFecha, String codigoCliente, String nombreCliente, LocalDateTime fechaCobro) {
		List<ChequePosfechado> chequesAGuardar = new ArrayList<>();

		for (CobroFormaPago cobroFormaPago : chequesAFecha) {

			chequesAGuardar.add(new ChequePosfechado(cobroFormaPago, codigoCliente, nombreCliente, fechaCobro));
		}

		LOG.info(String.format("Cheques Posfechados a guardar %s", gsonLog.toJson(chequesAGuardar)));
		chequePosfechadoRepositorio.saveAll(chequesAGuardar);
	}

	@Override
	public List<ChequePosfechadoDTO> obtenerChequesPorEstadoRecibido() {

		List<ChequePosfechadoDTO> cheques = chequePosfechadoRepositorio
				.findByEstadoIn(Arrays.asList(ChequePosfechadoEstado.RECIBIDO)).stream().map(x -> {

					List<String> documentoAplicado = buscarAplicaciones(x.getCobroFormaPago().getId());

					return new ChequePosfechadoDTO(x.getId(), x.getCobroFormaPago().getId(),
							x.getCobroFormaPago().getNumeroDocumento(), x.getCobroFormaPago().getBanco(),
							x.getCobroFormaPago().getValor(), x.getFechaEfectivizacion().toLocalDate(), documentoAplicado,
							x.getObservacion(), x.getCodigoCliente(), x.getNombreCliente(), x.getFechaCobro().toLocalDate(), "");

				}).collect(Collectors.toList());

		return cheques;
	}

	@Override
	public List<ChequePosfechadoDTO> obtenerChequesPorEstadoRevision() {
		List<ChequePosfechadoDTO> cheques = chequeControlesRepositorio.findByEstadoIsNull().stream().map(x -> {

			List<String> documentoAplicado = buscarAplicaciones(x.getCheque().getCobroFormaPago().getId());

			return new ChequePosfechadoDTO(x.getId(), x.getCheque().getId(), x.getCheque().getCobroFormaPago().getId(),
					x.getCheque().getCobroFormaPago().getNumeroDocumento(),
					x.getCheque().getCobroFormaPago().getBanco(), x.getCheque().getCobroFormaPago().getValor(),
					x.getCheque().getFechaEfectivizacion().toLocalDate(), documentoAplicado, x.getDiasProrroga(), x.isCanje(),
					x.isFechaDiferente(),x.getCheque().getCodigoCliente(), x.getCheque().getNombreCliente(), x.getCheque().getFechaCobro().toLocalDate(), x.getCreadoPor());

		}).collect(Collectors.toList());

		return cheques;
	}
	
	private List<String> buscarAplicaciones(long cobroFormaPagoId) {
		List<CobroCuotaFactura> aplicaciones = cobroCuotaFacturaRepositorio.findByCobroFormaPagoId(cobroFormaPagoId);
		return aplicaciones.stream().map(y -> y.getNumeroFactura())
				.collect(Collectors.toList());
	}

	@Override
	@Transactional(noRollbackFor = { MessagingException.class })
	public List<CobroChequePosfechadoDTO> procesar(List<ChequePosfechadoDTO> chequesAProcesar) {
		List<CobroChequePosfechadoDTO> respuesta = new ArrayList<>();
		boolean notificarChequesPorAprobar = Boolean.FALSE;

		for (ChequePosfechadoDTO x : chequesAProcesar) {
			if (x.isRevision()) {

				Optional<ChequePosfechado> chequeOpcional = chequePosfechadoRepositorio
						.findById(x.getChequePosfechadoId());
				if (chequeOpcional.isPresent()) {
					ChequePosfechado cheque = chequeOpcional.get();
					cheque.setEstado(ChequePosfechadoEstado.REVISION);
					crearControlesCheque(cheque, x);
				}
				notificarChequesPorAprobar = Boolean.TRUE;
			} else {
				respuesta.add(this.efectivizarChequeResultado(x));
				this.cambiarEstadoYObservacion(x.getChequePosfechadoId(), ChequePosfechadoEstado.CERRADO, "");
			}
		}
		
		if (notificarChequesPorAprobar)
			notificacionServicio.notificarChequesPosfechadosPorAutorizar();

		return respuesta;
	}
	
	private void crearControlesCheque(ChequePosfechado cheque,ChequePosfechadoDTO dto ) {		
		ChequePosfechadoControles control = new ChequePosfechadoControles(cheque,dto.esFechaDiferente(), dto.isCanje(), dto.getDiasProrroga(), "");
		LOG.info(String.format("ChequePosfechado %s control a guardar %s", dto.getNumeroCheque() ,gsonLog.toJson(control)));
		chequeControlesRepositorio.save(control);
	}

	@Override
	@Transactional(noRollbackFor = { MessagingException.class })
	public List<CobroChequePosfechadoAprobacionDTO> procesarAprobacion(ChequePosfechadoAprobacionDTO aprobacionDTO) {

		List<CobroChequePosfechadoAprobacionDTO> resultado = new ArrayList<CobroChequePosfechadoAprobacionDTO>();

		List<ChequePosfechadoDTO> rechazados = new ArrayList<ChequePosfechadoDTO>();
		aprobacionDTO.getChequesAProcesar().forEach(x -> {
			switch (aprobacionDTO.getEstado()) {
			case APROBAR:

				if (x.isCanje()) {
					LOG.info(String.format("ChequePosfechado %s aprobado por canje", x.getNumeroCheque()));
					cobroServicio.canjearChequePosFechado(x.getCobroFormaPagoId());
					this.cambiarCanje(x.getChequePosfechadoId(), ChequePosfechadoEstado.CERRADO,
							aprobacionDTO.getObservacion());
					break;
				}

				if (x.getDiasProrroga() > 0) {
					LOG.info(String.format("ChequePosfechado %s aprobado por diasProrroga", x.getNumeroCheque()));
					
					this.cambiarFechaEfectizacion(x.getChequePosfechadoId(), ChequePosfechadoEstado.RECIBIDO,
							aprobacionDTO.getObservacion(), x.getDiasProrroga());
					break;
				}
				
				break;

			case RECHAZAR:
				LOG.info(String.format("ChequePosfechado %s rechazado por %s", x.getNumeroCheque(), aprobacionDTO.getObservacion()));
				this.cambiarEstadoYObservacion(x.getChequePosfechadoId(), ChequePosfechadoEstado.RECIBIDO,
						aprobacionDTO.getObservacion());
				rechazados.add(x);
				break;

			default:
				break;
			}
			cambiarControles(x.getChequePosfechadoControlesId(),aprobacionDTO.getEstado(), aprobacionDTO.getObservacion());
			resultado.add(new CobroChequePosfechadoAprobacionDTO(aprobacionDTO.getEstado(),x));
		});

		Map<String, List<ChequePosfechadoDTO>> agrupacion = agruparPorUsuarioAnalista(rechazados);
		agrupacion.forEach(( usuarioAnalista, cheques ) -> {
			notificacionServicio.notificarChequesPosfechadosRechazados(aprobacionDTO.getObservacion(), usuarioAnalista, cheques);
		});
		return resultado;
	}
	
	private Map<String, List<ChequePosfechadoDTO>> agruparPorUsuarioAnalista(List<ChequePosfechadoDTO> dto){
		Map<String, List<ChequePosfechadoDTO>> agrupadoPorAnalista = dto.stream()
				.collect(Collectors.groupingBy(ChequePosfechadoDTO::getUsuarioAnalista));
		return agrupadoPorAnalista;
	}
	
	
	@Transactional
	private void cambiarEstadoYObservacion(long chequePosfechadoId, ChequePosfechadoEstado estado, String observacion) {
		Optional<ChequePosfechado> chequeOpcional = chequePosfechadoRepositorio.findById(chequePosfechadoId);
		if (chequeOpcional.isPresent()) {
			ChequePosfechado cheque = chequeOpcional.get();
			cheque.setEstado(estado);
			cheque.setObservacion(observacion);
			chequePosfechadoRepositorio.save(cheque);
		}
	}
	
	@Transactional
	private void cambiarCanje(long chequePosfechadoId, ChequePosfechadoEstado estado, String observacion) {
		Optional<ChequePosfechado> chequeOpcional = chequePosfechadoRepositorio.findById(chequePosfechadoId);
		if (chequeOpcional.isPresent()) {
			ChequePosfechado cheque = chequeOpcional.get();
			cheque.setEstado(estado);
			cheque.setObservacion(observacion);
			cheque.setCanje(true);
			chequePosfechadoRepositorio.save(cheque);
		}
	}
	
	@Transactional
	private void cambiarFechaEfectizacion(long chequePosfechadoId, ChequePosfechadoEstado estado, String observacion, int diasProrroga) {
		Optional<ChequePosfechado> chequeOpcional = chequePosfechadoRepositorio.findById(chequePosfechadoId);
		if (chequeOpcional.isPresent()) {
			ChequePosfechado cheque = chequeOpcional.get();
			cheque.setEstado(estado);
			cheque.setObservacion(observacion);
			cheque.setDiasProrroga(diasProrroga);
			cheque.setFechaEfectivizacion(cheque.getFechaEfectivizacion().plusDays(diasProrroga));
			chequePosfechadoRepositorio.save(cheque);
		}
	}
	
	@Transactional
	private void cambiarControles(long chequePosfechadoControlesId, RevisionEstado estado, String observacion) {
		Optional<ChequePosfechadoControles> chequeOpcional = chequeControlesRepositorio.findById(chequePosfechadoControlesId);
		if (chequeOpcional.isPresent()) {
			ChequePosfechadoControles controles = chequeOpcional.get();
			controles.setEstado(estado);
			controles.setObservacion(observacion);
			chequeControlesRepositorio.save(controles);
		}
	}
	
	private CobroChequePosfechadoDTO efectivizarChequeResultado(ChequePosfechadoDTO x) {
		LOG.info(String.format("Procesando Cheque Posfechado %s aplicado a las facturas %s",
				x.getNumeroCheque(), x.getDocumentosAplicados()));
		
		CobroDTO cobro = cobroServicio.efectivizarChequePosFechado(x.getCobroFormaPagoId());
		return new CobroChequePosfechadoDTO(x.getChequePosfechadoId(), cobro.getNumero(),
				cobro.getCobroFormaPagos().stream().findFirst().get().getNumero(), x.getNumeroCheque(),
				cobro.getCobroFormaPagos().stream().findFirst().get().getEstado());
	}

	@Override
	public byte[] generarReporte(SolicitarReporteChequePosfechadoDTO solicitudReporte) {

		if (solicitudReporte.getChequesPosfechadoIds().isEmpty()) {
			return null;
		}
		String usuarioEnSesion = UtilidadesSeguridad.usuarioEnSesion();
		Usuario usuario = usuarioRepositorio.findByNombreUsuario(usuarioEnSesion).orElse(null);

		try {
			ChequePosfechadoReporteDTO dto = new ChequePosfechadoReporteDTO(usuario.getNombreCompleto(),
					usuarioEnSesion, LocalDateTime.now(),
					crearDetalleReporte(solicitudReporte.getChequesPosfechadoIds()));
			
			LOG.info(String.format("Generando Reporte ChequePosfechados para perfil %s", solicitudReporte.getPerfil()));			
			return reporteServicio.generarReporte(obtenerRutaReporte(solicitudReporte.getPerfil()),
					Collections.singleton(dto), new HashMap<>());

		} catch (JasperReportsException e) {
			LOG.error(String.format("Error ChequePosfechado Reporte: %s", e.getMessage()));
			throw new ReporteExeption("ChequePosfechado");
		}

	}

	private List<ChequePosfechadoReporteDetalleDTO> crearDetalleReporte(List<Long> chequesPosfechadoIds) {

		List<ChequePosfechadoReporteDetalleDTO> detalle = new ArrayList<ChequePosfechadoReporteDetalleDTO>();

		List<ChequePosfechado> chequesProcesados = StreamSupport
				.stream(chequePosfechadoRepositorio.findAllById(chequesPosfechadoIds).spliterator(), false)
				.collect(Collectors.toList());

		if (!chequesProcesados.isEmpty()) {

			chequesProcesados.forEach(x -> {

				ChequePosfechadoControles ultimoControl = obtenerUltimaRevicionControles(x.getId());

				detalle.add(new ChequePosfechadoReporteDetalleDTO(
						x.getCobroFormaPago().getFechaEfectivizacion().toLocalDate(),
						x.getCobroFormaPago().getNumeroDocumento(), x.getCobroFormaPago().getBanco(),
						x.getCodigoCliente(), x.getNombreCliente(), x.getCobroFormaPago().getNumeroCobroGP(),
						x.getCobroFormaPago().getValor(), buscarAplicaciones(x.getCobroFormaPago().getId()),
						ultimoControl == null ? false : ultimoControl.isCanje(),
						ultimoControl == null ? false : ultimoControl.getDiasProrroga() > 0 ? true : false,
						ultimoControl == null ? null : x.getFechaEfectivizacion().plusDays(ultimoControl.getDiasProrroga()).toLocalDate(),
						ultimoControl == null ? null : ultimoControl.getEstado(),
						ultimoControl == null ? null : ultimoControl.getObservacion()));
			});

		}

		return detalle;
	}
	
	private ChequePosfechadoControles obtenerUltimaRevicionControles(long chequePosfechadoId) {
		List<ChequePosfechadoControles> revisiones = chequeControlesRepositorio.findByCheque_IdOrderByCreadoFechaDesc(chequePosfechadoId);
		return revisiones.stream().findFirst().orElse(null);
	}
	
	private String obtenerRutaReporte(PerfilNombre perfil) {
		switch (perfil) {
		case ANALISTA_TESORERIA:
			return "ChequePosfechado\\AnalistaTesoreria";

		case JEFE_TESORERIA:
			return "ChequePosfechado\\JefeTesoreria";

		default:
			return "";
		}
	}

}
