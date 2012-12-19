package au.csiro.ontology;

import java.util.Collection;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import au.csiro.ontology.axioms.IAxiom;
import au.csiro.ontology.model.Concept;

/**
 * Defines the methods of an ontology.
 * 
 * @author Alejandro Metke
 *
 */
public interface IOntology<T extends Comparable<T>> {
    
    public enum AxiomForm {STATED, INFERRED}
    
    /**
     * Returns the {@link Collection} of axioms in the ontology.
     * 
     * @return The axioms.
     */
    public Collection<IAxiom> getAxioms(AxiomForm form);
    
    /**
     * Returns a {@link Set} of {@link IAxiom}s that define this concept.
     * This includes the axioms of the form c [ x, x [ c, and c = x, where c is
     * a named concept and x is an abstract concept (which means it can be
     * either a named concept or a complex expression).
     * 
     * @param c A named concept.
     * @param form Indicates if the axioms requested are the stated or the 
     * inferred ones.
     * @return The set of axioms that define the concept.
     */
    public Set<IAxiom> getDefiningAxioms(Concept<T> c, AxiomForm form);
    
    /**
     * Returns a {@link Set} of {@link IAxiom}s that define this concept.
     * This includes the axioms of the form c [ x, x [ c, and c = x, where c is
     * a named concept and x is an abstract concept (which means it can be
     * either a named concept or a complex expression).
     * 
     * @param c The id of a named concept.
     * @param form Indicates if the axioms requested are the stated or the 
     * inferred ones.
     * @return The set of axioms that define the concept.
     */
    public Set<IAxiom> getDefiningAxioms(T c, AxiomForm form);
    
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

}