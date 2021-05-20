package com.altioracorp.wst.repositorio.cobros;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.altioracorp.wst.dominio.cobros.CobroFormaPago;
import com.altioracorp.wst.dominio.cobros.CobroFormaPagoEstado;
import com.altioracorp.wst.dominio.sistema.FormaPagoNombre;
import com.altioracorp.wst.repositorio.RepositorioBase;

public interface ICobroFormaPagoRepositorio extends RepositorioBase<CobroFormaPago>{

	Optional<CobroFormaPago> findByFormaPagoAndNumeroFacturaAndEstado(FormaPagoNombre formaPago, String numeroFactura, CobroFormaPagoEstado estado);
	
	List<CobroFormaPago> findByFormaPagoAndNumeroDocumentoAndBanco(FormaPagoNombre formaPago, String numeroDocumento, String banco);
	
	@Query(value = "select c.numero, cf.numero_documento, cf.banco from cobro c inner join cobro_forma_pago cf on  c.id= cf.cobro_id "
			+ "where c.codigo_cliente= :_cliente and cf.forma_pago in ('CHEQUE','CHEQUE_A_FECHA') "
			+ "and cf.numero_documento= :_numeroCheque ", nativeQuery = true)
	List<Object[]> existeChuequeEnOtrosCobrosPorCliente(@Param("_numeroCheque") String numeroCheque, @Param("_cliente") String cliente);
}
