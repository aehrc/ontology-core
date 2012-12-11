/**
 * Copyright CSIRO Australian e-Health Research Centre (http://aehrc.com).
 * All rights reserved. Use is subject to license terms and conditions. 
 */
package au.csiro.ontology.importer.owl;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Stack;

import javax.xml.bind.DatatypeConverter;

import org.semanticweb.owlapi.model.AxiomType;
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
import org.semanticweb.owlapi.model.OWLDeclarationAxiom;
import org.semanticweb.owlapi.model.OWLDisjointClassesAxiom;
import org.semanticweb.owlapi.model.OWLEntity;
import org.semanticweb.owlapi.model.OWLEquivalentClassesAxiom;
import org.semanticweb.owlapi.model.OWLEquivalentObjectPropertiesAxiom;
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

import au.csiro.ontology.IOntology;
import au.csiro.ontology.Ontology;
import au.csiro.ontology.axioms.ConceptInclusion;
import au.csiro.ontology.axioms.IAxiom;
import au.csiro.ontology.axioms.RoleInclusion;
import au.csiro.ontology.classification.IProgressMonitor;
import au.csiro.ontology.importer.IImporter;
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
import au.csiro.ontology.model.IRole;
import au.csiro.ontology.model.IntegerLiteral;
import au.csiro.ontology.model.LongLiteral;
import au.csiro.ontology.model.Operator;
import au.csiro.ontology.model.Role;
import au.csiro.ontology.model.StringLiteral;

/**
 * Imports axioms in OWL format into the internal representation used by
 * Snorocket. This initial implementation does not support versions.
 * 
 * @author Alejandro Metke
 * 
 */
public class OWLImporter implements IImporter {
    
    public static final String THING_IRI = "http://www.w3.org/2002/07/owl#Thing";
    public static final String NOTHING_IRI = "http://www.w3.org/2002/07/owl#Nothing";
    
    private final Set<OWLDataPropertyRangeAxiom> dprAxioms = 
            new HashSet<OWLDataPropertyRangeAxiom>();
    private final Map<OWL2Datatype, Set<OWL2Datatype>> types = new HashMap<>();
    private final List<String> problems = new ArrayList<String>();
    
    private OWLOntology ontology;
    private List<OWLAxiom> axioms;
    
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
        Set<OWL2Datatype> set = new HashSet<OWL2Datatype>();
        set.add(OWL2Datatype.XSD_BYTE);
        types.put(OWL2Datatype.XSD_BYTE, set);

        set = new HashSet<OWL2Datatype>();
        set.add(OWL2Datatype.XSD_SHORT);
        set.addAll(types.get(OWL2Datatype.XSD_BYTE));
        types.put(OWL2Datatype.XSD_SHORT, set);

        set = new HashSet<OWL2Datatype>();
        set.add(OWL2Datatype.XSD_INT);
        set.addAll(types.get(OWL2Datatype.XSD_SHORT));
        types.put(OWL2Datatype.XSD_INT, set);

        set = new HashSet<OWL2Datatype>();
        set.add(OWL2Datatype.XSD_LONG);
        set.addAll(types.get(OWL2Datatype.XSD_INT));
        types.put(OWL2Datatype.XSD_LONG, set);

        set = new HashSet<OWL2Datatype>();
        set.add(OWL2Datatype.XSD_UNSIGNED_BYTE);
        types.put(OWL2Datatype.XSD_UNSIGNED_BYTE, set);

        set = new HashSet<OWL2Datatype>();
        set.add(OWL2Datatype.XSD_UNSIGNED_SHORT);
        set.addAll(types.get(OWL2Datatype.XSD_UNSIGNED_BYTE));
        types.put(OWL2Datatype.XSD_UNSIGNED_SHORT, set);

        set = new HashSet<OWL2Datatype>();
        set.add(OWL2Datatype.XSD_UNSIGNED_INT);
        set.addAll(types.get(OWL2Datatype.XSD_UNSIGNED_SHORT));
        types.put(OWL2Datatype.XSD_UNSIGNED_INT, set);

        set = new HashSet<OWL2Datatype>();
        set.add(OWL2Datatype.XSD_UNSIGNED_LONG);
        set.addAll(types.get(OWL2Datatype.XSD_UNSIGNED_INT));
        types.put(OWL2Datatype.XSD_UNSIGNED_LONG, set);

