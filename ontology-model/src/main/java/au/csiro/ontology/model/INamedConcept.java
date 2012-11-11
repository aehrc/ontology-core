/**
 * Copyright CSIRO Australian e-Health Research Centre (http://aehrc.com).
 * All rights reserved. Use is subject to license terms and conditions.
 */
package au.csiro.ontology.model;

/**
 * Defines the methods of a named concept.
 * 
 * @author Alejandro Metke
 *
 */
public interface INamedConcept<T> extends IConcept {
    
    /**
     * Returns the id of this named concept.
     * 
     * @return
     */
    public T getId();
}
