package com.altioracorp.wst.util.validacionNumeroIdentificacion;

@SuppressWarnings("serial")
public class ValidacionNumeroIdentificacionPrincipalSucursalInvalidoException extends ValidacionNumeroIdentificacionException {

	private String principalSucursalCadena;

	public ValidacionNumeroIdentificacionPrincipalSucursalInvalidoException(String principalSucursalCadena) {
		this.principalSucursalCadena = principalSucursalCadena;
	}
	
	@Override
	public String getMessage() {
		return String.format("La principal/sucursal es inválido: %s", principalSucursalCadena);
	}
}
