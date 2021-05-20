package com.altioracorp.wst.repositorio.sistema;

import java.util.List;
import java.util.Optional;

import com.altioracorp.wst.dominio.sistema.FormaPago;
import com.altioracorp.wst.dominio.sistema.FormaPagoPuntoVenta;
import com.altioracorp.wst.dominio.sistema.PuntoVenta;
import com.altioracorp.wst.repositorio.RepositorioBase;

public interface IFormaPagoPvtaRepositorio extends RepositorioBase<FormaPagoPuntoVenta> {

	Optional<FormaPagoPuntoVenta> findByFormaPagoAndPuntoVenta(FormaPago fpago, PuntoVenta pvta);
	
	List<FormaPagoPuntoVenta> findByPuntoVenta(PuntoVenta pvta);
	
	List<FormaPagoPuntoVenta> findByPuntoVentaAndActivoTrue(PuntoVenta pvta);
}
