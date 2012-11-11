/**
 * Copyright CSIRO Australian e-Health Research Centre (http://aehrc.com).
 * All rights reserved. Use is subject to license terms and conditions.
 */
package au.csiro.ontology.importer.rf1;

/**
 * Represents a row in an RF1 descriptions file.
 * 
 * @author Alejandro Metke
 * 
 */
public class DescriptionRow {

    private final String descriptionId;
    private final String descriptionStatus;
    private final String conceptId;
    private final String term;
    private final String initialCapitalStatus;
    private final String descriptionType;
    private final String languageCode;
    
    /**
     * Creates a new DescriptionRow.
     * 
     * @param descriptionId
     * @param descriptionStatus
     * @param conceptId
     * @param term
     * @param initialCapitalStatus
     * @param descriptionType
     * @param languageCode
     */
    public DescriptionRow(String descriptionId, String descriptionStatus,
            String conceptId, String term, String initialCapitalStatus,
            String descriptionType, String languageCode) {
        super();
        this.descriptionId = descriptionId;
        this.descriptionStatus = descriptionStatus;
        this.conceptId = conceptId;
        this.term = term;
        this.initialCapitalStatus = initialCapitalStatus;
        this.descriptionType = descriptionType;
        this.languageCode = languageCode;
    }

    public String getDescriptionId() {
        return descriptionId;
    }

    public String getDescriptionStatus() {
        return descriptionStatus;
    }

    public String getConceptId() {
        return conceptId;
    }

    public String getTerm() {
        return term;
    }

    public String getInitialCapitalStatus() {
        return initialCapitalStatus;
    }

    public String getDescriptionType() {
        return descriptionType;
    }

    public String getLanguageCode() {
        return languageCode;
    }
    
}
