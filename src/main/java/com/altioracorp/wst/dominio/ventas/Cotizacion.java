package com.altioracorp.wst.dominio.ventas;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import org.apache.commons.collections4.CollectionUtils;

import com.altioracorp.wst.dominio.EntidadBase;
import com.altioracorp.wst.dominio.sistema.PuntoVenta;
import com.altioracorp.wst.dominio.sistema.TipoPago;
import com.altioracorp.wst.dominio.ventas.dto.CotizacionControles;
import com.altioracorp.wst.util.LocalDateTimeDeserialize;
import com.altioracorp.wst.util.LocalDateTimeSerialize;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

@SuppressWarnings("serial")
@Entity
public class Cotizacion extends EntidadBase {

	private String empresa;
	
	private String numero;
	
	private String codigoCliente;
	
	private String nombreCliente;
	
	@JsonSerialize(using = LocalDateTimeSerialize.class)
	@JsonDeserialize(using = LocalDateTimeDeserialize.class)
	private LocalDateTime fechaEmision;
	
	@Enumerated(EnumType.STRING)
	@NotNull
	private CotizacionEstado estado;
	
	@Enumerated(EnumType.STRING)
	@NotNull
	private TipoPago formaPago;
	
	private String condicionPago;
	
	private BigDecimal totalKilos;
	
	private BigDecimal totalIva;
	
	private BigDecimal totalBaseImponible;
	
	private BigDecimal subtotaNoIVA;
	
	private BigDecimal subtotalIVA;
	
	private BigDecimal total;
	
