package com.altioracorp.wst.servicioImpl.ventas;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import com.altioracorp.gpintegrator.client.Sales.*;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.altioracorp.wst.dominio.sistema.Bodega;
import com.altioracorp.wst.dominio.sistema.PuntoVenta;
import com.altioracorp.wst.dominio.sistema.Secuencial;
import com.altioracorp.wst.dominio.sistema.TipoDocumento;
import com.altioracorp.wst.dominio.sistema.Usuario;
import com.altioracorp.wst.dominio.ventas.DocumentoDetalle;
import com.altioracorp.wst.dominio.ventas.DocumentoDetalleCompartimiento;
import com.altioracorp.wst.dominio.ventas.DocumentoEstado;
import com.altioracorp.wst.dominio.ventas.DocumentoFactura;
import com.altioracorp.wst.dominio.ventas.DocumentoGuiaDespacho;
import com.altioracorp.wst.dominio.ventas.DocumentoNotaCredito;
import com.altioracorp.wst.dominio.ventas.Entrega;
import com.altioracorp.wst.dominio.ventas.FlujoAprobacion;
import com.altioracorp.wst.dominio.ventas.TipoDevolucion;
import com.altioracorp.wst.dominio.ventas.dto.NotaCreditoConsultaDTO;
import com.altioracorp.wst.dominio.ventas.dto.NotaCreditoDTO;
import com.altioracorp.wst.dominio.ventas.dto.NotaCreditoSolicitudRespuestaDTO;
import com.altioracorp.wst.exception.sistema.BodegaPrincipalNoExisteException;
import com.altioracorp.wst.exception.sistema.SecuancialNoExisteException;
import com.altioracorp.wst.exception.ventas.FacturaNoEncontradaException;
import com.altioracorp.wst.exception.ventas.NotaCreditoDetalleVacioException;
import com.altioracorp.wst.exception.ventas.NotaCreditoFacturaAnuladaException;
import com.altioracorp.wst.exception.ventas.NotaCreditoNoEncontradaException;
import com.altioracorp.wst.repositorio.sistema.ISecuencialRepositorio;
import com.altioracorp.wst.repositorio.sistema.IUsuarioRepositorio;
import com.altioracorp.wst.repositorio.ventas.IDocumentoFacturaRepositorio;
import com.altioracorp.wst.repositorio.ventas.IDocumentoGuiaDespachoRepositorio;
import com.altioracorp.wst.repositorio.ventas.IDocumentoNotaCreditoRepositorio;
import com.altioracorp.wst.servicio.sistema.IPuntoVentaBodegaServicio;
import com.altioracorp.wst.servicio.ventas.IDocumentoServicio;
import com.altioracorp.wst.servicio.ventas.IGuiaRemisionServicio;
import com.altioracorp.wst.servicio.ventas.INotaCreditoServicio;
import com.altioracorp.wst.util.UtilidadesCadena;
import com.altioracorp.wst.util.UtilidadesFecha;
import com.altioracorp.wst.util.UtilidadesSeguridad;

@Service
public class NotaCreditoServicioImpl implements INotaCreditoServicio {

    private static final Log LOG = LogFactory.getLog(NotaCreditoServicioImpl.class);

    @Value("${compartimiento.general.devolucion}")
    private String compartimientoGeneral;

    @Autowired
    private IDocumentoNotaCreditoRepositorio notaCreditoRepositorio;

    @Autowired
    private IDocumentoFacturaRepositorio facturaRepositorio;

    @Autowired
    private IUsuarioRepositorio usuarioRepositorio;

    @Autowired
    private IDocumentoServicio documentoServicio;

    @Autowired
    private Environment environment;

    @Autowired
    private ISecuencialRepositorio secuencialRepositorio;

    @Autowired
    private IPuntoVentaBodegaServicio puntoVentaBodegaServicio;

    @Autowired
    private IGuiaRemisionServicio guiaRemisionServicio;

    @Autowired
    private IDocumentoGuiaDespachoRepositorio guiaDespachoRepositorio;

