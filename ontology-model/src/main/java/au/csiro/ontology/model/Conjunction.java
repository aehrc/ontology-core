/**
 * Copyright CSIRO Australian e-Health Research Centre (http://aehrc.com).
 * All rights reserved. Use is subject to license terms and conditions.
 */
package au.csiro.ontology.model;

import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.SortedSet;
import java.util.TreeSet;

import javax.xml.bind.annotation.XmlRootElement;

/**
 * This class represents a conjunction (also referred to as an ObjectIntersectionOf in OWL).
 *
 * @author Alejandro Metke
 *
 */
@XmlRootElement
public class Conjunction extends Concept {

    private static final long serialVersionUID = 1L;

    private Concept[] concepts;

    private int hashCode;

    /**
     *
     */
    @SuppressWarnings("unchecked")
    public Conjunction() {
        this(Collections.EMPTY_SET);
    }

    /**
     *
     * @param concepts
     */
    public Conjunction(final Concept[] concepts) {
        setConcepts(concepts);
    }

    public Conjunction(final Collection<? extends Concept> concepts) {
        // Store the concepts in hashCode order so that equals() is order
        // independent, i.e. conjunctions are reflexive (should also be
        // transitive, but Agile says STTCPW)

        final SortedSet<Concept> sorted = new TreeSet<Concept>(concepts);
        this.concepts = sorted.toArray(new Concept[sorted.size()]);
        hashCode = sorted.hashCode();
    }

    public Concept[] getConcepts() {
        return concepts;
    }

    /**
     * @param concepts the concepts to set
     */
    public void setConcepts(Concept[] concepts) {
        final SortedSet<Concept> sorted = new TreeSet<Concept>();
        for (Concept concept : concepts) {
            sorted.add(concept);
        }
        this.concepts = sorted.toArray(new Concept[sorted.size()]);
        hashCode = sorted.hashCode();
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("(");
        if (concepts.length > 0) {
            sb.append(concepts[0]);
            for (int i = 1; i < concepts.length; i++) {
                sb.append(" + ");
                sb.append(concepts[i]);
            }
        }
        sb.append(")");
        return sb.toString();
    }

    @Override
    public int hashCode() {
        return hashCode;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        final Conjunction other = (Conjunction) obj;
        return hashCode == other.hashCode
                && Arrays.equals(concepts, other.concepts);
    }

    @Override
    public int compareTo(Concept o) {
        Class<? extends Conjunction> thisClass = this.getClass();
        Class<? extends Concept> otherClass = o.getClass();
        if(thisClass.equals(otherClass)) {
            Conjunction other = (Conjunction)o;
            // Equal if all concepts equal
            // Otherwise order depends on the length and then on the order of
            // first different concept

            int res = 0;
            res = concepts.length - other.concepts.length;
            if(res != 0) return res;

            for(int i = 0; i < concepts.length; i++) {
                try {
                    res = concepts[i].compareTo(other.concepts[i]);
                } catch(ClassCastException e) {
                    // Need to catch this because elements in the conjunction
                    // might be of different types
                    res = concepts[i].getClass().toString().compareTo(
                            other.concepts[i].getClass().toString());
                }

                if(res != 0) return res;
            }

            return 0;
        } else {
            return thisClass.toString().compareTo(otherClass.toString());
        }
    }

}
