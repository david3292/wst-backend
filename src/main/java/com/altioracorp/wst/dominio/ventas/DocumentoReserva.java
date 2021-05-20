package com.altioracorp.wst.dominio.ventas;

import java.time.LocalDateTime;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.validation.constraints.NotNull;

import com.altioracorp.wst.dominio.sistema.TipoDocumento;
import com.altioracorp.wst.util.LocalDateTimeDeserialize;
import com.altioracorp.wst.util.LocalDateTimeSerialize;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

@SuppressWarnings("serial")
@Entity
public class DocumentoReserva extends DocumentoBase {

	@Enumerated(EnumType.STRING)
	@NotNull
	private TipoReserva tipoReserva;

	@NotNull
	private String bodegaPrincipal;

	@Enumerated(EnumType.STRING)
	private Entrega entrega;

	private String direccionEntrega;

	@Column(columnDefinition = "varchar(MAX)")
	private String direccionEntregaDescripcion;
	
	@NotNull
	private int periodoGracia;

	private Integer porcentajeAbono;

	@JsonSerialize(using = LocalDateTimeSerialize.class)
	@JsonDeserialize(using = LocalDateTimeDeserialize.class)
	private LocalDateTime fechaMaximaAbono;

	@JsonSerialize(using = LocalDateTimeSerialize.class)
	@JsonDeserialize(using = LocalDateTimeDeserialize.class)
	private LocalDateTime fechaMaximaReserva;
	
	private String retirarCliente;

	public void inicializarResarva() {
		this.setEstado(DocumentoEstado.NUEVO);
		this.setTipo(TipoDocumento.RESERVA);
		this.setTipoReserva(TipoReserva.FACTURA);
	}
	
	public String getBodegaPrincipal() {
		return bodegaPrincipal;
	}

	public String getDireccionEntrega() {
		return direccionEntrega;
	}

	public String getDireccionEntregaDescripcion() {
		return direccionEntregaDescripcion;
	}

	public Entrega getEntrega() {
		return entrega;
	}

	public LocalDateTime getFechaMaximaAbono() {
		return fechaMaximaAbono;
	}

	public LocalDateTime getFechaMaximaReserva() {
		return fechaMaximaReserva;
	}

	public int getPeriodoGracia() {
		return periodoGracia;
	}

	public Integer getPorcentajeAbono() {
		return porcentajeAbono;
	}

	public TipoReserva getTipoReserva() {
		return tipoReserva;
	}

	public void setBodegaPrincipal(String bodegaPrincipal) {
		this.bodegaPrincipal = bodegaPrincipal;
	}

	public void setDireccionEntrega(String direccionEntrega) {
		this.direccionEntrega = direccionEntrega;
	}

	public void setDireccionEntregaDescripcion(String direccionEntregaDescripcion) {
		this.direccionEntregaDescripcion = direccionEntregaDescripcion;
	}

	public void setEntrega(Entrega entrega) {
		this.entrega = entrega;
	}

	public void setFechaMaximaAbono(LocalDateTime fechaMaximaAbono) {
		this.fechaMaximaAbono = fechaMaximaAbono;
	}

	public void setFechaMaximaReserva(LocalDateTime fechaMaximaReserva) {
		this.fechaMaximaReserva = fechaMaximaReserva;
	}

	public void setPeriodoGracia(int periodoGracia) {
		this.periodoGracia = periodoGracia;
	}

	public void setPorcentajeAbono(Integer porcentajeAbono) {
		this.porcentajeAbono = porcentajeAbono;
	}

	public void setTipo(TipoReserva tipoReserva) {
		this.tipoReserva = tipoReserva;
	}

	public void setTipoReserva(TipoReserva tipoReserva) {
		this.tipoReserva = tipoReserva;
	}

	public String getRetirarCliente() {
		return retirarCliente;
	}

	public void setRetirarCliente(String retirarCliente) {
		this.retirarCliente = retirarCliente;
	}
	
}
