/**
 * Copyright CSIRO Australian e-Health Research Centre (http://aehrc.com).
 * All rights reserved. Use is subject to license terms and conditions. 
 */
package au.csiro.ontology;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

/**
 * Represents a node in the taxonomy generated after classifying an ontology.
 * 
 * @author Alejandro Metke
 * 
 */
public class Node<T extends Comparable<T>> implements Serializable {
    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    /**
     * Set of equivalent concepts in this node.
     */
    protected final Set<T> equivalentConcepts = new HashSet<>();
    
    /**
     * Set of parents nodes.
     */
    protected final Set<Node<T>> parents = new HashSet<>();
    
    /**
     * Set of child nodes.
     */
    protected final Set<Node<T>> children = new HashSet<>();

    /**
     * @return the equivalentConcepts
     */
    public Set<T> getEquivalentConcepts() {
        return equivalentConcepts;
    }

    /**
     * @return the parents
     */
    public Set<Node<T>> getParents() {
        return parents;
    }

    /**
     * @return the children
     */
    public Set<Node<T>> getChildren() {
        return children;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        int size = equivalentConcepts.size();
        int i = 0;
        sb.append("{");
        for (T equiv : equivalentConcepts) {
            sb.append(equiv);
            if (++i < size)
                sb.append(", ");
        }
        sb.append("}");
        return sb.toString();
    }

}
