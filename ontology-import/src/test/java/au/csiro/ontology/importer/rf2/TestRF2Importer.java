/**
 * Copyright CSIRO Australian e-Health Research Centre (http://aehrc.com).
 * All rights reserved. Use is subject to license terms and conditions.
 */
package au.csiro.ontology.importer.rf2;

import java.util.Collections;
import java.util.Iterator;

import org.junit.Test;

import au.csiro.ontology.Ontology;
import au.csiro.ontology.input.Input.InputType;
import au.csiro.ontology.input.Inputs.ReleaseType;
import au.csiro.ontology.input.RF2Input;
import junit.framework.Assert;

/**
 * Unit tests for {@link RF2Importer}.
 *
 * @author Michael Lawley
 *
 */
public class TestRF2Importer {

    @Test
    public void testExtractVersionRows() {
        try {
            final RF2Input input = new RF2Input();
            input.setInputType(InputType.CLASSPATH);
            input.setReleaseType(ReleaseType.FULL);
            input.setModuleDependenciesRefsetFiles(Collections.singleton("/der2_ssRefset_ModuleDependencyFull_AU1000036_20121130.txt"));
            input.setConceptsFiles(Collections.singleton("/rf2_full_con_test.txt"));
            input.setRelationshipsFiles(Collections.singleton("/rf2_full_rel_test.txt"));

            final RF2Importer rf2i = new RF2Importer(input);
            final Iterator<Ontology> itr = rf2i.getOntologyVersions(null);
            while (itr.hasNext()) {
                Ontology o = itr.next();
                System.err.println(o);
            }
//                    "20110731");
        } catch(Exception e) {
            e.printStackTrace();
            Assert.assertTrue(false);
        }
    }

}
