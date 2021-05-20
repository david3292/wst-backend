package com.altioracorp.wst.servicioImpl.sistema;

import static com.altioracorp.wst.util.UtilidadesCadena.normalizarDireccionCorreoElectronico;
import static com.altioracorp.wst.util.UtilidadesCadena.normalizarNombre;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.altioracorp.wst.UserDetailsUsuarioLDAP;
import com.altioracorp.wst.dominio.ldap.UserDetailsVO;
import com.altioracorp.wst.dominio.sistema.PerfilNombre;
import com.altioracorp.wst.dominio.sistema.PuntoVenta;
import com.altioracorp.wst.dominio.sistema.Usuario;
import com.altioracorp.wst.dominio.sistema.UsuarioPerfil;
import com.altioracorp.wst.exception.sistema.UsuarioYaExistenteException;
import com.altioracorp.wst.repositorio.sistema.IConfiguracionUsuarioPerfilRepositorio;
import com.altioracorp.wst.repositorio.sistema.IUsuarioPerfilRepositorio;
import com.altioracorp.wst.repositorio.sistema.IUsuarioRepositorio;
import com.altioracorp.wst.servicio.sistema.IUsuarioServicio;
import com.altioracorp.wst.util.UtilidadesSeguridad;

@Service
public class UsuarioServicioImpl implements IUsuarioServicio {

	private static final Log LOG = LogFactory.getLog(UsuarioServicioImpl.class);
	
	@Autowired
	private IUsuarioRepositorio usuarioRepositorio;
	
	@Autowired
	private IUsuarioPerfilRepositorio usuarioPerfilRepositorio;
	
	@Autowired
	private IConfiguracionUsuarioPerfilRepositorio configUsuarioPerfilRepositorio;
	
//	@Override
//	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
//		
//		Optional<Usuario> usuario = usuarioRepositorio.findByNombreUsuario(username);	
//		if(!usuario.isPresent()) {
//			throw new UsernameNotFoundException(String.format("Usuario no existe", username));
//		}
//		
//		List<UsuarioPerfil> perfilesLista= usuarioPerfilRepositorio.findByUsuario(usuario.get());
//		
//		List<GrantedAuthority> roles = new ArrayList<>();
//		
//		perfilesLista.forEach(rol -> {
//			roles.add(new SimpleGrantedAuthority(rol.getPerfil().getNombre().getDescripcion()));
//		});
//
//		UserDetails ud = new User(usuario.get().getNombreUsuario(), usuario.get().getContrasena(), roles);
//		return ud;
//
//	}

	@Override
	public Usuario registrar(Usuario obj) {
		
		String nombreCompletoNormalizado = normalizarNombre(obj.getNombreCompleto());
		String correoNormalizado = normalizarDireccionCorreoElectronico(obj.getCorreo());
		asegurarUsuarioUnico(obj.getNombreUsuario());
		
		Usuario usuario = new Usuario(obj.getNombreUsuario(),nombreCompletoNormalizado, correoNormalizado, obj.getCargo());
		LOG.info("Guardando Usuario Nuevo: " + usuario);		
		return usuarioRepositorio.save(usuario);
	}

	@Override
	public Usuario modificar(Usuario obj) {
		
		String correoNormalizado = normalizarDireccionCorreoElectronico(obj.getCorreo());
		String nombreCompletoNormalizado = normalizarNombre(obj.getNombreCompleto());		
		obj.setCorreo(correoNormalizado);
		obj.setNombreCompleto(nombreCompletoNormalizado);
		
		LOG.info("Guardando Usuario : " + obj);	
		return usuarioRepositorio.save(obj);
	}

	@Override
	public List<Usuario> listarTodos() {
		return (List<Usuario>) usuarioRepositorio.findAll();
	}

	@Override
	public Usuario listarPorId(Long id) {
		Optional<Usuario> usuario = usuarioRepositorio.findById(id);
		return usuario.isPresent() ? usuario.get() : null;
	}

	@Override
	public boolean eliminar(Long id) {
		// TODO Auto-generated method stub
		return false;
	}
	
	private void asegurarUsuarioUnico(String nombreUsuario) {

		Optional<Usuario> usuario = usuarioRepositorio.findByNombreUsuario(nombreUsuario);
		if (usuario.isPresent()) {
			throw new UsuarioYaExistenteException(nombreUsuario);
		}
	}
	
	@Override
	public Optional<Usuario> listarPorNombreUsuario(String nombreUsuario){
		return usuarioRepositorio.findByNombreUsuario(nombreUsuario);
	}

	@Override
	public UserDetailsVO obtenerDetalleUsuarioEnSesion() {
		UserDetailsUsuarioLDAP usuario =  UtilidadesSeguridad.usuarioDetalleEnSesion();
		String s=UtilidadesSeguridad.usuarioEnSesion();
		return null;
	}

	@Override
	public void seleccionarPuntoVenta(PuntoVenta puntoVenta) {
		UserDetailsUsuarioLDAP usuario = UtilidadesSeguridad.usuarioDetalleEnSesion();
		usuario.getUserDetailsVO().setPuntoVentaSeleccionado(puntoVenta);
		usuario.getUserDetailsVO().setConfiguracionPuntoVentaSeleccionado(configUsuarioPerfilRepositorio.findByPuntoVentaAndUsuarioPerfil_Usuario(puntoVenta, usuario.getUserDetailsVO().getUsuario()).orElse(null));

	}

	@Override
	public List<Usuario> listarPorPerfilVendedor() {
		final List<UsuarioPerfil> lista = this.usuarioPerfilRepositorio.findByPerfil_Nombre(PerfilNombre.VENDEDOR);
		return lista.stream().map(u -> {
			return u.getUsuario();
		}).collect(Collectors.toList());
	}

	@Override
	public List<Usuario> listarPorPerfil(PerfilNombre perfil) {
		final List<UsuarioPerfil> lista = this.usuarioPerfilRepositorio.findByPerfil_Nombre(perfil);
		return lista.stream().map(u -> {
			return u.getUsuario();
		}).collect(Collectors.toList());
	}

}
