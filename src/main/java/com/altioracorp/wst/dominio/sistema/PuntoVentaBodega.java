package com.altioracorp.wst.dominio.sistema;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import com.altioracorp.wst.dominio.EntidadBase;

@SuppressWarnings("serial")
@Entity
public class PuntoVentaBodega extends EntidadBase {

	private boolean activo;
	
	private boolean principal;	
	
	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "bodega_id", referencedColumnName = "id", insertable = true, updatable = false)
	private Bodega bodega;
	
	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "punto_venta_id", referencedColumnName = "id", insertable = true, updatable = false)
	private PuntoVenta puntoVenta;
	
	public PuntoVentaBodega(){}

	public boolean isActivo() {
		return activo;
	}

	public void setActivo(boolean activo) {
		this.activo = activo;
	}

	public boolean isPrincipal() {
		return principal;
	}

	public void setPrincipal(boolean principal) {
		this.principal = principal;
	}

	public Bodega getBodega() {
		return bodega;
	}

	public void setBodega(Bodega bodega) {
		this.bodega = bodega;
	}

	public PuntoVenta getPuntoVenta() {
		return puntoVenta;
	}

	public void setPuntoVenta(PuntoVenta puntoVenta) {
		this.puntoVenta = puntoVenta;
	};
	
}
