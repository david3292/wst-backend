package com.altioracorp.wst.dominio.ldap;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.data.ldap.repository.config.EnableLdapRepositories;
import org.springframework.ldap.core.LdapTemplate;
import org.springframework.ldap.core.support.LdapContextSource;

@Configuration
@EnableLdapRepositories(basePackages = "com.altioracorp.wst.repositorio.ldap")
public class ConfiguracionLdap {

	@Bean
	public LdapTemplate ldapTemplate(@Autowired Environment env) {
		LdapTemplate ldaTemplate = new LdapTemplate(contextSource(env));
		ldaTemplate.setIgnorePartialResultException(Boolean.TRUE);
	    return ldaTemplate;
	}

	@Bean
	public LdapContextSource contextSource(Environment env) {
		LdapContextSource contextSource = new LdapContextSource();
		contextSource.setUrl(env.getRequiredProperty("ldap.url"));
		contextSource.setBase(env.getRequiredProperty("ldap.partitionSuffix"));
		contextSource.setUserDn(env.getRequiredProperty("ldap.principal"));
		contextSource.setPassword(env.getRequiredProperty("ldap.password"));
		contextSource.setPooled(false);
		
		return contextSource;
	}
}
