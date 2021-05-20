package com.altioracorp.wst.dominio.ventas;

import com.altioracorp.wst.dominio.EntidadBase;
import com.altioracorp.wst.dominio.sistema.TipoDocumento;
import com.altioracorp.wst.util.LocalDateTimeDeserialize;
import com.altioracorp.wst.util.LocalDateTimeSerialize;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

import org.apache.commons.collections.CollectionUtils;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@SuppressWarnings("serial")
@Entity
@Inheritance(strategy = InheritanceType.JOINED)
@Table(name = "documento")
public abstract class DocumentoBase extends EntidadBase {

	@JsonSerialize(using = LocalDateTimeSerialize.class)
	@JsonDeserialize(using = LocalDateTimeDeserialize.class)
	private LocalDateTime fechaEmision;

	@ManyToOne(fetch = FetchType.EAGER)
	// @JoinColumn(name = "cotizacion_id", referencedColumnName = "id", insertable =
	// false, updatable = false)
	private Cotizacion cotizacion;

	@Enumerated(EnumType.STRING)
	@NotNull
	private DocumentoEstado estado;

	@Enumerated(EnumType.STRING)
	@NotNull
	private TipoDocumento tipo;

	private String numero;

	@OneToMany(cascade = { CascadeType.ALL }, fetch = FetchType.LAZY, orphanRemoval = true)
	@JoinColumn(name = "documento_id", nullable = false)
	private List<DocumentoDetalle> detalle;

	@JsonSerialize(using = LocalDateTimeSerialize.class)
	@JsonDeserialize(using = LocalDateTimeDeserialize.class)
	private LocalDateTime fechaVencimiento;

	private String referencia;

	public DocumentoBase() {
		super();
		this.detalle = new ArrayList<>();
	}

	public DocumentoBase(LocalDateTime fechaEmision, Cotizacion cotizacion, @NotNull DocumentoEstado estado,
			@NotNull TipoDocumento tipo) {
		this(fechaEmision, estado, tipo);
		this.cotizacion = cotizacion;
	}

	public DocumentoBase(LocalDateTime fechaEmision, @NotNull DocumentoEstado estado, @NotNull TipoDocumento tipo) {
		super();
		this.fechaEmision = fechaEmision;
		this.estado = estado;
		this.tipo = tipo;
		this.detalle = new ArrayList<>();
	}

	public void agregarLineaDetalle(DocumentoDetalle linea) {
		detalle.add(linea);
	}

	public void eliminarLineaDetalle(DocumentoDetalle linea) {
		detalle.remove(linea);
	}

	public Cotizacion getCotizacion() {
		return cotizacion;
	}

	public List<DocumentoDetalle> getDetalle() {
		return detalle;
	}

	public DocumentoEstado getEstado() {
		return estado;
	}

	public LocalDateTime getFechaEmision() {
		return fechaEmision;
	}

	public LocalDateTime getFechaVencimiento() {
		return fechaVencimiento;
	}

	public String getNumero() {
		return numero;
	}

	public String getReferencia() {
		return referencia;
	}

	public TipoDocumento getTipo() {
		return tipo;
	}

	public void setCotizacion(Cotizacion cotizacion) {
		this.cotizacion = cotizacion;
	}

	public void setDetalle(List<DocumentoDetalle> detalle) {
		this.detalle = detalle;
	}

	public void setEstado(DocumentoEstado estado) {
		this.estado = estado;
	}

	public void setFechaEmision(LocalDateTime fechaEmision) {
		this.fechaEmision = fechaEmision;
	}

	public void setFechaVencimiento(LocalDateTime fechaVencimiento) {
		this.fechaVencimiento = fechaVencimiento;
	}

	public void setNumero(String numero) {
		this.numero = numero;
	}

	public void setReferencia(String referencia) {
		this.referencia = referencia;
	}

	public void setTipo(TipoDocumento tipo) {
		this.tipo = tipo;
	}

	@Override
	public String toString() {
		return "DocumentoBase [fechaEmision=" + fechaEmision + ", cotizacion=" + cotizacion + ", estado=" + estado
				+ ", tipo=" + tipo + ", numero=" + numero + ", detalle=" + detalle + ", fechaVencimiento="
				+ fechaVencimiento + ", referencia=" + referencia + "]";
	}

	public BigDecimal obtenerCantidadTotalDetalles() {
		return this.getDetalle().stream().map(d -> d.getCantidad()).reduce(BigDecimal.ZERO, BigDecimal::add);
	}

	public BigDecimal obtenerSaldoTotalDetalles() {
		return this.getDetalle().stream().map(d -> d.getSaldo()).reduce(BigDecimal.ZERO, BigDecimal::add);
	}
	
	public void limpiarCantidadesCero() {
		if(CollectionUtils.isNotEmpty(this.detalle))
			this.detalle.removeIf(d -> (d.getCantidad().compareTo(BigDecimal.ZERO) == 0));
	}

}
