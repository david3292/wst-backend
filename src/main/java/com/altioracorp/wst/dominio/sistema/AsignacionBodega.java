package com.altioracorp.wst.dominio.sistema;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.validation.constraints.NotNull;

import com.altioracorp.wst.dominio.EntidadBase;

@SuppressWarnings("serial")
@Entity
public class AsignacionBodega extends EntidadBase {

	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "configuracion_usuario_perfil_id", referencedColumnName = "id", insertable = true, updatable = true)
	private ConfiguracionUsuarioPerfil configuracionUsuarioPerfil;
	
	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "bodega_id", referencedColumnName = "id", insertable = true, updatable = true)
	private Bodega bodega;
	
	@NotNull
	private boolean acceso ;

	public AsignacionBodega() {}
	
	public AsignacionBodega(ConfiguracionUsuarioPerfil configuracionUsuarioPerfil, Bodega bodega, boolean acceso) {
		this.configuracionUsuarioPerfil = configuracionUsuarioPerfil;
		this.bodega = bodega;
		this.acceso = acceso;
	}

	public ConfiguracionUsuarioPerfil getConfiguracionUsuarioPerfil() {
		return configuracionUsuarioPerfil;
	}

	public void setConfiguracionUsuarioPerfil(ConfiguracionUsuarioPerfil configuracionUsuarioPerfil) {
		this.configuracionUsuarioPerfil = configuracionUsuarioPerfil;
	}

	public Bodega getBodega() {
		return bodega;
	}

	public void setBodega(Bodega bodega) {
		this.bodega = bodega;
	}

	public boolean isAcceso() {
		return acceso;
	}

	public void setAcceso(boolean acceso) {
		this.acceso = acceso;
	}

	@Override
	public String toString() {
		return "AsignacionBodega [configuracionUsuarioPerfil=" + configuracionUsuarioPerfil.getUsuarioPerfil() + ", bodega=" + bodega
				+ ", acceso=" + acceso + "]";
	}	
	
	
}
