package com.altioracorp.wst.servicioImpl.sistema;

import com.altioracorp.wst.dominio.sistema.ConfiguracionUsuarioPerfil;
import com.altioracorp.wst.dominio.sistema.PuntoVenta;
import com.altioracorp.wst.dominio.sistema.UsuarioPerfil;
import com.altioracorp.wst.repositorio.sistema.IPuntoVentaRepositorio;
import com.altioracorp.wst.servicio.sistema.IConfiguracionUsuarioPerfilServicio;
import com.altioracorp.wst.servicio.sistema.IPuntoVentaServicio;
import com.altioracorp.wst.servicio.sistema.IUsuarioPerfilServicio;
import com.altioracorp.wst.util.UtilidadesSeguridad;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class PuntoVentaServicioImpl implements IPuntoVentaServicio {

	private static final Log LOG = LogFactory.getLog(PuntoVentaServicioImpl.class);

	@Autowired
	private IPuntoVentaRepositorio puntoVentaRepositorio;

	@Autowired
	private IUsuarioPerfilServicio usuarioPerfilServicio;

	@Autowired
	private IConfiguracionUsuarioPerfilServicio configuracionUsuarioPerfilServicio;

	@Override
	public PuntoVenta registrar(PuntoVenta obj) {
		LOG.info("Registrando punto de venta: " + obj);
		return puntoVentaRepositorio.save(obj);
	}

	@Override
	public PuntoVenta modificar(PuntoVenta obj) {
		LOG.info("Modificando punto de venta: " + obj);
		return puntoVentaRepositorio.save(obj);
	}

	@Override
	public List<PuntoVenta> listarTodos() {
		return (List<PuntoVenta>) puntoVentaRepositorio.findAll();
	}

	@Override
	public PuntoVenta listarPorId(Long id) {
		Optional<PuntoVenta> puntoVenta = puntoVentaRepositorio.findById(id); 
		return puntoVenta.isEmpty() ? new PuntoVenta() : puntoVenta.get();
	}

	@Override
	public boolean eliminar(Long id) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public List<PuntoVenta> listarTodosActivos() {
		return listarTodos().stream().filter(PuntoVenta::isActivo).collect(Collectors.toList());
	}

	@Override
	public PuntoVenta buscarPorNombre(String nombre) {
		Optional<PuntoVenta> puntoVenta = puntoVentaRepositorio.findByNombre(nombre);
		return puntoVenta.isEmpty() ? new PuntoVenta() : puntoVenta.get();
	}

	@Override
	public List<PuntoVenta> buscarPorPerfilIdYUsuarioSesion(final Long idPerfil) {
		final Optional<UsuarioPerfil> usuarioPerfil = this.usuarioPerfilServicio.buscarPorNombreUsuarioYPerfilId(UtilidadesSeguridad.usuarioEnSesion(),
				idPerfil);

		if (!usuarioPerfil.isPresent()) {
			return null;
		}

		final List<ConfiguracionUsuarioPerfil> configuraciones = this.configuracionUsuarioPerfilServicio.buscarPorUsuarioPerfil(usuarioPerfil.get());

		final List<PuntoVenta> puntosVenta = configuraciones.stream().map(c -> {
			return c.getPuntoVenta();
		}).collect(Collectors.toList());

		return puntosVenta;
	}
}
