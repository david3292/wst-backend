package com.altioracorp.wst.servicio.email;

public interface IEmailServicio {

	void enviarEmailSimple(Mail mail);
	
	void enviarEmailAdjunto(Mail mail);
}
