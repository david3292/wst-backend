package com.altioracorp.wst.servicioImpl.ventas;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.xml.bind.JAXBException;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.altioracorp.gpintegrator.client.Company.CompanyLocation;
import com.altioracorp.gpintegrator.client.Company.InfoGuiaCompania;
import com.altioracorp.gpintegrator.client.GuiaRemision.AltGuiaIv10000;
import com.altioracorp.gpintegrator.client.GuiaRemision.AltGuiaTransaction;
import com.altioracorp.wst.AmbienteElectronica;
import com.altioracorp.wst.dominio.ventas.DocumentoBase;
import com.altioracorp.wst.dominio.ventas.DocumentoGuiaRemision;
import com.altioracorp.wst.dominio.ventas.dto.GuiaRemisionDTO;
import com.altioracorp.wst.repositorio.ventas.IDocumentoGuiaRemisionRepositorio;
import com.altioracorp.wst.servicio.sistema.IConstantesAmbienteWstServicio;
import com.altioracorp.wst.servicio.ventas.IEmpresaServicio;
import com.altioracorp.wst.servicio.ventas.IGuiaRemisionServicio;
import com.altioracorp.wst.util.UtilidadesFecha;
import com.altioracorp.wst.util.UtilidadesXml;
import com.altioracorp.wst.xml.guiaRemision.Destinatario;
import com.altioracorp.wst.xml.guiaRemision.Detalle;
import com.altioracorp.wst.xml.guiaRemision.GuiaRemision;
import com.altioracorp.wst.xml.guiaRemision.InfoTributaria;
import com.altioracorp.wst.xml.guiaRemision.ObligadoContabilidad;

@Service
public
class GuiaRemisionServicioImpl implements IGuiaRemisionServicio {

	private static final Log LOG = LogFactory.getLog(GuiaDespachoServicioImpl.class);
	
	@Autowired
	private IDocumentoGuiaRemisionRepositorio repositorio;
	
	@Autowired
	private IEmpresaServicio empresaServicio;
	
	@Autowired
	private IConstantesAmbienteWstServicio ambienteServicio;
	
	@Autowired
	private ModelMapper modelMapper;

	
	@Override
	public List<DocumentoGuiaRemision> buscarPorDocumentoPadreId(long documentoPadreId){		
		return repositorio.findByDocumentoPadreId(documentoPadreId);		
	}
	
	public DocumentoGuiaRemision save(DocumentoGuiaRemision guiaRemision) {
		return repositorio.save(guiaRemision);
	}
	
	@Override
	public List<GuiaRemisionDTO> listarTodasPorCotizacionID(Long cotizacionID) {
		List<GuiaRemisionDTO> dto = new ArrayList<>();

		List<DocumentoGuiaRemision> guias = repositorio.findByCotizacion_IdOrderByModificadoPorAsc(cotizacionID);

		guias.forEach(x->{
			
			dto.add(new GuiaRemisionDTO(x.getId(), x.getNumero(), x.getEstado(),""));
		});
		
		return dto;
	}

	@Override
	@Transactional(readOnly = true)
	public DocumentoBase obtenerGuiaRemisionPorId(long guiaRemisionId) {
		Optional<DocumentoGuiaRemision> guiaOpt = this.repositorio.findById(guiaRemisionId);
		if (guiaOpt.isEmpty())
			return new DocumentoGuiaRemision();
		else
			return guiaOpt.get();
	}

	@Override
	@Transactional(readOnly = true)
	public Boolean existeGuiasRemisionPorFacturaId(long facturaId) {
		long count = this.repositorio.countGuiaRemisionPorFacturaId(facturaId);
		return count != 0;
	}
	
	@Override
	public long countByDocumentoPadreId(long id) {
		return this.repositorio.countByDocumentoPadreId(id);
	}
	
	@Override
	public Optional<DocumentoGuiaRemision> findById(long id){
		return this.repositorio.findById(id);
	}
	
