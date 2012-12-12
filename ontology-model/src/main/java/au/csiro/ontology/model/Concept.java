/**
 * Copyright CSIRO Australian e-Health Research Centre (http://aehrc.com).
 * All rights reserved. Use is subject to license terms and conditions.
 */
package au.csiro.ontology.model;

/**
 * This class represents a named concept (also referred to as a class in OWL).
 * 
 * @author Alejandro Metke
 * @param <T>
 * 
 */
public class Concept<T extends Comparable<T>> implements INamedConcept<T> {
    
    /**
     * Serialisation version.
     */
    private static final long serialVersionUID = 1L;

    /**
     * Represents the top concept.
     */
    public static IConcept TOP = new Concept<>();
    
    /**
     * Represents the bottom concept.
     */
    public static IConcept BOTTOM = new Concept<>();
    
    /**
     * String identifier of this concept.
     */
    protected final T id;
    
    /**
     * Private constructor.
     */
    private Concept() {
        id = null;
    }
    
    /**
     * This method must be invoked after deserialising a reasoner because top
     * and bottom are just plain object references.
     */
    public static void reconnectTopBottom(IConcept top, IConcept bottom) {
        TOP = top;
        BOTTOM = bottom;
    }

    /**
     * Creates a new Concept.
     * 
     * @param id
     *            The concept's identifier.
     */
    public Concept(T id) {
        assert(id != null);
        this.id = id;
    }

    /**
     * Returns this concept's identifier.
     * 
     * @return The identifier.
     */
    public T getId() {
        return id;
    }

    @Override
    public String toString() {
        if(this == TOP) return "_top_";
        else if(this == BOTTOM) return "_bottom_";
        else return id.toString();
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((id == null) ? 0 : id.hashCode());
        return result;
    }

    @SuppressWarnings("rawtypes")
    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        Concept other = (Concept) obj;
        if (id == null) {
            /*
            if (other.id != null)
                return false;
            else
                assert(false);
            */
            // We return false because id is null, this is either TOP or BOTTOM
            // and if obj is not == this then they are not the same objects
            return false;
        } else if (!id.equals(other.id))
            return false;
        return true;
    }

    @SuppressWarnings({ "unchecked", "rawtypes" })
    @Override
    public int compareTo(IConcept o) {
        Class thisClass = this.getClass();
        Class otherClass = o.getClass();
        if(thisClass.equals(otherClass)) {
            Concept<T> other = (Concept<T>)o;
            return id.compareTo(other.id);
        } else {
            return thisClass.toString().compareTo(otherClass.toString());
        }
    }

}
