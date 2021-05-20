package com.altioracorp.wst.servicio.email;

import java.nio.charset.StandardCharsets;
import java.util.stream.Collectors;

import javax.mail.MessagingException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring5.SpringTemplateEngine;


@Service
public class EmailSercicioImpl implements IEmailServicio{

	private static final Log LOG = LogFactory.getLog(EmailSercicioImpl.class);
	
	@Autowired
	private JavaMailSender emailSender;
	
	@Autowired
	private SpringTemplateEngine templateEngine;
	
	@Override
	@Async
	public void enviarEmailSimple(Mail mail) {
		try {
			MimeMessage message = emailSender.createMimeMessage();
			MimeMessageHelper helper = new MimeMessageHelper(message, MimeMessageHelper.MULTIPART_MODE_MIXED_RELATED, StandardCharsets.UTF_8.name());
			
			Context context = new Context();
			context.setVariables(mail.getModel());
			
			String html = templateEngine.process(String.format("email/%s",mail.getNombrePlantilla()), context);
			helper.setText(html,true);
			helper.setTo(mail.getTo());
			helper.setCc(InternetAddress.parse(mail.getCc().stream().collect(Collectors.joining(","))));
			helper.setSubject(mail.getSubject());
			helper.setFrom(mail.getFrom());
			
			logEnvio(mail.getTo(), mail.getSubject());
			emailSender.send(message);
			
		} catch (MessagingException e) {
			
			logErrorEnvio(mail.getTo(), mail.getSubject(), e);
		}
		
		
	}

	@Override
	public void enviarEmailAdjunto(Mail mail) {
		// TODO Auto-generated method stub
		
	}
	
	
	private void logErrorEnvio(String direccionesDestino, String asunto, MessagingException e) {
		LOG.error(String.format("No se pudo enviar la notificación '%s' a %s", asunto, direccionesDestino), e);
	}
	
	private void logEnvio(String direccionesDestino, String asunto) {
		LOG.info(String.format("Enviando correo de notificación '%s' a %s", asunto, direccionesDestino));
	}

}
