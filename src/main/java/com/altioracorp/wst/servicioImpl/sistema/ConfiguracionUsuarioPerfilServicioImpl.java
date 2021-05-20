package com.altioracorp.wst.servicioImpl.sistema;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.altioracorp.wst.dominio.sistema.AsignacionBodega;
import com.altioracorp.wst.dominio.sistema.ConfiguracionUsuarioPerfil;
import com.altioracorp.wst.dominio.sistema.PerfilNombre;
import com.altioracorp.wst.dominio.sistema.PuntoVenta;
import com.altioracorp.wst.dominio.sistema.Usuario;
import com.altioracorp.wst.dominio.sistema.UsuarioPerfil;
import com.altioracorp.wst.exception.sistema.ConfiguracionUsuarioPerfilPuntoVentaYaExisteException;
import com.altioracorp.wst.repositorio.sistema.IAsignacionBodegaRepositorio;
import com.altioracorp.wst.repositorio.sistema.IConfiguracionUsuarioPerfilRepositorio;
import com.altioracorp.wst.servicio.sistema.IConfiguracionUsuarioPerfilServicio;
import com.altioracorp.wst.util.UtilidadesSeguridad;

@Service
public class ConfiguracionUsuarioPerfilServicioImpl implements IConfiguracionUsuarioPerfilServicio {

	private static final Log LOG = LogFactory.getLog(ConfiguracionUsuarioPerfilServicioImpl.class);
	
	@Autowired
	private IConfiguracionUsuarioPerfilRepositorio configuracionUsuarioPerfilRepositorio;
	
	@Autowired
	private IAsignacionBodegaRepositorio asignacionBodegaRepositorio;

	@Override
	public ConfiguracionUsuarioPerfil registrar(ConfiguracionUsuarioPerfil obj) {
		
		validarConfiguracionUnicaParaPuntoVentaYUsuarioPerfil(obj.getUsuarioPerfil(), obj.getPuntoVenta());
		obj.setActivo(Boolean.TRUE);
		LOG.info(String.format("Guardando Configuración Usuario Perfil Nuevo: %s", obj));
		return configuracionUsuarioPerfilRepositorio.save(obj);
	}

	@Override
	@Transactional
	public ConfiguracionUsuarioPerfil modificar(ConfiguracionUsuarioPerfil obj) {
		Optional<ConfiguracionUsuarioPerfil> configRecargado = configuracionUsuarioPerfilRepositorio.findById(obj.getId());
		
		if(configRecargado.isPresent()) {
			LOG.info(String.format("Guardando Configuración Usuario Perfil: %s", obj));
			configRecargado.get().setEditarCondicionPago(obj.isEditarCondicionPago());
			configRecargado.get().setEditarDescuentoAdicional((obj.isEditarDescuentoAdicional()));
			configRecargado.get().setEditarPorcentajeAnticipo((obj.isEditarPorcentajeAnticipo()));
			configRecargado.get().setEditarDescripcionArticulo((obj.isEditarDescripcionArticulo()));
			configRecargado.get().setEditarDescuentoFijo((obj.isEditarDescuentoFijo()));
			configRecargado.get().setEditarPrecioArticulo((obj.isEditarPrecioArticulo()));
			return configRecargado.get();
		}
		
		return null;
	}

	@Override
	public List<ConfiguracionUsuarioPerfil> listarTodos() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ConfiguracionUsuarioPerfil listarPorId(Long id) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	@Transactional(readOnly = false, propagation = Propagation.REQUIRED)
	public boolean eliminar(Long id) {
		elminarAsignacionbodegas(id);
		configuracionUsuarioPerfilRepositorio.deleteById(id);
		return true;
	}
	
	private void elminarAsignacionbodegas(Long id) {
		Optional<ConfiguracionUsuarioPerfil> configuracion = configuracionUsuarioPerfilRepositorio.findById(id);
		if(configuracion.isPresent()) {
			LOG.info(String.format("Eliminando Asignaciones Bodega de Configuracion Perfil: %s", configuracion.get()));
			List<AsignacionBodega> asignaciones = asignacionBodegaRepositorio.findByConfiguracionUsuarioPerfil(configuracion.get());
			asignacionBodegaRepositorio.deleteAll(asignaciones);
		}		
	}
	

	@Override
	public Optional<ConfiguracionUsuarioPerfil> buscarPorUsuarioPerfilYPuntoVenta(UsuarioPerfil usuarioPerfil,
			PuntoVenta puntoVenta) {
		return configuracionUsuarioPerfilRepositorio.findByUsuarioPerfilAndPuntoVenta(usuarioPerfil, puntoVenta);
	}

	@Override
	public List<ConfiguracionUsuarioPerfil> buscarPorUsuarioPerfil(UsuarioPerfil usuarioPerfil) {
		List<ConfiguracionUsuarioPerfil> lista = configuracionUsuarioPerfilRepositorio.findByUsuarioPerfil(usuarioPerfil);
		return lista;
	}	
	
	private void validarConfiguracionUnicaParaPuntoVentaYUsuarioPerfil(UsuarioPerfil usuarioPerfil, PuntoVenta puntoVenta) {
		Optional<ConfiguracionUsuarioPerfil> config = configuracionUsuarioPerfilRepositorio.findByUsuarioPerfilAndPuntoVenta(usuarioPerfil, puntoVenta);
		if(config.isPresent()) {
			throw new ConfiguracionUsuarioPerfilPuntoVentaYaExisteException(usuarioPerfil.getPerfil().getNombre().getDescripcion(), puntoVenta.getNombre());
		}
	}

	@Override
	public List<ConfiguracionUsuarioPerfil> buscarPorUsuario(Usuario usuario) {
		return configuracionUsuarioPerfilRepositorio.findByUsuarioPerfil_Usuario_NombreUsuario(usuario.getNombreUsuario());
	}

	@Override
	public List<PuntoVenta> buscarPuntosDeVentasPorUsuarioEnSesionYPerfil(PerfilNombre perfil) {
		return configuracionUsuarioPerfilRepositorio.findByUsuarioPerfil_Usuario_NombreUsuarioAndUsuarioPerfil_Perfil_nombre(UtilidadesSeguridad.usuarioEnSesion(), perfil).stream()
				.map(x -> x.getPuntoVenta()).collect(Collectors.toList());
	}

	@Override
	public List<PuntoVenta> buscarPuntosDeVentasPorUsuarioYPerfil(String usuario, PerfilNombre perfil) {
		return configuracionUsuarioPerfilRepositorio.findByUsuarioPerfil_Usuario_NombreUsuarioAndUsuarioPerfil_Perfil_nombre(usuario, perfil).stream()
				.map(x -> x.getPuntoVenta()).collect(Collectors.toList());
	}
}
