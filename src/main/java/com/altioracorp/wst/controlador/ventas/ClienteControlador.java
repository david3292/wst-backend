package com.altioracorp.wst.controlador.ventas;

import java.math.BigDecimal;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.altioracorp.gpintegrator.client.Country.Country;
import com.altioracorp.gpintegrator.client.Customer.Customer;
import com.altioracorp.wst.dominio.ventas.CriterioClienteDTO;
import com.altioracorp.wst.servicio.ventas.IClienteServicio;

@RestController
@RequestMapping("/clientes")
public class ClienteControlador {

	@Autowired
	private IClienteServicio servicio;
	
	@GetMapping("/{id}")
	public ResponseEntity<Customer> listarPorCustomerNumber(@PathVariable("id") String id) {
		Customer customer = servicio.obtenerCustomerPorCurstomerNumbre(id);
		return new ResponseEntity<Customer>(customer, HttpStatus.OK);
	}
	
	@PostMapping("/clientePorCriterio")
	public ResponseEntity<List<Customer>> listarPorCustomerPorCriterio(@RequestBody CriterioClienteDTO dto) {
		List<Customer> customer = servicio.obtenerCustomersPorCriterio(dto);
		return new ResponseEntity<List<Customer>>(customer, HttpStatus.OK);
	}
	
	@PostMapping("/clientesActivosPorCriterio")
	public ResponseEntity<List<Customer>> listarPorCustomerActivosPorCriterio(@RequestBody CriterioClienteDTO dto) {
		List<Customer> customer = servicio.obtenerCustomersActivosPorCriterio(dto);
		return new ResponseEntity<List<Customer>>(customer, HttpStatus.OK);
	}
	
	
	@GetMapping("/creditoDisponible/{custnmbr}/{crlamtm}")
	public ResponseEntity<BigDecimal> calcuarCreditoDisponible(@PathVariable("custnmbr") String custnmbr, @PathVariable("crlamtm") String crlamtm) {
		BigDecimal credito = servicio.calcularCreditoDisponibleWST(custnmbr, new BigDecimal(crlamtm));
		return new ResponseEntity<BigDecimal>(credito, HttpStatus.OK);
	}
	
	@GetMapping("/creditoDisponibleGP/{custnmbr}")
	public ResponseEntity<BigDecimal> calcuarCreditoDisponibleGP(@PathVariable("custnmbr") String custnmbr) {
		BigDecimal credito = servicio.calcularCreditoDisponible(custnmbr);
		return new ResponseEntity<BigDecimal>(credito, HttpStatus.OK);
	}
	
	@PostMapping
	public ResponseEntity<Object> registrar(@RequestBody Customer customer) {
		Boolean resultado = servicio.registrarCliente(customer);
		return new ResponseEntity<Object>(resultado, HttpStatus.OK);
	}
	
	@PutMapping
	public ResponseEntity<Object> modificar(@RequestBody Customer customer) {
		Boolean resultado = servicio.modificarCliente(customer);
		return new ResponseEntity<Object>(resultado, HttpStatus.OK);
	}
	
	@GetMapping("/listarPaisesGP")
	public ResponseEntity<List<Country>> listarPaisesGP() {
		List<Country> paises = servicio.obtenerPaisesTodos();
		return new ResponseEntity<List<Country>>(paises, HttpStatus.OK);
	}
	
}
