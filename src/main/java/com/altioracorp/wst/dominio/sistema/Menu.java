package com.altioracorp.wst.dominio.sistema;

import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import com.altioracorp.wst.dominio.EntidadBase;

@SuppressWarnings("serial")
@Entity
public class Menu extends EntidadBase {

	@Column(name = "icono", nullable = false, length = 64)
	private String icono;
	
	@NotNull
	@Size( max= 50 , message="nombre debe tener máximo 50 caractéres")
	private String nombre;	

	@NotNull
	@Size( max= 128 , message="url debe tener máximo 128 caractéres")
	private String url;
	
	@OneToMany(fetch = FetchType.EAGER)
	@JoinColumn(name = "menu_id")
	private List<Menu> items;

	public String getIcono() {
		return icono;
	}

	public void setIcono(String icono) {
		this.icono = icono;
	}

	public String getNombre() {
		return nombre;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public List<Menu> getItems() {
		return items;
	}

	public void setItems(List<Menu> items) {
		this.items = items;
	}	
	
	
}
