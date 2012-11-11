/**
 * Copyright CSIRO Australian e-Health Research Centre (http://aehrc.com).
 * All rights reserved. Use is subject to license terms and conditions.
 */
package au.csiro.ontology.model;

/**
 * This class represents a role (also referred to as an object property in OWL).
 * 
 * @author Alejandro Metke
 * 
 */
public class Role<T> implements INamedRole<T> {
    
    /**
     * Represents the role used to group related expressions in SNOMED.
     */
    public static IRole ROLE_GROUP = new Role<>();
    
    /**
     * String identifier of this concept.
     */
    protected final T id;
    
    /**
     * Private constructor.
     */
    private Role() {
        id = null;
    }
    
    /**
     * Creates a new Role.
     * 
     * @param id
     *            The role's identifier.
     */
    public Role(T id) {
        assert(id != null);
        this.id = id;
    }

    /**
     * Returns this role's identifier.
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
        Role other = (Role) obj;
        if (id == null) {
            if (other.id != null)
                return false;
            else
                assert(false);
        } else if (!id.equals(other.id))
            return false;
        return true;
    }

}
