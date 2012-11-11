/**
 * Copyright CSIRO Australian e-Health Research Centre (http://aehrc.com).
 * All rights reserved. Use is subject to license terms and conditions.
 */
package au.csiro.ontology.model;

/**
 * @author Alejandro Metke
 *
 */
public interface INamedFeature<T> extends IFeature {
    
    /**
     * Returns the id of this named feature.
     * 
     * @return
     */
    public T getId();
    
}
