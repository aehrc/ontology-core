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
import java.util.List;

import org.apache.log4j.Logger;

import au.csiro.ontology.snomed.refset.rf2.ILanguageRefset;
import au.csiro.ontology.snomed.refset.rf2.ILanguageRefsetMember;
import au.csiro.ontology.snomed.refset.rf2.IModuleDependencyRefset;
import au.csiro.ontology.snomed.refset.rf2.IModuleDependencyRefsetMember;
import au.csiro.ontology.snomed.refset.rf2.IRefset;
import au.csiro.ontology.snomed.refset.rf2.IRefsetMember;
import au.csiro.ontology.snomed.refset.rf2.LanguageRefset;
import au.csiro.ontology.snomed.refset.rf2.LanguageRefsetMember;
import au.csiro.ontology.snomed.refset.rf2.ModuleDependencyRefset;
import au.csiro.ontology.snomed.refset.rf2.ModuleDependencyRefsetMember;

/**
 * Imports RF2 refsets.
 * 
 * @author Alejandro Metke
 *
 */
public class RefsetImporter {
    
    // Logger
    private final static Logger log = Logger.getLogger(RefsetImporter.class);
    
    /**
     * Imports a refset from a file. This method closes the {@link InputStream}
     * after loading the refset.
     * 
     * @param refsetFile The input stream.
     * @param id The id of the refset
     * @param displayName The display name of the refset
     * @param version The version of the release that the refset belongs to
     * @return
     */
    public static IRefset importRefset(InputStream refsetFile, String id, 
            String displayName) {
        BufferedReader br = null;
        try {
            br = new BufferedReader(new InputStreamReader(refsetFile)); 
            String line = br.readLine();
            
            String[] cols = line.split("[\t]");
            assert(cols.length >= 6);
            
            if(cols.length == 7) {
                // Test if it is a language refset
                if(cols[6].equals("acceptabilityId")) {
                    
                    List<IRefsetMember> members = new ArrayList<>();
                    while (null != (line = br.readLine())) {
                        cols = line.split("[\t]");
                        boolean active = cols[2].equals("1") ? true : false;
                        ILanguageRefsetMember m = 
                                new LanguageRefsetMember(cols[0], 
                                        cols[1], active , cols[3], cols[4], 
                                        cols[5], cols[6]);
                        members.add(m);
                    }
                    
                    ILanguageRefset res = new LanguageRefset(id, displayName, 
                            members);
                    return res;
                } else {
                    throw new RuntimeException("Unsupported refset type with " +
                            "columns "+Arrays.asList(cols));
                }
            } else if(cols.length == 8) {
                // Test if it is a module dependency refset
                if(cols[6].equals("sourceEffectiveTime") && 
                        cols[7].equals("targetEffectiveTime")) {

                    List<IRefsetMember> members = new ArrayList<>();
                    while (null != (line = br.readLine())) {
                        cols = line.split("[\t]");
                        boolean active = cols[2].equals("1") ? true : false;
                        IModuleDependencyRefsetMember m = 
                                new ModuleDependencyRefsetMember(cols[0], 
                                        cols[1], active , cols[3], cols[4], 
                                        cols[5], cols[6], cols[7]);
                        members.add(m);
                    }
                    
                    IModuleDependencyRefset res = new ModuleDependencyRefset(
                            id, displayName, members);
                    
                    return res;
                } else {
                    throw new RuntimeException("Unsupported refset type with " +
                    		"columns "+Arrays.asList(cols));
                }
            } else {
                throw new RuntimeException("Only module dependency refsets " +
                		"are currently supported.");
            }
        } catch (Exception e) {
            log.error("Problem reading refset file "+refsetFile, e);
            throw new RuntimeException(e);
        } finally {
            if (br != null) {
                try {
                    br.close();
                } catch (Exception e) {
                }
            }
        }
    }

}
