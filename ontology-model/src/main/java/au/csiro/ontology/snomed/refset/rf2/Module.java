/**
 * Copyright CSIRO Australian e-Health Research Centre (http://aehrc.com).
 * All rights reserved. Use is subject to license terms and conditions.
 */
package au.csiro.ontology.snomed.refset.rf2;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

/**
 * Simple module implementation.
 * 
 * @author Alejandro Metke
 *
 */
public class Module implements IModule {

    protected String id;
    protected String version;
    protected final Set<IModule> dependencies = new HashSet<>();
    
    /**
     * Creates a new module.
     * 
     * @param id
     * @param version
     */
    public Module(String id, String version) {
        this.id = id;
        this.version = version;
    }

    @Override
    public String getId() {
        return id;
    }

    @Override
    public String getVersion() {
        return version;
    }

    @Override
    public Collection<IModule> getDependencies() {
        return dependencies;
    }

}
