/**
 * Copyright CSIRO Australian e-Health Research Centre (http://aehrc.com).
 * All rights reserved. Use is subject to license terms and conditions.
 */
package au.csiro.ontology.importer.rf1;

import java.io.File;
import java.io.FileInputStream;
import java.util.Map;

import junit.framework.Assert;

import org.junit.Test;

import au.csiro.ontology.IOntology;
import au.csiro.ontology.classification.NullProgressMonitor;


/**
 * Unit tests for {@link RF1Importer}.
 * 
 * @author Alejandro Metke
 * 
 */
public class TestRF1Importer {

    final static String TEST_DIR = "src/test/resources/";

    /**
     * Tests the main functionality of the importer. The results are based on
     * the meta data file which contains information only for the 20110731 and
     * 20120131 releases.
     */
    @Test
    public void testGetOntologyVersions() {
        RF1Importer rf1i = new RF1Importer(
                this.getClass().getResourceAsStream("/sct1_Concepts_Core_INT_20110731.txt"), 
                this.getClass().getResourceAsStream("/sct1_Relationships_Core_INT_20110731.txt"),
                "20110731");

        Map<String, Map<String, IOntology<String>>> ovs = 
                rf1i.getOntologyVersions(new NullProgressMonitor());
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
            RF1Importer rf1i = new RF1Importer(
                    new FileInputStream(new File(TEST_DIR + "rf1_con_test.txt")), 
                    new FileInputStream(new File(TEST_DIR + "rf1_rel_test.txt")), 
                    "20110731");
            VersionRows vr1 = rf1i.extractVersionRows();
            Assert.assertEquals("20110731", vr1.getVersionName());
            Assert.assertEquals(11, vr1.getConceptRows().size());
            Assert.assertEquals(13, vr1.getRelationshipRows().size());
        } catch(Exception e) {
            e.printStackTrace();
            Assert.assertTrue(false);
        }
    }

}
