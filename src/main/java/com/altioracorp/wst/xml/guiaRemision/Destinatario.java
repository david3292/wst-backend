package com.altioracorp.wst.xml.guiaRemision;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "destinatario", namespace = "", propOrder = {
    "identificacionDestinatario",
    "razonSocialDestinatario",
    "dirDestinatario",
    "motivoTraslado",
    "docAduaneroUnico",
    "codEstabDestino",
    "ruta",
    "codDocSustento",
    "numDocSustento",
    "numAutDocSustento",
    "fechaEmisionDocSustento",
    "detalles"
})
public class Destinatario {

	@XmlElement(required = true)
    protected String identificacionDestinatario;
    @XmlElement(required = true)
    protected String razonSocialDestinatario;
    @XmlElement(required = true)
    protected String dirDestinatario;
    @XmlElement(required = true)
    protected String motivoTraslado;
    protected String docAduaneroUnico;
    protected String codEstabDestino;
    protected String ruta;
    protected String codDocSustento;
    protected String numDocSustento;
    protected String numAutDocSustento;
    protected String fechaEmisionDocSustento;
    @XmlElement(required = true)
    protected Destinatario.Detalles detalles;

    /**
     * Gets the value of the identificacionDestinatario property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getIdentificacionDestinatario() {
        return identificacionDestinatario;
    }

    /**
     * Sets the value of the identificacionDestinatario property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setIdentificacionDestinatario(String value) {
        this.identificacionDestinatario = value;
    }

    /**
     * Gets the value of the razonSocialDestinatario property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getRazonSocialDestinatario() {
        return razonSocialDestinatario;
    }

    /**
     * Sets the value of the razonSocialDestinatario property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setRazonSocialDestinatario(String value) {
        this.razonSocialDestinatario = value;
    }

    /**
     * Gets the value of the dirDestinatario property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getDirDestinatario() {
        return dirDestinatario;
    }

    /**
     * Sets the value of the dirDestinatario property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setDirDestinatario(String value) {
        this.dirDestinatario = value;
    }

    /**
     * Gets the value of the motivoTraslado property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getMotivoTraslado() {
        return motivoTraslado;
    }

    /**
     * Sets the value of the motivoTraslado property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setMotivoTraslado(String value) {
        this.motivoTraslado = value;
    }

    /**
     * Gets the value of the docAduaneroUnico property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getDocAduaneroUnico() {
        return docAduaneroUnico;
    }

    /**
     * Sets the value of the docAduaneroUnico property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setDocAduaneroUnico(String value) {
        this.docAduaneroUnico = value;
    }

    /**
     * Gets the value of the codEstabDestino property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getCodEstabDestino() {
        return codEstabDestino;
    }

    /**
     * Sets the value of the codEstabDestino property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setCodEstabDestino(String value) {
        this.codEstabDestino = value;
    }

    /**
     * Gets the value of the ruta property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getRuta() {
        return ruta;
    }

    /**
     * Sets the value of the ruta property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setRuta(String value) {
        this.ruta = value;
    }

    /**
     * Gets the value of the codDocSustento property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getCodDocSustento() {
        return codDocSustento;
    }

    /**
     * Sets the value of the codDocSustento property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setCodDocSustento(String value) {
        this.codDocSustento = value;
    }

    /**
     * Gets the value of the numDocSustento property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getNumDocSustento() {
        return numDocSustento;
    }

    /**
     * Sets the value of the numDocSustento property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setNumDocSustento(String value) {
        this.numDocSustento = value;
    }

    /**
     * Gets the value of the numAutDocSustento property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getNumAutDocSustento() {
        return numAutDocSustento;
    }

    /**
     * Sets the value of the numAutDocSustento property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setNumAutDocSustento(String value) {
        this.numAutDocSustento = value;
    }

    /**
     * Gets the value of the fechaEmisionDocSustento property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getFechaEmisionDocSustento() {
        return fechaEmisionDocSustento;
    }

    /**
     * Sets the value of the fechaEmisionDocSustento property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setFechaEmisionDocSustento(String value) {
        this.fechaEmisionDocSustento = value;
    }

    /**
     * Gets the value of the detalles property.
     * 
     * @return
     *     possible object is
     *     {@link Destinatario.Detalles }
     *     
     */
    public Destinatario.Detalles getDetalles() {
        return detalles;
    }

    /**
     * Sets the value of the detalles property.
     * 
     * @param value
     *     allowed object is
     *     {@link Destinatario.Detalles }
     *     
     */
    public void setDetalles(Destinatario.Detalles value) {
        this.detalles = value;
    }


    /**
     * <p>Java class for anonymous complex type.
     * 
     * <p>The following schema fragment specifies the expected content contained within this class.
     * 
     * <pre>
     * &lt;complexType>
     *   &lt;complexContent>
     *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
     *       &lt;sequence maxOccurs="unbounded">
     *         &lt;element name="detalle" type="{}detalle"/>
     *       &lt;/sequence>
     *     &lt;/restriction>
     *   &lt;/complexContent>
     * &lt;/complexType>
     * </pre>
     * 
     * 
     */
    @XmlAccessorType(XmlAccessType.FIELD)
    @XmlType(name = "", propOrder = {
        "detalle"
    })
    public static class Detalles {

        @XmlElement(required = true)
        protected List<Detalle> detalle;

        /**
         * Gets the value of the detalle property.
         * 
         * <p>
         * This accessor method returns a reference to the live list,
         * not a snapshot. Therefore any modification you make to the
         * returned list will be present inside the JAXB object.
         * This is why there is not a <CODE>set</CODE> method for the detalle property.
         * 
         * <p>
         * For example, to add a new item, do as follows:
         * <pre>
         *    getDetalle().add(newItem);
         * </pre>
         * 
         * 
         * <p>
         * Objects of the following type(s) are allowed in the list
         * {@link Detalle }
         * 
         * 
         */
        public List<Detalle> getDetalle() {
            if (detalle == null) {
                detalle = new ArrayList<Detalle>();
            }
            return this.detalle;
        }

    }
	
}
