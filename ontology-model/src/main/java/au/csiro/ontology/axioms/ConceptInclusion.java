/**
 * Copyright CSIRO Australian e-Health Research Centre (http://aehrc.com).
 * All rights reserved. Use is subject to license terms and conditions.
 */
package au.csiro.ontology.axioms;

import au.csiro.ontology.model.IConcept;

/**
 * This class represents a concept inclusion axiom (also known as a SubClassOf
 * axiom in OWL).
 * 
 * @author Alejandro Metke
 * 
 */
public class ConceptInclusion implements IConceptInclusion {
    
    /**
     * The left hand side of the expression.
     */
    final protected IConcept lhs;
    
    /**
     * The right hand side of the expression.
     */
    final protected IConcept rhs;

    /**
     * Creates a new ConceptInclusion.
     * 
     * @param lhs
     * @param rhs
     */
    public ConceptInclusion(final IConcept lhs, final IConcept rhs) {
        if (null == lhs) {
            throw new IllegalArgumentException("LHS cannot be null (RHS = "
                    + rhs + ")");
        }
        this.lhs = lhs;
        if (null == rhs) {
            throw new IllegalArgumentException("RHS cannot be null (LHS = "
                    + lhs + ")");
        }
        this.rhs = rhs;
    }
    
    /**
     * Returns the left hand side of the expression.
     * 
     * @return
     */
    public IConcept lhs() {
        return lhs;
    }
    
    /**
     * Returns the right hand side of the expression.
     * 
     * @return
     */
    public IConcept rhs() {
        return rhs;
    }

    @Override
    public String toString() {
        return lhs + " \u2291 " + rhs;
    }

}
