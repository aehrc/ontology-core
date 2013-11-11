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
import au.csiro.ontology.model.Axiom;
import au.csiro.ontology.model.Concept;
import au.csiro.ontology.model.ConceptInclusion;
import au.csiro.ontology.model.Conjunction;
import au.csiro.ontology.model.Datatype;
import au.csiro.ontology.model.Existential;
import au.csiro.ontology.model.FloatLiteral;
import au.csiro.ontology.model.NamedConcept;
import au.csiro.ontology.model.NamedFeature;
import au.csiro.ontology.model.NamedRole;
import au.csiro.ontology.model.Operator;
import au.csiro.ontology.util.NullProgressMonitor;


/**
 * Unit tests for {@link RF2Importer}.
 * 
 * @author Alejandro Metke
 * 
 */
public class TestRF2Importer {
    
    /**
     * Tests the main functionality of the importer. The results are based on the meta data file which contains 
     * information only for the 20110731 and 20120131 releases.
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
    
    /**
     * Tests the concrete domains import functionality.
     * @throws ImportException 
     */
    @Test
    public void testConcreteDomains() throws ImportException {
        RF2Importer rf2i = new RF2Importer(this.getClass().getResourceAsStream("/config-amtv3.xml"));

        Iterator<Ontology> it = rf2i.getOntologyVersions(new NullProgressMonitor());
        Assert.assertTrue(it.hasNext());
        Ontology ont = it.next();
        
        // Look for codeine 6.25 mg | codeine phosphate 8 mg + paracetamol 500 mg tablet (medicinal product unit of use)
        boolean foundIt = false;
        for(Axiom axiom : ont.getStatedAxioms()) {
            if(axiom instanceof ConceptInclusion) {
                ConceptInclusion ci = (ConceptInclusion) axiom;
                Concept c = ci.getLhs();
                if(c instanceof NamedConcept) {
                    NamedConcept lhs = (NamedConcept) c;
                    if(lhs.getId().equals("22843011000036101")) {
                        foundIt = true;
                        System.out.println(axiom);
                        
                        Concept cr = ci.getRhs();
                        Assert.assertTrue(cr instanceof Conjunction);
                        Conjunction rhs = (Conjunction) cr;
                        
                        Concept[] concepts = rhs.getConcepts();
                        Assert.assertEquals(6, concepts.length);
                        
                        // First existential
                        Existential e1 = (Existential) concepts[0];
                        NamedRole nr1 = (NamedRole) e1.getRole();
                        Assert.assertEquals("roleGroup", nr1.getId());
                        Conjunction conj1 = (Conjunction) e1.getConcept();
                        Assert.assertEquals(2, conj1.getConcepts().length);
                        Existential e1b = (Existential) conj1.getConcepts()[0];
                        NamedRole nr1b = (NamedRole) e1b.getRole();
                        Assert.assertEquals("30364011000036101", nr1b.getId());
                        Conjunction conj1b = (Conjunction) e1b.getConcept();
                        Existential e1c = (Existential) conj1b.getConcepts()[0];
                        NamedRole nr1c = (NamedRole) e1c.getRole();
                        Assert.assertEquals("roleGroup", nr1c.getId());
                        Conjunction conj1c = (Conjunction) e1c.getConcept();
                        Assert.assertEquals(2, conj1c.getConcepts().length);
                        Datatype dt1 = (Datatype) conj1c.getConcepts()[0];
                        NamedFeature f1 = (NamedFeature) dt1.getFeature();
                        Assert.assertEquals("700000111000036105", f1.getId());
                        FloatLiteral lit = (FloatLiteral) dt1.getLiteral();
                        Assert.assertEquals(8, lit.getValue(), 0.001f);
                        Assert.assertTrue(dt1.getOperator() == Operator.EQUALS);
                        Assert.assertEquals("1979011000036106", ((NamedConcept) conj1b.getConcepts()[1]).getId());
                        Existential e1d = (Existential) conj1c.getConcepts()[1];
                        Assert.assertEquals("700000801000036102", ((NamedConcept) e1d.getConcept()).getId());
                        Assert.assertEquals("UNIT", ((NamedRole) e1d.getRole()).getId());
                        Existential e1e = (Existential) conj1.getConcepts()[1];
                        Assert.assertEquals("1978011000036103", ((NamedConcept) e1e.getConcept()).getId());
                        Assert.assertEquals("700000081000036101", ((NamedRole) e1e.getRole()).getId());
                        
                        // Second existential
                        Existential e2 = (Existential) concepts[1];
                        NamedRole nr2 = (NamedRole) e2.getRole();
                        Assert.assertEquals("roleGroup", nr2.getId());
                        Conjunction conj2 = (Conjunction) e2.getConcept();
                        Assert.assertEquals(2, conj2.getConcepts().length);
                        Existential e2b = (Existential) conj2.getConcepts()[0];
                        NamedRole nr2b = (NamedRole) e2b.getRole();
                        Assert.assertEquals("30364011000036101", nr2b.getId());
                        Conjunction conj2b = (Conjunction) e2b.getConcept();
                        Existential e2c = (Existential) conj2b.getConcepts()[0];
                        NamedRole nr2c = (NamedRole) e2c.getRole();
                        Assert.assertEquals("roleGroup", nr2c.getId());
                        Conjunction conj2c = (Conjunction) e2c.getConcept();
                        Assert.assertEquals(2, conj2c.getConcepts().length);
                        Datatype dt2 = (Datatype) conj2c.getConcepts()[0];
                        NamedFeature f2 = (NamedFeature) dt2.getFeature();
                        Assert.assertEquals("700000111000036105", f2.getId());
                        FloatLiteral lit2 = (FloatLiteral) dt2.getLiteral();
                        Assert.assertEquals(500, lit2.getValue(), 0.001f);
                        Assert.assertTrue(dt2.getOperator() == Operator.EQUALS);
                        Assert.assertEquals("2442011000036104", ((NamedConcept) conj2b.getConcepts()[1]).getId());
                        Existential e2d = (Existential) conj2c.getConcepts()[1];
                        Assert.assertEquals("700000801000036102", ((NamedConcept) e2d.getConcept()).getId());
                        Assert.assertEquals("UNIT", ((NamedRole) e2d.getRole()).getId());
                        Existential e2e = (Existential) conj2.getConcepts()[1];
                        Assert.assertEquals("2442011000036104", ((NamedConcept) e2e.getConcept()).getId());
                        Assert.assertEquals("700000081000036101", ((NamedRole) e2e.getRole()).getId());
                        
                        
                        // Third existential
                        Existential e3 = (Existential) concepts[2];
                        Existential e3b = (Existential) e3.getConcept();
                        Assert.assertEquals("154011000036109", ((NamedConcept) e3b.getConcept()).getId());
                        Assert.assertEquals("30523011000036108", ((NamedRole) e3b.getRole()).getId());
                        Assert.assertEquals("roleGroup", ((NamedRole) e3.getRole()).getId());
                        
                        // Fourth existential
                        Existential e4 = (Existential) concepts[3];
                        Existential e4b = (Existential) e4.getConcept();
                        Conjunction conj4 = (Conjunction) e4b.getConcept();
                        Existential e4c = (Existential) conj4.getConcepts()[0];
                        Conjunction conj4b = (Conjunction) e4c.getConcept();
                        Datatype dt4 = (Datatype) conj4b.getConcepts()[0];
                        NamedFeature f4 = (NamedFeature) dt4.getFeature();
                        Assert.assertEquals("700000141000036106", f4.getId());
                        FloatLiteral lit4 = (FloatLiteral) dt4.getLiteral();
                        Assert.assertEquals(1, lit4.getValue(), 0.001f);
                        Assert.assertTrue(dt4.getOperator() == Operator.EQUALS);
                        Existential e4d = (Existential) conj4b.getConcepts()[1];
                        Assert.assertEquals("63011000036109", ((NamedConcept) e4d.getConcept()).getId());
                        Assert.assertEquals("UNIT", ((NamedRole) e4d.getRole()).getId());
                        Assert.assertEquals("roleGroup", ((NamedRole) e4c.getRole()).getId());
                        Assert.assertEquals("700000381000036104", ((NamedConcept) conj4.getConcepts()[1]).getId());
                        Assert.assertEquals("30548011000036101", ((NamedRole) e4b.getRole()).getId());
                        Assert.assertEquals("roleGroup", ((NamedRole) e4.getRole()).getId());
                        
                        
                        Assert.assertEquals("21286011000036106", ((NamedConcept) concepts[4]).getId());
                        
                        Assert.assertEquals("30450011000036109", ((NamedConcept) concepts[5]).getId());
                        
                    }
                }
            }
        }
        
        Assert.assertTrue(foundIt);
    }
	
}
