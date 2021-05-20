package com.altioracorp.wst.exception.sistema;

@SuppressWarnings("serial")
public class AreaYaExistenteException  extends SistemaException{

	private String codigo;
	
	public AreaYaExistenteException(String codigo) {
		this.codigo = codigo;
	}
	
	@Override
	public String getMessage() {		
		return String.format("Área con código %s ya existe", codigo);
	}

}
