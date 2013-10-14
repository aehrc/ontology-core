/**
 * Copyright CSIRO Australian e-Health Research Centre (http://aehrc.com).
 * All rights reserved. Use is subject to license terms and conditions.
 */
package au.csiro.ontology;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Calendar;

import au.csiro.ontology.model.Axiom;
import au.csiro.ontology.model.BigIntegerLiteral;
import au.csiro.ontology.model.BooleanLiteral;
import au.csiro.ontology.model.Concept;
import au.csiro.ontology.model.ConceptInclusion;
import au.csiro.ontology.model.Conjunction;
import au.csiro.ontology.model.Datatype;
import au.csiro.ontology.model.DateLiteral;
import au.csiro.ontology.model.DecimalLiteral;
import au.csiro.ontology.model.DoubleLiteral;
import au.csiro.ontology.model.Existential;
import au.csiro.ontology.model.Feature;
import au.csiro.ontology.model.FloatLiteral;
import au.csiro.ontology.model.IntegerLiteral;
import au.csiro.ontology.model.Literal;
import au.csiro.ontology.model.LongLiteral;
import au.csiro.ontology.model.NamedConcept;
import au.csiro.ontology.model.NamedFeature;
import au.csiro.ontology.model.NamedRole;
import au.csiro.ontology.model.Operator;
import au.csiro.ontology.model.Role;
import au.csiro.ontology.model.RoleInclusion;
import au.csiro.ontology.model.StringLiteral;

/**
 * Concrete implementation of {@link IFactory}.
 * 
 * @author Alejandro Metke
 *
 */
@SuppressWarnings("deprecation")
public class Factory {

    public static Concept createNamedConcept(String id) {
        return new NamedConcept(id);
    }

    public static Role createNamedRole(String id) {
        return new NamedRole(id);
    }

    public static Feature createNamedFeature(String id) {
        return new NamedFeature(id);
    }

    public static Concept createConjunction(Concept... concepts) {
        return new Conjunction(concepts);
    }

    public static Concept createExistential(Role role, Concept filler) {
        return new Existential(role, filler);
    }

    public static Concept createDatatype(Feature feature, Operator operator, Literal literal) {
        return new Datatype(feature, operator, literal);
    }

    public static Axiom createConceptInclusion(Concept lhs, Concept rhs) {
        return new ConceptInclusion(lhs, rhs);
    }

    public static Axiom createRoleInclusion(Role[] lhs, Role rhs) {
        return new RoleInclusion(lhs, rhs);
    }
    
    @Deprecated
    public static Literal createBooleanLiteral(boolean value) {
        return new BooleanLiteral(value);
    }

    public static Literal createIntegerLiteral(int value) {
        return new IntegerLiteral(value);
    }
    
    public static Literal createFloatLiteral(float value) {
        return new FloatLiteral(value);
    }
    
    @Deprecated
    public static Literal createDoubleLiteral(double value) {
        return new DoubleLiteral(value);
    }
    
    public static Literal createDecimalLiteral(double value) {
        return new DecimalLiteral(value);
    }
    
    public static Literal createDecimalLiteral(BigDecimal value) {
        return new DecimalLiteral(value);
    }
    
    public static Literal createBigIntegerLiteral(long value) {
        return new BigIntegerLiteral(value);
    }
    
    public static Literal createBigIntegerLiteral(BigInteger value) {
        return new BigIntegerLiteral(value);
    }

    public static Literal createDateLiteral(Calendar value) {
        return new DateLiteral(value);
    }

    public static Literal createStringLiteral(String value) {
        return new StringLiteral(value);
    }

    public static Literal createLongLiteral(long value) {
        return new LongLiteral(value);
    }

}
