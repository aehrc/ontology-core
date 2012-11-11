/**
 * Copyright CSIRO Australian e-Health Research Centre (http://aehrc.com).
 * All rights reserved. Use is subject to license terms and conditions.
 */
package au.csiro.ontology.model;

/**
 * Defines the methods for a boolean literal.
 * 
 * @author Alejandro Metke
 *
 */
public interface IBooleanLiteral extends ILiteral {
    
    /**
     * Returns the value of this literal.
     * 
     * @return
     */
    public boolean getValue();
}
