/**
 * Copyright CSIRO Australian e-Health Research Centre (http://aehrc.com).
 * All rights reserved. Use is subject to license terms and conditions.
 */
package au.csiro.ontology.axioms;

import java.util.Arrays;

import au.csiro.ontology.model.IRole;

/**
 * This class represents a role inclusion axiom (also known as a
 * SubObjectPropertyOf axiom in OWL).
 * 
 * @author Alejandro Metke
 * 
 */
public class RoleInclusion implements IRoleInclusion {
    
    /**
     * The left hand side of the expression.
     */
    final protected IRole[] lhs;
    
    /**
     * The right hand side of the expression.
     */
    final protected IRole rhs;

    /**
     * Creates a new {@link RoleInclusion}.
     * 
     * @param lhs
     * @param rhs
     */
    public RoleInclusion(final IRole[] lhs, final IRole rhs) {
        this.lhs = lhs;
        Arrays.sort(lhs);
        this.rhs = rhs;
    }
    
    /**
     * Creates a new {@link RoleInclusion}.
     * 
     * @param lhs
     * @param rhs
     */
    public RoleInclusion(final IRole lhs, final IRole rhs) {
        this.lhs = new IRole[]{lhs};
        this.rhs = rhs;
    }
    
    /**
     * Returns the left hand side of the expression.
     * 
     * @return
     */
    @Override
    public IRole[] lhs() {
        return lhs;
    }
    
    /**
     * Returns the right hand side of the expression.
     * 
     * @return
     */
    @Override
    public IRole rhs() {
        return rhs;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(lhs[0]);
        for (int i = 1; i < lhs.length; i++) {
            sb.append(" o ");
            sb.append(lhs[i]);
        }
        sb.append(" \u2291 ");
        sb.append(rhs);
        return sb.toString();
    }

    @Override
    public int compareTo(IAxiom o) {
        if(!(o instanceof IRoleInclusion)) {
            return 1;
        } else {
            IRoleInclusion otherRi = (IRoleInclusion)o;
            int lhsRes = 0;
            IRole[] oLhs = otherRi.lhs();
            if(!Arrays.equals(lhs, oLhs)) {
                // If length is different put the shortest one first
                if(lhs.length < oLhs.length) {
                    lhsRes = -1;
                } else if(lhs.length > oLhs.length) {
                    lhsRes = 1;
                } else {
                    for(int i = 0; i < lhs.length; i++) {
                        int res = lhs[i].compareTo(oLhs[i]);
                        if(res < 0) {
                            lhsRes = -1;
                            break;
                        } else if(res > 0) {
                            lhsRes = 1;
                            break;
                        }
                    }
                }
            }
            int rhsRes = rhs.compareTo(otherRi.rhs());
            if(lhsRes == 0 && rhsRes == 0)
                return 0;
            else if(lhsRes != 0)
                return lhsRes;
            else
                return rhsRes;
        }
    }
    
}
