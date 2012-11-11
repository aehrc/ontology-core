/**
 * Copyright CSIRO Australian e-Health Research Centre (http://aehrc.com).
 * All rights reserved. Use is subject to license terms and conditions.
 */
package au.csiro.ontology.snomed.refset.rf2;

import java.util.Collection;

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
     * Returns the name of this refset.
     * 
     * @return
     */
    public String getDisplayName();
    
    /**
     * Returns the members of this refset.
     * 
     * @return
     */
    Collection<IRefsetMember> getMembers();
    
}
