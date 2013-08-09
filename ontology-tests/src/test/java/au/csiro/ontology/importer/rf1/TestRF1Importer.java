/**
 * Copyright CSIRO Australian e-Health Research Centre (http://aehrc.com).
 * All rights reserved. Use is subject to license terms and conditions.
 */
package au.csiro.ontology.importer.rf1;

import java.util.Iterator;

import junit.framework.Assert;

import org.junit.Test;

import au.csiro.ontology.Ontology;
import au.csiro.ontology.util.NullProgressMonitor;


/**
 * Unit tests for {@link RF1Importer}.
 * 
 * @author Alejandro Metke
 * 
 */
public class TestRF1Importer {

    /**
     * Tests the main functionality of the importer. The results are based on
     * the meta data file which contains information only for the 20110731 and
     * 20120131 releases.
     */
    @Test
    public void testGetOntologyVersions() {
        RF1Importer rf1i = new RF1Importer(
                this.getClass().getResourceAsStream(
                        "/snomed_int_rf1/Terminology/Content/sct1_Concepts_Core_INT_20110731.txt"), 
                this.getClass().getResourceAsStream(
                        "/snomed_int_rf1/Terminology/Content/sct1_Relationships_Core_INT_20110731.txt"),
                "20110731");
        
        Iterator<Ontology> it = rf1i.getOntologyVersions(new NullProgressMonitor());
        
        int num = 0;
        while(it.hasNext()) {
            it.next();
            num++;
        }
        
        Assert.assertEquals(1, num);
    }

}
