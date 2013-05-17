/**
 * Copyright CSIRO Australian e-Health Research Centre (http://aehrc.com).
 * All rights reserved. Use is subject to license terms and conditions.
 */
package au.csiro.ontology.model;


/**
 * @author Alejandro Metke
 *
 */
public interface IDatatype extends IConcept {
    public INamedFeature getFeature();
    public Operator getOperator();
    public ILiteral getLiteral();
}
