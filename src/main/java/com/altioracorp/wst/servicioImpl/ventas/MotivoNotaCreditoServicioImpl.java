package com.altioracorp.wst.servicioImpl.ventas;

import com.altioracorp.wst.dominio.ventas.MotivoNotaCredito;
import com.altioracorp.wst.repositorio.ventas.IMotivoNotaCreditoRepositorio;
import com.altioracorp.wst.servicio.ventas.IMotivoNotaCreditoServicio;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MotivoNotaCreditoServicioImpl implements IMotivoNotaCreditoServicio {

    @Autowired
    private IMotivoNotaCreditoRepositorio motivoNotaCreditoRepositorio;

    @Override
    public List<MotivoNotaCredito> listarTodosActivos() {
        return this.motivoNotaCreditoRepositorio.findByActivoTrue();
    }
}
