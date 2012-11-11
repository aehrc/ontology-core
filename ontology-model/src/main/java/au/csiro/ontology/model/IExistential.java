/**
 * Copyright CSIRO Australian e-Health Research Centre (http://aehrc.com).
 * All rights reserved. Use is subject to license terms and conditions.
 */
package au.csiro.ontology.model;

/**
 * @author Alejandro Metke
 *
 */
public interface IExistential<T> extends IConcept {
    
    /**
     * Returns the role in the existential.
     * 
     * @return
     */
    public INamedRole<T> getRole();
    
    /**
     * Returns the concept in the existential (also knwon as the filler).
     * 
     * @return
     */
    public IConcept getConcept();

}
