/**
 * Copyright CSIRO Australian e-Health Research Centre (http://aehrc.com).
 * All rights reserved. Use is subject to license terms and conditions.
 */
package au.csiro.ontology;

import java.util.Collection;
import java.util.Map;
import java.util.Set;

import au.csiro.ontology.axioms.IAxiom;
import au.csiro.ontology.model.AbstractInfo;
import au.csiro.ontology.model.Concept;

/**
 * Represents an ontology in our internal format. Includes the DL
 * representation (a collection of axioms) in stated form with additional 
 * information about every concept (which is needed for retrieval but not for 
 * classification).
 * 
 * @author Alejandro Metke
 *
 */
public class Ontology<T extends Comparable<T>> implements IOntology<T> {
    
    /**
     * The collection of axioms that form the ontology.
     */
    protected final Collection<IAxiom> axioms;
    
    /**
     * A map that contains the additional information for every concept in the
     * ontology, indexed by id.
     */
    protected final Map<T, AbstractInfo> infoMap;
    
    /**
     * Builds a new ontology.
     * 
     * @param axioms The axioms in the ontology.
     * @param infoMap The additional information.
     */
    public Ontology(Collection<IAxiom> axioms, 
            Map<T, AbstractInfo> infoMap) {
        this.axioms = axioms;
        this.infoMap = infoMap;
    }
    
    /**
     * Builds a new ontology.
     * 
     * @param axioms The axioms in the ontology.
     */
    public Ontology(Collection<IAxiom> axioms) {
        this(axioms, null);
    }

    @Override
    public Collection<IAxiom> getAxioms(AxiomForm form) {
        // TODO: implement the different axiom forms
        return axioms;
    }

    @Override
    public AbstractInfo getConceptInfo(Concept<T> c) {
        return infoMap.get(c.getId());
    }

    @Override
    public Set<IAxiom> getDefiningAxioms(Concept<T> c, AxiomForm form) {
        // TODO Auto-generated method stub
        return null;
    }

}
