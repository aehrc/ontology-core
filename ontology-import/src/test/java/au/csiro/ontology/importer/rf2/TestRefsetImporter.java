/**
 * Copyright CSIRO Australian e-Health Research Centre (http://aehrc.com).
 * All rights reserved. Use is subject to license terms and conditions.
 */
package au.csiro.ontology.importer.rf2;

import java.io.InputStream;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import junit.framework.Assert;

import org.junit.Test;

import au.csiro.ontology.importer.ImportException;
import au.csiro.ontology.snomed.refset.rf2.IModuleDependencyRefset;
import au.csiro.ontology.snomed.refset.rf2.ModuleDependency;

/**
 * Unit test cases for {@link RefsetImporter}.
 * 
 * @author Alejandro Metke
 *
 */
public class TestRefsetImporter {
    
    @Test
    public void testImportModuleDependencyRefset() throws ImportException {
        Set<InputStream> refsetFiles = new HashSet<InputStream>();
        refsetFiles.add(this.getClass().getResourceAsStream(
                "/der2_ssRefset_ModuleDependencyFull_AU1000036_20121130.txt"));
        IModuleDependencyRefset dr = 
                RefsetImporter.importModuleDependencyRefset(refsetFiles);
        
        Map<String, Map<String, ModuleDependency>> deps = dr.getModuleDependencies();
        
        // Test for 32506021000036107 (20121130) -> 
        // 900000000000207008 (20120731) -> 900000000000012004 (20120731)
        ModuleDependency md = deps.get("32506021000036107").get("20121130");
        Assert.assertEquals("32506021000036107", md.getId());
        Assert.assertEquals("20121130", md.getVersion());
        Assert.assertEquals(2, md.getDependencies().size());
        
        Assert.assertTrue(md.getDependencies().contains(new ModuleDependency("900000000000207008", "20120731")));
        Assert.assertTrue(md.getDependencies().contains(new ModuleDependency("900000000000012004", "20120731")));

        // Another test
        md = deps.get("32570491000036106").get("20120531");
        Assert.assertEquals("32570491000036106", md.getId());
        Assert.assertEquals("20120531", md.getVersion());
        Assert.assertEquals(3, md.getDependencies().size());
        
        Assert.assertTrue(md.getDependencies().contains(new ModuleDependency("32506021000036107", "20120531")));
        Assert.assertTrue(md.getDependencies().contains(new ModuleDependency("900000000000207008", "20120131")));
        Assert.assertTrue(md.getDependencies().contains(new ModuleDependency("900000000000012004", "20120131")));

        // This should have no entry
        Assert.assertNull(deps.get("900000000000012004"));
    }
}
