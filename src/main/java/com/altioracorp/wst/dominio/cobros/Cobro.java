package com.altioracorp.wst.dominio.cobros;

import com.altioracorp.wst.dominio.EntidadBase;
import com.altioracorp.wst.dominio.sistema.PuntoVenta;
import com.altioracorp.wst.util.LocalDateTimeDeserialize;
import com.altioracorp.wst.util.LocalDateTimeSerialize;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@SuppressWarnings("serial")
@Entity
public class Cobro extends EntidadBase {

	@ManyToOne(fetch = FetchType.EAGER)
	private PuntoVenta puntoVenta;

	@NotNull
	private String codigoCliente;
	
	@JsonSerialize(using = LocalDateTimeSerialize.class)
	@JsonDeserialize(using = LocalDateTimeDeserialize.class)
	private LocalDateTime fecha;

	private BigDecimal deuda;

	private BigDecimal valor;

	private String numero;

	private boolean activo;

	@Enumerated(EnumType.STRING)
	private CobroEstado estado;

	@OneToMany(cascade = {CascadeType.ALL}, fetch = FetchType.LAZY, orphanRemoval = true)
	@JoinColumn(name = "cobro_id", nullable = false, insertable = true)
	private List<CobroCuotaFactura> cuotaFacturas;

	@OneToMany(cascade = {CascadeType.ALL}, fetch = FetchType.LAZY, orphanRemoval = true)
	@JoinColumn(name = "cobro_id", nullable = false, insertable = true)
	private List<CobroFormaPago> cobroFormaPagos;

	@NotNull
	private String nombreCliente;

	@Transient
	private String nombreVendedor;

	public void agregarCobroFormaPago(CobroFormaPago formaPago) {
		cobroFormaPagos.add(formaPago);
		actualizarValor(formaPago);
	}

	private void actualizarValor(CobroFormaPago formaPago) {
		valor = valor.add(formaPago.getValor());
	}

	public void agregarCuotaFactura(List<CobroCuotaFactura> cuotasFactura) {
		cuotaFacturas.addAll(cuotasFactura);		
	}
	
	public List<CobroFormaPago> getCobroFormaPagos() {
		return cobroFormaPagos;
	}

	public String getCodigoCliente() {
		return codigoCliente;
	}

	public List<CobroCuotaFactura> getCuotaFacturas() {
		return cuotaFacturas;
	}

	public BigDecimal getDeuda() {
		return deuda;
	}

	public CobroEstado getEstado() {
		return estado;
	}

	public LocalDateTime getFecha() {
		return fecha;
	}

	public String getNumero() {
		return numero;
	}

	public PuntoVenta getPuntoVenta() {
		return puntoVenta;
	}

	public BigDecimal getValor() {
		return valor;
	}

	public boolean isActivo() {
		return activo;
	}

	public String getNombreCliente() {
		return nombreCliente;
	}

	public String getNombreVendedor() {
		return nombreVendedor;
	}

	public void setActivo(boolean activo) {
		this.activo = activo;
	}

	public void setCobroFormaPagos(List<CobroFormaPago> cobroFormaPagos) {
		this.cobroFormaPagos = cobroFormaPagos;
	}

	public void setCodigoCliente(String codigoCliente) {
		this.codigoCliente = codigoCliente;
	}

	public void setCuotaFacturas(List<CobroCuotaFactura> cuotaFacturas) {
		this.cuotaFacturas = cuotaFacturas;
	}

	public void setDeuda(BigDecimal deuda) {
		this.deuda = deuda;
	}

	public void setEstado(CobroEstado estado) {
		this.estado = estado;
	}

	public void setFecha(LocalDateTime fecha) {
		this.fecha = fecha;
	}

	public void setNumero(String numero) {
		this.numero = numero;
	}

	public void setPuntoVenta(PuntoVenta puntoVenta) {
		this.puntoVenta = puntoVenta;
	}

	public void setValor(BigDecimal valor) {
		this.valor = valor;
	}

	public void setNombreCliente(String nombreCliente) {
		this.nombreCliente = nombreCliente;
	}

	public void setNombreVendedor(String nombreVendedor) {
		this.nombreVendedor = nombreVendedor;
	}

	@Override
	public String toString() {
		return "Cobro [puntoVenta=" + puntoVenta + ", codigoCliente=" + codigoCliente + ", fecha=" + fecha + ", deuda="
				+ deuda + ", valor=" + valor + ", numero=" + numero + ", activo=" + activo + ", estado=" + estado
				+ ", cuotaFacturas=" + cuotaFacturas + ", cobroFormaPagos=" + cobroFormaPagos + "]";
	}

}
