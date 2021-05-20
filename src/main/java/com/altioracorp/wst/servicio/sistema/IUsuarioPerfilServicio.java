package com.altioracorp.wst.servicio.sistema;

import com.altioracorp.gpintegrator.client.Receivable.SalesPerson;
import com.altioracorp.wst.dominio.sistema.PerfilNombre;
import com.altioracorp.wst.dominio.sistema.Usuario;
import com.altioracorp.wst.dominio.sistema.UsuarioPerfil;
import com.altioracorp.wst.servicio.ICRUD;

import java.util.List;
import java.util.Optional;

public interface IUsuarioPerfilServicio extends ICRUD<UsuarioPerfil, Long> {

	List<UsuarioPerfil> listarPorUsuario(Usuario usuario);

	Optional<UsuarioPerfil> listarPorNombreUsuarioYPerfilNombre(String usuario, PerfilNombre perfilNombre);

	List<SalesPerson> listarCodigosVendedoresGP();

	Optional<UsuarioPerfil> buscarPorNombreUsuarioYPerfilId(final String nombreUsuario, final Long idPerfil);
	
}
