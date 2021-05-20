package com.altioracorp.wst.servicio.sistema;

import java.util.Optional;

import com.altioracorp.wst.dominio.sistema.ConfigSistema;
import com.altioracorp.wst.dominio.sistema.ConfiguracionSistema;
import com.altioracorp.wst.servicio.ICRUD;

public interface IConfiguracionSistemaServicio extends ICRUD<ConfiguracionSistema, Long> {
		
	ConfiguracionSistema obetenerConfigPorcentajeVariacionPrecio();
	
	ConfiguracionSistema obetenerConfigMaximoPorcentajeDescuentoFijo();
	
	Optional<ConfiguracionSistema> obetenerConfiguracionPorNombre(ConfigSistema nombreConfiguracion);
}
