package com.altioracorp.wst.dominio.sistema;

import javax.persistence.Entity;
import javax.validation.constraints.Size;

import com.altioracorp.wst.dominio.EntidadBase;

@SuppressWarnings("serial")
@Entity
public class Bodega extends EntidadBase {

	@Size(max = 32, message = "El campo de código no puede tener mas de 32 caractéres")
	private String codigo;
	
	@Size(max = 64, message = "El campo de descripción no puede tener mas de 64 caractéres")
	private String descripcion;
		
	private boolean activo;
	
	/*
	 * TODO: Campo referente a BODEGA CENTRO DE DISTRIBUCIÓN
	 * */
	private boolean bod_cd;
	
	/*
	 * TODO: Campo referente a BODEGA PUNTO DE VENTA
	 * */
	private boolean bod_PV;
	
	/*
	 * TODO: Campo referente a BODEGA REPOSICIÓN INVENTARIO
	 * */
	private boolean bod_repos_inv;
	
	public Bodega() {}

	public String getCodigo() {
		return codigo;
	}

	public void setCodigo(String codigo) {
		this.codigo = codigo;
	}

	public String getDescripcion() {
		return descripcion;
	}

	public void setDescripcion(String descripcion) {
		this.descripcion = descripcion;
	}

	public boolean isActivo() {
		return activo;
	}

	public void setActivo(boolean activo) {
		this.activo = activo;
	}

	public boolean isBod_cd() {
		return bod_cd;
	}

	public void setBod_cd(boolean bod_cd) {
		this.bod_cd = bod_cd;
	}

	public boolean isBod_PV() {
		return bod_PV;
	}

	public void setBod_PV(boolean bod_PV) {
		this.bod_PV = bod_PV;
	}

	public boolean isBod_repos_inv() {
		return bod_repos_inv;
	}

	public void setBod_repos_inv(boolean bod_repos_inv) {
		this.bod_repos_inv = bod_repos_inv;
	}

	@Override
	public String toString() {
		return "Bodega [codigo=" + codigo + ", descripcion=" + descripcion + ", bod_cd=" + bod_cd + ", bod_PV=" + bod_PV
				+ ", bod_repos_inv=" + bod_repos_inv + "]";
	};
	
}
