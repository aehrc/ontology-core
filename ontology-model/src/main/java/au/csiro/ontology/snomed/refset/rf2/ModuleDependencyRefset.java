/**
 * Copyright CSIRO Australian e-Health Research Centre (http://aehrc.com).
 * All rights reserved. Use is subject to license terms and conditions.
 */
package au.csiro.ontology.snomed.refset.rf2;

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
    
    protected final Map<String, Map<String, ModuleDependency>> dependencies = 
            new HashMap<>();
    
    /**
     * Creates a new module dependency reference set.
     * 
     * @param id
     * @param displayName
     */
    public ModuleDependencyRefset(Set<ModuleDependencyRow> members) {
        Map<String, Map<String, Set<ModuleDependency>>> map = new HashMap<>();
        
        Set<String> all = new HashSet<>();
        for(ModuleDependencyRow rf : members) {
            String moduleId = rf.getModuleId();
            String sourceEffectiveTime = rf.getSourceEffectiveTime();
            String referencedComponentId = rf.getReferencedComponentId();
            String targetEffectiveTime = rf.getTargetEffectiveTime();
            
            all.add(moduleId);
            all.add(referencedComponentId);
            //dependencies.add(referencedComponentId);
            
            Map<String, Set<ModuleDependency>> m1 = map.get(moduleId);
            if(m1 == null) {
                m1 = new HashMap<>();
                map.put(moduleId, m1);
            }
            
            Set<ModuleDependency> m2 = m1.get(sourceEffectiveTime);
            if(m2 == null) {
                m2 = new HashSet<>();
                m1.put(sourceEffectiveTime, m2);
            }
            
            m2.add(new ModuleDependency(referencedComponentId, targetEffectiveTime));
        }
        
        // Build the collection of module dependencies
        for(String id : all) {
            Map<String, Set<ModuleDependency>> versions = map.get(id);
            if(versions != null) {
                for(String version : versions.keySet()) {
                    ModuleDependency m = new ModuleDependency(id, version);
                    Set<ModuleDependency> deps = versions.get(version);
                    m.getDependencies().addAll(deps);
                    Map<String, ModuleDependency> c = dependencies.get(id);
                    if(c == null) {
                        c = new HashMap<>();
                        dependencies.put(id, c);
                    }
                    c.put(version, m);
                }
            }  
        }
    }

    @Override
    public Map<String, Map<String, ModuleDependency>> getModuleDependencies() {
        return dependencies;
    }

}
