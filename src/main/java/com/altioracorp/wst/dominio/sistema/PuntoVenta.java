package com.altioracorp.wst.dominio.sistema;

import javax.persistence.Entity;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import com.altioracorp.wst.dominio.EntidadBase;

@SuppressWarnings("serial")
@Entity
public class PuntoVenta extends EntidadBase {

	@NotNull
	@NotBlank
	@Size(max = 256, message = "El campo nobre no puede tener mas de 256 caractéres")
	private String nombre;

	@Size(max = 1024, message = "El campo ubicación no puede tener mas de 1024 caractéres")
	private String ubicacion;

	@Size(max = 1024, message = "El campo dirección no puede tener mas de 1024 caractéres")
	private String direccion;

	@Size(max = 3, message = "El campo dirección no puede tener mas de 3 caractéres")
	private String puntoEmision;

	private boolean activo;

	public PuntoVenta() {
	};

	public String getNombre() {
		return nombre;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	public String getUbicacion() {
		return ubicacion;
	}

	public void setUbicacion(String ubicacion) {
		this.ubicacion = ubicacion;
	}

	public String getDireccion() {
		return direccion;
	}

	public void setDireccion(String direccion) {
		this.direccion = direccion;
	}

	public boolean isActivo() {
		return activo;
	}

	public void setActivo(boolean activo) {
		this.activo = activo;
	}

	public String getPuntoEmision() {
		return puntoEmision;
	}

	public void setPuntoEmision(String puntoEmision) {
		this.puntoEmision = puntoEmision;
	}

	@Override
	public String toString() {
		return "PuntoVenta [nombre=" + nombre + ", ubicacion=" + ubicacion + ", direccion=" + direccion
				+ ", puntoEmision=" + puntoEmision + ", activo=" + activo + "]";
	}

}
