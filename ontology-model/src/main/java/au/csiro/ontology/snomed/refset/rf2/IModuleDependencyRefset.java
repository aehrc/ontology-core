/**
 * Copyright CSIRO Australian e-Health Research Centre (http://aehrc.com).
 * All rights reserved. Use is subject to license terms and conditions.
 */
package au.csiro.ontology.snomed.refset.rf2;

import java.util.Map;

/**
 * @author Alejandro Metke
 *
 */
public interface IModuleDependencyRefset extends IRefset {
    
    /**
     * Returns a {@link Map} of root module ids and a {@link Map} of 
     * {@link IModule}s indexed by version. Each {@link IModule} represents a 
     * versioned module and its dependencies. Every module can be seen as a root
     * module.
     * 
     * @return
     */
    public Map<String, Map<String, ModuleDependency>> getModuleDependencies();
    
}
