package com.altioracorp.wst.dominio.ventas;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.ManyToOne;

import com.altioracorp.wst.dominio.EntidadBase;
import com.altioracorp.wst.dominio.sistema.PerfilNombre;
import com.sun.istack.NotNull;

@SuppressWarnings("serial")
@Entity
public class CotizacionHistorialEstado extends EntidadBase {

	@ManyToOne(fetch = FetchType.LAZY)
	private Cotizacion cotizacion;
	
	@Enumerated(EnumType.STRING)
	@NotNull
	private CotizacionEstado estado;
	
	@NotNull
	private String usuario;
	
	@NotNull
	private String usuarioNombre;
	
	private String observacion;
	
	@Enumerated(EnumType.STRING)
	private PerfilNombre perfil;

	public Cotizacion getCotizacion() {
		return cotizacion;
	}

	public void setCotizacion(Cotizacion cotizacion) {
		this.cotizacion = cotizacion;
	}

	public String getUsuario() {
		return usuario;
	}

	public void setUsuario(String usuario) {
		this.usuario = usuario;
	}

	public String getObservacion() {
		return observacion;
	}

	public void setObservacion(String observacion) {
		this.observacion = observacion;
	}

	public CotizacionEstado getEstado() {
		return estado;
	}

	public void setEstado(CotizacionEstado estado) {
		this.estado = estado;
	}

	public String getUsuarioNombre() {
		return usuarioNombre;
	}

	public void setUsuarioNombre(String usuarioNombre) {
		this.usuarioNombre = usuarioNombre;
	}

	public PerfilNombre getPerfil() {
		return perfil;
	}

	public void setPerfil(PerfilNombre perfil) {
		this.perfil = perfil;
	}

	@Override
	public String toString() {
		return "AprobacionHistorialDocumento [documento=" + cotizacion + ", estado=" + estado
				+ ", usuario=" + usuario + ", usuarioNombre=" + usuarioNombre + ", observacion=" + observacion + "]";
	}

}
