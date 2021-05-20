package com.altioracorp.wst.servicioImpl.sistema;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.altioracorp.wst.dominio.sistema.Area;
import com.altioracorp.wst.dominio.sistema.Cargo;
import com.altioracorp.wst.exception.sistema.CargoYaExistenteException;
import com.altioracorp.wst.repositorio.sistema.ICargoRepositorio;
import com.altioracorp.wst.servicio.sistema.ICargoServicio;

@Service
public class CargoServicioImpl implements ICargoServicio {

	private static final Log LOG = LogFactory.getLog(CargoServicioImpl.class);
	
	@Autowired
	private ICargoRepositorio cargoRepositorio;	
	
	private void asegurarCargoNombreUnico(String codigo) {
		
		Optional<Cargo> Cargo = cargoRepositorio.findByNombre(codigo);
		if(Cargo.isPresent()) {
			throw new CargoYaExistenteException(codigo);
		}
	}

	@Override
	public Cargo registrar(Cargo obj) {
		
		asegurarCargoNombreUnico(obj.getNombre());

		LOG.info("Guardando Cargo Nuevo: " + obj);

		return cargoRepositorio.save(new Cargo(obj.getNombre(), obj.getArea()));
	}

	@Override
	public Cargo modificar(Cargo obj) {
		LOG.info("Guardando Cargo : " + obj);
		return cargoRepositorio.save(obj);
	}

	@Override
	public List<Cargo> listarTodos() {
		return (List<Cargo>) cargoRepositorio.findAll();
	}

	@Override
	public Cargo listarPorId(Long id) {
		Optional<Cargo> area = cargoRepositorio.findById(id);
		return area.isPresent() ? area.get() : null;
	}

	@Override
	public boolean eliminar(Long id) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public List<Cargo> buscarTodosActivos() {
		return listarTodos().stream().filter(x->x.isActivo()).collect(Collectors.toList());
	}

	@Override
	public List<Cargo> buscarPorAreaYActivos(Area area) {
		return cargoRepositorio.findByAreaAndActivoTrue(area);
	}

}
