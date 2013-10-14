/**
 * Copyright CSIRO Australian e-Health Research Centre (http://aehrc.com).
 * All rights reserved. Use is subject to license terms and conditions. 
 */
package au.csiro.ontology.model;

import javax.xml.bind.annotation.XmlRootElement;

/**
 * This class represents a double literal. This class is deprecated. Use DecimalLiteral instead.
 * 
 * @author Alejandro Metke
 * 
 */
@Deprecated
@XmlRootElement
public class DoubleLiteral extends Literal {

    private static final long serialVersionUID = 1L;
    
    private double value;
    
    /**
     * 
     */
    public DoubleLiteral() {
        
    }
    
    /**
     * 
     * @param type
     * @param value
     */
    public DoubleLiteral(double value) {
        this.value = value;
    }

    /**
     * @return the value
     */
    public double getValue() {
        return value;
    }

    /**
     * @param value the value to set
     */
    public void setValue(double value) {
        this.value = value;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        long temp;
        temp = Double.doubleToLongBits(value);
        result = prime * result + (int) (temp ^ (temp >>> 32));
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
        DoubleLiteral other = (DoubleLiteral) obj;
        if (Double.doubleToLongBits(value) != Double
                .doubleToLongBits(other.value))
            return false;
        return true;
    }

    @Override
    public String toString() {
        return String.valueOf(value);
    }

    public int compareTo(Literal o) {
        DoubleLiteral dl = (DoubleLiteral) o;
        double otherValue = dl.value;
        return Double.compare(value, otherValue);
    }

}
