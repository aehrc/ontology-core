/**
 * Copyright CSIRO Australian e-Health Research Centre (http://aehrc.com).
 * All rights reserved. Use is subject to license terms and conditions.
 */
package au.csiro.ontology.model;

import java.util.Arrays;
import java.util.Collection;
import java.util.SortedSet;
import java.util.TreeSet;

/**
 * This class represents a conjunction (also referred to as an
 * ObjectIntersectionOf in OWL).
 * 
 * @author Alejandro Metke
 * 
 */
public class Conjunction implements IConjunction {

    final private IConcept[] concepts;
    final private int hashCode;

    public Conjunction(final IConcept[] concepts) {
        final SortedSet<IConcept> sorted = new TreeSet<IConcept>();
        for (IConcept concept : concepts) {
            sorted.add(concept);
        }
        this.concepts = sorted.toArray(new IConcept[sorted.size()]);
        hashCode = sorted.hashCode();
    }

    public Conjunction(final Collection<? extends IConcept> concepts) {
        // Store the concepts in hashCode order so that equals() is order
        // independent, i.e. conjunctions are reflexive (should also be
        // transitive, but Agile says STTCPW)

        final SortedSet<IConcept> sorted = new TreeSet<>(
                concepts);
        this.concepts = sorted.toArray(new IConcept[sorted.size()]);
        hashCode = sorted.hashCode();
    }

    public IConcept[] getConcepts() {
        return concepts;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("(");
        sb.append(concepts[0]);
        for (int i = 1; i < concepts.length; i++) {
            sb.append(" + ");
            sb.append(concepts[i]);
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

    @SuppressWarnings("rawtypes")
    @Override
    public int compareTo(IConcept o) {
        Class thisClass = this.getClass();
        Class otherClass = o.getClass();
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
