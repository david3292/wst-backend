package com.altioracorp.wst;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer;
import org.springframework.security.oauth2.config.annotation.web.configuration.ResourceServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configurers.ResourceServerSecurityConfigurer;
import org.springframework.security.oauth2.provider.token.ResourceServerTokenServices;

@Configuration
@EnableResourceServer
public class ConfigServidorRecurso extends ResourceServerConfigurerAdapter {

	@Autowired
	private ResourceServerTokenServices tokenServices;

	@Value("${security.jwt.resource-ids}")
	private String resourceIds;

	@Override
	public void configure(ResourceServerSecurityConfigurer resources) throws Exception {
		resources.resourceId(resourceIds).tokenServices(tokenServices);
	}

	/*
	 * En este métod definiremos las URL de los servicios que necesitas agregar
	 * seguridad con tokens .exceptionHandling().authenticationEntryPoint(new
	 * AuthException()) para enviar exepción del acceso denegado
	 */
	@Override
	public void configure(HttpSecurity http) throws Exception {
		http
		.exceptionHandling()
		.authenticationEntryPoint(new AutorizacionException())
		.and()
		.requestMatchers()
		.and()
		.authorizeRequests()
		.antMatchers("/usuarios/**").authenticated()
		.antMatchers("/areas/**").authenticated()
		.antMatchers("/cargos/**").authenticated()
		.antMatchers("/menus/**").authenticated()
		.antMatchers("/configuraciones/**").authenticated()
		.antMatchers("/configuraciones-usuario-perfil/**").authenticated()
		.antMatchers("/asignacion-bodega/**").authenticated()
		.antMatchers("/puntos-venta/**").authenticated()
		.antMatchers("/pvta-bodegas/**").authenticated()
		.antMatchers("/condiciones-pago/**").authenticated()
		.antMatchers("/ldap/**").authenticated()
		.antMatchers("/empresa/**").authenticated()
		.antMatchers("/clientes/**").authenticated()
		.antMatchers("/articulos/**").authenticated()
		.antMatchers("/transferencias/**").authenticated()
		.antMatchers("/cotizaciones/**").authenticated()
		.antMatchers("/aprobacionesDocumento/**").authenticated()
		.antMatchers("/secuenciales/**").authenticated()
		.antMatchers("/reservaciones/**").authenticated()
		.antMatchers("/facturaciones/**").authenticated()
		.antMatchers("/formasPago/**").authenticated()
		.antMatchers("/formasPagoPuntoVenta/**").authenticated()
		.antMatchers("/cobros/**").authenticated()
		.antMatchers("/despachos/**").authenticated()
		.antMatchers("/cierreCajas/**").authenticated()
		.antMatchers("/guiasRemision/**").authenticated()
		.antMatchers("/reposiciones/**").authenticated()
		.antMatchers("/chequesPosfechados/**").authenticated()
		.antMatchers("/tokens/**").permitAll();

	}

}
