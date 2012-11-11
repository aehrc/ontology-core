/**
 * Copyright CSIRO Australian e-Health Research Centre (http://aehrc.com).
 * All rights reserved. Use is subject to license terms and conditions.
 */
package au.csiro.ontology.model;

/**
 * Base interface for features.
 * 
 * @author Alejandro Metke
 *
 */
public interface IFeature {
    
    abstract public String toString();

    abstract public int hashCode();

    abstract public boolean equals(Object o);
    
}
