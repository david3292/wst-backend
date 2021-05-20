package com.altioracorp.wst.repositorio.ventas;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

import com.altioracorp.wst.dominio.ventas.DocumentoEstado;
import com.altioracorp.wst.dominio.ventas.DocumentoReserva;
import com.altioracorp.wst.dominio.ventas.TipoReserva;

public interface IDocumentoReservaRepositorio extends IDocumentoRepositorio<DocumentoReserva> {

	List<DocumentoReserva> findByEstadoInAndCotizacion_CodigoCliente(Collection<DocumentoEstado> estados, String codigoCliente);
	
	Optional<DocumentoReserva> findByEstadoAndTipoReservaAndCotizacion_Id(DocumentoEstado estado, TipoReserva tipo, long cotizacionID);
	
	List<DocumentoReserva> findByCotizacion_IdOrderByModificadoPorAsc(Long cotizacionID);
	
	List<DocumentoReserva> findByEstadoInAndTipoReservaInAndCotizacion_Id(Collection<DocumentoEstado> estados,
			Collection<TipoReserva> tipos, Long cotizacionID);
	
	List<DocumentoReserva> findByCreadoPorAndEstadoAndTipoReservaAndCotizacion_PuntoVenta_IdOrderByModificadoFechaDesc(
			String usuario, DocumentoEstado estado, TipoReserva tipo, long puntoVentaId);
}
