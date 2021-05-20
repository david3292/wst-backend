package com.altioracorp.wst.dominio.cobros;

import com.altioracorp.wst.dominio.sistema.FormaPagoNombre;
import com.altioracorp.wst.dominio.sistema.PerfilNombre;

public class CajaDetalleConsultaDTO {

	private long idCaja;
	
	private PerfilNombre perfil;
	
	private FormaPagoNombre formaPago;
	
	private String nombreUsuario;

	public long getIdCaja() {
		return idCaja;
	}

	public FormaPagoNombre getFormaPago() {
		return formaPago;
	}

	public PerfilNombre getPerfil() {
		return perfil;
	}

	public String getNombreUsuario() {
		return nombreUsuario;
	}
	
	
}
