package com.altioracorp.wst.servicio.ventas;

import com.altioracorp.wst.dominio.ventas.MotivoNotaCredito;

import java.util.List;

public interface IMotivoNotaCreditoServicio {

    List<MotivoNotaCredito> listarTodosActivos();
}
