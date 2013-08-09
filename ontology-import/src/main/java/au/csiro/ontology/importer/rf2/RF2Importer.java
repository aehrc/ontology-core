/**
 * Copyright CSIRO Australian e-Health Research Centre (http://aehrc.com).
 * All rights reserved. Use is subject to license terms and conditions.
 */
package au.csiro.ontology.importer.rf2;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.Set;
import java.util.TreeSet;

import javax.xml.bind.JAXBException;

import org.apache.log4j.Logger;

import au.csiro.ontology.Ontology;
import au.csiro.ontology.importer.BaseImporter;
import au.csiro.ontology.importer.ImportException;
import au.csiro.ontology.input.Input.InputType;
import au.csiro.ontology.input.Inputs;
import au.csiro.ontology.input.ModuleInfo;
import au.csiro.ontology.input.RF2Input;
import au.csiro.ontology.input.Version;
import au.csiro.ontology.model.Axiom;
import au.csiro.ontology.model.Concept;
import au.csiro.ontology.model.ConceptInclusion;
import au.csiro.ontology.model.Conjunction;
import au.csiro.ontology.model.Existential;
import au.csiro.ontology.model.NamedConcept;
import au.csiro.ontology.model.NamedFeature;
import au.csiro.ontology.model.NamedRole;
import au.csiro.ontology.model.Role;
import au.csiro.ontology.model.RoleInclusion;
import au.csiro.ontology.snomed.refset.rf2.IModuleDependencyRefset;
import au.csiro.ontology.snomed.refset.rf2.ModuleDependency;
import au.csiro.ontology.util.IProgressMonitor;

/**
 * Imports ontologies specified in RF2 format into the internal representation.
 * 
 * @author Alejandro Metke
 * 
 */
public class RF2Importer extends BaseImporter {
    
    /**
     * Logger.
     */
    private final static Logger log = Logger.getLogger(RF2Importer.class);
    
    /**
     * The object that contains the information about the input files to use.
     */
    protected final Inputs inputs;
    
    /**
     * List of problems found while importing.
     */
    protected final List<String> problems = new ArrayList<String>();

