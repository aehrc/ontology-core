/**
 * Copyright CSIRO Australian e-Health Research Centre (http://aehrc.com).
 * All rights reserved. Use is subject to license terms and conditions.
 */
package au.csiro.ontology.importer;

import java.util.Map;

import au.csiro.ontology.IOntology;
import au.csiro.ontology.classification.IProgressMonitor;

/**
 * Defines the methods that must be implemented by classes used to import
 * ontologies defined in external formats into our internal format.
 * 
 * @author Alejandro Metke
 * 
 */
public interface IImporter {

    /**
     * <p>There are several models used to represent ontologies that have been
     * adopted by different groups. In this implementation a model based on
     * SNOMED's RF2 format is used because it contains features that are not 
     * available in other formats.</p>
     * 
     * <p>An ontology in RF2 maps to a set of interdependent modules. This 
     * method returns all the ontologies found in a set of files. 
     * Implementations of importers for other file formats can simply return a
     * single ontology.</p> 
     * 
     * <p>If the files contain related modules then all of them are returned as 
     * a single ontology. This method returns a map of maps. The first key 
     * refers to the ontology URI. For example, if importing SNOMED CT 
     * international, this key would be "snomed/sct/900000000000207008". The 
     * keys of the second map are the versions of this ontology. If importing a 
     * full release of SNOMED, then this map would have one key for each SNOMED 
     * release. </p>
     * 
     * <p>If importing an ontology from OWL, where versions and modules are not
     * natively supported, the first key will contain the ontology URI and the
     * second key will contain the date when the import process occurred.</p>
     * 
     * @return A map of maps with ontologies represented as sets of axioms in 
     * the internal format.
     */
    public Map<String, Map<String, IOntology<String>>> getOntologyVersions(
            IProgressMonitor monitor);

}