    @Override
    @Transactional
    synchronized public DocumentoNotaCredito registrar(DocumentoNotaCredito notaCredito) {

        Optional<DocumentoFactura> optional = this.facturaRepositorio.findById(notaCredito.getDocumentoFacturaId());
        if (!optional.isPresent()) {
            throw new FacturaNoEncontradaException(notaCredito.getReferencia());
        }

        DocumentoFactura factura = optional.get();
        
        if(factura.getEstado().equals(DocumentoEstado.ANULADO)) {
        	throw new NotaCreditoFacturaAnuladaException(factura.getNumero());
        }

        if (notaCredito.obtenerCantidadTotalDetalles().compareTo(factura.obtenerCantidadTotalDetalles()) == 0) {
            factura.setEstado(DocumentoEstado.ANULADO);
        }

        this.validarDetalleNotaCredito(notaCredito);
        notaCredito.setFechaEmision(LocalDateTime.now());
        notaCredito.setNotaCreditoIntegrada(Boolean.FALSE);
        notaCredito.setTipo(TipoDocumento.NOTA_CREDITO);
        notaCredito.setEstado(DocumentoEstado.NUEVO);
        notaCredito.setCotizacion(optional.get().getCotizacion());
        notaCredito.setMotivo(notaCredito.getMotivoNotaCredito().getMotivo());
        notaCredito.setTipoDevolucion(notaCredito.getMotivoNotaCredito().getTipoDevolucion());

        Secuencial secuencial = this.secuencialRepositorio.findByPuntoVentaAndTipoDocumento(factura.getCotizacion().getPuntoVenta(),
                notaCredito.getTipo());
        
        if (secuencial == null) {
            throw new SecuancialNoExisteException(notaCredito.getCotizacion().getPuntoVenta().getNombre(), TipoDocumento.NOTA_CREDITO.name());
        }
        notaCredito.setDocIdGP(secuencial.getDocIdGP());      

        notaCredito.getDetalle().forEach(detalle -> {
            detalle.setCompartimientos(this.generarCompartimientos(detalle));
            DocumentoDetalle detalleFactura = factura.getDetalle().stream().filter(d -> detalle.getCotizacionDetalle().getId() == d.getCotizacionDetalle().getId())
                    .findAny().orElse(null);
            detalleFactura.setSaldo(detalleFactura.getSaldo().subtract(detalle.getCantidad()));
        });

        LOG.info(String.format("Nota credito a guardar : %s", notaCredito));

        this.notaCreditoRepositorio.save(notaCredito);
        
        procesarNotaCredito(notaCredito, factura);
        

        return notaCredito;
    }
    
	private void procesarNotaCredito(DocumentoNotaCredito notaCredito, DocumentoFactura factura) {
		switch (notaCredito.getTipoDevolucion()) {
		case ARTICULO:

			if (this.requiereAprobacion(notaCredito.getDetalle(), factura.getId(), notaCredito.getTipoDevolucion())) {

				notaCredito.setEstado(DocumentoEstado.REVISION);
				notaCredito.setFlujoAprobacion(FlujoAprobacion.COMERCIAL);
				LOG.info(String.format("Nota Credito id:%s tipoDevolucion: %s se envia a flujo COMERCIAL",
						notaCredito.getId(), notaCredito.getTipoDevolucion()));

			} else {
				LOG.info(String.format("Nota Credito id:%s tipoDevolucion: %s se integra directamente",
						notaCredito.getId(), notaCredito.getTipoDevolucion()));
				this.integrarNotaCreditoGP(notaCredito, notaCredito.getDetalle(), factura);
				this.ajustarSaldoGuiaDespacho(notaCredito, factura.getId());
			}

			break;

		case REFACTURACION:
			notaCredito.setEstado(DocumentoEstado.REVISION);
			notaCredito.setFlujoAprobacion(FlujoAprobacion.COMERCIAL);
			LOG.info(String.format("Nota Credito id:%s tipoDevolucion: %s se envia a flujo COMERCIAL",
					notaCredito.getId(), notaCredito.getTipoDevolucion()));

			break;
		default:
			break;
		}
	}

    private List<DocumentoDetalleCompartimiento> generarCompartimientos(DocumentoDetalle documentoDetalle) {
        return Arrays.asList(new DocumentoDetalleCompartimiento(documentoDetalle.getCantidad(), compartimientoGeneral));
    }

    private void validarDetalleNotaCredito(DocumentoNotaCredito notaCredito) {
        if (notaCredito.getDetalle().isEmpty()) {
            throw new NotaCreditoDetalleVacioException();
        }
    }

