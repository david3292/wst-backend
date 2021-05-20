package com.altioracorp.wst.controlador.ventas;

import java.util.List;
import java.util.Map;

import javax.validation.Valid;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.altioracorp.gpintegrator.client.Item.Item;
import com.altioracorp.gpintegrator.client.Item.ItemInventory;
import com.altioracorp.gpintegrator.client.Item.Iv40201;
import com.altioracorp.gpintegrator.client.Item.IvItem40400;
import com.altioracorp.wst.dominio.sistema.Perfil;
import com.altioracorp.wst.dominio.ventas.ArticuloCompartimientoDTO;
import com.altioracorp.wst.dominio.ventas.CriterioArticuloDTO;
import com.altioracorp.wst.dominio.ventas.StockArticuloDTO;
import com.altioracorp.wst.dominio.ventas.StockSolicitudDTO;
import com.altioracorp.wst.servicio.ventas.IArticuloServicio;

@RestController
@RequestMapping("/articulos")
public class ArticuloControlador {

	@Autowired
	private IArticuloServicio servicio;

	@PostMapping("/listarPorCriterio")
	public ResponseEntity<List<Item>> listarArticulosPorCriterio(@RequestBody CriterioArticuloDTO criterio) {
		List<Item> lista = servicio.obtenerArticulosPorCriterio(criterio);
		return new ResponseEntity<List<Item>>(lista, HttpStatus.OK);
	}
	
	@PostMapping("/listarPorCriterioPerfil")
	public ResponseEntity<List<Item>> listarArticulosPorCriterioPerfil(@RequestBody CriterioArticuloDTO criterio) {
		List<Item> lista = servicio.obtenerArticulosPorCriterioPerfil(criterio);
		return new ResponseEntity<List<Item>>(lista, HttpStatus.OK);
	}

	@PostMapping("/stockPorCodigo")
	public ResponseEntity<StockArticuloDTO> obtenerStockArticuloPorItemnmbr(
			@Valid @RequestBody StockSolicitudDTO solicitudDTO) {
		StockArticuloDTO dto = servicio.obtenerStockArticuloPorItemnmbr(solicitudDTO);
		return new ResponseEntity<StockArticuloDTO>(dto, HttpStatus.OK);
	}
	
	@PostMapping("/stockPorCodigoNoVendedor")
	public ResponseEntity<StockArticuloDTO> obtenerStockArticuloPorItemnmbrNoVendedor(
			@Valid @RequestBody StockSolicitudDTO solicitudDTO) {
		StockArticuloDTO dto = servicio.obtenerStockArticuloPorItemnmbrNoVendedor(solicitudDTO);
		return new ResponseEntity<StockArticuloDTO>(dto, HttpStatus.OK);
	}

	@PostMapping("/stockBodegas")
	public ResponseEntity<StockArticuloDTO> obtenerStock(@RequestBody StockSolicitudDTO dto) {
		StockArticuloDTO obj = servicio.obtenerStockBodegas(dto);
		return new ResponseEntity<StockArticuloDTO>(obj, HttpStatus.CREATED);
	}
	
	@PostMapping("/compartimientos/stock")
	public ResponseEntity<List<ArticuloCompartimientoDTO>> obtenerStockArticuloCompartimiento(
			@Valid @RequestBody StockSolicitudDTO solicitudDTO) {
		List<ArticuloCompartimientoDTO> dto = servicio.obtenerStockArticuloCompartimiento(solicitudDTO);
		return new ResponseEntity<List<ArticuloCompartimientoDTO>>(dto, HttpStatus.OK);
	}

	@GetMapping("/codigoAlterno")
	public ResponseEntity<String> obtenerCodigoAlternoAleatorio(){
		String codigoAlterno = servicio.obtenerCodigoAlterno();
		if(StringUtils.isNotBlank(codigoAlterno))
			servicio.deshabilitarCodigoAlterno(codigoAlterno);		
		return ResponseEntity.ok(codigoAlterno);
	}
	
	@GetMapping("/byCodigoArticulo/{codigoArticulo}")
	public ResponseEntity<ItemInventory> obtenerArticuloPorCodigo(@PathVariable("codigoArticulo")String codigoArticulo){
		ItemInventory articulo = servicio.obtenerArticuloPorCodigo(codigoArticulo);
		return ResponseEntity.ok(articulo);
	}
	
	@GetMapping("/obtenerCategoriasPorTipo/{tipo}")
	public ResponseEntity<List<String>> obtenerCategoriasPorTipo(@PathVariable("tipo") int tipo){
		List<String> categorias = servicio.obtenerCategoriasPorTipo(tipo);
		return ResponseEntity.ok(categorias);
	}
	
	@GetMapping("/unidadesMedida")
	public ResponseEntity<List<Iv40201>> obtenerUnidadesMedida(){
		List<Iv40201> unidadesMedida = servicio.obtenerUnidadesDeMedida();
		return ResponseEntity.ok(unidadesMedida);
	}
	
	@PostMapping("/clasesPorPerfil")
	public ResponseEntity<List<IvItem40400>> obtenerClasesPorPeril(@RequestBody Perfil perfil){
		List<IvItem40400> clases = servicio.obtenerClasesArticuloPorPerfil(perfil);
		return ResponseEntity.ok(clases);
	}
	
	@GetMapping("/marcasArticulo")
	public ResponseEntity<List<String>> obtenerMarcasArticulo(){
		List<String> marcas = servicio.obtenerCategoriasPorTipo(3);
		return ResponseEntity.ok(marcas);
	}
	
	@GetMapping("/listaPrecios")
	public ResponseEntity<List<String>> obtenerListaPrecios(){
		List<String> listaPrecios = servicio.obtenerListasDePrecios();
		return ResponseEntity.ok(listaPrecios);
	}
	
	@PostMapping("/crearActualizar")
	public Map<String, Object> crearActualizarArticulo(@RequestBody ItemInventory articulo){
		Map<String, Object> resumenIntegracion = servicio.crearActualizarArticulo(articulo);
		return resumenIntegracion;
	}
}
