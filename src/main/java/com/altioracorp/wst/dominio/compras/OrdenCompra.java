package com.altioracorp.wst.dominio.compras;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicInteger;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Transient;

import org.apache.commons.collections4.CollectionUtils;

import com.altioracorp.wst.dominio.EntidadBase;
import com.altioracorp.wst.dominio.ventas.Cotizacion;
import com.altioracorp.wst.util.LocalDateTimeDeserialize;
import com.altioracorp.wst.util.LocalDateTimeSerialize;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

@SuppressWarnings("serial")
@Entity
public class OrdenCompra extends EntidadBase {
	@ManyToOne(fetch = FetchType.EAGER)
	private Cotizacion cotizacion;

	private String codigoProveedor;

	private String nombreProveedor;
	
	private String telefonoProveedor;
	
	private String emailProveedor;

	@JsonSerialize(using = LocalDateTimeSerialize.class)
	@JsonDeserialize(using = LocalDateTimeDeserialize.class)
	private LocalDateTime fechaEmision;
	
	@JsonSerialize(using = LocalDateTimeSerialize.class)
	@JsonDeserialize(using = LocalDateTimeDeserialize.class)
	private LocalDateTime fechaRecepcion;

	@Enumerated(EnumType.STRING)
	private OrdenCompraEstado estado;

	private String condicionPagoGp;
	
	private String condicionPago;
	
	private String bodegaEntrega;

	private String numero;
	
	private String numeroRecepcion;	

	private String referencia;

	private String codigoVendedor;
	
	private BigDecimal subTotalIva;
	
	private BigDecimal subTotalNoIva;
	
	private BigDecimal total;
	
	private BigDecimal totalIva;
	
	private String mensajeError;
	
	@JsonIgnore
	@Transient
	private String direccionDestino;
	
	@OneToMany(cascade = {CascadeType.ALL}, fetch = FetchType.LAZY, orphanRemoval = true)
	@JoinColumn(name = "orden_compra_id", nullable = false)
	private List<OrdenCompraDetalle> detalle = new ArrayList<>();

	public void agregarLineaDetalle(OrdenCompraDetalle linea) {
		detalle.add(linea);
	}

	public void eliminarLineaDetalle(OrdenCompraDetalle linea) {
		detalle.remove(linea);
	}
	
	public String getCondicionPago() {
		return condicionPago;
	}

	public void setCondicionPago(String condicionPago) {
		this.condicionPago = condicionPago;
	}

	public String getBodegaEntrega() {
		return bodegaEntrega;
	}

	public String getCodigoProveedor() {
		return codigoProveedor;
	}

	public String getCodigoVendedor() {
		return codigoVendedor;
	}

	public Cotizacion getCotizacion() {
		return cotizacion;
	}

	public OrdenCompraEstado getEstado() {
		return estado;
	}

	public LocalDateTime getFechaEmision() {
		return fechaEmision;
	}

	public String getNombreProveedor() {
		return nombreProveedor;
	}

	public String getNumero() {
		return numero;
	}

	public String getReferencia() {
		return referencia;
	}	

	public void setBodegaEntrega(String bodegaEntrega) {
		this.bodegaEntrega = bodegaEntrega;
	}

	public void setCodigoProveedor(String codigoProveedor) {
		this.codigoProveedor = codigoProveedor;
	}

	public void setCodigoVendedor(String codigoVendedor) {
		this.codigoVendedor = codigoVendedor;
	}

	public void setCotizacion(Cotizacion cotizacion) {
		this.cotizacion = cotizacion;
	}

	public void setEstado(OrdenCompraEstado estado) {
		this.estado = estado;
	}

	public void setFechaEmision(LocalDateTime fechaEmision) {
		this.fechaEmision = fechaEmision;
	}

	public void setNombreProveedor(String nombreProveedor) {
		this.nombreProveedor = nombreProveedor;
	}

	public void setNumero(String numero) {
		this.numero = numero;
	}
	
	public void setReferencia(String referencia) {
		this.referencia = referencia;
	}

	public List<OrdenCompraDetalle> getDetalle() {
		return detalle;
	}
	
	public String getMensajeError() {
		return mensajeError;
	}

	public void setMensajeError(String mensajeError) {
		this.mensajeError = mensajeError;
	}

	public String getNumeroRecepcion() {
		return numeroRecepcion;
	}

	public void setNumeroRecepcion(String numeroRecepcion) {
		this.numeroRecepcion = numeroRecepcion;
	}
	
	public BigDecimal getSubTotalIva() {
		return subTotalIva;
	}

	public void setSubTotalIva(BigDecimal subTotalIva) {
		this.subTotalIva = subTotalIva;
	}

	public BigDecimal getSubTotalNoIva() {
		return subTotalNoIva;
	}

	public void setSubTotalNoIva(BigDecimal subTotalNoIva) {
		this.subTotalNoIva = subTotalNoIva;
	}

	public BigDecimal getTotal() {
		return total;
	}

