package com.altioracorp.wst.servicioImpl.sistema;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import javax.transaction.Transactional;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.altioracorp.wst.dominio.sistema.Bodega;
import com.altioracorp.wst.dominio.sistema.PuntoVenta;
import com.altioracorp.wst.dominio.sistema.PuntoVentaBodega;
import com.altioracorp.wst.exception.sistema.PuntoVentaBodegaPrincipalYaExisteException;
import com.altioracorp.wst.exception.sistema.PuntoVentaBodegaYaExisteException;
import com.altioracorp.wst.repositorio.sistema.IPuntoVentaBodegaRepositorio;
import com.altioracorp.wst.servicio.sistema.IPuntoVentaBodegaServicio;

@Service
public class PuntoVentaBodegaServicioImpl implements IPuntoVentaBodegaServicio{

	private static final Log LOG = LogFactory.getLog(PuntoVentaBodegaServicioImpl.class);
	
	@Autowired
	private IPuntoVentaBodegaRepositorio puntoVentaBodegarepositorio;

	@Override
	public PuntoVentaBodega registrar(PuntoVentaBodega obj) {
		LOG.info(String.format("Guardando Bodega %s al Punto de Venta: %s", obj.getBodega().getCodigo(), obj.getPuntoVenta().getNombre()));
		asegurarBodegaUnica(obj);
		return puntoVentaBodegarepositorio.save(obj);
	}

	@Override
	@Transactional
	public PuntoVentaBodega modificar(PuntoVentaBodega obj) {
		Optional<PuntoVentaBodega> pvtaBodegaRecargado = puntoVentaBodegarepositorio.findById(obj.getId());
		if(pvtaBodegaRecargado.isPresent()) {
			if(obj.isPrincipal()) asegurarUnicaBodegaPricipal(obj);
			LOG.info(String.format("Modificando Bodega %s del Punto de Venta: %s", obj.getBodega().getCodigo(), obj.getPuntoVenta().getNombre()));
			pvtaBodegaRecargado.get().setActivo(obj.isActivo());
			pvtaBodegaRecargado.get().setPrincipal(obj.isPrincipal());
			return pvtaBodegaRecargado.get();
		}
		return null;				
	}

	@Override
	public List<PuntoVentaBodega> listarTodos() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public PuntoVentaBodega listarPorId(Long id) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean eliminar(Long id) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public List<Bodega> buscarBodegasPorPuntoVenta(PuntoVenta puntoVenta) {
		List<Bodega> bodegas = new ArrayList<>();		
		List<PuntoVentaBodega> pvtaBodegas= puntoVentaBodegarepositorio.findByPuntoVenta(puntoVenta);
		pvtaBodegas.forEach(x ->{
			bodegas.add(x.getBodega());
		});
		return bodegas;
	}

	@Override
	public List<PuntoVentaBodega> listarBodegasPorPuntoVenta(PuntoVenta puntoVenta) {
		return  puntoVentaBodegarepositorio.findByPuntoVenta(puntoVenta);
	}	
	
	private void asegurarBodegaUnica(PuntoVentaBodega pvtaBodega) {
		Optional<PuntoVentaBodega> pvtBodegaRecargado= puntoVentaBodegarepositorio.findByPuntoVentaAndBodega(pvtaBodega.getPuntoVenta(), pvtaBodega.getBodega());
		if(pvtBodegaRecargado.isPresent()) {
			throw new PuntoVentaBodegaYaExisteException(pvtBodegaRecargado.get().getBodega().getDescripcion());
		}
	}
	
	private void asegurarUnicaBodegaPricipal(PuntoVentaBodega pvtaBodega) {
		Optional<PuntoVentaBodega> pvtBodegaRecargado= puntoVentaBodegarepositorio.findByPrincipalTrueAndPuntoVenta(pvtaBodega.getPuntoVenta());
		if(pvtBodegaRecargado.isPresent()) {
			throw new PuntoVentaBodegaPrincipalYaExisteException(pvtBodegaRecargado.get().getBodega().getDescripcion());
		}
	}

	@Override
	public Bodega buscarBodegaPrincipalPorPuntoVenta(PuntoVenta puntoVenta) {
		List<PuntoVentaBodega> pvtaBodegas= puntoVentaBodegarepositorio.findByPuntoVenta(puntoVenta);
		Optional<PuntoVentaBodega> pvtaBdgOptional = pvtaBodegas.stream().filter(x -> x.isPrincipal()).findAny();
		return pvtaBdgOptional.isPresent() ? pvtaBdgOptional.get().getBodega() : null;
	}

	@Override
	public List<PuntoVentaBodega> listarBodegasPrincipalesPorCodigo(String codigoBodega) {
		return puntoVentaBodegarepositorio.findByActivoTrueAndPrincipalTrueAndBodega_Codigo(codigoBodega);
	}
}
