package com.altioracorp.wst.servicio.ventas;

import java.util.List;
import java.util.Map;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.altioracorp.wst.dominio.logistica.dto.GuiaDespachoConsulta;
import com.altioracorp.wst.dominio.logistica.dto.GuiaDespachoDTO;
import com.altioracorp.wst.dominio.ventas.DocumentoBase;
import com.altioracorp.wst.dominio.ventas.DocumentoFactura;
import com.altioracorp.wst.dominio.ventas.DocumentoGuiaDespacho;
import com.altioracorp.wst.dominio.ventas.DocumentoGuiaRemision;

public interface IGuiaDespachoServicio {


	DocumentoGuiaDespacho guardarDespachoDeFactura(DocumentoFactura factura);
	
	byte[] generarReportePorFacturaID(Long facturaID);
	
	byte[] generarReporte(Long despachoID);
	
	List<Long> listarGuiasPorCotizacionID(Long CotizacionID);

	List<GuiaDespachoDTO> obtenerGuiasDespachoPorUsuarioIdyPuntoVentaId(Map<String, Object> consulta);
	
	List<DocumentoGuiaRemision> obtenerGuiasRemisionPorDespachoId(long despachoId);
	
	Map<String, DocumentoBase> obtenerGuiaDespachoPorId(long guiaDespachoId);
	
	Map<String, Object> procesarDespacho(long guiaDespachoId, DocumentoGuiaRemision guiaRemision); 
	
	byte[] generarReporteDespachoCliente(Long despachoID, DocumentoGuiaRemision guiaRemision);
	
	Page<GuiaDespachoDTO> consultarGuiasDespacho(Pageable pageabe, GuiaDespachoConsulta consulta);
	
	List<GuiaDespachoDTO> listarTodasPorCotizacionID(long cotizacionID);

	DocumentoGuiaDespacho generarGuiaDespacho(long facturaId);	
	
	List<DocumentoFactura> facturasSinGuiasDespacho();
	
	byte[] generarReporteGuiaRemision(long guiaRemisionId);

}
