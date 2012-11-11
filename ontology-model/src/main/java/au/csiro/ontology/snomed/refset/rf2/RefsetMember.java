/**
 * Copyright CSIRO Australian e-Health Research Centre (http://aehrc.com).
 * All rights reserved. Use is subject to license terms and conditions.
 */
package au.csiro.ontology.snomed.refset.rf2;

/**
 * @author Alejandro Metke
 *
 */
public class RefsetMember implements IRefsetMember {
    
    protected String id;
    
    protected String effectiveTime;
    
    protected boolean active;
    
    protected String moduleId;
    
    protected String refsetId;
    
    protected String referencedComponentId;
    
    /**
     * Constructor.
     */
    public RefsetMember(String id, String effectiveTime, boolean active, 
            String moduleId, String refsetId, String referencedComponentId) {
        this.id = id;
        this.effectiveTime = effectiveTime;
        this.active = active;
        this.moduleId = moduleId;
        this.refsetId = refsetId;
        this.referencedComponentId = referencedComponentId;
    }

    @Override
    public String getId() {
        return id;
    }

    @Override
    public String getEffectiveTime() {
        return effectiveTime;
    }

    @Override
    public boolean isActive() {
        return active;
    }

    @Override
    public String getModuleId() {
        return moduleId;
    }

    
    @Override
    public String getRefsetId() {
        return refsetId;
    }

    @Override
    public String getReferencedComponentId() {
        return referencedComponentId;
    }

}
