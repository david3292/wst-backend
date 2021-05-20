package com.altioracorp.wst.controlador.sistema;

import java.util.ArrayList;
import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.altioracorp.wst.dominio.sistema.ConfigSistema;
import com.altioracorp.wst.dominio.sistema.ConfigSistemaUnidad;
import com.altioracorp.wst.dominio.sistema.ConfiguracionSistema;
import com.altioracorp.wst.servicio.sistema.IConfiguracionSistemaServicio;

@RestController
@RequestMapping("/configuraciones")
public class ConfiguracionSistemaControlador {
	
	@Autowired
	private IConfiguracionSistemaServicio servicio;
	
	@GetMapping
	public ResponseEntity<List<ConfiguracionSistema>> listarConfiguraciones() {
		List<ConfiguracionSistema> lista = servicio.listarTodos();
		return new ResponseEntity<List<ConfiguracionSistema>>(lista, HttpStatus.OK);
	}
	
	@GetMapping("/{id}")
	public ResponseEntity<ConfiguracionSistema> listarPorId(@PathVariable("id") Long id) {
		ConfiguracionSistema configuracion = servicio.listarPorId(id);
		return new ResponseEntity<ConfiguracionSistema>(configuracion, HttpStatus.OK);
	}
	
	@GetMapping("/variacionPrecio")
	public ResponseEntity<ConfiguracionSistema> obtenerConfiguracionPorcentajeVariacionPrecio() {
		ConfiguracionSistema configuracion = servicio.obetenerConfigPorcentajeVariacionPrecio();
		return new ResponseEntity<ConfiguracionSistema>(configuracion, HttpStatus.OK);
	}
	
	@PostMapping
	public ResponseEntity<ConfiguracionSistema> registrar(@Valid @RequestBody ConfiguracionSistema configuracionSistema) {
		ConfiguracionSistema obj = servicio.registrar(configuracionSistema);
		return new ResponseEntity<ConfiguracionSistema>(obj, HttpStatus.CREATED);
	}
	
	@PutMapping
	public ResponseEntity<ConfiguracionSistema> modificar(@Valid @RequestBody ConfiguracionSistema configuracionSistema) {
		ConfiguracionSistema obj = servicio.modificar(configuracionSistema);
		return new ResponseEntity<ConfiguracionSistema>(obj, HttpStatus.OK);
	}
	
	@GetMapping("/porcentajeDescuentoFijo")
	public ResponseEntity<ConfiguracionSistema> obtenerConfiguracionMaximoPorcentajeDescuentoFijo() {
		ConfiguracionSistema configuracion = servicio.obetenerConfigMaximoPorcentajeDescuentoFijo();
		return new ResponseEntity<ConfiguracionSistema>(configuracion, HttpStatus.OK);
	}
	
	@GetMapping("/configuracionesCatalogo")
    public ResponseEntity<List<CatalogoDTO>> listarConfiguracionesCatalogo() {
		ConfigSistema[] opciones = ConfigSistema.values();
		List<CatalogoDTO> catalogo =new ArrayList<>();
		for(ConfigSistema  ae : opciones ) {
			catalogo.add(new CatalogoDTO(ae.getDescripcion(), ae.toString()));
		}
        return ResponseEntity.ok(catalogo);
    }
	
	@GetMapping("/configuracionesUnidadesCatalogo")
    public ResponseEntity<List<CatalogoDTO>> listarConfiguracionesUnidadesCatalogo() {
		ConfigSistemaUnidad[] opciones = ConfigSistemaUnidad.values();
		List<CatalogoDTO> catalogo =new ArrayList<>();
		for(ConfigSistemaUnidad  ae : opciones ) {
			catalogo.add(new CatalogoDTO(ae.getDescripcion(), ae.toString()));
		}
        return ResponseEntity.ok(catalogo);
    }

}
