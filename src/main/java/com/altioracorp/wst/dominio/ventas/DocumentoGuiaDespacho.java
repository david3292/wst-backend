package com.altioracorp.wst.dominio.ventas;

import java.time.LocalDateTime;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Transient;

import com.altioracorp.wst.dominio.sistema.TipoDocumento;
import com.sun.istack.NotNull;

@SuppressWarnings("serial")
@Entity
public class DocumentoGuiaDespacho extends DocumentoBase {

	@NotNull
	private long documentoFacturaId;

	@NotNull
	private String bodega;

	@Enumerated(EnumType.STRING)
	private Entrega entrega;

	private String direccionEntrega;
	
	@Column(columnDefinition = "varchar(MAX)")
	private String direccionEntregaDescripcion;
	
	public String getDireccionEntregaDescripcion() {
		return direccionEntregaDescripcion;
	}

	public void setDireccionEntregaDescripcion(String direccionEntregaDescripcion) {
		this.direccionEntregaDescripcion = direccionEntregaDescripcion;
	}

	@Transient
	private String numeroCotizacion;
	
	@Transient
	private String numeroFactura;
	
	@Transient
	private String retirarCliente;

	public DocumentoGuiaDespacho() {
		super();
		// TODO Auto-generated constructor stub
	}
	
	public DocumentoGuiaDespacho(DocumentoBase documetoFactura, String bodega, Entrega entrega, String direccionEntrega, String direccionEntregaDescripcion) {
		super(LocalDateTime.now(), documetoFactura.getCotizacion(), DocumentoEstado.NUEVO, TipoDocumento.GUIA_DESPACHO);
		this.documentoFacturaId = documetoFactura.getId();
		this.bodega = bodega;
		this.entrega = entrega;
		this.direccionEntrega = direccionEntrega;
		this.direccionEntregaDescripcion = direccionEntregaDescripcion;
	}

	public String getBodega() {
		return bodega;
	}
	
	public String getDireccionEntrega() {
		return direccionEntrega;
	}

	public long getDocumentoFacturaId() {
		return documentoFacturaId;
	}

	public Entrega getEntrega() {
		return entrega;
	}

	public void setBodega(String bodega) {
		this.bodega = bodega;
	}

	public void setDireccionEntrega(String direccionEntrega) {
		this.direccionEntrega = direccionEntrega;
	}

	public void setDocumentoFacturaId(long documentoFacturaId) {
		this.documentoFacturaId = documentoFacturaId;
	}

	public void setEntrega(Entrega entrega) {
		this.entrega = entrega;
	}	

	public String getNumeroCotizacion() {
		return numeroCotizacion;
	}

	public void setNumeroCotizacion(String numeroCotizacion) {
		this.numeroCotizacion = numeroCotizacion;
	}

	public String getNumeroFactura() {
		return numeroFactura;
	}

	public void setNumeroFactura(String numeroFactura) {
		this.numeroFactura = numeroFactura;
	}
	
	public String getRetirarCliente() {
		return retirarCliente;
	}

	public void setRetirarCliente(String retirarCliente) {
		this.retirarCliente = retirarCliente;
	}

	@Override
	public String toString() {
		return "DocumentoGuiaDespacho [documentoFacturaId=" + documentoFacturaId + ", bodega=" + bodega + ", entrega="
				+ entrega + ", direccionEntrega=" + direccionEntrega + "]";
	}



}
