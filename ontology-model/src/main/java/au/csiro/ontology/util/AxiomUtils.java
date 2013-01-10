/**
 * Copyright CSIRO Australian e-Health Research Centre (http://aehrc.com).
 * All rights reserved. Use is subject to license terms and conditions.
 */
package au.csiro.ontology.util;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import au.csiro.ontology.axioms.ConceptInclusion;
import au.csiro.ontology.axioms.IAxiom;
import au.csiro.ontology.axioms.IConceptInclusion;
import au.csiro.ontology.axioms.IRoleInclusion;
import au.csiro.ontology.axioms.RoleInclusion;
import au.csiro.ontology.model.Concept;
import au.csiro.ontology.model.Conjunction;
import au.csiro.ontology.model.Existential;
import au.csiro.ontology.model.IConcept;
import au.csiro.ontology.model.INamedRole;
import au.csiro.ontology.model.IRole;
import au.csiro.ontology.model.Role;

/**
 * Several utilities for axioms.
 * 
 * @author Alejandro Metke
 *
 */
public class AxiomUtils {
    
    /**
     * Transforms an axiom into a {@link String} representation.
     * 
     * @param axiom
     * @return
     */
    public static String serialise(IAxiom axiom) {
        StringBuilder sb = new StringBuilder();
        sb.append("{");
        sb.append(axiom.getClass().getSimpleName());
        sb.append(",");
        
        if(axiom instanceof IConceptInclusion) {
            IConceptInclusion ci = (IConceptInclusion)axiom;
            sb.append(serialiseConcept(ci.lhs()));
            sb.append(",");
            sb.append(serialiseConcept(ci.rhs()));
        } else if(axiom instanceof IRoleInclusion) {
            IRoleInclusion ri = (IRoleInclusion)axiom;
            sb.append("[");
            IRole[] lhss = ri.lhs();
            if(lhss.length > 0) {
                sb.append(serialiseRole(lhss[0]));
            }
            for(int i = 1; i < lhss.length; i++) {
                sb.append(",");
                sb.append(serialiseRole(lhss[i]));
            }
            sb.append("],");
            sb.append(serialiseRole(ri.rhs()));
        } else {
            throw new RuntimeException("Invalid axiom type: "+
                    axiom.getClass().getSimpleName());
        }
        sb.append("}");
        return sb.toString();
    }
    
    @SuppressWarnings({ "rawtypes" })
    private static String serialiseConcept(IConcept c) {
        StringBuilder sb = new StringBuilder();
        sb.append("{");
        sb.append(c.getClass().getSimpleName());
        sb.append(",");
        
        if(c instanceof Concept) {
            Concept nc = (Concept)c;
            if(nc == Concept.TOP) {
                sb.append("type:TOP");
            } else if (nc == Concept.BOTTOM) {
                sb.append("type:BOTTOM");
            } else {
                Object id = nc.getId();
                sb.append(id.getClass().getName());
                sb.append(",\"");
                sb.append(id.toString());
                sb.append("\"");
            }
        } else if(c instanceof Conjunction) {
            Conjunction con = (Conjunction)c;
            IConcept[] ics = con.getConcepts();
            if(ics.length > 0) {
                sb.append(serialiseConcept(ics[0]));
                for(int i = 1; i < ics.length; i++) {
                    sb.append(",");
                    sb.append(serialiseConcept(ics[i]));
                }
            }
        } else if(c instanceof Existential) {
            Existential ex = (Existential)c;
            sb.append(serialiseRole(ex.getRole()));
            sb.append(",");
            sb.append(serialiseConcept(ex.getConcept()));
        }
        sb.append("}");
        return sb.toString();
    }
    
    @SuppressWarnings("rawtypes")
    private static String serialiseRole(IRole r) {
        StringBuilder sb = new StringBuilder();
        sb.append("{");
        sb.append(r.getClass().getSimpleName());
        sb.append(",");
        if(r instanceof INamedRole) {
            INamedRole nr = (INamedRole)r;
            Object id = nr.getId();
            sb.append(id.getClass().getName());
            sb.append(",\"");
            sb.append(id.toString());
            sb.append("\"");
        }
        sb.append("}");
        return sb.toString();
    }
    
