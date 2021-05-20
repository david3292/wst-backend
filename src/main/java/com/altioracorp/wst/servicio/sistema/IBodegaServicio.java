package com.altioracorp.wst.servicio.sistema;

import java.util.List;

import com.altioracorp.gpintegrator.client.SiteSetup.SiteSetup;
import com.altioracorp.wst.dominio.sistema.Bodega;
import com.altioracorp.wst.servicio.ICRUD;

public interface IBodegaServicio extends ICRUD<Bodega, Long>{

	List<Bodega> listarTodosActivos();
	
	List<SiteSetup> listarBodegasGP();
	
	SiteSetup listarBodegaPorLocnCode(String locnCode);
	
	String obtenerCodigoBodegaPrincipalPorUsuarioIdPerfilIdPuntoVentaId(long usuarioId, long perfilId, long puntoVentaId);
	
	List<Bodega> listarBodegaCentroDistribucion();
	
	List<Bodega> listarBodegaReposicionInventario();
	
}
