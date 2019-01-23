/**
 * Copyright CSIRO Australian e-Health Research Centre (http://aehrc.com).
 * All rights reserved. Use is subject to license terms and conditions.
 */
package au.csiro.ontology.snomed.refset.rf2;

import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import au.csiro.ontology.input.MapView;
import au.csiro.ontology.input.StructuredLog;

/**
 * Simple implementation of a module dependency refset member.
 *
 * @author Alejandro Metke
 *
 */
public class ModuleDependencyRow implements MapView {

    final static private Logger log = LoggerFactory.getLogger(ModuleDependencyRow.class);

    protected final String id;

    protected final String effectiveTime;

    protected final boolean active;

    protected final String moduleId;

    protected final String refsetId;

    protected final String referencedComponentId;

    protected final String sourceEffectiveTime;

    protected final String targetEffectiveTime;

    private boolean malformed = false;

    public ModuleDependencyRow(String id, String effectiveTime, boolean active,
            String moduleId, String refsetId, String referencedComponentId,
            String sourceEffectiveTime, String targetEffectiveTime) {

        this.id = id;
        this.effectiveTime = effectiveTime;
        this.active = active;
        this.moduleId = moduleId;
        this.refsetId = refsetId;
        this.referencedComponentId = referencedComponentId;
        this.sourceEffectiveTime = sourceEffectiveTime;
        this.targetEffectiveTime = targetEffectiveTime;

        // Only do semantic checks on active rows
        // otherwise errors can never be corrected
        if (active) {
            if (!refsetId.equals("900000000000534007")) {
                malformed = true;
                StructuredLog.RefsetIdMismatch.error(toMap(), log);
            }
            if (!effectiveTime.equals(sourceEffectiveTime)) {
                malformed = true;
                StructuredLog.TimesMismatch.error(toMap(), log);
            }
            if (ModuleDependencyRefset.parseTime(effectiveTime).compareTo(ModuleDependencyRefset.parseTime(sourceEffectiveTime)) < 0) {
                malformed = true;
                StructuredLog.EffectgiveTimeOrderMismatch.error(toMap(), log);
            }
            if (ModuleDependencyRefset.parseTime(sourceEffectiveTime).compareTo(ModuleDependencyRefset.parseTime(targetEffectiveTime)) < 0) {
                malformed = true;
                StructuredLog.SourceTimeMismatch.error(toMap(), log);
            }
        }
    }

    @Override
    public Map<String, Object> toMap() {
        final Map<String, Object> map = new HashMap<>();

        map.put("type", "ModuleDependencyRow");
        map.put("id", id);
        map.put("effectiveTime", effectiveTime);
        map.put("active", active);
        map.put("moduleId", moduleId);
        map.put("refsetId", refsetId);
        map.put("referencedComponentId", referencedComponentId);
        map.put("sourceEffectiveTime", sourceEffectiveTime);
        map.put("targetEffectiveTime", targetEffectiveTime);

        return map;
    }

    boolean isMalformed() {
        return malformed;
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
    public boolean isActive() {
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
     * @return the sourceEffectiveTime
     */
    public String getSourceEffectiveTime() {
        return sourceEffectiveTime;
    }

    /**
     * @return the targetEffectiveTime
     */
    public String getTargetEffectiveTime() {
        return targetEffectiveTime;
    }

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
        result = prime
                * result
                + ((sourceEffectiveTime == null) ? 0 : sourceEffectiveTime
                        .hashCode());
        result = prime
                * result
                + ((targetEffectiveTime == null) ? 0 : targetEffectiveTime
                        .hashCode());
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
        ModuleDependencyRow other = (ModuleDependencyRow) obj;
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
        if (sourceEffectiveTime == null) {
            if (other.sourceEffectiveTime != null)
                return false;
        } else if (!sourceEffectiveTime.equals(other.sourceEffectiveTime))
            return false;
        if (targetEffectiveTime == null) {
            if (other.targetEffectiveTime != null)
                return false;
        } else if (!targetEffectiveTime.equals(other.targetEffectiveTime))
            return false;
        return true;
    }

    @Override
    public String toString() {
        return StructuredLog.renderMap(toMap());
    }

}
