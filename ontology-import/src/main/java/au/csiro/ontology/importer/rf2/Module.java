/**
 * Copyright CSIRO Australian e-Health Research Centre (http://aehrc.com).
 * All rights reserved. Use is subject to license terms and conditions.
 */
package au.csiro.ontology.importer.rf2;

import java.util.HashMap;
import java.util.Map;

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
     * The collection of rows form the RF2 tables that define this module.
     */
    protected final Map<String, VersionRows> versions = new HashMap<>();
    
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
    public Map<String, VersionRows> getVersions() {
        return versions;
    }
    
}
