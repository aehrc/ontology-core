package au.csiro.ontology;

import java.util.Collection;
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
     * @return The set of axioms that define the concept.
     */
    public Set<IAxiom> getDefiningAxioms(Concept<T> c, AxiomForm form);

}