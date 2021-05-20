package com.altioracorp.wst.exception.sistema;

@SuppressWarnings("serial")
public abstract class SistemaException extends RuntimeException {

	@Override
	public abstract String getMessage();
}
