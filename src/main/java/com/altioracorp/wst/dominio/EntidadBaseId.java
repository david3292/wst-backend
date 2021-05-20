package com.altioracorp.wst.dominio;

import java.io.Serializable;

import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;

@MappedSuperclass
@SuppressWarnings("serial")
public abstract class EntidadBaseId implements Serializable {

	public static final String PROPIEDAD_ID = "id";
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private long id;

	public long getId() {
		return id;
	}
	
	public void setId(Long id) {
		this.id = id;
	}	
	
}
