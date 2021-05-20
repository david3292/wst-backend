package com.altioracorp.wst.dominio.ventas;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.math.RoundingMode;

@SuppressWarnings("serial")
@Entity
public class DocumentoNotaCredito extends DocumentoBase {

	@NotNull
	private long documentoFacturaId;

	@NotNull
	private String bodegaPrincipal;

	@Enumerated(EnumType.STRING)
	private TipoDevolucion tipoDevolucion;

	@NotNull
	private String docIdGP;

	private BigDecimal total;

	private String mensajeError;

	private String motivo;

	private boolean notaCreditoIntegrada;

	@Enumerated(EnumType.STRING)
	private FlujoAprobacion flujoAprobacion;

	private String comentario;

	@ManyToOne(fetch = FetchType.EAGER)
	private MotivoNotaCredito motivoNotaCredito;

	private Boolean revisionTecnica;
	
	//Solo para refacturación por cambio de razón social 
	private String codigoCliente;

	private String nombreCliente;
	
	private String codigoDireccion;
	
    private String descripcionDireccion;

	public String getBodegaPrincipal() {
		return bodegaPrincipal;
	}

	public String getCodigoCliente() {
		return codigoCliente;
	}

	public String getCodigoDireccion() {
		return codigoDireccion;
	}

	public String getComentario() {
		return comentario;
	}

	public String getDescripcionDireccion() {
		return descripcionDireccion;
	}

	public String getDocIdGP() {
		return docIdGP;
	}

	public long getDocumentoFacturaId() {
		return documentoFacturaId;
	}

	public FlujoAprobacion getFlujoAprobacion() {
		return flujoAprobacion;
	}

	public String getMensajeError() {
		return mensajeError;
	}
	
	public String getMotivo() {
		return motivo;
	}

	public MotivoNotaCredito getMotivoNotaCredito() {
		return motivoNotaCredito;
	}

	public String getNombreCliente() {
		return nombreCliente;
	}

	public Boolean getRevisionTecnica() {
		return revisionTecnica;
	}

	public TipoDevolucion getTipoDevolucion() {
		return tipoDevolucion;
	}

	public BigDecimal getTotal() {
		return total;
	}
	
	public BigDecimal getTotalCalculado() {
		BigDecimal subtotal = new BigDecimal(0);
		BigDecimal iva = new BigDecimal(0);
		for (DocumentoDetalle detalle : getDetalle()) {
			BigDecimal subtotalLinea = calcularValorPorCantidadSeleccionada(detalle);
			BigDecimal ivaLinea = detalle.getCotizacionDetalle().getImpuestoValor().multiply(subtotalLinea).divide(new BigDecimal(100)).setScale(2, RoundingMode.HALF_UP);
			subtotal = subtotal.add(subtotalLinea);
			iva = iva.add(ivaLinea);
		}
		return subtotal.add(iva);
	}
	
	private BigDecimal calcularValorPorCantidadSeleccionada(DocumentoDetalle linea) {
		BigDecimal subtotal = linea.getCotizacionDetalle().getSubTotal();		
		return linea.getCantidad().multiply(subtotal);
	}

	public boolean isNotaCreditoIntegrada() {
		return notaCreditoIntegrada;
	}

	public boolean isRevisionTecnica() {
		return revisionTecnica;
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

	public void setComentario(String comentario) {
		this.comentario = comentario;
	}

	public void setDescripcionDireccion(String descripcionDireccion) {
		this.descripcionDireccion = descripcionDireccion;
	}

	public void setDocIdGP(String docIdGP) {
		this.docIdGP = docIdGP;
	}

	public void setDocumentoFacturaId(long documentoFacturaId) {
		this.documentoFacturaId = documentoFacturaId;
	}

	public void setFlujoAprobacion(FlujoAprobacion flujoAprobacion) {
		this.flujoAprobacion = flujoAprobacion;
	}

	public void setMensajeError(String mensajeError) {
		this.mensajeError = mensajeError;
	}

	public void setMotivo(String motivo) {
		this.motivo = motivo;
	}

	public void setMotivoNotaCredito(MotivoNotaCredito motivoNotaCredito) {
		this.motivoNotaCredito = motivoNotaCredito;
	}

	public void setNombreCliente(String nombreCliente) {
		this.nombreCliente = nombreCliente;
	}

	public void setNotaCreditoIntegrada(boolean facturaIntegrada) {
		this.notaCreditoIntegrada = facturaIntegrada;
	}

	public void setRevisionTecnica(boolean revisionTecnica) {
		this.revisionTecnica = revisionTecnica;
	}

	public void setRevisionTecnica(Boolean revisionTecnica) {
		this.revisionTecnica = revisionTecnica;
	}

	public void setTipoDevolucion(TipoDevolucion tipoDevolucion) {
		this.tipoDevolucion = tipoDevolucion;
	}

	public void setTotal(BigDecimal total) {
		this.total = total;
	}

	@Override
	public String toString() {
		return "DocumentoNotaCredito [documentoFacturaId=" + documentoFacturaId + ", bodegaPrincipal=" + bodegaPrincipal
				+ ", tipoDevolucion=" + tipoDevolucion + ", docIdGP=" + docIdGP + ", total=" + total + ", mensajeError="
				+ mensajeError + ", motivo=" + motivo + ", notaCreditoIntegrada=" + notaCreditoIntegrada
				+ ", flujoAprobacion=" + flujoAprobacion + ", comentario=" + comentario + ", motivoNotaCredito="
				+ motivoNotaCredito + ", revisionTecnica=" + revisionTecnica + ", codigoCliente=" + codigoCliente
				+ ", nombreCliente=" + nombreCliente + ", codigoDireccion=" + codigoDireccion
				+ ", descripcionDireccion=" + descripcionDireccion + "]";
	}
}
