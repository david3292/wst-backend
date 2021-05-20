package com.altioracorp.wst.servicioImpl.notificaciones;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;

import com.altioracorp.wst.dominio.cobros.ChequePosfechadoDTO;
import com.altioracorp.wst.dominio.compras.RecepcionCompra;
import com.altioracorp.wst.dominio.sistema.PerfilNombre;
import com.altioracorp.wst.dominio.sistema.Usuario;
import com.altioracorp.wst.dominio.sistema.UsuarioPerfil;
import com.altioracorp.wst.dominio.ventas.DocumentoEstado;
import com.altioracorp.wst.dominio.ventas.DocumentoFactura;
import com.altioracorp.wst.dominio.ventas.DocumentoNotaCredito;
import com.altioracorp.wst.dominio.ventas.DocumentoReserva;
import com.altioracorp.wst.dominio.ventas.DocumentoTransferencia;
import com.altioracorp.wst.dominio.ventas.DocumentoTransferenciaSalida;
import com.altioracorp.wst.repositorio.sistema.IUsuarioPerfilRepositorio;
import com.altioracorp.wst.repositorio.sistema.IUsuarioRepositorio;
import com.altioracorp.wst.servicio.email.IEmailServicio;
import com.altioracorp.wst.servicio.email.Mail;
import com.altioracorp.wst.servicio.notificaciones.INotificacionServicio;
import com.altioracorp.wst.servicio.sistema.IUsuarioServicio;
import com.altioracorp.wst.servicio.ventas.INotaCreditoServicio;

@Service
public class NotificacionServicioImpl implements INotificacionServicio{

	private static final Log LOG = LogFactory.getLog(NotificacionServicioImpl.class);
	
	@Autowired
	private IEmailServicio emailServicio;
	
	@Autowired
	private IUsuarioRepositorio usuarioRepositorio;
	
	@Autowired
	private IUsuarioServicio usuarioServicio;
	
	@Autowired
	private Environment env;
	
	@Autowired
	private IUsuarioPerfilRepositorio usuarioPerfilRepositorio;
	
	@Autowired
	private INotaCreditoServicio notaCreditoServicio;
	
	@Override
	public void probarNotificacion() {
		
		Map<String, Object> parameters = new HashMap<>();
		parameters.put("nombreUsuario", "PERICO DE LOS PALOTES");
		parameters.put("numeroTransferencia", "TRF-00000023");
		parameters.put("numeroReserva", "RRF-00000011");
		parameters.put("numeroCotizacion", "CCL-00000011");
		
		Mail mail = crearEmailAEnviar("diegoalpala91@gmail.com", new ArrayList<String>(),"Correo Prueba", "emailTransferenciaAnulada",parameters);
		
		emailServicio.enviarEmailSimple(mail);
		
	}

	@Override
	public void notificarTransferenciaAnulada(DocumentoTransferencia transferencia) {

		if (transferencia != null) {

			LOG.info(String.format("Notificaciones: Iniciando proceso para Transferencia %s ",
					transferencia.getNumero()));
			final Usuario usuario = recuperarUsuario(transferencia.getCotizacion().getCreadoPor());

			if (usuario != null) {

				Map<String, Object> parameters = new HashMap<>();
				parameters.put("nombreUsuario", usuario.getNombreCompleto());
				parameters.put("numeroTransferencia", transferencia.getNumero());
				parameters.put("numeroReserva", transferencia.getReferencia());
				parameters.put("numeroCotizacion", transferencia.getCotizacion().getNumero());

				Mail mail = crearEmailAEnviar(usuario.getCorreo(), new ArrayList<String>(),
						String.format("Transferencia %s Anulada", transferencia.getNumero()),
						"emailTransferenciaAnulada", parameters);

				emailServicio.enviarEmailSimple(mail);
			} else {

				LOG.error(String.format(
						"Notificaciones : Transferencia %s => Usuario no encontrado para plantilla emailTransferenciaAnulada",
						transferencia.getNumero()));
			}

		}

	}

