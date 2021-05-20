package com.altioracorp.wst.dominio.ventas;

public class StockSolicitudDTO {

	private String itemnmbr;
	private String puntoVenta;
	private String bodega;

	public StockSolicitudDTO() { }
	
	public StockSolicitudDTO(String itemnmbr, String bodega) {
		super();
		this.itemnmbr = itemnmbr;
		this.bodega = bodega;
	}

	public String getBodega() {
		return bodega;
	}

	public void setBodega(String bodega) {
		this.bodega = bodega;
	}

	public String getItemnmbr() {
		return itemnmbr;
	}

	public void setItemnmbr(String itemnmbr) {
		this.itemnmbr = itemnmbr;
	}

	public String getPuntoVenta() {
		return puntoVenta;
	}

	public void setPuntoVenta(String puntoVenta) {
		this.puntoVenta = puntoVenta;
	}

}
