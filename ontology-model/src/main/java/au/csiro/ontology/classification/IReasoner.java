package au.csiro.ontology.classification;

import java.io.OutputStream;
import java.util.Iterator;
import java.util.Set;

import au.csiro.ontology.Taxonomy;

import au.csiro.ontology.IOntology;
import au.csiro.ontology.axioms.IAxiom;

/**
 * This interface represents the functionality of a reasoner. It uses the 
 * internal ontology model.
 * 
 * @author Alejandro Metke
 *
 */
@SuppressWarnings("deprecation")
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
     * Classifies the stated axioms in the supplied ontology. The first time 
     * this method is called it will perform a full classification. Subsequent 
     * calls will trigger incremental classifications.
     * 
     * @param ont An ontology.
     * @return IReasoner
     */
    public IReasoner<T> classify(IOntology<T> ont);
    
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
     * Returns an {@link IOntology} that represents the generated taxonomy. 
     * If no axioms have yet been classified it throws a 
     * {@link RuntimeException}.
     * 
     * @return The classified ontology.
     */
    public IOntology<T> getClassifiedOntology();
    
    /**
     * Saves this reasoner to the specified {@link OutputStream}.
     * 
     * @param out The {@link OutputStream}.
     */
    public void save(OutputStream out);
    
    /**
     * Returns the resulting {@link Taxonomy} after classification (or null if
     * the ontology has not been classified yet).
     * 
     * @return The taxonomy.
     * @deprecated Use getClassifiedOntology instead.
     */
    public Taxonomy<T> getTaxonomy();

}