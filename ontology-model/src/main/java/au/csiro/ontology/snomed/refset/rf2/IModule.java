/**
 * Copyright CSIRO Australian e-Health Research Centre (http://aehrc.com).
 * All rights reserved. Use is subject to license terms and conditions.
 */
package au.csiro.ontology.snomed.refset.rf2;

import java.util.Collection;

/**
 * A module.
 * 
 * @author Alejandro Metke
 *
 */
public interface IModule {
    
    /**
     * Returns the module id.
     * 
     * @return
     */
    public String getId();
    
    /**
     * Returns the module version.
     * 
     * @return
     */
    public String getVersion();
    
    /**
     * Returns the dependencies of the module.
     * @return
     */
    public Collection<IModule> getDependencies();
}
