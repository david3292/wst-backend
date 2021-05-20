package com.altioracorp.wst.dominio.cobros;

import java.math.BigDecimal;
import java.time.LocalDate;

import com.altioracorp.wst.dominio.sistema.TipoDocumento;

public class CobroDocumentoValorDTO {

	private Long idDocumento;

	private TipoDocumento tipo;

	private String numeroDocumento;

	private BigDecimal valor;
	
	private String nombreCliente;
	
	private LocalDate fechaMasAntigua;
	
	private boolean activo;

	public CobroDocumentoValorDTO() {
	}
	
	public CobroDocumentoValorDTO(Long idDocumento, TipoDocumento tipo, String numeroDocumento, BigDecimal valor,
			String nombreCliente, LocalDate fechaMasAntigua, boolean activo) {
		this.idDocumento = idDocumento;
		this.tipo = tipo;
		this.numeroDocumento = numeroDocumento;
		this.valor = valor;
		this.nombreCliente = nombreCliente;
		this.fechaMasAntigua = fechaMasAntigua;
		this.activo = activo;
	}
	
	public String getValorDescripcion() {
		return String.format("%s - $ %s", numeroDocumento, valor);
	}

	public Long getIdDocumento() {
		return idDocumento;
	}

	public void setIdDocumento(Long idDocumento) {
		this.idDocumento = idDocumento;
	}

	public TipoDocumento getTipo() {
		return tipo;
	}

	public void setTipo(TipoDocumento tipo) {
		this.tipo = tipo;
	}

	public String getNumeroDocumento() {
		return numeroDocumento;
	}

	public void setNumeroDocumento(String numeroDocumento) {
		this.numeroDocumento = numeroDocumento;
	}

	public void setValor(BigDecimal valor) {
		this.valor = valor;
	}

	public String getNombreCliente() {
		return nombreCliente;
	}

	public void setNombreCliente(String nombreCliente) {
		this.nombreCliente = nombreCliente;
	}

	public BigDecimal getValor() {
		return valor;
	}

	public LocalDate getFechaMasAntigua() {
		return fechaMasAntigua;
	}

	public void setFechaMasAntigua(LocalDate fechaMasAntigua) {
		this.fechaMasAntigua = fechaMasAntigua;
	}

	public boolean isActivo() {
		return activo;
	}

	public void setActivo(boolean activo) {
		this.activo = activo;
	}	
}
