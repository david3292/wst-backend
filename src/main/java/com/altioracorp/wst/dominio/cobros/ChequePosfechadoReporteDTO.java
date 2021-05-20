package com.altioracorp.wst.dominio.cobros;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class ChequePosfechadoReporteDTO {

	private String nombreUsuarioCompleto;

	private String nombreUsuario;

	private LocalDateTime fecha;

	private BigDecimal total = BigDecimal.ZERO;

	private List<ChequePosfechadoReporteDetalleDTO> detalle = new ArrayList<ChequePosfechadoReporteDetalleDTO>();

	public ChequePosfechadoReporteDTO(String nombreUsuarioCompleto, String nombreUsuario, LocalDateTime fecha,
			List<ChequePosfechadoReporteDetalleDTO> detalle) {

		this.nombreUsuarioCompleto = nombreUsuarioCompleto;
		this.nombreUsuario = nombreUsuario;
		this.fecha = fecha;
		this.detalle = detalle;
		calcularTotales();
	}

	private void calcularTotales() {
		total = detalle.stream().map(x -> x.getValor()).reduce(BigDecimal.ZERO, BigDecimal::add);
	}

	public String getNombreUsuarioCompleto() {
		return nombreUsuarioCompleto;
	}

	public String getNombreUsuario() {
		return nombreUsuario;
	}

	public LocalDateTime getFecha() {
		return fecha;
	}

	public String getObservacion() {
		if(!detalle.isEmpty()) {
			return detalle.get(0).getObservacion();
		}
		return null;
	}

	public BigDecimal getTotal() {
		return total;
	}

	public List<ChequePosfechadoReporteDetalleDTO> getDetalle() {
		return detalle;
	}

}
