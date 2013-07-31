/**
 * Copyright CSIRO Australian e-Health Research Centre (http://aehrc.com).
 * All rights reserved. Use is subject to license terms and conditions.
 */
package au.csiro.ontology.importer.rf1;

import junit.framework.Assert;

import org.junit.Test;


/**
 * Unit tests for {@link RF1Importer}.
 * 
 * @author Alejandro Metke
 * 
 */
public class TestRF1Importer {

    /**
     * Tests the assembly of versions from the raw data. The results are based
     * on the meta data file which contains information only for the 20110731
     * and 20120131 releases.
     */
    @Test
    public void testExtractVersionRows() {
        try {
            RF1Importer rf1i = new RF1Importer(
                    this.getClass().getResourceAsStream("/rf1_con_test.txt"), 
                    this.getClass().getResourceAsStream("/rf1_rel_test.txt"), 
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
