package com.altioracorp.wst.dominio.ventas.dto;

import javax.persistence.Column;
import javax.persistence.Entity;

import com.altioracorp.wst.dominio.EntidadBase;
import com.sun.istack.NotNull;


@SuppressWarnings("serial")
@Entity
public class CotizacionControles extends EntidadBase {

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

	public CotizacionControles() {
		
		this.formaPago = Boolean.FALSE;
		this.precioProducto = Boolean.FALSE;
		this.documentoSoportePendiente = Boolean.FALSE;
		this.documentosVencidos = Boolean.FALSE;
		this.creditoCerrado = Boolean.FALSE;
		this.excesoLimiteCredito = Boolean.FALSE;
		this.reservaStock = Boolean.FALSE;
		this.reservaTransito = Boolean.FALSE;
		this.descuentoFijo = Boolean.FALSE;
		this.descripcionArticulo = Boolean.FALSE;
		cambioMargenUtilidad = Boolean.FALSE;
		cambioCondicionPagoProveedor = Boolean.FALSE;
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

	public boolean soloEsExecesoLimiteCredito() {
		return isExcesoLimiteCredito() && ((isPrecioProducto() && isFormaPago() && isCreditoCerrado()
				&& isDocumentoSoportePendiente() && isReservaStock() && isReservaTransito() && isDescuentoFijo() && isDescripcionArticulo()
				&& isCambioMargenUtilidad() && isCambioCondicionPagoProveedor()) == Boolean.FALSE);
	}
	
	public void aprobarControles() {
		formaPago = Boolean.FALSE;
		precioProducto = Boolean.FALSE;
		documentoSoportePendiente = Boolean.FALSE;
		documentosVencidos = Boolean.FALSE;
		creditoCerrado = Boolean.FALSE;
		excesoLimiteCredito = Boolean.FALSE;
		reservaStock = Boolean.FALSE;
		reservaTransito = Boolean.FALSE;
		descuentoFijo = Boolean.FALSE;
		descripcionArticulo = Boolean.FALSE;
		cambioMargenUtilidad = Boolean.FALSE;
		cambioCondicionPagoProveedor = Boolean.FALSE;
	}
}
