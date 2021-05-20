package com.altioracorp.wst.dominio.sistema;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import com.altioracorp.wst.dominio.EntidadBase;

@SuppressWarnings("serial")
@Entity
public class Cargo extends EntidadBase {

	@NotNull
	@NotBlank
	@Size(max = 64 , message = "El campo de nombre no puede tener mas de 64 caractéres")
	private String nombre;
	
	@NotNull
	private boolean activo;
	
	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name="area_id",referencedColumnName = "id", insertable = true, updatable = true)
	private Area area;
	
	public Cargo() {}
	
	public Cargo( String nombre, Area area) {
		this.nombre = nombre;
		this.area = area;
		this.activo = Boolean.TRUE;
	}

	public String getNombre() {
		return nombre;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	public boolean isActivo() {
		return activo;
	}

	public void setActivo(boolean activo) {
		this.activo = activo;
	}

	public Area getArea() {
		return area;
	}

	public void setArea(Area area) {
		this.area = area;
	}

	@Override
	public String toString() {
		return "Cargo [nombre=" + nombre + ", activo=" + activo + ", area=" + area.getCodigo() + "]";
	}	
	
}
