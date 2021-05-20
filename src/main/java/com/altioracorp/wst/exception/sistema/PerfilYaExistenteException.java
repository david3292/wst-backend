package com.altioracorp.wst.exception.sistema;

@SuppressWarnings("serial")
public class PerfilYaExistenteException  extends SistemaException{

	private String codigo;
	
	public PerfilYaExistenteException(String codigo) {
		this.codigo = codigo;
	}
	
	@Override
	public String getMessage() {		
		return String.format("Perfil %s ya existe", codigo);
	}

}