        set = new HashSet<OWL2Datatype>();
        set.add(OWL2Datatype.XSD_POSITIVE_INTEGER);
        types.put(OWL2Datatype.XSD_POSITIVE_INTEGER, set);

        set = new HashSet<OWL2Datatype>();
        set.add(OWL2Datatype.XSD_NON_NEGATIVE_INTEGER);
        set.addAll(types.get(OWL2Datatype.XSD_POSITIVE_INTEGER));
        set.addAll(types.get(OWL2Datatype.XSD_UNSIGNED_LONG));
        types.put(OWL2Datatype.XSD_NON_NEGATIVE_INTEGER, set);

        set = new HashSet<OWL2Datatype>();
        set.add(OWL2Datatype.XSD_NEGATIVE_INTEGER);
        types.put(OWL2Datatype.XSD_NEGATIVE_INTEGER, set);

        set = new HashSet<OWL2Datatype>();
        set.add(OWL2Datatype.XSD_NON_POSITIVE_INTEGER);
        set.addAll(types.get(OWL2Datatype.XSD_NEGATIVE_INTEGER));
        types.put(OWL2Datatype.XSD_NON_POSITIVE_INTEGER, set);

        set = new HashSet<OWL2Datatype>();
        set.add(OWL2Datatype.XSD_INTEGER);
        set.addAll(types.get(OWL2Datatype.XSD_NON_POSITIVE_INTEGER));
        set.addAll(types.get(OWL2Datatype.XSD_NON_NEGATIVE_INTEGER));
        set.addAll(types.get(OWL2Datatype.XSD_LONG));
        types.put(OWL2Datatype.XSD_INTEGER, set);

        set = new HashSet<OWL2Datatype>();
        set.add(OWL2Datatype.XSD_DECIMAL);
        set.addAll(types.get(OWL2Datatype.XSD_INTEGER));
        types.put(OWL2Datatype.XSD_DECIMAL, set);

        set = new HashSet<OWL2Datatype>();
        set.add(OWL2Datatype.XSD_DOUBLE);
        types.put(OWL2Datatype.XSD_DOUBLE, set);

        set = new HashSet<OWL2Datatype>();
        set.add(OWL2Datatype.XSD_FLOAT);
        types.put(OWL2Datatype.XSD_FLOAT, set);

        set = new HashSet<OWL2Datatype>();
        set.add(OWL2Datatype.XSD_NCNAME);
        types.put(OWL2Datatype.XSD_NCNAME, set);

        set = new HashSet<OWL2Datatype>();
        set.add(OWL2Datatype.XSD_NAME);
        set.addAll(types.get(OWL2Datatype.XSD_NCNAME));
        types.put(OWL2Datatype.XSD_NAME, set);

        set = new HashSet<OWL2Datatype>();
        set.add(OWL2Datatype.XSD_NMTOKEN);
        types.put(OWL2Datatype.XSD_NMTOKEN, set);

        set = new HashSet<OWL2Datatype>();
        set.add(OWL2Datatype.XSD_LANGUAGE);
        types.put(OWL2Datatype.XSD_LANGUAGE, set);

        set = new HashSet<OWL2Datatype>();
        set.add(OWL2Datatype.XSD_TOKEN);
        set.addAll(types.get(OWL2Datatype.XSD_LANGUAGE));
        set.addAll(types.get(OWL2Datatype.XSD_NMTOKEN));
        set.addAll(types.get(OWL2Datatype.XSD_NAME));
        types.put(OWL2Datatype.XSD_TOKEN, set);

        set = new HashSet<OWL2Datatype>();
        set.add(OWL2Datatype.XSD_NORMALIZED_STRING);
        set.addAll(types.get(OWL2Datatype.XSD_TOKEN));
        types.put(OWL2Datatype.XSD_NORMALIZED_STRING, set);

        set = new HashSet<OWL2Datatype>();
        set.add(OWL2Datatype.XSD_STRING);
        set.addAll(types.get(OWL2Datatype.XSD_NORMALIZED_STRING));
        types.put(OWL2Datatype.XSD_STRING, set);

        set = new HashSet<OWL2Datatype>();
        set.add(OWL2Datatype.XSD_BOOLEAN);
        types.put(OWL2Datatype.XSD_BOOLEAN, set);

        set = new HashSet<OWL2Datatype>();
        set.add(OWL2Datatype.XSD_HEX_BINARY);
        types.put(OWL2Datatype.XSD_HEX_BINARY, set);

