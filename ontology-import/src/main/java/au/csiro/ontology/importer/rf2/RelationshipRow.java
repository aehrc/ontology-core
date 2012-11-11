/**
 * Copyright CSIRO Australian e-Health Research Centre (http://aehrc.com).
 * All rights reserved. Use is subject to license terms and conditions.
 */
package au.csiro.ontology.importer.rf2;

/**
 * @author Alejandro Metke
 * 
 */
public class RelationshipRow implements Comparable<RelationshipRow> {

    private final String id;
    private final String effectiveTime;
    private final String active;
    private final String moduleId;
    private final String sourceId;
    private final String destinationId;
    private final String relationshipGroup;
    private final String typeId;
    private final String characteristicTypeId;
    private final String modifierId;

    /**
     * Creates a new RelationshipRow.
     * 
     * @param id
     * @param effectiveTime
     * @param active
     * @param moduleId
     * @param sourceId
     * @param destinationId
     * @param relationshipGroup
     * @param typeId
     * @param characteristicTypeId
     * @param modifierId
     */
    public RelationshipRow(String id, String effectiveTime, String active,
            String moduleId, String sourceId, String destinationId,
            String relationshipGroup, String typeId,
            String characteristicTypeId, String modifierId) {
        super();
        this.id = id;
        this.effectiveTime = effectiveTime;
        this.active = active;
        this.moduleId = moduleId;
        this.sourceId = sourceId;
        this.destinationId = destinationId;
        this.relationshipGroup = relationshipGroup;
        this.typeId = typeId;
        this.characteristicTypeId = characteristicTypeId;
        this.modifierId = modifierId;
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
     * @return the sourceId
     */
    public String getSourceId() {
        return sourceId;
    }

    /**
     * @return the destinationId
     */
    public String getDestinationId() {
        return destinationId;
    }

    /**
     * @return the relationshipGroup
     */
    public String getRelationshipGroup() {
        return relationshipGroup;
    }

    /**
     * @return the typeId
     */
    public String getTypeId() {
        return typeId;
    }

    /**
     * @return the characteristicTypeId
     */
    public String getCharacteristicTypeId() {
        return characteristicTypeId;
    }

    /**
     * @return the modifierId
     */
    public String getModifierId() {
        return modifierId;
    }
    
    @Override
    public String toString() {
        return id + ", " + effectiveTime + ", " + active + ", " + moduleId + 
                ", " + sourceId + ", " + destinationId + ", " + 
                relationshipGroup + ", " + typeId + ", " + 
                characteristicTypeId + ", " + modifierId;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((active == null) ? 0 : active.hashCode());
        result = prime
                * result
                + ((characteristicTypeId == null) ? 0 : characteristicTypeId
                        .hashCode());
        result = prime * result
                + ((destinationId == null) ? 0 : destinationId.hashCode());
        result = prime * result
                + ((effectiveTime == null) ? 0 : effectiveTime.hashCode());
        result = prime * result + ((id == null) ? 0 : id.hashCode());
        result = prime * result
                + ((modifierId == null) ? 0 : modifierId.hashCode());
        result = prime * result
                + ((moduleId == null) ? 0 : moduleId.hashCode());
        result = prime
                * result
                + ((relationshipGroup == null) ? 0 : relationshipGroup
                        .hashCode());
        result = prime * result
                + ((sourceId == null) ? 0 : sourceId.hashCode());
        result = prime * result + ((typeId == null) ? 0 : typeId.hashCode());
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
        RelationshipRow other = (RelationshipRow) obj;
        if (active == null) {
            if (other.active != null)
                return false;
        } else if (!active.equals(other.active))
            return false;
        if (characteristicTypeId == null) {
            if (other.characteristicTypeId != null)
                return false;
        } else if (!characteristicTypeId.equals(other.characteristicTypeId))
            return false;
        if (destinationId == null) {
            if (other.destinationId != null)
                return false;
        } else if (!destinationId.equals(other.destinationId))
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
        if (modifierId == null) {
            if (other.modifierId != null)
                return false;
        } else if (!modifierId.equals(other.modifierId))
            return false;
        if (moduleId == null) {
            if (other.moduleId != null)
                return false;
        } else if (!moduleId.equals(other.moduleId))
            return false;
        if (relationshipGroup == null) {
            if (other.relationshipGroup != null)
                return false;
        } else if (!relationshipGroup.equals(other.relationshipGroup))
            return false;
        if (sourceId == null) {
            if (other.sourceId != null)
                return false;
        } else if (!sourceId.equals(other.sourceId))
            return false;
        if (typeId == null) {
            if (other.typeId != null)
                return false;
        } else if (!typeId.equals(other.typeId))
            return false;
        return true;
    }

    @Override
    public int compareTo(RelationshipRow other) {
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
            
            res = sourceId.compareTo(other.sourceId);
            if(res != 0) return res;
            res = destinationId.compareTo(other.destinationId);
            if(res != 0) return res;
            res = relationshipGroup.compareTo(other.relationshipGroup);
            if(res != 0) return res;
            res = typeId.compareTo(other.typeId);
            if(res != 0) return res;
            res = characteristicTypeId.compareTo(other.characteristicTypeId);
            if(res != 0) return res;
            res = modifierId.compareTo(other.modifierId);
            assert(res != 0);
            return res;
        }
    }
    
    

}
