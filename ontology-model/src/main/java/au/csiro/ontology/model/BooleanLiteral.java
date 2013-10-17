/**
 * Copyright CSIRO Australian e-Health Research Centre (http://aehrc.com).
 * All rights reserved. Use is subject to license terms and conditions. 
 */
package au.csiro.ontology.model;

import javax.xml.bind.annotation.XmlRootElement;

/**
 * This class represents a boolean literal. This class is deprecated because OWL EL does not support booleans.
 * 
 * @author Alejandro Metke
 * 
 */
@Deprecated
@XmlRootElement
public class BooleanLiteral extends Literal {

    private static final long serialVersionUID = 1L;
    
    private boolean value;
    
    /**
     * 
     */
    public BooleanLiteral() {
        super();
    }

    /**
     * 
     * @param value
     */
    public BooleanLiteral(boolean value) {
        this.value = value;
    }

    /**
     * @return the value
     */
    public boolean getValue() {
        return value;
    }

    /**
     * @param value the value to set
     */
    public void setValue(boolean value) {
        this.value = value;
    }

    /**
     * @see java.lang.Object#hashCode()
     */
    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + (value ? 1231 : 1237);
        return result;
    }

    /**
     * @see java.lang.Object#equals(java.lang.Object)
     */
    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        BooleanLiteral other = (BooleanLiteral) obj;
        if (value != other.value)
            return false;
        return true;
    }

    @Override
    public String toString() {
        if (value)
            return "true";
        return "false";
    }

    public int compareTo(Literal o) {
        BooleanLiteral bl = (BooleanLiteral) o;
        boolean otherValue = bl.value;
        if (value == otherValue) {
            return 0;
        } else {
            if (value == false) {
                return -1;
            } else {
                return 1;
            }
        }
    }

}
