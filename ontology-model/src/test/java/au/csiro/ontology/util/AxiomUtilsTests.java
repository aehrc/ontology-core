package au.csiro.ontology.util;

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
        String a1 = AxiomUtils.serialise(createAxiom("A", "B"));
        String a2 = AxiomUtils.serialise(createAxiom("C", "D", "E", "F"));
        String a3 = AxiomUtils.serialise(createAxiom("D", new Existential(new NamedRole("r"), new NamedConcept("V"))));
        
        System.out.println("Axiom 1: "+a1);
        System.out.println("Axiom 2: "+a2);
        System.out.println("Axiom 3: "+a3);
        
        Axiom ax1 = AxiomUtils.deserialise(a1);
        assertTrue(ax1 instanceof ConceptInclusion);
        ConceptInclusion ci1 = (ConceptInclusion) ax1;
        assertTrue(ci1.getLhs() instanceof NamedConcept);
        assertTrue(ci1.getRhs() instanceof NamedConcept);
        
        Axiom ax2 = AxiomUtils.deserialise(a2);
        assertTrue(ax2 instanceof ConceptInclusion);
        ConceptInclusion ci2 = (ConceptInclusion) ax2;
        assertTrue(ci2.getLhs() instanceof NamedConcept);
        assertTrue(ci2.getRhs() instanceof Conjunction);
        
        Axiom ax3 = AxiomUtils.deserialise(a3);
        assertTrue(ax3 instanceof ConceptInclusion);
        ConceptInclusion ci3 = (ConceptInclusion) ax3;
        assertTrue(ci3.getLhs() instanceof NamedConcept);
        assertTrue(ci3.getRhs() instanceof Existential);
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
