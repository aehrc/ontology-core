/**
 * Copyright CSIRO Australian e-Health Research Centre (http://aehrc.com).
 * All rights reserved. Use is subject to license terms and conditions.
 */
package au.csiro.ontology.importer.rf2;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.Set;
import java.util.SortedMap;
import java.util.SortedSet;
import java.util.TreeSet;

import javax.xml.bind.JAXBException;

import org.apache.log4j.Logger;

import au.csiro.ontology.IOntology;
import au.csiro.ontology.Ontology;
import au.csiro.ontology.axioms.ConceptInclusion;
import au.csiro.ontology.axioms.IAxiom;
import au.csiro.ontology.axioms.RoleInclusion;
import au.csiro.ontology.importer.IImporter;
import au.csiro.ontology.importer.ImportException;
import au.csiro.ontology.importer.input.Input.InputType;
import au.csiro.ontology.importer.input.Inputs;
import au.csiro.ontology.importer.input.ModuleInfo;
import au.csiro.ontology.importer.input.RF2Input;
import au.csiro.ontology.importer.input.Version;
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
import au.csiro.ontology.util.IProgressMonitor;
import au.csiro.ontology.util.Statistics;

/**
 * Imports ontologies specified in RF2 format into the internal representation.
 * 
 * @author Alejandro Metke
 * 
 */
public class RF2Importer implements IImporter {
    
    private final static Logger log = Logger.getLogger(RF2Importer.class);
    
    /**
     * The object that contains the information about the input files to use.
     */
    protected final Inputs inputs;

    protected final List<String> problems = new ArrayList<>();
    protected final Map<String, String> primitive = new HashMap<>();
    protected final Map<String, Set<String>> parents = new HashMap<>();
    protected final Map<String, Set<String>> children = new HashMap<>();
    protected final Map<String, List<String[]>> rels = new HashMap<>();
    protected final Map<String, Map<String, String>> roles = new HashMap<>();

    /**
     * Imports a set of ontologies.
     * 
     * @param inputsStream An input stream with the contents of the XML 
     * configuration file.
     */
    public RF2Importer(InputStream inputsStream) {
        try {
            inputs = Inputs.load(inputsStream);
        } catch (JAXBException e) {
            log.error("Malformed input file.", e);
            throw new ImportException("Malformed input file.", e);
        }
    }
    
    /**
     * Imports a set of ontologies. Loads the configuration file from the class
     * path.
     */
    public RF2Importer() {
        try {
            inputs = Inputs.load(
                    this.getClass().getResourceAsStream("/config.xml"));
        } catch (JAXBException e) {
            log.error("Malformed input file.", e);
            throw new ImportException("Malformed input file.", e);
        }
    }
    
    /**
     * Imports a set of ontologies using the supplied configuration object.
     * 
     * @param inputs
     */
    public RF2Importer(Inputs inputs) {
        this.inputs = inputs;
    }
    
    /**
     * Loads all the module dependency information from all RF2 inputs into a
     * single {@link IModuleDependencyRefset}.
     * 
     * @return
     */
    protected IModuleDependencyRefset loadModuleDependencies() {
        IModuleDependencyRefset res = null;
        for(RF2Input input : inputs.getRf2Inputs()) {
            InputType inputType = input.getInputType();
            for(String md : input.getModuleDependenciesRefsetFiles()) {
                InputStream is = null;
                if(inputType.equals(InputType.EXTERNAL)) {
                    try {
                        is = new FileInputStream(md);
                    } catch (FileNotFoundException e) {
                        is = null;
                    }
                } else if(inputType.equals(InputType.CLASSPATH)) {
                    is = this.getClass().getResourceAsStream(md);
                } else {
                    throw new RuntimeException("Unexpected input type "+
                            inputType);
                }
                
                if(is == null) {
                    throw new ImportException("Unable to load module " +
                    	"dependencias. Please check your input configuration " +
                    	"file. (input type = "+inputType+", file="+md+")");
                }
                
                if(res == null) {
                    res = (IModuleDependencyRefset)RefsetImporter.importRefset(
                            is, "", "");
                } else {
                    IModuleDependencyRefset other = 
                        (IModuleDependencyRefset)RefsetImporter.importRefset(
                                is, "", "");
                    res.merge(other);
                }
            }
        }
        
        return res;
    }
    
