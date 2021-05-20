package com.altioracorp.wst.dominio.sistema;

import javax.persistence.Entity;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import com.altioracorp.wst.dominio.EntidadBase;

@SuppressWarnings("serial")
@Entity
public class Clase extends EntidadBase {

	@NotNull
	@NotBlank
	@Size(max = 16 , message = "El campo de nombre no puede tener mas de 16 caractéres")
	private String nombre;
	
	@NotNull
	private boolean activo;
	
	public Clase() {}

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
	};
}
