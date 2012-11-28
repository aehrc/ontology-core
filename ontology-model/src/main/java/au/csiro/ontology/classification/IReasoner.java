package au.csiro.ontology.classification;

import java.util.Iterator;
import java.util.Set;

import au.csiro.ontology.IOntology;
import au.csiro.ontology.axioms.IAxiom;

/**
 * This interface represents the functionality of a reasoner. It uses the 
 * internal ontology model.
 * 
 * @author Alejandro Metke
 *
 */
public interface IReasoner<T extends Comparable<T>> {
    
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
     * Classifies the supplied axioms. The first time this method is called it 
     * will perform a full classification. Subsequent calls will trigger
     * incremental classifications.
     * 
     * @param axioms An iterator for the axioms. Implementations must be able
     * to handle null values.
     * @return IReasoner
     */
    public IReasoner<T> classify(Iterator<IAxiom> axioms);
    
    /**
     * Removes all the state in the classifier except the taxonomy generated
     * after classification. If the classification process has not been run then
     * this method has no effect. Once pruned, it is no longer possible to run
     * an incremental classification. Doing so will generate a 
     * {@link RuntimeException}. It is also impossible to return the stated and
     * inferred axioms. Calling the getClassifiedOntology method requesting
     * any of these will also generate a {@link RuntimeException}.
     */
    public void prune();
    
    /**
     * Returns an {@link IOntology} that represents the current set of stated
     * axioms, classified axioms, and the generated taxonomy. If no axioms have
     * yet been classified it returns null.
     * 
     * @return The classified ontology.
     */
    public IOntology<T> getClassifiedOntology();
    
    /**
     * Returns an {@link IOntology} that represents the current set of stated
     * axioms, classified axioms, and the generated taxonomy. If no axioms have
     * yet been classified it returns null.
     * 
     * @param includeTaxonomy Indicates if the taxonomy should be included in 
     * the generated ontology.
     * @param includeStatedAxioms Indicates if the stated axioms should be
     * included in the generated ontology.
     * @param includeInferredAxioms Indicates if the inferred axioms should be
     * included in the generated ontology.
     * 
     * @return The classified ontology.
     */
    public IOntology<T> getClassifiedOntology(boolean includeTaxonomy, 
            boolean includeStatedAxioms, boolean includeInferredAxioms);

}