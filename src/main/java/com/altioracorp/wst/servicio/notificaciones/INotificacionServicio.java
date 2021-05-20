package com.altioracorp.wst.servicio.notificaciones;

import java.util.List;

import com.altioracorp.wst.dominio.cobros.ChequePosfechadoDTO;
import com.altioracorp.wst.dominio.compras.RecepcionCompra;
import com.altioracorp.wst.dominio.ventas.DocumentoFactura;
import com.altioracorp.wst.dominio.ventas.DocumentoReserva;
import com.altioracorp.wst.dominio.ventas.DocumentoTransferencia;
import com.altioracorp.wst.dominio.ventas.DocumentoTransferenciaSalida;

public interface INotificacionServicio {

	void probarNotificacion();
	
	void notificarTransferenciaAnulada(DocumentoTransferencia transferencia);
	
	void notificarFacturaGenerada(DocumentoFactura factura);
	
	void notificarFacturaGeneradaError(DocumentoFactura factura);
	
	void notificarTransferenciaIncompleta (DocumentoTransferenciaSalida transferenciaSalida, DocumentoReserva reserva);
	
	void notificarReservaListaParaFaturar (DocumentoReserva reserva);
	
	void notificarChequesPosfechadosPorAutorizar();

	void notificarRecepcionOrdenCompra (RecepcionCompra recepcion);

	void notificarFacturaGeneradaRefacturacion(DocumentoFactura factura);
	
	void notificarChequesPosfechadosRechazados(String observacion, String usuarioAnalista, List<ChequePosfechadoDTO> cheques);

}
