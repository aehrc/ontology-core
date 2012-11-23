/**
 * Copyright CSIRO Australian e-Health Research Centre (http://aehrc.com).
 * All rights reserved. Use is subject to license terms and conditions.
 */
package au.csiro.ontology;

import java.util.Collection;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import au.csiro.ontology.axioms.IAxiom;
import au.csiro.ontology.axioms.IConceptInclusion;
import au.csiro.ontology.model.AbstractInfo;
import au.csiro.ontology.model.Concept;
import au.csiro.ontology.model.IConcept;

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
     * The collection of stated axioms that form the ontology.
     */
    protected final Collection<IAxiom> statedAxioms;
    
    /**
     * The collection of inferred axioms in distribution normal form.
     */
    protected final Collection<IAxiom> inferredAxioms;
    
    /**
     * Builds a new ontology.
     * 
     * @param axioms The axioms in the ontology.
     * @param inferredAxioms The axioms in the ontology after classification in 
     * DNF.
     * @param infoMap The additional information.
     */
    public Ontology(Collection<IAxiom> statedAxioms, 
            Collection<IAxiom> inferredAxioms,
            Map<T, AbstractInfo> infoMap) {
        this.statedAxioms = statedAxioms;
        this.inferredAxioms = inferredAxioms;
    }
    
    /**
     * Builds a new ontology.
     * 
     * @param axioms The axioms in the ontology.
     * @param inferredAxioms The axioms in the ontology after classification in 
     * DNF.
     * @param inferredAxioms
     */
    public Ontology(Collection<IAxiom> statedAxioms, 
            Collection<IAxiom> inferredAxioms) {
        this(statedAxioms, inferredAxioms, null);
    }
    
    /**
     * Builds a new ontology.
     * 
     * @param axioms The axioms in the ontology.
     */
    public Ontology(Collection<IAxiom> statedAxioms) {
        this(statedAxioms, null, null);
    }

    @Override
    public Collection<IAxiom> getAxioms(AxiomForm form) {
        if(form == AxiomForm.STATED) {
            return statedAxioms;
        } else if(form == AxiomForm.INFERRED) {
            return inferredAxioms;
        } else {
            throw new RuntimeException("Unknown axiom form "+form);
        }
    }

    @Override
    public Set<IAxiom> getDefiningAxioms(Concept<T> c, AxiomForm form) {
        if(form == AxiomForm.STATED) {
            return findDefiningAxioms(statedAxioms, c);
        } else if(form == AxiomForm.INFERRED) {
            return findDefiningAxioms(inferredAxioms, c);
        } else {
            throw new RuntimeException("Unknown axiom form "+form);
        }
    }
    
    private Set<IAxiom> findDefiningAxioms(Collection<IAxiom> axioms, 
            Concept<T> concept) {
        Set<IAxiom> res = new HashSet<>();
        for(IAxiom axiom : axioms) {
            if(axiom instanceof IConceptInclusion) {
                IConceptInclusion ci = (IConceptInclusion)axiom;
                IConcept lhs = ci.lhs();
                IConcept rhs = ci.rhs();
                
                if(concept.equals(lhs) || concept.equals(rhs)) {
                    res.add(axiom);
                }
            }
        }
        return res;
    }

}
