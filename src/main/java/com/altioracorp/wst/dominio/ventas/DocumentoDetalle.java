package com.altioracorp.wst.dominio.ventas;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Transient;

import com.altioracorp.wst.dominio.EntidadBase;

@SuppressWarnings("serial")
@Entity
public class DocumentoDetalle extends EntidadBase {

	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "cotizacion_detalle_id", referencedColumnName = "id", insertable = true, updatable = true)
	public CotizacionDetalle cotizacionDetalle;

	private String codigoBodega;

	@Column(columnDefinition = "numeric(19,5)")
	private BigDecimal cantidad;

	@Column(columnDefinition = "numeric(19,5)")
	private BigDecimal saldo;

	private String codigoArticulo;

	private String codigoArticuloAlterno;
	
	private BigDecimal costoUnitario;

	private String descripcionArticulo;

	private String claseArticulo;

	private BigDecimal pesoArticulo;

	private String unidadMedida;
	
	private Boolean servicio = false;

	@OneToMany(cascade = { CascadeType.ALL }, fetch = FetchType.LAZY, orphanRemoval = true)
	@JoinColumn(name = "documento_detalle_id")
	private List<DocumentoDetalleCompartimiento> compartimientos;
	
	@Column(columnDefinition = "numeric(19,5)")
	private BigDecimal cantidadReserva;

	@Transient
	private BigDecimal total;

	public DocumentoDetalle() {
		super();
	}

	public DocumentoDetalle(CotizacionDetalle cotizacionDetalle, String codigoBodega, BigDecimal cantidad) {
		this(codigoBodega, cantidad, cotizacionDetalle.getCodigoArticulo(),
				cotizacionDetalle.getCodigoArticuloAlterno(), cotizacionDetalle.getClaseArticulo(),
				cotizacionDetalle.getDescripcionArticulo(), cotizacionDetalle.getPesoArticulo(),
				cotizacionDetalle.getUnidadMedida());
		this.cotizacionDetalle = cotizacionDetalle;
	}

	public DocumentoDetalle(String codigoBodega, BigDecimal cantidad, String codigoArticulo,
			String codigoArticuloAlterno, String claseArticulo, String descripcionArticulo, BigDecimal pesoArticulo,
			String unidadMedida) {
		super();
		this.codigoBodega = codigoBodega;
		this.cantidad = cantidad;
		this.codigoArticulo = codigoArticulo;
		this.codigoArticuloAlterno = codigoArticuloAlterno;
		this.claseArticulo = claseArticulo;
		this.descripcionArticulo = descripcionArticulo;
		this.pesoArticulo = pesoArticulo;
		this.unidadMedida = unidadMedida;
		this.saldo = cantidad;
		this.compartimientos = new ArrayList<>();
	}
	
	public DocumentoDetalle(CotizacionDetalle cotizacionDetalle, String codigoBodega, BigDecimal cantidad, BigDecimal cantidadReserva) {
		super();
		this.cotizacionDetalle = cotizacionDetalle;
		this.codigoBodega = codigoBodega;
		this.cantidad = cantidad;
		this.saldo = cantidad;
		this.cantidadReserva = cantidadReserva;
		this.servicio = this.cotizacionDetalle.getServicio();
		this.compartimientos = new ArrayList<>();
	}

	public void agregarCompartimiento(DocumentoDetalleCompartimiento compartimiento) {
		compartimientos.add(compartimiento);
	}

	public void calcularTotal() {
		this.total = this.saldo;
	}

	public BigDecimal getCantidad() {
		return cantidad;
	}

	public String getClaseArticulo() {
		return claseArticulo;
	}

	public String getCodigoArticulo() {
		return codigoArticulo;
	}

	public String getCodigoArticuloAlterno() {
		return codigoArticuloAlterno;
	}

	public String getCodigoBodega() {
		return codigoBodega;
	}

	public List<DocumentoDetalleCompartimiento> getCompartimientos() {
		return compartimientos;
	}

	public BigDecimal getCostoUnitario() {
		return costoUnitario;
	}

	public CotizacionDetalle getCotizacionDetalle() {
		return cotizacionDetalle;
	}

	public String getDescripcionArticulo() {
		return descripcionArticulo;
	}

	public BigDecimal getPesoArticulo() {
		return pesoArticulo;
	}

	public BigDecimal getSaldo() {
		return saldo;
	}

	public BigDecimal getTotal() {
		return total;
	}

	public String getUnidadMedida() {
		return unidadMedida;
	}

	public void setCantidad(BigDecimal cantidad) {
		this.cantidad = cantidad;
	}

	public void setClaseArticulo(String claseArticulo) {
		this.claseArticulo = claseArticulo;
	}

	public void setCodigoArticulo(String codigoArticulo) {
		this.codigoArticulo = codigoArticulo;
	}

	public void setCodigoArticuloAlterno(String codigoArticuloAlterno) {
		this.codigoArticuloAlterno = codigoArticuloAlterno;
	}

	public void setCodigoBodega(String codigoBodega) {
		this.codigoBodega = codigoBodega;
	}

	public void setCompartimientos(List<DocumentoDetalleCompartimiento> compartimientos) {
		this.compartimientos = compartimientos;
	}

	public void setCostoUnitario(BigDecimal costoUnitario) {
		this.costoUnitario = costoUnitario;
	}

	public void setCotizacionDetalle(CotizacionDetalle cotizacionDetalle) {
		this.cotizacionDetalle = cotizacionDetalle;
	}

	public void setDescripcionArticulo(String descripcionArticulo) {
		this.descripcionArticulo = descripcionArticulo;
	}

	public void setPesoArticulo(BigDecimal pesoArticulo) {
		this.pesoArticulo = pesoArticulo;
	}

	public void setSaldo(BigDecimal saldo) {
		this.saldo = saldo;
	}

	public void setTotal(BigDecimal total) {
		this.total = total;
	}

	public void setUnidadMedida(String unidadMedida) {
		this.unidadMedida = unidadMedida;
	}
	
	public BigDecimal getCantidadReserva() {
		return cantidadReserva;
	}

	public void setCantidadReserva(BigDecimal cantidadReserva) {
		this.cantidadReserva = cantidadReserva;
	}

	public Boolean getServicio() {
		return servicio;
	}

	public void setServicio(Boolean servicio) {
		this.servicio = servicio;
	}

	@Override
	public String toString() {
		return "DocumentoDetalle [cotizacionDetalle=" + cotizacionDetalle + ", codigoBodega=" + codigoBodega
				+ ", cantidad=" + cantidad + ", saldo=" + saldo + ", codigoArticulo=" + codigoArticulo
				+ ", codigoArticuloAlterno=" + codigoArticuloAlterno + ", descripcionArticulo=" + descripcionArticulo
				+ ", claseArticulo=" + claseArticulo + ", pesoArticulo=" + pesoArticulo + ", unidadMedida="
				+ unidadMedida + ", compartimientos=" + compartimientos + ", total=" + total + "]";
	}

}