	@Override
	public void generarXmlGuiaRemision(AltGuiaTransaction guiaTransaction, DocumentoBase documento) {
		StringBuilder sb = new StringBuilder().append("<?xml version=\"1.0\" encoding=\"UTF-8\"?>");
		
		String guiaRemisionXml = StringUtils.EMPTY; 
		
		GuiaRemision guiaRemision = obtenerGuiaRemision(guiaTransaction, documento);
		
		try {
			guiaRemisionXml = UtilidadesXml.generarXml(guiaRemision, GuiaRemision.class);
			//guiaTransaction.setGuiaRemisionXml(guiaRemisionXml);
			sb.append(guiaRemisionXml);
			guiaTransaction.getCabeceraGuiaRemision().setUserDeleted("SI");
			guiaTransaction.getCabeceraGuiaRemision().setXml(sb.toString());
			//guiaTransaction.getCabeceraGuiaRemision().setCustomerVendorName(documento.getCotizacion().getNombreCliente());
		} catch (JAXBException e) {
			LOG.error(String.format("Error al generarl el xml del documento: %s, :: error:: %s", documento.getNumero(), e.getMessage()));
		}
	}
	
	@Override
	public GuiaRemision obtenerGuiaRemision(AltGuiaTransaction guiaTransaction, DocumentoBase documento) {
		
		GuiaRemision guiaRemision = new GuiaRemision();
		
		InfoTributaria infoTributaria = obtenerInformacionTributaria(guiaTransaction.getCabeceraGuiaRemision().getIvDocNbr());
		infoTributaria.setCodDoc(guiaTransaction.getCabeceraGuiaRemision().getIvDocTyp());
		guiaRemision.setInfoTributaria(infoTributaria);
		
		guiaRemision.setInfoGuiaRemision(obtenerInfoGuiaRemision(guiaTransaction, ((DocumentoGuiaRemision)documento)));
		
		guiaRemision.setDestinatarios(obtenerDestinatarios(guiaTransaction, documento));
		
		guiaRemision.setInfoAdicional(obtenerInformacionAdcional(guiaTransaction));
		
		guiaRemision.setVersion("1.1.0");
		guiaRemision.setId("comprobante");
		
		return guiaRemision;
	}
	
	private InfoTributaria obtenerInformacionTributaria(String numeroDocumento){
		String[] numeroDespompuesto = StringUtils.split(numeroDocumento,"-");
		
		com.altioracorp.gpintegrator.electronica.InfoTributaria infoTributariaConsulta = empresaServicio.obtenerInformacionTributaria();		
		InfoTributaria info = modelMapper.map(infoTributariaConsulta, InfoTributaria.class);
		info.setAmbiente(String.valueOf(obtenerAmbienteSri().getAmbienteSri()));
		info.setClaveAcceso("0123456789012345678901234567890123456789012345678");
		info.setEstab(numeroDespompuesto[0]);
		info.setPtoEmi(numeroDespompuesto[1]);
		info.setSecuencial(numeroDespompuesto[2]);
		
		return info;
	}
	
	private AmbienteElectronica obtenerAmbienteSri() {
		String ambienteString = ambienteServicio.getAmbienteSri();		
		return AmbienteElectronica.valueOf(ambienteString);
	}
	
	private GuiaRemision.InfoGuiaRemision obtenerInfoGuiaRemision(AltGuiaTransaction guiaTransaction, DocumentoGuiaRemision documento){
		
		InfoGuiaCompania infoGuiaCompania = empresaServicio.obtenerInformacionGuiaCompania();
		String direccionEstablecimiento = StringUtils.EMPTY;
		String localizacionId = documento.getNumero().substring(0,3);
		Optional<CompanyLocation> companyLocation =	infoGuiaCompania.getCompanyLocationData().stream().filter(c -> c.getLocatnid().equals(localizacionId)).findFirst();
		
		if(companyLocation.isPresent())
			direccionEstablecimiento = companyLocation.get().getAddress1();
		
		
		GuiaRemision.InfoGuiaRemision info = new GuiaRemision.InfoGuiaRemision();
		AltGuiaIv10000 cabeceraGuia = guiaTransaction.getCabeceraGuiaRemision();		
		
		info.setDirEstablecimiento(direccionEstablecimiento);
		info.setDirPartida(cabeceraGuia.getDirPartida());
		info.setRazonSocialTransportista(cabeceraGuia.getConductor());
		info.setTipoIdentificacionTransportista(obtenerTipoIdentificacion(cabeceraGuia.getIdConductor()));
		info.setRucTransportista(cabeceraGuia.getIdConductor());
		info.setObligadoContabilidad(ObligadoContabilidad.SI);
		info.setContribuyenteEspecial(infoGuiaCompania.getContribuyenteEspecial());
		info.setFechaIniTransporte(UtilidadesFecha.formatear(documento.getFechaInicioTraslado(), "dd/MM/yyyy"));
		info.setFechaFinTransporte(UtilidadesFecha.formatear(documento.getFechaFinTraslado(), "dd/MM/yyyy"));
		info.setPlaca(cabeceraGuia.getNumPlaca());
		
		return info;
	}
	