    @Override
    public Boolean validarDevolucionesEnRevisionPorFacturaId(long facturaId) {
        long count = this.notaCreditoRepositorio.countByEstadoAndDocumentoFacturaId(DocumentoEstado.REVISION, facturaId);
        return count != 0;
    }

    @Override
    public List<NotaCreditoDTO> listarDevolucionesEnRevision(Map<String, Object> consulta) {

        List<NotaCreditoDTO> devoluciones = new ArrayList<>();

        FlujoAprobacion flujoAprobacion = FlujoAprobacion.valueOf(consulta.get("tipoConsulta").toString());

        long puntoVentaId = Long.valueOf((int) consulta.get("puntoVentaId"));
        PuntoVenta puntoVenta = new PuntoVenta();
        puntoVenta.setId(puntoVentaId);
        Bodega bodega = puntoVentaBodegaServicio.buscarBodegaPrincipalPorPuntoVenta(puntoVenta);
        if(bodega == null) {
            throw new BodegaPrincipalNoExisteException();
        }

        if (flujoAprobacion.equals(FlujoAprobacion.BODEGA)) {

            devoluciones = notaCreditoRepositorio
                    .obtenerNotasCreditoPorAprobarOverviewByFlujoAndEstadoAndBodega(flujoAprobacion.toString(), DocumentoEstado.REVISION.toString(), bodega.getCodigo())
                    .stream().map(x -> {
                        return new NotaCreditoDTO(new BigInteger(x[0].toString()).longValue(),
                                TipoDevolucion.valueOf(x[1].toString()),
                                x[2].toString(),
                                x[3].toString(),
                                x[4].toString(),
                                x[5].toString(),
                                x[7].toString(),
                                x[8].toString(),
                                ((Timestamp) x[6]).toLocalDateTime(),
                                Boolean.valueOf(x[9].toString()),
                                "",
                                ""
                        );

                    }).collect(Collectors.toList());
        }

        if (flujoAprobacion.equals(FlujoAprobacion.COMERCIAL))
            devoluciones = notaCreditoRepositorio
                    .findByEstadoAndFlujoAprobacion(DocumentoEstado.REVISION, flujoAprobacion).stream().map(x -> {

                        DocumentoFactura factura = facturaRepositorio.findById(x.getDocumentoFacturaId()).get();
                        Usuario usuario = usuarioRepositorio.findByNombreUsuario(x.getCreadoPor()).get();

						return new NotaCreditoDTO(x.getId(), x.getMotivoNotaCredito().getTipoDevolucion(),
								x.getMotivoNotaCredito().getMotivo(), x.getCotizacion().getCodigoCliente(),
								x.getCotizacion().getNombreCliente(), factura.getNumero(), usuario.getNombreCompleto(),
								x.getCotizacion().getPuntoVenta().getNombre(), factura.getFechaEmision(),
								x.isRevisionTecnica(), x.getCodigoCliente(), x.getNombreCliente());

                    }).collect(Collectors.toList());

        LOG.info(String.format("Consultado Listado Devoluciones Por Aprobar Total=%s , para el flujo %s",
                devoluciones.size(), flujoAprobacion.toString()));

        return devoluciones;
    }

