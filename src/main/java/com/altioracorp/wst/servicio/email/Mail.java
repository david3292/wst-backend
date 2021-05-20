package com.altioracorp.wst.servicio.email;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class Mail {

	private String from;
	
	private String to;
	
	private List<String> Cc = new ArrayList<String>();
	
	private String subject;
	
	private String nombrePlantilla;
	
	private Map<String,Object> model;
	
	public Mail() {	}

	public Mail(String from, String to, String subject, String nombrePlantilla, Map<String, Object> model) {
		super();
		this.from = from;
		this.to = to;
		this.subject = subject;
		this.nombrePlantilla = nombrePlantilla;
		this.model = model;
	}
	
	public Mail(String from, String to, List<String> Cc, String subject, String nombrePlantilla, Map<String, Object> model) {
		super();
		this.from = from;
		this.to = to;
		this.Cc= Cc;
		this.subject = subject;
		this.nombrePlantilla = nombrePlantilla;
		this.model = model;
	}

	public String getFrom() {
		return from;
	}

	public void setFrom(String from) {
		this.from = from;
	}

	public String getTo() {
		return to;
	}

	public void setTo(String to) {
		this.to = to;
	}

	public String getSubject() {
		return subject;
	}

	public void setSubject(String subject) {
		this.subject = subject;
	}

	public Map<String, Object> getModel() {
		return model;
	}

	public void setModel(Map<String, Object> model) {
		this.model = model;
	}

	public String getNombrePlantilla() {
		return nombrePlantilla;
	}

	public void setNombrePlantilla(String nombrePlantilla) {
		this.nombrePlantilla = nombrePlantilla;
	}

	public List<String> getCc() {
		return Cc;
	}

	public void setCc(List<String> cc) {
		Cc = cc;
	}
	
}
