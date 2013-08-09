/**
 * Copyright CSIRO Australian e-Health Research Centre (http://aehrc.com).
 * All rights reserved. Use is subject to license terms and conditions.
 */
package au.csiro.ontology.model;

import javax.xml.bind.annotation.XmlRootElement;

/**
 * This class represents a named concept (also referred to as a class in OWL).
 * 
 * @author Alejandro Metke
 * 
 */
@XmlRootElement
public class NamedConcept extends Concept {

    private static final long serialVersionUID = 1L;

    /**
     * Represents the top concept.
     */
    public static String TOP = "_TOP_";
    
    /**
     * Represents the bottom concept.
     */
    public static String BOTTOM = "_BOTTOM_";
    
    /**
     * The top concept.
     */
    public static final Concept TOP_CONCEPT = new NamedConcept(TOP);
    
    /**
     * The bottom concept.
     */
    public static final Concept BOTTOM_CONCEPT = new NamedConcept(BOTTOM);
    
    /**
     * String identifier of this concept.
     */
    protected String id;
    
    /**
     * No args constructor.
     */
    public NamedConcept() {
        
    }
    
    /**
     * Creates a new Concept.
     * 
     * @param id
     *            The concept's identifier.
     */
    public NamedConcept(String id) {
        assert(id != null);
        this.id = id;
    }
    
    /**
     * Returns this concept's identifier.
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
        StringBuilder builder = new StringBuilder();
        builder.append("Concept [id=");
        builder.append(id);
        builder.append("]");
        return builder.toString();
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
        NamedConcept other = (NamedConcept) obj;
        if (id == null) {
            if (other.id != null)
                return false;
        } else if (!id.equals(other.id))
            return false;
        return true;
    }

    @Override
    public int compareTo(Concept other) {
        if(!(other instanceof NamedConcept)) {
            return -1;
        }
        return id.compareTo(((NamedConcept) other).id);
    }
    
}
