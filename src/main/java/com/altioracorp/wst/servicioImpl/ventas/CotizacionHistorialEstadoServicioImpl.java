package com.altioracorp.wst.servicioImpl.ventas;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.altioracorp.wst.dominio.sistema.Usuario;
import com.altioracorp.wst.dominio.ventas.Cotizacion;
import com.altioracorp.wst.dominio.ventas.CotizacionHistorialEstado;
import com.altioracorp.wst.repositorio.sistema.IUsuarioRepositorio;
import com.altioracorp.wst.repositorio.ventas.ICotizacionHistorialEstadoRepositorio;
import com.altioracorp.wst.servicio.ventas.ICotizacionHistorialEstadoServicio;
import com.altioracorp.wst.util.UtilidadesSeguridad;

@Service
public class CotizacionHistorialEstadoServicioImpl implements ICotizacionHistorialEstadoServicio{

	private static final Log LOG = LogFactory.getLog(CotizacionHistorialEstadoServicioImpl.class);
	
	@Autowired
	private ICotizacionHistorialEstadoRepositorio repositorio;
	
	@Autowired
	private IUsuarioRepositorio usuarioRepositorio;
	
	@Override
	public void registrar(Cotizacion documento, String observacion) {
		
		Usuario usuario = null;
		String usuarioEnSesion = UtilidadesSeguridad.usuarioEnSesion();

		if (usuarioEnSesion != null)
			usuario = usuarioRepositorio.findByNombreUsuario(usuarioEnSesion).orElse(null);

		CotizacionHistorialEstado historial = new CotizacionHistorialEstado();
		historial.setCotizacion(documento);
		historial.setEstado(documento.getEstado());
		historial.setUsuario(usuario == null ? usuarioEnSesion : usuario.getNombreUsuario());
		historial.setUsuarioNombre(usuario == null ? usuarioEnSesion : usuario.getNombreCompleto());
		historial.setObservacion(observacion);
		LOG.info(String.format("Guardando historial Cotización No %s: ", documento.getNumero()));
		repositorio.save(historial);
		
	}

}
