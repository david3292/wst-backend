package com.altioracorp.wst.repositorio.compras;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.altioracorp.wst.dominio.compras.OrdenCompra;
import com.altioracorp.wst.dominio.compras.OrdenCompraEstado;
import com.altioracorp.wst.repositorio.RepositorioBase;

public interface IOrdenCompraRepositorio extends RepositorioBase<OrdenCompra> {

	List<OrdenCompra> findByCotizacion_Id(long id);
	
	Optional<OrdenCompra> findByCodigoProveedorAndCotizacion_Id(String codigoProveedor, long id);
	
	List<OrdenCompra> findByBodegaEntregaAndEstadoIn(String bodegaEntraga, Collection<OrdenCompraEstado> estados);
	
	List<OrdenCompra> findByEstadoIn(Collection<OrdenCompraEstado> estados);
	
	@Query(value = "select count(id) from orden_compra where condicion_pago != condicion_pago_gp and cotizacion_id = :cotizacionId", nativeQuery = true)
	int countCambioCondicionPagoProveedorPorCotizacionid(@Param("cotizacionId")long cotizacionId);
	
	@Query(value = "select count(ocd.id) from orden_compra oc inner join orden_compra_detalle ocd on oc.id = ocd.orden_compra_id "
			+ "where margen_utilidad != margen_utilidad_original and cotizacion_id = :cotizacionId", nativeQuery = true)
	int countCambioMargenUtilidadDetallePorCotizacionId(@Param("cotizacionId")long cotizacionId);
	
	@Query(value = "select codigo_proveedor,nombre_proveedor, condicion_pago_gp, condicion_pago from orden_compra where condicion_pago != condicion_pago_gp and cotizacion_id = :cotizacionId", nativeQuery = true)
	List<Object[]> ordenesCompraCambioCondicionPagoProveedorPorCotizacionid(@Param("cotizacionId")long cotizacionId);
	
	@Query(value = "select ocd.codigo_articulo, cd.descripcion_articulo, ocd.margen_utilidad_original, ocd.margen_utilidad, oc.nombre_proveedor from orden_compra oc inner join orden_compra_detalle ocd on oc.id = ocd.orden_compra_id "
			+ "inner join cotizacion_detalle cd on ocd.cotizacion_detalle_id = cd.id "
			+ "where oc.cotizacion_id = :cotizacionId", nativeQuery = true)
	List<Object[]> detallesCompraCambioMargenUtilidadDetallePorCotizacionId(@Param("cotizacionId")long cotizacionId);
	
	boolean existsByCotizacion_id(long cotizacionId);
}
