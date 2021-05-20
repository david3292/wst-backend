package com.altioracorp.wst.servicio.ventas;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.client.HttpStatusCodeException;
import org.springframework.web.client.RestTemplate;

import com.altioracorp.gpintegrator.client.inventory.IvResponse;
import com.altioracorp.gpintegrator.client.inventory.IvTransferHeaderInsert;
import com.altioracorp.gpintegrator.client.inventory.IvTransferIntegrator;
import com.altioracorp.gpintegrator.client.inventory.IvTransferLineInsertItems;
import com.altioracorp.gpintegrator.client.inventory.IvTransferMultiBinInsertItems;
import com.altioracorp.wst.constantes.integracion.UrlIntegracionInventory;
import com.altioracorp.wst.dominio.ventas.DocumentoBase;
import com.altioracorp.wst.dominio.ventas.DocumentoDetalle;
import com.altioracorp.wst.dominio.ventas.DocumentoDetalleCompartimiento;
import com.altioracorp.wst.dominio.ventas.DocumentoEstado;
import com.altioracorp.wst.dominio.ventas.DocumentoTransferencia;
import com.altioracorp.wst.dominio.ventas.DocumentoTransferenciaEntrada;
import com.altioracorp.wst.dominio.ventas.DocumentoTransferenciaSalida;
import com.altioracorp.wst.dominio.ventas.TipoTransferencia;
import com.altioracorp.wst.repositorio.ventas.IDocumentoTransferenciaEntradaRepositorio;
import com.altioracorp.wst.repositorio.ventas.IDocumentoTransferenciaRepositorio;
import com.altioracorp.wst.repositorio.ventas.IDocumentoTransferenciaSalidaRepositorio;
import com.altioracorp.wst.servicio.sistema.IConstantesAmbienteWstServicio;
import com.altioracorp.wst.servicioImpl.ventas.TransferenciaServicioImpl;
import com.altioracorp.wst.util.UtilidadaesFuncionales;
import com.google.gson.Gson;

public abstract class TransferenciaComunes {
	
	protected static final Log LOG = LogFactory.getLog(TransferenciaServicioImpl.class);

	@Autowired
	protected RestTemplate restTemplate;
	
	@Autowired
	protected IConstantesAmbienteWstServicio constantesAmbienteServicio;
	
	@Autowired
	protected IDocumentoTransferenciaRepositorio transferenciaRepositorio;
	
	@Autowired
	protected IDocumentoTransferenciaEntradaRepositorio transferenciaEntradaRepositorio;
	
	@Autowired
	protected IDocumentoTransferenciaSalidaRepositorio transferenciaSalidaRepositorio;
	
	@Autowired
	protected Gson gsonLog;
	
	
	protected String obtenerNumeroDocumentoTransferencia(DocumentoBase documento) {
		String numeroDocumento = StringUtils.EMPTY;		
		final String url = constantesAmbienteServicio.getUrlIntegradorGp().concat(UrlIntegracionInventory.OBTENER_NUMERO_GP.getUrl().replace("${ivDocType}", String.valueOf(documento.getTipo().getCodigo())));
		try {
			numeroDocumento = restTemplate.getForObject(url, String.class);	
			numeroDocumento = numeroDocumento.replace("\"", StringUtils.EMPTY);
		} catch (Exception e) {
			LOG.error(String.format("Error al obtener el numero de documento: error: %s", e.getMessage()));
		}
		return numeroDocumento;
	}
	
