/**
 * Copyright CSIRO Australian e-Health Research Centre (http://aehrc.com).
 * All rights reserved. Use is subject to license terms and conditions.
 */
package au.csiro.ontology;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import au.csiro.ontology.axioms.IAxiom;
import au.csiro.ontology.axioms.IConceptInclusion;
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
     * A map that contains references to all the nodes in the taxonomy indexed
     * by id.
     */
    protected final Map<T, Node<T>> nodeMap;
    
    /**
     * Builds a new ontology.
     * 
     * @param statedAxioms The stated axioms in the ontology.
     * @param inferredAxioms The axioms in the ontology after classification in 
     * DNF.
     * @param infoMap The additional information.
     */
    public Ontology(Collection<IAxiom> statedAxioms, 
            Collection<IAxiom> inferredAxioms, Map<T, Node<T>> nodeMap) {
        if(statedAxioms == null) {
            this.statedAxioms = new ArrayList<>();
        } else {
            this.statedAxioms = statedAxioms;
        }
        if(inferredAxioms == null) {
            this.inferredAxioms = new ArrayList<>();
        } else {
            this.inferredAxioms = inferredAxioms;
        }
        if(nodeMap == null) {
            this.nodeMap = new HashMap<>();
        } else {
            this.nodeMap = nodeMap;
        }
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
            if(statedAxioms == null) return null;
            return findDefiningAxioms(statedAxioms, c);
        } else if(form == AxiomForm.INFERRED) {
            if(inferredAxioms == null) return null;
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
    
    @Override
    public Node<T> getNode(T id) {
        return nodeMap.get(id);
    }
    
    @Override
    public Iterator<Node<T>> nodeIterator() {
        return nodeMap.values().iterator();
    }

    @Override
    public Map<T, Node<T>> getNodeMap() {
        return nodeMap;
    }

    @Override
    public Set<IAxiom> getDefiningAxioms(T c,
            au.csiro.ontology.IOntology.AxiomForm form) {
        return getDefiningAxioms(new Concept<T>(c), form);
    }
    
}
