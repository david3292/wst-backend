package com.altioracorp.wst.dominio.ventas.dto;

import java.util.ArrayList;
import java.util.List;

import com.altioracorp.wst.dominio.ventas.DocumentoEstado;

public class FacturaDTO {

	private Long idFactura;

	private String numeroFactura;

	private List<String> numeroTransferencias = new ArrayList<>();

	private DocumentoEstado estado;

	private String error;

	private boolean facturaDespachada;

	public String getError() {
		return error;
	}

	public FacturaDTO() {
		super();
	}

	public FacturaDTO(Long idFactura, String numeroFactura, boolean facturaDespachada, DocumentoEstado estado,
			String error) {
		this.idFactura = idFactura;
		this.numeroFactura = numeroFactura;
		this.facturaDespachada = facturaDespachada;
		this.estado = estado;
		this.error = error;
	}

	public Long getIdFactura() {
		return idFactura;
	}

	public void setIdFactura(Long idFactura) {
		this.idFactura = idFactura;
	}

	public void setError(String error) {
		this.error = error;
	}

	public void agregarNumeroTransferencia(String numero) {
		numeroTransferencias.add(numero);
	}

	public DocumentoEstado getEstado() {
		return estado;
	}

	public String getNumeroFactura() {
		return numeroFactura;
	}

	public List<String> getNumeroTransferencias() {
		return numeroTransferencias;
	}

	public void setEstado(DocumentoEstado estado) {
		this.estado = estado;
	}

	public void setNumeroFactura(String numeroFactura) {
		this.numeroFactura = numeroFactura;
	}

	public void setNumeroTransferencias(List<String> numeroTransferencias) {
		this.numeroTransferencias = numeroTransferencias;
	}

	public boolean isFacturaDespachada() {
		return facturaDespachada;
	}

	public void setFacturaDespachada(boolean facturaDespachada) {
		this.facturaDespachada = facturaDespachada;
	}

}
