/**
 * Copyright CSIRO Australian e-Health Research Centre (http://aehrc.com).
 * All rights reserved. Use is subject to license terms and conditions.
 */
package au.csiro.ontology.snomed.refset.rf2;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Queue;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * This class represents a module dependency reference set.
 *
 * @author Alejandro Metke
 *
 */
public class ModuleDependencyRefset extends Refset implements IModuleDependencyRefset {

    private final static Logger log = LoggerFactory.getLogger(ModuleDependencyRefset.class);

    private final static class M implements Comparable<M> {
        final private String module;
        final private String time;

        M(final String module, final String time) {
            assert null != module;
            assert null != time;

            this.module = module;
            this.time = time;
        }

        @Override
        public int hashCode() {
            final int prime = 31;
            int result = 1;
            result = prime * result + module.hashCode();
            result = prime * result + time.hashCode();
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
            return module.equals(other.module) && time.equals(other.time);
        }

        @Override
        public String toString() {
//            return "http://snomed.info/module/" + module + "/time/" + time;
            return module + "/" + time;
        }

        @Override
        public int compareTo(M other) {
            final int mCmp = module.compareTo(other.module);
            if (mCmp == 0) {
                return parseTime(time).compareTo(parseTime(other.time));
            } else {
                return mCmp;
            }
        }

    }

    protected final Map<String, Map<String, ModuleDependency>> dependencies = new HashMap<String, Map<String, ModuleDependency>>();

    /**
     * Creates a new module dependency reference set.
     * <p>
     * Requirements:
     * <ul>
     * <li>All MDRS rows are available (actives <b>and</b> inactives)
     * <li>Result is a map of every valid "Version" defined by the supplied rows
     * <li>There is a valid Version for every unique combination of moduleId and sourceEffectiveTime
     * <li>As per the TIG, sourceEffectiveTime and effectiveTime must be equal in every row
     * <li>The sourceEffectiveTime is used to identify the Version-specific set of modules
     * <li>targetEffectiveTime is always less-than or equal to (<=) sourceEffectiveTime
     * </ul>
     * <p>
     * <b>Note</b>, while module dependencies are not explicitly required to be transitively consistent by the
     * IHTSDO Specifications (as documented in the TIG), it is implicitly required (at least for Core) by the
     * requirements that the Core is not "changed" by an Extension.
     * <p>
     * Strategy for loading:
     * <ul>
     * <li>Gather all (active <b>and</b> inactive) rows
     * <li>For each unique (active <b>or</b> inactive) moduleId and sourceEffectiveTime pair:
     * <ul>
     * <li>The set of associated referencedComponentId module and targetEffectiveTime pairs makes up the required modules
     * </ul>
     * <li>Compute the completion (transitive closure) of the dependency relationship
     * </ul>
     *
     * @param members
     */
    public ModuleDependencyRefset(Set<ModuleDependencyRow> members, boolean validate) throws ValidationException {
        id = "900000000000534007";

        final List<String> problems = new ArrayList<String>();

        // Index the dependency rows
        final Map<M, Set<M>> index = new HashMap<M, Set<M>>();
        for (ModuleDependencyRow member : members) {
            final M version = new M(member.getModuleId(), member.getSourceEffectiveTime());
            final M requiredModule = new M(member.getReferencedComponentId(), member.getTargetEffectiveTime());
            if (!member.isActive()) {
                final String message = "Inactive dependency: " + version + " to\t" + requiredModule;
                log.info(message);
                continue;
            }
            if (member.isMalformed()) {
                final String message = "Ignoring malformed MDRS entry: " + version + " to\t" + requiredModule;
                problems.add(message);
                log.warn(message);
                continue;
            }

            Set<M> vals = index.get(version);
            if (vals == null) {
                vals = new HashSet<M>();
                index.put(version, vals);
            }
            vals.add(requiredModule);
        }

        if (validate && !problems.isEmpty()) {
            throw new ValidationException("Malformed Module Dependency Reference Set", problems);
        }

        if (log.isTraceEnabled()) {
            // Use a TreeSet to get the output sorted
            for (final M version: new TreeSet<M>(index.keySet())) {
                log.trace("MDRS entry for version: " + version);
            }
        }

        // Compute the transitive closure of the required modules.
        // Note:
        // <li> if the MDRS conforms to the spec., then this would not be required
        // <li> furthermore, if the dependencies are self-consistent, then this should do nothing
        //
        tc(index);

        for (final M version : index.keySet()) {
            final String srcId = version.module;
            final String srcVer = version.time;

            ModuleDependency md = createDependency(version, index);
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
        final SortedSet<String> warnings = new TreeSet<String>();

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
                        warnings.add("Added implied transitive dependency from " + src + " to " + addition + " via " + key);
                    }
                }
            }
        }

        for (final String warning: warnings) {
            log.warn(warning);
        }
    }

    private ModuleDependency createDependency(M version, Map<M, Set<M>> index) {
        final ModuleDependency md = new ModuleDependency(version.module, version.time);

        Set<M> deps = index.get(version);
        if (deps != null) {
            for (M dep : deps) {
                md.getDependencies().add(createDependency(dep, index));
            }
        }

        return md;
    }

    @Override
    public Map<String, Map<String, ModuleDependency>> getModuleDependencies() {
        return dependencies;
    }

    static Date parseTime(String time) {
        final SimpleDateFormat ddf = new SimpleDateFormat("yyyyMMdd'T'HHmmss'Z'");
        try {
            return ddf.parse(time);
        } catch (ParseException e1) {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
            try {
                return sdf.parse(time);
            } catch (ParseException e2) {
                final String message = "Could not parse effectiveTime: " + time;
                log.error(message, e2);
                throw new RuntimeException(message, e2);
            }
        }

    }

}
