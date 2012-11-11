/**
 * Copyright CSIRO Australian e-Health Research Centre (http://aehrc.com).
 * All rights reserved. Use is subject to license terms and conditions.
 */
package au.csiro.ontology.model;

/**
 * @author Alejandro Metke
 *
 */
public interface IFloatLiteral extends ILiteral {
    
    /**
     * Returns the value of this literal.
     * 
     * @return
     */
    public float getValue();
    
}
