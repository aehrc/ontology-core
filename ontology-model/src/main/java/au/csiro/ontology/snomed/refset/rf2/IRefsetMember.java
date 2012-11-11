/**
 * Copyright CSIRO Australian e-Health Research Centre (http://aehrc.com).
 * All rights reserved. Use is subject to license terms and conditions.
 */
package au.csiro.ontology.snomed.refset.rf2;


/**
 * A row of an RF2 reference set.
 * 
 * @author Alejandro Metke
 *
 */
public interface IRefsetMember {
    
    /**
     * Unique identifier of this refset member.
     * 
     * @return
     */
    public String getId();
    
    /**
     * Returns a string representing the inclusive date at which this change 
     * becomes effective.
     * 
     * @return
     */
    public String getEffectiveTime();
    
    /**
     * Indicates whether the member's state was active or inactive from the 
     * nominal release date specified by the effectiveTime field.
     * 
     * @return
     */
    public boolean isActive();
    
    /**
     * Returns the member version's module id.
     * 
     * @return
     */
    public String getModuleId();
    
    /**
     * Returns the id of the SNOMED concept that represents the type of this
     * refset.
     * 
     * @return
     */
    public String getRefsetId();
    
    /**
     * Returns the id of a SNOMED concept that is referenced by this refset.
     * 
     * @return
     */
    public String getReferencedComponentId();
}
