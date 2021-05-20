package com.altioracorp.wst.servicio.sistema;

import java.util.List;
import java.util.Optional;

import com.altioracorp.wst.dominio.ldap.UserDetailsVO;
import com.altioracorp.wst.dominio.sistema.PerfilNombre;
import com.altioracorp.wst.dominio.sistema.PuntoVenta;
import com.altioracorp.wst.dominio.sistema.Usuario;
import com.altioracorp.wst.servicio.ICRUD;

public interface IUsuarioServicio extends ICRUD<Usuario, Long> {

	UserDetailsVO obtenerDetalleUsuarioEnSesion();

	void seleccionarPuntoVenta(PuntoVenta puntoVenta);

	List<Usuario> listarPorPerfilVendedor();
	
	Optional<Usuario> listarPorNombreUsuario(String nombreUsuario);

	List<Usuario> listarPorPerfil(PerfilNombre perfil);

}
