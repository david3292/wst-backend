package com.altioracorp.wst.repositorio.ventas;

import com.altioracorp.wst.dominio.ventas.DocumentoGuiaDespacho;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface IDocumentoGuiaDespachoRepositorio extends IDocumentoRepositorio<DocumentoGuiaDespacho> {

	Optional<DocumentoGuiaDespacho> findByDocumentoFacturaId(Long idFactura);
	
	List<DocumentoGuiaDespacho> findByCotizacion_IdOrderByModificadoFechaAsc(Long cotizacionID);
	
	@Query(value = "select gd.id, c.codigo_cliente, c.nombre_cliente, df.numero as numeroFactura, "
			+ "df.fecha_emision as fechaEmisionFactura, d.numero as numeroGuiaDespacho, d.fecha_emision as fechaEmisionGuiaDespacho, "
			+ "df.total, d.estado, gd.entrega , sum(docd.peso) as pesoTotal from documento d inner join documento_guia_despacho gd on d.id = gd.id "
			+ "inner join "
			+ "(select dd.id, dd.numero, dd.fecha_emision, ddf.total, ddf.entrega "
			+ "from documento dd inner join documento_factura ddf on ddf.id = dd.id) as df on df.id = gd.documento_factura_id inner join cotizacion c on c.id = d.cotizacion_id "
			+ "inner join ("
			+ "select dd.documento_id,(dd.cantidad * cd.peso_articulo) as peso from documento_detalle dd inner join cotizacion_detalle cd on dd.cotizacion_detalle_id = cd.id "
			+ ") as docd on docd.documento_id = d.id "
			+ "where gd.bodega = :_bodega and d.estado = :_estado "
			+ "group by gd.id, c.codigo_cliente, c.nombre_cliente, df.numero, df.fecha_emision, d.numero, d.fecha_emision, "
			+ "df.total, d.estado, gd.entrega", nativeQuery = true)
	List<Object[]> obtenerGuiasDespachoOverviewByBodegaAndEstado(@Param("_bodega") String bodega, @Param("_estado") String estado);

	long countByDocumentoFacturaId(long documentoFacturaId);

	DocumentoGuiaDespacho findByDocumentoFacturaId(long facturaId);
}