	private Usuario recuperarUsuario(String usuario) {
		Optional<Usuario> usuarioRecargado = usuarioRepositorio.findByNombreUsuario(usuario);
		if(usuarioRecargado.isPresent()) {
			return usuarioRecargado.get();
		}
		
		return null;
	}

	@Override
	public void notificarFacturaGenerada(DocumentoFactura factura) {
		if (factura != null) {

			LOG.info(String.format("Notificaciones: Iniciando proceso para Factura %s ", factura.getNumero()));
			final Usuario usuario = recuperarUsuario(factura.getCotizacion().getCreadoPor());

			if (usuario != null) {

				Map<String, Object> parameters = new HashMap<>();
				parameters.put("nombreUsuario", usuario.getNombreCompleto());
				parameters.put("numeroFactura", factura.getNumero());
				parameters.put("codigoCliente", factura.getCotizacion().getCodigoCliente());
				parameters.put("nombreCliente", factura.getCotizacion().getNombreCliente());
				parameters.put("numeroCotizacion", factura.getCotizacion().getNumero());

				Mail mail = crearEmailAEnviar(usuario.getCorreo(), new ArrayList<String>(),
						String.format("Factura %s Generada", factura.getNumero()), "emailFacturaGenerada", parameters);

				emailServicio.enviarEmailSimple(mail);
			} else {

				LOG.error(String.format(
						"Notificaciones : Factura %s => Usuario no encontrado para plantilla emailFacturaGenerada",
						factura.getNumero()));
			}
		}

	}

	@Override
	public void notificarFacturaGeneradaError(DocumentoFactura factura) {
		if (factura != null) {

			LOG.info(String.format("Notificaciones: Iniciando proceso para Factura %s ", factura.getNumero()));
			final Usuario usuario = recuperarUsuario(factura.getCotizacion().getCreadoPor());

			if (usuario != null) {

				Map<String, Object> parameters = new HashMap<>();
				parameters.put("nombreUsuario", usuario.getNombreCompleto());
				parameters.put("numeroFactura", factura.getNumero());
				parameters.put("codigoCliente", factura.getCotizacion().getCodigoCliente());
				parameters.put("nombreCliente", factura.getCotizacion().getNombreCliente());
				parameters.put("numeroCotizacion", factura.getCotizacion().getNumero());

				Mail mail = crearEmailAEnviar(usuario.getCorreo(), new ArrayList<String>(),
						String.format("Factura %s Generada Error", factura.getNumero()), "emailFacturaGeneradaError",
						parameters);

				emailServicio.enviarEmailSimple(mail);
			} else {

				LOG.error(String.format(
						"Notificaciones : Factura %s => Usuario no encontrado para plantilla emailFacturaGeneradaError",
						factura.getNumero()));
			}
		}

	}

	@Override
	public void notificarTransferenciaIncompleta(DocumentoTransferenciaSalida transferenciaSalida,
			DocumentoReserva reserva) {

		if (transferenciaSalida != null) {

			LOG.info(String.format("Notificaciones: Iniciando proceso para Transferencia %s ",
					transferenciaSalida.getNumero()));
			final Usuario usuario = recuperarUsuario(transferenciaSalida.getCotizacion().getCreadoPor());

			if (usuario != null) {

				Map<String, Object> parameters = new HashMap<>();
				parameters.put("nombreUsuario", usuario.getNombreCompleto());
				parameters.put("numeroTransferencia", transferenciaSalida.getReferencia());
				parameters.put("numeroReserva", reserva.getNumero());
				parameters.put("numeroCotizacion", transferenciaSalida.getCotizacion().getNumero());

				Mail mail = crearEmailAEnviar(usuario.getCorreo(), new ArrayList<String>(),
						String.format("Transferencia %s Incompleta", transferenciaSalida.getReferencia()),
						"emailTransferenciaIncompleta", parameters);

				emailServicio.enviarEmailSimple(mail);
			} else {

				LOG.error(String.format(
						"Notificaciones : Transferencia %s => Usuario no encontrado para plantilla emailTransferenciaAnulada",
						transferenciaSalida.getReferencia()));
			}

		}
	}
	
	

