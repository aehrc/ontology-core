/**
 * Copyright CSIRO Australian e-Health Research Centre (http://aehrc.com).
 * All rights reserved. Use is subject to license terms and conditions.
 */
package au.csiro.ontology.importer.rf2;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;

import org.apache.log4j.Logger;

import au.csiro.ontology.snomed.refset.rf2.IModuleDependencyRefset;
import au.csiro.ontology.snomed.refset.rf2.IModuleDependencyRefsetMember;
import au.csiro.ontology.snomed.refset.rf2.IRefset;
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
     * Imports a refset from a file.
     * 
     * @param refsetFile
     * @return
     */
    public static IRefset importRefset(File refsetFile) {
        BufferedReader br = null;
        try {
            br = new BufferedReader(new FileReader(refsetFile));
            String line = br.readLine();
            
            String[] cols = line.split("[\t]");
            assert(cols.length >= 6);
            
            if(cols.length == 8) {
                // Test if it is a module dependency refset
                if(cols[6].equals("sourceEffectiveTime") && 
                        cols[7].equals("targetEffectiveTime")) {
                    
                    IModuleDependencyRefset res = new ModuleDependencyRefset(
                            refsetFile.getName(), refsetFile.getName());
                    
                    while (null != (line = br.readLine())) {
                        cols = line.split("[\t]");
                        boolean active = cols[2].equals("1") ? true : false;
                        IModuleDependencyRefsetMember m = 
                                new ModuleDependencyRefsetMember(cols[0], 
                                        cols[1], active , cols[3], cols[4], 
                                        cols[5], cols[6], cols[7]);
                        res.getMembers().add(m);
                    }
                    
                    return res;
                } else {
                    throw new RuntimeException("Only module dependency " +
                    		"refsets are currently supported.");
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
