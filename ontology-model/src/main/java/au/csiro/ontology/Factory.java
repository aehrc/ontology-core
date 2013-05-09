/**
 * Copyright CSIRO Australian e-Health Research Centre (http://aehrc.com).
 * All rights reserved. Use is subject to license terms and conditions.
 */
package au.csiro.ontology;

import java.util.Calendar;

import au.csiro.ontology.axioms.ConceptInclusion;
import au.csiro.ontology.axioms.IAxiom;
import au.csiro.ontology.axioms.RoleInclusion;
import au.csiro.ontology.model.BooleanLiteral;
import au.csiro.ontology.model.Concept;
import au.csiro.ontology.model.Conjunction;
import au.csiro.ontology.model.Datatype;
import au.csiro.ontology.model.DateLiteral;
import au.csiro.ontology.model.DoubleLiteral;
import au.csiro.ontology.model.Existential;
import au.csiro.ontology.model.Feature;
import au.csiro.ontology.model.FloatLiteral;
import au.csiro.ontology.model.IConcept;
import au.csiro.ontology.model.ILiteral;
import au.csiro.ontology.model.INamedFeature;
import au.csiro.ontology.model.INamedRole;
import au.csiro.ontology.model.IRole;
import au.csiro.ontology.model.IntegerLiteral;
import au.csiro.ontology.model.LongLiteral;
import au.csiro.ontology.model.Operator;
import au.csiro.ontology.model.Role;
import au.csiro.ontology.model.StringLiteral;

/**
 * Concrete implemetation of {@link IFactory}.
 * 
 * @author Alejandro Metke
 *
 */
public class Factory<T extends Comparable<T>> implements IFactory<T> {

    public IConcept createConcept(T id) {
        return new Concept<T>(id);
    }

    public INamedRole<T> createRole(T id) {
        return new Role<T>(id);
    }

    public INamedFeature<T> createFeature(T id) {
        return new Feature<T>(id);
    }

    public IConcept createConjunction(IConcept... concepts) {
        return new Conjunction(concepts);
    }

    public IConcept createExistential(INamedRole<T> role, IConcept filler) {
        return new Existential<T>(role, filler);
    }

    public IConcept createDatatype(INamedFeature<T> feature, Operator operator, 
            ILiteral literal) {
        return new Datatype<T>(feature, operator, literal);
    }

    public IAxiom createConceptInclusion(IConcept lhs, IConcept rhs) {
        return new ConceptInclusion(lhs, rhs);
    }

    public IAxiom createRoleInclusion(IRole[] lhs, IRole rhs) {
        return new RoleInclusion(lhs, rhs);
    }

    public ILiteral createBooleanLiteral(boolean value) {
        return new BooleanLiteral(value);
    }

    public ILiteral createIntegerLiteral(int value) {
        return new IntegerLiteral(value);
    }

    public ILiteral createFloatLiteral(float value) {
        return new FloatLiteral(value);
    }

    public ILiteral createDoubleLiteral(double value) {
        return new DoubleLiteral(value);
    }

    public ILiteral createDateLiteral(Calendar value) {
        return new DateLiteral(value);
    }

    public ILiteral createStringLiteral(String value) {
        return new StringLiteral(value);
    }

    public ILiteral createLongLiteral(long value) {
        return new LongLiteral(value);
    }

}
