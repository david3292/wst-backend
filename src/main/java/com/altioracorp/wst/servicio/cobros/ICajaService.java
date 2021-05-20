package com.altioracorp.wst.servicio.cobros;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.altioracorp.wst.dominio.cobros.Caja;
import com.altioracorp.wst.dominio.cobros.CajaConsultaDTO;
import com.altioracorp.wst.dominio.cobros.CajaDetalle;
import com.altioracorp.wst.dominio.cobros.CajaDetalleConsultaDTO;
import com.altioracorp.wst.dominio.cobros.CierreCajaDetalleDTO;

public interface ICajaService {

	Caja consultarCobrosPorPuntoVenta(Long puntoVentaID);
	
	byte[] cerrarCaja(Caja caja);
	
	Page<CajaDetalle> consultarCajaCierre(final Pageable pageable, final CajaConsultaDTO consulta);
	
	List<CierreCajaDetalleDTO> consultarDetallaCierreCaje(CajaDetalleConsultaDTO consulta);
	
	byte[] generarReporteCierreCaja(long cajaId);
	
}