    /**
     * Determines which modules and versions should be loaded based on the
     * {@link Inputs} object. Returns a {@link Map} with the module ids as keys 
     * and the set of versions to import as values.
     * 
     * @return
     */
    protected Map<String, Set<Version>> getModuleVersionsToLoad() {
        Map<String, Set<Version>> res = new HashMap<>();
        for(RF2Input in : inputs.getRf2Inputs()) {
            for(ModuleInfo mi : in.getModules()) {
                String moduleId = mi.getId();
                Set<Version> versionsToLoad = res.get(moduleId);
                if(versionsToLoad == null) {
                    versionsToLoad = new HashSet<>();
                    res.put(moduleId, versionsToLoad);
                }
                for(Version v : mi.getVersions()) {
                    versionsToLoad.add(v);
                }
            }
        }
        
        return res;
    }
    
    /**
     * Assembles bundles based on the module dependency information. Returns a
     * {@link Map} of {@link Map}s indexed by module id and version, containing
     * a {@link Set} of {@link VersionRows} of all the modules in the bundle.
     * 
     * @param toLoad
     * @param deps
     * @param modules
     * @return
     */
    protected Map<String, Map<String, VersionRows>> getBundles(
            Map<String, Set<Version>> toLoad, 
            Map<String, Map<String, IModule>> deps, 
            Map<String, Module> modules) {
        Map<String, Map<String, Set<VersionRows>>> bundles = new HashMap<>();
        
        for(String moduleId : toLoad.keySet()) {
            Map<String, Set<VersionRows>> vMap = bundles.get(moduleId);
            if (vMap == null) {
                vMap = new HashMap<>();
                bundles.put(moduleId, vMap);
            }
            
            for(Version v : toLoad.get(moduleId)) {
                log.info("Importing module "+moduleId+" ("+v.getId()+")");
                IModule mod = deps.get(moduleId).get(v.getId());
                
                Set<VersionRows> bundle = new HashSet<>();

                // Add the root module to the bundle
                String version = mod.getVersion();
                VersionRows vr = getVersionRows(modules, mod, version);
                bundle.add(vr);

                // Add all the dependencies to the bundle
                Queue<IModule> depends = new LinkedList<>();
                depends.addAll(mod.getDependencies());

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
        
        Map<String, Map<String, VersionRows>> res = new HashMap<>();
        
        for(String modId : bundles.keySet()) {
            Map<String, VersionRows> val = new HashMap<>();
            res.put(modId, val);
            Map<String, Set<VersionRows>> dateVerMap = bundles.get(modId);
            for(String date : dateVerMap.keySet()) {
                VersionRows vr = new VersionRows();
                for(VersionRows vrs : dateVerMap.get(date)) {
                    vr.merge(vrs);
                }
                val.put(date, vr);
            }
        }
        
        return res;
    }
    
    protected Map<String, Map<String, IOntology<String>>> transform(
            Map<String, Set<Version>> toLoad, 
            Map<String, Map<String, VersionRows>> bundles, 
            Map<String, IConcept> ci, Map<String, INamedRole<String>> ri) {
        Map<String, Map<String, IOntology<String>>> res = new HashMap<>();
        
        // Transform each set of modules
        for(String modId : toLoad.keySet()) {
            for(Version v : toLoad.get(modId)) {
                String version = v.getId();
                Map<String, String> metadata = v.getMetadata();
                String conceptDefinedId = metadata.get("conceptDefinedId");
                String someId = metadata.get("someId");
                String isAId = metadata.get("isAId");
                String conceptModelAttId = metadata.get("conceptModelAttId");
                String neverGroupedIds = metadata.get("neverGroupedIds");
                
                // TODO: if version don't match the root module's version then
                // this will throw a NullPoinerException. Can this be smarter?
                VersionRows vr = bundles.get(modId).get(version);
                Collection<IAxiom> axioms = new ArrayList<>();

                // Process concept rows
                for (ConceptRow cr : vr.getConceptRows()) {
                    if ("1".equals(cr.getActive())) {
                        if (!conceptDefinedId.equals(
                                cr.getDefinitionStatusId())) {
                            primitive.put(cr.getId(), "1");
                        } else {
                            primitive.put(cr.getId(), "0");
                        }
                    }
                }

                // Process relationship rows
                for (RelationshipRow rr : vr.getRelationshipRows()) {
                    if (!someId.equals(rr.getModifierId())) {
                        throw new RuntimeException("Only existentials are "
                                + "supported.");
                    }

                    // only process active concepts and defining relationships
                    if ("1".equals(rr.getActive())) {
                        String type = rr.getTypeId();
                        String src = rr.getSourceId();
                        String dest = rr.getDestinationId();
                        if (isAId.equals(type)) {
                            populateParent(src, dest);
                            populateChildren(dest, src);
                        } else {
                            // Populate relationships
                            populateRels(src, type, dest,
                                    rr.getRelationshipGroup());
                        }
                    }
                }

                populateRoles(children.get(conceptModelAttId), "", version, 
                        metadata);

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
                                    if (neverGroupedIds.contains(first.role)) {
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
                
                ontVersions.put(version, new Ontology<String>(axioms, null));
            }
        }

        return res;
    }
    
    /**
     * Returns all the rows for a specific bundle.
     * 
     * @param moduleId
     * @param version
     * @param bundles
     * @return
     */
    protected VersionRows getRowsForBundle(String moduleId, String version, 
            Map<String, Map<String, Set<VersionRows>>> bundles) {
        Set<VersionRows> rows = bundles.get(moduleId).get(version);
        VersionRows vr = new VersionRows();
        for (VersionRows row : rows) {
            vr.merge(row);
        }
        return vr;
    }
    
    /**
     * Returns a snapshot for a version.
     * 
     * @param version
     * @return
     */
    public VersionRows getSnapshot(String rootModuleId, String version) {
        log.info("Extracting modules");
        Map<String, Module> modules = extractModules();

        log.info("Loading module dependencies");
        IModuleDependencyRefset md = loadModuleDependencies();
        
        if(md == null) {
            throw new ImportException("Couldn't load module dependency " +
                        "reference set for RF2 input files.");
        }
        
        Map<String, Map<String, IModule>> deps = md.getModuleDependencies();
        Map<String, Set<Version>> toLoad = getModuleVersionsToLoad();
        Map<String, Map<String, VersionRows>> bundles = getBundles(toLoad, 
                deps, modules);
        
        return bundles.get(rootModuleId).get(version);
    }

    @Override
    public Map<String, Map<String, IOntology<String>>> getOntologyVersions(
            IProgressMonitor monitor) {
        
        long start = System.currentTimeMillis();

        Map<String, IConcept> ci = new HashMap<>();
        Map<String, INamedRole<String>> ri = new HashMap<>();
        // No need for feature index because plain RF2 does not support concrete
        // domains

        // 1. Extract the modules - this is just the collection of raw data from
        // the RF2 tables
        log.info("Extracting modules");
        Map<String, Module> modules = extractModules();
        
        // 2. Load module dependencies
        log.info("Loading module dependencies");
        IModuleDependencyRefset md = loadModuleDependencies();
        
        if(md == null) {
            throw new ImportException("Couldn't load module dependency " +
            		"reference set for RF2 input files.");
        }
        
        // Each map entry contains a map of modules indexed by version
        Map<String, Map<String, IModule>> deps = md.getModuleDependencies();
        
        // 3. Determine which modules and versions must be loaded
        log.info("Determining which modules and versions to load");
        Map<String, Set<Version>> toLoad = getModuleVersionsToLoad();
        
        // 4. Assemble the bundles based on the module dependencies
        log.info("Assembling bundles based on module dependencies");
        Map<String, Map<String, VersionRows>> bundles = getBundles(toLoad, 
                deps, modules);
        
        // 5. Up to this point we have the raw bundled data - we need to keep 
        // only the latest version of each entity
        log.info("Filtering bundles");
        filterBundles(bundles);
        
        // 6. Transform into axioms
        log.info("Transforming into axioms");
        Map<String, Map<String, IOntology<String>>> res = transform(toLoad, 
                bundles, ci, ri);
        
        Statistics.INSTANCE.setTime("rf2 loading", 
                System.currentTimeMillis() - start);
        return res;
    }
    
    /**
     * Removes all previous versions of the same entity in a bundle.
     * 
     * @param bundles
     */
    protected void filterBundles(Map<String, Map<String, VersionRows>> 
        bundles) {
        for(String modId : bundles.keySet()) {
            Map<String, VersionRows> dateVerMap = bundles.get(modId);
            for(String date : dateVerMap.keySet()) {
                VersionRows vr = dateVerMap.get(date);
                
                Map<String, Object[]> map = new HashMap<>();
                for(ConceptRow cr : vr.getConceptRows()) {
                    String id = cr.getId();
                    String et = cr.getEffectiveTime();
                    Object[] obj = map.get(id);
                    if(obj == null) {
                        obj = new Object[2];
                        map.put(id, obj);
                    }
                    if(obj[0] != null) {
                        String currDate = (String)obj[0];
                        if(et.compareTo(currDate) > 0) {
                            obj[0] = et;
                            obj[1] = cr;
                        }
                    } else {
                        obj[0] = et;
                        obj[1] = cr;
                    }
                }
                
                VersionRows nvr = new VersionRows();
                for(String key : map.keySet()) {
                    Object[] val = map.get(key);
                    nvr.getConceptRows().add((ConceptRow)val[1]);
                }
                map.clear();
                
                for(RelationshipRow cr : vr.getRelationshipRows()) {
                    String id = cr.getId();
                    String et = cr.getEffectiveTime();
                    Object[] obj = map.get(id);
                    if(obj == null) {
                        obj = new Object[2];
                        map.put(id, obj);
                    }
                    if(obj[0] != null) {
                        String currDate = (String)obj[0];
                        if(et.compareTo(currDate) > 0) {
                            obj[0] = et;
                            obj[1] = cr;
                        }
                    } else {
                        obj[0] = et;
                        obj[1] = cr;
                    }
                }
                
                for(String key : map.keySet()) {
                    Object[] val = map.get(key);
                    nvr.getRelationshipRows().add((RelationshipRow)val[1]);
                }
                map.clear();
                
                // Replace with filtered map
                dateVerMap.put(date, nvr);
            }
        }
    }
    
    protected VersionRows getVersionRows(Map<String, Module> modules, 
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
    
    /**
     * Groups concepts by module.
     * 
     * @param crs
     * @return
     */
    protected Map<String, Map<String, SortedSet<ConceptRow>>> groupConcepts(
            List<ConceptRow> crs) {
        // Groups concepts and relationships by module
        Map<String, Map<String, SortedSet<ConceptRow>>> res = 
                new HashMap<>();
        for (ConceptRow cr : crs) {
            String id = cr.getId();
            String module = cr.getModuleId();
            Map<String, SortedSet<ConceptRow>> conceptMap = 
                    res.get(module);
            if (conceptMap == null) {
                conceptMap = new HashMap<>();
                res.put(module, conceptMap);
            }
            SortedSet<ConceptRow> set = conceptMap.get(id);
            if (set == null) {
                set = new TreeSet<>();
                conceptMap.put(id, set);
            }
            set.add(cr);
        }
        return res;
    }
    
    /**
     * Groups relationships by module.
     * 
     * @param rrs
     * @return
     */
    protected Map<String, Map<String, SortedSet<RelationshipRow>>> groupRels(
            List<RelationshipRow> rrs) {
        Map<String, Map<String, SortedSet<RelationshipRow>>> res = 
                new HashMap<>();
        for (RelationshipRow rr : rrs) {
            String id = rr.getId();
            String module = rr.getModuleId();
            Map<String, SortedSet<RelationshipRow>> relMap = 
                    res.get(module);
            if(relMap == null) {
                relMap = new HashMap<>();
                res.put(module, relMap);
            }
            SortedSet<RelationshipRow> set = relMap.get(id);
            if (set == null) {
                set = new TreeSet<>();
                relMap.put(id, set);
            }
            set.add(rr);
        }
        return res;
    }

    /**
     * Processes the raw RF2 files and generates a map of {@link Module}s, 
     * indexed by module id.
     */
    protected Map<String, Module> extractModules() {
        
        // Map of module ids to modules
        Map<String, Module> moduleMap = new HashMap<>();
        
        for(RF2Input input : inputs.getRf2Inputs()) {
            String conceptsFile = input.getConceptsFile();
            InputType inputType = input.getInputType();
            InputStream in = null;
            if(inputType.equals(InputType.EXTERNAL)) {
                try {
                    in = new FileInputStream(conceptsFile);
                } catch (FileNotFoundException e) {
                    in = null;
                }
            } else if(inputType.equals(InputType.CLASSPATH)) {
                in = this.getClass().getResourceAsStream(conceptsFile);
            } else {
                throw new RuntimeException("Unexpected input type "+inputType);
            }
            
            if(in == null) {
                throw new ImportException("Unable to load concepts file. " +
                        "Please check your input configuration file. " +
                        "(input type = "+inputType+", file="+conceptsFile+")");
            }
            
            try (BufferedReader br = new BufferedReader(
                    new InputStreamReader(in))) {
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
    
                    ConceptRow cr = new ConceptRow(id, effectiveTime, active, 
                            moduleId, definitionStatusId);
                    
                    Module m = moduleMap.get(moduleId);
                    if(m == null) {
                        m = new Module(moduleId);
                        moduleMap.put(moduleId, m);
                    }
                    Map<String, VersionRows> vMap = m.getVersions();
                    VersionRows vr = vMap.get(effectiveTime);
                    if(vr == null) {
                        vr = new VersionRows();
                        vMap.put(effectiveTime, vr);
                    }
                    vr.getConceptRows().add(cr);
                }
            } catch (IOException e) {
                log.error(e);
                throw new ImportException("Problem while loading concepts.", e);
            } 
            
            // Load relationships
            String relationshipsFile = input.getStatedRelationshipsFile();
            if(inputType.equals(InputType.EXTERNAL)) {
                try {
                    in = new FileInputStream(relationshipsFile);
                } catch (FileNotFoundException e) {
                    in = null;
                }
            } else if(inputType.equals(InputType.CLASSPATH)) {
                in = this.getClass().getResourceAsStream(relationshipsFile);
            } else {
                throw new RuntimeException("Unexpected input type "+inputType);
            }
            
            if(in == null) {
                throw new ImportException("Unable to load realtionships " +
                        "file. Please check your input configuration file. " +
                        "(input type = "+inputType+
                        ", file="+relationshipsFile+")");
            }
            
            try (BufferedReader br = new BufferedReader(
                    new InputStreamReader(in))) {
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
                    final String relationshipGroup = line.substring(idx6 + 1, 
                            idx7);
                    final String typeId = line.substring(idx7 + 1, idx8);
                    final String characteristicTypeId = line.substring(idx8 + 1,
                            idx9);
                    final String modifierId = line.substring(idx9 + 1);
                    
                    RelationshipRow rr = new RelationshipRow(id, effectiveTime, 
                            active, moduleId, sourceId, destinationId, 
                            relationshipGroup, typeId, characteristicTypeId, 
                            modifierId);
                    
                    Module m = moduleMap.get(moduleId);
                    if(m == null) {
                        m = new Module(moduleId);
                        moduleMap.put(moduleId, m);
                    }
                    Map<String, VersionRows> vMap = m.getVersions();
                    VersionRows vr = vMap.get(effectiveTime);
                    if(vr == null) {
                        vr = new VersionRows();
                        vMap.put(effectiveTime, vr);
                    }
                    vr.getRelationshipRows().add(rr);
                }
            } catch (IOException e) {
                log.error(e);
                throw new ImportException(
                        "Problem while loading Relationships.", e);
            } 
        }
        
        for(String key : moduleMap.keySet()) {
            Module m = moduleMap.get(key);
            VersionRows last = null;
            SortedMap<String, VersionRows> vMap = m.getVersions();
            for(String version : vMap.keySet()) {
                VersionRows vr = vMap.get(version);
                if(last != null) {
                    vr.merge(last);
                }
                last = vr;
            }
        }
        
        return moduleMap;
    }
    
    /**
     * Merges two {@link VersionRows} considering that 
     * @param tgt
     * @param src
     */
    protected void smartMerge(VersionRows tgt, VersionRows src) {
        
    }

    protected ConceptRow getConceptRowForDate(SortedSet<ConceptRow> conceptSet,
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
            SortedSet<RelationshipRow> relationshipSet, String date) {
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
            String version, Map<String, String> metadata) {
        if(roles == null) return;
        for (String role : roles) {
            Set<String> cs = children.get(role);
            if (cs != null) {
                populateRoles(cs, role, version, metadata);
            }
            String[] ris = metadata.get("rightIdentityIds").split("[,]");
            String ri = (ris[0].equals(role)) ? ris[1] : null;
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

    @Override
    public List<String> getProblems() {
        return Collections.emptyList();
    }

}
