/**
 * Copyright CSIRO Australian e-Health Research Centre (http://aehrc.com).
 * All rights reserved. Use is subject to license terms and conditions. 
 */
package au.csiro.ontology.model;

import javax.xml.bind.annotation.XmlRootElement;

/**
 * This class represents a float literal.
 * 
 * @author Alejandro Metke
 * 
 */
@XmlRootElement
public class FloatLiteral extends Literal {

    private static final long serialVersionUID = 1L;
    
    private float value;
    
    /**
     * 
     */
    public FloatLiteral() {
        
    }
    
    /**
     * 
     * @param type
     */
    public FloatLiteral(float value) {
        this.value = value;
    }

    /**
     * @return the value
     */
    public float getValue() {
        return value;
    }

    /**
     * @param value the value to set
     */
    public void setValue(float value) {
        this.value = value;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + Float.floatToIntBits(value);
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        FloatLiteral other = (FloatLiteral) obj;
        if (Float.floatToIntBits(value) != Float.floatToIntBits(other.value))
            return false;
        return true;
    }

    @Override
    public String toString() {
        return String.valueOf(value);
    }
    
    @Override
    public int compareTo(Literal o) {
        FloatLiteral fl = (FloatLiteral) o;
        float otherValue = fl.value;
        return Float.compare(value, otherValue);
    }

}
