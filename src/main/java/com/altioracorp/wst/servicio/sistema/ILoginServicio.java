package com.altioracorp.wst.servicio.sistema;

import java.util.Optional;

import com.altioracorp.wst.dominio.sistema.Usuario;

public interface ILoginServicio {

	Optional<Usuario> verificarNombreUsuario(String usuario);
}
