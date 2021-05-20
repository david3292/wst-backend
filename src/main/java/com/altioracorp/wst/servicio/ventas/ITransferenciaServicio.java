package com.altioracorp.wst.servicio.ventas;

import java.util.List;
import java.util.Map;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.altioracorp.wst.dominio.logistica.dto.ArticuloReposicionDTO;
import com.altioracorp.wst.dominio.logistica.dto.ReposicionArticuloBodegaDTO;
import com.altioracorp.wst.dominio.logistica.dto.ReposicionDTO;
import com.altioracorp.wst.dominio.logistica.dto.ReposicionDetalleDTO;
import com.altioracorp.wst.dominio.logistica.dto.ReposicionRespuestaCambioLineaDTO;
import com.altioracorp.wst.dominio.logistica.dto.ReposicionSugerenciaDTO;
import com.altioracorp.wst.dominio.logistica.dto.TransferenciaConsulta;
import com.altioracorp.wst.dominio.logistica.dto.TransferenciaDTO;
import com.altioracorp.wst.dominio.ventas.DocumentoBase;
import com.altioracorp.wst.dominio.ventas.DocumentoDetalle;
import com.altioracorp.wst.dominio.ventas.DocumentoGuiaRemision;
import com.altioracorp.wst.dominio.ventas.DocumentoTransferencia;
import com.altioracorp.wst.dominio.ventas.DocumentoTransferenciaEntrada;
import com.altioracorp.wst.dominio.ventas.dto.TransferenciaConsultaDTO;

public interface ITransferenciaServicio {
	
	DocumentoTransferencia guardar(DocumentoTransferencia transferencia);

	DocumentoBase buscarDocumentoTransferenciaPorId(long id);
	
	DocumentoBase buscarDocumentoTransferenciaSalidaPorId(long id);
	
	DocumentoBase buscarDocumentoTransferenciaEntradaPorId(long id);
	
	DocumentoDetalle obtenerDocumentoDetallePorIdConCompartimientos(long documentoDetalleId);
	
	void actualizarDetalleTransferenciaSalida(long transferenciaSalidaId, DocumentoDetalle documentoDetalle);
	
	void actualizarDetalleTransferenciaEntrada(long transferenciaEntradaId, DocumentoDetalle documentoDetalle);
	
	List<DocumentoBase> obtenerDocumentosTransferenciaSalidaId(long transferenciaId);
	
	DocumentoBase crearNuevaTransferenciaSalida(long transferenciaId);
	
	DocumentoGuiaRemision crearActualizarDocumentoGuiaRemision(long transferenciaSalidaId, DocumentoGuiaRemision guiaRemision);
	
	DocumentoBase obtenerDocumentoGuiaRemisionPorTransferenciaSalidaId(long transferenciaSalidaId);
	
	List<DocumentoTransferenciaEntrada> obtenerDocumentosTransferenciaEntradaId(long transferenciaSalidaId);
	
	byte[] generarReporteTransferenciaSalida(long transferenciaSalidaId);
	
	List<TransferenciaConsultaDTO> listarTodasPorCotizacionID(long cotizacionID);
	
	List<TransferenciaDTO> listarTransferenciasPorUsuarioIdYPuntoVentaId(Map<String, Object> consulta);
	
	byte[] generarReporteTransferenciaEntrada(long transferenciaEntradaId);
	
	byte[] generarReporteTransferencia(long transferenciaId);
	
	Page<TransferenciaDTO> consultarTransferencia(Pageable pageable, TransferenciaConsulta consulta);
	
	//TODO: Reposiciones
	
	ReposicionDTO sugerirReposicion(ReposicionSugerenciaDTO dto);
	
	void anularTransferenciaReposicion(long transferenciaId);
	
	ArticuloReposicionDTO obtenerReposicionArticulo(ReposicionArticuloBodegaDTO articuloBodegaDTO);
	
	void emitirTransferenciaReposicion(long transferenciaId);
	
	ReposicionRespuestaCambioLineaDTO actualizarCantidadReposicionDetalle(ReposicionDetalleDTO detalleDTO);
	
	void eliminarDetalleReposicion(long detalleId);

}
