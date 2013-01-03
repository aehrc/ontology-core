/**
 * Copyright CSIRO Australian e-Health Research Centre (http://aehrc.com).
 * All rights reserved. Use is subject to license terms and conditions.
 */
package au.csiro.ontology.importer.rf2;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.Set;
import java.util.SortedMap;
import java.util.SortedSet;
import java.util.TreeMap;
import java.util.TreeSet;

import au.csiro.ontology.IOntology;
import au.csiro.ontology.Ontology;
import au.csiro.ontology.axioms.ConceptInclusion;
import au.csiro.ontology.axioms.IAxiom;
import au.csiro.ontology.axioms.RoleInclusion;
import au.csiro.ontology.classification.IProgressMonitor;
import au.csiro.ontology.importer.IImporter;
import au.csiro.ontology.importer.SnomedMetadata;
import au.csiro.ontology.model.Concept;
import au.csiro.ontology.model.Conjunction;
import au.csiro.ontology.model.Existential;
import au.csiro.ontology.model.IConcept;
import au.csiro.ontology.model.IExistential;
import au.csiro.ontology.model.INamedRole;
import au.csiro.ontology.model.IRole;
import au.csiro.ontology.model.Role;
import au.csiro.ontology.snomed.refset.rf2.IModule;
import au.csiro.ontology.snomed.refset.rf2.IModuleDependencyRefset;
import au.csiro.ontology.util.Statistics;

/**
 * Imports ontologies specified in RF2 format into the internal representation.
 * 
 * @author Alejandro Metke
 * 
 */
public class RF2Importer implements IImporter {

    /**
     * Indicates the type of SNOMED release.
     * 
     * @author Alejandro Metke
     * 
     */
    public enum ReleaseType {
        FULL, SNAPSHOT, INCREMENTAL
    };

    /**
     * The concepts file.
     */
    protected final InputStream conceptsFile;

    /**
     * The relationships file.
     */
    protected final InputStream relationshipsFile;

    /**
     * The module dependency refset.
     */
    protected final InputStream moduleDependencyFile;

    /**
     * Indicates the type of release that the input files represent.
     */
    protected ReleaseType type;

    /**
     * Contains the meta-data necessary to transform the distribution form of
     * SNOMED into a DL model.
     */
    protected SnomedMetadata metadata = new SnomedMetadata();

    protected final List<String> problems = new ArrayList<>();
    protected final Map<String, String> primitive = new HashMap<>();
    protected final Map<String, Set<String>> parents = new HashMap<>();
    protected final Map<String, Set<String>> children = new HashMap<>();
    protected final Map<String, List<String[]>> rels = new HashMap<>();
    protected final Map<String, Map<String, String>> roles = new HashMap<>();

    /**
     * Stores the processed ontologies.
     */
    protected final List<Set<IAxiom>> ontologies = new ArrayList<>();

    /**
     * Imports an ontology in RF2 format. The type parameter indicates the type
     * of release (full, snapshot or incremental).
     * 
     * @param conceptsFile
     * @param descriptionsFile
     * @param relationshipsFile
     * @param moduleDependencyFile
     * @param type
     */
    public RF2Importer(InputStream conceptsFile, InputStream relationshipsFile, 
            InputStream moduleDependencyFile, ReleaseType type) {
        this.conceptsFile = conceptsFile;
        this.relationshipsFile = relationshipsFile;
        this.moduleDependencyFile = moduleDependencyFile;
        this.type = type;
    }
    
    private VersionRows getVersionRows(Map<String, Module> modules, 
            IModule module, String version) {
        VersionRows vr = modules.get(module.getId()).getVersions()
                .get(module.getVersion());
        if(vr == null) {
            // vr might be null when using this importer if the
            // only changes in the dependency are in the 
            // descriptions, because the descriptions are not
            // loaded. In this case we need to find the previous
            // version.
            Map<String, VersionRows> m = modules.get(
                    module.getId()).getVersions();
            Set<String> keys = m.keySet();
            String[] keysArr = keys.toArray(
                    new String[keys.size()]);
            Arrays.sort(keysArr);
            
            // Find previous version in sorted array
            int index = Arrays.binarySearch(keysArr, 0, 
                    keysArr.length, module.getVersion());
            assert(index < 0);
            // x = (-(insertion point) - 1)
            // x + 1 = -insertionPoint
            // -x - 1 = insertionPoint
            index = (index * -1) - 1;
            vr = modules.get(module.getId()).getVersions()
                    .get(keysArr[index - 1]);
            assert(vr != null);
        }
        return vr;
    }

