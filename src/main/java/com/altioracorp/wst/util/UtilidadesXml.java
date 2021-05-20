package com.altioracorp.wst.util;

import java.io.StringWriter;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;

import org.apache.commons.lang3.StringUtils;

public class UtilidadesXml {

	public static String generarXml(Object documento, Class<?> ... classesToBeBound) throws JAXBException {
		String xml = StringUtils.EMPTY;
		
		JAXBContext jContext = JAXBContext.newInstance(classesToBeBound);
		Marshaller m = jContext.createMarshaller();
		m.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.FALSE);
		m.setProperty(Marshaller.JAXB_FRAGMENT, true);
		
		StringWriter sw = new StringWriter();
		m.marshal(documento, sw);
		
		xml = sw.toString();
		
		return xml;
	}
	
}
