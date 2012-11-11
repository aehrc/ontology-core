/**
 * Copyright CSIRO Australian e-Health Research Centre (http://aehrc.com).
 * All rights reserved. Use is subject to license terms and conditions.
 */
package au.csiro.ontology.axioms;

import au.csiro.ontology.model.IConcept;

/**
 * Defines the methods of a concept inclusion axiom (also known as a SubClassOf
 * axiom in OWL).
 * 
 * @author Alejandro Metke
 *
 */
public interface IConceptInclusion extends IAxiom {
    
    /**
     * Returns the left hand side of the expression.
     * 
     * @return
     */
    public IConcept lhs();
    
    /**
     * Returns the right hand side of the expression.
     * 
     * @return
     */
    public IConcept rhs();
    
}