	@Override
	//synchronized palabra reservada para controlar la concurrencia y garantizar que no se procese la misma solicitud (n) veces
	synchronized public DocumentoNotaCredito responderSolicitudDevoluciones(NotaCreditoSolicitudRespuestaDTO respuesta) {
		Optional<DocumentoNotaCredito> devolucion = notaCreditoRepositorio.findById(respuesta.getNotaCreditoId());
		if (devolucion.isPresent()) {

			if (devolucion.get().getNumero() == null) {

				Optional<DocumentoFactura> optional = this.facturaRepositorio
						.findById(devolucion.get().getDocumentoFacturaId());
				if (!optional.isPresent()) {
					throw new FacturaNoEncontradaException(devolucion.get().getReferencia());
				}

				DocumentoFactura factura = optional.get();

				LOG.info(String.format(
						"Respondiendo Solcitud Devolucion de factura %s enviado por %s usuario:%s con Accion %s",
						devolucion.get().getReferencia(), respuesta.getFlujoAprobacion().toString(),
						UtilidadesSeguridad.usuarioEnSesion(), respuesta.getEstado().toString()));

				switch (respuesta.getEstado()) {
				case APROBAR:
					if (respuesta.getFlujoAprobacion().equals(FlujoAprobacion.COMERCIAL)) {
						
						if(devolucion.get().getTipoDevolucion().equals(TipoDevolucion.REFACTURACION)) {
							this.integrarNotaCreditoGP(devolucion.get(), devolucion.get().getDetalle(), factura);
						}else {
							devolucion.get().setFlujoAprobacion(FlujoAprobacion.BODEGA);
						}

					} else if (respuesta.getFlujoAprobacion().equals(FlujoAprobacion.BODEGA)) {
						this.integrarNotaCreditoGP(devolucion.get(), devolucion.get().getDetalle(), factura);
						this.ajustarSaldoGuiaDespacho(devolucion.get(), factura.getId());
						if (devolucion.get().obtenerCantidadTotalDetalles()
								.compareTo(factura.obtenerSaldoTotalDetalles()) == 0) {
							factura.setEstado(DocumentoEstado.ANULADO);
						}
					}
					break;

				case RECHAZAR:
					devolucion.get().setEstado(DocumentoEstado.RECHAZADO);
					devolucion.get().setComentario(respuesta.getComentario());

					devolucion.get().getDetalle().forEach(detalle -> {
						DocumentoDetalle detalleFactura = factura.getDetalle().stream()
								.filter(d -> detalle.getCotizacionDetalle().getCodigoArticulo()
										.equalsIgnoreCase(d.getCotizacionDetalle().getCodigoArticulo()))
								.findAny().orElse(null);
						detalleFactura.setSaldo(detalleFactura.getSaldo().add(detalle.getCantidad()));
					});
					factura.setEstado(DocumentoEstado.EMITIDO);
					break;

				default:
					break;
				}
				notaCreditoRepositorio.save(devolucion.get());
				
				return devolucion.get(); 
			} else {
				LOG.info(String.format("Solicitud ya fue respondida para la NC: %s por el usuario: %s",
						devolucion.get().getNumero(), devolucion.get().getModificadoPor()));
			}
		}
		
		return null;
	}

    private void integrarNotaCreditoGP(DocumentoNotaCredito notaCredito, List<DocumentoDetalle> detalleFactura, DocumentoFactura factura) {
        if (!notaCredito.isNotaCreditoIntegrada()) {

            if (notaCredito.getNumero() == null) {
                Secuencial secuencial = documentoServicio.secuencialDocumento(notaCredito.getCotizacion().getPuntoVenta(),
                        notaCredito.getTipo());
                notaCredito.setNumero(secuencial.getNumeroSecuencial());
                notaCredito.setDocIdGP(secuencial.getDocIdGP());
            }
            SopRequest sopRequest = this.obtenerEntidadIntegracionNotaCredito(notaCredito, detalleFactura, factura);
            SopResponse sopResponse = documentoServicio.integrarDocumento(sopRequest);
            if (sopResponse.getErrorCode() != null && sopResponse.getErrorCode().equals("0")) {
                notaCredito.setEstado(DocumentoEstado.EMITIDO);
                notaCredito.setNotaCreditoIntegrada(Boolean.TRUE);
                notaCredito.setTotal(consultarTotalNotaCredito(notaCredito.getNumero()));

                this.ajustarCompartimientosFactura(factura, notaCredito);

                LOG.info(String.format("NC integrada: %s", notaCredito.getNumero()));
            } else {
                notaCredito.setEstado(DocumentoEstado.ERROR);
                notaCredito.setMensajeError(UtilidadesCadena.truncar(sopResponse.getErrorMessage(), 255));
                notaCredito.setNotaCreditoIntegrada(Boolean.FALSE);
                LOG.error(String.format("Error al integrar NC : %s Error: %s", notaCredito.getNumero(), sopResponse.getErrorMessage()));
            }
        }
    }

