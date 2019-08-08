/**
 * Copyright CSIRO Australian e-Health Research Centre (http://aehrc.com).
 * All rights reserved. Use is subject to license terms and conditions.
 */
package au.csiro.ontology.importer.owl;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.EmptyStackException;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.Stack;

import javax.xml.bind.DatatypeConverter;

import org.semanticweb.owlapi.model.OWLAnnotationAssertionAxiom;
import org.semanticweb.owlapi.model.OWLAxiom;
import org.semanticweb.owlapi.model.OWLClass;
import org.semanticweb.owlapi.model.OWLClassExpression;
import org.semanticweb.owlapi.model.OWLClassExpressionVisitor;
import org.semanticweb.owlapi.model.OWLDataAllValuesFrom;
import org.semanticweb.owlapi.model.OWLDataExactCardinality;
import org.semanticweb.owlapi.model.OWLDataHasValue;
import org.semanticweb.owlapi.model.OWLDataMaxCardinality;
import org.semanticweb.owlapi.model.OWLDataMinCardinality;
import org.semanticweb.owlapi.model.OWLDataOneOf;
import org.semanticweb.owlapi.model.OWLDataProperty;
import org.semanticweb.owlapi.model.OWLDataPropertyExpression;
import org.semanticweb.owlapi.model.OWLDataPropertyRangeAxiom;
import org.semanticweb.owlapi.model.OWLDataRange;
import org.semanticweb.owlapi.model.OWLDataSomeValuesFrom;
import org.semanticweb.owlapi.model.OWLDatatype;
import org.semanticweb.owlapi.model.OWLDatatypeRestriction;
import org.semanticweb.owlapi.model.OWLDeclarationAxiom;
import org.semanticweb.owlapi.model.OWLDisjointClassesAxiom;
import org.semanticweb.owlapi.model.OWLEntity;
import org.semanticweb.owlapi.model.OWLEquivalentClassesAxiom;
import org.semanticweb.owlapi.model.OWLEquivalentObjectPropertiesAxiom;
import org.semanticweb.owlapi.model.OWLFacetRestriction;
import org.semanticweb.owlapi.model.OWLFunctionalDataPropertyAxiom;
import org.semanticweb.owlapi.model.OWLLiteral;
import org.semanticweb.owlapi.model.OWLObjectAllValuesFrom;
import org.semanticweb.owlapi.model.OWLObjectComplementOf;
import org.semanticweb.owlapi.model.OWLObjectExactCardinality;
import org.semanticweb.owlapi.model.OWLObjectHasSelf;
import org.semanticweb.owlapi.model.OWLObjectHasValue;
import org.semanticweb.owlapi.model.OWLObjectIntersectionOf;
import org.semanticweb.owlapi.model.OWLObjectMaxCardinality;
import org.semanticweb.owlapi.model.OWLObjectMinCardinality;
import org.semanticweb.owlapi.model.OWLObjectOneOf;
import org.semanticweb.owlapi.model.OWLObjectPropertyExpression;
import org.semanticweb.owlapi.model.OWLObjectSomeValuesFrom;
import org.semanticweb.owlapi.model.OWLObjectUnionOf;
import org.semanticweb.owlapi.model.OWLOntology;
import org.semanticweb.owlapi.model.OWLReflexiveObjectPropertyAxiom;
import org.semanticweb.owlapi.model.OWLSubClassOfAxiom;
import org.semanticweb.owlapi.model.OWLSubObjectPropertyOfAxiom;
import org.semanticweb.owlapi.model.OWLSubPropertyChainOfAxiom;
import org.semanticweb.owlapi.model.OWLTransitiveObjectPropertyAxiom;
import org.semanticweb.owlapi.vocab.OWL2Datatype;
import org.semanticweb.owlapi.vocab.OWLFacet;

