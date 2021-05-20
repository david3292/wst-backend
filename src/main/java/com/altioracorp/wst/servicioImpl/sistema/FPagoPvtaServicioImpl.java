package com.altioracorp.wst.servicioImpl.sistema;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import javax.transaction.Transactional;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.altioracorp.gpintegrator.client.CheckBook.CheckBook;
import com.altioracorp.wst.constantes.integracion.UrlIntegracionCheckBook;
import com.altioracorp.wst.controlador.sistema.FormaPagoPvtaDTO;
import com.altioracorp.wst.dominio.sistema.FormaPagoPuntoVenta;
import com.altioracorp.wst.dominio.sistema.PuntoVenta;
import com.altioracorp.wst.exception.sistema.FPagoPVentaYaExisteException;
import com.altioracorp.wst.repositorio.sistema.IFormaPagoPvtaRepositorio;
import com.altioracorp.wst.servicio.sistema.IConstantesAmbienteWstServicio;
import com.altioracorp.wst.servicio.sistema.IFPagoPvtaServicio;

@Service
public class FPagoPvtaServicioImpl implements IFPagoPvtaServicio{

	private static final Log LOG = LogFactory.getLog(FPagoPvtaServicioImpl.class);
	
	@Autowired
	private IFormaPagoPvtaRepositorio repositorio;
	
	@Autowired
	private RestTemplate restTemplate;
	
	@Autowired
	private IConstantesAmbienteWstServicio constantesAmbiente;
	
	@Override
	public FormaPagoPuntoVenta registrar(FormaPagoPuntoVenta obj) {
		LOG.info(String.format("Forma Pago a Punto Venta a Guardar: %s", obj));
		asegurarFormaPagoYPuntoVentaUnico(obj);
		return repositorio.save(obj);
	}

	@Override
	@Transactional
	public FormaPagoPuntoVenta modificar(FormaPagoPuntoVenta obj) {
		Optional<FormaPagoPuntoVenta> recargado = repositorio.findById(obj.getId());
		if(recargado.isPresent()) {
			recargado.get().setChequera(obj.getChequera());
			recargado.get().setActivo(obj.isActivo());
			LOG.info(String.format("Forma Pago a Punto Venta a modeficar: %s", recargado.get()));
			return recargado.get();
		}
		return null;
	}

	@Override
	public List<FormaPagoPuntoVenta> listarTodos() {
		return (List<FormaPagoPuntoVenta>) repositorio.findAll();
	}

	@Override
	public FormaPagoPuntoVenta listarPorId(Long id) {
		return repositorio.findById(id).orElse(null);
	}

	@Override
	public boolean eliminar(Long id) {
		// TODO Auto-generated method stub
		return false;
	}
	
	private void asegurarFormaPagoYPuntoVentaUnico(FormaPagoPuntoVenta obj) {
		Optional<FormaPagoPuntoVenta> recargado = repositorio.findByFormaPagoAndPuntoVenta(obj.getFormaPago(),
				obj.getPuntoVenta());
		if (recargado.isPresent()) {
			throw new FPagoPVentaYaExisteException(recargado.get().getFormaPago().getNombre().getDescripcion(),
					recargado.get().getPuntoVenta().getNombre());
		}
	}

	@Override
	public List<FormaPagoPuntoVenta> listarPorPuntoVenta(PuntoVenta puntoVenta) {
		return repositorio.findByPuntoVenta(puntoVenta);
	}

	@Override
	public List<CheckBook> listarChequerasGP() {
		String url = constantesAmbiente.getUrlIntegradorGp().concat(UrlIntegracionCheckBook.LISTAR_TODOS.getUrl());		
		CheckBook[] response = restTemplate
				.getForObject(url, CheckBook[].class);
		return Arrays.asList(response);
	}

	@Override
	public List<FormaPagoPvtaDTO> listarActivosPorPuntoVenta(PuntoVenta puntoVenta) {
		List<FormaPagoPuntoVenta> formas = repositorio.findByPuntoVentaAndActivoTrue(puntoVenta);
		List<FormaPagoPvtaDTO> dto = new ArrayList<>();
		formas.forEach(x ->{
			dto.add(new FormaPagoPvtaDTO(x.getFormaPago().getNombre(), x.getChequera()));
		});
		
		return dto;
	}

}
