/**
 * Copyright CSIRO Australian e-Health Research Centre (http://aehrc.com).
 * All rights reserved. Use is subject to license terms and conditions.
 */
package au.csiro.ontology.importer.rf1;

/**
 * @author Alejandro Metke
 * 
 */
public class RelationshipRow {

    private final String relationshipId;
    private final String conceptId1;
    private final String relationshipType;
    private final String conceptId2;
    private final String characteristicType;
    private final String refinability;
    private final String relationshipGroup;
    
    /**
     * Creates a new RelationshipRow.
     * 
     * @param relationshipId
     * @param conceptId1
     * @param relationshipType
     * @param conceptId2
     * @param characteristicType
     * @param refinability
     * @param relationshipGroup
     */
    public RelationshipRow(String relationshipId, String conceptId1,
            String relationshipType, String conceptId2,
            String characteristicType, String refinability,
            String relationshipGroup) {
        super();
        this.relationshipId = relationshipId;
        this.conceptId1 = conceptId1;
        this.relationshipType = relationshipType;
        this.conceptId2 = conceptId2;
        this.characteristicType = characteristicType;
        this.refinability = refinability;
        this.relationshipGroup = relationshipGroup;
    }

    public String getRelationshipId() {
        return relationshipId;
    }

    public String getConceptId1() {
        return conceptId1;
    }

    public String getRelationshipType() {
        return relationshipType;
    }

    public String getConceptId2() {
        return conceptId2;
    }

    public String getCharacteristicType() {
        return characteristicType;
    }

    public String getRefinability() {
        return refinability;
    }

    public String getRelationshipGroup() {
        return relationshipGroup;
    }

}
