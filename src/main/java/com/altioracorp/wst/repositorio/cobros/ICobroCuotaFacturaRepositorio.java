package com.altioracorp.wst.repositorio.cobros;

import java.util.List;

import com.altioracorp.wst.dominio.cobros.CobroCuotaFactura;
import com.altioracorp.wst.repositorio.RepositorioBase;

public interface ICobroCuotaFacturaRepositorio extends RepositorioBase<CobroCuotaFactura> {

	List<CobroCuotaFactura> findByCobroFormaPagoId(long cobroFormaPagoId);

}
