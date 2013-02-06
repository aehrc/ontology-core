/**
 * Copyright CSIRO Australian e-Health Research Centre (http://aehrc.com).
 * All rights reserved. Use is subject to license terms and conditions.
 */
package au.csiro.ontology.snomed.refset.rf2;

/**
 * A language reference set.
 * 
 * @author Alejandro Metke
 *
 */
public interface ILanguageRefset extends IRefset {
    
    /**
     * Determines if a description is preferred or acceptable.
     * 
     * @param descId The description id.
     * @return boolean if it si preferred or false if it is acceptable.
     */
    public boolean isPreferred(String descId);
    
}
