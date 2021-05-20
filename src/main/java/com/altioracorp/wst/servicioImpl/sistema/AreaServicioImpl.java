package com.altioracorp.wst.servicioImpl.sistema;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.altioracorp.wst.dominio.sistema.Area;
import com.altioracorp.wst.exception.sistema.AreaYaExistenteException;
import com.altioracorp.wst.repositorio.sistema.IAreaRepositorio;
import com.altioracorp.wst.servicio.sistema.IAreaServicio;

@Service
public class AreaServicioImpl implements IAreaServicio {

	private static final Log LOG = LogFactory.getLog(AreaServicioImpl.class);
	
	@Autowired
	private IAreaRepositorio areaRepositorio;	
	
	private void asegurarAreaCodigoUnico(String codigo) {
		
		Optional<Area> Area = areaRepositorio.findByCodigo(codigo);
		if(Area.isPresent()) {
			throw new AreaYaExistenteException(codigo);
		}
	}

	@Override
	public Area registrar(Area obj) {
		
		asegurarAreaCodigoUnico(obj.getCodigo());

		LOG.info("Guardando Área Nueva: " + obj);

		return areaRepositorio.save(new Area(obj.getCodigo(), obj.getAreaFuncional(), obj.getAreaReporta()));
	}

	@Override
	public Area modificar(Area obj) {
		LOG.info("Guardando Área : " + obj);
		return areaRepositorio.save(obj);
	}

	@Override
	public List<Area> listarTodos() {
		return (List<Area>) areaRepositorio.findAll();
	}

	@Override
	public Area listarPorId(Long id) {
		Optional<Area> area = areaRepositorio.findById(id);
		return area.isPresent() ? area.get() : null;
	}

	@Override
	public boolean eliminar(Long id) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public List<Area> buscarTodosActivos() {
		return listarTodos().stream().filter(x->x.isActivo()).collect(Collectors.toList());
	}

}