    @Override
    public Map<String, Map<String, IOntology<String>>> getOntologyVersions(
            IProgressMonitor monitor) {
        
        long start = System.currentTimeMillis();
        
        // TODO: do something with the monitor
        // TODO: refactor this to avoid duplicate code in ExtendedRF2Importer

        Map<String, IConcept> ci = new HashMap<>();
        Map<String, INamedRole<String>> ri = new HashMap<>();
        // No need for feature index because plain RF2 does not support concrete
        // domains

        // Extract the (versioned) modules
        Map<String, Module> modules = extractModules();
        Map<String, Map<String, Set<VersionRows>>> bundles = new HashMap<>();
        
        if(moduleDependencyFile != null) {
            // Calculate the module dependencies
            IModuleDependencyRefset md = (IModuleDependencyRefset) 
                    RefsetImporter.importRefset(moduleDependencyFile, 
                            "moduleDependency", "moduleDependency");
    
            // Each map entry contains a collection of modules, one for each 
            // version
            Map<String, Collection<IModule>> deps = md.getModuleDependencies();
    
            // Bundle the modules based on the dependency information
            for (String moduleId : deps.keySet()) {
    
                Map<String, Set<VersionRows>> vMap = bundles.get(moduleId);
                if (vMap == null) {
                    vMap = new HashMap<>();
                    bundles.put(moduleId, vMap);
                }
    
                // Each im is a version of the module
                for (IModule im : deps.get(moduleId)) {
                    Set<VersionRows> bundle = new HashSet<>();
    
                    // Add the root module to the bundle
                    String version = im.getVersion();
                    VersionRows vr = getVersionRows(modules, im, version);
                    bundle.add(vr);
    
                    // Add all the dependencies to the bundle
                    Queue<IModule> depends = new LinkedList<>();
                    depends.addAll(im.getDependencies());
    
                    while (!depends.isEmpty()) {
                        IModule depend = depends.poll();
                        vr = getVersionRows(modules, depend, version);
                        bundle.add(vr);
                        depends.addAll(depend.getDependencies());
                    }
    
                    // Add the bundle to the bundles map
                    vMap.put(version, bundle);
                }
            }
        } else {
            // Deal with the case of no module dependency information
            // TODO: should we assume all modules are independent or related?
            // For now we'll assume all are related and the root module is
            // selected arbitrarily
            Map<String, Set<VersionRows>> versionsMap = new HashMap<>();
            List<Module> modList = new ArrayList<>(modules.values());
            assert(modList.size() > 0);
            
            // Add the first module as the root
            Module first = modList.get(0);
            bundles.put(first.getId(), versionsMap);
            Map<String, VersionRows> fvMap = first.getVersions();
            for(String key : fvMap.keySet()) {
                Set<VersionRows> set = versionsMap.get(key);
                if(set == null) {
                    set = new HashSet<>();
                    versionsMap.put(key,  set);
                }
                set.add(fvMap.get(key));
            }
            
            // Add the rest of the modules to the same entry
            for(int i = 1; i < modList.size(); i++) {
                Module m = modList.get(i);
                Map<String, VersionRows> vMap = m.getVersions();
                for(String key : vMap.keySet()) {
                    Set<VersionRows> set = versionsMap.get(key);
                    if(set == null) {
                        set = new HashSet<>();
                        versionsMap.put(key,  set);
                    }
                    set.add(vMap.get(key));
                }
            }
        }

        Map<String, Map<String, IOntology<String>>> res = new HashMap<>();

        // Transform each set of modules
        for (String modId : bundles.keySet()) {
            Map<String, Set<VersionRows>> map = bundles.get(modId);
            for (String version : map.keySet()) {
                Set<VersionRows> rows = map.get(version);
                VersionRows vr = new VersionRows();
                for (VersionRows row : rows)
                    vr.merge(row);

                // If no meta-data is available for this version then we skip it
                // TODO: can we assume we have metadata per version regardless
                // of the module?
                if (!metadata.hasVersionMetadata(version))
                    continue;

                Collection<IAxiom> axioms = new ArrayList<>();

                // Process concept rows
                for (ConceptRow cr : vr.getConceptRows()) {

                    // TODO: this code is excluding inactive concepts. Do we
                    // need to keep these for ontoserver?
                    if ("1".equals(cr.getActive())) {
                        if (!metadata.getConceptDefinedId(version).equals(
                                cr.getDefinitionStatusId())) {
                            primitive.put(cr.getId(), "1");
                        } else {
                            primitive.put(cr.getId(), "0");
                        }
                    }
                }

                // Process relationship rows
                for (RelationshipRow rr : vr.getRelationshipRows()) {
                    if (!metadata.getSomeId(version).equals(
                            rr.getModifierId())) {
                        throw new RuntimeException("Only existentials are "
                                + "supported.");
                    }

                    // only process active concepts and defining relationships
                    if ("1".equals(rr.getActive())) {
                        String type = rr.getTypeId();
                        String src = rr.getSourceId();
                        String dest = rr.getDestinationId();
                        if (metadata.getIsAId(version).equals(type)) {
                            populateParent(src, dest);
                            populateChildren(dest, src);
                        } else {
                            // Populate relationships
                            populateRels(src, type, dest,
                                    rr.getRelationshipGroup());
                        }
                    }
                }

                // FIXME: deal with nulls!
                populateRoles(
                        children.get(metadata.getConceptModelAttId(version)),
                        "", version);

                // Add role axioms
                for (String r1 : roles.keySet()) {
                    String parentRole = roles.get(r1).get("parentrole");

                    if (!"".equals(parentRole)) {
                        IRole lhs = getRole(r1, ri);
                        IRole rhs = getRole(parentRole, ri);
                        axioms.add(new RoleInclusion(new IRole[] { lhs }, rhs));
                    }

                    String rightId = roles.get(r1).get("rightID");
                    if (!"".equals(rightId)) {
                        IRole lhs1 = getRole(r1, ri);
                        IRole lhs2 = getRole(rightId, ri);
                        axioms.add(new RoleInclusion(
                                new IRole[] { lhs1, lhs2 }, lhs1));
                    }
                }

                // Add concept axioms
                for (String c1 : primitive.keySet()) {
                    if (roles.get(c1) != null)
                        continue;
                    Set<String> prs = parents.get(c1);
                    int numParents = (prs != null) ? prs.size() : 0;

                    List<String[]> relsVal = rels.get(c1);
                    int numRels = 0;
                    if (relsVal != null)
                        numRels = 1;

                    int numElems = numParents + numRels;

                    if (numElems == 0) {
                        // do nothing
                    } else if (numElems == 1) {
                        IConcept lhs = getConcept(c1, ci);
                        IConcept rhs = getConcept(prs.iterator().next(), ci);
                        axioms.add(new ConceptInclusion(lhs, rhs));
                    } else {
                        List<IConcept> conjs = new ArrayList<>();

                        for (String pr : prs) {
                            conjs.add(getConcept(pr, ci));
                        }

                        if (relsVal != null) {
                            for (Set<RoleValuePair> rvs : groupRoles(relsVal)) {
                                if (rvs.size() > 1) {
                                    IConcept[] innerConjs = new IConcept[rvs
                                            .size()];
                                    int j = 0;
                                    for (RoleValuePair rv : rvs) {
                                        INamedRole<String> role = getRole(
                                                rv.role, ri);
                                        IConcept filler = getConcept(rv.value,
                                                ci);
                                        Existential<String> exis = 
                                                new Existential<String>(
                                                role, filler);
                                        innerConjs[j++] = exis;
                                    }
                                    // Wrap with a role group
                                    conjs.add(new Existential<String>(getRole(
                                            "RoleGroup", ri), new Conjunction(
                                            innerConjs)));
                                } else {
                                    RoleValuePair first = rvs.iterator().next();
                                    INamedRole<String> role = getRole(
                                            first.role, ri);
                                    IConcept filler = getConcept(first.value,
                                            ci);
                                    IExistential<String> exis = 
                                            new Existential<>(
                                            role, filler);
                                    if (metadata.getNeverGroupedIds(version)
                                            .contains(first.role)) {
                                        // Does not need a role group
                                        conjs.add(exis);
                                    } else {
                                        // Needs a role group
                                        conjs.add(new Existential<String>(
                                            getRole("RoleGroup", ri), exis));
                                    }
                                }
                            }
                        }

                        IConcept[] conjsArr = new IConcept[conjs.size()];
                        for (int j = 0; j < conjsArr.length; j++) {
                            conjsArr[j] = conjs.get(j);
                        }

                        axioms.add(new ConceptInclusion(getConcept(c1, ci),
                                new Conjunction(conjsArr)));

                        if (primitive.get(c1).equals("0")) {
                            axioms.add(new ConceptInclusion(new Conjunction(
                                    conjsArr), getConcept(c1, ci)));
                        }
                    }
                }
                
                Map<String, IOntology<String>> ontVersions = res.get(modId);
                if(ontVersions == null) {
                    ontVersions = new HashMap<>();
                    res.put(modId, ontVersions);
                }
                
                ontVersions.put(version, new Ontology<String>(axioms, null, 
                        null));
            }
        }
        
        Statistics.INSTANCE.setTime("rf2 loading", 
                System.currentTimeMillis() - start);
        return res;
    }

