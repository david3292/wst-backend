package com.altioracorp.wst.dominio;

import java.util.Date;

import javax.persistence.EntityListeners;
import javax.persistence.MappedSuperclass;

import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import com.sun.istack.NotNull;

@MappedSuperclass
@EntityListeners(AuditingEntityListener.class)
@SuppressWarnings("serial")
public abstract class EntidadBase extends EntidadBaseId {

	public static final String PROPIEDAD_FECHA_CREACION = "fechaCreacion";
	public static final String PROPIEDAD_FECHA_MODIFICACION = "fechaModificacion";

	@CreatedDate
	private Date creadoFecha;

	@CreatedBy
	@NotNull
	private String creadoPor;

	@LastModifiedDate
	private Date modificadoFecha;

	@LastModifiedBy
	@NotNull
	private String modificadoPor;

	public Date getCreadoFecha() {
		return creadoFecha;
	}

	public String getCreadoPor() {
		return creadoPor;
	}

	public Date getModificadoFecha() {
		return modificadoFecha;
	}

	public String getModificadoPor() {
		return modificadoPor;
	}

	public void setCreadoFecha(Date creadoFecha) {
		this.creadoFecha = creadoFecha;
	}

	public void setCreadoPor(String creadoPor) {
		this.creadoPor = creadoPor;
	}

}
