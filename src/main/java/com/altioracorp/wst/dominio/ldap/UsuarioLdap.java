package com.altioracorp.wst.dominio.ldap;

import java.io.Serializable;

import javax.naming.Name;

import org.springframework.ldap.odm.annotations.Attribute;
import org.springframework.ldap.odm.annotations.Entry;
import org.springframework.ldap.odm.annotations.Id;

import com.fasterxml.jackson.annotation.JsonIgnore;


@SuppressWarnings("serial")
@Entry(objectClasses = {"user"})
public class UsuarioLdap implements Serializable{

	@Id
	@JsonIgnore
	private Name id;
	
	@Attribute(name = "userPassword")
	private String password;

	@Attribute(name = "SamAccountName")
	private String userId;
	
	@Attribute(name = "cn")
	private String userName;
	
	public Name getId() {
		return id;
	}

	public String getPassword() {
		return password;
	}

	public String getUserId() {
		return userId;
	}

	public String getUserIdYUserName() {
		return String.format("%s (%s)", getUserId(), getUserName());
	}

	public String getUserName() {
		return userName;
	}
}
