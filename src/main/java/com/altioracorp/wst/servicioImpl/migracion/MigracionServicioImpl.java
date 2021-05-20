package com.altioracorp.wst.servicioImpl.migracion;

import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.poi.EncryptedDocumentException;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.altioracorp.wst.dominio.cobros.CondicionCobroFactura;
import com.altioracorp.wst.dominio.sistema.Bodega;
import com.altioracorp.wst.dominio.sistema.PuntoVenta;
import com.altioracorp.wst.dominio.sistema.Secuencial;
import com.altioracorp.wst.dominio.sistema.TipoDocumento;
import com.altioracorp.wst.dominio.sistema.TipoPago;
import com.altioracorp.wst.dominio.ventas.Cotizacion;
import com.altioracorp.wst.dominio.ventas.CotizacionDetalle;
import com.altioracorp.wst.dominio.ventas.CotizacionEstado;
import com.altioracorp.wst.dominio.ventas.DocumentoDetalle;
import com.altioracorp.wst.dominio.ventas.DocumentoEstado;
import com.altioracorp.wst.dominio.ventas.DocumentoFactura;
import com.altioracorp.wst.dominio.ventas.Entrega;
import com.altioracorp.wst.exception.migracion.MigradorDatosException;
import com.altioracorp.wst.exception.sistema.PuntoVentaNoExisteException;
import com.altioracorp.wst.exception.sistema.PuntoVentaSinBodegaPrincipalException;
import com.altioracorp.wst.exception.sistema.SecuancialNoExisteException;
import com.altioracorp.wst.exception.ventas.FacturaNoEncontradaException;
import com.altioracorp.wst.repositorio.cobros.ICondicionCobroFacturaRepositorio;
import com.altioracorp.wst.repositorio.ventas.ICotizacionRepositorio;
import com.altioracorp.wst.repositorio.ventas.IDocumentoFacturaRepositorio;
import com.altioracorp.wst.servicio.migracion.IMigracionServicio;
import com.altioracorp.wst.servicio.sistema.IPuntoVentaBodegaServicio;
import com.altioracorp.wst.servicio.sistema.IPuntoVentaServicio;
import com.altioracorp.wst.servicio.ventas.IDocumentoServicio;
import com.altioracorp.wst.util.UtilidadesPoi;

@Service
public class MigracionServicioImpl implements IMigracionServicio {

	private static final Log LOG = LogFactory.getLog(MigracionServicioImpl.class);

	public static final BigDecimal PORCENTAJE_IMPUESTO = new BigDecimal(0.12);

	@Autowired
	private IPuntoVentaServicio puntoVentaServicio;

	@Autowired
	IPuntoVentaBodegaServicio puntoVentaBodegaServicio;

	@Autowired
	private IDocumentoServicio documentoServicio;

	@Autowired
	private IDocumentoFacturaRepositorio facturaRepositorio;

	@Autowired
	private ICondicionCobroFacturaRepositorio condicionCobroFacturaRepositorio;
	
	@Autowired
	private ICotizacionRepositorio cotizacionRepositorio;

