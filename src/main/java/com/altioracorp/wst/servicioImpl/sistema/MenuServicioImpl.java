package com.altioracorp.wst.servicioImpl.sistema;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.altioracorp.wst.dominio.sistema.Menu;
import com.altioracorp.wst.dominio.sistema.UsuarioMenuDTO;
import com.altioracorp.wst.dominio.sistema.UsuarioPerfil;
import com.altioracorp.wst.repositorio.sistema.IUsuarioPerfilRepositorio;
import com.altioracorp.wst.servicio.sistema.IMenuServicio;

@Service
public class MenuServicioImpl implements IMenuServicio {

	@Autowired
	private IUsuarioPerfilRepositorio usuarioPerfilRepositorio;
	
	@Override
	public List<Menu> listarMenuPorUsuario(String usuario) {
		List<Menu> menus = new ArrayList<>();
		
		Optional<UsuarioPerfil> usuarioPerfil=usuarioPerfilRepositorio.findByUsuario_NombreUsuario(usuario).stream().findFirst();
		
		if(usuarioPerfil.isPresent()) {
			menus= usuarioPerfil.get().getPerfil().getListaMenu();
		}
		
		return menus;
	}

	@Override
	public UsuarioMenuDTO listarUsuarioMenuPorUsuario(UsuarioPerfil usuarioPerfil) {
		
		List<UsuarioPerfil> perfiles =usuarioPerfilRepositorio.findByUsuario_NombreUsuario(usuarioPerfil.getUsuario().getNombreUsuario());
		
		Optional<UsuarioPerfil> usuarioPerfilRecargado = perfiles.stream().filter(x -> x.getPerfil().getNombre().equals(usuarioPerfil.getPerfil().getNombre())).findFirst();
		if(usuarioPerfilRecargado.isPresent()) {
			return new UsuarioMenuDTO(usuarioPerfilRecargado.get().getUsuario().getNombreCompleto(), usuarioPerfilRecargado.get().getUsuario().getNombreUsuario(), verificarMenuRepetido(usuarioPerfilRecargado.get().getPerfil().getListaMenu()));
		}else {		
			return new UsuarioMenuDTO();
		}
	}
	
	private List<Menu> verificarMenuRepetido(List<Menu> listaMenu){
		// TODO: Pensado para 2 niveles.
		List<Menu> subMenuLista = new ArrayList<>();
		
		listaMenu.forEach(x -> {
			subMenuLista.addAll(x.getItems());
		});
		
		listaMenu.removeAll(subMenuLista);
		
		return listaMenu.stream().distinct().collect(Collectors.toList());
	}

}
