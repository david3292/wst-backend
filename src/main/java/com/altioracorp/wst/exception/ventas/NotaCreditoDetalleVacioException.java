package com.altioracorp.wst.exception.ventas;

@SuppressWarnings("serial")
public class NotaCreditoDetalleVacioException extends VentasException {

    @Override
    public String getMessage() {
        return String.format("Error al finalizar Nota Crédito, no existe detalle ");
    }
}
