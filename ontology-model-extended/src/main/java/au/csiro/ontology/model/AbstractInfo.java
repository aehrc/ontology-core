/**
 * Copyright CSIRO Australian e-Health Research Centre (http://aehrc.com).
 * All rights reserved. Use is subject to license terms and conditions.
 */
package au.csiro.ontology.model;

/**
 * Base class used to provide additional information about entities.
 * 
 * @author Alejandro Metke
 * 
 */
public abstract class AbstractInfo {
    
    /**
     * Returns a human-readable label for this entity.
     * 
     * @return
     */
    public abstract String getLabel();

}
