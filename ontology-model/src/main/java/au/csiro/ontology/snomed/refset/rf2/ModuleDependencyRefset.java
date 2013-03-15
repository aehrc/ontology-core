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
    
    protected final Set<IRefsetMember> members;
    
    /**
     * Creates a new module dependency reference set.
     * 
     * @param id
     * @param displayName
     */
    public ModuleDependencyRefset(Set<IRefsetMember> members) {
        this.members = members;
    }

    @Override
    public Map<String, Map<String, IModule>> getModuleDependencies() {
        Map<String, Map<String, Set<IModule>>> map = new HashMap<>();
        
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
            //dependencies.add(referencedComponentId);
            
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
        
        Map<String, Map<String, IModule>> res = new HashMap<>();
        
        // Build the collection of module dependencies
        for(String id : all) {
            Map<String, Set<IModule>> versions = map.get(id);
            if(versions != null) {
                for(String version : versions.keySet()) {
                    IModule m = new Module(id, version);
                    Set<IModule> deps = versions.get(version);
                    m.getDependencies().addAll(deps);
                    Map<String, IModule> c = res.get(id);
                    if(c == null) {
                        c = new HashMap<>();
                        res.put(id, c);
                    }
                    c.put(version, m);
                }
            }  
        }
        return res;
    }

    /**
     * @return the members
     */
    @Override
    public Set<IRefsetMember> getMembers() {
        return members;
    }

    @Override
    public void merge(IModuleDependencyRefset other) {
        members.addAll(other.getMembers());
    }

}
