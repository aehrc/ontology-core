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
 * This class repesents a module dependeny reference set.
 * 
 * @author Alejandro Metke
 *
 */
public class ModuleDependencyRefset extends Refset implements
        IModuleDependencyRefset {
    
    protected final Map<String, Map<String, ModuleDependency>> dependencies = 
            new HashMap<String, Map<String, ModuleDependency>>();
            
    /**
     * Creates a new module dependency reference set.
     * 
     * @param members
     */
    public ModuleDependencyRefset(Set<ModuleDependencyRow> members) {
        // Index the dependency rows
        Map<String, Set<String>> index = new HashMap<String, Set<String>>();
        for(ModuleDependencyRow member : members) {
            String key = member.getModuleId() + "_" + 
                    member.getSourceEffectiveTime();
            String val = member.getReferencedComponentId() + "_" + 
                    member.getTargetEffectiveTime();
            
            Set<String> vals = index.get(key);
            if(vals == null) {
                vals = new HashSet<String>();
                index.put(key, vals);
            }
            vals.add(val);
        }
        
        for(String src : index.keySet()) {
            String[] parts = src.split("[_]");
            String srcId = parts[0];
            String srcVer = parts[1];

            ModuleDependency md = createDependency(src, index);
            Map<String, ModuleDependency> verDepMap = dependencies.get(srcId);
            if(verDepMap == null) {
                verDepMap = new HashMap<String, ModuleDependency>();
                dependencies.put(srcId, verDepMap);
            }
            
            verDepMap.put(srcVer, md);
        }
    }
    
    private ModuleDependency createDependency(String key, 
            Map<String, Set<String>> index) {
        String[] parts = key.split("[_]");
        ModuleDependency md = new ModuleDependency(parts[0], parts[1]);
        
        Set<String> deps = index.get(key);
        if(deps != null) {
            for(String dep : deps) {
                ModuleDependency nmd = createDependency(dep, index);
                md.getDependencies().add(nmd);
            }
        }
        
        return md;
    }

    public Map<String, Map<String, ModuleDependency>> getModuleDependencies() {
        return dependencies;
    }

}
