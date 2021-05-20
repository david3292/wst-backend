package com.altioracorp.wst.servicio.sistema;

import static com.altioracorp.wst.util.UtilidadesConfiguracion.verificarValor;

import javax.annotation.PostConstruct;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix = "sistema")
public class ConfiguracionWST {

	private static final Log LOG = LogFactory.getLog(ConfiguracionWST.class);
	
	private String ambiente;
	
	@PostConstruct
	public void asegurarValores() {
		verificarValor("Sistema WST Ambiente", ambiente, LOG, true);
	}

	public String getAmbiente() {
		return ambiente;
	}

	public void setAmbiente(String ambiente) {
		this.ambiente = ambiente;
	}
	
	
}
