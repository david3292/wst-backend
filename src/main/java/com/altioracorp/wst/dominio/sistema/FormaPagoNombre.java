package com.altioracorp.wst.dominio.sistema;

public enum FormaPagoNombre {
	
	ANTICIPO("AT", 9, "Anticipo", false, true), 
	CHEQUE("CH", 0, "Cheque", true, true), 
	CHEQUE_A_FECHA("CHF", 0, "Cheque a Fecha", false, false),
	EFECTIVO("EF", 1, "Efectivo", true, true), 
	NOTA_CREDITO("NC", 8, "Nota Crédito", false, true),
	RETENCION("RT", 0, "Retención", false, false), 
	TARJETA_CREDITO("TC", 2, "Tarjeta Crédito", true, true),
	TARJETA_DEBITO("TD", 1, "Tarjeta Débito", true, true), 
	TRANSFERENCIA("TF", 1, "Transferencias", true, true);

	private String abreviatura;

	private int codigo;

	private String descripcion;

	private boolean integrarCobro;

	private boolean integrarAplicacion;

	private FormaPagoNombre(String abreviatura, int codigo, String descripcion, boolean integrarCobro, boolean integrarAplicacion) {
		
		this.abreviatura = abreviatura;
		this.codigo = codigo;
		this.descripcion = descripcion;
		this.integrarCobro = integrarCobro;
		this.integrarAplicacion = integrarAplicacion;
	}
	
	public String getAbreviatura() {
		return abreviatura;
	}

	public int getCodigo() {
		return codigo;
	}

	public String getDescripcion() {
		return descripcion;
	}

	public boolean isIntegrarAplicacion() {
		return integrarAplicacion;
	}

	public boolean isIntegrarCobro() {
		return integrarCobro;
	}

}
