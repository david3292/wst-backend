package com.altioracorp.wst.servicioImpl.sistema;

import java.util.List;
import java.util.Optional;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.altioracorp.wst.dominio.sistema.PuntoVenta;
import com.altioracorp.wst.dominio.sistema.Secuencial;
import com.altioracorp.wst.dominio.sistema.TipoDocumento;
import com.altioracorp.wst.exception.sistema.PuntoVentaNoExisteException;
import com.altioracorp.wst.exception.sistema.SecuencialYaExisteException;
import com.altioracorp.wst.repositorio.sistema.ISecuencialRepositorio;
import com.altioracorp.wst.servicio.sistema.IPuntoVentaServicio;
import com.altioracorp.wst.servicio.sistema.ISecuencialServicio;

@Service
public class SecuencialServicioImpl implements ISecuencialServicio {

	private static final Log LOG = LogFactory.getLog(SecuencialServicioImpl.class);

	@Autowired
	private ISecuencialRepositorio secuencialRepositorio;
	
	@Autowired
	private IPuntoVentaServicio puntoVentaServicio;

	private void asegurarTipoDocumentoYPuntoEmision(Secuencial secuencial) {
		Optional<Secuencial> Secuencial = Optional.ofNullable(secuencialRepositorio
				.findByPuntoVentaAndTipoDocumento(secuencial.getPuntoVenta(), secuencial.getTipoDocumento()));
		if (Secuencial.isPresent()) {
			throw new SecuencialYaExisteException(secuencial.getPuntoVenta().getNombre(),
					secuencial.getTipoDocumento().toString());
		}
	}

	@Override
	public Secuencial registrar(Secuencial secuencial) {

		asegurarTipoDocumentoYPuntoEmision(secuencial);
		
		LOG.info("Punto venta" + secuencial.getPuntoVenta().getId());

		LOG.info("Guardando Nuevo Secuencial: " + secuencial);

		return secuencialRepositorio.save(new Secuencial(secuencial.getPuntoVenta(), secuencial.getTipoDocumento(),
				secuencial.getDocIdGP(), secuencial.getAbreviatura()));

	}

	@Override
	public Secuencial modificar(Secuencial obj) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<Secuencial> listarTodos() {
		return (List<Secuencial>) secuencialRepositorio.findAll();
	}

	@Override
	public Secuencial listarPorId(Long id) {
		Optional<Secuencial> secuencial = secuencialRepositorio.findById(id);
		return secuencial.isPresent() ? secuencial.get() : null;
	}

	@Override
	public boolean eliminar(Long id) {
		// TODO Auto-generated method stub
		return false;
	}

	public Secuencial ObtenerSecuencialPorPuntoVentaYTipoDocumento(long puntoVentaId, TipoDocumento tipoDocumento) {
		
		PuntoVenta puntoVenta = puntoVentaServicio.listarPorId(puntoVentaId);
		
		if (puntoVenta == null) {
			throw new PuntoVentaNoExisteException();
		}
		
		Optional<Secuencial> secuencialOP = Optional
				.ofNullable(secuencialRepositorio.findByPuntoVentaAndTipoDocumento(puntoVenta, tipoDocumento));

		if (secuencialOP.isPresent()) {
			Secuencial secuencial = secuencialOP.get();
			if (!secuencial.getTipoDocumento().esDocumentoGP()) {
				secuencial.aumentarSucesion();
				secuencialRepositorio.save(secuencial);
			}

			return secuencial;
		}
		return null;
	}

}
