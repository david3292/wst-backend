package com.altioracorp.wst;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.oauth2.provider.token.DefaultTokenServices;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.security.oauth2.provider.token.store.JwtAccessTokenConverter;
import org.springframework.security.oauth2.provider.token.store.JwtTokenStore;

import com.altioracorp.wst.repositorio.sistema.IConfiguracionUsuarioPerfilRepositorio;
import com.altioracorp.wst.repositorio.sistema.IUsuarioRepositorio;
import com.altioracorp.wst.servicioImpl.ldap.ILdapServicio;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class ConfigSeguridad extends WebSecurityConfigurerAdapter {

//	Traemos las porpiedades definidas en el properties
	@Value("${security.signing-key}")
	private String signingKey;

	@Value("${security.encoding-strength}")
	private Integer encodingStrength;

	@Value("${security.security-realm}")
	private String securityRealm;
	
	@Autowired
	private DataSource dataSource;
	
	@Autowired
	private BCryptPasswordEncoder bcrypt;
	
	@Autowired
	private ILdapServicio ldapServicio;
	
	@Autowired
	private IUsuarioRepositorio usuarioRepositorio;
	
	@Autowired
	private IConfiguracionUsuarioPerfilRepositorio configUsuarioPerfilRepositorio;
	
//	@Autowired	
//	private UserDetailsService userDetailsService;	

	//Función para poner el hash de encode en las contraseñas.
	@Bean
	public BCryptPasswordEncoder passwordEncoder() {
		BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder();
		return bCryptPasswordEncoder;
	}
	
	@Autowired
	@Qualifier(UserDetailsServices.SUPERADMIN)
	private UserDetailsServiceSuperadmin userDetailsServiceSuperadmin;
	
	@Bean
	@Override
	protected AuthenticationManager authenticationManager() throws Exception {
		return super.authenticationManager();
	}
	
	public AuthenticationProviderLDAP authenticationProviderUsuarioLDAP() {
		return new AuthenticationProviderLDAP(ldapServicio, usuarioRepositorio, configUsuarioPerfilRepositorio);
	}
	
//	Como userDetailsService va a tener a todos los usuarios con sus contraseñas se necesita del Bcrypt para comparar claves por ende se hace la inyección
//  de la dependencia BCryptPasswordEncoder
	@Autowired
	public void configure(AuthenticationManagerBuilder auth) throws Exception{
		auth.userDetailsService(userDetailsServiceSuperadmin).passwordEncoder(bcrypt);
		auth.authenticationProvider(authenticationProviderUsuarioLDAP());
		//auth.userDetailsService(userDetailsService).passwordEncoder(bcrypt);
	}
	
	/*
	 * SessionCreationPolicy.STATELESS Spring no conoce el estado del frontend
	 * .csrf() = Tokens que viene por default en SpringSecurity y Evita ataque de javascrpit dentro de un formulario
	 * Se deshabilita .csrf().disable(); porque ya se crea el propio mecanismo con jwt
	 * */
	@Override
	protected void configure(HttpSecurity http) throws Exception {
		http		
        .sessionManagement()
        .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
        .and()
        .httpBasic()
        .realmName(securityRealm)
        .and()
        .csrf()
        .disable();        
	}
	
	/*
	 * Para crear tokens
	 * */
	@Bean
	public JwtAccessTokenConverter accessTokenConverter() {
		JwtAccessTokenConverter converter = new JwtAccessTokenConverter();
		converter.setSigningKey(signingKey);		
		return converter;
	}
	
	@Bean
	public TokenStore tokenStore() {
		//Para trabajar en memoria.
		return new JwtTokenStore(accessTokenConverter());
		//Para trabajar con base de datos.
		//return new JdbcTokenStore(this.dataSource);
	}
	
	@Bean
	@Primary
	public DefaultTokenServices tokenServices() {
		DefaultTokenServices defaultTokenServices = new DefaultTokenServices();
		defaultTokenServices.setTokenStore(tokenStore());
		defaultTokenServices.setSupportRefreshToken(true);			
		defaultTokenServices.setReuseRefreshToken(false);	
		return defaultTokenServices;
	}
	
}

