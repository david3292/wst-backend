package com.altioracorp.wst.servicioImpl.cobros;

import java.math.BigDecimal;
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

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.altioracorp.wst.dominio.cobros.Caja;
import com.altioracorp.wst.dominio.cobros.CajaConsultaDTO;
import com.altioracorp.wst.dominio.cobros.CajaDetalle;
import com.altioracorp.wst.dominio.cobros.CajaDetalleConsultaDTO;
import com.altioracorp.wst.dominio.cobros.CierreCajaDetalleDTO;
import com.altioracorp.wst.dominio.cobros.CierreCajaReporteDTO;
import com.altioracorp.wst.dominio.cobros.Cobro;
import com.altioracorp.wst.dominio.cobros.CobroEstado;
import com.altioracorp.wst.dominio.cobros.CobroFormaPago;
import com.altioracorp.wst.dominio.sistema.FormaPagoNombre;
import com.altioracorp.wst.dominio.sistema.PerfilNombre;
import com.altioracorp.wst.dominio.sistema.PuntoVenta;
import com.altioracorp.wst.dominio.sistema.Usuario;
import com.altioracorp.wst.exception.reporte.JasperReportsException;
import com.altioracorp.wst.exception.reporte.ReporteExeption;
import com.altioracorp.wst.exception.sistema.PuntoVentaNoExisteException;
import com.altioracorp.wst.repositorio.cobros.ICajaRepositorio;
import com.altioracorp.wst.repositorio.cobros.ICobroRepositorio;
import com.altioracorp.wst.repositorio.sistema.IPuntoVentaRepositorio;
import com.altioracorp.wst.repositorio.sistema.IUsuarioRepositorio;
import com.altioracorp.wst.servicio.cobros.ICajaService;
import com.altioracorp.wst.servicio.reporte.IGeneradorJasperReports;
import com.altioracorp.wst.util.UtilidadesSeguridad;

@Service
public class CajaServiceImpl implements ICajaService{

	private static final Log LOG = LogFactory.getLog(CajaServiceImpl.class);
	
	@Autowired
	private ICobroRepositorio cobroRepositorio;
	
	@Autowired
	private ICajaRepositorio repositorio;
	
	@Autowired
	private IPuntoVentaRepositorio puntoVentaRepositorio;
	
	@Autowired
	private IUsuarioRepositorio usuarioRepositorio;
	
	@Autowired
	private IGeneradorJasperReports reporteServicio;
	
    @Autowired
    private EntityManager entityManager;
	
	@Override
	public Caja consultarCobrosPorPuntoVenta(Long puntoVentaID) {
		
		Optional<PuntoVenta> puntoVenta = puntoVentaRepositorio.findById(puntoVentaID);
		
		if(puntoVenta.isPresent()) {
			Caja ultimoCierreCaja = consultarUltimoCierreCaja(puntoVentaID);
			LocalDateTime ultimaFechaCierreCaja = ultimoCierreCaja == null? LocalDateTime.now().minusYears(1): ultimoCierreCaja.getFechaCierre();
			
			List<Cobro> cobros = cobroRepositorio.findByModificadoPorAndEstadoInAndPuntoVenta_Id(
					UtilidadesSeguridad.nombreUsuarioEnSesion(), Arrays.asList(CobroEstado.PENDIENTE, CobroEstado.CERRADO),
					puntoVentaID);
			
			List<CobroFormaPago> cobroFormasPago =  new ArrayList<>();
			cobros.stream().filter(x-> x.getFecha().isAfter(ultimaFechaCierreCaja)).forEach(x -> cobroFormasPago.addAll(x.getCobroFormaPagos()));
			Caja preCierreCaja = crearPreCierreDeCaja(cobroFormasPago);
			preCierreCaja.setFechaInicio(ultimoCierreCaja == null ? null: ultimaFechaCierreCaja );
			preCierreCaja.setPuntoVenta(puntoVenta.get());
			return preCierreCaja;
		}else {
			throw new PuntoVentaNoExisteException();
		}
		
	}
	
