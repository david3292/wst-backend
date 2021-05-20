package com.altioracorp.wst.xml.factura;

import java.math.BigDecimal;
import java.util.List;

import org.json.JSONObject;

import com.altioracorp.wst.util.JsonUtilities;
import com.fasterxml.jackson.core.JsonProcessingException;

public class Detalle {

	protected String codigoPrincipal;
    protected String codigoAuxiliar;
    protected String descripcion;
    protected String unidadMedida;
    
    protected BigDecimal cantidad;
    
    protected BigDecimal precioUnitario;
    protected BigDecimal precioSinSubsidio;
    
    protected BigDecimal descuento;
    
    protected BigDecimal precioTotalSinImpuesto;
    protected List<DetAdicional> detallesAdicionales;
    
    protected List<Impuesto> impuestos;
    
    public Detalle(final JSONObject jsonObject) {
        this.cantidad = JsonUtilities.getBigDecimalFromJson(jsonObject, "cantidad");
        this.codigoAuxiliar = JsonUtilities.getStringFromJson(jsonObject, "codigoAuxiliar");
        this.codigoPrincipal = JsonUtilities.getStringFromJson(jsonObject, "codigoPrincipal");
        this.descripcion = JsonUtilities.getStringFromJson(jsonObject, "descripcion");
        this.descuento = JsonUtilities.getBigDecimalFromJson(jsonObject, "descuento");
        this.unidadMedida = JsonUtilities.getStringFromJson(jsonObject, "unidadMedida");
        this.precioTotalSinImpuesto = JsonUtilities.getBigDecimalFromJson(jsonObject, "precioTotalSinImpuesto");
        this.precioUnitario = JsonUtilities.getBigDecimalFromJson(jsonObject, "precioUnitario");

        final JSONObject impuestoJson = JsonUtilities.getObjectFromJson(jsonObject, "impuestos");
        try {
            this.impuestos = Converter.getDetalleImpuestosFromJson(impuestoJson);

        } catch (final JsonProcessingException e) {
            e.printStackTrace();
        }

        final JSONObject detAdicionalesJson = JsonUtilities.getObjectFromJson(jsonObject, "detallesAdicionales");
        this.detallesAdicionales = Converter.getInfoAdicionalDetalleFromJson(detAdicionalesJson);
    }

	public String getCodigoPrincipal() {
		return codigoPrincipal;
	}

	public void setCodigoPrincipal(String codigoPrincipal) {
		this.codigoPrincipal = codigoPrincipal;
	}

	public String getCodigoAuxiliar() {
		return codigoAuxiliar;
	}

	public void setCodigoAuxiliar(String codigoAuxiliar) {
		this.codigoAuxiliar = codigoAuxiliar;
	}

	public String getDescripcion() {
		return descripcion;
	}

	public void setDescripcion(String descripcion) {
		this.descripcion = descripcion;
	}

	public String getUnidadMedida() {
		return unidadMedida;
	}

	public void setUnidadMedida(String unidadMedida) {
		this.unidadMedida = unidadMedida;
	}

	public BigDecimal getCantidad() {
		return cantidad;
	}

	public void setCantidad(BigDecimal cantidad) {
		this.cantidad = cantidad;
	}

	public BigDecimal getPrecioUnitario() {
		return precioUnitario;
	}

	public void setPrecioUnitario(BigDecimal precioUnitario) {
		this.precioUnitario = precioUnitario;
	}

	public BigDecimal getPrecioSinSubsidio() {
		return precioSinSubsidio;
	}

	public void setPrecioSinSubsidio(BigDecimal precioSinSubsidio) {
		this.precioSinSubsidio = precioSinSubsidio;
	}

	public BigDecimal getDescuento() {
		return descuento;
	}

	public void setDescuento(BigDecimal descuento) {
		this.descuento = descuento;
	}

	public BigDecimal getPrecioTotalSinImpuesto() {
		return precioTotalSinImpuesto;
	}

	public void setPrecioTotalSinImpuesto(BigDecimal precioTotalSinImpuesto) {
		this.precioTotalSinImpuesto = precioTotalSinImpuesto;
	}

	public List<DetAdicional> getDetallesAdicionales() {
		return detallesAdicionales;
	}

	public void setDetallesAdicionales(List<DetAdicional> detallesAdicionales) {
		this.detallesAdicionales = detallesAdicionales;
	}

	public List<Impuesto> getImpuestos() {
		return impuestos;
	}

	public void setImpuestos(List<Impuesto> impuestos) {
		this.impuestos = impuestos;
	}

	public String getDetAdicionalCadena1() {
		if(detallesAdicionales != null) {
			if(detallesAdicionales.size()>0) {
				return String.format("%s %s", detallesAdicionales.get(0).getValor(), "%");
			}
		}
		
		return null;
	}
    
}