        set = new HashSet<OWL2Datatype>();
        set.add(OWL2Datatype.XSD_BASE_64_BINARY);
        types.put(OWL2Datatype.XSD_BASE_64_BINARY, set);

        set = new HashSet<OWL2Datatype>();
        set.add(OWL2Datatype.XSD_ANY_URI);
        types.put(OWL2Datatype.XSD_ANY_URI, set);

        set = new HashSet<OWL2Datatype>();
        set.add(OWL2Datatype.XSD_DATE_TIME);
        types.put(OWL2Datatype.XSD_DATE_TIME, set);

        set = new HashSet<OWL2Datatype>();
        set.add(OWL2Datatype.XSD_DATE_TIME_STAMP);
        types.put(OWL2Datatype.XSD_DATE_TIME_STAMP, set);

        set = new HashSet<OWL2Datatype>();
        set.add(OWL2Datatype.OWL_RATIONAL);
        types.put(OWL2Datatype.OWL_RATIONAL, set);

        set = new HashSet<OWL2Datatype>();
        set.add(OWL2Datatype.OWL_REAL);
        types.put(OWL2Datatype.OWL_REAL, set);

        set = new HashSet<OWL2Datatype>();
        set.add(OWL2Datatype.RDF_PLAIN_LITERAL);
        types.put(OWL2Datatype.RDF_PLAIN_LITERAL, set);

        set = new HashSet<OWL2Datatype>();
        set.add(OWL2Datatype.RDFS_LITERAL);
        types.put(OWL2Datatype.RDFS_LITERAL, set);