	protected IvTransferIntegrator obtenerDatosParaIntegracionTransferencia(DocumentoBase transferencia) {
		LOG.info(String.format("Se prepara la informacion para integrar la transferencia con id %d secuencial %s", transferencia.getId(), transferencia.getNumero()));
		Boolean esSalida = true;
		DocumentoTransferenciaEntrada transferenciaEntrada = null;
		DocumentoTransferenciaSalida transferenciasaliSalida = null;
		if(transferencia instanceof DocumentoTransferenciaEntrada) {
			esSalida =false;
			transferenciaEntrada = (DocumentoTransferenciaEntrada) transferencia;
		}else
			transferenciasaliSalida = (DocumentoTransferenciaSalida) transferencia;
		
		final boolean esSalidaFinal = Boolean.valueOf(esSalida);
		
		LocalDate fechaTransferencia = LocalDate.now();
		final String numeroDocumento = transferencia.getNumero();
//		String bodegaOrigen = esSalidaFinal ? transferenciasaliSalida.getBodegaOrigen() : constantesAmbienteServicio.getBodegaTemporalLocnCode();
//		String bodegaDestino = esSalidaFinal ? constantesAmbienteServicio.getBodegaTemporalLocnCode() : transferenciaEntrada.getBodegaDestino();
		
		String bodegaOrigen = obtenerBodegaOrigen(transferencia);
		String bodegaDestino = obtenerBodegaDestino(transferencia);
		
		String bachNumb = getBatchNumberForTransferencia(transferencia);		
		String binTemporal = constantesAmbienteServicio.getBodegaTemporalBin();
		
		IvTransferIntegrator inventoryIntegrator = new IvTransferIntegrator();
		//cabecera
		IvTransferHeaderInsert header = new IvTransferHeaderInsert();
		header.setIvDocNbr(numeroDocumento);
		header.setBachNumb(bachNumb);
		header.setDocDate(fechaTransferencia.toString());
		header.setUsrDefnd1(esSalidaFinal ? transferenciasaliSalida.getNumero() : transferenciaEntrada.getNumero());
		inventoryIntegrator.setIvHeader(header);
		
		//lineas
		inventoryIntegrator.setIvLines(transferencia.getDetalle().stream().map(detalle -> {			
			IvTransferLineInsertItems line = new IvTransferLineInsertItems();			
			line.setIvDocNbr(numeroDocumento);
			line.setItemNmbr(detalle.getCodigoArticulo());
			line.setTrxQty(detalle.getCantidad().doubleValue());
			line.setTrfQtyTy(1);
			line.setTrtQtyTy(1);
			line.setTrxLoctn(bodegaOrigen);
			line.setTrnstLoc(bodegaDestino);
			return line;
		}).collect(Collectors.toList()));
		
		//compartimientos
		AtomicInteger secuencia = new AtomicInteger(16384);
		inventoryIntegrator.setIvBins(transferencia.getDetalle().stream()
			.map(detalle -> {
				return detalle.getCompartimientos().stream().map(compartimiento -> {
					IvTransferMultiBinInsertItems bin = new IvTransferMultiBinInsertItems();
					bin.setIvDocNbr(numeroDocumento);
					bin.setItemNmbr(detalle.getCodigoArticulo());
					bin.setBin( esSalidaFinal ? compartimiento.getCompartimiento() : binTemporal);
					bin.setToBin(esSalidaFinal ? binTemporal : compartimiento.getCompartimiento());
					bin.setQuantity(compartimiento.getCantidad().doubleValue());
					bin.setLnSeqNmr(Double.valueOf(secuencia.get()));
					bin.setCreateBin(1);
					secuencia.set(secuencia.get() + 16384);
					return bin;
				}).collect(Collectors.toList());
			})
			.flatMap(Collection::stream)
			.collect(Collectors.toList()));
		
		consolidarDatosIntegracionTransferencia(inventoryIntegrator);
		
		return inventoryIntegrator;
	}
	
	private String obtenerBodegaOrigen(DocumentoBase documento) {
		String origen = StringUtils.EMPTY;
		
		if(documento instanceof DocumentoTransferenciaEntrada) {
			DocumentoTransferenciaEntrada tEntrada = (DocumentoTransferenciaEntrada) documento;
			DocumentoTransferenciaSalida transferencia = transferenciaSalidaRepositorio.findById(tEntrada.getDocumentoTransferenciaSalidaId()).get();
			
			if(transferencia.isReposicion())
				origen = constantesAmbienteServicio.getBodegaReposicionLocnCode();
			else
				origen = constantesAmbienteServicio.getBodegaTemporalLocnCode();
			
		}else {
			DocumentoTransferenciaSalida tSalida = (DocumentoTransferenciaSalida) documento;
			origen = tSalida.getBodegaOrigen();
		}
		
		return origen;
	}
	
	private String obtenerBodegaDestino(DocumentoBase documento) {
		String destino = StringUtils.EMPTY;
		
		if(documento instanceof DocumentoTransferenciaEntrada) {
			DocumentoTransferenciaEntrada tEntrada = (DocumentoTransferenciaEntrada) documento;
			destino = tEntrada.getBodegaDestino();
		}else {
			DocumentoTransferenciaSalida tSalida = (DocumentoTransferenciaSalida) documento;
			DocumentoTransferencia transferencia = transferenciaRepositorio.findById(tSalida.getDocumentoTransferenciaId()).get();
			
			if(TipoTransferencia.VENTA.equals(transferencia.getTipoTransferencia()))
				destino = constantesAmbienteServicio.getBodegaTemporalLocnCode();
			else
				destino = constantesAmbienteServicio.getBodegaReposicionLocnCode();
		}
		
		return destino;
	}
	
	
	protected String getBatchNumberForTransferencia(DocumentoBase transferencia) {
		long conteoTransferencias = 0;
		String prefix = "WST";
		if(transferencia instanceof DocumentoTransferenciaSalida) {
			prefix = prefix.concat("S");
			conteoTransferencias = transferenciaSalidaRepositorio.countByDocumentoTransferenciaIdAndEstado(((DocumentoTransferenciaSalida)transferencia).getDocumentoTransferenciaId(), DocumentoEstado.EMITIDO);
		}else{
			prefix = prefix.concat("E");
			conteoTransferencias = transferencia.getId() - 1;
		}
		StringBuilder batchNumber = new StringBuilder();
		
		String regex = "^0+(?!$)";
		if(transferencia.getCotizacion() != null) {
			String numeroCotizacion = transferencia.getCotizacion().getNumero();
			String[] numeroSplit = numeroCotizacion.split("-");
			String numero = numeroSplit[1].replaceAll(regex, "");
			return batchNumber.append(prefix).append(numeroSplit[0]).append("-").append(numero).append("-").append(++conteoTransferencias).toString();
		}else {
			String numeroTransferencia = transferencia.getReferencia().substring(0,3).concat("-").concat(transferencia.getReferencia().substring(3));
			
			String[] numeroSplit = numeroTransferencia.split("-");
			String numero = numeroSplit[1].replaceAll(regex, "");
			return batchNumber.append(prefix).append(numeroSplit[0]).append("-").append(numero).append("-").append(++conteoTransferencias).toString();
		}
		
	}
	