    /**
     * Transforms a string representation of an axiom into an {@link IAxiom}.
     * 
     * @param s
     * @return
     */
    public static IAxiom deserialise(String s) {
        int start = s.indexOf('{', 0);
        if(start != 0) {
            throw new RuntimeException(
                    "Malformed axiom string: should start with '{' ("+s+")");
        }
        
        String axiomType = s.substring(start+1, s.indexOf(',', start+1));
        start = start+1+axiomType.length()+1;
        
        if("ConceptInclusion".equals(axiomType)) {
            int closingIndex = findClosingIndex(s, '{', '}', start);
            String concept = s.substring(start, closingIndex);
            IConcept lhs = deserialiseConcept(concept);
            int secondClosingIndex = 
                    findClosingIndex(s, '{', '}', closingIndex+1);
            concept = s.substring(closingIndex+1, secondClosingIndex);
            IConcept rhs = deserialiseConcept(concept);
            return new ConceptInclusion(lhs, rhs);
        } else if("RoleInclusion".equals(axiomType)) {
            int closingIndex = findClosingIndex(s, '[', ']', start);
            String roles = s.substring(start+1, closingIndex-1);
            
            int rstart = 0;
            List<IRole> lhsRoles = new ArrayList<>();
            
            while(rstart < roles.length()) {
                int rclosingIndex = findClosingIndex(roles, '{', '}', rstart);
                String role = roles.substring(rstart, rclosingIndex);
                rstart = rclosingIndex+1;
                lhsRoles.add(deserialiseRole(role));
            }
            
            start = closingIndex+1; // move to start of rhs
            closingIndex = findClosingIndex(s, '{', '}', start);
            String role = s.substring(start, closingIndex);
            IRole rhs = deserialiseRole(role);
            IRole[] lhs = new IRole[lhsRoles.size()];
            for(int i = 0; i < lhs.length; i++) {
                lhs[i] = lhsRoles.get(i);
            }
            
            return new RoleInclusion(lhs, rhs);
        } else {
            throw new RuntimeException(
                    "Malformed axiom string: unknown axiom type "+axiomType+
                    " ("+s+")");
        }
    }
    
    @SuppressWarnings({ "rawtypes", "unchecked" })
    private static IConcept deserialiseConcept(String s) {
        int start = s.indexOf('{', 0);
        if(start != 0) {
            throw new RuntimeException(
                    "Malformed concept string: should start with '{' ("+s+")");
        }
        
        String conceptType = s.substring(start+1, s.indexOf(',', start+1));
        start = start+1+conceptType.length()+1;
        
        if("Concept".equals(conceptType)) {
            String type = s.substring(start, s.indexOf(',', start));
            start = start+type.length()+1;
            // Only String is supported for now
            if("java.lang.String".equals(type)) {
                // Find string enclosed in ""
                // TODO: need to deal with nested escaped quotation marks
                int closingIndex = s.indexOf('"', start + 1);
                String literal = s.substring(start+1, closingIndex);
                return new Concept<String>(literal);
            } else {
                throw new RuntimeException(
                        "Unsupported parametrised type "+type);
            }
        } else if("Conjunction".equals(conceptType)) {
            List<IConcept> conjuncts = new ArrayList<>();
            
            while(start < s.length() && s.charAt(start) != '}') {
                int closingIndex = findClosingIndex(s, '{', '}', start);
                String concept = s.substring(start, closingIndex);
                start = closingIndex+1;
                conjuncts.add(deserialiseConcept(concept));
            }
            start++;
            return new Conjunction(conjuncts);
        } else if("Existential".equals(conceptType)) {
            int closingIndex = findClosingIndex(s, '{', '}', start);
            String role = s.substring(start, closingIndex);
            start = closingIndex+1;
            INamedRole r = deserialiseRole(role);
            closingIndex = findClosingIndex(s, '{', '}', start);
            String concept = s.substring(start, closingIndex);
            IConcept filler = deserialiseConcept(concept);
            return new Existential<String>(r, filler);
        } else {
            throw new RuntimeException(
                    "Malformed concept string: unknown concept type "+
                            conceptType+" ("+s+")");
        }
    }
    
    @SuppressWarnings("rawtypes")
    private static INamedRole deserialiseRole(String s) {
        int start = s.indexOf('{', 0);
        if(start != 0) {
            throw new RuntimeException(
                    "Malformed role string: should start with '{' ("+s+")");
        }
        
        String roleType = s.substring(start+1, s.indexOf(',', start+1));
        start = start+1+roleType.length()+1;
        
        if("Role".equals(roleType)) {
            String type = s.substring(start, s.indexOf(',', start));
            start = start+type.length()+1;
            // Only String is supported for now
            if("java.lang.String".equals(type)) {
                // Find string enclosed in ""
                // TODO: need to deal with nested escaped quotation marks
                int closingIndex = s.indexOf('"', start + 1);
                String literal = s.substring(start+1, closingIndex);
                return new Role<String>(literal);
            } else {
                throw new RuntimeException(
                        "Unsupported parametrised type "+type);
            }
        } else {
            throw new RuntimeException(
                    "Malformed role string: unknown concept type "+
                            roleType+" ("+s+")");
        }
    }
    
