package com.altioracorp.wst.dominio.ventas.dto;

public class OrdenCompraDetalleDTO {

	private String codigoArticulo;
	
	private String descripcionArticulo;
	
	private String margenUtilidad;
	
	private String margenUtilidadOriginal;
	
	private String nombreProveedor;

	public String getCodigoArticulo() {
		return codigoArticulo;
	}

	public void setCodigoArticulo(String codigoArticulo) {
		this.codigoArticulo = codigoArticulo;
	}

	public String getDescripcionArticulo() {
		return descripcionArticulo;
	}

	public void setDescripcionArticulo(String descripcionArticulo) {
		this.descripcionArticulo = descripcionArticulo;
	}

	public String getMargenUtilidad() {
		return margenUtilidad;
	}

	public void setMargenUtilidad(String margenUtilidad) {
		this.margenUtilidad = margenUtilidad;
	}

	public String getMargenUtilidadOriginal() {
		return margenUtilidadOriginal;
	}

	public void setMargenUtilidadOriginal(String margenUtilidadOriginal) {
		this.margenUtilidadOriginal = margenUtilidadOriginal;
	}

	public String getNombreProveedor() {
		return nombreProveedor;
	}

	public void setNombreProveedor(String nombreProveedor) {
		this.nombreProveedor = nombreProveedor;
	}

}
