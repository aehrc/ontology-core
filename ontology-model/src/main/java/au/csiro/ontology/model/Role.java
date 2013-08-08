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
public class Role implements INamedRole {
    
    /**
     * String identifier of this concept.
     */
    protected String id;
    
    /**
     * 
     */
    public Role() {
        
    }
    
    /**
     * Creates a new Role.
     * 
     * @param id
     *            The role's identifier.
     */
    public Role(String id) {
        assert(id != null);
        this.id = id;
    }

    /**
     * Returns this role's identifier.
     * 
     * @return The identifier.
     */
    public String getId() {
        return id;
    }

    /**
     * @param id the id to set
     */
    public void setId(String id) {
        this.id = id;
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

    public int compareTo(IRole o) {
        if(!(o instanceof INamedRole)) {
            return -1;
        } else {
            return id.compareTo(((INamedRole)o).getId());
        }
    }

}
