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
        
        Map<String, Map<String, ModuleDependency>> deps = 
                dr.getModuleDependencies();
        
        // Test for 32506021000036107 (20121130) -> 
        // 900000000000207008 (20120731) -> 900000000000012004 (20120731)
        ModuleDependency md = deps.get("32506021000036107").get("20121130");
        Assert.assertEquals("32506021000036107", md.getId());
        Assert.assertEquals("20121130", md.getVersion());
        Assert.assertEquals(1, md.getDependencies().size());
        
        ModuleDependency md2 = md.getDependencies().iterator().next();
        Assert.assertEquals("900000000000207008", md2.getId());
        Assert.assertEquals("20120731", md2.getVersion());
        Assert.assertEquals(1, md2.getDependencies().size());
        
        ModuleDependency md3 = md2.getDependencies().iterator().next();
        Assert.assertEquals("900000000000012004", md3.getId());
        Assert.assertEquals("20120731", md3.getVersion());
        Assert.assertEquals(0, md3.getDependencies().size());
        
        // Test for 32570491000036106 (20120531) -> 
        // 32506021000036107 (20120531) -> 900000000000207008 (20120131) -> 
        // 900000000000012004 (20120131)
        md = deps.get("32570491000036106").get("20120531");
        Assert.assertEquals("32570491000036106", md.getId());
        Assert.assertEquals("20120531", md.getVersion());
        Assert.assertEquals(1, md.getDependencies().size());
        
        md2 = deps.get("32506021000036107").get("20120531");
        Assert.assertEquals("32506021000036107", md2.getId());
        Assert.assertEquals("20120531", md2.getVersion());
        Assert.assertEquals(1, md2.getDependencies().size());
        
        md3 = md2.getDependencies().iterator().next();
        Assert.assertEquals("900000000000207008", md3.getId());
        Assert.assertEquals("20120131", md3.getVersion());
        Assert.assertEquals(1, md3.getDependencies().size());
        
        ModuleDependency md4 = md3.getDependencies().iterator().next();
        Assert.assertEquals("900000000000012004", md4.getId());
        Assert.assertEquals("20120131", md4.getVersion());
        Assert.assertEquals(0, md4.getDependencies().size());
    }
}