    /**
     * Imports a set of ontologies.
     * 
     * @param inputsStream An input stream with the contents of the XML 
     * configuration file.
     * @throws ImportException 
     */
    public RF2Importer(InputStream inputsStream) throws ImportException {
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
     * @throws ImportException 
     */
    public RF2Importer() throws ImportException {
        final String configFile = "/config.xml";
        final String message = "Malformed input file: " + configFile;
        try {
            inputs = Inputs.load(this.getClass().getResourceAsStream(configFile));
        } catch (NullPointerException e) {
            log.error(message, e);
            throw new ImportException(message, e);
        } catch (JAXBException e) {
            log.error(message, e);
            throw new ImportException(message, e);
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
     * @throws ImportException 
     */
    protected IModuleDependencyRefset loadModuleDependencies() throws ImportException {
        Set<InputStream> iss = new HashSet<InputStream>();
        for(RF2Input input : inputs.getRf2Inputs()) {
            InputType inputType = input.getInputType();
            for(String md : input.getModuleDependenciesRefsetFiles()) {
                final String message = "Unable to load module " +
                        "dependencias. Please check your input configuration " +
                        "file. (input type = "+inputType+", file="+md+")";
                try {
                    iss.add(input.getInputStream(md));
                } catch (NullPointerException e) {
                    log.error(message, e);
                    throw new ImportException(message, e);
                } catch (IOException e) {
                    log.error(message, e);
                    throw new ImportException(message, e);
                }
            }
        }
        
        IModuleDependencyRefset res = 
                RefsetImporter.importModuleDependencyRefset(iss);
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
        Map<String, Set<Version>> res = new HashMap<String, Set<Version>>();
        for(RF2Input in : inputs.getRf2Inputs()) {
            for(ModuleInfo mi : in.getModules()) {
                String moduleId = mi.getId();
                Set<Version> versionsToLoad = res.get(moduleId);
                if(versionsToLoad == null) {
                    versionsToLoad = new HashSet<Version>();
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
     * Transforms a {@link VersionRows} object into an {@link IOntology} using
     * the supplied meta-data.
     * 
     * @param vr
     * @param metadata
     * @return
     */
    protected Ontology transform(String id, String version, VersionRows vr, Map<String, String> metadata, 
            IProgressMonitor monitor) {
        
        final Map<String, Concept> ci = new HashMap<String, Concept>();
        final Map<String, NamedRole> ri = new HashMap<String, NamedRole>();
        
        final Map<String, String> primitive = new HashMap<String, String>();
        final Map<String, Set<String>> parents = new HashMap<String, Set<String>>();
        final Map<String, Set<String>> children = new HashMap<String, Set<String>>();
        final Map<String, List<String[]>> rels = new HashMap<String, List<String[]>>();
        final Map<String, Map<String, String>> roles = new HashMap<String, Map<String, String>>();

        final String conceptDefinedId = metadata.get("conceptDefinedId");
        final String someId = metadata.get("someId");
        final String isAId = metadata.get("isAId");
        final String conceptModelAttId = metadata.get("conceptModelAttId");
        final String neverGroupedIds = metadata.get("neverGroupedIds");
        final String rightIdentityIds = metadata.get("rightIdentityIds");
        final String roleGroupId = metadata.get("roleGroupId");

        final Collection<Axiom> axioms = new ArrayList<Axiom>();

        // Process concept rows
        for (ConceptRow cr : vr.getConceptRows()) {
            if ("1".equals(cr.getActive())) {
                if (!conceptDefinedId.equals(cr.getDefinitionStatusId())) {
                    primitive.put(cr.getId(), "1");
                } else {
                    primitive.put(cr.getId(), "0");
                }
            }
        }

        // Process relationship rows
        for (RelationshipRow rr : vr.getRelationshipRows()) {
            if (!someId.equals(rr.getModifierId())) {
                throw new RuntimeException("Only existentials are supported.");
            }

            // only process active concepts and defining relationships
            if ("1".equals(rr.getActive())) {
                String type = rr.getTypeId();
                String src = rr.getSourceId();
                String dest = rr.getDestinationId();
                if (isAId.equals(type)) {
                    populateParent(src, dest, parents);
                    populateChildren(dest, src, children);
                } else {
                    // Populate relationships
                    populateRels(rr.getId(), src, type, dest, rr.getRelationshipGroup(), rels);
                }
            }
        }

        populateRoles(children.get(conceptModelAttId), "", rightIdentityIds, children, roles);

        // Add role axioms
        for (String r1 : roles.keySet()) {
            String parentRole = roles.get(r1).get("parentrole");

            if (!"".equals(parentRole)) {
                Role lhs = getRole(r1, ri);
                Role rhs = getRole(parentRole, ri);
                axioms.add(new RoleInclusion(new Role[] { lhs }, rhs));
            }

            String rightId = roles.get(r1).get("rightID");
            if (!"".equals(rightId)) {
                Role lhs1 = getRole(r1, ri);
                Role lhs2 = getRole(rightId, ri);
                axioms.add(new RoleInclusion(new Role[] { lhs1, lhs2 }, lhs1));
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
                Concept lhs = getConcept(c1, ci);
                Concept rhs = getConcept(prs.iterator().next(), ci);
                axioms.add(new ConceptInclusion(lhs, rhs));
            } else {
                List<Concept> conjs = new ArrayList<Concept>();

                for (String pr : prs) {
                    conjs.add(getConcept(pr, ci));
                }

                if (relsVal != null) {
                    for (Set<RoleValuePair> rvs : groupRoles(relsVal)) {
                        if (rvs.size() > 1) {
                            Concept[] innerConjs = new Concept[rvs.size()];
                            int j = 0;
                            for (RoleValuePair rv : rvs) {
                                NamedRole role = getRole(rv.role, ri);
                                Concept filler = getConcept(rv.value, ci);
                                Existential exis = new Existential(role, filler);
                                innerConjs[j++] = exis;
                            }
                            // Wrap with a role group
                            conjs.add(new Existential(getRole(
                                    roleGroupId, ri), new Conjunction(
                                    innerConjs)));
                        } else {
                            RoleValuePair first = rvs.iterator().next();
                            NamedRole role = getRole(first.role, ri);
                            Concept filler = getConcept(first.value, ci);
                            Existential exis = new Existential(role, filler);
                            if (neverGroupedIds.contains(first.role)) {
                                // Does not need a role group
                                conjs.add(exis);
                            } else {
                                // Needs a role group
                                conjs.add(new Existential(getRole(roleGroupId, ri), exis));
                            }
                        }
                    }
                }

                Concept[] conjsArr = new Concept[conjs.size()];
                for (int j = 0; j < conjsArr.length; j++) {
                    conjsArr[j] = conjs.get(j);
                }

                axioms.add(new ConceptInclusion(getConcept(c1, ci), new Conjunction(conjsArr)));

                if (primitive.get(c1).equals("0")) {
                    axioms.add(new ConceptInclusion(new Conjunction(conjsArr), getConcept(c1, ci)));
                }
            }
        }
        
        return new Ontology(id, version, axioms, null);
    }

    public Iterator<Ontology> getOntologyVersions(IProgressMonitor monitor) throws ImportException {
        return new OntologyInterator(monitor);
    }
    
    /**
     * Returns a {@link VersionRows} object for an {@link ImportEntry}.
     * 
     * @param entry
     * @return
     * @throws ImportException 
     */
    protected VersionRows getBundle(ImportEntry entry) throws ImportException {
        // Add module information to map for easy lookup
        Map<String, String> modMap = new HashMap<String, String>();
        for(Module module : entry.getModules()) {
            String modId = module.getModuleId();
            String modVer = module.getModuleVersion();
            modMap.put(modId, modVer);
        }
        
        // Map needed to find the correct version of each concept to load for
        // this import entry
        Map<String, ConceptRow> conceptMap = new HashMap<String, ConceptRow>();
        
        // Map needed to find the correct version of each relationship to load 
        // for this import entry
        Map<String, RelationshipRow> relationshipMap = 
                new HashMap<String, RelationshipRow>();
        
        for (RF2Input input : inputs.getRf2Inputs()) {
            InputType inputType = input.getInputType();
            Set<String> conceptsFiles = input.getConceptsFiles();
            log.info("Read concepts info");
            for(String conceptsFile : conceptsFiles) {
                final String message = "Unable to load concepts file. " +
                        "Please check your input configuration file. " +
                        "(input type = "+inputType+", file="+conceptsFile+")";
                try {
                    loadConceptRows(modMap, conceptMap, input.getInputStream(conceptsFile));
                } catch (NullPointerException e) {
                    log.error(message, e);
                    throw new ImportException(message, e);
                } catch (IOException e) {
                    log.error(message, e);
                    throw new ImportException(message, e);
                }
            }
            
            // Load relationships
            Set<String> relationshipsFiles = input.getStatedRelationshipsFiles();
            if(relationshipsFiles == null || relationshipsFiles.isEmpty()) {
                log.info("Read inferred relationships info");
                relationshipsFiles = input.getRelationshipsFiles();
            } else {
                log.info("Read stated relationships info");
            }
            
            if(relationshipsFiles == null || relationshipsFiles.isEmpty()) {
                throw new ImportException("No relationships files was specified.");
            }
            
            for(String relationshipsFile : relationshipsFiles) {
                final String message = "Unable to load realtionships file. Please check your input configuration " +
                        "file. (input type = " + inputType+", file=" + relationshipsFile+")";
                try {
                    loadRelationshipRows(modMap, relationshipMap, input.getInputStream(relationshipsFile));
                } catch (NullPointerException e) {
                    log.error(message, e);
                    throw new ImportException(message, e);
                } catch (IOException e) {
                    log.error(message, e);
                    throw new ImportException(message, e);
                }
            }
        }
        
        VersionRows vr = new VersionRows(conceptMap.values(), relationshipMap.values());
        
        conceptMap = null;
        relationshipMap = null;
        
        return vr;
    }

    protected void loadRelationshipRows(Map<String, String> modMap, Map<String, RelationshipRow> relationshipMap,
            final InputStream inputStream) throws IOException {
        BufferedReader br = null;
        try {
            br = new BufferedReader(new InputStreamReader(inputStream));
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
      
                if (idx1 < 0 || idx2 < 0 || idx3 < 0 || idx4 < 0 || 
                        idx5 < 0 || idx6 < 0 || idx7 < 0 || idx8 < 0 || 
                        idx9 < 0) {
                    br.close();
                    throw new RuntimeException("Concepts: Mis-formatted line, expected 10 " +
                        "tab-separated fields, got: " + line);
                }
      
                final String id = line.substring(0, idx1);
                final String effectiveTime = line.substring(idx1 + 1, idx2);
                final String active = line.substring(idx2 + 1, idx3);
                final String moduleId = line.substring(idx3 + 1, idx4);
                final String sourceId = line.substring(idx4 + 1, idx5);
                final String destinationId = line.substring(idx5 + 1, idx6);
                final String relationshipGroup = line.substring(idx6 + 1, idx7);
                final String typeId = line.substring(idx7 + 1, idx8);
                final String characteristicTypeId = line.substring(idx8 + 1, idx9);
                final String modifierId = line.substring(idx9 + 1);
                
                String tgtVer = modMap.get(moduleId);
                if(tgtVer == null) continue;
                int rel = effectiveTime.compareTo(tgtVer);
                if(rel <= 0) {
                    RelationshipRow currRelationshipRow = relationshipMap.get(id);
                    if(currRelationshipRow == null || effectiveTime.compareTo(
                            currRelationshipRow.getEffectiveTime()) > 0) {
                        RelationshipRow rr = new RelationshipRow(id, effectiveTime, active, moduleId, sourceId, 
                                destinationId, relationshipGroup, typeId, characteristicTypeId, modifierId);
                        relationshipMap.put(id, rr);
                    }
                }
            }
        } finally {
            if(br != null) {
                try { br.close(); } catch(Exception e) {};
            }
        }
    }

    protected void loadConceptRows(Map<String, String> modMap, Map<String, ConceptRow> conceptMap, 
            final InputStream inputStream) throws IOException, UnsupportedEncodingException {
        BufferedReader br = null;
        try {
            br = new BufferedReader(new InputStreamReader(inputStream));
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
                    throw new RuntimeException("Concepts: Mis-formatted line, expected at least 5 tab-separated " +
                        "fields, got: " + line);
                }
      
                final String id = line.substring(0, idx1);
                final String effectiveTime = line.substring(idx1 + 1, idx2);
                final String active = line.substring(idx2 + 1, idx3);
                final String moduleId = line.substring(idx3 + 1, idx4);
                final String definitionStatusId = line.substring(idx4 + 1);
                
                String tgtVer = modMap.get(moduleId);
                if(tgtVer == null) continue;
                int rel = effectiveTime.compareTo(tgtVer);
                if(rel <= 0) {
                    ConceptRow currConceptRow = conceptMap.get(id);
                    if(currConceptRow == null || effectiveTime.compareTo(currConceptRow.getEffectiveTime()) > 0) {
                        ConceptRow cr = new ConceptRow(id, effectiveTime, active, moduleId, definitionStatusId);
                        conceptMap.put(id, cr);
                    }
                }
            }
        } finally {
            if(br != null) {
                try { br.close(); } catch(Exception e) {};
            }
        }
    }

    protected Concept getConcept(String id, Map<String, Concept> ci) {
        Concept c = ci.get(id);
        if (c == null) {
            c = new NamedConcept(id);
            ci.put(id, c);
        }
        return c;
    }

    protected NamedRole getRole(String id, Map<String, NamedRole> ri) {
        NamedRole r = ri.get(id);
        if (r == null) {
            r = new NamedRole(id);
            ri.put(id, r);
        }
        return r;
    }

    protected NamedFeature getFeature(String id, Map<String, NamedFeature> fi) {
        NamedFeature f = fi.get(id);
        if (f == null) {
            f = new NamedFeature(id);
            fi.put(id, f);
        }
        return f;
    }
    
    protected void populateParent(String src, String tgt, Map<String, Set<String>> parents) {
        Set<String> prs = parents.get(src);
        if (prs == null) {
            prs = new TreeSet<String>();
            parents.put(src, prs);
        }
        prs.add(tgt);
    }

    protected void populateChildren(String src, String tgt, Map<String, Set<String>> children) {
        Set<String> prs = children.get(src);
        if (prs == null) {
            prs = new TreeSet<String>();
            children.put(src, prs);
        }
        prs.add(tgt);
    }
    
    protected void populateRels(String comp, String src, String role, String tgt, String group, 
            Map<String, List<String[]>> rels) {
        List<String[]> val = rels.get(src);
        if (val == null) {
            val = new ArrayList<String[]>();
            rels.put(src, val);
        }
        val.add(new String[] { comp, role, tgt, group });
    }

    protected void populateRoles(Set<String> roles, String parentSCTID, String rightIdentityIds, Map<String, 
            Set<String>> children, Map<String, Map<String, String>> rolesMap) {
        if(roles == null) return;
        for (String role : roles) {
            Set<String> cs = children.get(role);
            if (cs != null) {
                populateRoles(cs, role, rightIdentityIds, children, rolesMap);
            }
            String[] ris = rightIdentityIds.split("[,]");
            String ri = (ris[0].equals(role)) ? ris[1] : null;
            if (ri != null) {
                populateRoleDef(role, ri, parentSCTID, rolesMap);
            } else {
                populateRoleDef(role, "", parentSCTID, rolesMap);
            }
        }
    }
    
    protected void populateRoleDef(String code, String rightId, String parentRole, Map<String, 
            Map<String, String>> roles) {
        Map<String, String> vals = roles.get(code);
        if (vals == null) {
            vals = new HashMap<String, String>();
            roles.put(code, vals);
        }
        vals.put("rightID", rightId);
        vals.put("parentrole", parentRole);
    }

    protected Set<Set<RoleValuePair>> groupRoles(List<String[]> groups) {
        Map<String, Set<RoleValuePair>> roleGroups = 
                new HashMap<String, Set<RoleValuePair>>();

        for (String[] group : groups) {
            String comp = group[0];
            String attr = group[1];
            String val = group[2];
            String roleGroup = group[3];
            Set<RoleValuePair> lrvp = roleGroups.get(roleGroup);
            if (lrvp == null) {
                lrvp = new HashSet<RoleValuePair>();
                roleGroups.put(roleGroup, lrvp);
            }
            lrvp.add(new RoleValuePair(attr, val, comp));
        }

        Set<Set<RoleValuePair>> res = new HashSet<Set<RoleValuePair>>();
        for (String roleGroup : roleGroups.keySet()) {
            Set<RoleValuePair> val = roleGroups.get(roleGroup);

            // 0 indicates not grouped
            if ("0".equals(roleGroup)) {
                for (RoleValuePair rvp : val) {
                    Set<RoleValuePair> sin = new HashSet<RoleValuePair>();
                    sin.add(rvp);
                    res.add(sin);
                }
            } else {
                Set<RoleValuePair> item = new HashSet<RoleValuePair>();
                for (RoleValuePair trvp : val) {
                    item.add(trvp);
                }
                res.add(item);
            }
        }
        return res;
    }
    
    public List<String> getProblems() {
        return Collections.emptyList();
    }

    protected class RoleValuePair {
        final String role;
        final String value;
        final String id;

        RoleValuePair(String role, String value, String id) {
            this.role = role;
            this.value = value;
            this.id = id;
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
    
    class OntologyInterator implements Iterator<Ontology> {
        
        private final List<ImportEntry> entries = new ArrayList<ImportEntry>();
        private final IProgressMonitor monitor;
        
        public OntologyInterator(IProgressMonitor monitor) throws ImportException {
            this.monitor = monitor;
            
            // 1. Load module dependencies
            log.info("Loading module dependencies");
            IModuleDependencyRefset mdr = loadModuleDependencies();
            
            if(mdr == null) {
                throw new ImportException("Couldn't load module dependency reference set for RF2 input files.");
            }
            
            // Each map entry contains a map of modules indexed by version
            Map<String, Map<String, ModuleDependency>> deps = 
                    mdr.getModuleDependencies();
            
            // 2. Determine which modules and versions must be loaded
            log.info("Determining which root modules and versions to load");
            Map<String, Set<Version>> toLoad = getModuleVersionsToLoad();
            
            // 3. Create import entries
            log.info("Creating import entries");
            for(String rootModuleId : toLoad.keySet()) {
                Set<Version> versions = toLoad.get(rootModuleId);
                for(Version version : versions) {
                    String ver = version.getId();
                    Map<String, String> metadata = version.getMetadata();
                    ModuleDependency md = deps.get(rootModuleId).get(ver);
                    Set<Module> modules = new HashSet<Module>();
                    
                    Queue<ModuleDependency> depends = 
                            new LinkedList<ModuleDependency>();
                    depends.add(md);

                    while (!depends.isEmpty()) {
                        ModuleDependency d = depends.poll();
                        modules.add(new Module(d.getId(), d.getVersion()));
                        depends.addAll(d.getDependencies());
                    }
                    
                    entries.add(new ImportEntry(rootModuleId, ver, metadata, modules));
                }
            }
            log.info("Found "+entries.size()+" entries to import");
        }
        
        public boolean hasNext() {
            return !entries.isEmpty();
        }

        /**
         * @throws RuntimeException in case an {@code ImportException} has occurred.
         */
        public Ontology next() throws RuntimeException {
            try {
                ImportEntry entry = entries.remove(entries.size()-1);
                VersionRows bundle = getBundle(entry);
                return transform(entry.getRootModuleId(), entry.getRootModuleVersion(), bundle, entry.getMetadata(), 
                        monitor);
            } catch (ImportException e) {
                throw new RuntimeException(e);
            }
        }

        public void remove() {
            throw new UnsupportedOperationException();
        }
        
    }

}