	public void setTotal(BigDecimal total) {
		this.total = total;
	}

	public BigDecimal getTotalIva() {
		return totalIva;
	}

	public void setTotalIva(BigDecimal totalIva) {
		this.totalIva = totalIva;
	}

	public void setDetalle(List<OrdenCompraDetalle> detalle) {
		this.detalle = detalle;
	}

	public String getTelefonoProveedor() {
		return telefonoProveedor;
	}

	public void setTelefonoProveedor(String telefonoProveedor) {
		this.telefonoProveedor = telefonoProveedor;
	}

	public String getEmailProveedor() {
		return emailProveedor;
	}

	public void setEmailProveedor(String emailProveedor) {
		this.emailProveedor = emailProveedor;
	}
	
	public String getDireccionDestino() {
		return direccionDestino;
	}

	public void setDireccionDestino(String direccionDestino) {
		this.direccionDestino = direccionDestino;
	}
	
	public LocalDateTime getFechaRecepcion() {
		return fechaRecepcion;
	}

	public void setFechaRecepcion(LocalDateTime fechaRecepcion) {
		this.fechaRecepcion = fechaRecepcion;
	}
	
	public String getCondicionPagoGp() {
		return condicionPagoGp;
	}

	public void setCondicionPagoGp(String condicionPagoGp) {
		this.condicionPagoGp = condicionPagoGp;
	}

	public void actualizarDetalle(OrdenCompraDetalle ordenCompraDetalle) {
		if(this.detalle == null)
			this.detalle = new ArrayList<OrdenCompraDetalle>();
		
		Optional<OrdenCompraDetalle> detalleOpt = this.detalle.stream().filter(d -> d.getCodigoArticulo().equals(ordenCompraDetalle.getCodigoArticulo())).findFirst();
		if(detalleOpt.isPresent()) {
			OrdenCompraDetalle detalleActualizar = detalleOpt.get();
			detalleActualizar.setCantidad(ordenCompraDetalle.getCantidad());
			detalleActualizar.setSaldo(ordenCompraDetalle.getSaldo());
			detalleActualizar.setCostoUnitario(ordenCompraDetalle.getCostoUnitario());
			detalleActualizar.setPrecioVenta(ordenCompraDetalle.getPrecioVenta());			
			detalleActualizar.setMargenUtilidad(ordenCompraDetalle.getMargenUtilidad());
			detalleActualizar.setMargenUtilidadOriginal(ordenCompraDetalle.getMargenUtilidadOriginal());
			detalleActualizar.calcularTotal();
		}else {
			this.detalle.add(ordenCompraDetalle);
		}
	}
	
	public boolean eliminarDetalle(String codigoArticulo) {
		boolean eliminado = false;
		if(!CollectionUtils.isEmpty(this.detalle)) {
			eliminado = this.detalle.removeIf(d -> d.getCodigoArticulo().equals(codigoArticulo));
		}
		return eliminado;
	}
	
	public void calcularTotales() {
		this.subTotalNoIva = this.detalle.stream().filter(x -> x.getCotizacionDetalle().getImpuestoValor().compareTo(BigDecimal.ZERO) == 0).map(OrdenCompraDetalle::getTotal).reduce(BigDecimal.ZERO, BigDecimal::add);
		this.subTotalIva = this.detalle.stream().filter(x -> x.getCotizacionDetalle().getImpuestoValor().compareTo(BigDecimal.ZERO) > 0).map(OrdenCompraDetalle::getTotal).reduce(BigDecimal.ZERO, BigDecimal::add);
		this.total = this.subTotalIva.add(subTotalNoIva);
		calcularIva();
		this.total = this.subTotalIva.add(this.subTotalNoIva).add(this.totalIva);
	}
	
	private void calcularIva() {
		this.totalIva = BigDecimal.ZERO;
		this.detalle.stream().filter(x -> x.getCotizacionDetalle().getImpuestoValor().compareTo(BigDecimal.ZERO) > 0).findFirst()
				.ifPresent(x -> {
					this.totalIva = this.subTotalIva.multiply(x.getCotizacionDetalle().getImpuestoValor()).divide(new BigDecimal(100));
				});
	}
	
	public BigDecimal getSubTotal() {
		return this.subTotalIva.add(this.subTotalNoIva);
	}
	
	public void eliminarDetalle(OrdenCompraDetalle ordenCompraDetalle) {
		if(!CollectionUtils.isEmpty(this.detalle))
			this.detalle.removeIf(d -> d.getCodigoArticulo().equals(ordenCompraDetalle.getCodigoArticulo()));
	}
	
	public void establecerSecuenciales() {
		final int base = 16384;
		AtomicInteger secuencial = new AtomicInteger(base);
		if(CollectionUtils.isNotEmpty(this.detalle)) {
			this.detalle.forEach(d -> {
				d.setNumeroSecuencia(secuencial.get());
				secuencial.set(secuencial.get() + base);
			});
		}
	}
}
