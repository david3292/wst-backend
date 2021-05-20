package com.altioracorp.wst.servicio.sistema;

import static com.altioracorp.wst.util.UtilidadesConfiguracion.verificarValor;

import javax.annotation.PostConstruct;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;


@Component
@ConfigurationProperties(prefix = "integrador")
public class ConfiguracionIntegradorGP {
	
private static final Log LOG = LogFactory.getLog(ConfiguracionIntegradorGP.class);
	
	private String url;
	
	private String nombreBaseDatosGP;
	
	private String bodegaTemporalLocncode;
	
	private String bodegaReposicionLocncode;
	
	private String bodegaTemporalBin;
	
	private String binRecepcionOrdenCompran;

	private String compartimientoGeneral;
	
	private String ambienteSri;
	
	@PostConstruct
	public void asegurarValores() {
		verificarValor("URL API", url, LOG, true);
		verificarValor("Nombre BaseDatos GP", nombreBaseDatosGP, LOG, true);
		verificarValor("Bodega temporal GP", bodegaTemporalLocncode, LOG, true);
		verificarValor("Bodega reposicion GP", bodegaReposicionLocncode, LOG, true);
		verificarValor("Bin temporal GP", bodegaTemporalBin, LOG, true);
		verificarValor("Bin recepcion Orden Compra GP", binRecepcionOrdenCompran, LOG, true);
		verificarValor("Compartimiento General", compartimientoGeneral, LOG, true);
		verificarValor("Ambiente SRI", ambienteSri, LOG, true);
	}

	public String getUrl() {
		return url;
	}

	public String getNombreBaseDatosGP() {
		return nombreBaseDatosGP;
	}

	public void setNombreBaseDatosGP(String nombreBaseDatosGP) {
		this.nombreBaseDatosGP = nombreBaseDatosGP;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public String getBodegaTemporalLocncode() {
		return bodegaTemporalLocncode;
	}

	public void setBodegaTemporalLocncode(String bodegaTemporalLocncode) {
		this.bodegaTemporalLocncode = bodegaTemporalLocncode;
	}

	public String getBodegaTemporalBin() {
		return bodegaTemporalBin;
	}

	public void setBodegaTemporalBin(String bodegaTemporalBin) {
		this.bodegaTemporalBin = bodegaTemporalBin;
	}

	public String getBinRecepcionOrdenCompran() {
		return binRecepcionOrdenCompran;
	}

	public void setBinRecepcionOrdenCompran(String binRecepcionOrdenCompran) {
		this.binRecepcionOrdenCompran = binRecepcionOrdenCompran;
	}

	public String getCompartimientoGeneral() {
		return compartimientoGeneral;
	}

	public void setCompartimientoGeneral(String compartimientoGeneral) {
		this.compartimientoGeneral = compartimientoGeneral;
	}
	
	public String getAmbienteSri() {
		return ambienteSri;
	}

	public void setAmbienteSri(String ambienteSri) {
		this.ambienteSri = ambienteSri;
	}

	public String getBodegaReposicionLocncode() {
		return bodegaReposicionLocncode;
	}

	public void setBodegaReposicionLocncode(String bodegaReposicionLocncode) {
		this.bodegaReposicionLocncode = bodegaReposicionLocncode;
	}

}
