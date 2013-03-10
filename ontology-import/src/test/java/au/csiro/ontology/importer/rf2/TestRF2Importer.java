/**
 * Copyright CSIRO Australian e-Health Research Centre (http://aehrc.com).
 * All rights reserved. Use is subject to license terms and conditions.
 */
package au.csiro.ontology.importer.rf2;

import java.util.Map;

import junit.framework.Assert;

import org.junit.Test;

import au.csiro.ontology.IOntology;
import au.csiro.ontology.importer.Inputs.ReleaseType;
import au.csiro.ontology.util.NullProgressMonitor;


/**
 * Unit tests for {@link RF2Importer}.
 * 
 * @author Alejandro Metke
 * 
 */
public class TestRF2Importer {
    
    /**
     * Tests the main functionality of the importer. The results are based on
     * the meta data file which contains information only for the 20110731 and
     * 20120131 releases.
     */
    @Test
    public void testGetOntologyVersions() {
        RF2Importer rf2i = new RF2Importer(
            this.getClass().getResourceAsStream(
                    "/snomed_int_full_rf2/Terminology/" +
                    "sct2_Concept_Full_INT_20120131.txt"), 
            this.getClass().getResourceAsStream(
                    "/snomed_int_full_rf2/Terminology/" +
                    "sct2_StatedRelationship_Full_INT_20120131.txt"),
            this.getClass().getResourceAsStream(
                    "/snomed_int_full_rf2/Terminology/" +
                    "der2_ssRefset_ModuleDependencyFull_INT_20120131.txt"),
            ReleaseType.FULL);
        Map<String, Map<String, IOntology<String>>> ovs = 
                rf2i.getOntologyVersions(new NullProgressMonitor());
        Assert.assertEquals(1, ovs.keySet().size());
    }

    /**
     * Tests the assembly of versions from the raw data. The results are based
     * on the meta data file which contains information only for the 20110731
     * and 20120131 releases.
     */
    @Test
    public void testExtractVersionRows() {
        try {
            RF2Importer rf2i = new RF2Importer(
                    this.getClass().getResourceAsStream(
                            "/rf2_full_con_test.txt"), 
                    this.getClass().getResourceAsStream(
                            "/rf2_full_rel_test.txt"), 
                    null,
                    ReleaseType.FULL);
            
            Map<String, Module> res = rf2i.extractModules();
            
            // The map should contain a single module
            Assert.assertEquals(1, res.size());
            
            Module m = res.get("900000000000207008");
            Map<String, VersionRows> vMap = m.getVersions();
            
            VersionRows vr1 = vMap.get("20020131");
            Assert.assertEquals(2, vr1.getConceptRows().size());
            Assert.assertEquals(1, vr1.getRelationshipRows().size());
    
            VersionRows vr2 = vMap.get("20030131");
            Assert.assertEquals(3, vr2.getConceptRows().size());
            Assert.assertEquals(3, vr2.getRelationshipRows().size());
    
            VersionRows vr3 = vMap.get("20110731");
            Assert.assertEquals(3, vr3.getConceptRows().size());
            Assert.assertEquals(3, vr3.getRelationshipRows().size());
    
            VersionRows vr4 = vMap.get("20120131");
            Assert.assertEquals(3, vr4.getConceptRows().size());
            Assert.assertEquals(3, vr4.getRelationshipRows().size());
        } catch(Exception e) {
            e.printStackTrace();
            Assert.assertTrue(false);
        }
    }

}
