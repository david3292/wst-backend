package com.altioracorp.wst.dominio.cobros;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

public class CobroReporteDTO {

	private String nombreUsuarioCompleto;
	
	private String nombreUsuario;
	
	private String puntoVenta;
	
	private LocalDateTime fecha;
	
	private String numero;
	
	private String estado;
	
	private String codigoCliente;
	
	private String nombreCliente;
	
	private String direccionCliente;
	
	private BigDecimal totalCobro = BigDecimal.ZERO;
	
	private BigDecimal totalAplicado = BigDecimal.ZERO;
	
	private BigDecimal saldoFavor = BigDecimal.ZERO;
	
	private List<CobroReporteDetalleDTO> detalle;

	public CobroReporteDTO(String nombreUsuarioCompleto, String nombreUsuario, String puntoVenta, LocalDateTime fecha,
			String numero, String estado, List<CobroReporteDetalleDTO> detalle, String codigoCliente, String nombreCliente, String direccionCliente, BigDecimal total) {
		super();
		this.nombreUsuarioCompleto = nombreUsuarioCompleto;
		this.nombreUsuario = nombreUsuario;
		this.puntoVenta = puntoVenta;
		this.fecha = fecha;
		this.numero = numero;
		this.estado = estado;
		this.detalle = detalle;
		this.codigoCliente = codigoCliente;
		this.nombreCliente = nombreCliente;
		this.direccionCliente = direccionCliente;
		this.totalCobro = total;
		calcularTotales();
	}
	
	private void calcularTotales() {
		totalAplicado = detalle.stream().filter(x -> x.isAplicado()).map(x -> x.getValor()).reduce(BigDecimal.ZERO,
				BigDecimal::add);
		saldoFavor = totalCobro.subtract(totalAplicado);
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

	public String getNumero() {
		return numero;
	}

	public String getEstado() {
		return estado;
	}

	public List<CobroReporteDetalleDTO> getDetalle() {
		return detalle;
	}

	public String getCodigoCliente() {
		return codigoCliente;
	}

	public String getNombreCliente() {
		return nombreCliente;
	}

	public String getDireccionCliente() {
		return direccionCliente;
	}

	public BigDecimal getTotalCobro() {
		return totalCobro;
	}

	public BigDecimal getTotalAplicado() {
		return totalAplicado;
	}

	public BigDecimal getSaldoFavor() {
		return saldoFavor;
	}
		
}
