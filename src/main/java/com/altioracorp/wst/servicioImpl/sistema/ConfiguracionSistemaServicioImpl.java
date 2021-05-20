package com.altioracorp.wst.servicioImpl.sistema;

import java.util.List;
import java.util.Optional;

import javax.transaction.Transactional;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.altioracorp.wst.dominio.sistema.ConfigSistema;
import com.altioracorp.wst.dominio.sistema.ConfiguracionSistema;
import com.altioracorp.wst.exception.sistema.ConfiguracionYaExistenteException;
import com.altioracorp.wst.repositorio.sistema.IConfiguracionSistemaRepositorio;
import com.altioracorp.wst.servicio.sistema.IConfiguracionSistemaServicio;

@Service
public class ConfiguracionSistemaServicioImpl implements IConfiguracionSistemaServicio {

	private static final Log LOG = LogFactory.getLog(ConfiguracionSistemaServicioImpl.class);
	
	@Autowired
	private IConfiguracionSistemaRepositorio configuracionSistemaRepositorio;	
	
	private void asegurarConfiguracionSistemaNombreUnico(ConfigSistema nombre) {
		
		Optional<ConfiguracionSistema> configuracionSistema = configuracionSistemaRepositorio.findByNombre(nombre);
		if(configuracionSistema.isPresent()) {
			throw new ConfiguracionYaExistenteException(nombre.getDescripcion());
		}
	}

	@Override
	public ConfiguracionSistema registrar(ConfiguracionSistema obj) {
		asegurarConfiguracionSistemaNombreUnico(obj.getNombre());

		LOG.info("Guardando Configuración Nueva: " + obj);

		return configuracionSistemaRepositorio
				.save(new ConfiguracionSistema(obj.getNombre(), obj.getValor(), obj.getUnidadMedida()));
	}

	@Override
	@Transactional
	public ConfiguracionSistema modificar(ConfiguracionSistema obj) {
		LOG.info("Modificando Configuración : " + obj);
		Optional<ConfiguracionSistema> optional = configuracionSistemaRepositorio.findById(obj.getId());
		if(optional.isPresent()) {
			optional.get().setActivo(obj.isActivo());
			optional.get().setValor(obj.getValor());
			optional.get().setUnidadMedida(obj.getUnidadMedida());
		}
		return obj;
	}

	@Override
	public List<ConfiguracionSistema> listarTodos() {
		return (List<ConfiguracionSistema>) configuracionSistemaRepositorio.findAll();
	}

	@Override
	public ConfiguracionSistema listarPorId(Long id) {
		Optional<ConfiguracionSistema> configuracion = configuracionSistemaRepositorio.findById(id);
		return configuracion.isPresent() ? configuracion.get() : null;
	}

	@Override
	public boolean eliminar(Long id) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public ConfiguracionSistema obetenerConfigPorcentajeVariacionPrecio() {
		Optional<ConfiguracionSistema> configuracion = configuracionSistemaRepositorio.findByNombre(ConfigSistema.MAXIMO_PORCENTAJE_VARIACION_PRECIO);
		return configuracion.isPresent()? configuracion.get(): new ConfiguracionSistema();
	}

	@Override
	public Optional<ConfiguracionSistema> obetenerConfiguracionPorNombre(ConfigSistema nombreConfiguracion) {
		return configuracionSistemaRepositorio.findByNombre(nombreConfiguracion);
	}

	@Override
	public ConfiguracionSistema obetenerConfigMaximoPorcentajeDescuentoFijo() {
		Optional<ConfiguracionSistema> configuracion = configuracionSistemaRepositorio.findByNombre(ConfigSistema.MAXIMO_PORCENTAJE_DESCUENTO_FIJO);
		return configuracion.isPresent()? configuracion.get(): new ConfiguracionSistema();
	}



}
