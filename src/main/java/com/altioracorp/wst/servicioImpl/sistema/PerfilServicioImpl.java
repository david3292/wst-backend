package com.altioracorp.wst.servicioImpl.sistema;

import java.util.List;
import java.util.Optional;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.altioracorp.wst.dominio.sistema.Perfil;
import com.altioracorp.wst.dominio.sistema.PerfilNombre;
import com.altioracorp.wst.exception.sistema.PerfilYaExistenteException;
import com.altioracorp.wst.repositorio.sistema.IPerfilRepositorio;
import com.altioracorp.wst.servicio.sistema.IPerfilServicio;

@Service
public class PerfilServicioImpl implements IPerfilServicio {

	private static final Log LOG = LogFactory.getLog(PerfilServicioImpl.class);
	
	@Autowired
	private IPerfilRepositorio perfilRepositorio;	
	
	private void asegurarPerfilNombreUnico(PerfilNombre codigo) {
		
		Optional<Perfil> Perfil = perfilRepositorio.findByNombre(codigo);
		if(Perfil.isPresent()) {
			throw new PerfilYaExistenteException(codigo.getDescripcion());
		}
	}

	@Override
	public Perfil registrar(Perfil obj) {
		
		asegurarPerfilNombreUnico(obj.getNombre());

		LOG.info("Guardando Perfil Nuevo: " + obj);

		return perfilRepositorio.save(new Perfil(obj.getNombre()));
	}

	@Override
	public Perfil modificar(Perfil obj) {
		LOG.info("Guardando Perfil : " + obj);
		return perfilRepositorio.save(obj);
	}

	@Override
	public List<Perfil> listarTodos() {
		return (List<Perfil>) perfilRepositorio.findAll();
	}

	@Override
	public Perfil listarPorId(Long id) {
		Optional<Perfil> perfil = perfilRepositorio.findById(id);
		return perfil.isPresent() ? perfil.get() : null;
	}

	@Override
	public boolean eliminar(Long id) {
		// TODO Auto-generated method stub
		return false;
	}
}
