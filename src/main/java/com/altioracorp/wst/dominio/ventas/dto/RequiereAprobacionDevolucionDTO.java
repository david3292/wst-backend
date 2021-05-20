package com.altioracorp.wst.dominio.ventas.dto;

import com.altioracorp.wst.dominio.ventas.DocumentoDetalle;
import com.altioracorp.wst.dominio.ventas.TipoDevolucion;

import java.io.Serializable;
import java.util.List;

/**
 * @author dalpala
 *
 */
@SuppressWarnings("serial")
public class RequiereAprobacionDevolucionDTO implements Serializable {

    private List<DocumentoDetalle> detalles;
    private long facturaId;
    private TipoDevolucion tipoDevolucion;

    public RequiereAprobacionDevolucionDTO() {

    }

    public List<DocumentoDetalle> getDetalles() {
        return detalles;
    }

    public void setDetalles(List<DocumentoDetalle> detalles) {
        this.detalles = detalles;
    }

    public long getFacturaId() {
        return facturaId;
    }

    public void setFacturaId(long facturaId) {
        this.facturaId = facturaId;
    }

	public TipoDevolucion getTipoDevolucion() {
		return tipoDevolucion;
	}

	public void setTipoDevolucion(TipoDevolucion tipoDevolucion) {
		this.tipoDevolucion = tipoDevolucion;
	}
}