	private Caja crearPreCierreDeCaja(List<CobroFormaPago> cobroFormasPago) {
		Caja caja = new Caja();
		Map<FormaPagoNombre, List<CobroFormaPago>> formasPagoAgrupado = cobroFormasPago.stream()
				.collect(Collectors.groupingBy(CobroFormaPago::getFormaPago, Collectors.toList()));

		formasPagoAgrupado.forEach((formaPagoNombre, lista) -> {
			caja.agregarDetalle(new CajaDetalle(formaPagoNombre,
					lista.stream().map(x -> x.getValor()).reduce(BigDecimal.ZERO, BigDecimal::add)));
		});

		return caja;
	}

	private Caja consultarUltimoCierreCaja(Long puntoVentaID) {
		List<Caja> cierresCaja = repositorio.findByCreadoPorAndPuntoVenta_IdOrderByFechaCierreDesc(
				UtilidadesSeguridad.nombreUsuarioEnSesion(), puntoVentaID);
		
		if (!cierresCaja.isEmpty()) {
			Caja cierreCaja = cierresCaja.stream().findFirst().get();
			LOG.info(String.format("Último Cierre de Caja: %s del usuario: %s", cierreCaja.getFechaCierre(),
					cierreCaja.getCreadoPor()));
			return cierreCaja;
			
		} else {
			LOG.info(String.format("El usuario: %s no ha realizado ningún cierre de caja",
					UtilidadesSeguridad.nombreUsuarioEnSesion()));
			return null;
		}
	}

	@Override
	@Transactional
	public byte[] cerrarCaja(Caja caja) {

		LOG.info(String.format("Cierre de Caja a Guardar: %s", caja));
		
		caja.setFechaCierre(LocalDateTime.now());
		
		Caja cierreCajaRegistrado = repositorio.save(caja);

		Optional<Usuario> usuario = usuarioRepositorio.findByNombreUsuario(cierreCajaRegistrado.getCreadoPor());

		CierreCajaReporteDTO dto = new CierreCajaReporteDTO(usuario.get().getNombreCompleto(),
				usuario.get().getNombreUsuario(), cierreCajaRegistrado.getPuntoVenta().getNombre(),
				cierreCajaRegistrado.getFechaCierre(), cierreCajaRegistrado.getFechaInicio(),
				cierreCajaRegistrado.getCajaDetalles());

		try {
			return reporteServicio.generarReporte("CierreCaja", Collections.singleton(dto), new HashMap<>());
		} catch (JasperReportsException e) {
			LOG.error(String.format("Error Cierre Caja Reporte: %s", e.getMessage()));
			throw new ReporteExeption("Cierre Caja");
		}
	}

	@Override
	public Page<CajaDetalle> consultarCajaCierre(Pageable pageable, CajaConsultaDTO consulta) {
		try {

			final CriteriaBuilder criteriaBuilder = this.entityManager.getCriteriaBuilder();
			final CriteriaQuery<CajaDetalle> query = criteriaBuilder.createQuery(CajaDetalle.class);

			final Root<CajaDetalle> cajaRoot = query.from(CajaDetalle.class);
			final List<Predicate> predicadosConsulta = new ArrayList<Predicate>();

			if (consulta.getRol().getDescripcion().equals(PerfilNombre.CAJERO.getDescripcion())) {
				predicadosConsulta
						.add(criteriaBuilder.equal(cajaRoot.get("creadoPor"), UtilidadesSeguridad.usuarioEnSesion()));
			}
			
			if (consulta.getRol().getDescripcion().equals(PerfilNombre.JEFE_COBRANZAS.getDescripcion())) {
				if(consulta.getUsuario() != null) {
					predicadosConsulta
					.add(criteriaBuilder.equal(cajaRoot.get("creadoPor"), consulta.getUsuario()));
				}
			}

			if (consulta.getIdPuntoVenta() != 0) {
				predicadosConsulta
						.add(criteriaBuilder.equal(cajaRoot.get("caja").get("puntoVenta").get("id"), consulta.getIdPuntoVenta()));
			}

			if (consulta.getFechaInicio() != null && consulta.getFechaFin() != null) {
				predicadosConsulta.add(criteriaBuilder.between(cajaRoot.get("caja").get("fechaCierre"),
						consulta.getFechaInicio().withHour(0).withMinute(0).withSecond(0),
						consulta.getFechaFin().withHour(23).withMinute(59).withSecond(59)));
			}

			if (consulta.getFechaInicio() != null && consulta.getFechaFin() == null) {
				predicadosConsulta.add(criteriaBuilder.between(cajaRoot.get("caja").get("fechaCierre"),
						consulta.getFechaInicio().withHour(0).withMinute(0).withSecond(0),
						consulta.getFechaInicio().withHour(23).withMinute(59).withSecond(59)));
			}

			query.where(predicadosConsulta.toArray(new Predicate[predicadosConsulta.size()]))
					.orderBy(criteriaBuilder.desc(cajaRoot.get("caja").get("fechaCierre")));

			final TypedQuery<CajaDetalle> statement = this.entityManager.createQuery(query);

			List<CajaDetalle> cobrosResult = statement.getResultList();			
			cobrosResult.forEach(x -> x.getCaja().setCajaDetalles(new ArrayList<>()));
			
			final int sizeTotal = cobrosResult.size();

			final int start = (int) pageable.getOffset();
			final int end = (start + pageable.getPageSize()) > cobrosResult.size() ? cobrosResult.size()
					: (start + pageable.getPageSize());

			cobrosResult = cobrosResult.subList(start, end);

			final Page<CajaDetalle> pageResut = new PageImpl<CajaDetalle>(cobrosResult, pageable, sizeTotal);

			return pageResut;
		} catch (final Exception ex) {
			final Page<CajaDetalle> pageResult = new PageImpl<CajaDetalle>(new ArrayList<CajaDetalle>(), pageable, 0);
			return pageResult;
		}

	}

