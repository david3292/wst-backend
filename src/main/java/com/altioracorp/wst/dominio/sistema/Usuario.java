package com.altioracorp.wst.dominio.sistema;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import com.altioracorp.wst.dominio.EntidadBase;

@SuppressWarnings("serial")
@Entity
public class Usuario extends EntidadBase {

	@NotNull
	@NotBlank
	private String nombreUsuario;

	@NotNull
	@NotBlank
	private String nombreCompleto;

	@NotNull
	@NotBlank
	private String correo;

	private String contrasena;

	private boolean activo;

	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "cargo_id", referencedColumnName = "id", insertable = true, updatable = true)
	private Cargo cargo;

	public Usuario() {
	}

	public Usuario(String nombreUsuario, String nombreCompleto, String correo, Cargo cargo) {
		this.nombreUsuario = nombreUsuario;
		this.nombreCompleto = nombreCompleto;
		this.correo = correo;
		this.cargo = cargo;
		this.activo = Boolean.TRUE;
	}

	public String getContrasena() {
		return contrasena;
	}

	public String getNombreUsuario() {
		return nombreUsuario;
	}

	public String getNombreCompleto() {
		return nombreCompleto;
	}

	public String getCorreo() {
		return correo;
	}

	public boolean isActivo() {
		return activo;
	}

	public Cargo getCargo() {
		return cargo;
	}

	public void setNombreUsuario(String nombreUsuario) {
		this.nombreUsuario = nombreUsuario;
	}

	public void setNombreCompleto(String nombreCompleto) {
		this.nombreCompleto = nombreCompleto;
	}

	public void setCorreo(String correo) {
		this.correo = correo;
	}

	public void setContrasena(String contrasena) {
		this.contrasena = contrasena;
	}

	public void setActivo(boolean activo) {
		this.activo = activo;
	}

	public void setCargo(Cargo cargo) {
		this.cargo = cargo;
	}

	@Override
	public String toString() {
		return "Usuario [nombreUsuario=" + nombreUsuario + ", nombreCompleto=" + nombreCompleto + ", correo=" + correo
				+ ", contrasena=" + contrasena + ", activo=" + activo + ", cargo=" + cargo + "]";
	}

}
