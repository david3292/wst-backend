package com.altioracorp.wst.servicioImpl.sistema;

import com.altioracorp.gpintegrator.client.Receivable.SalesPerson;
import com.altioracorp.wst.constantes.integracion.UrlIntegracionSalesPerson;
import com.altioracorp.wst.dominio.sistema.*;
import com.altioracorp.wst.exception.sistema.PerfilAsignadoYaExisteException;
import com.altioracorp.wst.repositorio.sistema.IAsignacionBodegaRepositorio;
import com.altioracorp.wst.repositorio.sistema.IConfiguracionUsuarioPerfilRepositorio;
import com.altioracorp.wst.repositorio.sistema.IUsuarioPerfilRepositorio;
import com.altioracorp.wst.servicio.sistema.IConstantesAmbienteWstServicio;
import com.altioracorp.wst.servicio.sistema.IUsuarioPerfilServicio;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@Service
public class UsuarioPerfilServicioImpl implements IUsuarioPerfilServicio {

	private static final Log LOG = LogFactory.getLog(UsuarioPerfilServicioImpl.class);
	
	@Autowired
	private IUsuarioPerfilRepositorio usuarioPerfilRepositorio;	
	
	@Autowired
	private IConfiguracionUsuarioPerfilRepositorio configuracionUsuarioPerfilRepositorio;
	
	@Autowired
	private IAsignacionBodegaRepositorio asignacionBodegaRepositorio;	
	
	@Autowired
	private RestTemplate restTemplate;
	
	@Autowired
	private IConstantesAmbienteWstServicio constantesAmbiente;

	@Override
	public UsuarioPerfil registrar(UsuarioPerfil obj) {
		
		asegurarPerfilUnico(obj);

		LOG.info("Guardando UsuarioPerfil Nuevo: " + obj);

		return usuarioPerfilRepositorio.save(obj);
	}
	 

	@Override
	public UsuarioPerfil modificar(UsuarioPerfil obj) {
		LOG.info("Guardando UsuarioPerfil : " + obj);
		return usuarioPerfilRepositorio.save(obj);
	}

	@Override
	public List<UsuarioPerfil> listarTodos() {
		return (List<UsuarioPerfil>) usuarioPerfilRepositorio.findAll();
	}

	@Override
	public UsuarioPerfil listarPorId(Long id) {
		Optional<UsuarioPerfil> perfil = usuarioPerfilRepositorio.findById(id);
		return perfil.isPresent() ? perfil.get() : null;
	}

	
	@Override
	@Transactional(readOnly = false, propagation = Propagation.REQUIRED)
	public boolean eliminar(Long id) {
		eliminarConfiguracion(id);
		usuarioPerfilRepositorio.deleteById(id);
		return true;
	}
	
	private void eliminarConfiguracion(Long idUsuarioPerfil) {
		Optional<UsuarioPerfil> usuarioPerfil = usuarioPerfilRepositorio.findById(idUsuarioPerfil);
		if(usuarioPerfil.isPresent()) {
			LOG.info("Eliminando configuración de UsuarioPerfil : " + usuarioPerfil.get());
			List<ConfiguracionUsuarioPerfil> configuraciones = configuracionUsuarioPerfilRepositorio.findByUsuarioPerfil(usuarioPerfil.get());
			eliminarAsignacionBodega(configuraciones);
			configuracionUsuarioPerfilRepositorio.deleteAll(configuraciones);
		}
		
	}
	
	private void eliminarAsignacionBodega(List<ConfiguracionUsuarioPerfil> configuraciones) {
		configuraciones.forEach(x ->{
			List<AsignacionBodega> asignaciones = asignacionBodegaRepositorio.findByConfiguracionUsuarioPerfil(x);
			asignacionBodegaRepositorio.deleteAll(asignaciones);
		});
	}
	
	private void asegurarPerfilUnico(UsuarioPerfil usuarioPerfil) {
		
		Optional<UsuarioPerfil> UsuarioPerfil = usuarioPerfilRepositorio.findByUsuarioAndPerfil(usuarioPerfil.getUsuario(), usuarioPerfil.getPerfil());
		if(UsuarioPerfil.isPresent()) {
			throw new PerfilAsignadoYaExisteException(usuarioPerfil.getPerfil().getNombre().getDescripcion());
		}
	}

	@Override
	public List<UsuarioPerfil> listarPorUsuario(Usuario usuario) {
		List<UsuarioPerfil> perfilesAsignados= new ArrayList<>();
		perfilesAsignados = usuarioPerfilRepositorio.findByUsuario(usuario);
		perfilesAsignados.forEach(x->x.setUsuario(null));
		return perfilesAsignados;
	}

	@Override
	public Optional<UsuarioPerfil> listarPorNombreUsuarioYPerfilNombre(String usuario, PerfilNombre perfilNombre) {
		return usuarioPerfilRepositorio.findByUsuario_NombreUsuarioAndPerfil_Nombre(usuario, perfilNombre);
	}


	@Override
	public List<SalesPerson> listarCodigosVendedoresGP() {
		String url = constantesAmbiente.getUrlIntegradorGp().concat(UrlIntegracionSalesPerson.LISTAR_PERSONA_VENTAS.getUrl());

		SalesPerson[] response = restTemplate
				.getForObject(url, SalesPerson[].class);
		return Arrays.asList(response);
	}

	@Override
	public Optional<UsuarioPerfil> buscarPorNombreUsuarioYPerfilId(final String nombreUsuario, final Long idPerfil) {
		return this.usuarioPerfilRepositorio.findByUsuario_NombreUsuarioAndPerfil_Id(nombreUsuario, idPerfil);
	}

}