	@Override
	@Transactional(readOnly = false, propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
	public void importarDatos(final File archivo) {

		LOG.info("Leyendo archivo: " + archivo);

		try {
			leerDatos(archivo);
		} catch (Exception e) {
			throw new MigradorDatosException(e);
		}
	}

	private void leerCondicionCobroFactura(final DocumentoFactura factura, final Row row) {
		CondicionCobroFactura condicionCobroFactura = new CondicionCobroFactura(factura,
				UtilidadesPoi.leerLocalDate(row, 1), UtilidadesPoi.leerInteger(row, 2),
				UtilidadesPoi.leerInteger(row, 2), UtilidadesPoi.leerInteger(row, 2),
				UtilidadesPoi.leerBigDecimal(row, 5));
		condicionCobroFacturaRepositorio.save(condicionCobroFactura);

	}

	private void leerCondicionesCobroFacturas(List<DocumentoFactura> facturas, final Workbook workbook) {

		LOG.info("Leyendo datos de Condición Cobros...");

		final Sheet sheet = workbook.getSheetAt(1);

		for (Row row : sheet) {

			final int indiceFila = row.getRowNum();

			if (indiceFila < 1) {
				continue;
			}

			String numeroFactura = UtilidadesPoi.leerString(row, 0, true);

			DocumentoFactura factura = facturas.stream().filter(x -> x.getNumero().equals(numeroFactura)).findAny()
					.get();

			if (factura == null) {
				throw new FacturaNoEncontradaException(numeroFactura);
			}

			leerCondicionCobroFactura(factura, row);

		}
	}

	private Cotizacion leerCotizacion(PuntoVenta puntoVenta, Row row) {

		Secuencial secuencial = documentoServicio.secuencialDocumento(puntoVenta, TipoDocumento.COTIZACION);
		if (secuencial == null)
			throw new SecuancialNoExisteException(puntoVenta.getNombre(), TipoDocumento.COTIZACION.name());

		Date fecha = new Date();
		Cotizacion cotizacion = new Cotizacion();
		cotizacion.setDatoInicial(true);
		cotizacion.setNumero(secuencial.getNumeroSecuencial());
		cotizacion.setEstado(CotizacionEstado.FACTURADO);
		cotizacion.setActivo(true);
		cotizacion.setCreadoPor(UtilidadesPoi.leerString(row, 1, true));
		cotizacion.setCreadoFecha(fecha);
		cotizacion.setCodigoCliente(UtilidadesPoi.leerString(row, 3, true));
		cotizacion.setPuntoVenta(puntoVenta);
		cotizacion.setCodigoDireccion(UtilidadesPoi.leerString(row, 4, true));
		cotizacion.setDescripcionDireccion(UtilidadesPoi.leerString(row, 5, true));
		cotizacion.setFormaPago(obtenerTipoPago(row, 6));
		cotizacion.setCondicionPago(UtilidadesPoi.leerString(row, 7, true));
		cotizacion.setFechaEmision(UtilidadesPoi.leerLocalDateTime(row, 8));
		cotizacion.setEmpresa(UtilidadesPoi.leerString(row, 18, true));
		cotizacion.setCodigoVendedor("001");
		
		cotizacion = cotizacionRepositorio.save(cotizacion);
		leerCotizacionDetalle(cotizacion, row);

		return cotizacion;
	}

	private void leerCotizacionDetalle(Cotizacion cotizacion, Row row) {
		CotizacionDetalle cotizacionDetalle = new CotizacionDetalle();
		cotizacionDetalle.setCreadoPor(cotizacion.getCreadoPor());
		cotizacionDetalle.setCreadoFecha(cotizacion.getCreadoFecha());
		cotizacionDetalle.setCodigoArticulo(UtilidadesPoi.leerString(row, 9, true));
		cotizacionDetalle.setClaseArticulo(UtilidadesPoi.leerString(row, 10, true));
		cotizacionDetalle.setDescripcionArticulo(UtilidadesPoi.leerString(row, 11, true));
		cotizacionDetalle.setUnidadMedida(UtilidadesPoi.leerString(row, 12, true));
		cotizacionDetalle.setImpuestoDetalle(UtilidadesPoi.leerString(row, 13, true));
		cotizacionDetalle.setPesoArticulo(UtilidadesPoi.leerBigDecimal(row, 14));
		cotizacionDetalle.setImpuestoValor(UtilidadesPoi.leerBigDecimal(row, 15));
		cotizacionDetalle.setPrecio(UtilidadesPoi.leerBigDecimal(row, 16));
		cotizacionDetalle.setCantidad(UtilidadesPoi.leerBigDecimal(row, 17));
		cotizacionDetalle.setSaldo(BigDecimal.ZERO);
		cotizacionDetalle.setDescuentoAdicional(BigDecimal.ZERO);
		cotizacionDetalle.setDescuentoFijo(BigDecimal.ZERO);
		cotizacionDetalle.calcularPrecioUnitario();
		cotizacionDetalle.calcularValorDescuento();		
		cotizacionDetalle.calcularSubtotal();
		cotizacionDetalle.calcularTotal();
		cotizacion.agregarLineaDetalle(cotizacionDetalle);
		cotizacion.setTotalKilos(cotizacion.getDetalle().stream().map(x -> x.getPesoArticulo()).reduce(BigDecimal.ZERO, BigDecimal::add));
		cotizacion.setTotalBaseImponible(
				cotizacion.getDetalle().stream().map(x -> x.getTotal()).reduce(BigDecimal.ZERO, BigDecimal::add));
		cotizacion.setTotalIva(cotizacion.getTotalBaseImponible().multiply(PORCENTAJE_IMPUESTO));

	}

	private void leerDatos(final File archivo) throws EncryptedDocumentException, InvalidFormatException, IOException {

		try (Workbook workbook = WorkbookFactory.create(archivo)) {
			
			final List<DocumentoFactura> facturas = leerFacturas(workbook);

			leerCondicionesCobroFacturas(facturas, workbook);

		} catch (Exception e) {
			
			throw new MigradorDatosException(e);
		}

	}

	private DocumentoFactura leerFactura(Row row) {
		DocumentoFactura factura = new DocumentoFactura();
		PuntoVenta puntoVenta = puntoVentaServicio.buscarPorNombre(UtilidadesPoi.leerString(row, 2, true));
		if (puntoVenta == null)
			throw new PuntoVentaNoExisteException();

		Bodega bodegaPrincipal = puntoVentaBodegaServicio.buscarBodegaPrincipalPorPuntoVenta(puntoVenta);
		if (bodegaPrincipal == null)
			throw new PuntoVentaSinBodegaPrincipalException(puntoVenta.getNombre());

		Cotizacion cotizacion = leerCotizacion(puntoVenta, row);
		factura.setCotizacion(cotizacion);
		factura.setTipo(TipoDocumento.FACTURA);
		factura.setCreadoPor(cotizacion.getCreadoPor());
		factura.setCreadoFecha(cotizacion.getCreadoFecha());
		factura.setFechaEmision(cotizacion.getFechaEmision());
		factura.setNumero(UtilidadesPoi.leerString(row, 0, true));
		factura.setFacturaIntegrada(true);
		factura.setDespachada(true);
		factura.setEstado(DocumentoEstado.EMITIDO);
		factura.setEntrega(Entrega.CLIENTE);
		factura.setTotal(cotizacion.getTotal());
		factura.setPeriodoGracia(0);
		factura.setBodegaPrincipal(bodegaPrincipal.getCodigo());

		leerFacturaDetalle(factura, row);

		facturaRepositorio.save(factura);

		return factura;
	}

	private void leerFacturaDetalle(DocumentoFactura factura, Row row) {
		CotizacionDetalle cotizacionDetalle = factura.getCotizacion().getDetalle().stream().findFirst().get();
		DocumentoDetalle detalle = new DocumentoDetalle(cotizacionDetalle, factura.getBodegaPrincipal(), cotizacionDetalle.getCantidad());
		detalle.setSaldo(BigDecimal.ZERO);
		detalle.setTotal(cotizacionDetalle.getTotal());
		factura.agregarLineaDetalle(detalle);
	}

	private List<DocumentoFactura> leerFacturas(final Workbook workbook) {

		LOG.info("Leyendo datos de Facturas...");

		List<DocumentoFactura> facturas = new ArrayList<DocumentoFactura>();

		final Sheet sheet = workbook.getSheetAt(0);

		for (Row row : sheet) {
			final int indiceFila = row.getRowNum();
			if (indiceFila < 1) {
				continue;
			}

			facturas.add(leerFactura(row));

		}

		return facturas;

	}

	private TipoPago obtenerTipoPago(Row row, int indiceColumna) {
		switch (UtilidadesPoi.leerString(row, indiceColumna, true)) {
		case "CONTADO":
			return TipoPago.CONTADO;
		case "CREDITO":
			return TipoPago.CREDITO;
		case "CREDITO_SOPORTE":
			return TipoPago.CREDITO_SOPORTE;
		default:
			return TipoPago.CREDITO;
		}
	}

}
