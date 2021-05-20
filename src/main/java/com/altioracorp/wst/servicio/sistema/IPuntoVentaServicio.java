package com.altioracorp.wst.servicio.sistema;

import com.altioracorp.wst.dominio.sistema.PuntoVenta;
import com.altioracorp.wst.servicio.ICRUD;

import java.util.List;

public interface IPuntoVentaServicio extends ICRUD<PuntoVenta, Long> {

	List<PuntoVenta> listarTodosActivos();

	PuntoVenta buscarPorNombre(String nombre);

	List<PuntoVenta> buscarPorPerfilIdYUsuarioSesion(Long idPerfil);
}
