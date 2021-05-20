package com.altioracorp.wst.util;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;

public class UtilidadesPoi {

	private static final Set<String> VALORES_NULOS = Collections
			.unmodifiableSet(new HashSet<>(Arrays.asList("N/A", "NULL", "-", "SinDatos", "SINDATOS")));

	public static final BigDecimal leerBigDecimal(Row row, int indiceColumna) {

		final Cell cell = row.getCell(indiceColumna);

		if (cell != null) {
			switch (cell.getCellTypeEnum()) {
			case NUMERIC:
				return new BigDecimal(cell.getNumericCellValue());
			case STRING:
				return new BigDecimal(cell.getStringCellValue());
			default:
				return BigDecimal.ZERO;
			}
		}

		return BigDecimal.ZERO;
	}

	public static final Date leerDate(Row row, int indiceColumna) {

		final Cell cell = row.getCell(indiceColumna);

		if (cell != null) {
			switch (cell.getCellTypeEnum()) {
			case NUMERIC:
				return cell.getDateCellValue();
			default:
				return null;
			}
		}

		return null;
	}

	public static final double leerDouble(Row row, int indiceColumna) {

		final Cell cell = row.getCell(indiceColumna);

		if (cell != null) {
			switch (cell.getCellTypeEnum()) {
			case NUMERIC:
				return (double) cell.getNumericCellValue();
			case STRING:
				return Double.parseDouble(cell.getStringCellValue());
			default:
				return 0.0;
			}
		}

		return 0.0;
	}

	public static final int leerInteger(Row row, int indiceColumna) {

		final Cell cell = row.getCell(indiceColumna);

		if (cell != null) {
			switch (cell.getCellTypeEnum()) {
			case NUMERIC:
				return (int) cell.getNumericCellValue();
			case STRING:
				return Integer.parseInt(cell.getStringCellValue());
			default:
				return -1;
			}
		}

		return -1;
	}
	
	public static final LocalDateTime leerLocalDateTime(Row row, int indiceColumna) {

		final Cell cell = row.getCell(indiceColumna);

		if (cell != null) {
			switch (cell.getCellTypeEnum()) {
			case NUMERIC:
				return cell.getDateCellValue().toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime();
			default:
				return null;
			}
		}

		return null;
	}
	
	public static final LocalDate leerLocalDate(Row row, int indiceColumna) {

		final Cell cell = row.getCell(indiceColumna);

		if (cell != null) {
			switch (cell.getCellTypeEnum()) {
			case NUMERIC:
				return cell.getDateCellValue().toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
			default:
				return null;
			}
		}

		return null;
	}
	
	public static final long leerLong(Row row, int indiceColumna) {

		final Cell cell = row.getCell(indiceColumna);

		if (cell != null) {
			switch (cell.getCellTypeEnum()) {
			case NUMERIC:
				return (long) cell.getNumericCellValue();
			case STRING:
				return Long.parseLong(cell.getStringCellValue());
			default:
				return -1;
			}
		}

		return -1;
	}


	public static final String leerString(Row row, int indiceColumna, boolean convertirVacioANull) {

		final Cell cell = row.getCell(indiceColumna);

		if (cell != null) {
			switch (cell.getCellTypeEnum()) {
			case STRING:
				final String valor = cell.getStringCellValue();
				if (UtilidadesCadena.noEsNuloNiBlanco(valor)) {
					if (!VALORES_NULOS.contains(valor.trim())) {
						return valor.trim();
					} else {
						return null;
					}
				} else {
					if (convertirVacioANull) {
						return null;
					} else {
						return valor;
					}
				}
			case NUMERIC:
				return String.valueOf((long) cell.getNumericCellValue());
			default:
				return null;
			}
		}

		return null;
	}

	public UtilidadesPoi() {
	}

}
