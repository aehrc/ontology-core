/**
 * Copyright CSIRO Australian e-Health Research Centre (http://aehrc.com).
 * All rights reserved. Use is subject to license terms and conditions.
 */
package au.csiro.ontology.importer.rf1;

import java.util.ArrayList;
import java.util.List;

/**
 * This class represents a collection of rows from the RF1 concepts,
 * descriptions, and relationships tables that correspond to a logical version.
 * The version number is assigned by the user.
 * 
 * @author Alejandro Metke
 * 
 */
public class VersionRows {

    /**
     * The concept rows in this version.
     */
    protected final List<ConceptRow> conceptRows = new ArrayList<>();

    /**
     * The description rows in this version.
     */
    protected final List<DescriptionRow> descriptionRows = new ArrayList<>();

    /**
     * The relationship rows in this version.
     */
    protected final List<RelationshipRow> relationshipRows = new ArrayList<>();

    /**
     * The name of this version.
     */
    private String versionName;

    /**
     * Builds a new VersionRows.
     */
    public VersionRows(String versionName) {
        this.versionName = versionName;
    }

    /**
     * @return the conceptRows
     */
    public List<ConceptRow> getConceptRows() {
        return conceptRows;
    }

    /**
     * @return the descriptionRows
     */
    public List<DescriptionRow> getDescriptionRows() {
        return descriptionRows;
    }

    /**
     * @return the relationshipRows
     */
    public List<RelationshipRow> getRelationshipRows() {
        return relationshipRows;
    }

    /**
     * Returns the name of this version.
     * 
     * @return
     */
    public String getVersionName() {
        return versionName;
    }

    /**
     * Sets the name of this version.
     * 
     * @param versionName
     */
    public void setVersionName(String versionName) {
        this.versionName = versionName;
    }

}
