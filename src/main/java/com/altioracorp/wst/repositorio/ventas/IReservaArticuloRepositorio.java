package com.altioracorp.wst.repositorio.ventas;

import java.util.Optional;

import com.altioracorp.wst.dominio.ventas.ReservaArticulo;
import com.altioracorp.wst.repositorio.RepositorioBase;

public interface IReservaArticuloRepositorio extends RepositorioBase<ReservaArticulo> {

	Optional<ReservaArticulo> findByCodigoArticuloAndCodigoBodega(String codigoArticulo, String codigoBodega);

}