    private SopRequest obtenerEntidadIntegracionNotaCredito(DocumentoNotaCredito notaCredito, List<DocumentoDetalle> detalleFactura, DocumentoFactura factura) {

        String docDate = UtilidadesFecha.formatear(LocalDateTime.now(), "yyyy-MM-dd");

        DocumentHeader documentHeader = new DocumentHeader();
        documentHeader.setSoptype(notaCredito.getTipo().getCodigo());
        documentHeader.setDocid(notaCredito.getDocIdGP());
        documentHeader.setSopnumbe(notaCredito.getNumero());
        documentHeader.setLocncode(notaCredito.getBodegaPrincipal());
        documentHeader.setDocdate(docDate);
        documentHeader.setCustnmbr(notaCredito.getCotizacion().getCodigoCliente());
        documentHeader.setBachnumb("WEBNC".concat(Long.toString(notaCredito.getId())));
        documentHeader.setPrbtadcd(notaCredito.getCotizacion().getCodigoDireccion());
        if (factura.getEntrega().equals(Entrega.CLIENTE))
            documentHeader.setPrstadcd(factura.getCotizacion().getCodigoDireccion());
        else {
            documentHeader.setPrstadcd(factura.getDireccionEntrega());
        }
        documentHeader.setOrignumb(factura.getNumero());
        documentHeader.setCreatedist(1);
        documentHeader.setCreatetaxes(1);
        documentHeader.setDefpricing(1);
        documentHeader.setLocation(true);

        DocumentUserDefined documentUserDefined = new DocumentUserDefined();
        documentUserDefined.setSoptype(notaCredito.getTipo().getCodigo());
        documentUserDefined.setSopnumbe(notaCredito.getNumero());
        documentUserDefined.setUserdef1(notaCredito.getCotizacion().getOrdenCliente());
        documentUserDefined.setUserdef2(notaCredito.getCotizacion().getTotalKilos().toString());
        documentUserDefined.setUsrdef03(notaCredito.getCotizacion().getCodigoDireccion());

        List<DocumentLine> documentLines = new ArrayList<DocumentLine>();
        List<DocumentBin> documentBins = new ArrayList<DocumentBin>();

        int i = 1;

        for (DocumentoDetalle detalle : detalleFactura) {

            int secuencia = i * 16384;

            DocumentLine documentLine = new DocumentLine();
            documentLine.setSoptype(notaCredito.getTipo().getCodigo());
            documentLine.setSopnumbe(notaCredito.getNumero());
            documentLine.setCustnmbr(notaCredito.getCotizacion().getCodigoCliente());
            documentLine.setDocdate(docDate);
            documentLine.setLocncode(detalle.getCodigoBodega());
            documentLine.setItemnmbr(detalle.getCotizacionDetalle().getCodigoArticulo());
            documentLine.setLnitmseq(secuencia);
            documentLine.setUnitprce(detalle.getCotizacionDetalle().getPrecioUnitario());
            documentLine.setQuantity(detalle.getCantidad());
            documentLine.setQtyonhnd(detalle.getCantidad());
            documentLine.setMrkdnpct((detalle.getCotizacionDetalle().getDescuentoFijo()));
            documentLine.setUofm(detalle.getCotizacionDetalle().getUnidadMedida());
            documentLine.setPrclevel(environment.getRequiredProperty("integrador.listaPrecio"));
            documentLine.setDefpricing(0);
            documentLine.setAutoallocateserial(0);
            documentLine.setAutoallocatelot(0);
            documentLine.setAutoAssignBin(0);
            documentLines.add(documentLine);

            i++;
        }

        SopRequest sopRequest = new SopRequest();
        sopRequest.setDocumentHeader(documentHeader);
        sopRequest.setDocumentLines(documentLines);
        sopRequest.setDocumentBins(documentBins);
        sopRequest.setDocumentUserDefined(documentUserDefined);

        return sopRequest;
    }

    @Override
    public List<NotaCreditoConsultaDTO> listarTodasPorCotizacionID(Long cotizacionID) {
        List<NotaCreditoConsultaDTO> dto = new ArrayList<>();

        List<DocumentoNotaCredito> notasCredito = notaCreditoRepositorio.findByCotizacion_IdOrderByModificadoPorAsc(cotizacionID);

        notasCredito.forEach(x -> {
            dto.add(new NotaCreditoConsultaDTO(x.getId(), x.getNumero(), x.getEstado(), ""));
        });

        return dto;
    }

