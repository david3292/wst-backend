package com.altioracorp.wst.repositorio.ventas;

import com.altioracorp.wst.dominio.sistema.TipoDocumento;
import com.altioracorp.wst.dominio.ventas.Cotizacion;
import com.altioracorp.wst.dominio.ventas.DocumentoEstado;
import com.altioracorp.wst.dominio.ventas.DocumentoFactura;

import java.util.Collection;
import java.util.List;

public interface IDocumentoFacturaRepositorio extends IDocumentoRepositorio<DocumentoFactura> {

	List<DocumentoFactura> findByEstadoInAndCotizacion_CodigoCliente(Collection<DocumentoEstado> estados, String codigoCiente);

	List<DocumentoFactura> findByEstadoIn(Collection<DocumentoEstado> estados);

	List<DocumentoFactura> findByCotizacionInAndEstadoIn(List<Cotizacion> cotizacion, Collection<DocumentoEstado> estados);

	List<DocumentoFactura> findByCotizacion_IdOrderByModificadoPorAsc(Long cotizacionID);

	List<DocumentoFactura> findByCotizacionAndTipo(Cotizacion cotizacion, TipoDocumento tipoDocumento);

	List<DocumentoFactura> findByEstadoAndDespachada(DocumentoEstado estado, boolean despachada);

	List<DocumentoFactura> findByEstadoAndCotizacion_PuntoVenta_IdAndCotizacion_CreadoPorOrderByFechaEmisionDesc(DocumentoEstado estado, long id, String usuario);
	
	List<DocumentoFactura> findByEstadoInAndRefacturacionTrue(Collection<DocumentoEstado> estados);

}
