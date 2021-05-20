package com.altioracorp.wst.exception.ventas;

@SuppressWarnings("serial")
public class UsuarioNoTieneCodigoVendedorException extends VentasException {
	
	private String vendedor;
	
	public UsuarioNoTieneCodigoVendedorException(String vendedor) {
		this.vendedor = vendedor;
	}

	@Override
	public String getMessage() {
		return String.format("El vendedor %s, no tiene asociado un cóodigo vendedor de GP", vendedor);
	}
}