    @Override
    @Transactional
    public DocumentoNotaCredito actualizarLineaDetalle(long notaCreditoId, DocumentoDetalle detalle) {
        Optional<DocumentoNotaCredito> devolucion = notaCreditoRepositorio.findById(notaCreditoId);

        if (devolucion.isPresent()) {

            devolucion.get().getDetalle().forEach(x -> {
                if (x.getId() == detalle.getId()) {
                    x.setCantidad(detalle.getCantidad());
                    this.actualizarSaldoFactura(devolucion.get().getDocumentoFacturaId(), detalle);
                    LOG.info(String.format("Nota Credito id= %s: Linea Detalle Modificada [cantidad = %s, saldo= %s ]",
                            devolucion.get().getId(), x.getCantidad(), x.getSaldo()));
                }
            });

            return devolucion.get();
        }

        throw new NotaCreditoNoEncontradaException();
    }

    @Override
    public DocumentoNotaCredito buscarPorId(long id) {
        Optional<DocumentoNotaCredito> notaCredito = notaCreditoRepositorio.findById(id);
        return notaCredito.isPresent() ? notaCredito.get() : null;
    }

	@Override
	public Boolean requiereAprobacion(List<DocumentoDetalle> detallesNotaCredito, long facturaId,
			TipoDevolucion tipoDevolucion) {

		switch (tipoDevolucion) {
		case ARTICULO:
			if(this.guiaRemisionServicio.existeGuiasRemisionPorFacturaId(facturaId)) {
				
			}
			DocumentoGuiaDespacho guiaDespacho = this.guiaDespachoRepositorio.findByDocumentoFacturaId(facturaId);
			if (guiaDespacho != null) {
				for (DocumentoDetalle d : detallesNotaCredito) {
					DocumentoDetalle detalleComun = guiaDespacho.getDetalle().stream()
							.filter(x -> x.getCotizacionDetalle().getId() == d.getCotizacionDetalle().getId()).findAny()
							.get();
					if (d.getCantidad().compareTo(detalleComun.getSaldo()) == 1) {
						return true;
					}
				}
			}
			return false;
		case REFACTURACION:
			return true;

		case DESCUENTO:
			return false;

		default:
			return false;
		}
	}

    @Transactional
    public void ajustarSaldoGuiaDespacho(DocumentoNotaCredito notaCredito, long facturaId) {
        DocumentoGuiaDespacho guiaDespacho = this.guiaDespachoRepositorio.findByDocumentoFacturaId(facturaId);
        if (guiaDespacho != null) {
            guiaDespacho.getDetalle().forEach(detalleGuia -> {
                notaCredito.getDetalle().forEach(detalleNota -> {
                    if (detalleGuia.getCotizacionDetalle().getId() == detalleNota.getCotizacionDetalle().getId()) {
                        if (detalleNota.getCantidad().compareTo(detalleGuia.getSaldo()) == -1 || detalleNota.getCantidad().compareTo(detalleGuia.getSaldo()) == 0) {
                            detalleGuia.setSaldo(detalleGuia.getSaldo().subtract(detalleNota.getCantidad()));
                            this.ajustarCompartimientos(detalleGuia, detalleNota);
                        }
                    }
                });
                if (guiaDespacho.getDetalle().stream().allMatch(x -> x.getSaldo().compareTo(BigDecimal.ZERO) == 0)) {
                    guiaDespacho.setEstado(DocumentoEstado.COMPLETADO);
                }
            });
            this.guiaDespachoRepositorio.save(guiaDespacho);
        }
    }

    @Override
    @Transactional
    public NotaCreditoConsultaDTO reintegrarNotaCredito(long id) {

        Optional<DocumentoNotaCredito> documentoNotaCreditoOptional = this.notaCreditoRepositorio.findById(id);

        if (documentoNotaCreditoOptional.isPresent()) {

            DocumentoNotaCredito notaCredito = documentoNotaCreditoOptional.get();

            if (notaCredito.getEstado().equals(DocumentoEstado.ERROR)) {

                Optional<DocumentoFactura> documentoFacturaOptional = this.facturaRepositorio.findById(notaCredito.getDocumentoFacturaId());

                if (documentoFacturaOptional.isPresent()) {

                    DocumentoFactura factura = documentoFacturaOptional.get();

                    integrarNotaCreditoGP(notaCredito, notaCredito.getDetalle(), factura);

                    this.notaCreditoRepositorio.save(notaCredito);

                    return new NotaCreditoConsultaDTO(notaCredito.getId(), notaCredito.getNumero(), notaCredito.getEstado(),
                            notaCredito.getMensajeError());

                } else {

                    return new NotaCreditoConsultaDTO(id, "", DocumentoEstado.ERROR, "No se ha encontrado la factura a la que se aplicó la nota de crédito.");
                }
            }
        }

        return new NotaCreditoConsultaDTO(id, "", DocumentoEstado.ERROR, "No existe nota de crédito para reintegrar.");
    }

