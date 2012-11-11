/**
 * Copyright CSIRO Australian e-Health Research Centre (http://aehrc.com).
 * All rights reserved. Use is subject to license terms and conditions.
 */
package au.csiro.ontology.model;

/**
 * This class represents a feature (also referred to as a data property in OWL).
 * 
 * @author Alejandro Metke
 * 
 */
public class Feature<T extends Comparable<T>> implements INamedFeature<T> {

    /**
     * String identifier of this concept.
     */
    protected final T id;

    /**
     * Creates a new Concept.
     * 
     * @param id
     *            The concept's identifier.
     */
    public Feature(T id) {
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
        return id.toString();
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
        Feature other = (Feature) obj;
        if (id == null) {
            if (other.id != null)
                return false;
        } else if (!id.equals(other.id))
            return false;
        return true;
    }

}
