package com.altioracorp.wst.servicio.ventas;

import java.util.List;

import com.altioracorp.gpintegrator.client.GuiaRemision.AltGuiaTransaction;
import com.altioracorp.gpintegrator.client.ReceivablePostedTransaction.Rm20101Header;
import com.altioracorp.gpintegrator.client.Sales.DocumentNumber;
import com.altioracorp.gpintegrator.client.Sales.DocumentResponse;
import com.altioracorp.gpintegrator.client.Sales.SopRequest;
import com.altioracorp.gpintegrator.client.Sales.SopResponse;
import com.altioracorp.gpintegrator.client.inventory.IvResponse;
import com.altioracorp.wst.dominio.sistema.PuntoVenta;
import com.altioracorp.wst.dominio.sistema.Secuencial;
import com.altioracorp.wst.dominio.sistema.TipoDocumento;
import com.altioracorp.wst.dominio.ventas.DocumentoBase;
import com.altioracorp.wst.dominio.ventas.DocumentoGuiaRemision;

public interface IDocumentoServicio {

	Secuencial secuencialDocumento(PuntoVenta puntoVenta, TipoDocumento tipoDocumento);

	SopResponse integrarDocumento(SopRequest request);

	void insertarLocalizacionSop(DocumentNumber documentNumber);
	
	DocumentResponse consultarDocumento(String numeroDocumento, Integer soptype);
	
	List<Rm20101Header> consultarNotasDeCreditoPorCliente(String codigoCliente);
	
	String obtenerNumeroDocumentoGuiaRemision(DocumentoGuiaRemision guiaRemision);
	
	String obtenerBachNumber(DocumentoBase documento);

	IvResponse llamarApiIntegracionGuiaRemision(AltGuiaTransaction guiaTransaction);
	
	List<Rm20101Header> consultarCobrosPorCliente(String codigoCliente);
	
	boolean documentoEstaContabilizado(String numeroDocumento);
}
