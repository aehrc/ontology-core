/**
 * Copyright CSIRO Australian e-Health Research Centre (http://aehrc.com).
 * All rights reserved. Use is subject to license terms and conditions.
 */
package au.csiro.ontology.importer;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.apache.log4j.Logger;

/**
 * This class contains the meta-data that is necessary to successfully import
 * the SNOMED distribution files into a DL model.
 * 
 * @author Alejandro Metke
 * 
 */
public class SnomedMetadata {

    // Logger
    private final static Logger log = Logger.getLogger(SnomedMetadata.class);

    protected Map<String, Map<String, String>> metadata = new HashMap<>();

    protected Map<String, Set<String>> neverGroupedIds = new HashMap<>();

    protected Map<String, Map<String, String>> rightIdentities = new HashMap<>();

    /**
     * Constructor.
     */
    public SnomedMetadata() {

        try (BufferedReader br = new BufferedReader(new InputStreamReader(this
                .getClass().getResourceAsStream("/metadata.txt")))) {
            String line;

            // Keep track of releases that use the same meta-data
            Map<String, String[]> equivs = new HashMap<>();
            while ((line = br.readLine()) != null) {
                // Skip blank lines
                if (line.trim().equals(""))
                    continue;
                String[] parts = line.split("[.=]");
                if (parts.length != 3) {
                    log.warn("Invalid line found: " + line);
                    continue;
                }
                if (parts[1].equals("equivs")) {
                    String[] dates = parts[2].split("[,]");
                    equivs.put(parts[0], dates);
                } else if (parts[1].equals("neverGroupedIds")) {
                    String[] ids = parts[2].split("[,]");
                    Set<String> ngis = new HashSet<>();
                    for (String id : ids) {
                        ngis.add(id);
                    }
                    neverGroupedIds.put(parts[0], ngis);
                } else if (parts[1].equals("rightIdentities")) {
                    String[] ids = parts[2].split("[,]");
                    if (ids.length != 2) {
                        log.warn("Invalid right identities found: " + line);
                    } else {
                        Map<String, String> ri = rightIdentities.get(parts[0]);
                        if (ri == null) {
                            ri = new HashMap<>();
                            rightIdentities.put(parts[0], ri);
                        }
                        ri.put(ids[0], ids[1]);
                    }
                } else {
                    Map<String, String> map = metadata.get(parts[0]);
                    if (map == null) {
                        map = new HashMap<>();
                        metadata.put(parts[0], map);
                    }
                    map.put(parts[1], parts[2]);
                }
            }

            // Copy equivalent values
            for (String key : equivs.keySet()) {
                Map<String, String> meta = metadata.get(key);
                Set<String> neverGrouped = neverGroupedIds.get(key);
                Map<String, String> rightIdents = rightIdentities.get(key);
                for (String equiv : equivs.get(key)) {
                    metadata.put(equiv, meta);
                    neverGroupedIds.put(equiv, neverGrouped);
                    rightIdentities.put(equiv, rightIdents);
                }
            }
        } catch (IOException e) {
            throw new RuntimeException("Unable to load metadata!", e);
        }
    }

    /**
     * Returns true if there is meta data available for a particular version.
     * 
     * @param version
     *            The version.
     * @return
     */
    public boolean hasVersionMetadata(String version) {
        return metadata.keySet().contains(version)
                && neverGroupedIds.keySet().contains(version)
                && rightIdentities.keySet().contains(version);
    }

    /**
     * Returns the root concept id for roles.
     * 
     * @param version
     *            The SNOMED version.
     * @return The root concept id for roles.
     */
    public String getConceptModelAttId(String version) {
        Map<String, String> map = metadata.get(version);
        if (map != null) {
            return map.get("conceptModelAttId");
        }
        return null;
    }

    /**
     * Returns the role id for is-a relationships.
     * 
     * @param version
     *            The SNOMED version.
     * @return The role id for is-a relationships.
     */
    public String getIsAId(String version) {
        Map<String, String> map = metadata.get(version);
        if (map != null) {
            return map.get("isAId");
        }
        return null;
    }

    /**
     * Returns the id of SNOMED's core module.
     * 
     * @param version
     *            The SNOMED version.
     * @return Id of SNOMED's core module.
     */
    public String getCoreModuleId(String version) {
        Map<String, String> map = metadata.get(version);
        if (map != null) {
            return map.get("coreModuleId");
        }
        return null;
    }

