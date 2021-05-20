package com.altioracorp.wst.xml.factura;

import java.math.BigDecimal;
import java.util.List;

import com.altioracorp.wst.xml.guiaRemision.ObligadoContabilidad;

public class InfoFactura {
	 protected String fechaEmision;
     protected String dirEstablecimiento;
     protected String contribuyenteEspecial;
     protected ObligadoContabilidad obligadoContabilidad;
     protected String comercioExterior;
     protected String incoTermFactura;
     protected String lugarIncoTerm;
     protected String paisOrigen;
     protected String puertoEmbarque;
     protected String puertoDestino;
     protected String paisDestino;
     protected String paisAdquisicion;
     
     protected String tipoIdentificacionComprador;
     protected String guiaRemision;
     
     protected String razonSocialComprador;
     
     protected String identificacionComprador;
     protected String direccionComprador;
     
     protected BigDecimal totalSinImpuestos;
     protected BigDecimal totalSubsidio;
     protected String incoTermTotalSinImpuestos;
     
     protected BigDecimal totalDescuento;
     protected String codDocReembolso;
     protected BigDecimal totalComprobantesReembolso;
     protected BigDecimal totalBaseImponibleReembolso;
     protected BigDecimal totalImpuestoReembolso;
     
     protected List<Impuesto> totalConImpuestos;
     protected List<Compensacion> compensaciones;
     protected BigDecimal propina;
     protected BigDecimal fleteInternacional;
     protected BigDecimal seguroInternacional;
     protected BigDecimal gastosAduaneros;
     protected BigDecimal gastosTransporteOtros;
     
     protected BigDecimal importeTotal;
     protected String moneda;
     protected List<Pago> pagos;
     protected BigDecimal valorRetIva;
     protected BigDecimal valorRetRenta;
     
	public String getFechaEmision() {
		return fechaEmision;
	}
	public void setFechaEmision(String fechaEmision) {
		this.fechaEmision = fechaEmision;
	}
	public String getDirEstablecimiento() {
		return dirEstablecimiento;
	}
	public void setDirEstablecimiento(String dirEstablecimiento) {
		this.dirEstablecimiento = dirEstablecimiento;
	}
	public String getContribuyenteEspecial() {
		return contribuyenteEspecial;
	}
	public void setContribuyenteEspecial(String contribuyenteEspecial) {
		this.contribuyenteEspecial = contribuyenteEspecial;
	}
	public ObligadoContabilidad getObligadoContabilidad() {
		return obligadoContabilidad;
	}
	public void setObligadoContabilidad(ObligadoContabilidad obligadoContabilidad) {
		this.obligadoContabilidad = obligadoContabilidad;
	}
	
	public void setObligadoContabilidadString(String obligadoContabilidad) {
		this.obligadoContabilidad = ObligadoContabilidad.valueOf(obligadoContabilidad);
	}
	
