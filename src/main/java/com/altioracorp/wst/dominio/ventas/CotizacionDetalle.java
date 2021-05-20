package com.altioracorp.wst.dominio.ventas;

import java.math.BigDecimal;
import java.math.RoundingMode;

import javax.persistence.Column;
import javax.persistence.Entity;

import com.altioracorp.wst.dominio.EntidadBase;

/**
 * @author dquimbiulco
 *
 */
@SuppressWarnings("serial")
@Entity
public class CotizacionDetalle extends EntidadBase {

	private String codigoArticulo;

	private String codigoArticuloAlterno;

	private String descripcionArticulo;
	
	private String descripcionArticuloGP;

	private String claseArticulo;

	private BigDecimal pesoArticulo;

	@Column(columnDefinition = "numeric(19,5)")
	private BigDecimal cantidad;

	private BigDecimal precio = BigDecimal.ZERO;
	
	private BigDecimal precioGP = BigDecimal.ZERO;

	private String impuestoDetalle;

	private BigDecimal impuestoValor = BigDecimal.ZERO;

	private BigDecimal descuentoFijo = BigDecimal.ZERO;
	
	private BigDecimal descuentoFijoGP = BigDecimal.ZERO;

	private BigDecimal subTotal = BigDecimal.ZERO;

	private BigDecimal descuentoAdicional = BigDecimal.ZERO;

	private BigDecimal total = BigDecimal.ZERO;

	private Boolean reservaStock;

	private BigDecimal precioUnitario;

	private BigDecimal valorDescuento;
	
	private BigDecimal costoUnitario;

	@Column(columnDefinition = "numeric(19,5)")
	private BigDecimal saldo;

	private String unidadMedida;
	
	private String unidadMedidaCompra;

	private Boolean servicio = false;
	
	private boolean generaCompra;
	
	@Column(columnDefinition = "numeric(19,5)")
	private BigDecimal cantidadReserva = BigDecimal.ZERO;

	public CotizacionDetalle() {
	}

	public void calcularPrecioUnitario() {
		BigDecimal descuento = (precio.multiply(descuentoAdicional).divide(new BigDecimal(100))).setScale(2,
				RoundingMode.HALF_UP);
		precioUnitario = precio.subtract(descuento);
	}

	public void calcularSubtotal() {
		subTotal = (precioUnitario.subtract(valorDescuento));
	}

	public void calcularTotal() {
		total = subTotal.multiply(cantidad);
	}

	public void calcularValorDescuento() {
		valorDescuento = (precioUnitario.multiply(descuentoFijo.divide(new BigDecimal(100)))).setScale(2,
				RoundingMode.HALF_UP);
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

	public String getDescripcionArticulo() {
		return descripcionArticulo;
	}

	public BigDecimal getDescuentoAdicional() {
		return descuentoAdicional;
	}

	public BigDecimal getDescuentoFijo() {
		return descuentoFijo;
	}

	public String getImpuestoDetalle() {
		return impuestoDetalle;
	}

	public BigDecimal getImpuestoValor() {
		return impuestoValor;
	}

	public BigDecimal getPrecio() {
		return precio;
	}

	public BigDecimal getPrecioUnitario() {
		return precioUnitario;
	}

	public Boolean getReservaStock() {
		return reservaStock;
	}

	public BigDecimal getSaldo() {
		return saldo;
	}

	public Boolean getServicio() {
		return servicio;
	}

	public BigDecimal getSubTotal() {
		return subTotal;
	}

	public BigDecimal getTotal() {
		return total;
	}

	public String getUnidadMedida() {
		return unidadMedida;
	}

	public BigDecimal getValorDescuento() {
		return valorDescuento;
	}

	public void setCantidad(BigDecimal cantidad) {
		this.cantidad = cantidad;
		this.saldo = cantidad;
	}

	public void setClaseArticulo(String claseArticulo) {
		this.claseArticulo = claseArticulo;
	}

	public void setCodigoArticulo(String codigoArticulo) {
		this.codigoArticulo = codigoArticulo;
	}

	public void setDescripcionArticulo(String descripcionArticulo) {
		this.descripcionArticulo = descripcionArticulo;
	}

	public void setDescuentoAdicional(BigDecimal descuentoAdicional) {
		this.descuentoAdicional = descuentoAdicional;
	}

	public void setDescuentoFijo(BigDecimal descuentoFijo) {
		this.descuentoFijo = descuentoFijo;
	}

	public void setImpuestoDetalle(String impuestoDetalle) {
		this.impuestoDetalle = impuestoDetalle;
	}

	public void setImpuestoValor(BigDecimal impuestoValor) {
		this.impuestoValor = impuestoValor;
	}

	public void setPrecio(BigDecimal precio) {
		this.precio = precio;
	}

	public void setPrecioUnitario(BigDecimal precioUnitario) {
		this.precioUnitario = precioUnitario;
	}

	public void setReservaStock(Boolean reservaStock) {
		this.reservaStock = reservaStock;
	}

	public void setSaldo(BigDecimal saldo) {
		this.saldo = saldo;
	}

	public void setServicio(Boolean servicio) {
		this.servicio = servicio;
	}

	public void setSubTotal(BigDecimal subTotal) {
		this.subTotal = subTotal;
	}

	public void setTotal(BigDecimal total) {
		this.total = total;
	}

	public void setUnidadMedida(String unidadMedida) {
		this.unidadMedida = unidadMedida;
	}

	public void setValorDescuento(BigDecimal valorDescuento) {
		this.valorDescuento = valorDescuento;
	}


	public BigDecimal getPesoArticulo() {
		return pesoArticulo;
	}

	public void setPesoArticulo(BigDecimal pesoArticulo) {
		this.pesoArticulo = pesoArticulo;
	}

	public String getCodigoArticuloAlterno() {
		return codigoArticuloAlterno;
	}

	public void setCodigoArticuloAlterno(String codigoArticuloAlterno) {
		this.codigoArticuloAlterno = codigoArticuloAlterno;
	}

	public String getDescripcionArticuloGP() {
		return descripcionArticuloGP;
	}

	public void setDescripcionArticuloGP(String descripcionArticuloGP) {
		this.descripcionArticuloGP = descripcionArticuloGP;
	}

	public BigDecimal getPrecioGP() {
		return precioGP;
	}

	public void setPrecioGP(BigDecimal precioGP) {
		this.precioGP = precioGP;
	}

	public BigDecimal getDescuentoFijoGP() {
		return descuentoFijoGP;
	}

	public void setDescuentoFijoGP(BigDecimal descuentoFijoGP) {
		this.descuentoFijoGP = descuentoFijoGP;
	}

	public boolean getGeneraCompra() {
		return generaCompra;
	}

	public void setGeneraCompra(boolean generaCompra) {
		this.generaCompra = generaCompra;
	}

	public BigDecimal getCostoUnitario() {
		return costoUnitario;
	}

	public void setCostoUnitario(BigDecimal costoUnitario) {
		this.costoUnitario = costoUnitario;
	}

	public String getUnidadMedidaCompra() {
		return unidadMedidaCompra;
	}

	public void setUnidadMedidaCompra(String unidadMedidaCompra) {
		this.unidadMedidaCompra = unidadMedidaCompra;
	}

	public BigDecimal getCantidadReserva() {
		return cantidadReserva;
	}

	public void setCantidadReserva(BigDecimal cantidadReserva) {
		this.cantidadReserva = cantidadReserva;
	}
		
}
