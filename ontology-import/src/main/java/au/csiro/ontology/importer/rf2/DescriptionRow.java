/**
 * Copyright CSIRO Australian e-Health Research Centre (http://aehrc.com).
 * All rights reserved. Use is subject to license terms and conditions.
 */
package au.csiro.ontology.importer.rf2;

/**
 * Represents a row in an RF2 descriptions file.
 * 
 * @author Alejandro Metke
 * 
 */
public class DescriptionRow implements Comparable<DescriptionRow>{

    private final String id;
    private final String effectiveTime;
    private final String active;
    private final String moduleId;
    private final String conceptId;
    private final String languageCode;
    private final String typeId;
    private final String term;
    private final String caseSignificanceId;

    /**
     * Creates a new DescriptionRow.
     * 
     * @param id
     * @param effectiveTime
     * @param active
     * @param moduleId
     * @param conceptId
     * @param languageCode
     * @param typeId
     * @param term
     * @param caseSignificanceId
     */
    public DescriptionRow(String id, String effectiveTime, String active,
            String moduleId, String conceptId, String languageCode,
            String typeId, String term, String caseSignificanceId) {
        super();
        this.id = id;
        this.effectiveTime = effectiveTime;
        this.active = active;
        this.moduleId = moduleId;
        this.conceptId = conceptId;
        this.languageCode = languageCode;
        this.typeId = typeId;
        this.term = term;
        this.caseSignificanceId = caseSignificanceId;
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
     * @return the conceptId
     */
    public String getConceptId() {
        return conceptId;
    }

    /**
     * @return the languageCode
     */
    public String getLanguageCode() {
        return languageCode;
    }

    /**
     * @return the typeId
     */
    public String getTypeId() {
        return typeId;
    }

    /**
     * @return the term
     */
    public String getTerm() {
        return term;
    }

    /**
     * @return the caseSignificanceId
     */
    public String getCaseSignificanceId() {
        return caseSignificanceId;
    }
    
    @Override
    public String toString() {
        return id + ", " + effectiveTime + ", " + active + ", " + moduleId + 
                ", " + conceptId + ", " + languageCode + ", " + typeId + ", " + 
                term + ", " + caseSignificanceId;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((active == null) ? 0 : active.hashCode());
        result = prime
                * result
                + ((caseSignificanceId == null) ? 0 : caseSignificanceId
                        .hashCode());
        result = prime * result
                + ((conceptId == null) ? 0 : conceptId.hashCode());
        result = prime * result
                + ((effectiveTime == null) ? 0 : effectiveTime.hashCode());
        result = prime * result + ((id == null) ? 0 : id.hashCode());
        result = prime * result
                + ((languageCode == null) ? 0 : languageCode.hashCode());
        result = prime * result
                + ((moduleId == null) ? 0 : moduleId.hashCode());
        result = prime * result + ((term == null) ? 0 : term.hashCode());
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
        DescriptionRow other = (DescriptionRow) obj;
        if (active == null) {
            if (other.active != null)
                return false;
        } else if (!active.equals(other.active))
            return false;
        if (caseSignificanceId == null) {
            if (other.caseSignificanceId != null)
                return false;
        } else if (!caseSignificanceId.equals(other.caseSignificanceId))
            return false;
        if (conceptId == null) {
            if (other.conceptId != null)
                return false;
        } else if (!conceptId.equals(other.conceptId))
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
        if (languageCode == null) {
            if (other.languageCode != null)
                return false;
        } else if (!languageCode.equals(other.languageCode))
            return false;
        if (moduleId == null) {
            if (other.moduleId != null)
                return false;
        } else if (!moduleId.equals(other.moduleId))
            return false;
        if (term == null) {
            if (other.term != null)
                return false;
        } else if (!term.equals(other.term))
            return false;
        if (typeId == null) {
            if (other.typeId != null)
                return false;
        } else if (!typeId.equals(other.typeId))
            return false;
        return true;
    }

    @Override
    public int compareTo(DescriptionRow other) {
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
            res = conceptId.compareTo(other.conceptId);
            if(res != 0) return res;
            res = languageCode.compareTo(other.languageCode);
            if(res != 0) return res;
            res = typeId.compareTo(other.typeId);
            if(res != 0) return res;
            res = term.compareTo(other.term);
            if(res != 0) return res;
            res = caseSignificanceId.compareTo(other.caseSignificanceId);
            assert(res != 0);
            return res;
        }
    }

}
