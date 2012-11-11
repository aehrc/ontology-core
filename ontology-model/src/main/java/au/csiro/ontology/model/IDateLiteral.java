/**
 * Copyright CSIRO Australian e-Health Research Centre (http://aehrc.com).
 * All rights reserved. Use is subject to license terms and conditions.
 */
package au.csiro.ontology.model;

import java.util.Calendar;

/**
 *  A date literal.
 * 
 * @author Alejandro Metke
 *
 */
public interface IDateLiteral extends ILiteral {
    
    /**
     * Returns the value of this literal.
     * 
     * @return
     */
    public Calendar getValue();
}
