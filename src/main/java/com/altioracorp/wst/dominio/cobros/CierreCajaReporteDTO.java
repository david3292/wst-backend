package com.altioracorp.wst.dominio.cobros;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

public class CierreCajaReporteDTO {

	private String nombreUsuarioCompleto;
	
	private String nombreUsuario;
	
	private String puntoVenta;
	
	private LocalDateTime fecha;
	
	private LocalDateTime fechaUltimoCierre;
	
	private List<CajaDetalle> detalle;
	
	private BigDecimal totalCobrado = BigDecimal.ZERO;
	
	private BigDecimal totalContado = BigDecimal.ZERO;
	
	private BigDecimal totalDiferencia = BigDecimal.ZERO;

	public CierreCajaReporteDTO(String nombreUsuarioCompleto, String nombreUsuario, String puntoVenta,
			LocalDateTime fecha, LocalDateTime fechaUltimoCierre, List<CajaDetalle> detalle) {
		super();
		this.nombreUsuarioCompleto = nombreUsuarioCompleto;
		this.nombreUsuario = nombreUsuario;
		this.puntoVenta = puntoVenta;
		this.fecha = fecha;
		this.fechaUltimoCierre = fechaUltimoCierre;
		this.detalle = detalle;
		calcularTotales();
	}
	
	private void calcularTotales() {
		calcularTotalCobrado();
		calcularTotalContado();
		calcularTotalDiferencia();
	}
	
	public void calcularTotalCobrado() {
		this.totalCobrado= this.detalle.stream().map(x ->x.getValorCobrado()).reduce(BigDecimal.ZERO, BigDecimal::add);
	}
	
	public void calcularTotalContado() {
		this.totalContado= this.detalle.stream().map(x ->x.getValorContado()).reduce(BigDecimal.ZERO, BigDecimal::add);
	}
	
	public void calcularTotalDiferencia() {
		this.totalDiferencia = this.detalle.stream().map(x ->x.getDiferencia()).reduce(BigDecimal.ZERO, BigDecimal::add);
	}

	public String getNombreUsuarioCompleto() {
		return nombreUsuarioCompleto;
	}

	public String getNombreUsuario() {
		return nombreUsuario;
	}

	public String getPuntoVenta() {
		return puntoVenta;
	}

	public LocalDateTime getFecha() {
		return fecha;
	}

	public LocalDateTime getFechaUltimoCierre() {
		return fechaUltimoCierre;
	}

	public List<CajaDetalle> getDetalle() {
		return detalle;
	}

	public BigDecimal getTotalCobrado() {
		return totalCobrado;
	}

	public BigDecimal getTotalContado() {
		return totalContado;
	}

	public BigDecimal getTotalDiferencia() {
		return totalDiferencia;
	}
}
