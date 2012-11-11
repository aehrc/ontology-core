package au.csiro.ontology.classification;

import java.util.Set;

import au.csiro.ontology.Taxonomy;
import au.csiro.ontology.axioms.IAxiom;

/**
 * This interface represents the functionality of a reasoner. It uses the 
 * internal ontology model.
 * 
 * @author Alejandro Metke
 *
 */
public interface IReasoner<T> {
    
    /**
     * Performs a full classification and returns an instance of the reasoner
     * with the classification results.
     * 
     * @param axioms The axioms in the base ontology.
     * @return IReasoner
     */
    public IReasoner<T> classify(Set<IAxiom> axioms);

    /**
     * Performs an incremental classification and returns an instance of the
     * reasoner with the new state.
     * 
     * @param axioms The incremental axioms.
     * @return IReasoner
     */
    public IReasoner<T> classifyIncremental(Set<IAxiom> axioms);
    
    /**
     * Removes all the state in the classifier except the taxonomy generated
     * after classification. If the classification process has not been run then
     * this method has no effect. Once pruned, it is no longer possible to run
     * an incremental classification. Doing so will generate a 
     * {@link RuntimeException}.
     */
    public void prune();

    /**
     * Returns the resulting {@link Taxonomy} after classification (or null if
     * the ontology has not been classified yet).
     * 
     * @return The taxonomy.
     */
    public Taxonomy<T> getTaxonomy();

}