/**
 * Copyright CSIRO Australian e-Health Research Centre (http://aehrc.com).
 * All rights reserved. Use is subject to license terms and conditions.
 */
package au.csiro.ontology.snomed.refset.rf2;

/**
 * Simple implementation of a module dependency refset member.
 * 
 * @author Alejandro Metke
 *
 */
public class ModuleDependencyRefsetMember extends RefsetMember implements
        IModuleDependencyRefsetMember {
    
    protected String sourceEffectiveTime;
    
    protected String targetEffectiveTime;
    
    /**
     * Creates a new module dependency refset member.
     */
    public ModuleDependencyRefsetMember(String id, String effectiveTime, 
            boolean active, String moduleId, String refsetId, 
            String referencedComponentId, String sourceEffectiveTime, 
            String targetEffectiveTime) {
       super(id, effectiveTime, active, moduleId, refsetId, 
               referencedComponentId);
       this.sourceEffectiveTime = sourceEffectiveTime;
       this.targetEffectiveTime = targetEffectiveTime;
    }

    @Override
    public String getSourceEffectiveTime() {
        return sourceEffectiveTime;
    }

    @Override
    public String getTargetEffectiveTime() {
        return targetEffectiveTime;
    }

}
