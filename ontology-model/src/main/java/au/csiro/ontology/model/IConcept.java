/**
 * Copyright CSIRO Australian e-Health Research Centre (http://aehrc.com).
 * All rights reserved. Use is subject to license terms and conditions.
 */
package au.csiro.ontology.model;

/**
 * Base interface for concepts.
 * 
 * @author Alejandro Metke
 *
 */
public interface IConcept extends Comparable<IConcept>{
    
    public String toString();

    public int hashCode();

    public boolean equals(Object o);
    
}
