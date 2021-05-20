package com.altioracorp.wst.dominio.ventas.dto;

import com.altioracorp.wst.dominio.ventas.AprobacionEstado;
import com.altioracorp.wst.dominio.ventas.FlujoAprobacion;

public class NotaCreditoSolicitudRespuestaDTO {

	private long notaCreditoId;
	
	private AprobacionEstado estado;
	
	private FlujoAprobacion flujoAprobacion;
	
	private String comentario;

	public NotaCreditoSolicitudRespuestaDTO() {
		super();
	}
	
	public long getNotaCreditoId() {
		return notaCreditoId;
	}

	public AprobacionEstado getEstado() {
		return estado;
	}

	public FlujoAprobacion getFlujoAprobacion() {
		return flujoAprobacion;
	}

	public String getComentario() {
		return comentario;
	}
	
}
