package com.altioracorp.wst.xml.guiaRemision;

import javax.xml.bind.annotation.XmlEnum;
import javax.xml.bind.annotation.XmlType;

@XmlType(name = "obligadoContabilidad", namespace = "")
@XmlEnum
public enum ObligadoContabilidad {
	
	SI,
	NO;
	
	public String value() {
        return name();
    }

    public static ObligadoContabilidad fromValue(String v) {
        return valueOf(v);
    }
    
}
