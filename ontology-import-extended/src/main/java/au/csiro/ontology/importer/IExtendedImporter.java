/**
 * Copyright CSIRO Australian e-Health Research Centre (http://aehrc.com).
 * All rights reserved. Use is subject to license terms and conditions.
 */
package au.csiro.ontology.importer;

import java.util.Map;

import au.csiro.ontology.IExtendedOntology;
import au.csiro.ontology.classification.IProgressMonitor;

/**
 * @author Alejandro Metke
 *
 */
public interface IExtendedImporter extends IImporter {
    /**
     * This method imports ontologies into the internal format and includes
     * additional information that is not needed for classification but is
     * useful in other scenarios, such as retrieval.
     */
    public Map<String, Map<String, IExtendedOntology<String>>> 
        getExtendedOntologyVersions(IProgressMonitor monitor);
}
