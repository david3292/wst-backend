package com.altioracorp.wst.exception.logistica;

import com.altioracorp.wst.exception.ventas.VentasException;

@SuppressWarnings("serial")
public class TransferenciaSinDetalleException extends VentasException{

	@Override
	public String getMessage() {		
		return "Error, transferencia sin detalle";
	}

}