    @Override
    public List<DocumentoNotaCredito> listarPorEstadoError() {
        return this.notaCreditoRepositorio.findByEstadoIn(Arrays.asList(DocumentoEstado.ERROR));
    }

    @Transactional
    public void ajustarCompartimientos(DocumentoDetalle detalleAjustar, DocumentoDetalle detalleNota) {
        if (detalleAjustar.getCompartimientos() != null && !detalleAjustar.getCompartimientos().isEmpty()) {

            if (detalleAjustar.getCompartimientos().size() > 1) {

                BigDecimal aux = detalleNota.getCantidad();

                detalleAjustar.getCompartimientos().sort((d1, d2) -> d2.getCantidad().compareTo(d1.getCantidad()));

                for (DocumentoDetalleCompartimiento compartimiento : detalleAjustar.getCompartimientos()) {
                    if (aux.compareTo(compartimiento.getCantidad()) == -1 || aux.compareTo(compartimiento.getCantidad()) == 0) {
                        BigDecimal diferencia = compartimiento.getCantidad().subtract(aux);
                        aux = aux.subtract(compartimiento.getCantidad());
                        compartimiento.setCantidad(diferencia);
                    } else {
                        aux = aux.subtract(compartimiento.getCantidad());
                        compartimiento.setCantidad(BigDecimal.ZERO);
                    }

                    if (aux.compareTo(BigDecimal.ZERO) == 0) {
                        break;
                    }
                }

            } else {
                DocumentoDetalleCompartimiento compartimiento = detalleAjustar.getCompartimientos().get(0);
                compartimiento.setCantidad(compartimiento.getCantidad().subtract(detalleNota.getCantidad()));
            }
        }
    }

    @Transactional
    public void ajustarCompartimientosFactura(DocumentoFactura factura, DocumentoNotaCredito notaCredito) {
        notaCredito.getDetalle().forEach(detNota -> {
            DocumentoDetalle detFactura = factura.getDetalle().stream().filter(x -> x.getCotizacionDetalle().getId()
                    == detNota.getCotizacionDetalle().getId())
                    .findAny().orElse(null);
            if (detFactura != null) {
                this.ajustarCompartimientos(detFactura, detNota);
            }
        });
    }

    private void actualizarSaldoFactura(long facturaId, DocumentoDetalle detalle) {
        Optional<DocumentoFactura> facturaOptional = this.facturaRepositorio.findById(facturaId);

        if (facturaOptional.isPresent()) {
            facturaOptional.get().getDetalle().forEach(x -> {
                if (x.getCotizacionDetalle().getId() == detalle.getCotizacionDetalle().getId()) {
                    BigDecimal diferencia = detalle.getSaldo().subtract(detalle.getCantidad());
                    x.setSaldo(x.getSaldo().add(diferencia));
                }
            });
        }

        this.facturaRepositorio.save(facturaOptional.get());
    }

	@Override
	public DocumentoNotaCredito obtenerPorNumeroDocumento(String numero) {
		Optional<DocumentoNotaCredito> documento = notaCreditoRepositorio.findByNumero(numero);
		return documento.isPresent() ? documento.get() : null;
	}


    private BigDecimal consultarTotalNotaCredito(String numeroDocumento) {
        BigDecimal total = BigDecimal.ZERO;
        DocumentResponse documento = documentoServicio.consultarDocumento(numeroDocumento, TipoDocumento.NOTA_CREDITO.getCodigo());
        if (documento != null) {
            total = documento.getDocumentHeader().getDocamnt();
        }
        return total;
    }

	@Override
	public boolean esGeneradaPorMotivoRefacturacion(String numero) {
		Optional<Object> resultado = notaCreditoRepositorio.esMotivoRefacturacion(numero);
		if(resultado.isPresent()) {
			if(resultado.get().toString().equalsIgnoreCase("si"))
				return true;
			
			return false;
		}
		return false;
	}


}
