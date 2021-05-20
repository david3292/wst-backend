package com.altioracorp.wst.repositorio.ventas;

import com.altioracorp.wst.dominio.ventas.DocumentoEstado;
import com.altioracorp.wst.dominio.ventas.DocumentoNotaCredito;
import com.altioracorp.wst.dominio.ventas.FlujoAprobacion;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

public interface IDocumentoNotaCreditoRepositorio extends IDocumentoRepositorio<DocumentoNotaCredito> {

    long countByEstadoAndDocumentoFacturaId(DocumentoEstado estado, long facturaId);
    
    List<DocumentoNotaCredito> findByEstadoAndFlujoAprobacion(DocumentoEstado estado, FlujoAprobacion flujoAprobacion);
    
    List<DocumentoNotaCredito> findByEstadoAndFlujoAprobacionAndCotizacion_PuntoVenta_Id(DocumentoEstado estado, FlujoAprobacion flujoAprobacion, long puntoVentaID);

	@Query(value = "select nc.id, mnc.tipo_devolucion, mnc.motivo, c.codigo_cliente, c.nombre_cliente, df.numero,df.fecha_emision,u.nombre_completo, pv.nombre, "
			+ "nc.revision_tecnica "
			+ "from documento_nota_credito nc inner join documento d on nc.id=d.id "
			+ "inner join motivo_nota_credito mnc on mnc.id = nc.motivo_nota_credito_id "
			+ "inner join documento_factura f on nc.documento_factura_id = f.id "
			+ "inner join documento df on f.id=df.id "
			+ "inner join usuario u on d.creado_por=u.nombre_usuario "
			+ "inner join cotizacion c on c.id=d.cotizacion_id "
			+ "inner join punto_venta pv on pv.id=c.punto_venta_id "
			+ "where nc.flujo_aprobacion= :_flujo and d.estado = :_estado and f.bodega_principal= :_bodega", nativeQuery = true)
	List<Object[]> obtenerNotasCreditoPorAprobarOverviewByFlujoAndEstadoAndBodega(@Param("_flujo") String flujoAprobacion, @Param("_estado") String estado, @Param("_bodega") String bodega);

	Optional<DocumentoNotaCredito> findByNumeroAndReferencia(String numeroNotaCredito, String numeroFactura);

	List<DocumentoNotaCredito> findByCotizacion_IdOrderByModificadoPorAsc(long cotizacionID);

	List<DocumentoNotaCredito> findByEstadoIn(Collection<DocumentoEstado> estados);
	
	Optional<DocumentoNotaCredito> findByNumero(String numero);
	
	@Query(value = "select 'si' from documento d inner join documento_nota_credito nc on d.id=nc.id "
			+ "inner join motivo_nota_credito mnc on mnc.id=nc.motivo_nota_credito_id "
			+ "where numero = :_numero and mnc.tipo_devolucion='REFACTURACION'", nativeQuery = true)
	Optional<Object> esMotivoRefacturacion(@Param("_numero")String numero);

}
