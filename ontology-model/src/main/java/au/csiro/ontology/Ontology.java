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
     * The id of the ontology.
     */
    protected final String id;
    
    /**
     * The version of the ontology.
     */
    protected final String version;
    
    /**
     * The collection of stated axioms that form the ontology.
     */
    protected final Collection<IAxiom> statedAxioms;
    
    /**
     * A map that contains references to all the nodes in the taxonomy indexed
     * by id.
     */
    protected final Map<T, Node<T>> nodeMap = new HashMap<T, Node<T>>();
    
    /**
     * Set of {@link Node}s pontentially affected by the last incremental
     * classification.
     */
    protected final Set<Node<T>> lastAffectedNodes = new HashSet<Node<T>>();
    
    /**
     * Builds a new ontology.
     * 
     * @param statedAxioms
     * @param nodeMap
     * @param lastAffectedNodes
     */
    public Ontology(String id, String version, 
            Collection<IAxiom> statedAxioms, Map<T, Node<T>> nodeMap, 
            Set<Node<T>> lastAffectedNodes) {
        this.id = id;
        this.version = version;
        if(statedAxioms == null) {
            this.statedAxioms = new ArrayList<IAxiom>();
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
    public Ontology(String id, String version, 
            Collection<IAxiom> statedAxioms, Map<T, Node<T>> nodeMap) {
    	this(id, version, statedAxioms, nodeMap, null);
    }

    public Collection<IAxiom> getStatedAxioms() {
        return statedAxioms;
    }
    
    public Node<T> getNode(T id) {
        return nodeMap.get(id);
    }
    
    public Iterator<Node<T>> nodeIterator() {
        Set<Node<T>> set = new HashSet<Node<T>>(nodeMap.values());
        return set.iterator();
    }

    public Map<T, Node<T>> getNodeMap() {
        return nodeMap;
    }

    @SuppressWarnings("unchecked")
    public Node<T> getTopNode() {
        return getNode((T)Concept.TOP);
    }

    @SuppressWarnings("unchecked")
    public Node<T> getBottomNode() {
        return getNode((T)Concept.BOTTOM);
    }

    public void setNodeMap(Map<T, Node<T>> nodeMap) {
        this.nodeMap.clear();
        this.nodeMap.putAll(nodeMap);
    }

    public Set<Node<T>> getAffectedNodes() {
        return lastAffectedNodes;
    }

    public void setAffectedNodes(Set<Node<T>> nodes) {
        lastAffectedNodes.clear();
        lastAffectedNodes.addAll(nodes);
    }

    public String getId() {
        return id;
    }

    public String getVersion() {
        return version;
    }
    
}
