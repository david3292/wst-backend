package com.altioracorp.wst.dominio.sistema;

import java.util.List;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import com.altioracorp.wst.dominio.EntidadBase;

@SuppressWarnings("serial")
@Entity
public class Perfil extends EntidadBase {

	@NotNull
	@Enumerated(EnumType.STRING)
	private PerfilNombre nombre;
	
	@Size( max=256 , message = "descripción rol debe tener  máximo 256 caractéres")
	private String descripcion;
	
	@ManyToMany(fetch = FetchType.EAGER)
	@JoinTable(name = "perfil_menu", joinColumns= @JoinColumn(name="perfil_id", referencedColumnName = "id"), inverseJoinColumns = @JoinColumn(name="menu_id", referencedColumnName = "id"))
	private List<Menu> listaMenu;
	
	public Perfil() {}	
	
	public Perfil( PerfilNombre nombre) {
		this.nombre = nombre;
	}

	public PerfilNombre getNombre() {
		return nombre;
	}

	public void setNombre(PerfilNombre nombre) {
		this.nombre = nombre;
	}

	public String getDescripcion() {
		return descripcion;
	}

	public void setDescripcion(String descripcion) {
		this.descripcion = descripcion;
	}

	public List<Menu> getListaMenu() {
		return listaMenu;
	}

	public void setListaMenu(List<Menu> listaMenu) {
		this.listaMenu = listaMenu;
	}

	@Override
	public String toString() {
		return "Perfil [nombre=" + nombre + ", descripcion=" + descripcion + ", listaMenu=" + listaMenu + "]";
	}
}