        set = new HashSet<OWL2Datatype>();
        set.add(OWL2Datatype.RDF_XML_LITERAL);
        types.put(OWL2Datatype.RDF_XML_LITERAL, set);
    }

    private IAxiom transformOWLSubPropertyChainOfAxiom(
            OWLSubPropertyChainOfAxiom a) {
        List<OWLObjectPropertyExpression> sub = a.getPropertyChain();
        OWLObjectPropertyExpression sup = a.getSuperProperty();

        int size = sub.size();
        IRole[] lhss = new IRole[size];
        for (int i = 0; i < size; i++) {
            lhss[i] = new Role<>(sub.get(i).asOWLObjectProperty().toStringID());
        }

        Role<String> rhs = new Role<>(sup.asOWLObjectProperty().toStringID());

        if (lhss.length == 1 || lhss.length == 2) {
            return new RoleInclusion(lhss, rhs);
        } else {
            throw new RuntimeException(
                    "RoleChains longer than 2 not supported.");
        }
    }

    private IAxiom transformOWLSubObjectPropertyOfAxiom(
            OWLSubObjectPropertyOfAxiom a) {
        OWLObjectPropertyExpression sub = a.getSubProperty();
        OWLObjectPropertyExpression sup = a.getSuperProperty();

        Role<String> lhs = new Role<>(sub.asOWLObjectProperty().toStringID());
        Role<String> rhs = new Role<>(sup.asOWLObjectProperty().toStringID());

        return new RoleInclusion(new Role[]{lhs}, rhs);
    }

    private IAxiom transformOWLReflexiveObjectPropertyAxiom(
            OWLReflexiveObjectPropertyAxiom a) {
        OWLObjectPropertyExpression exp = a.getProperty();
        return new RoleInclusion(new Role[] {}, 
                new Role<>(exp.asOWLObjectProperty().toStringID()));
    }

    private IAxiom transformOWLTransitiveObjectPropertyAxiom(
            OWLTransitiveObjectPropertyAxiom a) {
        OWLObjectPropertyExpression exp = a.getProperty();
        Role<String> r = new Role<>(exp.asOWLObjectProperty().toStringID());
        return new RoleInclusion(new Role[] { r, r }, r);
    }

    private IAxiom transformOWLSubClassOfAxiom(OWLSubClassOfAxiom a) {
        OWLClassExpression sub = a.getSubClass();
        OWLClassExpression sup = a.getSuperClass();

        IConcept subConcept = getConcept(sub);
        IConcept superConcept = getConcept(sup);

        if (subConcept != null && superConcept != null) {
            return new ConceptInclusion(subConcept, superConcept);
        } else {
            throw new RuntimeException("Unable to load axiom " + a);
        }
    }

    private List<IAxiom> transformOWLEquivalentClassesAxiom(
            OWLEquivalentClassesAxiom a) {
        List<IAxiom> axioms = new ArrayList<>();
        List<OWLClassExpression> exps = a.getClassExpressionsAsList();

        int size = exps.size();

        for (int i = 0; i < size - 1; i++) {
            OWLClassExpression e1 = exps.get(i);
            IConcept concept1 = getConcept(e1);
            for (int j = i; j < size; j++) {
                OWLClassExpression e2 = exps.get(j);
                if (e1 == e2)
                    continue;
                IConcept concept2 = getConcept(e2);
                axioms.add(new ConceptInclusion(concept1, concept2));
                axioms.add(new ConceptInclusion(concept2, concept1));
            }
        }
        return axioms;
    }

    private IAxiom transformOWLDisjointClassesAxiom(OWLDisjointClassesAxiom a) {
        List<OWLClassExpression> exps = a.getClassExpressionsAsList();
        List<IConcept> concepts = new ArrayList<IConcept>();
        for (OWLClassExpression exp : exps) {
            concepts.add(getConcept(exp));
        }

        IConcept[] conjs = new IConcept[concepts.size()];
        int i = 0;
        for (; i < concepts.size(); i++) {
            conjs[i] = concepts.get(i);
        }

        return new ConceptInclusion(new Conjunction(conjs), Concept.BOTTOM);
    }

    private List<IAxiom> transformOWLEquivalentObjectPropertiesAxiom(
            OWLEquivalentObjectPropertiesAxiom a) {
        List<IAxiom> axioms = new ArrayList<>();
        for (OWLSubObjectPropertyOfAxiom ax : a.asSubObjectPropertyOfAxioms()) {
            OWLObjectPropertyExpression sub = ax.getSubProperty();
            OWLObjectPropertyExpression sup = ax.getSuperProperty();

            axioms.add(new RoleInclusion(new Role<>(sub.asOWLObjectProperty()
                    .toStringID()), new Role<>(sup.asOWLObjectProperty()
                    .toStringID())));
        }
        return axioms;
    }

    private Set<IAxiom> transform(List<OWLAxiom> axioms, 
            IProgressMonitor monitor) {
        monitor.taskStarted("Loading axioms");
        final Set<IAxiom> res = new HashSet<>();
        int totalAxioms = axioms.size();
        int workDone = 0;

        for (OWLAxiom axiom : axioms) {
            if (axiom instanceof OWLDeclarationAxiom) {
                OWLDeclarationAxiom a = (OWLDeclarationAxiom)axiom;
                OWLEntity ent = a.getEntity();
                if (ent.isOWLClass()) {
                    res.add(new ConceptInclusion(
                            new Concept<>(ent.asOWLClass().toStringID()), 
                            Concept.TOP));
                } else if (ent.isOWLObjectProperty()) {
                    // Do nothing for now.
                } else if (ent.isOWLDataProperty()) {
                    // Do nothing for now.
                }
            } else if (axiom instanceof OWLSubPropertyChainOfAxiom) {
                OWLSubPropertyChainOfAxiom a = (OWLSubPropertyChainOfAxiom) axiom;
                res.add(transformOWLSubPropertyChainOfAxiom(a));
                monitor.step(workDone, totalAxioms);
            } else if (axiom instanceof OWLSubObjectPropertyOfAxiom) {
                OWLSubObjectPropertyOfAxiom a = (OWLSubObjectPropertyOfAxiom) axiom;
                res.add(transformOWLSubObjectPropertyOfAxiom(a));
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
                res.add(transformOWLSubClassOfAxiom(a));
                monitor.step(++workDone, totalAxioms);
            } else if (axiom instanceof OWLEquivalentClassesAxiom) {
                OWLEquivalentClassesAxiom a = (OWLEquivalentClassesAxiom) axiom;
                res.addAll(transformOWLEquivalentClassesAxiom(a));
                monitor.step(++workDone, totalAxioms);
            } else if (axiom instanceof OWLDisjointClassesAxiom) {
                OWLDisjointClassesAxiom a = (OWLDisjointClassesAxiom) axiom;
                res.add(transformOWLDisjointClassesAxiom(a));
                monitor.step(++workDone, totalAxioms);
            } else if (axiom instanceof OWLEquivalentObjectPropertiesAxiom) {
                OWLEquivalentObjectPropertiesAxiom a = (OWLEquivalentObjectPropertiesAxiom) axiom;
                res.addAll(transformOWLEquivalentObjectPropertiesAxiom(a));
                monitor.step(++workDone, totalAxioms);
            }
        }

        // TODO: deal with other axioms types even if Snorocket does not
        // currently support them
        monitor.taskEnded();
        return res;
    }

    private Set<IAxiom> transform(OWLOntology ont, IProgressMonitor monitor) {
        monitor.taskStarted("Loading axioms");
        final Set<IAxiom> axioms = new HashSet<>();

        int totalAxioms = ont.getAxiomCount(AxiomType.DECLARATION, true)
                + ont.getAxiomCount(AxiomType.SUB_OBJECT_PROPERTY, true)
                + ont.getAxiomCount(AxiomType.REFLEXIVE_OBJECT_PROPERTY, true)
                + ont.getAxiomCount(AxiomType.TRANSITIVE_OBJECT_PROPERTY, true)
                + ont.getAxiomCount(AxiomType.SUB_PROPERTY_CHAIN_OF, true)
                + ont.getAxiomCount(AxiomType.SUBCLASS_OF, true)
                + ont.getAxiomCount(AxiomType.EQUIVALENT_CLASSES, true)
                + ont.getAxiomCount(AxiomType.DISJOINT_CLASSES, true)
                + ont.getAxiomCount(AxiomType.EQUIVALENT_OBJECT_PROPERTIES,
                        true)
                + ont.getAxiomCount(AxiomType.DATA_PROPERTY_RANGE, true);

        int workDone = 0;
        
        for (OWLDeclarationAxiom a : ont.getAxioms(AxiomType.DECLARATION, true)) {
            OWLEntity ent = a.getEntity();
            if (ent.isOWLClass()) {
                axioms.add(new ConceptInclusion(
                        new Concept<>(ent.asOWLClass().toStringID()), 
                        Concept.TOP));
            } else if (ent.isOWLObjectProperty()) {
                // Do nothing for now.
            } else if (ent.isOWLDataProperty()) {
                // Do nothing for now.
            }
            workDone++;
            monitor.step(workDone, totalAxioms);
        }

        for (OWLDataPropertyRangeAxiom a : ont.getAxioms(
                AxiomType.DATA_PROPERTY_RANGE, true)) {
            dprAxioms.add(a);
            workDone++;
            monitor.step(workDone, totalAxioms);
        }

        for (OWLSubPropertyChainOfAxiom a : ont.getAxioms(
                AxiomType.SUB_PROPERTY_CHAIN_OF, true)) {
            axioms.add(transformOWLSubPropertyChainOfAxiom(a));
            workDone++;
            monitor.step(workDone, totalAxioms);
        }

        for (OWLSubObjectPropertyOfAxiom a : ont.getAxioms(
                AxiomType.SUB_OBJECT_PROPERTY, true)) {
            axioms.add(transformOWLSubObjectPropertyOfAxiom(a));
            workDone++;
            monitor.step(workDone, totalAxioms);
        }

        for (OWLReflexiveObjectPropertyAxiom a : ont.getAxioms(
                AxiomType.REFLEXIVE_OBJECT_PROPERTY, true)) {
            axioms.add(transformOWLReflexiveObjectPropertyAxiom(a));
            workDone++;
            monitor.step(workDone, totalAxioms);
        }

        for (OWLTransitiveObjectPropertyAxiom a : ont.getAxioms(
                AxiomType.TRANSITIVE_OBJECT_PROPERTY, true)) {
            axioms.add(transformOWLTransitiveObjectPropertyAxiom(a));
            workDone++;
            monitor.step(workDone, totalAxioms);
        }

        for (OWLSubClassOfAxiom a : ont.getAxioms(AxiomType.SUBCLASS_OF, true)) {
            axioms.add(transformOWLSubClassOfAxiom(a));
            workDone++;
            monitor.step(workDone, totalAxioms);
        }

        for (OWLEquivalentClassesAxiom a : ont.getAxioms(
                AxiomType.EQUIVALENT_CLASSES, true)) {
            axioms.addAll(transformOWLEquivalentClassesAxiom(a));
            workDone++;
            monitor.step(workDone, totalAxioms);
        }

        for (OWLDisjointClassesAxiom a : ont.getAxioms(
                AxiomType.DISJOINT_CLASSES, true)) {
            axioms.add(transformOWLDisjointClassesAxiom(a));
            workDone++;
            monitor.step(workDone, totalAxioms);
        }

        for (OWLEquivalentObjectPropertiesAxiom a : ont.getAxioms(
                AxiomType.EQUIVALENT_OBJECT_PROPERTIES, true)) {
            axioms.addAll(transformOWLEquivalentObjectPropertiesAxiom(a));
            workDone++;
            monitor.step(workDone, totalAxioms);
        }

        // TODO: deal with other axioms types even if Snorocket does not
        // currently support them
        monitor.taskEnded();
        return axioms;
    }

    /**
     * 
     * @param l
     * @return
     */
    private ILiteral getLiteral(OWLLiteral l) {
        OWLDatatype dt = l.getDatatype();
        String literal = l.getLiteral();

        ILiteral res = null;

        if (dt.isBoolean()) {
            res = new BooleanLiteral(Boolean.parseBoolean(literal));
        } else if (dt.isDouble()) {
            res = new DoubleLiteral(Double.parseDouble(literal));
        } else if (dt.isFloat()) {
            res = new FloatLiteral(Float.parseFloat(literal));
        } else if (dt.isInteger()) {
            res = new IntegerLiteral(Integer.parseInt(literal));
        } else if (dt.isRDFPlainLiteral()) {
            res = new StringLiteral(literal);
        } else {
            OWL2Datatype odt = dt.getBuiltInDatatype();
            switch (odt) {
            case XSD_LONG:
                res = new LongLiteral(Long.parseLong(literal));
                break;
            case XSD_DATE_TIME:
                res = new DateLiteral(DatatypeConverter.parseDateTime(literal));
                break;
            default:
                throw new IllegalArgumentException("Unsupported literal " + l);
            }
        }

        return res;
    }

    /**
     * Determines if the datatype specified in a property is compatible with an
     * actual datatype.
     * 
     * @param propertyType
     * @param actualType
     * @return
     */
    private boolean compatibleTypes(OWLDatatype propertyType,
            OWLDatatype actualType) {
        OWL2Datatype pt = propertyType.getBuiltInDatatype();
        OWL2Datatype at = actualType.getBuiltInDatatype();
        Set<OWL2Datatype> compatible = types.get(pt);
        boolean res = false;
        if (compatible != null && compatible.contains(at)) {
            res = true;
        }
        return res;
    }
    
    private void checkInconsistentProperty(OWLDataProperty dp, 
            OWLDatatype type) {
        for (OWLDataPropertyRangeAxiom a : dprAxioms) {
            OWLDataPropertyExpression pe = a.getProperty();
            OWLDataRange r = a.getRange();
            // TODO: check DataOneOf
            // TODO: check OWLDataIntersectionOf
            OWLDatatype otype = r.asOWLDatatype();

            if (!pe.isAnonymous()) {
                OWLDataProperty odp = pe.asOWLDataProperty();

                if (dp.equals(odp)) {
                    boolean compatible = compatibleTypes(otype, type);
                    if (!compatible) {
                        // throw new InconsistentOntologyException();
                        problems.add("The literal value restriction "
                                + dp + " is inconsistent with the data "
                                + "property range axiom " + a);
                    }
                }
            } else {
                System.err.println("Found anonymous data property "
                        + "expression in data property range axiom: "
                        + pe);
            }
        }
    }

    /**
     * 
     * @param desc
     * @return
     */
    private IConcept getConcept(OWLClassExpression desc) {
        final Stack<IConcept> stack = new Stack<IConcept>();
        desc.accept(new OWLClassExpressionVisitor() {

            private void unimplemented(OWLClassExpression e) {
                System.err.println("not implemented: " + e);
            }

            private IConcept pop() {
                return stack.pop();
            }

            private void push(IConcept concept) {
                stack.push(concept);
            }

            public void visit(OWLDataMaxCardinality e) {
                unimplemented(e);
            }

            public void visit(OWLDataExactCardinality e) {
                unimplemented(e);
            }

            public void visit(OWLDataMinCardinality e) {
                unimplemented(e);
            }

            public void visit(OWLDataHasValue e) {
                OWLDataPropertyExpression dpe = e.getProperty();
                // TODO: consider the case where dpe is anonymous
                OWLDataProperty dp = dpe.asOWLDataProperty();
                OWLLiteral l = e.getValue();
                OWLDatatype type = l.getDatatype();

                checkInconsistentProperty(dp, type);

                Feature<String> f = new Feature<>(dp.toStringID());
                push(new Datatype<>(f, Operator.EQUALS, getLiteral(l)));
            }

            public void visit(OWLDataAllValuesFrom e) {
                unimplemented(e);
            }

            public void visit(OWLDataSomeValuesFrom e) {
                // TODO: apparently there is no way to include multiple data
                // property expressions in OWLAPI even though the spec allows it
                OWLDataProperty dp = e.getProperty().asOWLDataProperty();
                OWLDataRange range = e.getFiller();
                OWLLiteral l = null;
                
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
                } else {
                    problems.add("Only a data range of type OWLDataOneOf is " +
                    	"supported in an OWLDataSomeValuesFrom expression. " +
                    	"Found a different type in "+e);
                    return;
                }
                
                OWLDatatype type = range.asOWLDatatype();
                checkInconsistentProperty(dp, type);
                
                Feature<String> f = new Feature<>(dp.toStringID());
                push(new Datatype<>(f, Operator.EQUALS, getLiteral(l)));
            }

            public void visit(OWLObjectOneOf e) {
                // TODO: implement to support EL profile
                unimplemented(e);
            }

            public void visit(OWLObjectHasSelf e) {
                // TODO: implement to support EL profile
                
                // There is no model object to support this.
                
                /*
                 * A self-restriction ObjectHasSelf( OPE ) consists of an object
                 * property expression OPE, and it contains all those 
                 * individuals that are connected by OPE to themselves.
                 */
                
                /*Role<String> r = new Role<>(
                        e.getProperty().asOWLObjectProperty().toStringID());*/

                unimplemented(e);
            }

            public void visit(OWLObjectMaxCardinality e) {
                unimplemented(e);
            }

            public void visit(OWLObjectExactCardinality e) {
                unimplemented(e);
            }

            public void visit(OWLObjectMinCardinality e) {
                unimplemented(e);
            }

            public void visit(OWLObjectHasValue e) {
                // TODO: implement to support EL profile
                
                // We do not support individuals
                unimplemented(e);
            }

            public void visit(OWLObjectAllValuesFrom e) {
                unimplemented(e);
            }

            public void visit(OWLObjectSomeValuesFrom e) {
                Role<String> r = new Role<>(e.getProperty().asOWLObjectProperty()
                        .toStringID());
                e.getFiller().accept(this);
                push(new Existential<>(r, pop()));
            }

            public void visit(OWLObjectComplementOf e) {
                unimplemented(e);
            }

            public void visit(OWLObjectUnionOf e) {
                unimplemented(e);
            }

            public void visit(OWLObjectIntersectionOf e) {
                List<IConcept> items = new ArrayList<IConcept>();

                for (OWLClassExpression desc : e.getOperands()) {
                    desc.accept(this);
                    items.add(pop());
                }

                Conjunction conj = new Conjunction(items);
                push(conj);
            }

            public void visit(OWLClass e) {
                String id = e.toStringID();
                if (("<"+THING_IRI+">").equals(id) || THING_IRI.equals(id))
                    push(Concept.TOP);
                else if (("<"+NOTHING_IRI+">").equals(id)
                        || NOTHING_IRI.equals(id))
                    push(Concept.BOTTOM);
                else
                    push(new Concept<>(id));
            }

        });

        if (stack.size() != 1) {
            throw new RuntimeException("Stack size should be 1 but is "
                    + stack.size());
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
    public List<String> getProblems() {
        return problems;
    }

    @Override
    public Map<String, Map<String, IOntology<String>>> getOntologyVersions(
            IProgressMonitor monitor) {
        Set<IAxiom> ont = null;
        String url = null;
        if(ontology != null) {
            ont = transform(ontology, monitor);
            url = ontology.getOntologyID().toString();
        } else if(axioms != null) {
            ont = transform(axioms, monitor);
            url = "incremental";
        } else {
            throw new IllegalArgumentException("No OWL ontology to transform.");
        }
        
        // TODO: extract the additional information from the OWL ontology
        Ontology<String> o = new Ontology<>(ont, null, null);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
        Map<String, Map<String, IOntology<String>>> res = new HashMap<>();
        Map<String, IOntology<String>> map = new HashMap<>();
        map.put(sdf.format(new Date()), o);
        res.put(url, map);
             
        return res;
    }

}
