/**
 * Copyright CSIRO Australian e-Health Research Centre (http://aehrc.com).
 * All rights reserved. Use is subject to license terms and conditions.
 */
package au.csiro.ontology.snomed.refset.rf2;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

/**
 * Contains information about modules and their dependencies.
 * 
 * @author Alejandro Metke
 *
 */
public class ModuleDependency {

    protected String id;
    protected String version;
    protected final Set<ModuleDependency> dependencies = new HashSet<>();
    
    /**
     * Creates a new module.
     * 
     * @param id
     * @param version
     */
    public ModuleDependency(String id, String version) {
        this.id = id;
        this.version = version;
    }

    public String getId() {
        return id;
    }

    public String getVersion() {
        return version;
    }

    public Collection<ModuleDependency> getDependencies() {
        return dependencies;
    }

}
