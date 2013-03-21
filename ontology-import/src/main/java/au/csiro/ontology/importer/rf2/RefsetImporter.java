/**
 * Copyright CSIRO Australian e-Health Research Centre (http://aehrc.com).
 * All rights reserved. Use is subject to license terms and conditions.
 */
package au.csiro.ontology.importer.rf2;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import org.apache.log4j.Logger;

import au.csiro.ontology.importer.ImportException;
import au.csiro.ontology.snomed.refset.rf2.IModuleDependencyRefset;
import au.csiro.ontology.snomed.refset.rf2.ModuleDependencyRefset;
import au.csiro.ontology.snomed.refset.rf2.ModuleDependencyRow;

/**
 * Imports RF2 reference sets.
 * 
 * @author Alejandro Metke
 *
 */
public class RefsetImporter {
    
    // Logger
    private final static Logger log = Logger.getLogger(RefsetImporter.class);
    
    /**
     * Imports a module dependency reference set from a {@link Set} of 
     * {@link InputStream}s. This method closes the {@link InputStream}s after 
     * loading the reference sets.
     * 
     * @param refsetFiles The input streams.
     * @return
     */
    public static IModuleDependencyRefset importModuleDependencyRefset(
            Set<InputStream> refsetFiles) {
        
        Set<ModuleDependencyRow> members = new HashSet<>();
        for(InputStream refsetFile : refsetFiles) {
            try (BufferedReader br = new BufferedReader(
                    new InputStreamReader(refsetFile))){
                String line = br.readLine();
                
                String[] cols = line.split("[\t]");
                assert(cols.length >= 6);
                
                if(cols.length == 8) {
                    // Test if it is a module dependency refset
                    if(cols[6].equals("sourceEffectiveTime") && 
                            cols[7].equals("targetEffectiveTime")) {

                        while (null != (line = br.readLine())) {
                            cols = line.split("[\t]");
                            boolean active = cols[2].equals("1") ? true : false;
                            ModuleDependencyRow m = 
                                    new ModuleDependencyRow(cols[0], 
                                            cols[1], active , cols[3], cols[4], 
                                            cols[5], cols[6], cols[7]);
                            members.add(m);
                        }
                    } else {
                        throw new ImportException("Malformed module " +
                        	"dependency reference set with columns "+
                                Arrays.asList(cols));
                    }
                } else {
                    throw new ImportException("Malformed module dependency " +
                    	"reference set with columns "+Arrays.asList(cols));
                }
            } catch (Exception e) {
                log.error("Problem reading refset file "+refsetFile, e);
                throw new ImportException("Problem reading refset file ", e);
            }
        }
        
        IModuleDependencyRefset res = new ModuleDependencyRefset(members);
        return res;
    }

}