import au.csiro.ontology.Ontology;
import au.csiro.ontology.importer.BaseImporter;
import au.csiro.ontology.importer.ImportException;
import au.csiro.ontology.model.Axiom;
import au.csiro.ontology.model.BigIntegerLiteral;
import au.csiro.ontology.model.Concept;
import au.csiro.ontology.model.ConceptInclusion;
import au.csiro.ontology.model.Conjunction;
import au.csiro.ontology.model.Datatype;
import au.csiro.ontology.model.DateLiteral;
import au.csiro.ontology.model.DecimalLiteral;
import au.csiro.ontology.model.Existential;
import au.csiro.ontology.model.FloatLiteral;
import au.csiro.ontology.model.FunctionalFeature;
import au.csiro.ontology.model.IntegerLiteral;
import au.csiro.ontology.model.Literal;
import au.csiro.ontology.model.NamedConcept;
import au.csiro.ontology.model.NamedFeature;
import au.csiro.ontology.model.NamedRole;
import au.csiro.ontology.model.Operator;
import au.csiro.ontology.model.Role;
import au.csiro.ontology.model.RoleInclusion;
import au.csiro.ontology.model.StringLiteral;
import au.csiro.ontology.util.IProgressMonitor;
import au.csiro.ontology.util.Statistics;

/**
 * Imports axioms in OWL format into the internal representation used by
 * Snorocket. This initial implementation does not support versions.
 *
 * @author Alejandro Metke
 *
 */
public class OWLImporter extends BaseImporter {

    public static final String THING_IRI = "http://www.w3.org/2002/07/owl#Thing";
    public static final String NOTHING_IRI = "http://www.w3.org/2002/07/owl#Nothing";

    private final Set<OWLDataPropertyRangeAxiom> dprAxioms = new HashSet<>();
    private final List<String> problems = new ArrayList<>();

    private OWLOntology ontology;
    private List<OWLAxiom> axioms;

    private SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");

    /**
     * The OWL EL spec only allows using the following types: owl:real, owl:rational, xsd:decimal, xsd:integer,
     * xsd:nonNegativeInteger. The Java types that most naturally correspond to these types are BigInteger and
     * BigDecimal. However, the memory consumption increases and the performance decreases when using these types
     * instead of the Java built-in types. When the following flags are set to true, the transformation uses the
     * built-in data types instead of the math types.
     */
    private boolean useSimpleInts = true;
    private boolean useSimpleFloats = false;

    public OWLImporter(OWLOntology ontology) {
        this();
        this.ontology = ontology;
    }

    public OWLImporter(List<OWLAxiom> axioms) {
        this();
        this.axioms = axioms;
    }

    /**
     * Private constructor.
     */
    private OWLImporter() {

    }

    /**
     * @return the useSimpleInts
     */
    public boolean isUseSimpleInts() {
        return useSimpleInts;
    }

    /**
     * @param useSimpleInts the useSimpleInts to set
     */
    public void setUseSimpleInts(boolean useSimpleInts) {
        this.useSimpleInts = useSimpleInts;
    }

    /**
     * @return the useSimpleFloats
     */
    public boolean isUseSimpleFloats() {
        return useSimpleFloats;
    }

    /**
     * @param useSimpleFloats the useSimpleFloats to set
     */
    public void setUseSimpleFloats(boolean useSimpleFloats) {
        this.useSimpleFloats = useSimpleFloats;
    }

    private Collection<Axiom> transformOWLSubPropertyChainOfAxiom(OWLSubPropertyChainOfAxiom a) {
        List<OWLObjectPropertyExpression> sub = a.getPropertyChain();
        OWLObjectPropertyExpression sup = a.getSuperProperty();

        int size = sub.size();
        NamedRole[] lhss = new NamedRole[size];
        for (int i = 0; i < size; i++) {
            lhss[i] = new NamedRole(sub.get(i).asOWLObjectProperty().toStringID());
        }

        NamedRole rhs = new NamedRole(sup.asOWLObjectProperty().toStringID());

        if (lhss.length == 1 || lhss.length == 2) {
            return Arrays.asList(new RoleInclusion(lhss, rhs), new ConceptInclusion(new NamedConcept(lhss[0].getId()), new NamedConcept(rhs.getId())));
        } else {
            problems.add("Unable to import axiom "+a.toString()+". RoleChains longer than 2 not supported.");
            return null;
        }
    }

