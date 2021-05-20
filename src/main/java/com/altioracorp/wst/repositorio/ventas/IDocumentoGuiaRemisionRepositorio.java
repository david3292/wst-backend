package com.altioracorp.wst.repositorio.ventas;

import com.altioracorp.wst.dominio.ventas.DocumentoGuiaRemision;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface IDocumentoGuiaRemisionRepositorio extends IDocumentoRepositorio<DocumentoGuiaRemision> {

	List<DocumentoGuiaRemision> findByDocumentoPadreId(long documentoPadreId);

	List<DocumentoGuiaRemision> findByCotizacion_IdOrderByModificadoPorAsc(long cotizacionID);

	long countByDocumentoPadreId(long documentoPadreId);

	@Query(
			value = "SELECT COUNT(*) FROM documento_guia_remision gr INNER JOIN documento_guia_despacho gd ON gr.documento_padre_id = gd.id " +
					"INNER JOIN documento_factura df ON gd.documento_factura_id = df.id WHERE df.id = :facturaId",
			nativeQuery = true
	)
	long countGuiaRemisionPorFacturaId(long facturaId);
	
	@Query(value = "select d.id padreId, g.id guiaId, d.tipo, d.estado  from documento_guia_remision g inner join documento d on g.documento_padre_id = d.id where g.id = :_guiaRemisionId", nativeQuery = true)
	List<Object[]> obtenerInfoGuiaRemision(@Param("_guiaRemisionId") long guiaRemisionId);

}
