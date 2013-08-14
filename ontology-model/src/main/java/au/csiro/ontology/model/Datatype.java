package au.csiro.ontology.model;

import javax.xml.bind.annotation.XmlRootElement;

/**
 * A datatype expression that represents a set of individuals that have a
 * property with a certain value. The expression consists of a feature, an
 * operator (=, <, <=, >, >=), and a literal value.
 * 
 * @author Alejandro Metke
 * 
 */
@XmlRootElement
public class Datatype extends Concept {

    private static final long serialVersionUID = 1L;
    
    private Feature feature;
    
    private Operator operator;
    
    private Literal literal;
    
    /**
     * 
     */
    public Datatype() {
        
    }
    
    /**
     * 
     * @param feature
     * @param operator
     * @param literal
     */
    public Datatype(Feature feature, Operator operator, Literal literal) {
        this.feature = feature;
        this.operator = operator;
        this.literal = literal;
    }

    public Feature getFeature() {
        return feature;
    }

    public Operator getOperator() {
        return operator;
    }

    public Literal getLiteral() {
        return literal;
    }

    /**
     * @param feature the feature to set
     */
    public void setFeature(Feature feature) {
        this.feature = feature;
    }

    /**
     * @param operator the operator to set
     */
    public void setOperator(Operator operator) {
        this.operator = operator;
    }

    /**
     * @param literal the literal to set
     */
    public void setLiteral(Literal literal) {
        this.literal = literal;
    }

    @Override
    public String toString() {
        return feature + ".(" + operator + "," + literal + ")";
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((feature == null) ? 0 : feature.hashCode());
        result = prime * result + ((literal == null) ? 0 : literal.hashCode());
        result = prime * result + ((operator == null) ? 0 : operator.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        Datatype other = (Datatype) obj;
        if (feature == null) {
            if (other.feature != null)
                return false;
        } else if (!feature.equals(other.feature))
            return false;
        if (literal == null) {
            if (other.literal != null)
                return false;
        } else if (!literal.equals(other.literal))
            return false;
        if (operator != other.operator)
            return false;
        return true;
    }

    @SuppressWarnings({ "rawtypes" })
    public int compareTo(Concept o) {
        Class thisClass = this.getClass();
        Class otherClass = o.getClass();
        if(thisClass.equals(otherClass)) {
            Datatype other = (Datatype)o;
            int res = 0;
            res = feature.compareTo(other.feature);
            if(res != 0) return res;
            res = operator.compareTo(other.operator);
            if(res != 0) return res;
            res = literal.compareTo(other.literal);
            return res;
        } else {
            return thisClass.toString().compareTo(otherClass.toString());
        }
    }

}
