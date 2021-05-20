package com.altioracorp.wst.dominio.cobros;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public class CobroDocumentoDTO {

	private BigDecimal deudaTotal;
	
	private String tipoCredito;
	
	private String condicionPago;
	
	private List<CuotaDTO> cuotas;
	
	private Long idPuntoVenta;
	
	private boolean facturaContabilizada;

	public CobroDocumentoDTO() {
		this.cuotas =  new ArrayList<>();
	}

	public BigDecimal getDeudaTotal() {
		return deudaTotal;
	}

	public void setDeudaTotal(BigDecimal deudaTotal) {
		this.deudaTotal = deudaTotal;
	}

	public String getTipoCredito() {
		return tipoCredito;
	}

	public void setTipoCredito(String tipoCredito) {
		this.tipoCredito = tipoCredito;
	}

	public String getCondicionPago() {
		return condicionPago;
	}

	public void setCondicionPago(String condicionPago) {
		this.condicionPago = condicionPago;
	}

	public List<CuotaDTO> getCuotas() {
		return cuotas;
	}

	public void setCuotas(List<CuotaDTO> cuotas) {
		this.cuotas = cuotas;
	}
	
	public void agregarCuota(CuotaDTO dto) {
		this.cuotas.add(dto);
	}

	public Long getIdPuntoVenta() {
		return idPuntoVenta;
	}

	public void setIdPuntoVenta(Long idPuntoVenta) {
		this.idPuntoVenta = idPuntoVenta;
	}

	public boolean isFacturaContabilizada() {
		return facturaContabilizada;
	}

	public void setFacturaContabilizada(boolean facturaContabilizada) {
		this.facturaContabilizada = facturaContabilizada;
	}
	
}
