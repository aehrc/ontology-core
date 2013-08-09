/**
 * Copyright CSIRO Australian e-Health Research Centre (http://aehrc.com).
 * All rights reserved. Use is subject to license terms and conditions. 
 */
package au.csiro.ontology.model;

import javax.xml.bind.annotation.XmlRootElement;

/**
 * This class represents an integer literal.
 * 
 * @author Alejandro Metke
 * 
 */
@XmlRootElement
public class IntegerLiteral extends Literal {

    private static final long serialVersionUID = 1L;
    
    private int value;
    
    /**
     * 
     */
    public IntegerLiteral() {
        
    }
    
    /**
     * Constructor.
     * 
     * @param type
     */
    public IntegerLiteral(int value) {
        this.value = value;
    }

    /**
     * @return the value
     */
    public int getValue() {
        return value;
    }

    /**
     * @param value the value to set
     */
    public void setValue(int value) {
        this.value = value;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + value;
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
        IntegerLiteral other = (IntegerLiteral) obj;
        if (value != other.value)
            return false;
        return true;
    }

    @Override
    public String toString() {
        return String.valueOf(value);
    }

    public int compareTo(Literal o) {
        return ((Integer) value).compareTo(((IntegerLiteral) o).value);
    }

}
