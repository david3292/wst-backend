package com.altioracorp.wst.repositorio.ldap;

import java.util.Optional;

import org.springframework.data.ldap.repository.LdapRepository;

import com.altioracorp.wst.dominio.ldap.UsuarioLdap;

public interface IUsuarioLdapRepositorio  extends LdapRepository<UsuarioLdap> {

	Optional<UsuarioLdap> findByUserId(String username);
}
