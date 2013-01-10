/**
 * Copyright CSIRO Australian e-Health Research Centre (http://aehrc.com).
 * All rights reserved. Use is subject to license terms and conditions. 
 */
package au.csiro.ontology.importer.rf1;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
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
import au.csiro.ontology.model.Role;
import au.csiro.ontology.util.Statistics;

/**
 * Transforms the native RF1 files used in SNOMED into the internal 
 * representation. Because of the limitations of the RF1 format, this importer 
 * does not handle versions.
 * 
 * @author Alejandro Metke
 * 
 */
public class RF1Importer implements IImporter {

    protected final InputStream conceptsFile;
    protected final InputStream relationshipsFile;
    protected final String version;

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
     * Creates a new {@link RF1Importer}.
     * 
     * @param conceptsFile
     * @param descriptionsFile
     * @param relationshipsFile
     * @param version The version of this ontology.
     */
    public RF1Importer(InputStream conceptsFile, InputStream relationshipsFile, 
            String version) {
        this.conceptsFile = conceptsFile;
        this.relationshipsFile = relationshipsFile;
        this.version = version;
    }
    
    @Override
    public Map<String, Map<String, IOntology<String>>> getOntologyVersions(
            IProgressMonitor monitor) {
        
        long start = System.currentTimeMillis();
        
        monitor.taskStarted("Loading axioms");
        
        Map<String, Map<String, IOntology<String>>> res = new HashMap<>();

        // Extract the version rows
        VersionRows vr = extractVersionRows();
        
        // Transform the group of rows
        String version = vr.getVersionName();

        // If no meta-data is available for this version then we skip it
        if (!metadata.hasVersionMetadata(version))
            return null;

        Collection<IAxiom> axioms = new ArrayList<>();

        // Process concept rows
        for (ConceptRow cr : vr.getConceptRows()) {
            if (!"CONCEPTID".equals(cr.getConceptId()) && 
                    "0".equals(cr.getConceptStatus())) {
                primitive.put(cr.getConceptId(), cr.getIsPrimitive());
            }
        }

        // Process relationship rows
        for (RelationshipRow rr : vr.getRelationshipRows()) {

            // only process active concepts and defining relationships
            if (!"RELATIONSHIPID".equals(rr.getRelationshipId())
                    && "0".equals(rr.getCharacteristicType())) {
                if (metadata.getIsAId(version).equals(rr.getRelationshipType())) {
                    populateParent(rr.getConceptId1(), rr.getConceptId2());
                    populateChildren(rr.getConceptId2(), rr.getConceptId1());
                } else {
                    // Populate relationships
                    populateRels(rr.getConceptId1(), 
                            rr.getRelationshipType(), rr.getConceptId2(), 
                            rr.getRelationshipGroup());
                }
            }
        }
        
        Set<String> conceptModelChildren = children.get(
                metadata.getConceptModelAttId(version));
        if (conceptModelChildren != null)
            populateRoles(conceptModelChildren, "");

        // Add the role axioms
        for (String r1 : roles.keySet()) {
            String parentRole = roles.get(r1).get("parentrole");

            if (!"".equals(parentRole)) {
                axioms.add(new RoleInclusion(
                        new Role<String>(r1), 
                        new Role<String>(parentRole)));
            }

            String rightId = roles.get(r1).get("rightID");
            if (!"".equals(rightId)) {
                axioms.add(new RoleInclusion(new Role[] { 
                        new Role<String>(r1), new Role<String>(rightId) }, 
                        new Role<String>(r1)));
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
            } else if (numElems == 1 && (prs != null && !prs.isEmpty())) {
                axioms.add(new ConceptInclusion(new Concept<String>(c1), 
                        new Concept<String>(prs.iterator().next())));
            } else {
                List<IConcept> conjs = new ArrayList<>();
                
                if(prs != null) {
                    for (String pr : prs) {
                        conjs.add(new Concept<String>(pr));
                    }
                }

                if (relsVal != null) {
                    for (Set<RoleValuePair> rvs : groupRoles(relsVal)) {
                        if (rvs.size() > 1) {
                            List<IConcept> innerConjs = new ArrayList<>();
                            for (RoleValuePair rv : rvs) {
                                Role<String> role = new Role<>(rv.role);
                                Concept<String> filler = new Concept<>(
                                        rv.value);
                                Existential<String> exis = 
                                        new Existential<>(role, filler);
                                innerConjs.add(exis);
                            }
                            // Wrap with a role group
                            conjs.add(new Existential<String>(
                                    new Role<String>("RoleGroup"), 
                                    new Conjunction(innerConjs)));
                        } else {
                            RoleValuePair first = rvs.iterator().next();
                            Role<String> role = new Role<>(first.role);
                            Concept<String> filler = new Concept<>(first.value);
                            Existential<String> exis = new Existential<>(role, 
                                    filler);
                            if (metadata.getNeverGroupedIds(
                                    version).contains(first.role)) {
                                // Does not need a role group
                                conjs.add(exis);
                            } else {
                                // Needs a role group
                                conjs.add(new Existential<>(
                                        new Role<>("RoleGroup"), exis));
                            }
                        }
                    }
                }

                axioms.add(new ConceptInclusion(new Concept<>(c1), 
                        new Conjunction(conjs)));

                if (primitive.get(c1).equals("0")) {
                    axioms.add(new ConceptInclusion(new Conjunction(conjs),
                            new Concept<>(c1)));
                }
            }
        }
        
        Map<String, IOntology<String>> map = new HashMap<>();
        map.put(vr.getVersionName(), new Ontology<String>(axioms, null));
        res.put("snomed", map);
        
        Statistics.INSTANCE.setTime("rf1 loading", 
                System.currentTimeMillis() - start);
        return res;
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

    protected void populateRels(String src, String role, String tgt, String group) {
        List<String[]> val = rels.get(src);
        if (val == null) {
            val = new ArrayList<>();
            rels.put(src, val);
        }
        val.add(new String[] { role, tgt, group });
    }

    protected void populateRoles(Set<String> roles, String parentSCTID) {
        for (String role : roles) {
            Set<String> cs = children.get(role);
            if (cs != null) {
                populateRoles(cs, role);
            }
            String ri = metadata.getRightIdentities(version).get(role);
            if (ri != null) {
                populateRoleDef(role, ri, parentSCTID);
            } else {
                populateRoleDef(role, "", parentSCTID);
            }
        }
    }

    protected void populateRoleDef(String code, String rightId, String parentRole) {
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

    public void clear() {
        problems.clear();
        primitive.clear();
        parents.clear();
        children.clear();
        rels.clear();
        roles.clear();
    }

    public List<String> getProblems() {
        return problems;
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
            if (role == null) {
                if (other.role != null)
                    return false;
            } else if (!role.equals(other.role))
                return false;
            if (value == null) {
                if (other.value != null)
                    return false;
            } else if (!value.equals(other.value))
                return false;
            return true;
        }

        private RF1Importer getOuterType() {
            return RF1Importer.this;
        }
    }

    public boolean usesConcreteDomains() {
        return false;
    }
    
    /**
     * Processes the raw RF1 files and generates a {@link VersionRows}.
     */
    @SuppressWarnings("resource")
    public VersionRows extractVersionRows() {
        // Read all the concepts from the raw data
        List<ConceptRow> crs = new ArrayList<>();
        
        try (BufferedReader br = new BufferedReader(
                new InputStreamReader(conceptsFile))) {
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
                int idx5 = line.indexOf('\t', idx4 + 1);

                // 0..idx1 == conceptId
                // idx1+1..idx2 == conceptStatus
                // idx2+1..idx3 == fullySpecifiedName
                // idx3+1..idx4 == ctv3Id
                // idx4+1..idx5 == snomedId
                // idx5+1..end == isPrimitive

                if (idx1 < 0 || idx2 < 0 || idx3 < 0 || idx4 < 0 || idx5 < 0) {
                    br.close();
                    throw new RuntimeException(
                            "Concepts: Mis-formatted "
                                    + "line, expected at least 6 tab-separated fields, "
                                    + "got: " + line);
                }

                final String conceptId = line.substring(0, idx1);
                final String conceptStatus = line.substring(idx1 + 1, idx2);
                final String fullySpecifiedName = line.substring(idx2 + 1, idx3);
                final String ctv3Id = line.substring(idx3 + 1, idx4);
                final String snomedId = line.substring(idx4 + 1, idx5);
                final String isPrimitive = line.substring(idx5 + 1);

                crs.add(new ConceptRow(conceptId, conceptStatus, 
                        fullySpecifiedName, ctv3Id, snomedId, isPrimitive));
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        // Read all the relationships from the raw data
        List<RelationshipRow> rrs = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(
                new InputStreamReader(relationshipsFile))) {
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

                // 0..idx1 == relationshipId
                // idx1+1..idx2 == conceptId1
                // idx2+1..idx3 == relationshipType
                // idx3+1..idx4 == conceptId2
                // idx4+1..idx5 == characteristicType
                // idx5+1..idx6 == refinability
                // idx6+1..end == relationshipGroup

                if (idx1 < 0 || idx2 < 0 || idx3 < 0 || idx4 < 0 || idx5 < 0
                        || idx6 < 0) {
                    br.close();
                    throw new RuntimeException("Concepts: Mis-formatted "
                            + "line, expected 7 tab-separated fields, "
                            + "got: " + line);
                }

                final String relationshipId = line.substring(0, idx1);
                final String conceptId1 = line.substring(idx1 + 1, idx2);
                final String relationshipType = line.substring(idx2 + 1, idx3);
                final String conceptId2 = line.substring(idx3 + 1, idx4);
                final String characteristicType = line.substring(idx4 + 1, idx5);
                final String refinability = line.substring(idx5 + 1, idx6);
                final String relationshipGroup = line.substring(idx6 + 1);

                rrs.add(new RelationshipRow(relationshipId, conceptId1, 
                        relationshipType, conceptId2, characteristicType, 
                        refinability, relationshipGroup));
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        // In this case we know we are dealing with a single version so we need
        // to generate a single version row
        
        VersionRows vr = new VersionRows(version);
        vr.getConceptRows().addAll(crs);
        vr.getRelationshipRows().addAll(rrs);

        return vr;
    }

    public SnomedMetadata getMetadata() {
        return metadata;
    }

}
