/**
 * Copyright CSIRO Australian e-Health Research Centre (http://aehrc.com).
 * All rights reserved. Use is subject to license terms and conditions.
 */
package au.csiro.ontology.importer.rf2;

import java.util.SortedMap;
import java.util.TreeMap;

/**
 * Represents an RF2 module.
 * 
 * @author Alejandro Metke
 *
 */
public class Module {
    
    /**
     * The id of this module.
     */
    protected final String id;
    
    /**
     * A collection of rows from the RF2 tables for each version of this module.
     * The map is indexed by version.
     */
    protected final SortedMap<String, VersionRows> versions = new TreeMap<>();
    
    /**
     * Creates a new module.
     * 
     * @param id
     */
    public Module(String id) {
        this.id = id;
    }

    /**
     * @return the id
     */
    public String getId() {
        return id;
    }

    /**
     * @return the versions
     */
    public SortedMap<String, VersionRows> getVersions() {
        return versions;
    }
    
}
