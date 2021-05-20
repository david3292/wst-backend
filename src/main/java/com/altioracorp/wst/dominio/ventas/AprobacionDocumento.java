package com.altioracorp.wst.dominio.ventas;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.ManyToOne;
import javax.validation.constraints.Size;

import com.altioracorp.wst.dominio.EntidadBase;
import com.altioracorp.wst.dominio.sistema.PerfilNombre;
import com.sun.istack.NotNull;

@SuppressWarnings("serial")
@Entity
public class AprobacionDocumento extends EntidadBase {

	@NotNull
	private boolean activo;
	
	private String usuario;
	
	@NotNull
	@Enumerated(EnumType.STRING)
	private PerfilNombre perfil;
	
	@Enumerated(EnumType.STRING)
	private AprobacionEstado estado;
	
	@ManyToOne(fetch = FetchType.EAGER)
	private Cotizacion cotizacion;
	
	@ManyToOne(fetch = FetchType.EAGER)
	private DocumentoFactura factura;
	
	@ManyToOne(fetch = FetchType.EAGER)
	private DocumentoReserva reserva;
	
	@Size(max = 1024 , message = "El campo observación no puede tener mas de 1024 caractéres.")
	private String observacion;
	
	@NotNull
	private boolean formaPago;
	
	@NotNull
	private boolean precioProducto;
	
	@NotNull
	@Column(columnDefinition = "bit default 0")
	private boolean descuentoFijo;
	
	@NotNull
	@Column(columnDefinition = "bit default 0")
	private boolean descripcionArticulo;
	
	@NotNull
	private boolean documentoSoportePendiente;
	
	@NotNull
	private boolean documentosVencidos;
	
	@NotNull
	private boolean creditoCerrado;
	
	@NotNull
	private boolean excesoLimiteCredito;
	
	@NotNull
	private boolean reservaStock;
	
	@NotNull
	private boolean reservaTransito;
	
	@NotNull
	private boolean cambioMargenUtilidad;
	
	@NotNull
	private boolean cambioCondicionPagoProveedor;

	public AprobacionDocumento() {
		activo = Boolean.TRUE;
	}

	public boolean isActivo() {
		return activo;
	}

	public void setActivo(boolean activo) {
		this.activo = activo;
	}

	public String getUsuario() {
		return usuario;
	}

	public void setUsuario(String usuario) {
		this.usuario = usuario;
	}

	public AprobacionEstado getEstado() {
		return estado;
	}

	public void setEstado(AprobacionEstado estado) {
		this.estado = estado;
	}

	public Cotizacion getCotizacion() {
		return cotizacion;
	}

	public void setCotizacion(Cotizacion cotizacion) {
		this.cotizacion = cotizacion;
	}

	public DocumentoFactura getFactura() {
		return factura;
	}

	public void setFactura(DocumentoFactura factura) {
		this.factura = factura;
	}

	public DocumentoReserva getReserva() {
		return reserva;
	}

	public void setReserva(DocumentoReserva reserva) {
		this.reserva = reserva;
	}

	public String getObservacion() {
		return observacion;
	}

	public void setObservacion(String observacion) {
		this.observacion = observacion;
	}

	public boolean isFormaPago() {
		return formaPago;
	}

	public void setFormaPago(boolean formaPago) {
		this.formaPago = formaPago;
	}

	public boolean isPrecioProducto() {
		return precioProducto;
	}

	public void setPrecioProducto(boolean precioProducto) {
		this.precioProducto = precioProducto;
	}

	public boolean isDocumentoSoportePendiente() {
		return documentoSoportePendiente;
	}

	public void setDocumentoSoportePendiente(boolean documentoSoportePendiente) {
		this.documentoSoportePendiente = documentoSoportePendiente;
	}

	public boolean isDocumentosVencidos() {
		return documentosVencidos;
	}

	public void setDocumentosVencidos(boolean documentosVencidos) {
		this.documentosVencidos = documentosVencidos;
	}

	public boolean isCreditoCerrado() {
		return creditoCerrado;
	}

	public void setCreditoCerrado(boolean creditoCerrado) {
		this.creditoCerrado = creditoCerrado;
	}

	public boolean isExcesoLimiteCredito() {
		return excesoLimiteCredito;
	}

	public void setExcesoLimiteCredito(boolean excesoLimiteCredito) {
		this.excesoLimiteCredito = excesoLimiteCredito;
	}

	public boolean isReservaStock() {
		return reservaStock;
	}

	public void setReservaStock(boolean reservaStock) {
		this.reservaStock = reservaStock;
	}

	public boolean isReservaTransito() {
		return reservaTransito;
	}

	public void setReservaTransito(boolean reservaTransito) {
		this.reservaTransito = reservaTransito;
	}

	public PerfilNombre getPerfil() {
		return perfil;
	}

	public void setPerfil(PerfilNombre perfil) {
		this.perfil = perfil;
	}

	public boolean isDescuentoFijo() {
		return descuentoFijo;
	}

	public void setDescuentoFijo(boolean descuentoFijo) {
		this.descuentoFijo = descuentoFijo;
	}

	public boolean isDescripcionArticulo() {
		return descripcionArticulo;
	}

	public void setDescripcionArticulo(boolean descripcionArticulo) {
		this.descripcionArticulo = descripcionArticulo;
	}
	
	public boolean isCambioMargenUtilidad() {
		return cambioMargenUtilidad;
	}

	public void setCambioMargenUtilidad(boolean cambioMargenUtilidad) {
		this.cambioMargenUtilidad = cambioMargenUtilidad;
	}

	public boolean isCambioCondicionPagoProveedor() {
		return cambioCondicionPagoProveedor;
	}

	public void setCambioCondicionPagoProveedor(boolean cambioCondicionPagoProveedor) {
		this.cambioCondicionPagoProveedor = cambioCondicionPagoProveedor;
	}

	@Override
	public String toString() {
		return "AprobacionDocumento [activo=" + activo + ", usuario=" + usuario + ", perfil=" + perfil + ", estado="
				+ estado + ", cotizacion=" + cotizacion + ", factura=" + factura + ", reserva=" + reserva
				+ ", observacion=" + observacion + "]";
	}
	
}
