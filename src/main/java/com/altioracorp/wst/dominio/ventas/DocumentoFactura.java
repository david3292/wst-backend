package com.altioracorp.wst.dominio.ventas;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Transient;

import com.altioracorp.wst.dominio.sistema.TipoDocumento;

@SuppressWarnings("serial")
@Entity
public class DocumentoFactura extends DocumentoBase {

	private long documentoReservaId;

	private String direccionEntrega;

	@Column(columnDefinition = "varchar(MAX)")
	private String direccionEntregaDescripcion;

	private String bodegaPrincipal;

	private boolean facturaIntegrada;

	@Enumerated(EnumType.STRING)
	private Entrega entrega;

	private String docIdGP;

	private BigDecimal total;

	private int periodoGracia;

	private String mensajeError;

	private boolean despachada;

	private String codigoCliente;

	private String nombreCliente;
	
	private String codigoDireccion;
	
    private String descripcionDireccion;

	private boolean refacturacion;
	
	private String retirarCliente;
	
	@Transient
	private boolean tieneGuiaRemision;

	public DocumentoFactura() {
		super();
	}

	public DocumentoFactura(DocumentoNotaCredito documetoNotaCredito, int periodoGracia) {
		super(LocalDateTime.now(), documetoNotaCredito.getCotizacion(), DocumentoEstado.NUEVO, TipoDocumento.FACTURA);
		this.documentoReservaId = documetoNotaCredito.getId();
		this.bodegaPrincipal = documetoNotaCredito.getBodegaPrincipal();
		this.entrega = Entrega.CLIENTE;
		this.direccionEntrega = documetoNotaCredito.getCotizacion().getCodigoDireccion();
		this.direccionEntregaDescripcion = documetoNotaCredito.getCotizacion().getDescripcionDireccion();
		this.periodoGracia = periodoGracia;
		this.despachada = true;
		this.facturaIntegrada = false;
		this.refacturacion = true;

		if (documetoNotaCredito.getTipoDevolucion().equals(TipoDevolucion.REFACTURACION)
				&& documetoNotaCredito.getMotivoNotaCredito().isCambioRazonSocial()) {
			this.codigoCliente = documetoNotaCredito.getCodigoCliente();
			this.nombreCliente = documetoNotaCredito.getNombreCliente();
			this.codigoDireccion = documetoNotaCredito.getCodigoDireccion();
			this.descripcionDireccion = documetoNotaCredito.getDescripcionDireccion();
		} else {
			this.codigoCliente = documetoNotaCredito.getCotizacion().getCodigoCliente();
			this.nombreCliente = documetoNotaCredito.getCotizacion().getNombreCliente();
			this.codigoDireccion = documetoNotaCredito.getCotizacion().getCodigoDireccion();
			this.descripcionDireccion = documetoNotaCredito.getCotizacion().getDescripcionDireccion();
		}

	}

	public DocumentoFactura(DocumentoReserva documetoReserva) {
		super(LocalDateTime.now(), documetoReserva.getCotizacion(), DocumentoEstado.NUEVO, TipoDocumento.FACTURA);
		this.documentoReservaId = documetoReserva.getId();
		this.bodegaPrincipal = documetoReserva.getBodegaPrincipal();
		this.entrega = documetoReserva.getEntrega();
		this.direccionEntrega = documetoReserva.getDireccionEntrega();
		this.direccionEntregaDescripcion = documetoReserva.getDireccionEntregaDescripcion();
		this.periodoGracia = documetoReserva.getPeriodoGracia();
		this.despachada = false;
		this.facturaIntegrada = false;
		this.refacturacion = false;
		this.codigoCliente = documetoReserva.getCotizacion().getCodigoCliente();
		this.nombreCliente = documetoReserva.getCotizacion().getNombreCliente();
		this.codigoDireccion = documetoReserva.getCotizacion().getCodigoDireccion();
		this.descripcionDireccion = documetoReserva.getCotizacion().getDescripcionDireccion();
		this.retirarCliente = documetoReserva.getRetirarCliente();
	}

	public boolean esFacturaIntegrada() {
		return facturaIntegrada;
	}

