package com.altioracorp.wst.exception.ventas;

@SuppressWarnings("serial")
public class NotaCreditoNoEncontradaException extends VentasException {

    @Override
    public String getMessage() {
        return String.format("Error Nota Crédito no existe ");
    }
}
