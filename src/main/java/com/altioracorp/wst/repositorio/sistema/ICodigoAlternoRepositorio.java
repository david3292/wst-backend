package com.altioracorp.wst.repositorio.sistema;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.altioracorp.wst.dominio.sistema.CodigoAlterno;
import com.altioracorp.wst.repositorio.RepositorioBase;

public interface ICodigoAlternoRepositorio extends RepositorioBase<CodigoAlterno>{

	@Query(value = "select top 1 codigo_alterno from codigo_alterno where en_uso = 0 order by id asc", nativeQuery = true)
	String getCodigoAlterno();
	
	@Modifying
	@Query(value = "update codigo_alterno set en_uso = 1 where codigo_alterno = :codigoAlterno", nativeQuery = true)
	Integer disableCodigoAlterno(@Param("codigoAlterno") String codigoAlterno);
	
}
