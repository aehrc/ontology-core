/**
 * Copyright CSIRO Australian e-Health Research Centre (http://aehrc.com).
 * All rights reserved. Use is subject to license terms and conditions. 
 */
package au.csiro.ontology.model;

import java.text.SimpleDateFormat;
import java.util.Calendar;

/**
 * This class represents a date literal.
 * 
 * @author Alejandro Metke
 * 
 */
public class DateLiteral implements IDateLiteral {

    private final SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
    private final Calendar value;

    public DateLiteral(Calendar value) {
        this.value = value;
    }

    /**
     * @return the value
     */
    @Override
    public Calendar getValue() {
        return value;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((value == null) ? 0 : value.hashCode());
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
        DateLiteral other = (DateLiteral) obj;
        if (value == null) {
            if (other.value != null)
                return false;
        } else if (!value.equals(other.value))
            return false;
        return true;
    }

    @Override
    public String toString() {
        return sdf.format(value.getTime());
    }

    @Override
    public int compareTo(ILiteral o) {
        DateLiteral dl = (DateLiteral) o;
        Calendar otherValue = dl.value;
        return value.compareTo(otherValue);
    }

}
