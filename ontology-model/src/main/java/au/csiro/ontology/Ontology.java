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
public class Ontology implements IOntology {
    
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
     * The collection of inferred axioms that form the ontology.
     */
    protected final Collection<IAxiom> inferredAxioms = new ArrayList<IAxiom>();
    
    /**
     * A map that contains references to all the nodes in the taxonomy indexed
     * by id.
     */
    protected final Map<String, Node> nodeMap = new HashMap<String, Node>();
    
    /**
     * Set of {@link Node}s potentially affected by the last incremental
     * classification.
     */
    protected final Set<Node> lastAffectedNodes = new HashSet<Node>();
    
    /**
     * Builds a new ontology.
     * 
     * @param id
     * @param version
     * @param statedAxioms
     * @param nodeMap
     * @param lastAffectedNodes
     */
    public Ontology(String id, String version, 
            Collection<IAxiom> statedAxioms, Map<String, Node> nodeMap, 
            Set<Node> lastAffectedNodes) {
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
            Collection<IAxiom> statedAxioms, Map<String, Node> nodeMap) {
    	this(id, version, statedAxioms, nodeMap, null);
    }
    
    @Override
    public Collection<IAxiom> getStatedAxioms() {
        return statedAxioms;
    }

    @Override
    public Collection<IAxiom> getInferredAxioms() {
        return inferredAxioms;
    }
    
    @Override
    public Node getNode(String id) {
        return nodeMap.get(id);
    }
    
    @Override
    public Iterator<Node> nodeIterator() {
        Set<Node> set = new HashSet<Node>(nodeMap.values());
        return set.iterator();
    }
    
    @Override
    public Map<String, Node> getNodeMap() {
        return nodeMap;
    }

    @Override
    public Node getTopNode() {
        return getNode(Concept.TOP);
    }
    
    @Override
    public Node getBottomNode() {
        return getNode(Concept.BOTTOM);
    }
    
    @Override
    public void setNodeMap(Map<String, Node> nodeMap) {
        this.nodeMap.clear();
        this.nodeMap.putAll(nodeMap);
    }
    
    @Override
    public Set<Node> getAffectedNodes() {
        return lastAffectedNodes;
    }
    
    @Override
    public void setAffectedNodes(Set<Node> nodes) {
        lastAffectedNodes.clear();
        lastAffectedNodes.addAll(nodes);
    }
    
    @Override
    public String getId() {
        return id;
    }

    @Override
    public String getVersion() {
        return version;
    }
    
}
