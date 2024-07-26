//
// Este archivo ha sido generado por Eclipse Implementation of JAXB v3.0.0 
// Visite https://eclipse-ee4j.github.io/jaxb-ri 
// Todas las modificaciones realizadas en este archivo se perderán si se vuelve a compilar el esquema de origen. 
// Generado el: 2024.06.21 a las 09:46:48 AM CEST 
//


package com.hive5.ds.elements;

import javax.xml.datatype.XMLGregorianCalendar;
import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlRootElement;
import jakarta.xml.bind.annotation.XmlSchemaType;
import jakarta.xml.bind.annotation.XmlType;


/**
 * <p>Clase Java para anonymous complex type.
 * 
 * <p>El siguiente fragmento de esquema especifica el contenido que se espera que haya en esta clase.
 * 
 * <pre>
 * &lt;complexType&gt;
 *   &lt;complexContent&gt;
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *       &lt;sequence&gt;
 *         &lt;element name="title" type="{http://www.w3.org/2001/XMLSchema}string"/&gt;
 *         &lt;element name="description" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="startDate" type="{http://www.w3.org/2001/XMLSchema}date"/&gt;
 *         &lt;element name="endDate" type="{http://www.w3.org/2001/XMLSchema}date"/&gt;
 *         &lt;element name="priority" type="{http://www.w3.org/2001/XMLSchema}int"/&gt;
 *         &lt;element name="userId" type="{http://www.w3.org/2001/XMLSchema}int"/&gt;
 *         &lt;element name="boardId" type="{http://www.w3.org/2001/XMLSchema}int"/&gt;
 *         &lt;element name="statusId" type="{http://www.w3.org/2001/XMLSchema}int"/&gt;
 *       &lt;/sequence&gt;
 *     &lt;/restriction&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = {
    "title",
    "description",
    "startDate",
    "endDate",
    "priority",
    "userId",
    "boardId",
    "statusId"
})
@XmlRootElement(name = "createCardRequest")
public class CreateCardRequest {

    @XmlElement(required = true)
    protected String title;
    protected String description;
    @XmlElement(required = true)
    @XmlSchemaType(name = "date")
    protected XMLGregorianCalendar startDate;
    @XmlElement(required = true)
    @XmlSchemaType(name = "date")
    protected XMLGregorianCalendar endDate;
    protected int priority;
    protected int userId;
    protected int boardId;
    protected int statusId;

    /**
     * Obtiene el valor de la propiedad title.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getTitle() {
        return title;
    }

    /**
     * Define el valor de la propiedad title.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setTitle(String value) {
        this.title = value;
    }

    /**
     * Obtiene el valor de la propiedad description.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getDescription() {
        return description;
    }

    /**
     * Define el valor de la propiedad description.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setDescription(String value) {
        this.description = value;
    }

    /**
     * Obtiene el valor de la propiedad startDate.
     * 
     * @return
     *     possible object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public XMLGregorianCalendar getStartDate() {
        return startDate;
    }

    /**
     * Define el valor de la propiedad startDate.
     * 
     * @param value
     *     allowed object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public void setStartDate(XMLGregorianCalendar value) {
        this.startDate = value;
    }

    /**
     * Obtiene el valor de la propiedad endDate.
     * 
     * @return
     *     possible object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public XMLGregorianCalendar getEndDate() {
        return endDate;
    }

    /**
     * Define el valor de la propiedad endDate.
     * 
     * @param value
     *     allowed object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public void setEndDate(XMLGregorianCalendar value) {
        this.endDate = value;
    }

    /**
     * Obtiene el valor de la propiedad priority.
     * 
     */
    public int getPriority() {
        return priority;
    }

    /**
     * Define el valor de la propiedad priority.
     * 
     */
    public void setPriority(int value) {
        this.priority = value;
    }

    /**
     * Obtiene el valor de la propiedad userId.
     * 
     */
    public int getUserId() {
        return userId;
    }

    /**
     * Define el valor de la propiedad userId.
     * 
     */
    public void setUserId(int value) {
        this.userId = value;
    }

    /**
     * Obtiene el valor de la propiedad boardId.
     * 
     */
    public int getBoardId() {
        return boardId;
    }

    /**
     * Define el valor de la propiedad boardId.
     * 
     */
    public void setBoardId(int value) {
        this.boardId = value;
    }

    /**
     * Obtiene el valor de la propiedad statusId.
     * 
     */
    public int getStatusId() {
        return statusId;
    }

    /**
     * Define el valor de la propiedad statusId.
     * 
     */
    public void setStatusId(int value) {
        this.statusId = value;
    }

}