	@Override
	public void notificarReservaListaParaFaturar(DocumentoReserva reserva) {
		if(reserva != null) {
			
			final Usuario usuario = recuperarUsuario(reserva.getCotizacion().getCreadoPor());
			
			if (usuario != null) {

				Map<String, Object> parameters = new HashMap<>();
				parameters.put("nombreUsuario", usuario.getNombreCompleto());
				parameters.put("numeroReserva", reserva.getNumero());
				parameters.put("codigoCliente", reserva.getCotizacion().getCodigoCliente());
				parameters.put("nombreCliente", reserva.getCotizacion().getNombreCliente());
				parameters.put("numeroCotizacion", reserva.getCotizacion().getNumero());

				Mail mail = crearEmailAEnviar(usuario.getCorreo(), new ArrayList<String>(),
						String.format("Reserva %s Lista Para Facturar", reserva.getNumero()),
						"emailReservaListaParaFacturar", parameters);

				emailServicio.enviarEmailSimple(mail);
			} else {

				LOG.error(String.format(
						"Notificaciones : Reserva %s => Usuario no encontrado para plantilla emailReservaListaParaFacturar",
						reserva.getNumero()));
			}
		}
		
	}

	@Override
	public void notificarChequesPosfechadosPorAutorizar() {
		List<UsuarioPerfil> usuarios = usuarioPerfilRepositorio.findByPerfil_Nombre(PerfilNombre.JEFE_TESORERIA);
		
		if(!usuarios.isEmpty()) {
			
			usuarios.forEach(x -> {
				Map<String, Object> parameters = new HashMap<>();
				parameters.put("nombreUsuario", x.getUsuario().getNombreCompleto());
				parameters.put("linkSistema", env.getProperty("sistema.url"));
				
				Mail mail = crearEmailAEnviar(x.getUsuario().getCorreo(), new ArrayList<String>(),
						"Cheques Posfechados Por Aprobar",
						"emailChequePosFechadoPorAprobar", parameters);

				emailServicio.enviarEmailSimple(mail);

			});
			
		}else {
			LOG.error(String.format(
					"Notificaciones : ChesquesPosfechadosPorAprobar Usuario JEFE_TESORERIA no encontrado para plantilla emailChequePosFechadoPorAprobar"));
		}
	}
	
	@Override
	public void notificarRecepcionOrdenCompra(RecepcionCompra recepcion) {
		if(recepcion != null) {
			
			final Usuario usuario = recuperarUsuario(recepcion.getOrdenCompra().getCotizacion().getCreadoPor());
			
			if (usuario != null) {

				Map<String, Object> parameters = new HashMap<>();
				parameters.put("nombreUsuario", usuario.getNombreCompleto());
				parameters.put("numeroRecepcion", recepcion.getNumeroRecepcion());
				parameters.put("ordenCompraNumero", recepcion.getOrdenCompra().getNumero());
				parameters.put("numeroCotizacion", recepcion.getOrdenCompra().getCotizacion().getNumero());

				Mail mail = crearEmailAEnviar(usuario.getCorreo(), new ArrayList<String>(),
						String.format("Recepción %s Generada", recepcion.getNumeroRecepcion()),
						"emailRecepcionOrdenCompra", parameters);

				emailServicio.enviarEmailSimple(mail);
			} else {

				LOG.error(String.format(
						"Notificaciones : Recepcion %s => Usuario no encontrado para plantilla emailRecepcionOrdenCompra",
						recepcion.getNumeroRecepcion()));
			}
		}
		
	}