    private Collection<Axiom> transformOWLSubObjectPropertyOfAxiom(OWLSubObjectPropertyOfAxiom a) {
        OWLObjectPropertyExpression sub = a.getSubProperty();
        OWLObjectPropertyExpression sup = a.getSuperProperty();

        NamedRole lhs = new NamedRole(sub.asOWLObjectProperty().toStringID());
        NamedRole rhs = new NamedRole(sup.asOWLObjectProperty().toStringID());

        return Arrays.asList(new RoleInclusion(new Role[]{lhs}, rhs), new ConceptInclusion(new NamedConcept(lhs.getId()), new NamedConcept(rhs.getId())));
    }

    private Axiom transformOWLReflexiveObjectPropertyAxiom(OWLReflexiveObjectPropertyAxiom a) {
        OWLObjectPropertyExpression exp = a.getProperty();
        return new RoleInclusion(new Role[] {}, new NamedRole(exp.asOWLObjectProperty().toStringID()));
    }

    private Axiom transformOWLTransitiveObjectPropertyAxiom(OWLTransitiveObjectPropertyAxiom a) {
        OWLObjectPropertyExpression exp = a.getProperty();
        Role r = new NamedRole(exp.asOWLObjectProperty().toStringID());
        return new RoleInclusion(new Role[] { r, r }, r);
    }

    private Axiom transformOWLFunctionalDataPropertyAxiom(OWLFunctionalDataPropertyAxiom a) {
        OWLDataPropertyExpression prop = a.getProperty();
        NamedFeature feature = new NamedFeature(prop.asOWLDataProperty().toStringID());
        return new FunctionalFeature(feature);
    }

    private Axiom transformOWLSubClassOfAxiom(OWLSubClassOfAxiom a) {
        OWLClassExpression sub = a.getSubClass();
        OWLClassExpression sup = a.getSuperClass();

        try {
            Concept subConcept = getConcept(sub);
            Concept superConcept = getConcept(sup);
            return new ConceptInclusion(subConcept, superConcept);
        } catch(UnsupportedOperationException e) {
            problems.add(e.getMessage());
            return null;
        }
    }

    private List<Axiom> transformOWLEquivalentClassesAxiom(
            OWLEquivalentClassesAxiom a) {
        List<Axiom> axioms = new ArrayList<>();
        List<OWLClassExpression> exps = a.getClassExpressionsAsList();

        int size = exps.size();

        for (int i = 0; i < size - 1; i++) {
            try {
                OWLClassExpression e1 = exps.get(i);
                Concept concept1 = getConcept(e1);
                for (int j = i; j < size; j++) {
                    OWLClassExpression e2 = exps.get(j);
                    if (e1 == e2)
                        continue;
                    Concept concept2 = getConcept(e2);
                    axioms.add(new ConceptInclusion(concept1, concept2));
                    axioms.add(new ConceptInclusion(concept2, concept1));
                }
            } catch(UnsupportedOperationException e) {
                problems.add(e.getMessage());
            }
        }
        return axioms;
    }

    private Axiom transformOWLDisjointClassesAxiom(OWLDisjointClassesAxiom a) {
        try {
            List<OWLClassExpression> exps = a.getClassExpressionsAsList();
            List<Concept> concepts = new ArrayList<>();
            for (OWLClassExpression exp : exps) {
                concepts.add(getConcept(exp));
            }

            Concept[] conjs = new Concept[concepts.size()];
            int i = 0;
            for (; i < concepts.size(); i++) {
                conjs[i] = concepts.get(i);
            }

            return new ConceptInclusion(new Conjunction(conjs), NamedConcept.BOTTOM_CONCEPT);
        } catch(UnsupportedOperationException e) {
            problems.add(e.getMessage());
            return null;
        }
    }

    private List<Axiom> transformOWLEquivalentObjectPropertiesAxiom(
            OWLEquivalentObjectPropertiesAxiom a) {
        List<Axiom> axioms = new ArrayList<>();
        for (OWLSubObjectPropertyOfAxiom ax : a.asSubObjectPropertyOfAxioms()) {
            OWLObjectPropertyExpression sub = ax.getSubProperty();
            OWLObjectPropertyExpression sup = ax.getSuperProperty();

            axioms.add(
                    new RoleInclusion(new NamedRole(sub.asOWLObjectProperty().toStringID()),
                    new NamedRole(sup.asOWLObjectProperty().toStringID()))
            );
        }
        return axioms;
    }

