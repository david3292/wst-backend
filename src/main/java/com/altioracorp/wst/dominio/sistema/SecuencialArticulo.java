package com.altioracorp.wst.dominio.sistema;

import javax.persistence.Entity;
import javax.persistence.Transient;

import com.altioracorp.wst.dominio.EntidadBase;

@SuppressWarnings("serial")
@Entity
public class SecuencialArticulo extends EntidadBase{
	
	private String clase;
	
	private String linea;
	
	private String tipoCompra;
	
	private Integer sucesion;
	
	@Transient
	private String codigoArticulo;

	public String getLinea() {
		return linea;
	}

	public void setLinea(String linea) {
		this.linea = linea;
	}

	public String getTipoCompra() {
		return tipoCompra;
	}

	public void setTipoCompra(String tipoCompra) {
		this.tipoCompra = tipoCompra;
	}

	public Integer getSucesion() {
		return sucesion;
	}

	public void setSucesion(Integer sucesion) {
		this.sucesion = sucesion;
	}

	public String getClase() {
		return clase;
	}

	public void setClase(String clase) {
		this.clase = clase;
	}

	public String getCodigoArticulo() {
		return codigoArticulo;
	}

	public void setCodigoArticulo(String codigoArticulo) {
		this.codigoArticulo = codigoArticulo;
	}

	public void incrementar() {
		++this.sucesion;
	}

}
