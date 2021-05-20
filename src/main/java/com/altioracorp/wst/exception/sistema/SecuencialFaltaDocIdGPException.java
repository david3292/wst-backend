package com.altioracorp.wst.exception.sistema;

@SuppressWarnings("serial")
public class SecuencialFaltaDocIdGPException extends SistemaException {
	private String tipoDocumento;

	public String getTipoDocumento() {
		return tipoDocumento;
	}

	public void setTipoDocumento(String tipoDocumento) {
		this.tipoDocumento = tipoDocumento;
	}
	
	public SecuencialFaltaDocIdGPException(String tipoDocumento) {
		this.setTipoDocumento(tipoDocumento);
	}
	
	@Override
	public String getMessage() {
		return String.format("La secuencia para %s necesita un docId de GP", tipoDocumento);
	}
}
