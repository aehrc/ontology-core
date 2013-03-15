/**
 * Copyright CSIRO Australian e-Health Research Centre (http://aehrc.com).
 * All rights reserved. Use is subject to license terms and conditions.
 */
package au.csiro.ontology.snomed.refset.rf2;

import java.util.Set;


/**
 * An RF2 reference set.
 * 
 * @author Alejandro Metke
 *
 */
public interface IRefset {
    
    /**
     * Returns the id of this refset.
     * 
     * @return
     */
    public String getId();
    
    /**
     * Returns the name of this reference set.
     * 
     * @return
     */
    public String getDisplayName();
    
    /**
     * Sets the display name for this reference set.
     * @param displayName
     */
    public void setDisplayName(String displayName);
    
    /**
     * Returns the raw members of the reference set.
     * 
     * @return
     */
    public Set<IRefsetMember> getMembers();
    
}
