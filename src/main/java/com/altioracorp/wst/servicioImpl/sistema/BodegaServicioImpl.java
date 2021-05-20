package com.altioracorp.wst.servicioImpl.sistema;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.altioracorp.gpintegrator.client.SiteSetup.SiteSetup;
import com.altioracorp.wst.constantes.integracion.UrlIntegracionSiteSetup;
import com.altioracorp.wst.dominio.sistema.Bodega;
import com.altioracorp.wst.exception.sistema.BodegaYaExisteException;
import com.altioracorp.wst.repositorio.sistema.IBodegaRepositorio;
import com.altioracorp.wst.servicio.sistema.IBodegaServicio;
import com.altioracorp.wst.servicio.sistema.IConstantesAmbienteWstServicio;

@Service
public class BodegaServicioImpl implements IBodegaServicio {

	private static final Log LOG = LogFactory.getLog(BodegaServicioImpl.class);

	@Autowired
	private IBodegaRepositorio bodegaRepositorio;

	@Autowired
	private RestTemplate restTemplate;
	
	@Autowired
	private IConstantesAmbienteWstServicio constantesAmbiente;

	@Override
	public Bodega registrar(Bodega obj) {
		asegurarCodigoBodega(obj.getCodigo());
		LOG.info("Registrando bodega: " + obj);
		return bodegaRepositorio.save(obj);
	}

	@Override
	public Bodega modificar(Bodega obj) {
		LOG.info("Modificado bodega: " + obj);
		return bodegaRepositorio.save(obj);
	}

	@Override
	public List<Bodega> listarTodos() {
		return (List<Bodega>) bodegaRepositorio.findAll();
	}

	@Override
	public Bodega listarPorId(Long id) {
		Optional<Bodega> bodegaOptional = bodegaRepositorio.findById(id);
		return bodegaOptional.isEmpty() ? new Bodega() : bodegaOptional.get();
	}

	@Override
	public boolean eliminar(Long id) {
		// TODO Auto-generated method stub
		return false;
	}

	private void asegurarCodigoBodega(String codigoBodega) {
		Optional<Bodega> bodegaOptional = this.bodegaRepositorio.findByCodigo(codigoBodega);
		if (bodegaOptional.isPresent())
			throw new BodegaYaExisteException(codigoBodega);
	}

	@Override
	public List<Bodega> listarTodosActivos() {
		return listarTodos().stream().filter(Bodega::isActivo).collect(Collectors.toList());
	}

	@Override
	public List<SiteSetup> listarBodegasGP() {
		String url = constantesAmbiente.getUrlIntegradorGp().concat(UrlIntegracionSiteSetup.LISTAR_TODOS.getUrl());		
		SiteSetup[] response = restTemplate
				.getForObject(url, SiteSetup[].class);
		return Arrays.asList(response);
	}

	@Override
	public SiteSetup listarBodegaPorLocnCode(String locnCode) {
		String url = constantesAmbiente.getUrlIntegradorGp()
				.concat(UrlIntegracionSiteSetup.LISTAR_POR_LOCNCODE.getUrl()
				.replace("${locnCode}", locnCode));
		SiteSetup response = restTemplate.getForObject(url, SiteSetup.class);
		return response;
	}

	@Override
	public String obtenerCodigoBodegaPrincipalPorUsuarioIdPerfilIdPuntoVentaId(long usuarioId, long perfilId,
			long puntoVentaId) {		
		return this.bodegaRepositorio.obtenerCodigoBodegaPrincipalPorUsuarioIdPerfilIdPuntoVentaId(usuarioId, perfilId, puntoVentaId);
	}

	@Override
	public List<Bodega> listarBodegaCentroDistribucion() {
		return bodegaRepositorio.findByActivoTrue().stream().filter(Bodega::isBod_cd).collect(Collectors.toList());
	}

	@Override
	public List<Bodega> listarBodegaReposicionInventario() {
		return bodegaRepositorio.findByActivoTrue().stream().filter(Bodega::isBod_repos_inv).collect(Collectors.toList());
	}

	
	
}
