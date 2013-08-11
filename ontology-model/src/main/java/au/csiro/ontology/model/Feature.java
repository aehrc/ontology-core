/**
 * Copyright CSIRO Australian e-Health Research Centre (http://aehrc.com).
 * All rights reserved. Use is subject to license terms and conditions.
 */
package au.csiro.ontology.model;

import java.io.Serializable;

import javax.xml.bind.annotation.XmlRootElement;

/**
 * Base class for features.
 * 
 * @author Alejandro Metke
 *
 */
@XmlRootElement
public abstract class Feature implements Comparable<Feature>, Serializable {
    
    private static final long serialVersionUID = 1L;

    abstract public String toString();

    abstract public int hashCode();

    abstract public boolean equals(Object o);
    
}