    /**
     * Returns the Id of SNOMED's meta-data module.
     * 
     * @param version
     *            The SNOMED version.
     * @return Id of SNOMED's meta-data module.
     */
    public String getMetadataModuleId(String version) {
        Map<String, String> map = metadata.get(version);
        if (map != null) {
            return map.get("metadataModuleId");
        }
        return null;
    }

    /**
     * Returns the id of the enumeration in SNOMED used to indicate that a
     * concept is fully defined.
     * 
     * @param version
     *            The SNOMED version.
     * @return Id of the enumeration in SNOMED used to indicate that a concept
     *         is fully defined.
     */
    public String getConceptDefinedId(String version) {
        Map<String, String> map = metadata.get(version);
        if (map != null) {
            return map.get("conceptDefinedId");
        }
        return null;
    }

    /**
     * Returns the id of the enumeration in SNOMED used to represent an
     * existential quantification.
     * 
     * @param version
     *            The SNOMED version.
     * @return Id of enumeration in SNOMED used to represent an existential
     *         quantification.
     */
    public String getSomeId(String version) {
        Map<String, String> map = metadata.get(version);
        if (map != null) {
            return map.get("someId");
        }
        return null;
    }

    /**
     * Returns the id of the enumeration in SNOMED used to represent a universal
     * quantification.
     * 
     * @param version
     *            The SNOMED version.
     * @return Id of the enumeration in SNOMED used to represent a universal
     *         quantification.
     */
    public String getAllId(String version) {
        Map<String, String> map = metadata.get(version);
        if (map != null) {
            return map.get("allId");
        }
        return null;
    }

    /**
     * Returns the id of the enumeration in SNOMED used to indicate that a
     * description is a fully specified name.
     * 
     * @param version
     *            The SNOMED version.
     * @return The id of the enumeration in SNOMED used to indicate that a
     *         description is a fully specified name.
     */
    public String getFsnId(String version) {
        Map<String, String> map = metadata.get(version);
        if (map != null) {
            return map.get("fsnId");
        }
        return null;
    }

    /**
     * Returns the id of the enumeration in SNOMED used to indicate that a
     * description is a synonym.
     * 
     * @param version
     *            The SNOMED version.
     * @return Id of the enumeration in SNOMED used to indicate that a
     *         description is a synonym.
     */
    public String getSynonymId(String version) {
        Map<String, String> map = metadata.get(version);
        if (map != null) {
            return map.get("synonymId");
        }
        return null;
    }

    /**
     * Returns the id of the enumeration in SNOMED used to indicate that a
     * description is a definition.
     * 
     * @param version
     *            The SNOMED version.
     * @return Id of enumeration in SNOMED used to indicate that a description
     *         is a definition.
     */
    public String getDefinitionId(String version) {
        Map<String, String> map = metadata.get(version);
        if (map != null) {
            return map.get("definitionId");
        }
        return null;
    }

    /**
     * Returns the set of ids of SNOMED roles that should never be placed in a
     * role group.
     * 
     * @param version
     *            The SNOMED version.
     * @return Set of ids of SNOMED roles that should never be placed in a role
     *         group.
     */
    public Set<String> getNeverGroupedIds(String version) {
        return neverGroupedIds.get(version);
    }

    /**
     * Returns the right identity axioms that cannot be represented in RF1 or
     * RF2 formats. An example is direct-substance o has-active-ingredient [
     * direct-substance. The key of the returned map is the first element in the
     * LHS and the value is the second element in the LHS. The RHS, because it
     * is a right identity axiom, is always the same as the first element in the
     * LHS.
     * 
     * @param version
     *            The SNOMED version.
     * @return The right identity axioms.
     */
    public Map<String, String> getRightIdentities(String version) {
        return rightIdentities.get(version);
    }

    /**
     * Returns the id for the "role group" role.
     * 
     * @param version
     *            The SNOMED version.
     * @return The id for the "role group" role.
     */
    public String getRoleGroupId(String version) {
        Map<String, String> map = metadata.get(version);
        if (map != null) {
            return map.get("roleGroupId");
        }
        return null;
    }

}
