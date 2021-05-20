package com.altioracorp.wst.servicio.cobros;

import java.math.BigDecimal;
import java.util.List;

import com.altioracorp.wst.dominio.cobros.CobroDocumentoDTO;
import com.altioracorp.wst.dominio.cobros.CobroDocumentoValorDTO;
import com.altioracorp.wst.dominio.ventas.DocumentoFactura;

public interface ICondicionCobroFacturaServicio {

	void registrarDesgloseCobrosFactura(DocumentoFactura factura);
	
	List<CobroDocumentoValorDTO> consultarCobrosPorCliente(String codigoCliente);
	
	CobroDocumentoDTO consultarDetallePagoPorFactura(Long idFactura);
	
	boolean existenCodicionesCobroVencidasPorCliente(String codigoCliente);
	
	BigDecimal deudaTotalPorCliente(String codigoCliente);
}