    private Set<Axiom> transform(List<OWLAxiom> axioms, IProgressMonitor monitor) throws ImportException {
        monitor.taskStarted("Loading axioms");
        final Set<Axiom> res = new HashSet<>();
        int totalAxioms = axioms.size();
        int workDone = 0;

        for (OWLAxiom axiom : axioms) {
            if (axiom instanceof OWLDeclarationAxiom) {
                OWLDeclarationAxiom a = (OWLDeclarationAxiom)axiom;
                OWLEntity ent = a.getEntity();
                if (ent.isOWLClass()) {
                    res.add(new ConceptInclusion(
                            new NamedConcept(ent.asOWLClass().toStringID()), NamedConcept.TOP_CONCEPT));
                } else if (ent.isOWLObjectProperty()) {
                    // Do nothing for now.
                } else if (ent.isOWLDataProperty()) {
                    // Do nothing for now.
                }
            } else if (axiom instanceof OWLSubPropertyChainOfAxiom) {
                OWLSubPropertyChainOfAxiom a = (OWLSubPropertyChainOfAxiom) axiom;
                Collection<Axiom> ax = transformOWLSubPropertyChainOfAxiom(a);
                if(ax != null) res.addAll(ax);
                monitor.step(workDone, totalAxioms);
            } else if (axiom instanceof OWLSubObjectPropertyOfAxiom) {
                OWLSubObjectPropertyOfAxiom a = (OWLSubObjectPropertyOfAxiom) axiom;
                res.addAll(transformOWLSubObjectPropertyOfAxiom(a));
                monitor.step(++workDone, totalAxioms);
            } else if (axiom instanceof OWLReflexiveObjectPropertyAxiom) {
                OWLReflexiveObjectPropertyAxiom a = (OWLReflexiveObjectPropertyAxiom) axiom;
                res.add(transformOWLReflexiveObjectPropertyAxiom(a));
                monitor.step(++workDone, totalAxioms);
            } else if (axiom instanceof OWLTransitiveObjectPropertyAxiom) {
                OWLTransitiveObjectPropertyAxiom a = (OWLTransitiveObjectPropertyAxiom) axiom;
                res.add(transformOWLTransitiveObjectPropertyAxiom(a));
                monitor.step(++workDone, totalAxioms);
            } else if (axiom instanceof OWLSubClassOfAxiom) {
                OWLSubClassOfAxiom a = (OWLSubClassOfAxiom) axiom;
                Axiom ax = transformOWLSubClassOfAxiom(a);
                if(ax != null) res.add(ax);
                monitor.step(++workDone, totalAxioms);
            } else if (axiom instanceof OWLEquivalentClassesAxiom) {
                OWLEquivalentClassesAxiom a = (OWLEquivalentClassesAxiom) axiom;
                res.addAll(transformOWLEquivalentClassesAxiom(a));
                monitor.step(++workDone, totalAxioms);
            } else if (axiom instanceof OWLDisjointClassesAxiom) {
                OWLDisjointClassesAxiom a = (OWLDisjointClassesAxiom) axiom;
                Axiom ax = transformOWLDisjointClassesAxiom(a);
                if(ax != null) res.add(ax);
                monitor.step(++workDone, totalAxioms);
            } else if (axiom instanceof OWLEquivalentObjectPropertiesAxiom) {
                OWLEquivalentObjectPropertiesAxiom a = (OWLEquivalentObjectPropertiesAxiom) axiom;
                res.addAll(transformOWLEquivalentObjectPropertiesAxiom(a));
                monitor.step(++workDone, totalAxioms);
            } else if (axiom instanceof OWLFunctionalDataPropertyAxiom) {
                OWLFunctionalDataPropertyAxiom a = (OWLFunctionalDataPropertyAxiom) axiom;
                res.add(transformOWLFunctionalDataPropertyAxiom(a));
                monitor.step(++workDone, totalAxioms);
            } else if (axiom instanceof OWLAnnotationAssertionAxiom) {
                // Do nothing
                monitor.step(++workDone, totalAxioms);
            } else {
                problems.add("The axiom " + axiom.toString() + " is not currently supported by Snorocket.");
                System.err.println("The axiom " + axiom.toString() + " is not currently supported by Snorocket.");
            }
        }

        // TODO: deal with other axioms types even if Snorocket does not
        // currently support them

        monitor.taskEnded();

        /*
        if(!problems.isEmpty()) {
            throw new ImportException("Problems occurred during import. See getProblems()");
        }
        */

        return res;
    }

