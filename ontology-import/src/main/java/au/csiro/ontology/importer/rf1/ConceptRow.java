/**
 * Copyright CSIRO Australian e-Health Research Centre (http://aehrc.com).
 * All rights reserved. Use is subject to license terms and conditions.
 */
package au.csiro.ontology.importer.rf1;

/**
 * Represents a row in an RF1 concepts file.
 * 
 * @author Alejandro Metke
 * 
 */
public class ConceptRow {

    private final String conceptId;
    private final String conceptStatus;
    private final String fullySpecifiedName;
    private final String ctv3Id;
    private final String snomedId;
    private final String isPrimitive;

    /**
     * Creates a new ConceptRow.
     * 
     * @param id
     * @param effectiveTime
     * @param active
     * @param moduleId
     * @param definitionStatusId
     */
    public ConceptRow(String conceptId, String conceptStatus, 
            String fullySpecifiedName, String ctv3Id, String snomedId, 
            String isPrimitive) {
        super();
        this.conceptId = conceptId;
        this.conceptStatus = conceptStatus;
        this.fullySpecifiedName = fullySpecifiedName;
        this.ctv3Id = ctv3Id;
        this.snomedId = snomedId;
        this.isPrimitive = isPrimitive;
    }

    public String getConceptId() {
        return conceptId;
    }

    public String getConceptStatus() {
        return conceptStatus;
    }

    public String getFullySpecifiedName() {
        return fullySpecifiedName;
    }

    public String getCtv3Id() {
        return ctv3Id;
    }

    public String getSnomedId() {
        return snomedId;
    }

    public String getIsPrimitive() {
        return isPrimitive;
    }

}