	public String getBodegaPrincipal() {
		return bodegaPrincipal;
	}

	public String getCodigoCliente() {
		return codigoCliente;
	}

	public String getCodigoDireccion() {
		return codigoDireccion;
	}

	public String getDescripcionDireccion() {
		return descripcionDireccion;
	}

	public String getDireccionEntrega() {
		return direccionEntrega;
	}

	public String getDireccionEntregaDescripcion() {
		return direccionEntregaDescripcion;
	}

	public String getDocIdGP() {
		return docIdGP;
	}

	public long getDocumentoReservaId() {
		return documentoReservaId;
	}

	public Entrega getEntrega() {
		return entrega;
	}

	public String getMensajeError() {
		return mensajeError;
	}

	public String getNombreCliente() {
		return nombreCliente;
	}

	public int getPeriodoGracia() {
		return periodoGracia;
	}

	public BigDecimal getTotal() {
		return total;
	}

	public boolean isDespachada() {
		return despachada;
	}

	public boolean isFacturaIntegrada() {
		return facturaIntegrada;
	}

	public boolean isRefacturacion() {
		return refacturacion;
	}

	public void setBodegaPrincipal(String bodegaPrincipal) {
		this.bodegaPrincipal = bodegaPrincipal;
	}

	public void setCodigoCliente(String codigoCliente) {
		this.codigoCliente = codigoCliente;
	}

	public void setCodigoDireccion(String codigoDireccion) {
		this.codigoDireccion = codigoDireccion;
	}

	public void setDescripcionDireccion(String descripcionDireccion) {
		this.descripcionDireccion = descripcionDireccion;
	}

	public void setDespachada(boolean despachada) {
		this.despachada = despachada;
	}

	public void setDireccionEntrega(String direccionEntrega) {
		this.direccionEntrega = direccionEntrega;
	}

	public void setDireccionEntregaDescripcion(String direccionEntregaDescripcion) {
		this.direccionEntregaDescripcion = direccionEntregaDescripcion;
	}

	public void setDocIdGP(String docIdGP) {
		this.docIdGP = docIdGP;
	}

	public void setDocumentoReservaId(long documentoReservaId) {
		this.documentoReservaId = documentoReservaId;
	}

	public void setEntrega(Entrega entrega) {
		this.entrega = entrega;
	}

	public void setFacturaIntegrada(boolean facturaintegrada) {
		this.facturaIntegrada = facturaintegrada;
	}

	public void setMensajeError(String mensajeError) {
		this.mensajeError = mensajeError;
	}

	public void setNombreCliente(String nombreCliente) {
		this.nombreCliente = nombreCliente;
	}

	public void setPeriodoGracia(int periodoGracia) {
		this.periodoGracia = periodoGracia;
	}

	public void setRefacturacion(boolean refacturacion) {
		this.refacturacion = refacturacion;
	}

	public void setTotal(BigDecimal total) {
		this.total = total;
	}

	public boolean isTieneGuiaRemision() {
		return tieneGuiaRemision;
	}

	public void setTieneGuiaRemision(boolean tieneGuiaRemision) {
		this.tieneGuiaRemision = tieneGuiaRemision;
	}

	public String getRetirarCliente() {
		return retirarCliente;
	}

	public void setRetirarCliente(String retirarCliente) {
		this.retirarCliente = retirarCliente;
	}

	@Override
	public String toString() {
		return "DocumentoFactura [documentoReservaId=" + documentoReservaId + ", direccionEntrega=" + direccionEntrega
				+ ", direccionEntregaDescripcion=" + direccionEntregaDescripcion + ", bodegaPrincipal="
				+ bodegaPrincipal + ", facturaIntegrada=" + facturaIntegrada + ", entrega=" + entrega + ", docIdGP="
				+ docIdGP + ", total=" + total + ", periodoGracia=" + periodoGracia + ", mensajeError=" + mensajeError
				+ ", despachada=" + despachada + ", codigoCliente=" + codigoCliente + ", nombreCliente=" + nombreCliente
				+ ", codigoDireccion=" + codigoDireccion + ", descripcionDireccion=" + descripcionDireccion
				+ ", refacturacion=" + refacturacion + "]";
	}

}
