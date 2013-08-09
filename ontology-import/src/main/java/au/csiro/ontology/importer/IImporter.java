/**
 * Copyright CSIRO Australian e-Health Research Centre (http://aehrc.com).
 * All rights reserved. Use is subject to license terms and conditions.
 */
package au.csiro.ontology.importer;

import java.util.Iterator;
import java.util.List;

import au.csiro.ontology.Ontology;
import au.csiro.ontology.util.IProgressMonitor;

/**
 * Defines the methods that must be implemented by classes used to import
 * ontologies defined in external formats into our internal format.
 * 
 * @author Alejandro Metke
 * 
 */
public interface IImporter {

    /**
     * <p>There are several models used to represent ontologies that have been adopted by different groups. In this 
     * implementation a model based on SNOMED's RF2 format is used because it contains features that are not available 
     * in other formats.</p>
     * 
     * <p>An ontology in RF2 maps to a set of interdependent modules. This method returns all the ontologies found in a 
     * set of files. Implementations of importers for other file formats can simply return a single ontology.</p> 
     * 
     * <p>If the files contain related modules then all of them are returned as a single ontology. This method returns 
     * an iterator that allows importing each ontology version.</p>
     * 
     * @param monitor A progress monitor.
     * 
     * @return An interator that allows importing one ontology version at a time.
     * 
     * @throws ImportException if a problem occurs when importing the ontologies.
     */
    public Iterator<Ontology> getOntologyVersions(IProgressMonitor monitor) throws ImportException;
    
    /**
     * Returns a list of problems that happened during the import process. This method should be called after an 
     * {@link ImportException} is thrown to get a detailed list of problems.
     * 
     * @return
     */
    public List<String> getProblems();

}
