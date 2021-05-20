package com.altioracorp.wst.servicioImpl.sistema;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import javax.transaction.Transactional;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.altioracorp.wst.controlador.sistema.CatalogoDTO;
import com.altioracorp.wst.dominio.sistema.FormaPago;
import com.altioracorp.wst.dominio.sistema.FormaPagoNombre;
import com.altioracorp.wst.exception.sistema.FormaPagoYaExisteException;
import com.altioracorp.wst.repositorio.sistema.IFormaPagoRepositorio;
import com.altioracorp.wst.servicio.sistema.IFormaPagoServicio;

@Service
public class FormaPagoServicioImpl implements IFormaPagoServicio {

	private static final Log LOG = LogFactory.getLog(FormaPagoServicioImpl.class);
	
	@Autowired
	private IFormaPagoRepositorio formaPagoRepositorio; 
	
	@Override
	public FormaPago registrar(FormaPago obj) {
		LOG.info(String.format("Forma Pago a guardar %s", obj));
		asegurarFormaPagoUnica(obj);
		return formaPagoRepositorio.save(obj);
	}

	@Override
	@Transactional
	public FormaPago modificar(FormaPago obj) {
		Optional<FormaPago> formaPagoRecargado = formaPagoRepositorio.findById(obj.getId());
		if(formaPagoRecargado.isPresent()) {
			formaPagoRecargado.get().setActivo(obj.isActivo());
			formaPagoRecargado.get().setIntegracionCobro(obj.isIntegracionCobro());
			formaPagoRecargado.get().setChequePosFechado(obj.isChequePosFechado());
			LOG.info(String.format("Forma Pago modificada %s", formaPagoRecargado.get()));
		}
		return null;
	}

	@Override
	public List<FormaPago> listarTodos() {
		return (List<FormaPago>) formaPagoRepositorio.findAll();
	}

	@Override
	public FormaPago listarPorId(Long id) {
		return formaPagoRepositorio.findById(id).orElse(null);
	}

	@Override
	public boolean eliminar(Long id) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public List<FormaPago> listarActivos() {
		return formaPagoRepositorio.findByActivoTrue();
	}
	
	private void asegurarFormaPagoUnica(FormaPago fpago) {	
		Optional<FormaPago> fPagoRecargado = formaPagoRepositorio.findByNombre(fpago.getNombre());
		if(fPagoRecargado.isPresent()) {
			throw new FormaPagoYaExisteException(fPagoRecargado.get().getNombre().getDescripcion());
		}
	}

	@Override
	public List<CatalogoDTO> listarCatalogoFormaPagoNombre() {
		FormaPagoNombre[] opciones = FormaPagoNombre.values();
		List<CatalogoDTO> catalogo =new ArrayList<>();
		for(FormaPagoNombre  ae : opciones ) {
			catalogo.add(new CatalogoDTO(ae.getDescripcion(), ae.toString()));
		}
		return catalogo;
	}

}
