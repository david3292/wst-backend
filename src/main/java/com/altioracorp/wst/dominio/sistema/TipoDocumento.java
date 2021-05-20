package com.altioracorp.wst.dominio.sistema;

public enum TipoDocumento {
	COBRO(0, "Cobro", false), COTIZACION(0, "Cotización", false), RESERVA(0, "Reserva", false),
	FACTURA(3, "Factura", true), NOTA_CREDITO(4, "Nota Crédito", true), GUIA_DESPACHO(0, "Guía de despacho", false),
	GUIA_REMISION(0, "Guía de remisión", false), TRANSFERENCIA(0, "Transferencia", false),
	TRANSFERENCIA_SALIDA(3, "Transferencia salida", true), TRANSFERENCIA_INGRESO(3, "Transferencia entrada", true),
	REPOSICION(0, "Reposición", false);

//	SOPTYPE en GP
	private int codigoGP;

	private boolean documentoGP;

	private String descripcion;

	private TipoDocumento(int codigo, String descripcion, Boolean documentoGP) {

		this.setCodigo(codigo);
		this.setDescripcion(descripcion);
		this.setDocumentoGP(documentoGP);
	}

	public boolean esDocumentoGP() {
		return documentoGP;
	}

	public void setDocumentoGP(boolean esDocumentoGP) {
		this.documentoGP = esDocumentoGP;
	}

	public int getCodigo() {
		return codigoGP;
	}

	public void setCodigo(int codigo) {
		this.codigoGP = codigo;
	}

	public String getDescripcion() {
		return descripcion;
	}

	public void setDescripcion(String descripcion) {
		this.descripcion = descripcion;
	}

}
