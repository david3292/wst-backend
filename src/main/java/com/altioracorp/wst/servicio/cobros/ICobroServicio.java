package com.altioracorp.wst.servicio.cobros;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.altioracorp.gpintegrator.client.Bank.Bank;
import com.altioracorp.gpintegrator.client.CreditCard.CreditCard;
import com.altioracorp.gpintegrator.client.ReceivablePostedTransaction.Rm20101Header;
import com.altioracorp.wst.dominio.cobros.Cobro;
import com.altioracorp.wst.dominio.cobros.CobroConsultaDTO;
import com.altioracorp.wst.dominio.cobros.CobroCuotaFactura;
import com.altioracorp.wst.dominio.cobros.CobroDTO;


public interface ICobroServicio {

    List<Bank> listarBancos();

    List<CreditCard> listarTarjetasCredito();

    CobroDTO procesarCobro(long cobroId);

    Cobro registrarCobro(Cobro cobro);

    List<Rm20101Header> listarNotasCreditoPorCliente(String codigoCliente);

    List<Rm20101Header> listarCobrosPorClienteGP(String codigoCliente);
    
    Cobro buscarCobroPorEstadoYCodigoClienteYUsuario(String codigoCliente);

    byte[] generarReportePorNumero(String numero);

    Page<Cobro> consultarCobros(Pageable pageabe, CobroConsultaDTO consulta);

	boolean eliminarCobroFormaPago(Long cobroFormaPagoID, Long cobroID);
	
	boolean exsiteNumeroChequeEnOtrosCobros(String codigoCliente, String numeroCheque);
	
	Cobro registrarCobroGeneral(Cobro cobro);
	
	Cobro listarCobroPorId(long id);
	
	Cobro registrarCobroCuotaFactura(long cobroId, List<CobroCuotaFactura> cuotas);
	
	boolean validarAnticipoTieneAplicacionesConEstadoError(String numeroAnticipo);
	
	CobroDTO efectivizarChequePosFechado (long cobroFormaPagoId);
	
	void canjearChequePosFechado (long cobroFormaPagoId);
	
	List<Cobro> listarCobrosEstadoPendiente();
	
	CobroDTO reintegrarCobro(long cobroId);

}
