package com.altioracorp.wst.xml.factura;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Factura {

	protected InfoTributaria infoTributaria;
	
	protected InfoFactura infoFactura;
	
	protected List<Detalle> detalles;
	
	protected List<CampoAdicional> infoAdicional;
	
	protected String numeroAutorizacion;
	
	private String observacion;

	public InfoTributaria getInfoTributaria() {
		return infoTributaria;
	}

	public void setInfoTributaria(InfoTributaria infoTributaria) {
		this.infoTributaria = infoTributaria;
	}

	public InfoFactura getInfoFactura() {
		return infoFactura;
	}

	public void setInfoFactura(InfoFactura infoFactura) {
		this.infoFactura = infoFactura;
	}

	public List<Detalle> getDetalles() {
		return detalles;
	}

	public void setDetalles(List<Detalle> detalles) {
		this.detalles = detalles;
	}

	public List<CampoAdicional> getInfoAdicional() {
		return infoAdicional;
	}

	public void setInfoAdicional(List<CampoAdicional> infoAdicional) {
		this.infoAdicional = infoAdicional;
	}
	
	public String getNumeroAutorizacion() {
		return numeroAutorizacion;
	}

	public void setNumeroAutorizacion(String numeroAutorizacion) {
		this.numeroAutorizacion = numeroAutorizacion;
	}

	public BigDecimal getAumentadoSubtotal12() {
		return baseImponible(CodigoImpuesto.IVA, TarifaIVA.DOCE_PORCIENTO, TarifaIVA.CATORCE_PORCIENTO);
	}
	
	private BigDecimal baseImponible(CodigoImpuesto codigoImpuesto, TarifaIVA... tarifasIVA) {
		
		for (TarifaIVA tarifaIVA : tarifasIVA) {
			Impuesto totalImpuesto = elemento(codigoImpuesto, tarifaIVA);
			if (totalImpuesto != null) {
				return totalImpuesto.getBaseImponible();
			}
		}
		
		return BigDecimal.ZERO; 
	}
	
	private Impuesto elemento(CodigoImpuesto codigoImpuesto, TarifaIVA tarifaIVA) {
		
		if (getInfoFactura() != null) {
			
			List<Impuesto> listaTotalImpuesto = getInfoFactura().getTotalConImpuestos();
			
			if (listaTotalImpuesto != null) {
				
				if (listaTotalImpuesto != null) {
					for (Impuesto totalImpuesto : listaTotalImpuesto) {
						if ((totalImpuesto.getCodigo().equals(codigoImpuesto.getNumero())
								&& (totalImpuesto.getCodigoPorcentaje().equals(tarifaIVA.getNumero())))) {
							
							return totalImpuesto;
						}
					}					
				}
			}
		}
		
		return null;
	}
	
	public BigDecimal getAumentadoTipoExportacionParaSubtotal0() {		
		return BigDecimal.ZERO;
	}
	
	public BigDecimal getAumentadoSubtotal0() {
		return baseImponible(CodigoImpuesto.IVA, TarifaIVA.CERO_PORCIENTO);
	}
	
	public BigDecimal getAumentadoSubtotalNoObjetoIVA() {
		return baseImponible(CodigoImpuesto.IVA, TarifaIVA.NO_OBJETO_IMPUESTO);
	}
	
	public BigDecimal getAumentadoSubtotalExentoIVA() {
		return baseImponible(CodigoImpuesto.IVA, TarifaIVA.EXENTO_IVA);
	}
	
	public BigDecimal getAumentadoSubTotalSinImpuestos() {				
		return getInfoFactura().getTotalSinImpuestos();
	}
	
	public BigDecimal getAumentadoIVA12() {
		return valor(CodigoImpuesto.IVA, TarifaIVA.DOCE_PORCIENTO, TarifaIVA.CATORCE_PORCIENTO);
	}
	
	private BigDecimal valor(CodigoImpuesto codigoImpuesto, TarifaIVA... tarifasIVA) {
		
		for (TarifaIVA tarifaIVA : tarifasIVA) {
			Impuesto totalImpuesto = elemento(codigoImpuesto, tarifaIVA);
			if (totalImpuesto != null) {
				return totalImpuesto.getValor(); 			
			}
		}
		
		return BigDecimal.ZERO;
	}
	
	public List<ComprobanteAumentadoFormaPago> getAumentadoFormasPago() {
		
		final List<ComprobanteAumentadoFormaPago> formasPago = new ArrayList<>();
		
		final List<Pago> pagoLista = getInfoFactura().getPagos();
		
		if (pagoLista != null) {
			
			if ((pagoLista != null) && (!pagoLista.isEmpty())) {
				for (Pago pago : pagoLista) {
					formasPago.add(new ComprobanteAumentadoFormaPago(
							FormaPagoXML.paraCodigo(pago.getFormaPago()), 
							pago.getTotal()));
				}
			}
		}
		
		return formasPago;
	}
	
	public String getAumentadoIVAPorcentaje() {
		
		for (TarifaIVA tarifaIVA : Arrays.asList(TarifaIVA.CATORCE_PORCIENTO, TarifaIVA.DOCE_PORCIENTO)) {
			
			final Impuesto totalImpuesto = elemento(CodigoImpuesto.IVA, tarifaIVA);
			
			if (totalImpuesto != null) {
				return tarifaIVA.getPorcentaje();
			}
		}
		
		return "_";
	}
	
	public String getInfoAdicionalCoutas() {
		return valorCampoAdicional("CUOTAS");
	}
	
	public String getInfoAdicionalCuentaBancaria() {
		return valorCampoAdicional("CUENTABANCARIA");
	}
	
	public String getInfoAdicionalDireccion() {
		return valorCampoAdicional("INFO06");
	}
	
	public String getInfoAdicionalObservacion() {
		return valorCampoAdicional("OBSERVACION");
	}
	
	public String getInfoAdicionalTelefono() {
		return valorCampoAdicional("INFO05");
	}
	
	public String getInfoAdicionalTelefonoEmpresa() {
		return valorCampoAdicional("INFO04");
	}
	
	public String getInfoAdicionalTerminoPago() {
		return valorCampoAdicional("INFO02");
	}
	
	public String getInfoAdicionalVendedor() {
		return valorCampoAdicional("INFO01");
	}
	
	public String getInfoAdicionalKGR() {
		return valorCampoAdicional("INFO08");
	}
	
	public String getInfoAdicionalOrdenCompra() {
		return valorCampoAdicional("INFO07");
	}
	
	public String getInfoAdicionalTerminosPago() {
		return valorCampoAdicional("INFO02");
	}
	
	public String getInfoAdicionalCotizacion() {
		return valorCampoAdicional("INFO03");
	}
	
	public String getInfoAdicionalDespacho() {
		return valorCampoAdicional("INFO09");
	}
	
	public String getInfoAdicionalEmail() {
		return valorCampoAdicional("EMAIL");
	}
	
	protected final String valorCampoAdicional(String nombreCampoAdicional) {
		
		List<CampoAdicional> camposAdicionales = getInfoAdicional();
		
		if (camposAdicionales != null) {
			if (camposAdicionales != null) {
				for (CampoAdicional campoAdicional : camposAdicionales) {				
					if (campoAdicional.getNombre().equals(nombreCampoAdicional)) {
						return campoAdicional.getValue();
					}
				}
			}
		}
		
		// Se devuelve null en lugar de la cadena vacía, 
		// porque la cadena vacía puede ser un valor válido o esperado.
		return null;
	}
	
	public boolean isInfoAdicionalFacturaNegociable() {
		
		final String valor = valorCampoAdicional("TIPOFACTURA");	
		
		if (valor != null && !valor.isEmpty()) {
			
			return valor.trim().equals("NEGOCIABLE") ? true : false;
		}		
		return false;
	}

	public String getObservacion() {
		return observacion;
	}

	public void setObservacion(String observacion) {
		this.observacion = observacion;
	}
}
