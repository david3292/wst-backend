package com.altioracorp.wst.dominio.cobros;

import java.util.ArrayList;
import java.util.List;

public class CobroDTO {

	private String numero;
	
	private CobroEstado estado;

	public CobroEstado getEstado() {
		return estado;
	}

	public void setEstado(CobroEstado estado) {
		this.estado = estado;
	}

	private List<CobroFormaPagoDTO> cobroFormaPagos = new ArrayList<CobroFormaPagoDTO>();
	
	private List<Long> guiaDespachoIds = new ArrayList<Long>();

	public void agregarFormaPago(CobroFormaPagoDTO formaPago) {
		cobroFormaPagos.add(formaPago);
	}

	public List<CobroFormaPagoDTO> getCobroFormaPagos() {
		return cobroFormaPagos;
	}

	public List<Long> getGuiaDespachoIds() {
		return guiaDespachoIds;
	}

	public String getNumero() {
		return numero;
	}

	public void setCobroFormaPagos(List<CobroFormaPagoDTO> cobroFormaPagos) {
		this.cobroFormaPagos = cobroFormaPagos;
	}

	public void setGuiaDespachoIds(List<Long> guiaDespachoIds) {
		this.guiaDespachoIds = guiaDespachoIds;
	}
	
	public void setNumero(String numero) {
		this.numero = numero;
	}

}
