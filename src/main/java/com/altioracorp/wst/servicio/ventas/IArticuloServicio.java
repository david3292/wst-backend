package com.altioracorp.wst.servicio.ventas;

import java.util.List;
import java.util.Map;

import com.altioracorp.gpintegrator.client.Item.Item;
import com.altioracorp.gpintegrator.client.Item.ItemInventory;
import com.altioracorp.gpintegrator.client.Item.ItemReplenishment;
import com.altioracorp.gpintegrator.client.Item.Iv40201;
import com.altioracorp.gpintegrator.client.Item.IvItem40400;
import com.altioracorp.wst.dominio.sistema.Perfil;
import com.altioracorp.wst.dominio.ventas.ArticuloCompartimientoDTO;
import com.altioracorp.wst.dominio.ventas.CriterioArticuloDTO;
import com.altioracorp.wst.dominio.ventas.DocumentoDetalle;
import com.altioracorp.wst.dominio.ventas.StockArticuloDTO;
import com.altioracorp.wst.dominio.ventas.StockSolicitudDTO;

public interface IArticuloServicio {

	List<Item> obtenerArticulosPorCriterio(CriterioArticuloDTO criterio);
	
	StockArticuloDTO obtenerStockArticuloPorItemnmbr(StockSolicitudDTO dto);
	
	StockArticuloDTO obtenerStockBodegas(StockSolicitudDTO dto);
	
	List<ArticuloCompartimientoDTO> obtenerStockArticuloCompartimiento(StockSolicitudDTO dto);
	
	StockArticuloDTO obtenerStockArticuloPorItemnmbrNoVendedor(StockSolicitudDTO dto);
	
	List<ArticuloCompartimientoDTO> obtenerCompartimientosPorBodega(StockSolicitudDTO dto);
	
	void liberarReservasArticulosYCompartimientos(List<DocumentoDetalle> detalles);
	
	void liberarReservasCompartimientos(DocumentoDetalle detalle);
	
	String obtenerCodigoAlterno();
	
	void deshabilitarCodigoAlterno(String codigoAlterno);
	
	void reservarArticulos(List<DocumentoDetalle> detalle);
	
	void reservarArticuloCompartimientos(DocumentoDetalle detalle);
	
	void liberarReservasArticulos(List<DocumentoDetalle> detalles);
	
	void agregarCompartimientoADetalle (DocumentoDetalle detalle);
	
	List<IvItem40400> obtenerClasesArticuloPorPerfil(Perfil perfil);
	
	List<Iv40201> obtenerUnidadesDeMedida();
	
	ItemInventory obtenerArticuloPorCodigo(String codigoArticulo);
	
	List<String> obtenerCategoriasPorTipo(int tipo);
	
	List<String> obtenerListasDePrecios();
	
	Map<String, Object> crearActualizarArticulo(ItemInventory articulo);
	
	List<Item> obtenerArticulosPorCriterioPerfil(CriterioArticuloDTO criterio);
	
	List<ItemReplenishment> obtenerReposicion(String bodegaOrigen, String bodegaDestino);
	
	Item obtenerReposicionPorArticuloYBodega(String codigoArticulo, String codigoBodega);
	
	void reservarArticulosReposicion(List<DocumentoDetalle> detalles);
	
	void liberarReservasArticulosReposicion(List<DocumentoDetalle> detalles);
	
}
