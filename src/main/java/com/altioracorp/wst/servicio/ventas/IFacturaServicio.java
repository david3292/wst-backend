package com.altioracorp.wst.servicio.ventas;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.altioracorp.wst.dominio.ventas.DocumentoDetalle;
import com.altioracorp.wst.dominio.ventas.DocumentoFactura;
import com.altioracorp.wst.dominio.ventas.DocumentoNotaCredito;
import com.altioracorp.wst.dominio.ventas.DocumentoReserva;
import com.altioracorp.wst.dominio.ventas.dto.FacturaConsultaDTO;
import com.altioracorp.wst.dominio.ventas.dto.FacturaCotizacionDTO;
import com.altioracorp.wst.dominio.ventas.dto.FacturaDTO;

public interface IFacturaServicio {

	DocumentoFactura listarPorCotizacionID(Long cotizacionID);
	
	FacturaDTO reintegrarFactura(long id);

    List<DocumentoFactura> listarPorEstadoError();

    List<FacturaDTO> listarTodasPorCotizacionID(Long cotizacionID);

    void liberarReservasArticulosYCompartimientos(List<DocumentoDetalle> detalles);

    Page<FacturaCotizacionDTO> consultarFacturasCotizaciones(Pageable pageabe, FacturaConsultaDTO consulta);

    List<DocumentoFactura> listarEmitidasPorUsuarioYPuntoVenta(long idPuntoVenta);

    DocumentoFactura obtenerFacturaPorIdParaDevoluciones(long facturaID);
    
    DocumentoFactura generarFactura(DocumentoReserva reserva);
    
    DocumentoFactura generarRefacturacion(DocumentoNotaCredito notaCredito);

    byte[] generarReporte(String numeroFactura);
}
