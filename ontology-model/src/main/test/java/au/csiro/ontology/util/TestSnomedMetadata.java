/**
 * Copyright CSIRO Australian e-Health Research Centre (http://aehrc.com).
 * All rights reserved. Use is subject to license terms and conditions.
 */
package au.csiro.ontology.util;

import java.util.Map;
import java.util.Set;

import org.junit.Test;

import au.csiro.ontology.util.SnomedMetadata;

import junit.framework.Assert;

/**
 * Unit test cases for the {@link SnomedMetadata} class.
 * 
 * @author Alejandro Metke
 * 
 */
public class TestSnomedMetadata {

    @Test
    public void testGetConceptModelAttId() {
        SnomedMetadata smd = SnomedMetadata.INSTANCE;
        String cmid = smd.getConceptModelAttId("20110731");
        Assert.assertEquals("410662002", cmid);

        cmid = smd.getConceptModelAttId("20120131");
        Assert.assertEquals("410662002", cmid);

        cmid = smd.getConceptModelAttId("20020131");
        Assert.assertNull(cmid);
    }

    public void testGetNeverGroupedIds() {
        SnomedMetadata smd = SnomedMetadata.INSTANCE;
        Set<String> ngids = smd.getNeverGroupedIds("20110731");
        Assert.assertEquals(4, ngids.size());

        ngids = smd.getNeverGroupedIds("20120131");
        Assert.assertEquals(4, ngids.size());

        ngids = smd.getNeverGroupedIds("20020131");
        Assert.assertNull(ngids);
    }

    public void testGetRightIdentities() {
        SnomedMetadata smd = SnomedMetadata.INSTANCE;
        Map<String, String> rids = smd.getRightIdentities("20110731");
        Assert.assertEquals("127489000", rids.get("363701004"));

        rids = smd.getRightIdentities("20120131");
        Assert.assertEquals("127489000", rids.get("363701004"));

        rids = smd.getRightIdentities("20020131");
        Assert.assertNull(rids);
    }
}
