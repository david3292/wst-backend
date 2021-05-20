package com.altioracorp.wst.controlador.ventas;

import com.altioracorp.wst.dominio.ventas.MotivoNotaCredito;
import com.altioracorp.wst.servicio.ventas.IMotivoNotaCreditoServicio;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/motivoNotaCredito")
public class MotivoNotaCreditoControlador {

    @Autowired
    private IMotivoNotaCreditoServicio servicio;

    @GetMapping("/listarTodos")
    public ResponseEntity<List<MotivoNotaCredito>> obtenerTodos() {
        List<MotivoNotaCredito> motivos = servicio.listarTodosActivos();
        return new ResponseEntity<>(motivos, HttpStatus.OK);
    }
}
