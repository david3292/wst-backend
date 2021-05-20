package com.altioracorp.wst.dominio.ventas;

import java.time.LocalDateTime;

import javax.persistence.Entity;
import javax.persistence.Transient;
import javax.validation.constraints.NotNull;

import com.altioracorp.wst.util.LocalDateTimeDeserialize;
import com.altioracorp.wst.util.LocalDateTimeSerialize;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

@SuppressWarnings("serial")
@Entity
public class DocumentoGuiaRemision extends DocumentoBase {

	@NotNull
	private long documentoPadreId;

	@NotNull
	private String nombreConductor;

	@NotNull
	private String cedulaConductor;

	@NotNull
	@JsonSerialize(using = LocalDateTimeSerialize.class)
	@JsonDeserialize(using = LocalDateTimeDeserialize.class)
	private LocalDateTime fechaInicioTraslado;

	@NotNull
	@JsonSerialize(using = LocalDateTimeSerialize.class)
	@JsonDeserialize(using = LocalDateTimeDeserialize.class)
	private LocalDateTime fechaFinTraslado;

	@NotNull
	private String bodegaPartida;

	@NotNull
	private String direccionPartida;

	@NotNull
	private String direccionEntega;

	@NotNull
	private String motivo;

	@NotNull
	private String ruta;

	@NotNull
	private String placa;
	
	private String mensajeError;
	
	private String correo;
	
	@Transient
	private String direccionEntregaCodigo;

	public String getBodegaPartida() {
		return bodegaPartida;
	}

	public String getCedulaConductor() {
		return cedulaConductor;
	}

	public String getDireccionEntega() {
		return direccionEntega;
	}

	public String getDireccionPartida() {
		return direccionPartida;
	}

	public long getDocumentoPadreId() {
		return documentoPadreId;
	}

	public LocalDateTime getFechaFinTraslado() {
		return fechaFinTraslado;
	}

	public LocalDateTime getFechaInicioTraslado() {
		return fechaInicioTraslado;
	}

	public String getMotivo() {
		return this.motivo;
	}

	public String getNombreConductor() {
		return nombreConductor;
	}

	public String getPlaca() {
		return this.placa;
	}

	public String getRuta() {
		return this.ruta;
	}

	public void setBodegaPartida(String bodegaPartida) {
		this.bodegaPartida = bodegaPartida;
	}

	public void setCedulaConductor(String cedulaConductor) {
		this.cedulaConductor = cedulaConductor;
	}

	public void setDireccionEntega(String direccionEntega) {
		this.direccionEntega = direccionEntega;
	}

	public void setDireccionPartida(String direccionPartida) {
		this.direccionPartida = direccionPartida;
	}

	public void setDocumentoPadreId(long documentoPadreId) {
		this.documentoPadreId = documentoPadreId;
	}

	public void setFechaFinTraslado(LocalDateTime fechaFinTraslado) {
		this.fechaFinTraslado = fechaFinTraslado;
	}

	public void setFechaInicioTraslado(LocalDateTime fechaInicioTraslado) {
		this.fechaInicioTraslado = fechaInicioTraslado;
	}

	public void setMotivo(String motivo) {
		this.motivo = motivo;
	}

	public void setNombreConductor(String nombreConductor) {
		this.nombreConductor = nombreConductor;
	}

	public void setPlaca(String placa) {
		this.placa = placa;
	}

	public void setRuta(String ruta) {
		this.ruta = ruta;
	}

	public String getMensajeError() {
		return mensajeError;
	}

	public void setMensajeError(String mensajeError) {
		this.mensajeError = mensajeError;
	}

	public String getCorreo() {
		return correo;
	}

	public void setCorreo(String correo) {
		this.correo = correo;
	}

	public String getDireccionEntregaCodigo() {
		return direccionEntregaCodigo;
	}

	public void setDireccionEntregaCodigo(String direccionEntregaCodigo) {
		this.direccionEntregaCodigo = direccionEntregaCodigo;
	}
	
}
