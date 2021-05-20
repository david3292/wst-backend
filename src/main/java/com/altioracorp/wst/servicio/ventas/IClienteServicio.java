package com.altioracorp.wst.servicio.ventas;

import java.math.BigDecimal;
import java.util.List;

import com.altioracorp.gpintegrator.client.Country.Country;
import com.altioracorp.gpintegrator.client.Customer.Customer;
import com.altioracorp.wst.dominio.ventas.CriterioClienteDTO;

public interface IClienteServicio {

	Customer obtenerCustomerPorCurstomerNumbre(String customerNumber);
	
	List<Customer> obtenerCustomersPorCriterio(CriterioClienteDTO dto);
	
	List<Customer> obtenerCustomersActivosPorCriterio(CriterioClienteDTO dto);
	
	BigDecimal calcularCreditoDisponible(String customerNumber);
	
	BigDecimal calcularCreditoDisponibleWST( String customerNumber, BigDecimal crlmtamt );
	
	boolean registrarCliente(Customer cliente);
	
	boolean modificarCliente(Customer cliente);
	
	List<Country> obtenerPaisesTodos();
}
