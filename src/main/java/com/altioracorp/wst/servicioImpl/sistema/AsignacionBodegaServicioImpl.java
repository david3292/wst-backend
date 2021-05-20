package com.altioracorp.wst.servicioImpl.sistema;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.altioracorp.wst.dominio.sistema.AsignacionBodega;
import com.altioracorp.wst.dominio.sistema.ConfiguracionUsuarioPerfil;
import com.altioracorp.wst.dominio.sistema.PuntoVenta;
import com.altioracorp.wst.dominio.sistema.PuntoVentaBodega;
import com.altioracorp.wst.repositorio.sistema.IAsignacionBodegaRepositorio;
import com.altioracorp.wst.repositorio.sistema.IPuntoVentaBodegaRepositorio;
import com.altioracorp.wst.servicio.sistema.IAsignacionBodegaServicio;

@Service
public class AsignacionBodegaServicioImpl implements IAsignacionBodegaServicio {

	private static final Log LOG = LogFactory.getLog(AsignacionBodegaServicioImpl.class);
	
	@Autowired
	private IAsignacionBodegaRepositorio asignacionBodegaRepositorio;
	
	@Autowired
	private IPuntoVentaBodegaRepositorio puntoVentaBodegaRepositorio;

	@Override
	public AsignacionBodega registrar(AsignacionBodega obj) {		
		LOG.info(String.format("Guardando Acceso Bodega Nuevo : %s ", obj));
		return asignacionBodegaRepositorio.save(obj);
	}

	@Override
	public AsignacionBodega modificar(AsignacionBodega obj) {
		LOG.info(String.format("Guardando Acceso Bodega : %s ", obj));
		return asignacionBodegaRepositorio.save(obj);
	}
	
	@Override
	public List<AsignacionBodega> modificarLista(List<AsignacionBodega> asignacionesBodega) {
		LOG.info(String.format("Guardando lista de Acceso Bodega : %s elementos ", asignacionesBodega.size()));
		return (List<AsignacionBodega>) asignacionBodegaRepositorio.saveAll(asignacionesBodega);		
	}

	@Override
	public List<AsignacionBodega> listarTodos() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public AsignacionBodega listarPorId(Long id) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean eliminar(Long id) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public List<AsignacionBodega> buscarPorConfiguracionUsuarioPerfil(ConfiguracionUsuarioPerfil configuracionUsuarioPerfil) {
		crearListaAsignacionBodega(configuracionUsuarioPerfil);
		return  asignacionBodegaRepositorio.findByConfiguracionUsuarioPerfil(configuracionUsuarioPerfil);
	}	
	
	private void crearListaAsignacionBodega(ConfiguracionUsuarioPerfil configuracionUsuarioPerfil) {
		List<AsignacionBodega> asignaciones = asignacionBodegaRepositorio.findByConfiguracionUsuarioPerfil(configuracionUsuarioPerfil);
		List<PuntoVentaBodega> bodegas = obtenerBodegasPorPuntoVenta(configuracionUsuarioPerfil.getPuntoVenta());
		List<PuntoVentaBodega> bodegasFaltantes = new ArrayList<>();
		if(asignaciones.isEmpty()) {
			guardarBodegasParaAsignar(bodegas, configuracionUsuarioPerfil);
		}else {
			bodegas.forEach(x ->{
				boolean bodegaExiste = asignaciones.stream().anyMatch(asignacion -> asignacion.getBodega().getId()== x.getBodega().getId());
				if(!bodegaExiste){
					bodegasFaltantes.add(x);
				}
			});
			guardarBodegasParaAsignar(bodegasFaltantes, configuracionUsuarioPerfil);
		}
	}
	
	private void guardarBodegasParaAsignar(List<PuntoVentaBodega> bodegas, ConfiguracionUsuarioPerfil config) {
		List<AsignacionBodega> bodegasAsignadas = new ArrayList<>();
		bodegas.forEach(x ->{
			bodegasAsignadas.add(new AsignacionBodega(config, x.getBodega(),Boolean.FALSE));
		});
		asignacionBodegaRepositorio.saveAll(bodegasAsignadas);
		
	}

	private List<PuntoVentaBodega> obtenerBodegasPorPuntoVenta(PuntoVenta puntoVenta){
		return puntoVentaBodegaRepositorio.findByPuntoVenta(puntoVenta);
	}
	
}