	public String getComercioExterior() {
		return comercioExterior;
	}
	public void setComercioExterior(String comercioExterior) {
		this.comercioExterior = comercioExterior;
	}
	public String getIncoTermFactura() {
		return incoTermFactura;
	}
	public void setIncoTermFactura(String incoTermFactura) {
		this.incoTermFactura = incoTermFactura;
	}
	public String getLugarIncoTerm() {
		return lugarIncoTerm;
	}
	public void setLugarIncoTerm(String lugarIncoTerm) {
		this.lugarIncoTerm = lugarIncoTerm;
	}
	public String getPaisOrigen() {
		return paisOrigen;
	}
	public void setPaisOrigen(String paisOrigen) {
		this.paisOrigen = paisOrigen;
	}
	public String getPuertoEmbarque() {
		return puertoEmbarque;
	}
	public void setPuertoEmbarque(String puertoEmbarque) {
		this.puertoEmbarque = puertoEmbarque;
	}
	public String getPuertoDestino() {
		return puertoDestino;
	}
	public void setPuertoDestino(String puertoDestino) {
		this.puertoDestino = puertoDestino;
	}
	public String getPaisDestino() {
		return paisDestino;
	}
	public void setPaisDestino(String paisDestino) {
		this.paisDestino = paisDestino;
	}
	public String getPaisAdquisicion() {
		return paisAdquisicion;
	}
	public void setPaisAdquisicion(String paisAdquisicion) {
		this.paisAdquisicion = paisAdquisicion;
	}
	public String getTipoIdentificacionComprador() {
		return tipoIdentificacionComprador;
	}
	public void setTipoIdentificacionComprador(String tipoIdentificacionComprador) {
		this.tipoIdentificacionComprador = tipoIdentificacionComprador;
	}
	public String getGuiaRemision() {
		return guiaRemision;
	}
	public void setGuiaRemision(String guiaRemision) {
		this.guiaRemision = guiaRemision;
	}
	public String getRazonSocialComprador() {
		return razonSocialComprador;
	}
	public void setRazonSocialComprador(String razonSocialComprador) {
		this.razonSocialComprador = razonSocialComprador;
	}
	public String getIdentificacionComprador() {
		return identificacionComprador;
	}
	public void setIdentificacionComprador(String identificacionComprador) {
		this.identificacionComprador = identificacionComprador;
	}
	public String getDireccionComprador() {
		return direccionComprador;
	}
	public void setDireccionComprador(String direccionComprador) {
		this.direccionComprador = direccionComprador;
	}
	public BigDecimal getTotalSinImpuestos() {
		return totalSinImpuestos;
	}
	public void setTotalSinImpuestos(BigDecimal totalSinImpuestos) {
		this.totalSinImpuestos = totalSinImpuestos;
	}
	public BigDecimal getTotalSubsidio() {
		return totalSubsidio;
	}
	public void setTotalSubsidio(BigDecimal totalSubsidio) {
		this.totalSubsidio = totalSubsidio;
	}
	public String getIncoTermTotalSinImpuestos() {
		return incoTermTotalSinImpuestos;
	}
	public void setIncoTermTotalSinImpuestos(String incoTermTotalSinImpuestos) {
		this.incoTermTotalSinImpuestos = incoTermTotalSinImpuestos;
	}
	public BigDecimal getTotalDescuento() {
		return totalDescuento;
	}
	public void setTotalDescuento(BigDecimal totalDescuento) {
		this.totalDescuento = totalDescuento;
	}
	public String getCodDocReembolso() {
		return codDocReembolso;
	}
	public void setCodDocReembolso(String codDocReembolso) {
		this.codDocReembolso = codDocReembolso;
	}
	public BigDecimal getTotalComprobantesReembolso() {
		return totalComprobantesReembolso;
	}
	public void setTotalComprobantesReembolso(BigDecimal totalComprobantesReembolso) {
		this.totalComprobantesReembolso = totalComprobantesReembolso;
	}
	public BigDecimal getTotalBaseImponibleReembolso() {
		return totalBaseImponibleReembolso;
	}
	public void setTotalBaseImponibleReembolso(BigDecimal totalBaseImponibleReembolso) {
		this.totalBaseImponibleReembolso = totalBaseImponibleReembolso;
	}
	public BigDecimal getTotalImpuestoReembolso() {
		return totalImpuestoReembolso;
	}
	public void setTotalImpuestoReembolso(BigDecimal totalImpuestoReembolso) {
		this.totalImpuestoReembolso = totalImpuestoReembolso;
	}
	public List<Impuesto> getTotalConImpuestos() {
		return totalConImpuestos;
	}
	public void setTotalConImpuestos(List<Impuesto> totalConImpuestos) {
		this.totalConImpuestos = totalConImpuestos;
	}
	public List<Compensacion> getCompensaciones() {
		return compensaciones;
	}
	public void setCompensaciones(List<Compensacion> compensaciones) {
		this.compensaciones = compensaciones;
	}
	public BigDecimal getPropina() {
		return propina;
	}
	public void setPropina(BigDecimal propina) {
		this.propina = propina;
	}
	public BigDecimal getFleteInternacional() {
		return fleteInternacional;
	}
	public void setFleteInternacional(BigDecimal fleteInternacional) {
		this.fleteInternacional = fleteInternacional;
	}
	public BigDecimal getSeguroInternacional() {
		return seguroInternacional;
	}
	public void setSeguroInternacional(BigDecimal seguroInternacional) {
		this.seguroInternacional = seguroInternacional;
	}
	public BigDecimal getGastosAduaneros() {
		return gastosAduaneros;
	}
	public void setGastosAduaneros(BigDecimal gastosAduaneros) {
		this.gastosAduaneros = gastosAduaneros;
	}
	public BigDecimal getGastosTransporteOtros() {
		return gastosTransporteOtros;
	}
	public void setGastosTransporteOtros(BigDecimal gastosTransporteOtros) {
		this.gastosTransporteOtros = gastosTransporteOtros;
	}
	public BigDecimal getImporteTotal() {
		return importeTotal;
	}
	public void setImporteTotal(BigDecimal importeTotal) {
		this.importeTotal = importeTotal;
	}
	public String getMoneda() {
		return moneda;
	}
	public void setMoneda(String moneda) {
		this.moneda = moneda;
	}
	public List<Pago> getPagos() {
		return pagos;
	}
	public void setPagos(List<Pago> pagos) {
		this.pagos = pagos;
	}
	public BigDecimal getValorRetIva() {
		return valorRetIva;
	}
	public void setValorRetIva(BigDecimal valorRetIva) {
		this.valorRetIva = valorRetIva;
	}
	public BigDecimal getValorRetRenta() {
		return valorRetRenta;
	}
	public void setValorRetRenta(BigDecimal valorRetRenta) {
		this.valorRetRenta = valorRetRenta;
	}
     
     
}
