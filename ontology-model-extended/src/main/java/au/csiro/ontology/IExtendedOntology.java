/**
 * Copyright CSIRO Australian e-Health Research Centre (http://aehrc.com).
 * All rights reserved. Use is subject to license terms and conditions.
 */
package au.csiro.ontology;

import au.csiro.ontology.model.AbstractInfo;
import au.csiro.ontology.model.INamedConcept;

/**
 * Defines the methods of an ontology that includes extra information about
 * its members typically useful in retrieval scenarios.
 * 
 * @author Alejandro Metke
 *
 */
public interface IExtendedOntology<T extends Comparable<T>> 
    extends IOntology<T> {
    
    /**
     * Returns extra information for a concept. This method is typically used
     * in retrieval applications.
     * 
     * @param c A named concept.
     * @return The extra information about the concept.
     */
    public AbstractInfo getConceptInfo(INamedConcept<T> c);
}
