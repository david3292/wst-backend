package com.altioracorp.wst.exception.migracion;

@SuppressWarnings("serial")
public class MigradorDatosException extends RuntimeException {

	public MigradorDatosException(Exception e) {
		super(e);
	}
}