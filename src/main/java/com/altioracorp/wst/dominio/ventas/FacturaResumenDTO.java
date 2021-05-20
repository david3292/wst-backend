package com.altioracorp.wst.dominio.ventas;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;

public class FacturaResumenDTO {

	private BigDecimal total;
	
	private BigDecimal iva;
	
	private BigDecimal subTotalNoIva;
	
	private BigDecimal subTotalIva;
	
	private List<DocumentoDetalle> detalle;
	
	private List<DocumentoDetalle> detalleTransferencias;
	
	public FacturaResumenDTO() {
		total = BigDecimal.ZERO;		
		iva = BigDecimal.ZERO;		
		subTotalNoIva = BigDecimal.ZERO;		
		subTotalIva = BigDecimal.ZERO;		
		detalle = new ArrayList<>();		
		detalleTransferencias = new ArrayList<>();
	}
	
	public void agregarLineaDetalle(DocumentoDetalle linea) {
		detalle.add(linea);
		calcularValores(linea);
	}
	
	public void agregarLineaTransferencia(DocumentoDetalle lineaTransferencia) {
		detalleTransferencias.add(lineaTransferencia);
	}
	
	private void calcularValores(DocumentoDetalle linea) {
		if(linea.getCotizacionDetalle().getImpuestoValor() == BigDecimal.ZERO) {
			subTotalNoIva = subTotalNoIva.add(calcularValorPorCantidadSeleccionada(linea));
		}else {
			iva = linea.getCotizacionDetalle().getImpuestoValor();
			subTotalIva = subTotalIva.add(calcularValorPorCantidadSeleccionada(linea));
		}
		iva = (subTotalIva.multiply(iva).divide(new BigDecimal(100))).setScale(2, RoundingMode.HALF_UP);
		total = subTotalNoIva.add(subTotalIva).add(iva);
	}
	
	private BigDecimal calcularValorPorCantidadSeleccionada(DocumentoDetalle linea) {
		BigDecimal subtotal = linea.getCotizacionDetalle().getSubTotal();		
		return linea.getCantidad().multiply(subtotal);
	}
	
	public BigDecimal getTotal() {
		return total.setScale(2, RoundingMode.HALF_UP);
	}
	
	public void setTotal(BigDecimal total) {
		this.total = total;
	}
	
	public BigDecimal getIva() {
		return iva;
	}
	
	public void setIva(BigDecimal iva) {
		this.iva = iva;
	}

	public BigDecimal getSubTotalNoIva() {
		return subTotalNoIva.setScale(2, RoundingMode.HALF_UP);
	}
	
	public void setSubTotalNoIva(BigDecimal subTotalNoIva) {
		this.subTotalNoIva = subTotalNoIva;
	}
	
	public BigDecimal getSubTotalIva() {
		return subTotalIva.setScale(2, RoundingMode.HALF_UP);
	}
	
	public void setSubTotalIva(BigDecimal subTotalIva) {
		this.subTotalIva = subTotalIva;
	}
	
	public List<DocumentoDetalle> getDetalle() {
		return detalle;
	}

	public List<DocumentoDetalle> getDetalleTransferencias() {
		return detalleTransferencias;
	}
	
	
}
