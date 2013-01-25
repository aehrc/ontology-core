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
     * The collection of stated axioms that form the ontology.
     */
    protected final Collection<IAxiom> statedAxioms;
    
    /**
     * A map that contains references to all the nodes in the taxonomy indexed
     * by id.
     */
    protected final Map<T, Node<T>> nodeMap = new HashMap<>();
    
    /**
     * Set of {@link Node}s pontentially affected by the last incremental
     * classification.
     */
    protected final Set<Node<T>> lastAffectedNodes = new HashSet<>();
    
    /**
     * Builds a new ontology.
     * 
     * @param statedAxioms
     * @param nodeMap
     * @param lastAffectedNodes
     */
    public Ontology(Collection<IAxiom> statedAxioms, Map<T, Node<T>> nodeMap, 
    		Set<Node<T>> lastAffectedNodes) {
        if(statedAxioms == null) {
            this.statedAxioms = new ArrayList<>();
        } else {
            this.statedAxioms = statedAxioms;
        }
        if(nodeMap != null)
        	this.nodeMap.putAll(nodeMap);
        if(lastAffectedNodes != null)
        	this.lastAffectedNodes.addAll(lastAffectedNodes);
    }
    
    /**
     * Builds a new ontology.
     * 
     * @param statedAxioms
     * @param nodeMap
     */
    public Ontology(Collection<IAxiom> statedAxioms, Map<T, Node<T>> nodeMap) {
    	this(statedAxioms, nodeMap, null);
    }

    @Override
    public Collection<IAxiom> getStatedAxioms() {
        return statedAxioms;
    }
    
    @Override
    public Node<T> getNode(T id) {
        return nodeMap.get(id);
    }
    
    @Override
    public Iterator<Node<T>> nodeIterator() {
        Set<Node<T>> set = new HashSet<>(nodeMap.values());
        return set.iterator();
    }

    @Override
    public Map<T, Node<T>> getNodeMap() {
        return nodeMap;
    }

    @SuppressWarnings("unchecked")
    @Override
    public Node<T> getTopNode() {
        return getNode((T)Concept.TOP);
    }

    @SuppressWarnings("unchecked")
    @Override
    public Node<T> getBottomNode() {
        return getNode((T)Concept.BOTTOM);
    }

    @Override
    public void setNodeMap(Map<T, Node<T>> nodeMap) {
        this.nodeMap.clear();
        this.nodeMap.putAll(nodeMap);
    }

	@Override
	public Set<Node<T>> getAffectedNodes() {
		return lastAffectedNodes;
	}

	@Override
	public void setAffectedNodes(Set<Node<T>> nodes) {
		lastAffectedNodes.clear();
		lastAffectedNodes.addAll(nodes);
	}
    
}
