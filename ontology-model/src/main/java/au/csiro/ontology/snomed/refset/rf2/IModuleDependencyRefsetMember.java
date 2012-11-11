/**
 * Copyright CSIRO Australian e-Health Research Centre (http://aehrc.com).
 * All rights reserved. Use is subject to license terms and conditions.
 */
package au.csiro.ontology.snomed.refset.rf2;

/**
 * @author Alejandro Metke
 *
 */
public interface IModuleDependencyRefsetMember extends IRefsetMember {
    
    /**
     * Returns the effective time of the source module. This allows a specific 
     * module version to be selected as having a dependency.
     * 
     * @return
     */
    public String getSourceEffectiveTime();
    
    /**
     * Returns the effective time of the target module. This allows a specific 
     * module version to be selected as being the subject of a dependency.
     * 
     * @return
     */
    public String getTargetEffectiveTime();
}
