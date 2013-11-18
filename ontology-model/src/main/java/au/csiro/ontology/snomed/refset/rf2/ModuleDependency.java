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

    final protected String id;
    final protected String version;
    final protected Set<ModuleDependency> dependencies = new HashSet<ModuleDependency>();

    /**
     * Creates a new module.
     *
     * @param id not null
     * @param version not null
     */
    public ModuleDependency(String id, String version) {
        assert id != null;
        assert version != null;

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

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + id.hashCode();
        result = prime * result + version.hashCode();
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        final ModuleDependency other = (ModuleDependency) obj;
        return id.equals(other.id) && version.equals(other.version);
    }

}