	private String obtenerTipoIdentificacion(String identificacion) {
		switch(identificacion.length()) {
			case 10: return "05";
			case 13: return "04";
			default: return "06";
		}
	}
	
	private GuiaRemision.Destinatarios obtenerDestinatarios(AltGuiaTransaction guiaTransaction, DocumentoBase documento){
		AltGuiaIv10000 cabecera = guiaTransaction.getCabeceraGuiaRemision();
		
		GuiaRemision.Destinatarios destinatarios = new GuiaRemision.Destinatarios();
		
		Destinatario destinatario = new Destinatario();
		destinatario.setIdentificacionDestinatario(cabecera.getCustNmbr());
		destinatario.setRazonSocialDestinatario(cabecera.getCustomerVendorName());
		destinatario.setDirDestinatario(cabecera.getDirLlegada());
		destinatario.setMotivoTraslado(cabecera.getMotTraslado());
		destinatario.setCodEstabDestino(cabecera.getIvDocNbr().substring(0,3));
		destinatario.setRuta(cabecera.getRuta());
		destinatario.setCodDocSustento(cabecera.getTipAfecta());
		
		if(cabecera.getTipAfecta().equals("06"))
			destinatario.setNumDocSustento(cabecera.getIvDocNbr());
		else
			destinatario.setNumDocSustento(cabecera.getFacAfecta());
		
		destinatario.setNumAutDocSustento("0000000000");
		destinatario.setFechaEmisionDocSustento(UtilidadesFecha.formatear(LocalDateTime.now(), "dd/MM/yyyy"));
		
		destinatario.setDetalles(obtenerDetalles(guiaTransaction, documento));
		
		destinatarios.getDestinatario().add(destinatario);
		
		return destinatarios;
	}
	
	private Destinatario.Detalles obtenerDetalles(AltGuiaTransaction guiaTransaction, DocumentoBase documento){
		
		Destinatario.Detalles detalles = new Destinatario.Detalles();
		
		detalles.getDetalle().addAll(guiaTransaction.getDetalleGuiaRemision().stream().map(d -> {
			Detalle detalle = new Detalle();
			detalle.setCodigoInterno(d.getItemNmbr());
			detalle.setCantidad(d.getTrxQty());
			detalle.setDescripcion(d.getDescripcionArticulo());			
			return detalle;
		}).collect(Collectors.toList()));
		
		return detalles;
	}
	
	private GuiaRemision.InfoAdicional obtenerInformacionAdcional(AltGuiaTransaction guiaTransaction){
		
		GuiaRemision.InfoAdicional infoAdicional = new GuiaRemision.InfoAdicional();
		
		GuiaRemision.InfoAdicional.CampoAdicional campo;
		if(StringUtils.isNotBlank(guiaTransaction.getCabeceraGuiaRemision().getComentario())){
			campo = new GuiaRemision.InfoAdicional.CampoAdicional();
			campo.setNombre("INFO01");
			campo.setValue(guiaTransaction.getCabeceraGuiaRemision().getComentario());
			infoAdicional.getCampoAdicional().add(campo);
		}
		
		campo = new GuiaRemision.InfoAdicional.CampoAdicional();
		campo.setNombre("Email");
		campo.setValue(guiaTransaction.getCabeceraGuiaRemision().getEmail());
		infoAdicional.getCampoAdicional().add(campo);
		
		return infoAdicional;
	}
	
	@Override
	public Map<String, Object> obtenerInfoGuiaRemision(long guiaRemisionId) {
		
		Map<String, Object> response = new HashMap<String, Object>();
		response.put("mensaje", "ERROR");
		
		List<Object[]> result = repositorio.obtenerInfoGuiaRemision(guiaRemisionId);
		if(CollectionUtils.isNotEmpty(result)) {
			Object[] guiaRemisionInfo = result.stream().findFirst().get();
			
			response.put("documentoPadreId", guiaRemisionInfo[0]);
			response.put("guiaRemisionId", guiaRemisionInfo[1]);
			response.put("tipoDocumento", guiaRemisionInfo[2]);
			response.put("documentoPadreEstado", guiaRemisionInfo[3]);
			response.put("mensaje", "OK");
		}
		
		return response;
	}
}