	@Override
	public List<CierreCajaDetalleDTO> consultarDetallaCierreCaje(CajaDetalleConsultaDTO consulta) {
		Optional<Caja> cajaRecargado = repositorio.findById(consulta.getIdCaja());
		List<CierreCajaDetalleDTO> detalleDTO = new ArrayList<CierreCajaDetalleDTO>();

		if (cajaRecargado.isPresent()) {
			String usuario = consulta.getPerfil().equals(PerfilNombre.CAJERO) ? UtilidadesSeguridad.usuarioEnSesion()
					: consulta.getNombreUsuario();

			LocalDateTime fechaInicio = cajaRecargado.get().getFechaInicio() != null
					? cajaRecargado.get().getFechaInicio()
					: LocalDateTime.now().withMonth(1).withYear(2021);
			List<Cobro> cobrosResumen = cobroRepositorio.findByCreadoPorAndFechaBetween(usuario, fechaInicio,
					cajaRecargado.get().getFechaCierre());

			for (Cobro cobro : cobrosResumen) {
				cobro.getCobroFormaPagos().stream().filter(x -> x.getFormaPago().equals(consulta.getFormaPago()))
						.forEach(y -> {
							detalleDTO.add(new CierreCajaDetalleDTO(y.getNumeroCobroGP(), y.getBanco(), y.getChequera(),
									y.getNumeroDocumento(), cobro.getNombreCliente(), cobro.getCodigoCliente(),
									y.getValor(), cobro.getFecha()));
						});
			}

		}
		return detalleDTO;
	}

	@Override
	public byte[] generarReporteCierreCaja(long cajaId) {
		
		Optional<Caja> cajaOptional = repositorio.findById(cajaId);
		
		if(cajaOptional.isPresent()) {
			Caja cierreCajaRegistrado = cajaOptional.get();
			LOG.info(String.format("Generando reporte de  Cierre de caja %s", cierreCajaRegistrado));
			Optional<Usuario> usuario = usuarioRepositorio.findByNombreUsuario(cierreCajaRegistrado.getCreadoPor());

			CierreCajaReporteDTO dto = new CierreCajaReporteDTO(usuario.get().getNombreCompleto(),
					usuario.get().getNombreUsuario(), cierreCajaRegistrado.getPuntoVenta().getNombre(),
					cierreCajaRegistrado.getFechaCierre(), cierreCajaRegistrado.getFechaInicio(),
					cierreCajaRegistrado.getCajaDetalles());

			try {
				return reporteServicio.generarReporte("CierreCaja", Collections.singleton(dto), new HashMap<>());
			} catch (JasperReportsException e) {
				LOG.error(String.format("Error Cierre Caja Reporte: %s", e.getMessage()));
				throw new ReporteExeption("Cierre Caja");
			}
		}

		
		return null;
	}

}
