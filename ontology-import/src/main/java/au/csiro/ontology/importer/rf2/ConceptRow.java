/**
 * Copyright CSIRO Australian e-Health Research Centre (http://aehrc.com).
 * All rights reserved. Use is subject to license terms and conditions.
 */
package au.csiro.ontology.importer.rf2;

/**
 * Represents a row in an RF2 concepts file.
 * 
 * @author Alejandro Metke
 * 
 */
public class ConceptRow implements Comparable<ConceptRow> {

    private final String id;
    private final String effectiveTime;
    private final String active;
    private final String moduleId;
    private final String definitionStatusId;

    /**
     * Creates a new ConceptRow.
     * 
     * @param id
     * @param effectiveTime
     * @param active
     * @param moduleId
     * @param definitionStatusId
     */
    public ConceptRow(String id, String effectiveTime, String active,
            String moduleId, String definitionStatusId) {
        super();
        this.id = id;
        this.effectiveTime = effectiveTime;
        this.active = active;
        this.moduleId = moduleId;
        this.definitionStatusId = definitionStatusId;
    }

    /**
     * @return the id
     */
    public String getId() {
        return id;
    }

    /**
     * @return the effectiveTime
     */
    public String getEffectiveTime() {
        return effectiveTime;
    }

    /**
     * @return the active
     */
    public String getActive() {
        return active;
    }

    /**
     * @return the moduleId
     */
    public String getModuleId() {
        return moduleId;
    }

    /**
     * @return the definitionStatusId
     */
    public String getDefinitionStatusId() {
        return definitionStatusId;
    }
    
    @Override
    public String toString() {
        return id + ", " + effectiveTime + ", " + active + ", " + moduleId + 
                ", " + definitionStatusId;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((active == null) ? 0 : active.hashCode());
        result = prime
                * result
                + ((definitionStatusId == null) ? 0 : definitionStatusId
                        .hashCode());
        result = prime * result
                + ((effectiveTime == null) ? 0 : effectiveTime.hashCode());
        result = prime * result + ((id == null) ? 0 : id.hashCode());
        result = prime * result
                + ((moduleId == null) ? 0 : moduleId.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        ConceptRow other = (ConceptRow) obj;
        if (active == null) {
            if (other.active != null)
                return false;
        } else if (!active.equals(other.active))
            return false;
        if (definitionStatusId == null) {
            if (other.definitionStatusId != null)
                return false;
        } else if (!definitionStatusId.equals(other.definitionStatusId))
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
        return true;
    }

    @Override
    public int compareTo(ConceptRow other) {
        if(this.equals(other))
            return 0;
        else {
            int res = effectiveTime.compareTo(other.effectiveTime);
            if(res != 0) return res;
            res = moduleId.compareTo(other.moduleId);
            if(res != 0) return res;
            res = id.compareTo(other.id);
            if(res != 0) return res;
            res = active.compareTo(other.active);
            if(res != 0) return res;
            res = definitionStatusId.compareTo(other.definitionStatusId);
            assert(res != 0);
            return res;
        }
    }

}
