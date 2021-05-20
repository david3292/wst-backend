package com.altioracorp.wst.servicio.ventas;

import com.altioracorp.wst.dominio.ventas.DocumentoDetalle;
import com.altioracorp.wst.dominio.ventas.DocumentoNotaCredito;
import com.altioracorp.wst.dominio.ventas.TipoDevolucion;
import com.altioracorp.wst.dominio.ventas.dto.NotaCreditoConsultaDTO;
import com.altioracorp.wst.dominio.ventas.dto.NotaCreditoDTO;
import com.altioracorp.wst.dominio.ventas.dto.NotaCreditoSolicitudRespuestaDTO;

import java.util.List;
import java.util.Map;

public interface INotaCreditoServicio {

    DocumentoNotaCredito registrar(DocumentoNotaCredito notaCredito);

    Boolean validarDevolucionesEnRevisionPorFacturaId(long facturaId);

    List<NotaCreditoDTO> listarDevolucionesEnRevision(Map<String, Object> consulta);

    DocumentoNotaCredito responderSolicitudDevoluciones(NotaCreditoSolicitudRespuestaDTO respuesta);

    List<NotaCreditoConsultaDTO> listarTodasPorCotizacionID(Long cotizacionID);

    DocumentoNotaCredito actualizarLineaDetalle(long notaCreditoId, DocumentoDetalle detalle);

    DocumentoNotaCredito buscarPorId(long id);

    Boolean requiereAprobacion(List<DocumentoDetalle> detallesNotaCredito, long facturaId, TipoDevolucion tipoDevolucion);

    NotaCreditoConsultaDTO reintegrarNotaCredito(long id);

    List<DocumentoNotaCredito> listarPorEstadoError();
    
    DocumentoNotaCredito obtenerPorNumeroDocumento(String numero);
    
    boolean esGeneradaPorMotivoRefacturacion(String numero);
}
