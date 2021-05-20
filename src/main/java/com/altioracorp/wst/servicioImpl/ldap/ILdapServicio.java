package com.altioracorp.wst.servicioImpl.ldap;

import java.util.List;
import java.util.Optional;

import com.altioracorp.wst.dominio.ldap.UsuarioLdap;

public interface ILdapServicio {

	/**
	 * Retorna TRUE en caso de que el ususario se pueda logear mediante Active
	 * Directory. FALSE caso contrario.
	 * 
	 * @param userName - UID del usuario. Ejemplo: UID="cnaranjo".
	 * @param password - Credencial de acceso del Active Directory.
	 * @return
	 */
	Boolean authenticate(String userName, String password);

	/**
	 * Obtiene un listado de usuarios del Active Directory.
	 * 
	 * @return List<UsuarioLdap>.
	 */
	List<UsuarioLdap> findAll();

	/**
	 * Obtiene un objeto de tipo Usuario con informacion obtenida del Active
	 * Directory.
	 * 
	 * @param username - Nombre del usuario a buscar. Ejemplo: CN="Diego Alpala".
	 * @return Objeto de tipo Usuario.
	 */
	Optional<UsuarioLdap> findByUserName(String username);
}
