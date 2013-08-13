package au.csiro.ontology.util;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.Collection;

import org.junit.Test;

import au.csiro.ontology.model.Axiom;
import au.csiro.ontology.model.Concept;
import au.csiro.ontology.model.ConceptInclusion;
import au.csiro.ontology.model.Conjunction;
import au.csiro.ontology.model.Existential;
import au.csiro.ontology.model.NamedConcept;
import au.csiro.ontology.model.NamedRole;

public class AxiomUtilsTests {

    @Test
    public void testAxioms() {
        
        Axiom ax1 = createAxiom("A", "B");
        Axiom ax2 = createAxiom("C", "D", "E", "F");
        Axiom ax3 = createAxiom("D", new Existential(new NamedRole("r"), new NamedConcept("V")));
        String a1 = AxiomUtils.serialise(ax1);
        String a2 = AxiomUtils.serialise(ax2);
        String a3 = AxiomUtils.serialise(ax3);
        
        System.out.println("Axiom 1: "+a1);
        System.out.println("Axiom 2: "+a2);
        System.out.println("Axiom 3: "+a3);
        
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
