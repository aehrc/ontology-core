/**
 * Copyright CSIRO Australian e-Health Research Centre (http://aehrc.com).
 * All rights reserved. Use is subject to license terms and conditions.
 */
package au.csiro.ontology;

import java.util.Calendar;

import au.csiro.ontology.axioms.IAxiom;
import au.csiro.ontology.model.ILiteral;
import au.csiro.ontology.model.IConcept;
import au.csiro.ontology.model.INamedFeature;
import au.csiro.ontology.model.INamedRole;
import au.csiro.ontology.model.IRole;
import au.csiro.ontology.model.Operator;

/**
 * Defines the methods required to create expressions and axioms.
 * 
 * @author Alejandro Metke
 *
 */
public interface IFactory<T extends Comparable<T>> {
    
    /**
     * Creates a concept with the specified id.
     * 
     * @param id
     * @return
     */
    public IConcept createConcept(T id);
    
    /**
     * Creates a role with the specified id.
     * 
     * @param id
     * @return
     */
    public INamedRole<T> createRole(T id);
    
    /**
     * Creates a feature with the specified id.
     * 
     * @param id
     * @return
     */
    public INamedFeature<T> createFeature(T id);
    
    /**
     * Creates a conjunction.
     * 
     * @param concepts
     * @return
     */
    public IConcept createConjunction(IConcept... concepts);
    
    /**
     * Creates an existential.
     * 
     * @param role
     * @param filler
     * @return
     */
    public IConcept createExistential(INamedRole<T> role, IConcept filler);
    
    /**
     * Creates a datatype expression.
     * 
     * @param feature
     * @param literal
     * @return
     */
    public IConcept createDatatype(INamedFeature<T> feature, Operator operator, 
            ILiteral literal);
    
    /**
     * Creates a general concept inclusion axiom.
     * 
     * @param lhs
     * @param rhs
     * @return
     */
    public IAxiom createConceptInclusion(IConcept lhs, IConcept rhs);
    
    /**
     * Creates a role inclusion axiom.
     * 
     * @param lhs
     * @param rhs
     * @return
     */
    public IAxiom createRoleInclusion(IRole[] lhs, IRole rhs);
    
    /**
     * Creates a boolean literal.
     * 
     * @param value
     * @return
     */
    public ILiteral createBooleanLiteral(boolean value);
    
    /**
     * Creates an integer literal.
     * 
     * @param value
     * @return
     */
    public ILiteral createIntegerLiteral(int value);
    
    /**
     * Creates a float literal.
     * 
     * @param value
     * @return
     */
    public ILiteral createFloatLiteral(float value);
    
    /**
     * Creates a double literal.
     * 
     * @param value
     * @return
     */
    public ILiteral createDoubleLiteral(double value);
    
    /**
     * Creates a date literal.
     * 
     * @param date
     * @return
     */
    public ILiteral createDateLiteral(Calendar value);
    
    /**
     * Creates a string literal.
     * 
     * @param string
     * @return
     */
    public ILiteral createStringLiteral(String value);
    
    /**
     * Creates a long literal.
     * 
     * @param value
     * @return
     */
    public ILiteral createLongLiteral(long value);
    
}