    private static int findClosingIndex(String s, char oc, char cc, int start) {
        start++; // skip leading char
        int num = 1;
        while(num > 0 && start != -1) {
            int openPos = s.indexOf(oc, start);
            int closePos = s.indexOf(cc, start);
            
            if(closePos == -1) {
                throw new RuntimeException("Malformed axiom string: number " +
                		"of brackets does not match ("+s+")");
            }
            
            if(openPos != -1) {
                if(openPos < closePos) {
                    num++;
                    start = openPos+1;
                } else if(openPos > closePos) {
                    num--;
                    start = closePos+1;
                } else {
                    assert(false);
                }
            } else {
                num--;
                start = closePos+1;
            }
        }
        
        if(num != 0) {
            throw new RuntimeException("Malformed axiom string: number of " +
                    "brackets does not match ("+s+")");
        }
        
        return start;
    }
    
    /**
     * Returns a {@link Set} of {@link IAxiom}s that define a concept.
     * This includes the axioms of the form c [ x, x [ c, and c = x, where c is
     * a named concept and x is an abstract concept (which means it can be
     * either a named concept or a complex expression).
     * 
     * @param axioms
     * @param concept
     * @return
     */
    @SuppressWarnings("rawtypes")
    public static Set<IAxiom> findDefiningAxioms(Collection<IAxiom> axioms, 
            Concept concept) {
        Set<IAxiom> res = new HashSet<>();
        for(IAxiom axiom : axioms) {
            if(axiom instanceof IConceptInclusion) {
                IConceptInclusion ci = (IConceptInclusion)axiom;
                IConcept lhs = ci.lhs();
                IConcept rhs = ci.rhs();
                
                if(concept.equals(lhs) || concept.equals(rhs)) {
                    res.add(axiom);
                }
            }
        }
        return res;
    }
    
    /**
     * @param args
     */
    public static void main(String[] args) {
        final String FINGER = "Finger";
        final String BODY_PART = "Body Part";
        final String HAND = "Hand";
        final String SUB_PART = "sub-part";
        final String PART_OF = "part-of";
        final String HAS_LOCATION = "has-location";
        
        Concept<String> finger = new Concept<>(FINGER);
        Concept<String> bodyPart = new Concept<>(BODY_PART);
        Concept<String> hand = new Concept<>(HAND);
        
        Role<String> subPart = new Role<>(SUB_PART);
        Role<String> partOf = new Role<>(PART_OF);
        Role<String> hasLocation = new Role<>(HAS_LOCATION);
        
        IAxiom axiom1 = new ConceptInclusion(finger, 
                new Conjunction(new IConcept[]{
                        bodyPart, 
                        new Existential<String>(subPart, hand)}
                ));
        
        String s = AxiomUtils.serialise(axiom1);
        System.out.println(s);
        
        IAxiom axiom1b = AxiomUtils.deserialise(s);
        
        ConceptInclusion ci = (ConceptInclusion)axiom1b;
        System.out.println("lhs: "+ci.lhs());
        System.out.println("rhs: "+ci.rhs());
        
        IAxiom axiom2 = new RoleInclusion(
                new IRole[]{subPart, subPart}, partOf
        );
        s= AxiomUtils.serialise(axiom2);
        System.out.println(s);
        
        IAxiom axiom2b = AxiomUtils.deserialise(s);
        RoleInclusion ri = (RoleInclusion)axiom2b;
        System.out.println("lhs: "+ri.lhs());
        System.out.println("rhs: "+ri.rhs());
        
        IAxiom axiom3 = new RoleInclusion(new IRole[]{}, hasLocation);
        s = AxiomUtils.serialise(axiom3);
        System.out.println(s);
        
        IAxiom axiom3b = AxiomUtils.deserialise(s);
        ri = (RoleInclusion)axiom3b;
        System.out.println("lhs: "+ri.lhs());
        System.out.println("rhs: "+ri.rhs());
        
        IAxiom axiom4 = AxiomUtils.deserialise("{ConceptInclusion,{Concept,java.lang.String,\"449539009\"},{Existential,{Role,java.lang.String,\"RoleGroup\"},{Concept,au.csiro.snorocket.core.model.Conjunction,\"(4 . 704 + 5 . 2490)\"}}}");
        ci = (ConceptInclusion)axiom4;
        System.out.println("lhs: "+ci.lhs());
        System.out.println("rhs: "+ci.rhs());
    }

}
