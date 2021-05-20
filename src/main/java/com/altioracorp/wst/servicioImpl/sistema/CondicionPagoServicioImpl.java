package com.altioracorp.wst.servicioImpl.sistema;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import javax.transaction.Transactional;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.altioracorp.gpintegrator.client.PaymentTerms.PaymentTerms;
import com.altioracorp.wst.constantes.integracion.UrlIntegracionPaymentTerms;
import com.altioracorp.wst.controlador.sistema.CatalogoDTO;
import com.altioracorp.wst.dominio.sistema.CondicionPago;
import com.altioracorp.wst.dominio.sistema.TipoPago;
import com.altioracorp.wst.exception.sistema.CondicionPagoYaExisteException;
import com.altioracorp.wst.repositorio.sistema.ICondicionPagoRepositorio;
import com.altioracorp.wst.servicio.sistema.ICondicionPagoServicio;
import com.altioracorp.wst.servicio.sistema.IConstantesAmbienteWstServicio;

@Service
public class CondicionPagoServicioImpl implements ICondicionPagoServicio {

	private static final Log LOG = LogFactory.getLog(CondicionPagoServicioImpl.class);
	
	@Autowired
	private ICondicionPagoRepositorio condicionPagoRepositorio;	
	
	@Autowired
	private RestTemplate restTemplate;
	
	@Autowired
	private IConstantesAmbienteWstServicio constantesAmbiente;
	
	
	private void asegurarCondicionPagoUnico(String termino) {
		
		Optional<CondicionPago> CondicionPago = condicionPagoRepositorio.findByTermino(termino);
		if(CondicionPago.isPresent()) {
			throw new CondicionPagoYaExisteException(termino);
		}
	}

	@Override
	@Transactional
	public CondicionPago registrar(CondicionPago obj) {
		
		asegurarCondicionPagoUnico(obj.getTermino());
		LOG.info("Guardando Condición de Pago Nuevo: " + obj);
		integarCondicionPagoAGP(obj);		
		return condicionPagoRepositorio.save(obj);
	}

	@Override
	public CondicionPago modificar(CondicionPago obj) {
		
		Optional<CondicionPago> recargado = condicionPagoRepositorio.findById(obj.getId());
		if(recargado.isPresent()) {
			obj.setCreadoFecha(recargado.get().getCreadoFecha());
			obj.setCreadoPor(recargado.get().getCreadoPor());
		}
		LOG.info("Guardando Condición de Pago : " + obj);
		integarCondicionPagoAGP(obj);
		return condicionPagoRepositorio.save(obj);
	}

	@Override
	public List<CondicionPago> listarTodos() {
		return (List<CondicionPago>) condicionPagoRepositorio.findAll();
	}

	@Override
	public CondicionPago listarPorId(Long id) {
		Optional<CondicionPago> area = condicionPagoRepositorio.findById(id);
		return area.isPresent() ? area.get() : null;
	}

	@Override
	public boolean eliminar(Long id) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public List<CondicionPago> buscarTodosActivos() {
		return condicionPagoRepositorio.findByActivoTrue();
	}

	@Override
	public List<CatalogoDTO> crarCatalogoTipoPago() {
		TipoPago[] opciones = TipoPago.values();
		List<CatalogoDTO> catalogo =new ArrayList<>();
		for(TipoPago  ae : opciones ) {
			catalogo.add(new CatalogoDTO(ae.getDescripcion(), ae.toString()));
		}
		return catalogo;
	}
	
	private boolean integarCondicionPagoAGP(CondicionPago condicionPago) {
		String url = constantesAmbiente.getUrlIntegradorGp().concat(UrlIntegracionPaymentTerms.INTEGRAR.getUrl());				
		PaymentTerms paymentTerms = new PaymentTerms(condicionPago.getTermino(),
				String.valueOf(condicionPago.getTotalDias()), condicionPago.getId() == 0 ? "0" : "1");
		LOG.info(String.format("Integrando Condicion de pago a GP: %s",paymentTerms ));
		
		HttpEntity<PaymentTerms> request = new HttpEntity<>(paymentTerms);
		ResponseEntity<Object> response = restTemplate
		  .exchange(url, HttpMethod.POST, request, Object.class);
		LOG.info(String.format("Integrado Condicion de pago a GP resultado: %s",response.getBody() ));
		return true;
	}

	@Override
	public Optional<CondicionPago> buscarPorTermino(String termino) {
		return condicionPagoRepositorio.findByTermino(termino);
	}

}
