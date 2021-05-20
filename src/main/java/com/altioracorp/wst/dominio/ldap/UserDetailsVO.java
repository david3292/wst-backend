package com.altioracorp.wst.dominio.ldap;

import java.util.List;

import com.altioracorp.wst.dominio.sistema.ConfiguracionUsuarioPerfil;
import com.altioracorp.wst.dominio.sistema.PuntoVenta;
import com.altioracorp.wst.dominio.sistema.Usuario;

public class UserDetailsVO {
	
	private List<PuntoVenta> puntosVenta;
	
	private PuntoVenta puntoVentaSeleccionado;
	
	private ConfiguracionUsuarioPerfil configuracionPuntoVentaSeleccionado;
	
	private String perfilIngreso;
	
	private List<String> perfiles;
	
	private Usuario usuario;

	public UserDetailsVO() {}

	public List<PuntoVenta> getPuntosVenta() {
		return puntosVenta;
	}
	
	public boolean puedeEditarCondicionPago() {
		return configuracionPuntoVentaSeleccionado.isEditarCondicionPago();
	}
	
	public boolean puedeEditarDescuentoAdicional() {
		return configuracionPuntoVentaSeleccionado.isEditarDescuentoAdicional();
	}
	
	public boolean puedeEditarPorcentajeAnticipo() {
		return configuracionPuntoVentaSeleccionado.isEditarPorcentajeAnticipo();
	}

	public void setPuntosVenta(List<PuntoVenta> puntosVenta) {
		this.puntosVenta = puntosVenta;
	}

	public PuntoVenta getPuntoVentaSeleccionado() {
		return puntoVentaSeleccionado;
	}

	public void setPuntoVentaSeleccionado(PuntoVenta puntoVentaSeleccionado) {
		this.puntoVentaSeleccionado = puntoVentaSeleccionado;
	}

	public ConfiguracionUsuarioPerfil getConfiguracionPuntoVentaSeleccionado() {
		return configuracionPuntoVentaSeleccionado;
	}

	public void setConfiguracionPuntoVentaSeleccionado(ConfiguracionUsuarioPerfil configuracionPuntoVentaSeleccionado) {
		this.configuracionPuntoVentaSeleccionado = configuracionPuntoVentaSeleccionado;
	}

	public Usuario getUsuario() {
		return usuario;
	}

	public void setUsuario(Usuario usuario) {
		this.usuario = usuario;
	}

	public String getPerfilIngreso() {
		return perfilIngreso;
	}

	public void setPerfilIngreso(String perfilIngreso) {
		this.perfilIngreso = perfilIngreso;
	}

	public List<String> getPerfiles() {
		return perfiles;
	}

	public void setPerfiles(List<String> perfiles) {
		this.perfiles = perfiles;
	}

	
}
