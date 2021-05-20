package com.altioracorp.wst.repositorio.ventas;

import java.util.Optional;

import com.altioracorp.wst.dominio.ventas.ReservaArticuloCompartimiento;
import com.altioracorp.wst.repositorio.RepositorioBase;

public interface IReservaArticuloCompartimientoRepositorio extends RepositorioBase<ReservaArticuloCompartimiento> {
	
	Optional<ReservaArticuloCompartimiento> findByCodigoArticuloAndCodigoBodegaAndCompartimiento(String codigoArticulo, String codigoBodega, String compartimiento);

}
