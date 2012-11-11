/**
 * Copyright CSIRO Australian e-Health Research Centre (http://aehrc.com).
 * All rights reserved. Use is subject to license terms and conditions.
 */
package au.csiro.ontology.model;

/**
 * This class represents an existential (also known as an ObjectSomeValuesFrom
 * in OWL).
 * 
 * @author Alejandro Metke
 * 
 */
public class Existential<T extends Comparable<T>> implements IExistential<T> {

    private INamedRole<T> role;
    private IConcept concept;

    public Existential(INamedRole<T> role, IConcept concept) {
        this.role = role;
        this.concept = concept;
    }

    @Override
    public String toString() {
        return role + " . " + concept;
    }
    
    @Override
    public INamedRole<T> getRole() {
        return role;
    }
    
    @Override
    public IConcept getConcept() {
        return concept;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((concept == null) ? 0 : concept.hashCode());
        result = prime * result + ((role == null) ? 0 : role.hashCode());
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
        Existential other = (Existential) obj;
        if (concept == null) {
            if (other.concept != null)
                return false;
        } else if (!concept.equals(other.concept))
            return false;
        if (role == null) {
            if (other.role != null)
                return false;
        } else if (!role.equals(other.role))
            return false;
        return true;
    }

    @SuppressWarnings({ "unchecked", "rawtypes" })
    @Override
    public int compareTo(IConcept o) {
        Class thisClass = this.getClass();
        Class otherClass = o.getClass();
        if(thisClass.equals(otherClass)) {
            Existential<T> other = (Existential<T>)o;
            int res = 0;
            res = role.getId().compareTo(other.role.getId());
            if(res != 0) return res;
            try {
                res = concept.compareTo(other.concept);
            } catch(ClassCastException e) {
                // Need to catch this because elements in the conjunction might
                // be of different types
                res = concept.getClass().toString().compareTo(
                        other.concept.getClass().toString());
            }
            if(res != 0) return res;
            return 0;
        } else {
            return thisClass.toString().compareTo(otherClass.toString());
        }
    }

}
