package com.altioracorp.wst.servicioImpl.sistema;

import static org.springframework.ldap.query.LdapQueryBuilder.query;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ldap.core.LdapTemplate;
import org.springframework.stereotype.Service;

import com.altioracorp.wst.dominio.ldap.UsuarioLdap;
import com.altioracorp.wst.repositorio.ldap.IUsuarioLdapRepositorio;
import com.altioracorp.wst.servicio.sistema.ConfiguracionWST;
import com.altioracorp.wst.servicioImpl.ldap.ILdapServicio;

@Service
public class SerivicioLdapImpl implements ILdapServicio {
	
	@Autowired
	private LdapTemplate ldapTemplate;
	
	@Autowired
	private IUsuarioLdapRepositorio usuarioLdapRepositorio;
	
	@Autowired
	private ConfiguracionWST configuracionWST;
	
	@Override
	public Boolean authenticate(String userName, String password) {
		try {
			if(configuracionWST.getAmbiente().equalsIgnoreCase("DESARROLLO")){
				return Boolean.TRUE;
			}else {
				ldapTemplate.authenticate(query().where("SamAccountName").is(userName), password);
				return Boolean.TRUE;
			}
			
		} catch (Exception e) {
			return Boolean.FALSE;
		}
	}

	@Override
	public List<UsuarioLdap> findAll() {
		return (List<UsuarioLdap>) this.usuarioLdapRepositorio.findAll();
	}

	@Override
	public Optional<UsuarioLdap> findByUserName(String username) {
		return usuarioLdapRepositorio.findByUserId(username);
	}

}
