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

    /* (non-Javadoc)
     * @see java.lang.Object#hashCode()
     */
    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + (active ? 1231 : 1237);
        result = prime * result
                + ((effectiveTime == null) ? 0 : effectiveTime.hashCode());
        result = prime * result + ((id == null) ? 0 : id.hashCode());
        result = prime * result
                + ((moduleId == null) ? 0 : moduleId.hashCode());
        result = prime
                * result
                + ((referencedComponentId == null) ? 0 : referencedComponentId
                        .hashCode());
        result = prime * result
                + ((refsetId == null) ? 0 : refsetId.hashCode());
        return result;
    }

    /* (non-Javadoc)
     * @see java.lang.Object#equals(java.lang.Object)
     */
    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        RefsetMember other = (RefsetMember) obj;
        if (active != other.active)
            return false;
        if (effectiveTime == null) {
            if (other.effectiveTime != null)
                return false;
        } else if (!effectiveTime.equals(other.effectiveTime))
            return false;
        if (id == null) {
            if (other.id != null)
                return false;
        } else if (!id.equals(other.id))
            return false;
        if (moduleId == null) {
            if (other.moduleId != null)
                return false;
        } else if (!moduleId.equals(other.moduleId))
            return false;
        if (referencedComponentId == null) {
            if (other.referencedComponentId != null)
                return false;
        } else if (!referencedComponentId.equals(other.referencedComponentId))
            return false;
        if (refsetId == null) {
            if (other.refsetId != null)
                return false;
        } else if (!refsetId.equals(other.refsetId))
            return false;
        return true;
    }

}
