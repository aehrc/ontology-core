/**
 * Copyright CSIRO Australian e-Health Research Centre (http://aehrc.com).
 * All rights reserved. Use is subject to license terms and conditions.
 */
package au.csiro.ontology;

import java.util.Calendar;

import au.csiro.ontology.model.Axiom;
import au.csiro.ontology.model.BooleanLiteral;
import au.csiro.ontology.model.Concept;
import au.csiro.ontology.model.ConceptInclusion;
import au.csiro.ontology.model.Conjunction;
import au.csiro.ontology.model.Datatype;
import au.csiro.ontology.model.DateLiteral;
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
public class Factory {

    public Concept createConcept(String id) {
        return new NamedConcept(id);
    }

    public Role createRole(String id) {
        return new NamedRole(id);
    }

    public Feature createFeature(String id) {
        return new NamedFeature(id);
    }

    public Concept createConjunction(Concept... concepts) {
        return new Conjunction(concepts);
    }

    public Concept createExistential(NamedRole role, Concept filler) {
        return new Existential(role, filler);
    }

    public Concept createDatatype(NamedFeature feature, Operator operator, Literal literal) {
        return new Datatype(feature, operator, literal);
    }

    public Axiom createConceptInclusion(Concept lhs, Concept rhs) {
        return new ConceptInclusion(lhs, rhs);
    }

    public Axiom createRoleInclusion(Role[] lhs, Role rhs) {
        return new RoleInclusion(lhs, rhs);
    }

    public Literal createBooleanLiteral(boolean value) {
        return new BooleanLiteral(value);
    }

    public Literal createIntegerLiteral(int value) {
        return new IntegerLiteral(value);
    }

    public Literal createFloatLiteral(float value) {
        return new FloatLiteral(value);
    }

    public Literal createDoubleLiteral(double value) {
        return new DoubleLiteral(value);
    }

    public Literal createDateLiteral(Calendar value) {
        return new DateLiteral(value);
    }

    public Literal createStringLiteral(String value) {
        return new StringLiteral(value);
    }

    public Literal createLongLiteral(long value) {
        return new LongLiteral(value);
    }

}
