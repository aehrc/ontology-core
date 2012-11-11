/**
 * Copyright CSIRO Australian e-Health Research Centre (http://aehrc.com).
 * All rights reserved. Use is subject to license terms and conditions.
 */
package au.csiro.ontology.snomed.refset.rf2;

import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * @author Alejandro Metke
 *
 */
public class ModuleDependencyRefset extends Refset implements
        IModuleDependencyRefset {

    /**
     * Creates a new module dependency refset.
     * 
     * @param id
     * @param displayName
     */
    public ModuleDependencyRefset(String id, String displayName) {
        super(id, displayName);
    }

    @Override
    public Map<String, Collection<IModule>> getModuleDependencies() {
        Map<String, Map<String, Set<IModule>>> map = new HashMap<>();
        Set<String> dependencies = new HashSet<>();
        Set<String> all = new HashSet<>();
        for(IRefsetMember im : members) {
            IModuleDependencyRefsetMember rf = 
                    (IModuleDependencyRefsetMember)im;
            String moduleId = rf.getModuleId();
            String sourceEffectiveTime = rf.getSourceEffectiveTime();
            String referencedComponentId = rf.getReferencedComponentId();
            String targetEffectiveTime = rf.getTargetEffectiveTime();
            
            all.add(moduleId);
            all.add(referencedComponentId);
            dependencies.add(referencedComponentId);
            
            Map<String, Set<IModule>> m1 = map.get(moduleId);
            if(m1 == null) {
                m1 = new HashMap<>();
                map.put(moduleId, m1);
            }
            
            Set<IModule> m2 = m1.get(sourceEffectiveTime);
            if(m2 == null) {
                m2 = new HashSet<>();
                m1.put(sourceEffectiveTime, m2);
            }
            
            m2.add(new Module(referencedComponentId, targetEffectiveTime));
        }
        
        // The root modules are the ones that are not dependencies of other 
        // modules
        all.removeAll(dependencies);
        
        if(all.isEmpty()) {
            throw new RuntimeException("Found cyclic dependencies in modules");
        }
        
        Map<String, Collection<IModule>> res = new HashMap<>();
        
        // Build the collection of module dependencies
        for(String id : all) {
            Map<String, Set<IModule>> versions = map.get(id);
            for(String version : versions.keySet()) {
                IModule m = new Module(id, version);
                Set<IModule> deps = versions.get(version);
                m.getDependencies().addAll(deps);
                Collection<IModule> c = res.get(id);
                if(c == null) {
                    c = new HashSet<>();
                    res.put(id, c);
                }
                c.add(m);
            }
        }
        return res;
    }

}
