package com.altioracorp.wst.repositorio.sistema;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.altioracorp.wst.dominio.sistema.Bodega;
import com.altioracorp.wst.repositorio.RepositorioBase;

public interface IBodegaRepositorio extends RepositorioBase<Bodega>{	
	
	Optional<Bodega> findByCodigo(String codigo);
	
	@Query(value = "select b.codigo from usuario_perfil up "
			+ "inner join configuracion_usuario_perfil cup on cup.usuario_perfil_id = up.id "
			+ "inner join punto_venta pv on pv.id = cup.punto_venta_id "
			+ "inner join punto_venta_bodega pvb on pvb.punto_venta_id = pv.id "
			+ "inner join bodega b on b.id = pvb.bodega_id "
			+ "where pvb.principal = 1 and up.usuario_id = :_uId and perfil_id = :_pId and pv.id = :_pvId", nativeQuery = true)
	String obtenerCodigoBodegaPrincipalPorUsuarioIdPerfilIdPuntoVentaId(@Param("_uId")long usuarioId, @Param("_pId")long perfilId, @Param("_pvId")long puntoVentaId);
	
	List<Bodega> findByActivoTrue();
	
}
