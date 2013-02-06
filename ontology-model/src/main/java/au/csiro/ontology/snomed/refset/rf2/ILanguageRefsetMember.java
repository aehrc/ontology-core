/**
 * Copyright CSIRO Australian e-Health Research Centre (http://aehrc.com).
 * All rights reserved. Use is subject to license terms and conditions.
 */
package au.csiro.ontology.snomed.refset.rf2;

/**
 * @author Alejandro Metke
 *
 */
public interface ILanguageRefsetMember extends IRefsetMember {
    
    /**
     * Returns the id of the concept that represents either that the term is
     * acceptable or preferred. In the July 2012 SNOMED release these concepts
     * are 900000000000548007 (preferred) and 900000000000549004 (acceptable).
     * 
     * @return
     */
    public String getAccceptabilityId();
    
}