    /**
     * Processes the raw RF2 files and generates a map of {@link Module}s, 
     * indexed by module id.
     */
    protected Map<String, Module> extractModules() {
        // Store a map between module ids and a sorted set of effective times
        SortedMap<String, SortedSet<String>> moduleTimesMap = new TreeMap<>();

        // Read all the concepts from the raw data
        List<ConceptRow> crs = new ArrayList<>();
        BufferedReader br = null;
        try {
            br = new BufferedReader(new InputStreamReader(conceptsFile));
            String line = br.readLine(); // Skip first line

            while (null != (line = br.readLine())) {
                line = new String(line.getBytes(), "UTF8");
                if (line.trim().length() < 1) {
                    continue;
                }
                int idx1 = line.indexOf('\t');
                int idx2 = line.indexOf('\t', idx1 + 1);
                int idx3 = line.indexOf('\t', idx2 + 1);
                int idx4 = line.indexOf('\t', idx3 + 1);

                // 0..idx1 == id
                // idx1+1..idx2 == effectiveTime
                // idx2+1..idx3 == active
                // idx3+1..idx4 == moduleId
                // idx4+1..end == definitionStatusId

                if (idx1 < 0 || idx2 < 0 || idx3 < 0 || idx4 < 0) {
                    br.close();
                    throw new RuntimeException(
                        "Concepts: Mis-formatted "
                            + "line, expected at least 5 tab-separated fields, "
                            + "got: " + line);
                }

                final String id = line.substring(0, idx1);
                final String effectiveTime = line.substring(idx1 + 1, idx2);
                final String active = line.substring(idx2 + 1, idx3);
                final String moduleId = line.substring(idx3 + 1, idx4);
                final String definitionStatusId = line.substring(idx4 + 1);

                SortedSet<String> times = moduleTimesMap.get(moduleId);
                if (times == null) {
                    times = new TreeSet<>();
                    moduleTimesMap.put(moduleId, times);
                }
                times.add(effectiveTime);

                crs.add(new ConceptRow(id, effectiveTime, active, moduleId,
                        definitionStatusId));
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        } finally {
            if (br != null) {
                try {
                    br.close();
                } catch (Exception e) {
                }
            }
        }

        // Read all the relationships from the raw data
        List<RelationshipRow> rrs = new ArrayList<>();
        try {
            br = new BufferedReader(new InputStreamReader(relationshipsFile)); 
            String line = br.readLine(); // Skip first line
            while (null != (line = br.readLine())) {
                if (line.trim().length() < 1) {
                    continue;
                }
                int idx1 = line.indexOf('\t');
                int idx2 = line.indexOf('\t', idx1 + 1);
                int idx3 = line.indexOf('\t', idx2 + 1);
                int idx4 = line.indexOf('\t', idx3 + 1);
                int idx5 = line.indexOf('\t', idx4 + 1);
                int idx6 = line.indexOf('\t', idx5 + 1);
                int idx7 = line.indexOf('\t', idx6 + 1);
                int idx8 = line.indexOf('\t', idx7 + 1);
                int idx9 = line.indexOf('\t', idx8 + 1);

                // 0..idx1 == id
                // idx1+1..idx2 == effectiveTime
                // idx2+1..idx3 == active
                // idx3+1..idx4 == moduleId
                // idx4+1..idx5 == sourceId
                // idx5+1..idx6 == destinationId
                // idx6+1..idx7 == relationshipGroup
                // idx7+1..idx8 == typeId
                // idx8+1..idx9 == characteristicTypeId
                // idx9+1..end == modifierId

                if (idx1 < 0 || idx2 < 0 || idx3 < 0 || idx4 < 0 || idx5 < 0
                        || idx6 < 0 || idx7 < 0 || idx8 < 0 || idx9 < 0) {
                    br.close();
                    throw new RuntimeException("Concepts: Mis-formatted "
                            + "line, expected 10 tab-separated fields, "
                            + "got: " + line);
                }

                final String id = line.substring(0, idx1);
                final String effectiveTime = line.substring(idx1 + 1, idx2);
                final String active = line.substring(idx2 + 1, idx3);
                final String moduleId = line.substring(idx3 + 1, idx4);
                final String sourceId = line.substring(idx4 + 1, idx5);
                final String destinationId = line.substring(idx5 + 1, idx6);
                final String relationshipGroup = line.substring(idx6 + 1, idx7);
                final String typeId = line.substring(idx7 + 1, idx8);
                final String characteristicTypeId = line.substring(idx8 + 1,
                        idx9);
                final String modifierId = line.substring(idx9 + 1);
                
                SortedSet<String> times = moduleTimesMap.get(moduleId);
                if (times == null) {
                    times = new TreeSet<>();
                    moduleTimesMap.put(moduleId, times);
                }
                times.add(effectiveTime);

                rrs.add(new RelationshipRow(id, effectiveTime, active,
                        moduleId, sourceId, destinationId, relationshipGroup,
                        typeId, characteristicTypeId, modifierId));
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        } finally {
            if (br != null) {
                try {
                    br.close();
                } catch (Exception e) {
                }
            }
        }

        // Groups concepts, descriptions, and relationships by module
        Map<String, Map<String, SortedSet<ConceptRow>>> moduleConceptMap = 
                new HashMap<>();
        for (ConceptRow cr : crs) {
            String id = cr.getId();
            String module = cr.getModuleId();
            Map<String, SortedSet<ConceptRow>> conceptMap = 
                    moduleConceptMap.get(module);
            if (conceptMap == null) {
                conceptMap = new HashMap<>();
                moduleConceptMap.put(module, conceptMap);
            }
            SortedSet<ConceptRow> set = conceptMap.get(id);
            if (set == null) {
                set = new TreeSet<>();
                conceptMap.put(id, set);
            }
            set.add(cr);
        }

        Map<String, Map<String, SortedSet<RelationshipRow>>> moduleRelMap = 
                new HashMap<>();
        for (RelationshipRow rr : rrs) {
            String id = rr.getId();
            String module = rr.getModuleId();
            Map<String, SortedSet<RelationshipRow>> relMap = 
                    moduleRelMap.get(module);
            if(relMap == null) {
                relMap = new HashMap<>();
                moduleRelMap.put(module, relMap);
            }
            SortedSet<RelationshipRow> set = relMap.get(id);
            if (set == null) {
                set = new TreeSet<>();
                relMap.put(id, set);
            }
            set.add(rr);
        }
        
        // Create modules and index by module id
        Map<String, Module> res = new HashMap<>();

        for (String module : moduleTimesMap.keySet()) {
            Module m = new Module(module);
            SortedSet<String> dates = moduleTimesMap.get(module);
            for (String date : dates) {
                VersionRows vr = new VersionRows();
                for (String moduleId : moduleConceptMap.keySet()) {
                    Map<String, SortedSet<ConceptRow>> idConceptRowMap = 
                            moduleConceptMap.get(moduleId);
                    for(String id : idConceptRowMap.keySet()) {
                        ConceptRow cr = getConceptRowForDate(
                                idConceptRowMap.get(id), date);
                        if (cr != null)
                            vr.getConceptRows().add(cr);
                    }
                }

                for (String moduleId : moduleRelMap.keySet()) {
                    Map<String, SortedSet<RelationshipRow>> idRelRowMap = 
                            moduleRelMap.get(moduleId);
                    for(String id : idRelRowMap.keySet()) {
                        RelationshipRow rr = getRelationshipRowForDate(
                                idRelRowMap.get(id), date);
                        if (rr != null)
                            vr.getRelationshipRows().add(rr);
                    }
                }
                m.getVersions().put(date, vr);
            }
            res.put(module, m);
        }

        return res;
    }

    protected ConceptRow getConceptRowForDate(Set<ConceptRow> conceptSet,
            String date) {
        if(conceptSet == null) return null;
        // We need to find the concept with matching date or the previous
        // concept if that one does not exist
        ConceptRow prev = null;

        for (ConceptRow cr : conceptSet) {
            int rel = cr.getEffectiveTime().compareTo(date);
            if (rel == 0) {
                // Found it!
                return cr;
            } else if (rel > 0) {
                return prev;
            } else {
                prev = cr;
            }
        }

        return prev;
    }
    
    protected RelationshipRow getRelationshipRowForDate(
            Set<RelationshipRow> relationshipSet, String date) {
        if(relationshipSet == null) return null;
        RelationshipRow prev = null;

        for (RelationshipRow rr : relationshipSet) {
            int rel = rr.getEffectiveTime().compareTo(date);
            if (rel == 0) {
                // Found it!
                return rr;
            } else if (rel > 0) {
                return prev;
            } else {
                prev = rr;
            }
        }

        return prev;
    }

    protected IConcept getConcept(String id, Map<String, IConcept> ci) {
        IConcept c = ci.get(id);
        if (c == null) {
            c = new Concept<String>(id);
            ci.put(id, c);
        }
        return c;
    }

    protected INamedRole<String> getRole(String id,
            Map<String, INamedRole<String>> ri) {
        INamedRole<String> r = ri.get(id);
        if (r == null) {
            r = new Role<String>(id);
            ri.put(id, r);
        }
        return r;
    }

    protected void populateParent(String src, String tgt) {
        Set<String> prs = parents.get(src);
        if (prs == null) {
            prs = new TreeSet<>();
            parents.put(src, prs);
        }
        prs.add(tgt);
    }

    protected void populateChildren(String src, String tgt) {
        Set<String> prs = children.get(src);
        if (prs == null) {
            prs = new TreeSet<>();
            children.put(src, prs);
        }
        prs.add(tgt);
    }

    protected void populateRels(String src, String role, String tgt,
            String group) {
        List<String[]> val = rels.get(src);
        if (val == null) {
            val = new ArrayList<>();
            rels.put(src, val);
        }
        val.add(new String[] { role, tgt, group });
    }

    protected void populateRoles(Set<String> roles, String parentSCTID,
            String version) {
        if(roles == null) return;
        for (String role : roles) {
            Set<String> cs = children.get(role);
            if (cs != null) {
                populateRoles(cs, role, version);
            }
            String ri = metadata.getRightIdentities(version).get(role);
            if (ri != null) {
                populateRoleDef(role, ri, parentSCTID);
            } else {
                populateRoleDef(role, "", parentSCTID);
            }
        }
    }

    protected void populateRoleDef(String code, String rightId,
            String parentRole) {
        Map<String, String> vals = roles.get(code);
        if (vals == null) {
            vals = new HashMap<>();
            roles.put(code, vals);
        }
        vals.put("rightID", rightId);
        vals.put("parentrole", parentRole);
    }

    protected Set<Set<RoleValuePair>> groupRoles(List<String[]> groups) {
        Map<String, Set<RoleValuePair>> roleGroups = new HashMap<>();

        for (String[] group : groups) {
            String roleGroup = group[2];
            Set<RoleValuePair> lrvp = roleGroups.get(roleGroup);
            if (lrvp == null) {
                lrvp = new HashSet<>();
                roleGroups.put(group[2], lrvp);
            }
            lrvp.add(new RoleValuePair(group[0], group[1]));
        }

        Set<Set<RoleValuePair>> res = new HashSet<>();
        for (String roleGroup : roleGroups.keySet()) {
            Set<RoleValuePair> val = roleGroups.get(roleGroup);

            // 0 indicates not grouped
            if ("0".equals(roleGroup)) {
                for (RoleValuePair rvp : val) {
                    Set<RoleValuePair> sin = new HashSet<>();
                    sin.add(rvp);
                    res.add(sin);
                }
            } else {
                Set<RoleValuePair> item = new HashSet<>();
                for (RoleValuePair trvp : val) {
                    item.add(trvp);
                }
                res.add(item);
            }
        }
        return res;
    }

    protected class RoleValuePair {
        String role;
        String value;

        RoleValuePair(String role, String value) {
            this.role = role;
            this.value = value;
        }

        @Override
        public int hashCode() {
            final int prime = 31;
            int result = 1;
            result = prime * result + getOuterType().hashCode();
            result = prime * result + ((role == null) ? 0 : role.hashCode());
            result = prime * result + ((value == null) ? 0 : value.hashCode());
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
            RoleValuePair other = (RoleValuePair) obj;
            if (!getOuterType().equals(other.getOuterType()))
                return false;
            if (role == null && other.role != null)
                return false;
            else if (!role.equals(other.role))
                return false;
            if (value == null && other.value != null)
                return false;
            else if (!value.equals(other.value))
                return false;
            return true;
        }

        private RF2Importer getOuterType() {
            return RF2Importer.this;
        }
    }

}