	private String comentario;
	
	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "punto_venta_id", nullable = false, updatable = false)
	private PuntoVenta puntoVenta;
	
	private Boolean activo;
	
	@JsonSerialize(using = LocalDateTimeSerialize.class)
	@JsonDeserialize(using = LocalDateTimeDeserialize.class)
	private LocalDateTime fechaVencimiento;
	
	private String ordenCliente;
	
	@OneToMany(cascade = {CascadeType.ALL}, fetch = FetchType.LAZY, orphanRemoval = true)
	@JoinColumn(name = "cotizacion_id", nullable = false)
	private List<CotizacionDetalle> detalle = new ArrayList<>();
	
	@OneToOne(cascade = {CascadeType.ALL}, fetch = FetchType.EAGER)
	@JoinColumn(name = "controles_id", referencedColumnName = "id", insertable = true, updatable = true)
	private CotizacionControles controles;

	//	Se obtiene del cliente en GP y de no existir debe ser 0
	private int diaMaximoFacturacion;

	private String codigoDireccion;
	
    private String descripcionDireccion;
    
    @NotNull
    @NotBlank
    private String codigoVendedor;
    
    @Column(columnDefinition = "bit default 0")
    private boolean datoInicial;

	public Cotizacion() {
		this.detalle = new ArrayList<>();
	}
	
	public void agregarDetalle(CotizacionDetalle linea) {
		
		Optional<CotizacionDetalle> lineaEncontrada = this.detalle.stream().filter(x->x.getCodigoArticulo().equals(linea.getCodigoArticulo())).findAny();
		if(lineaEncontrada.isPresent()) {
			lineaEncontrada.get().setCantidad(lineaEncontrada.get().getCantidad().add(linea.getCantidad()));
			lineaEncontrada.get().calcularTotal();
		}else {
			detalle.add(linea);	
		}
		calcularTotales();
	}
	
	public void calcularTotales() {
		subtotaNoIVA = detalle.stream().filter(x->x.getImpuestoValor().compareTo(BigDecimal.ZERO) == 0).map(x->x.getTotal()).reduce(BigDecimal.ZERO, BigDecimal::add);
		subtotalIVA = detalle.stream().filter(x->x.getImpuestoValor().compareTo(BigDecimal.ZERO) != 0).map(x->x.getTotal()).reduce(BigDecimal.ZERO, BigDecimal::add);
		totalKilos = detalle.stream().map(x->x.getPesoArticulo().multiply(x.getCantidad())).reduce(BigDecimal.ZERO, BigDecimal::add);
		calcularIva();
		calcularTotal();
	}
	
	private void calcularIva() {
		totalIva = (subtotalIVA.multiply(new BigDecimal(0.12))).setScale(2, RoundingMode.HALF_UP);
	}
	
	private void calcularTotal() {	
		total = subtotalIVA.add(subtotaNoIVA).add(totalIva);
	}
	
	public void eliminarLineaDetalle(long lineaID) {
		this.detalle.removeIf(x-> x.getId() == lineaID);
		calcularTotales();
	}

	public void agregarLineaDetalle(CotizacionDetalle linea) {
		detalle.add(linea);
	}
	
	public Boolean getActivo() {
		return activo;
	}
	
	public String getCodigoCliente() {
		return codigoCliente;
	}

	public String getCodigoDireccion() {
		return codigoDireccion;
	}

	public String getCodigoVendedor() {
		return codigoVendedor;
	}
	
	public String getComentario() {
		return comentario;
	}
	
	public String getCondicionPago() {
		return condicionPago;
	}

	public CotizacionControles getControles() {
		return controles;
	}

	public String getDescripcionDireccion() {
		return descripcionDireccion;
	}

	public List<CotizacionDetalle> getDetalle() {
		return detalle;
	}

	public int getDiaMaximoFacturacion() {
		return diaMaximoFacturacion;
	}

	public String getEmpresa() {
		return empresa;
	}

	public CotizacionEstado getEstado() {
		return estado;
	}

	public LocalDateTime getFechaEmision() {
		return fechaEmision;
	}

	public LocalDateTime getFechaVencimiento() {
		return fechaVencimiento;
	}

	public TipoPago getFormaPago() {
		return formaPago;
	}

	@JsonIgnore
	public String getFormaPagoCadena() {
		return formaPago.getDescripcion();
	}

	public String getNombreCliente() {
		return nombreCliente;
	}

	public String getNumero() {
		return numero;
	}

	public String getOrdenCliente() {
		return ordenCliente;
	}

	public PuntoVenta getPuntoVenta() {
		return puntoVenta;
	}

	public BigDecimal getTotal() {
		return total;
	}

	public BigDecimal getTotalBaseImponible() {
		return totalBaseImponible;
	}

	public BigDecimal getTotalIva() {
		return totalIva;
	}
	
	public BigDecimal getTotalKilos() {
		return totalKilos;
	}

	public boolean isDatoInicial() {
		return datoInicial;
	}

	public void setActivo(Boolean activo) {
		this.activo = activo;
	}

	public void setCodigoCliente(String codigoCliente) {
		this.codigoCliente = codigoCliente;
	}

	public void setCodigoDireccion(String codigoDireccion) {
		this.codigoDireccion = codigoDireccion;
	}

	public void setCodigoVendedor(String codigoVendedor) {
		this.codigoVendedor = codigoVendedor;
	}

	public void setComentario(String comentario) {
		this.comentario = comentario;
	}

	public void setCondicionPago(String condicionPago) {
		this.condicionPago = condicionPago;
	}

	public void setControles(CotizacionControles controles) {
		this.controles = controles;
	}

	public void setDatoInicial(boolean datoInicial) {
		this.datoInicial = datoInicial;
	}

	public void setDescripcionDireccion(String descripcionDireccion) {
		this.descripcionDireccion = descripcionDireccion;
	}

	public void setDetalle(List<CotizacionDetalle> detalle) {
		this.detalle = detalle;
	}

	public void setDiaMaximoFacturacion(int diaMaximoFacturacion) {
		this.diaMaximoFacturacion = diaMaximoFacturacion;
	}

	public void setEmpresa(String empresa) {
		this.empresa = empresa;
	}

	public void setEstado(CotizacionEstado estado) {
		this.estado = estado;
	}

	public void setFechaEmision(LocalDateTime fechaEmision) {
		this.fechaEmision = fechaEmision;
	}

	public void setFechaVencimiento(LocalDateTime fechaVencimiento) {
		this.fechaVencimiento = fechaVencimiento;
	}

	public void setFormaPago(TipoPago formaPago) {
		this.formaPago = formaPago;
	}

	public void setNombreCliente(String nombreCliente) {
		this.nombreCliente = nombreCliente;
	}

	public void setNumero(String numero) {
		this.numero = numero;
	}

	public void setOrdenCliente(String ordenCliente) {
		this.ordenCliente = ordenCliente;
	}

	public void setPuntoVenta(PuntoVenta puntoVenta) {
		this.puntoVenta = puntoVenta;
	}

	public void setTotal(BigDecimal total) {
		this.total = total;
	}

	public void setTotalBaseImponible(BigDecimal totalBaseImponible) {
		this.totalBaseImponible = totalBaseImponible;
	}

	public void setTotalIva(BigDecimal totalIva) {
		this.totalIva = totalIva;
	}

	public void setTotalKilos(BigDecimal totalKilos) {
		this.totalKilos = totalKilos;
	}

	
	public BigDecimal getSubtotaNoIVA() {
		return subtotaNoIVA;
	}

	public void setSubtotaNoIVA(BigDecimal subtotaNoIVA) {
		this.subtotaNoIVA = subtotaNoIVA;
	}

	public BigDecimal getSubtotalIVA() {
		return subtotalIVA;
	}

	public void setSubtotalIVA(BigDecimal subtotalIVA) {
		this.subtotalIVA = subtotalIVA;
	}
	
	public Optional<CotizacionDetalle> buscarDetallePorId(long id) {
		Optional<CotizacionDetalle> detalleOpt = Optional.empty();
		if(!CollectionUtils.isEmpty(this.detalle)) {
			detalleOpt = this.detalle.stream().filter(d -> d.getId() == id).findFirst();
		}
		return detalleOpt;
	}

	@Override
	public String toString() {
		return "Cotizacion [empresa=" + empresa + ", numero=" + numero + ", codigoCliente=" + codigoCliente
				+ ", nombreCliente=" + nombreCliente + ", fechaEmision=" + fechaEmision + ", estado=" + estado
				+ ", formaPago=" + formaPago + ", condicionPago=" + condicionPago + ", totalKilos=" + totalKilos
				+ ", totalIva=" + totalIva + ", totalBaseImponible=" + totalBaseImponible + ", total=" + total
				+ ", comentario=" + comentario + ", puntoVenta=" + puntoVenta + ", activo=" + activo
				+ ", fechaVencimiento=" + fechaVencimiento + ", detalle=" + detalle + "]";
	}
	
}
