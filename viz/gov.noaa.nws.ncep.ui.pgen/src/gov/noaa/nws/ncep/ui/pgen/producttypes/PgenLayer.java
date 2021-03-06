//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, vJAXB 2.1.10 in JDK 6 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2010.09.28 at 12:29:46 PM EDT 
//

package gov.noaa.nws.ncep.ui.pgen.producttypes;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

/**
 * <p>
 * Java class for anonymous complex type.
 * 
 * <p>
 * The following schema fragment specifies the expected content contained within
 * this class.
 * 
 * <pre>
 * &lt;complexType>
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element ref="{http://www.example.org/productType}Color"/>
 *         &lt;element ref="{http://www.example.org/productType}PgenControls" minOccurs="0"/>
 *         &lt;element ref="{http://www.example.org/productType}PgenActions" minOccurs="0"/>
 *         &lt;element ref="{http://www.example.org/productType}PgenClass" maxOccurs="unbounded" minOccurs="0"/>
 *       &lt;/sequence>
 *       &lt;attribute name="OutputFile" type="{http://www.w3.org/2001/XMLSchema}string" />
 *       &lt;attribute name="InputFile" type="{http://www.w3.org/2001/XMLSchema}string" />
 *       &lt;attribute name="Filled" type="{http://www.w3.org/2001/XMLSchema}boolean" />
 *       &lt;attribute name="MonoColor" type="{http://www.w3.org/2001/XMLSchema}boolean" />
 *       &lt;attribute name="OnOff" type="{http://www.w3.org/2001/XMLSchema}boolean" />
 *       &lt;attribute name="ContourParm" type="{http://www.w3.org/2001/XMLSchema}string" />
 *       &lt;attribute name="Name" type="{http://www.w3.org/2001/XMLSchema}string" />
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = { "color", "pgenControls", "pgenActions",
        "pgenClass" })
@XmlRootElement(name = "PgenLayer")
public class PgenLayer {

    @XmlElement(name = "Color", required = true)
    protected Color color;

    @XmlElement(name = "PgenControls")
    protected PgenControls pgenControls;

    @XmlElement(name = "PgenActions")
    protected PgenActions pgenActions;

    @XmlElement(name = "PgenClass")
    protected List<PgenClass> pgenClass;

    @XmlAttribute(name = "OutputFile")
    protected String outputFile;

    @XmlAttribute(name = "InputFile")
    protected String inputFile;

    @XmlAttribute(name = "Filled")
    protected Boolean filled;

    @XmlAttribute(name = "MonoColor")
    protected Boolean monoColor;

    @XmlAttribute(name = "OnOff")
    protected Boolean onOff;

    @XmlAttribute(name = "Name")
    protected String name;

    @XmlAttribute(name = "MetaInfo")
    protected String metaInfo;

    @XmlAttribute(name = "ContourParm")
    protected String contourParm;

    public String getMetaInfo() {
        return metaInfo;
    }

    public void setMetaInfo(String metaInfo) {
        this.metaInfo = metaInfo;
    }

    /**
     * Gets the value of the color property.
     * 
     * @return possible object is {@link Color }
     * 
     */
    public Color getColor() {
        return color;
    }

    /**
     * Sets the value of the color property.
     * 
     * @param value
     *            allowed object is {@link Color }
     * 
     */
    public void setColor(Color value) {
        this.color = value;
    }

    /**
     * Gets the value of the pgenControls property.
     * 
     * @return possible object is {@link PgenControls }
     * 
     */
    public PgenControls getPgenControls() {
        return pgenControls;
    }

    /**
     * Sets the value of the pgenControls property.
     * 
     * @param value
     *            allowed object is {@link PgenControls }
     * 
     */
    public void setPgenControls(PgenControls value) {
        this.pgenControls = value;
    }

    /**
     * Gets the value of the pgenActions property.
     * 
     * @return possible object is {@link PgenActions }
     * 
     */
    public PgenActions getPgenActions() {
        return pgenActions;
    }

    /**
     * Sets the value of the pgenActions property.
     * 
     * @param value
     *            allowed object is {@link PgenActions }
     * 
     */
    public void setPgenActions(PgenActions value) {
        this.pgenActions = value;
    }

    /**
     * Gets the value of the pgenClass property.
     * 
     * <p>
     * This accessor method returns a reference to the live list, not a
     * snapshot. Therefore any modification you make to the returned list will
     * be present inside the JAXB object. This is why there is not a
     * <CODE>set</CODE> method for the pgenClass property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * 
     * <pre>
     * getPgenClass().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link PgenClass }
     * 
     * 
     */
    public List<PgenClass> getPgenClass() {
        if (pgenClass == null) {
            pgenClass = new ArrayList<PgenClass>();
        }
        return this.pgenClass;
    }

    /**
     * Gets the value of the outputFile property.
     * 
     * @return possible object is {@link String }
     * 
     */
    public String getOutputFile() {
        return outputFile;
    }

    /**
     * Sets the value of the outputFile property.
     * 
     * @param value
     *            allowed object is {@link String }
     * 
     */
    public void setOutputFile(String value) {
        this.outputFile = value;
    }

    /**
     * Gets the value of the inputFile property.
     * 
     * @return possible object is {@link String }
     * 
     */
    public String getInputFile() {
        return inputFile;
    }

    /**
     * Sets the value of the inputFile property.
     * 
     * @param value
     *            allowed object is {@link String }
     * 
     */
    public void setInputFile(String value) {
        this.inputFile = value;
    }

    /**
     * Gets the value of the filled property.
     * 
     * @return possible object is {@link Boolean }
     * 
     */
    public Boolean isFilled() {
        return filled;
    }

    /**
     * Sets the value of the filled property.
     * 
     * @param value
     *            allowed object is {@link Boolean }
     * 
     */
    public void setFilled(Boolean value) {
        this.filled = value;
    }

    /**
     * Gets the value of the monoColor property.
     * 
     * @return possible object is {@link Boolean }
     * 
     */
    public Boolean isMonoColor() {
        return monoColor;
    }

    /**
     * Sets the value of the monoColor property.
     * 
     * @param value
     *            allowed object is {@link Boolean }
     * 
     */
    public void setMonoColor(Boolean value) {
        this.monoColor = value;
    }

    /**
     * Gets the value of the onOff property.
     * 
     * @return possible object is {@link Boolean }
     * 
     */
    public Boolean isOnOff() {
        return onOff;
    }

    /**
     * Sets the value of the onOff property.
     * 
     * @param value
     *            allowed object is {@link Boolean }
     * 
     */
    public void setOnOff(Boolean value) {
        this.onOff = value;
    }

    /**
     * Gets the value of the name property.
     * 
     * @return possible object is {@link String }
     * 
     */
    public String getName() {
        return name;
    }

    /**
     * Sets the value of the name property.
     * 
     * @param value
     *            allowed object is {@link String }
     * 
     */
    public void setName(String value) {
        this.name = value;
    }

    /**
     * Gets the value of the contourParm property.
     * 
     * @return possible object is {@link String }
     * 
     */
    public String getContourParm() {
        return contourParm;
    }

    /**
     * Sets the value of the contourParm property.
     * 
     * @param value
     *            allowed object is {@link String }
     * 
     */
    public void setContourParm(String contourParm) {
        this.contourParm = contourParm;
    }

}
