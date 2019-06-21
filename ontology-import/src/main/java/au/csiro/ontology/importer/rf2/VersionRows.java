/**
 * Copyright CSIRO Australian e-Health Research Centre (http://aehrc.com).
 * All rights reserved. Use is subject to license terms and conditions.
 */
package au.csiro.ontology.importer.rf2;

import java.util.ArrayList;
import java.util.Collection;

import au.csiro.ontology.snomed.refset.rf2.RefsetRow;

/**
 * This class represents a collection of rows from the RF2 concepts,
 * owlAxioms, relationships, and concrete domain tables that correspond
 * to a logical stated version.
 *
 * @author Alejandro Metke
 *
 */
public class VersionRows {

	/**
	 * The concept rows in this version.
	 */
	protected final Collection<ConceptRow> conceptRows;

        /**
         * The inferred relationship rows in this version.
         */
        protected final Collection<RelationshipRow> inferredRelationshipRows;

	/**
	 * The stated relationship rows in this version.
	 */
	protected final Collection<RelationshipRow> statedRelationshipRows;

	/**
	 * The concrete domains reference set rows in this version.
	 */
	protected final Collection<RefsetRow> concreteDomainRows;

        /**
         * The attribute domains reference set rows in this version.
         */
        protected final Collection<RefsetRow> attributeDomainRows;

	/**
	 * The OWL reference set rows in this version.
	 */
	protected final Collection<RefsetRow> owlRows;

	/**
	 * Builds a new VersionRows.
	 */
	public VersionRows() {
		this(new ArrayList<ConceptRow>(), new ArrayList<RelationshipRow>(), new ArrayList<RelationshipRow>(), new ArrayList<RefsetRow>(), new ArrayList<RefsetRow>(), new ArrayList<RefsetRow>());
	}

	public VersionRows(Collection<ConceptRow> conceptRows, Collection<RelationshipRow> inferredRelationshipRows, Collection<RelationshipRow> statedRelationshipRows,
			Collection<RefsetRow> concreteDomainRows, Collection<RefsetRow> attributeDomainRows,
			Collection<RefsetRow> owlRows) {
		this.conceptRows = conceptRows;
                this.inferredRelationshipRows = inferredRelationshipRows;
		this.statedRelationshipRows = statedRelationshipRows;
		this.concreteDomainRows = concreteDomainRows;
                this.attributeDomainRows = attributeDomainRows;
		this.owlRows = owlRows;
	}

	/**
	 * @return the conceptRows
	 */
	public Collection<ConceptRow> getConceptRows() {
		return conceptRows;
	}

        /**
         * @return the inferredRelationshipRows
         */
        public Collection<RelationshipRow> getInferredRelationshipRows() {
                return inferredRelationshipRows;
        }

	/**
	 * @return the statedRelationshipRows
	 */
	public Collection<RelationshipRow> getStatedRelationshipRows() {
		return statedRelationshipRows;
	}

	/**
	 *
	 * @return the concrete domain refsetRows (includes current inactive rows)
	 */
	public Collection<RefsetRow> getConcreteDomainRows() {
		return concreteDomainRows;
	}

        /**
         * The attributeDomainRows are part of the MRCM and indicate which attributes are always / never grouped.
         *
         * @return the attribute domain refsetRows (includes current inactive rows)
         */
        public Collection<RefsetRow> getAttributeDomainRows() {
                return attributeDomainRows;
        }

	/**
	 *
	 * @return the owl refsetRows (includes current inactive rows)
	 */
	public Collection<RefsetRow> getOwlRows() {
		return owlRows;
	}

	/**
	 * Merges another {@link VersionRows} into this.
	 *
	 * @param other
	 */
	public void merge(VersionRows other) {
		conceptRows.addAll(other.conceptRows);
                inferredRelationshipRows.addAll(other.inferredRelationshipRows);
		statedRelationshipRows.addAll(other.statedRelationshipRows);
		concreteDomainRows.addAll(other.getConcreteDomainRows());
                attributeDomainRows.addAll(other.getAttributeDomainRows());
		owlRows.addAll(other.getConcreteDomainRows());
	}

}
