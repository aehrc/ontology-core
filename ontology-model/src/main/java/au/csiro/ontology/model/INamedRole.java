/**
 * Copyright CSIRO Australian e-Health Research Centre (http://aehrc.com).
 * All rights reserved. Use is subject to license terms and conditions.
 */
package au.csiro.ontology.model;

/**
 * Defines the methods of a named role.
 * 
 * @author Alejandro Metke
 *
 */
public interface INamedRole<T> extends IRole {
    
    /**
     * Returns the id of this named role.
     * 
     * @return
     */
    public T getId();
    
}
