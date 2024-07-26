//
// Este archivo ha sido generado por Eclipse Implementation of JAXB v3.0.0 
// Visite https://eclipse-ee4j.github.io/jaxb-ri 
// Todas las modificaciones realizadas en este archivo se perderán si se vuelve a compilar el esquema de origen. 
// Generado el: 2024.06.18 a las 01:28:00 PM CEST 
//


package com.hive5.ds.elements;

import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlRootElement;
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
 *         &lt;element name="id" type="{http://www.w3.org/2001/XMLSchema}int"/&gt;
 *         &lt;element name="firstName" type="{http://ds.hive5.com/elements}nameType"/&gt;
 *         &lt;element name="lastName" type="{http://ds.hive5.com/elements}nameType"/&gt;
 *         &lt;element name="email" type="{http://ds.hive5.com/elements}emailType"/&gt;
 *         &lt;element name="password" type="{http://ds.hive5.com/elements}passwordType"/&gt;
 *         &lt;element name="role" type="{http://ds.hive5.com/elements}roleType"/&gt;
 *         &lt;element name="boards" type="{http://ds.hive5.com/elements}boardsType"/&gt;
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
    "id",
    "firstName",
    "lastName",
    "email",
    "password",
    "role",
    "boards"
})
@XmlRootElement(name = "user")
public class User {

    protected int id;
    @XmlElement(required = true)
    protected String firstName;
    @XmlElement(required = true)
    protected String lastName;
    @XmlElement(required = true)
    protected String email;
    @XmlElement(required = true)
    protected String password;
    @XmlElement(required = true)
    protected RoleType role;
    @XmlElement(required = true)
    protected BoardsType boards;

    /**
     * Obtiene el valor de la propiedad id.
     * 
     */
    public int getId() {
        return id;
    }

    /**
     * Define el valor de la propiedad id.
     * 
     */
    public void setId(int value) {
        this.id = value;
    }

    /**
     * Obtiene el valor de la propiedad firstName.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getFirstName() {
        return firstName;
    }

    /**
     * Define el valor de la propiedad firstName.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setFirstName(String value) {
        this.firstName = value;
    }

    /**
     * Obtiene el valor de la propiedad lastName.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getLastName() {
        return lastName;
    }

    /**
     * Define el valor de la propiedad lastName.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setLastName(String value) {
        this.lastName = value;
    }

    /**
     * Obtiene el valor de la propiedad email.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getEmail() {
        return email;
    }

    /**
     * Define el valor de la propiedad email.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setEmail(String value) {
        this.email = value;
    }

    /**
     * Obtiene el valor de la propiedad password.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getPassword() {
        return password;
    }

    /**
     * Define el valor de la propiedad password.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setPassword(String value) {
        this.password = value;
    }

    /**
     * Obtiene el valor de la propiedad role.
     * 
     * @return
     *     possible object is
     *     {@link RoleType }
     *     
     */
    public RoleType getRole() {
        return role;
    }

    /**
     * Define el valor de la propiedad role.
     * 
     * @param value
     *     allowed object is
     *     {@link RoleType }
     *     
     */
    public void setRole(RoleType value) {
        this.role = value;
    }

    /**
     * Obtiene el valor de la propiedad boards.
     * 
     * @return
     *     possible object is
     *     {@link BoardsType }
     *     
     */
    public BoardsType getBoards() {
        return boards;
    }

    /**
     * Define el valor de la propiedad boards.
     * 
     * @param value
     *     allowed object is
     *     {@link BoardsType }
     *     
     */
    public void setBoards(BoardsType value) {
        this.boards = value;
    }

}
