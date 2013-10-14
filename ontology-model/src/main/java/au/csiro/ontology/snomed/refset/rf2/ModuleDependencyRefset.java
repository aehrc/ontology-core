/**
 * Copyright CSIRO Australian e-Health Research Centre (http://aehrc.com).
 * All rights reserved. Use is subject to license terms and conditions.
 */
package au.csiro.ontology.snomed.refset.rf2;

import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Queue;
import java.util.Set;

import org.apache.log4j.Logger;

/**
 * This class represents a module dependency reference set.
 * 
 * @author Alejandro Metke
 * 
 */
public class ModuleDependencyRefset extends Refset implements IModuleDependencyRefset {

    private final static Logger log = Logger.getLogger(ModuleDependencyRefset.class);
    
    private final static class M {
        final private String module;
        final private String time;

        M(final String module, final String time) {
            this.module = module;
            this.time = time;
        }

        @Override
        public int hashCode() {
            final int prime = 31;
            int result = 1;
            result = prime * result + ((module == null) ? 0 : module.hashCode());
            result = prime * result + ((time == null) ? 0 : time.hashCode());
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
            M other = (M) obj;
            if (module == null) {
                if (other.module != null)
                    return false;
            } else if (!module.equals(other.module))
                return false;
            if (time == null) {
                if (other.time != null)
                    return false;
            } else if (!time.equals(other.time))
                return false;
            return true;
        }

        @Override
        public String toString() {
//            return "http://snomed.info/module/" + module + "/time/" + time;
            return "[module=" + module + ", time=" + time + "]";
        }

    }

    protected final Map<String, Map<String, ModuleDependency>> dependencies = new HashMap<String, Map<String, ModuleDependency>>();

    /**
     * Creates a new module dependency reference set.
     * 
     * @param members
     */
    public ModuleDependencyRefset(Set<ModuleDependencyRow> members) {
        // Index the dependency rows
        final Map<M, Set<M>> index = new HashMap<M, Set<M>>();
        for (ModuleDependencyRow member : members) {
            final M key = new M(member.getModuleId(), member.getSourceEffectiveTime());
            final M val = new M(member.getReferencedComponentId(), member.getTargetEffectiveTime());
            if (!member.isActive()) {
                log.warn("Inactive dependency: " + key + " to\t" + val);
                continue;
            }

            Set<M> vals = index.get(key);
            if (vals == null) {
                vals = new HashSet<M>();
                index.put(key, vals);
            }
            vals.add(val);
        }

        tc(index);
        
        for (M src : index.keySet()) {
            String srcId = src.module;
            String srcVer = src.time;

            ModuleDependency md = createDependency(src, index);
            Map<String, ModuleDependency> verDepMap = dependencies.get(srcId);
            if (verDepMap == null) {
                verDepMap = new HashMap<String, ModuleDependency>();
                dependencies.put(srcId, verDepMap);
            }

            verDepMap.put(srcVer, md);
        }
    }

    /**
     * Compute the transitive closure of the dependencies
     * 
     * @param index
     */
    private static void tc(Map<M, Set<M>> index) {
        for (Entry<M, Set<M>> entry: index.entrySet()) {
            final M src = entry.getKey();
            final Set<M> dependents = entry.getValue();
            final Queue<M> queue = new LinkedList<M>(dependents);

            while (!queue.isEmpty()) {
                final M key = queue.poll();
                if (!index.containsKey(key)) {
                    continue;
                }

                for (M addition: index.get(key)) {
                    if (!dependents.contains(addition)) {
                        dependents.add(addition);
                        queue.add(addition);
                        log.warn("Added missing transitive dependency from " + src + " to " + addition + " via " + key);
                    }
                }
            }
        }
    }

    private ModuleDependency createDependency(M key, Map<M, Set<M>> index) {
        ModuleDependency md = new ModuleDependency(key.module, key.time);

        Set<M> deps = index.get(key);
        if (deps != null) {
            for (M dep : deps) {
                ModuleDependency nmd = createDependency(dep, index);
                md.getDependencies().add(nmd);
            }
        }

        return md;
    }

    @Override
    public Map<String, Map<String, ModuleDependency>> getModuleDependencies() {
        return dependencies;
    }

}
