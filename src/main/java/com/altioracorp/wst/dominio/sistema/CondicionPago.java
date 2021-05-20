package com.altioracorp.wst.dominio.sistema;

import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import com.altioracorp.wst.dominio.EntidadBase;

@SuppressWarnings("serial")
@Entity
public class CondicionPago extends EntidadBase {

	@NotNull
	@Size(max = 64, message = "El campo de término no puede tener mas de 64 caractéres")
	private String termino;
	
	@NotNull
	private Integer cuotas;
	
	@NotNull
	private Integer totalDias;
	
	@NotNull
	private boolean documentoSoporte;
	
	@NotNull
	private boolean activo;
	
	@NotNull
	@Enumerated(EnumType.STRING)
	private TipoPago tipoPago; 
	
	@OneToMany(cascade = {CascadeType.ALL}, fetch = FetchType.EAGER, orphanRemoval = true)
	@JoinColumn(name = "condicion_pago_id", nullable = false)
	private List<CondicionPagoDetalle> detalle;

	public CondicionPago() {}
	
	public CondicionPago(String termino, int cuotas, int totalDias, boolean documentoSoporte, boolean activo,
			TipoPago tipoPago) {
		this.termino = termino;
		this.cuotas = cuotas;
		this.totalDias = totalDias;
		this.documentoSoporte = documentoSoporte;
		this.activo = activo;
		this.tipoPago = tipoPago;
	}

	public String getTermino() {
		return termino;
	}

	public void setTermino(String termino) {
		this.termino = termino;
	}

	public Integer getCuotas() {
		return cuotas;
	}

	public void setCuotas(Integer cuotas) {
		this.cuotas = cuotas;
	}

	public Integer getTotalDias() {
		return totalDias;
	}

	public void setTotalDias(Integer totalDias) {
		this.totalDias = totalDias;
	}

	public boolean isDocumentoSoporte() {
		return documentoSoporte;
	}

	public void setDocumentoSoporte(boolean documentoSoporte) {
		this.documentoSoporte = documentoSoporte;
	}

	public boolean isActivo() {
		return activo;
	}

	public void setActivo(boolean activo) {
		this.activo = activo;
	}

	public TipoPago getTipoPago() {
		return tipoPago;
	}

	public void setTipoPago(TipoPago tipoPago) {
		this.tipoPago = tipoPago;
	}

	public List<CondicionPagoDetalle> getDetalle() {
		return detalle;
	}

	public void setDetalle(List<CondicionPagoDetalle> detalle) {
		this.detalle = detalle;
	}

	@Override
	public String toString() {
		return "CondicionPago [termino=" + termino + ", cuotas=" + cuotas + ", totalDias=" + totalDias
				+ ", documentoSoporte=" + documentoSoporte + ", activo=" + activo + ", tipoPago=" + tipoPago + "]";
	}
}
