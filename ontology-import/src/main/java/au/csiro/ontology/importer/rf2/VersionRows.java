/**
 * Copyright CSIRO Australian e-Health Research Centre (http://aehrc.com).
 * All rights reserved. Use is subject to license terms and conditions.
 */
package au.csiro.ontology.importer.rf2;

import java.util.ArrayList;
import java.util.List;

/**
 * This class represents a collection of rows from the RF2 concepts,
 * descriptions, and relationships tables that correspond to a logical version.
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
     * Builds a new VersionRows.
     */
    public VersionRows() {

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
     * Merges another {@link VersionRows} into this.
     * 
     * @param other
     */
    public void merge(VersionRows other) {
        conceptRows.addAll(other.conceptRows);
        descriptionRows.addAll(other.descriptionRows);
        relationshipRows.addAll(other.relationshipRows);
    }

}
