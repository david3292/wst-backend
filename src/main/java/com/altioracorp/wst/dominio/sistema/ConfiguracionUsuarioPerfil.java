package com.altioracorp.wst.dominio.sistema;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.validation.constraints.NotNull;

import com.altioracorp.wst.dominio.EntidadBase;

@SuppressWarnings("serial")
@Entity
public class ConfiguracionUsuarioPerfil extends EntidadBase{
	
	@NotNull
	private boolean editarDescuentoAdicional;
	
	@NotNull
	private boolean editarCondicionPago;
	
	@NotNull
	private boolean editarPorcentajeAnticipo;
	
	@NotNull
	@Column(columnDefinition = "bit default 0")
	private boolean editarDescripcionArticulo;
	
	@NotNull
	@Column(columnDefinition = "bit default 0")
	private boolean editarPrecioArticulo;
	
	@NotNull
	@Column(columnDefinition = "bit default 0")
	private boolean editarDescuentoFijo;
	
	@NotNull
	private boolean activo;
	
	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "usuario_perfil_id", referencedColumnName = "id", insertable = true, updatable = true)
	private UsuarioPerfil usuarioPerfil;
	
	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "punto_venta_id", referencedColumnName = "id", insertable = true, updatable = true)
	private PuntoVenta puntoVenta;
	
	public ConfiguracionUsuarioPerfil(){}
	

	public boolean isEditarDescuentoAdicional() {
		return editarDescuentoAdicional;
	}

	public void setEditarDescuentoAdicional(boolean editarDescuentoAdicional) {
		this.editarDescuentoAdicional = editarDescuentoAdicional;
	}

	public boolean isEditarCondicionPago() {
		return editarCondicionPago;
	}

	public void setEditarCondicionPago(boolean editarCondicionPago) {
		this.editarCondicionPago = editarCondicionPago;
	}

	public boolean isEditarPorcentajeAnticipo() {
		return editarPorcentajeAnticipo;
	}

	public void setEditarPorcentajeAnticipo(boolean editarPorcentajeAnticipo) {
		this.editarPorcentajeAnticipo = editarPorcentajeAnticipo;
	}

	public boolean isActivo() {
		return activo;
	}

	public void setActivo(boolean activo) {
		this.activo = activo;
	}

	public UsuarioPerfil getUsuarioPerfil() {
		return usuarioPerfil;
	}

	public void setUsuarioPerfil(UsuarioPerfil usuarioPerfil) {
		this.usuarioPerfil = usuarioPerfil;

	}

	public PuntoVenta getPuntoVenta() {
		return puntoVenta;
	}

	public void setPuntoVenta(PuntoVenta puntoVenta) {
		this.puntoVenta = puntoVenta;
	}


	public boolean isEditarDescripcionArticulo() {
		return editarDescripcionArticulo;
	}


	public void setEditarDescripcionArticulo(boolean editarDescripcionArticulo) {
		this.editarDescripcionArticulo = editarDescripcionArticulo;
	}


	public boolean isEditarPrecioArticulo() {
		return editarPrecioArticulo;
	}


	public void setEditarPrecioArticulo(boolean editarPrecioArticulo) {
		this.editarPrecioArticulo = editarPrecioArticulo;
	}


	public boolean isEditarDescuentoFijo() {
		return editarDescuentoFijo;
	}


	public void setEditarDescuentoFijo(boolean editarDescuentoFijo) {
		this.editarDescuentoFijo = editarDescuentoFijo;
	};
	
	
}
