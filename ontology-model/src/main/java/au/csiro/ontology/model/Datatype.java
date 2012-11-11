package au.csiro.ontology.model;

/**
 * A datatype expression that represents a set of individuals that have a
 * property with a certain value. The expression consists of a feature, an
 * operator (=, <, <=, >, >=), and a literal value.
 * 
 * @author Alejandro Metke
 * 
 */
public class Datatype<T extends Comparable<T>> implements IDatatype<T> {
    
    private INamedFeature<T> feature;
    private Operator operator;
    private ILiteral literal;

    /**
     * 
     * @param feature
     * @param operator
     * @param literal
     */
    public Datatype(INamedFeature<T> feature, Operator operator, 
            ILiteral literal) {
        this.feature = feature;
        this.operator = operator;
        this.literal = literal;
    }

    public INamedFeature<T> getFeature() {
        return feature;
    }

    public Operator getOperator() {
        return operator;
    }

    public ILiteral getLiteral() {
        return literal;
    }

    @Override
    public String toString() {
        return feature + ".";
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((feature == null) ? 0 : feature.hashCode());
        result = prime * result + ((literal == null) ? 0 : literal.hashCode());
        result = prime * result
                + ((operator == null) ? 0 : operator.hashCode());
        return result;
    }

    @SuppressWarnings("rawtypes")
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

    @SuppressWarnings({ "unchecked", "rawtypes" })
    @Override
    public int compareTo(IConcept o) {
        Class thisClass = this.getClass();
        Class otherClass = o.getClass();
        if(thisClass.equals(otherClass)) {
            Datatype<T> other = (Datatype<T>)o;
            int res = 0;
            res = feature.getId().compareTo(other.feature.getId());
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