	private void consolidarDatosIntegracionTransferencia(IvTransferIntegrator inventoryIntegrator) {
		
		List<IvTransferLineInsertItems> lineas = inventoryIntegrator.getIvLines().stream().filter(UtilidadaesFuncionales.distinctByKey(IvTransferLineInsertItems::getItemNmbr)).collect(Collectors.toList());
		if(inventoryIntegrator.getIvLines().size() > lineas.size()) {
			LOG.info("Consolidando lineas para integrar");
			lineas.forEach(l -> l.setTrxQty(inventoryIntegrator.getIvLines().stream().filter(f -> f.getItemNmbr().equals(l.getItemNmbr())).mapToDouble(IvTransferLineInsertItems::getTrxQty).sum()));
			
			LOG.info("Consolidando bins para integrar");
			List<IvTransferMultiBinInsertItems> bins = inventoryIntegrator.getIvBins().stream().filter(UtilidadaesFuncionales.distinctByKey(IvTransferMultiBinInsertItems::getBinKey)).collect(Collectors.toList());
			bins.forEach(b -> b.setQuantity(inventoryIntegrator.getIvBins().stream().filter(f -> f.getBinKey().equals(b.getBinKey())).mapToDouble(IvTransferMultiBinInsertItems::getQuantity).sum()));
			
			inventoryIntegrator.setIvLines(lineas);
			inventoryIntegrator.setIvBins(bins);
		}
		
	}
	
	protected IvResponse llamarApiIntegracionTransferencia(IvTransferIntegrator inventoryIntegrator) {
		try {
			final String url = constantesAmbienteServicio.getUrlIntegradorGp().concat(UrlIntegracionInventory.INTEGRAR_INVENTORY.getUrl()); 
			return restTemplate.postForObject(url, inventoryIntegrator, IvResponse.class);
		}catch(HttpStatusCodeException e) {
			LOG.error(e.getMessage());
			IvResponse response = new IvResponse(); 
			response.setErrorCode("-1");
			response.setErrorMessage(e.getMessage());
			return response;
		}
	}
	
	protected void guardarTranferencia(DocumentoBase transferencia, String mensajeError) {
		if(transferencia instanceof DocumentoTransferenciaEntrada) {
			DocumentoTransferenciaEntrada transferenciaEntrada = (DocumentoTransferenciaEntrada)transferencia;			
			transferenciaEntrada.setMensajeError(mensajeError);
			transferenciaEntradaRepositorio.save(transferenciaEntrada);
		}else {
			DocumentoTransferenciaSalida transferenciaSalida = (DocumentoTransferenciaSalida)transferencia;
			transferenciaSalida.setMensajeError(mensajeError);
			transferenciaSalida.setFechaEmision(LocalDateTime.now());
			transferenciaSalidaRepositorio.save(transferenciaSalida);
		}
	}
	
	protected boolean validaTransferenciaComplartimientosIguales(DocumentoBase transferencia) {
		if(transferencia instanceof DocumentoTransferenciaEntrada)
			return true;
		else {
			BigDecimal cantidadCompartimientos = transferencia.getDetalle().stream()
					.map(DocumentoDetalle::getCompartimientos)
					.flatMap(Collection::stream)
					.map(DocumentoDetalleCompartimiento::getCantidad)
					.reduce(BigDecimal.ZERO, BigDecimal::add);
			
			BigDecimal cantidadTransferencia = transferencia.getDetalle().stream()
					.map(DocumentoDetalle::getCantidad)
					.reduce(BigDecimal.ZERO, BigDecimal::add);
			
			LOG.info(String.format("Cantidad Detalle %s, Cantidad Compartimientos %s", cantidadTransferencia.toString(), cantidadCompartimientos.toString()));
			
			return cantidadCompartimientos.compareTo(cantidadTransferencia) == 0;
		}
	}
}
