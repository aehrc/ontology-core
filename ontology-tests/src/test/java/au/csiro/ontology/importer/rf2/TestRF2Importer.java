/**
 * Copyright CSIRO Australian e-Health Research Centre (http://aehrc.com).
 * All rights reserved. Use is subject to license terms and conditions.
 */
package au.csiro.ontology.importer.rf2;

import java.util.Iterator;

import junit.framework.Assert;

import org.junit.Test;

import au.csiro.ontology.Ontology;
import au.csiro.ontology.importer.ImportException;
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
     * @throws ImportException 
     */
    @Test
    public void testGetOntologyVersions() throws ImportException {
        RF2Importer rf2i = new RF2Importer(this.getClass().getResourceAsStream("/config-snomed.xml"));

        Iterator<Ontology> it = rf2i.getOntologyVersions(new NullProgressMonitor());
        
        int num = 0;
        while(it.hasNext()) {
            it.next();
            num++;
        }
        
        Assert.assertEquals(2, num);
    }
	
}