	private Set<Axiom> transform(OWLOntology ont, IProgressMonitor monitor) throws ImportException {
        return transform(Collections.list(Collections.enumeration(ont.getAxioms())), monitor);
    }

    /**
     *
     * @param l
     * @return
     */
    private Literal getLiteral(OWLLiteral l) {
        OWLDatatype dt = l.getDatatype();
        String literal = l.getLiteral();

        Literal res = null;

        if(dt.isBuiltIn()) {
            OWL2Datatype odt = dt.getBuiltInDatatype();
            switch (odt) {
                case RDF_PLAIN_LITERAL:
                case RDF_XML_LITERAL:
                case XSD_STRING:
                case XSD_NORMALIZED_STRING:
                case XSD_NAME:
                case XSD_NCNAME:
                case XSD_NMTOKEN:
                case XSD_HEX_BINARY:
                case XSD_BASE_64_BINARY:
                case XSD_ANY_URI:
                case XSD_TOKEN:
                    res = new StringLiteral(literal);
                    break;
                case XSD_INTEGER:
                case XSD_NON_NEGATIVE_INTEGER:
                    if(useSimpleInts) {
                        res = new IntegerLiteral(Integer.parseInt(literal));
                    } else {
                        res = new BigIntegerLiteral(new BigInteger(literal));
                    }
                    break;
                case XSD_DATE_TIME:
                    res = new DateLiteral(DatatypeConverter.parseDateTime(literal));
                    break;
                case OWL_RATIONAL:
                case OWL_REAL:
                case XSD_DECIMAL:
                    if(useSimpleFloats) {
                        res = new FloatLiteral(Float.parseFloat(literal));
                    } else {
                        res = new DecimalLiteral(new BigDecimal(literal));
                    }
                    break;
                default:
                    problems.add("Unsupported literal " + l);
            }
        } else {
            problems.add("Datatype is not built in: " + dt);
        }

        return res;
    }

    private void checkInconsistentProperty(OWLDataProperty dp, OWLDatatype type) {
        for (OWLDataPropertyRangeAxiom a : dprAxioms) {
            OWLDataPropertyExpression pe = a.getProperty();
            OWLDataRange r = a.getRange();
            // TODO: check DataOneOf
            // TODO: check OWLDataIntersectionOf
            OWLDatatype otype = r.asOWLDatatype();

            if (!pe.isAnonymous()) {
                if (!otype.equals(type)) {
                    problems.add("The literal value restriction " + dp + " is inconsistent with the data property " +
                    		"range axiom " + a);
                }
            } else {
                problems.add("Found anonymous data property expression in data property range axiom: " + pe);
            }
        }
    }

