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
     * Classifies the supplied axioms. The first time this method is called it 
     * will perform a full classification. Subsequent calls will trigger
     * incremental classifications.
     * 
     * @param axioms The axioms in the base ontology.
     * @return IReasoner
     */
    public IReasoner<T> classify(Set<IAxiom> axioms);
    
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