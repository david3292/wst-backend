package com.altioracorp.wst.dominio.sistema;

import static com.altioracorp.wst.util.UtilidadesCadena.completarCerosIzquierda;
import static com.altioracorp.wst.util.UtilidadesCadena.esNuloOBlanco;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import com.altioracorp.wst.dominio.EntidadBase;
import com.altioracorp.wst.exception.sistema.SecuencialFaltaAbreviaturaException;
import com.altioracorp.wst.exception.sistema.SecuencialFaltaDocIdGPException;

@SuppressWarnings("serial")
@Entity
public class Secuencial extends EntidadBase {

	private static final int LONGITUD_NUMERO = 10;

	@Enumerated(EnumType.STRING)
	@NotNull
	private TipoDocumento tipoDocumento;

	@Size(max = 64, message = "El campo de documentoGP no puede tener mas de 64 caractéres")
	private String docIdGP;

	private int sucesion;

	private String abreviatura;

	@NotNull
	private boolean activo;

	private String numeroSecuencial;

	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "punto_venta_id", referencedColumnName = "id", insertable = true, updatable = true)
	private PuntoVenta puntoVenta;
	
	public Secuencial() {
		
	}

	public Secuencial(PuntoVenta puntoventa, TipoDocumento tipoDocumento, String docIdGP, String abreviatura) {
		if (tipoDocumento.esDocumentoGP()) {
			if (esNuloOBlanco(docIdGP)) {
				throw new SecuencialFaltaDocIdGPException(tipoDocumento.toString());
			} else {
				this.docIdGP = docIdGP;
			}
		} else {
			if (esNuloOBlanco(abreviatura)) {
				throw new SecuencialFaltaAbreviaturaException(tipoDocumento.toString());
			} else {
				this.abreviatura = abreviatura;
				this.sucesion = 0;
			}
		}
		this.puntoVenta = puntoventa;
		this.tipoDocumento = tipoDocumento;
		this.activo = Boolean.TRUE;
	}

	public int aumentarSucesion() {

		if (!this.tipoDocumento.esDocumentoGP())
			return sucesion++;
		return 0;
	}

	public String getAbreviatura() {
		return abreviatura;
	}

	public String getDocIdGP() {
		return docIdGP;
	}

	public String getNumeroSecuencial() {
		if (!this.tipoDocumento.esDocumentoGP()) {
			return this.abreviatura.concat("-").concat(completarCerosIzquierda(this.sucesion, LONGITUD_NUMERO));
		}
		return this.numeroSecuencial;
	}

	public PuntoVenta getPuntoVenta() {
		return puntoVenta;
	}

	public int getSucesion() {
		return sucesion;
	}

	public TipoDocumento getTipoDocumento() {
		return tipoDocumento;
	}

	public boolean isActivo() {
		return activo;
	}

	public void setAbreviatura(String abreviatura) {
		this.abreviatura = abreviatura;
	}

	public void setActivo(boolean activo) {
		this.activo = activo;
	}

	public void setDocIdGP(String docIdGP) {
		this.docIdGP = docIdGP;
	}

	public void setNumeroSecuencial(String numero) {
		this.numeroSecuencial = numero;
	}

	public void setPuntoVenta(PuntoVenta puntoVenta) {
		this.puntoVenta = puntoVenta;
	}

	public void setSucesion(int sucesion) {
		this.sucesion = sucesion;
	}
	
	public void setTipoDocumento(TipoDocumento tipoDocumento) {
		this.tipoDocumento = tipoDocumento;
	}

}