    /**
     *
     * @param desc
     * @return
     */
    private Concept getConcept(OWLClassExpression desc) {
        final Stack<Concept> stack = new Stack<>();
        desc.accept(new OWLClassExpressionVisitor() {

            private void unimplemented(OWLClassExpression e) {
                String message = "The class expression "+ e.getClassExpressionType().getName()+
                        " is not currently supported by Snorocket.";
                throw new UnsupportedOperationException(message);
            }

            private Concept pop() {
                return stack.pop();
            }

            private void push(Concept concept) {
                stack.push(concept);
            }

            @Override
            public void visit(OWLDataMaxCardinality e) {
                unimplemented(e);
            }

            @Override
            public void visit(OWLDataExactCardinality e) {
                unimplemented(e);
            }

            @Override
            public void visit(OWLDataMinCardinality e) {
                unimplemented(e);
            }

            @Override
            public void visit(OWLDataHasValue e) {
                OWLDataPropertyExpression dpe = e.getProperty();
                // TODO: consider the case where dpe is anonymous
                OWLDataProperty dp = dpe.asOWLDataProperty();
                OWLLiteral l = e.getFiller();
                OWLDatatype type = l.getDatatype();

                checkInconsistentProperty(dp, type);

                NamedFeature f = new NamedFeature(dp.toStringID());
                Literal lit = getLiteral(l);
                if(lit != null) {
                    push(new Datatype(f, Operator.EQUALS,lit));
                } else {
                    problems.add("Axiom " + e + " will be ignored because of the unsupported literal.");
                }
            }

            @Override
            public void visit(OWLDataAllValuesFrom e) {
                unimplemented(e);
            }

            @Override
            public void visit(OWLDataSomeValuesFrom e) {
                OWLDataProperty dp = e.getProperty().asOWLDataProperty();
                OWLDataRange range = e.getFiller();

                /*
                 * An OWLDataRange can be one of the following:
                 * Datatype | DataIntersectionOf | DataUnionOf |
                 * DataComplementOf | DataOneOf | DatatypeRestriction
                 *
                 * We initially support only DataOneOf.
                 */
                if(range instanceof OWLDataOneOf) {
                    OWLDataOneOf doo = (OWLDataOneOf)range;
                    Set<OWLLiteral> values = doo.getValues();
                    if(values.size() != 1) {
                        problems.add("Expected only a single literal in "+e);
                        return;
                    }
                    OWLLiteral l = (OWLLiteral)values.toArray()[0];
                    OWLDatatype type = l.getDatatype();
                    checkInconsistentProperty(dp, type);

                    NamedFeature f = new NamedFeature(dp.toStringID());
                    Literal lit = getLiteral(l);
                    if(lit != null) {
                        push(new Datatype(f, Operator.EQUALS, lit));
                    } else {
                        problems.add("Axiom " + e + " will be ignored because of the unsupported literal.");
                    }
                } else if(range instanceof OWLDatatypeRestriction) {
                    NamedFeature f = new NamedFeature(dp.toStringID());

                    OWLDatatypeRestriction dtr = (OWLDatatypeRestriction)range;
                    Set<OWLFacetRestriction> frs = dtr.getFacetRestrictions();

                    List<Datatype> conjuncts = new ArrayList<>();
                    for(OWLFacetRestriction fr : frs) {
                        OWLLiteral l = fr.getFacetValue();

                        Literal lit = getLiteral(l);

                        if(lit != null) {
                            checkInconsistentProperty(dp, l.getDatatype());
                            OWLFacet facet = fr.getFacet();

                            switch(facet) {
                                case MAX_EXCLUSIVE:
                                    conjuncts.add(new Datatype(f, Operator.LESS_THAN, lit));
                                    break;
                                case MAX_INCLUSIVE:
                                    conjuncts.add(new Datatype(f, Operator.LESS_THAN_EQUALS, lit));
                                    break;
                                case MIN_EXCLUSIVE:
                                    conjuncts.add(new Datatype(f, Operator.GREATER_THAN, lit));
                                    break;
                                case MIN_INCLUSIVE:
                                    conjuncts.add(new Datatype(f, Operator.GREATER_THAN_EQUALS, lit));
                                    break;
                                default:
                                    throw new RuntimeException("Unsupported facet "+facet);
                            }
                        } else {
                            problems.add("Axiom " + e + " will be ignored because of the unsupported literal.");
                        }
                    }

                    // Create conjunctions with all restrictions
                    if(conjuncts.size() == 1) {
                        push(conjuncts.get(0));
                    } else if(!conjuncts.isEmpty()){
                        push(new Conjunction(conjuncts));
                    }
                } else {
                    throw new RuntimeException("Unsupporter OWLDataRange: "+
                            range.getClass().getName());
                }
            }

            @Override
            public void visit(OWLObjectOneOf e) {
                // TODO: implement to support EL profile
                unimplemented(e);
            }

            @Override
            public void visit(OWLObjectHasSelf e) {
                // TODO: implement to support EL profile

                // There is no model object to support this.

                /*
                 * A self-restriction ObjectHasSelf( OPE ) consists of an object
                 * property expression OPE, and it contains all those
                 * individuals that are connected by OPE to themselves.
                 */

                /*Role r = new Role<>(e.getProperty().asOWLObjectProperty().toStringID());*/

                unimplemented(e);
            }

            @Override
            public void visit(OWLObjectMaxCardinality e) {
                unimplemented(e);
            }

            @Override
            public void visit(OWLObjectExactCardinality e) {
                unimplemented(e);
            }

            @Override
            public void visit(OWLObjectMinCardinality e) {
                unimplemented(e);
            }

            @Override
            public void visit(OWLObjectHasValue e) {
                // TODO: implement to support EL profile

                // We do not support individuals
                unimplemented(e);
            }

            @Override
            public void visit(OWLObjectAllValuesFrom e) {
                unimplemented(e);
            }

            @Override
            public void visit(OWLObjectSomeValuesFrom e) {
                NamedRole r = new NamedRole(e.getProperty().asOWLObjectProperty().toStringID());
                e.getFiller().accept(this);
                try {
                    push(new Existential(r, pop()));
                } catch(EmptyStackException ex) {
                    problems.add("Unable to add axiom " + e + " because of previous problems.");
                }
            }

            @Override
            public void visit(OWLObjectComplementOf e) {
                unimplemented(e);
            }

            @Override
            public void visit(OWLObjectUnionOf e) {
                unimplemented(e);
            }

            @Override
            public void visit(OWLObjectIntersectionOf e) {
                List<Concept> items = new ArrayList<>();

                for (OWLClassExpression desc : e.getOperands()) {
                    desc.accept(this);
                    try {
                        items.add(pop());
                    } catch(EmptyStackException ex) {
                        problems.add("Unable to add conjunct " + desc + " because of previous problems.");
                    }
                }

                if(!items.isEmpty()) {
                    Conjunction conj = new Conjunction(items);
                    push(conj);
                }
            }

            @Override
            public void visit(OWLClass e) {
                String id = e.toStringID();
                if (("<"+THING_IRI+">").equals(id) || THING_IRI.equals(id))
                    push(NamedConcept.TOP_CONCEPT);
                else if (("<"+NOTHING_IRI+">").equals(id) || NOTHING_IRI.equals(id))
                    push(NamedConcept.BOTTOM_CONCEPT);
                else
                    push(new NamedConcept(id));
            }

        });

        if (stack.size() != 1) {
            throw new RuntimeException("Stack size should be 1 but is " + stack.size());
        }

        return stack.pop();
    }

