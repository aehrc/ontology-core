package au.csiro.ontology.util;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.Collection;

import org.junit.Test;

import au.csiro.ontology.model.Axiom;
import au.csiro.ontology.model.Concept;
import au.csiro.ontology.model.ConceptInclusion;
import au.csiro.ontology.model.Conjunction;
import au.csiro.ontology.model.Datatype;
import au.csiro.ontology.model.Existential;
import au.csiro.ontology.model.FloatLiteral;
import au.csiro.ontology.model.IntegerLiteral;
import au.csiro.ontology.model.NamedConcept;
import au.csiro.ontology.model.NamedFeature;
import au.csiro.ontology.model.NamedRole;
import au.csiro.ontology.model.Operator;

public class AxiomUtilsTests {
    
    @Test
    public void testConcepts() {
        Concept c1 = new NamedConcept("A");
        Concept c2 = new Existential(new NamedRole("r"), c1);
        Concept c3 = new Datatype(new NamedFeature("f"), Operator.EQUALS, new IntegerLiteral(21));
        Concept c4 = new Conjunction(new Concept[] {c1, c2});
        
        String s1 = AxiomUtils.serialiseConcept(c1);
        String s2 = AxiomUtils.serialiseConcept(c2);
        String s3 = AxiomUtils.serialiseConcept(c3);
        String s4 = AxiomUtils.serialiseConcept(c4);
        
        //System.out.println("Concept 1: "+s1);
        //System.out.println("Concept 2: "+s2);
        //System.out.println("Concept 3: "+s3);
        //System.out.println("Concept 4: "+s4);
        
        Concept c1b = AxiomUtils.deserialiseConcept(s1);
        Concept c2b = AxiomUtils.deserialiseConcept(s2);
        Concept c3b = AxiomUtils.deserialiseConcept(s3);
        Concept c4b = AxiomUtils.deserialiseConcept(s4);
        assertEquals(c1,  c1b);
        assertEquals(c2,  c2b);
        assertEquals(c3,  c3b);
        assertNotNull(c4b);
        //assertEquals(c4,  c4b); FIXME: deserialisation is not keeping conjunction order!
    }
    
    @Test
    public void testAxioms() {
        
        Axiom ax1 = createAxiom("A", "B");
        Axiom ax2 = createAxiom("C", "D", "E", "F");
        Axiom ax3 = createAxiom("D", new Existential(new NamedRole("r"), new NamedConcept("V")));
        FloatLiteral fl = new FloatLiteral();
        fl.setValue(2.0f);
        Axiom ax4 = new ConceptInclusion(new NamedConcept("A"), new Conjunction(new Concept[] {new NamedConcept("B"), 
                new Datatype(new NamedFeature("f"), Operator.EQUALS, fl)}));
        String a1 = AxiomUtils.serialise(ax1);
        String a2 = AxiomUtils.serialise(ax2);
        String a3 = AxiomUtils.serialise(ax3);
        String a4 = AxiomUtils.serialise(ax4);
        
        //System.out.println("Axiom 1: "+a1);
        //System.out.println("Axiom 2: "+a2);
        //System.out.println("Axiom 3: "+a3);
        //System.out.println("Axiom 4: "+a4);
        
        Axiom ax1b = AxiomUtils.deserialise(a1);
        assertTrue(ax1b instanceof ConceptInclusion);
        ConceptInclusion ci1 = (ConceptInclusion) ax1b;
        assertTrue(ci1.getLhs() instanceof NamedConcept);
        assertTrue(ci1.getRhs() instanceof NamedConcept);
        assertEquals(ax1, ax1b);
        
        Axiom ax2b = AxiomUtils.deserialise(a2);
        assertTrue(ax2b instanceof ConceptInclusion);
        ConceptInclusion ci2 = (ConceptInclusion) ax2b;
        assertTrue(ci2.getLhs() instanceof NamedConcept);
        assertTrue(ci2.getRhs() instanceof Conjunction);
        assertEquals(ax2, ax2b);
        
        Axiom ax3b = AxiomUtils.deserialise(a3);
        assertTrue(ax3b instanceof ConceptInclusion);
        ConceptInclusion ci3 = (ConceptInclusion) ax3b;
        assertTrue(ci3.getLhs() instanceof NamedConcept);
        assertTrue(ci3.getRhs() instanceof Existential);
        assertEquals(ax3, ax3b);
        
        Axiom ax4b = AxiomUtils.deserialise(a4);
        assertTrue(ax4b instanceof ConceptInclusion);
        ConceptInclusion ci4 = (ConceptInclusion) ax4b;
        assertTrue(ci4.getLhs() instanceof NamedConcept);
        assertTrue(ci4.getRhs() instanceof Conjunction);
        //assertEquals(ax4, ax4b); FIXME: deserialisation not keeping conjunction order!
    }

    private Axiom createAxiom(String c1, Concept concept) {
        Concept lhs = new NamedConcept(c1);
        Concept rhs = concept;
        return new ConceptInclusion(lhs, rhs);
    }

    private Axiom createAxiom(String c1, String c2) {
        Concept lhs = new NamedConcept(c1);
        Concept rhs = new NamedConcept(c2);
        return new ConceptInclusion(lhs, rhs);
    }

    private Axiom createAxiom(String c1, String... cs) {
        Concept lhs = new NamedConcept(c1);
        Collection<Concept> concepts = new ArrayList<Concept>();
        for (final String c: cs) {
            concepts.add(new NamedConcept(c));
        }
        Conjunction rhs = new Conjunction(concepts);
        return new ConceptInclusion(lhs, rhs);
    }

}
