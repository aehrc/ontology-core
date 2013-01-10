package au.csiro.ontology;

import java.util.Collection;
import java.util.Iterator;
import java.util.Map;

import au.csiro.ontology.axioms.IAxiom;

/**
 * Defines the methods of an ontology.
 * 
 * @author Alejandro Metke
 *
 */
public interface IOntology<T extends Comparable<T>> {
    
    /**
     * Returns the {@link Collection} of axioms in the ontology.
     * 
     * @return The axioms.
     */
    public Collection<IAxiom> getStatedAxioms();
    
    /**
     * Returns the {@link Node} in the taxonomy that contains a specified 
     * concept or null if such {@link Node} does not exist.
     * 
     * @param id The concept's id.
     * @return The node.
     */
    public Node<T> getNode(T id);
    
    /**
     * Returns the {@link Node} in the taxonomy that corresponds to top.
     * 
     * @return The top node.
     */
    public Node<T> getTopNode();
    
    /**
     * Returns the {@link Node} in the taxonomy that correspond to bottom.
     * 
     * @return The bottom node.
     */
    public Node<T> getBottomNode();
    
    /**
     * Returns an {@link Iterator} for all the nodes in the taxonomy or null if
     * the ontology has not been classified.
     * 
     * @return The iterator.
     */
    public Iterator<Node<T>> nodeIterator();
    
    /**
     * Returns the taxonomy represented by a {@link Map} of keys to 
     * {@link Node}s.
     * 
     * @return The taxonomy.
     */
    public Map<T, Node<T>> getNodeMap();
    
    /**
     * Sets the taxonomy represented by a {@link Map} of keys to 
     * {@link Node}s.
     * 
     * @param nodeMap The taxonomy.
     */
    public void setNodeMap(Map<T, Node<T>> nodeMap);

}