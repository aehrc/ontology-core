/**
 * Copyright CSIRO Australian e-Health Research Centre (http://aehrc.com).
 * All rights reserved. Use is subject to license terms and conditions.
 */
package au.csiro.ontology.snomed.refset.rf2;

import java.util.HashMap;
import java.util.Map;

import au.csiro.ontology.input.MapView;

/**
 * @author Alejandro Metke
 *
 */
public class RefsetRow implements MapView, Comparable<RefsetRow> {

    private static final String[] NONE = {};

    protected final String id;
    protected final String effectiveTime;
    protected final String active;
    protected final String moduleId;
    protected final String refsetId;
    protected final String referencedComponentId;
    protected final String[] extras;

    /**
     * Constructor.
     *
     * @param id
     * @param effectiveTime
     * @param active
     * @param moduleId
     * @param refsetId
     * @param referencedComponentId
     */
    public RefsetRow(String id, String effectiveTime, String active, String moduleId, String refsetId,
            String referencedComponentId, String... extras) {
        super();
        this.id = id;
        this.effectiveTime = effectiveTime;
        this.active = active;
        this.moduleId = moduleId;
        this.refsetId = refsetId;
        this.referencedComponentId = referencedComponentId;
        this.extras = extras == null ? NONE : extras;

        assert extras.length == getColumns().length;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((active == null) ? 0 : active.hashCode());
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
        result = prime * result
                + ((extras == null) ? 0 : extras.hashCode());
        return result;
    }

    /**
     * @return the names of the extra columns
     */
    public String[] getColumns() {
        return NONE;
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
     * @return the refsetId
     */
    public String getRefsetId() {
        return refsetId;
    }

    /**
     * @return the referencedComponentId
     */
    public String getReferencedComponentId() {
        return referencedComponentId;
    }

    /**
     * @return the extra columns
     */
    public String[] getExtras() {
        return extras;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        RefsetRow other = (RefsetRow) obj;
        if (active == null) {
            if (other.active != null)
                return false;
        } else if (!active.equals(other.active))
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
        if (extras == null) {
            if (other.extras != null)
                return false;
        } else if (!extras.equals(other.extras))
            return false;
        return true;
    }

    @Override
    public int compareTo(RefsetRow other) {
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
            res = refsetId.compareTo(other.refsetId);
            if(res != 0) return res;
            res = referencedComponentId.compareTo(other.referencedComponentId);
            if(res != 0) return res;
            res = extras.length - other.extras.length;
            if(res != 0) return res;
            for (int i = 0; i < extras.length; i++) {
                res = extras[i].compareTo(other.extras[i]);
                if(res != 0) return res;
            }
            return res;
        }
    }

    @Override
    public Map<String, Object> toMap() {
        Map<String, Object> map = new HashMap<>();
        map.put("id", id);
        map.put("effectiveTime", effectiveTime);
        map.put("active", active);
        map.put("moduleId", moduleId);
        map.put("refsetId", refsetId);
        map.put("referencedComponentId", referencedComponentId);
        map.put("extras", extras);
        return map ;
    }

}
