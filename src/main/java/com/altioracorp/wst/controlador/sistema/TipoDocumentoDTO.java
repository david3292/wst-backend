package com.altioracorp.wst.controlador.sistema;

import com.altioracorp.wst.dominio.sistema.TipoDocumento;

public class TipoDocumentoDTO {

	private int codigo;

	private boolean documentoGP;

	private String descripcion;
	
	private TipoDocumento tipoDocumento;

	public TipoDocumentoDTO(TipoDocumento tipoDocumento) {
		this.codigo = tipoDocumento.getCodigo();
		this.documentoGP = tipoDocumento.esDocumentoGP();
		this.descripcion = tipoDocumento.getDescripcion();
		this.tipoDocumento = tipoDocumento;
	}
	
	public TipoDocumentoDTO() {
		
	}

	public boolean getDocumentoGP() {
		return documentoGP;
	}

	public int getCodigo() {
		return codigo;
	}

	public String getDescripcion() {
		return descripcion;
	}

	public TipoDocumento getTipoDocumento() {
		return tipoDocumento;
	}

}