    /**
     * Clears all the state in the importer.
     */
    public void clear() {
        dprAxioms.clear();
        problems.clear();
    }

    /**
     * @return the problems
     */
    @Override
    public List<String> getProblems() {
        return problems;
    }

    @Override
    public Iterator<Ontology> getOntologyVersions(IProgressMonitor monitor) {
        return new OntologyIterator(monitor);
    }

    class OntologyIterator implements Iterator<Ontology> {
        private boolean accessed = false;
        private IProgressMonitor monitor;

        public OntologyIterator(IProgressMonitor monitor) {
            this.monitor = monitor;
        }

        @Override
        public boolean hasNext() {
            return !accessed;
        }

        @Override
        public Ontology next() throws IllegalArgumentException, RuntimeException {
            long start = System.currentTimeMillis();

            Set<Axiom> ont = null;
            try {
                if(ontology != null) {
                    ont = transform(ontology, monitor);
                } else if(axioms != null) {
                    ont = transform(axioms, monitor);
                } else {
                    throw new IllegalArgumentException("No OWL ontology to transform.");
                }
            } catch (ImportException e) {
                throw new RuntimeException(e);
            }

            String id = null;
            if(ontology != null) {
                id = ontology.getOntologyID().toString();
            } else {
                id = "incremental";
            }

            String version = sdf.format(new Date());

            Ontology res = new Ontology(id, version, ont, null);
            Statistics.INSTANCE.setTime("owl loading", System.currentTimeMillis() - start);
            accessed = true;
            return res;
        }

        @Override
        public void remove() {
            throw new UnsupportedOperationException();
        }

    }

}