	@Override
	public void notificarFacturaGeneradaRefacturacion(DocumentoFactura factura) {
		if (factura != null) {

			LOG.info(String.format("Notificaciones: Iniciando proceso para Refactura %s ", factura.getNumero()));
			final Usuario usuario = recuperarUsuario(factura.getCotizacion().getCreadoPor());
			final DocumentoNotaCredito notaCredito = notaCreditoServicio
					.obtenerPorNumeroDocumento(factura.getReferencia());
			if (usuario != null) {

				Map<String, Object> parameters = new HashMap<>();
				parameters.put("nombreUsuario", usuario.getNombreCompleto());
				parameters.put("numeroFactura", factura.getNumero());
				parameters.put("codigoCliente", factura.getCodigoCliente());
				parameters.put("nombreCliente", factura.getNombreCliente());

				if (factura.getCodigoCliente().equalsIgnoreCase(factura.getCotizacion().getCodigoCliente())) {
					parameters.put("codigoClienteNC", "");
					parameters.put("nombreClienteNC", "");
				} else {
					parameters.put("codigoClienteNC", factura.getCotizacion().getCodigoCliente());
					parameters.put("nombreClienteNC", factura.getCotizacion().getNombreCliente());
				}

				parameters.put("numeroNotaCredito", factura.getReferencia());
				parameters.put("numeroCotizacion", factura.getCotizacion().getNumero());
				parameters.put("motivoRefactura",
						notaCredito != null ? notaCredito.getMotivoNotaCredito().getMotivo() : "");

				List<String> correosCopia = recuperarCorreoJefeCobranzas();
				Mail mail = crearEmailAEnviar(usuario.getCorreo(), correosCopia,
						String.format("Factura %s Generada", factura.getNumero()),
						factura.getEstado().equals(DocumentoEstado.EMITIDO) ? "emailFacturaGeneradaRefactura"
								: "emailFacturaGeneradaErrorRefactura",
						parameters);

				emailServicio.enviarEmailSimple(mail);
			} else {

				LOG.error(String.format(
						"Notificaciones : Refactura %s => Usuario no encontrado para plantilla emailFacturaGeneradaRefactura",
						factura.getNumero()));
			}
		}
	}

	private Mail crearEmailAEnviar(String direccionDestino,List<String> direccionesCopia, String asunto, String plantilla,
			Map<String, Object> parameters) {

		LOG.info(String.format("Creado Objeto Email-> Asunto: %s, DireccionesDestino: %s, Plantilla: %s ", asunto,
				direccionDestino, plantilla));

		return new Mail(env.getProperty("spring.mail.username"), direccionDestino, direccionesCopia, asunto, plantilla, parameters);

	}
	
	private List<String> recuperarCorreoJefeCobranzas(){
		List<Usuario> usuarios = usuarioServicio.listarPorPerfil(PerfilNombre.JEFE_COBRANZAS);
		return usuarios.stream().map(x->x.getCorreo()).collect(Collectors.toList());
	}

	@Override
	public void notificarChequesPosfechadosRechazados(String observacion, String usuarioAnalista, List<ChequePosfechadoDTO> cheques) {
		LOG.info(String.format("Notificaciones: Iniciando proceso para Cheques posfechados rechazados %s ", cheques.size()));
		final Usuario usuario = recuperarUsuario(usuarioAnalista);
		
		if((usuario != null) && (!cheques.isEmpty())) {
			
				Map<String, Object> parameters = new HashMap<>();
				parameters.put("nombreUsuario", usuario.getNombreCompleto());
				parameters.put("observacion", observacion);
				parameters.put("chequesPosfechados", cheques);
				parameters.put("linkSistema", env.getProperty("sistema.url"));
				
				Mail mail = crearEmailAEnviar(usuario.getCorreo(), new ArrayList<String>(),
						"Cheques Posfechados Rechazados",
						"emailChequePosFechadoRechazado", parameters);

				emailServicio.enviarEmailSimple(mail);

		}else {
			LOG.error(String.format(
					"Notificaciones : ChesquesPosfechadosRechazado Usuario ANALISTA_TESORERIA no encontrado para plantilla emailChequePosFechadoRechazado"));
		}		
	}

}
