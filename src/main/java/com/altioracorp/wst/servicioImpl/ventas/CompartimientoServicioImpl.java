package com.altioracorp.wst.servicioImpl.ventas;

import java.math.BigDecimal;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.altioracorp.wst.constantes.integracion.UrlIntegracionItem;
import com.altioracorp.wst.dominio.ventas.ArticuloCompartimientoDTO;
import com.altioracorp.wst.dominio.ventas.StockSolicitudDTO;
import com.altioracorp.wst.servicio.sistema.IConstantesAmbienteWstServicio;
import com.altioracorp.wst.servicio.ventas.IArticuloServicio;
import com.altioracorp.wst.servicio.ventas.ICompartimientoServicio;

@Service
public class CompartimientoServicioImpl implements ICompartimientoServicio{
	
	private static final Log LOG = LogFactory.getLog(FacturaServicioImpl.class);
	
	@Autowired
	private IConstantesAmbienteWstServicio ambienteServicio;
	
	@Autowired
	private IArticuloServicio articuloservicio;
	
	@Autowired
	private RestTemplate restTemplate;
	
	@Override
	public String ObtenerCompartimientoPorCodigoArticuloYBodega(String codigoArticulo, String codigoBodega) {
		String compartimiento = StringUtils.EMPTY;
		
		if(StringUtils.isBlank(compartimiento)) compartimiento = obtenerCompartimientoMayorCantidadPorCodigoArticuloYBodega(codigoArticulo, codigoBodega);
		if(StringUtils.isBlank(compartimiento)) compartimiento = obtenerCompartimientoConUltimoMovimientoPorCodigoArticuloYBodega(codigoArticulo, codigoBodega);
		if(StringUtils.isBlank(compartimiento)) compartimiento = obtenerCompartimientoPredeterminado();
		
		return compartimiento;
	}
	
	@Override
	public String obtenerCompartimientoMayorCantidadPorCodigoArticuloYBodega(String codigoArticulo, String codigoBodega) {
		String nombreCompartimiento = StringUtils.EMPTY;
		StockSolicitudDTO stockSolicitado = new StockSolicitudDTO(); 
		
		stockSolicitado.setItemnmbr(codigoArticulo);
		stockSolicitado.setBodega(codigoBodega);
		
		List<ArticuloCompartimientoDTO> compartimientos = articuloservicio.obtenerStockArticuloCompartimiento(stockSolicitado);
		if(!CollectionUtils.isEmpty(compartimientos)) {
			compartimientos.removeIf(c -> c.getCantidadExistente().compareTo(BigDecimal.ZERO) == 0);
			compartimientos.removeIf(f -> f.getCompartimiento().trim().equals("GENERAL"));
			if(!CollectionUtils.isEmpty(compartimientos)) {
				Optional<ArticuloCompartimientoDTO> compartimiento = compartimientos.stream()
						.max(Comparator.comparing(ArticuloCompartimientoDTO::getCantidadExistente));
				if(compartimiento.isPresent());
					nombreCompartimiento = compartimiento.get().getCompartimiento();
			}
			
		}
		return nombreCompartimiento;
	}

	@Override
	public String obtenerCompartimientoConUltimoMovimientoPorCodigoArticuloYBodega(String codigoArticulo, String codigoBodega) {
		
		String compartimiento = StringUtils.EMPTY;
		final String url =  ambienteServicio.getUrlIntegradorGp().concat(UrlIntegracionItem.OBTENER_BIN_ULTIMO_MOVIMIENTO.getUrl())
				.replace("${codigoArticulo}", codigoArticulo).replace("${codigoBodega}", codigoBodega);
		
		try {
			
			compartimiento = restTemplate.getForObject(url, String.class);
			if(StringUtils.isNotBlank(compartimiento))
				compartimiento = compartimiento.replace("\"", "");
			
		} catch (Exception e) {
			compartimiento = StringUtils.EMPTY;
			LOG.error(String.format("Error al obtener compartimiento para el codigo: %s, bodega: %s", codigoArticulo, codigoBodega));
		}
				
		return compartimiento;
	}

	@Override
	public String obtenerCompartimientoPredeterminado() {
		return ambienteServicio.getCompartimientoGeneral();
	}
	
}
