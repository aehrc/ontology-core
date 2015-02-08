/**
 * Copyright CSIRO Australian e-Health Research Centre (http://aehrc.com).
 * All rights reserved. Use is subject to license terms and conditions.
 */
package au.csiro.ontology.util;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.UnsupportedEncodingException;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.eclipse.persistence.jaxb.MarshallerProperties;

import au.csiro.ontology.model.Axiom;
import au.csiro.ontology.model.BigIntegerLiteral;
import au.csiro.ontology.model.BooleanLiteral;
import au.csiro.ontology.model.Concept;
import au.csiro.ontology.model.ConceptInclusion;
import au.csiro.ontology.model.Conjunction;
import au.csiro.ontology.model.Datatype;
import au.csiro.ontology.model.DateLiteral;
import au.csiro.ontology.model.DecimalLiteral;
import au.csiro.ontology.model.DoubleLiteral;
import au.csiro.ontology.model.Existential;
import au.csiro.ontology.model.FloatLiteral;
import au.csiro.ontology.model.IntegerLiteral;
import au.csiro.ontology.model.Literal;
import au.csiro.ontology.model.LongLiteral;
import au.csiro.ontology.model.NamedConcept;
import au.csiro.ontology.model.NamedFeature;
import au.csiro.ontology.model.NamedRole;
import au.csiro.ontology.model.RoleInclusion;
import au.csiro.ontology.model.StringLiteral;

/**
 * Several utilities for axioms.
 * 
 * @author Alejandro Metke
 *
 */
@SuppressWarnings("deprecation")
public class AxiomUtils {
    
    /**
     * Logger.
     */
    private final static Logger log = LoggerFactory.getLogger(AxiomUtils.class);
    
    private static Marshaller marshaller;
    private static Unmarshaller unmarshaller;
    
    private static void init() {
        // Initialise JAXB
        try {
            JAXBContext jc = JAXBContext.newInstance(new Class[] { ConceptInclusion.class, RoleInclusion.class, 
                    NamedConcept.class, Conjunction.class, Existential.class, Datatype.class, NamedFeature.class, 
                    NamedRole.class, IntegerLiteral.class, StringLiteral.class, LongLiteral.class, DateLiteral.class, 
                    DecimalLiteral.class, BigIntegerLiteral.class, FloatLiteral.class, DoubleLiteral.class, 
                    BooleanLiteral.class}); 
            marshaller = jc.createMarshaller();
            marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
            marshaller.setProperty(MarshallerProperties.MEDIA_TYPE, "application/json");
            unmarshaller = jc.createUnmarshaller();
            unmarshaller.setProperty(MarshallerProperties.MEDIA_TYPE, "application/json");
        } catch(JAXBException e) {
            log.error("There was a problem initialising JAXB.", e);
        }
    }
    
    /**
     * Transforms a concept into a {@link String} representation.
     * 
     * @param concept
     * @return
     * @throws RuntimeException
     */
    public static String serialiseConcept(Concept concept) {
        if(marshaller == null) init();
        try {
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            marshaller.marshal(concept, baos);
            return baos.toString("UTF8");
        } catch(JAXBException e) {
            log.error("There was a problem serialising a concept. JAXB threw an exception.", e);
            throw new RuntimeException(e);
        } catch (UnsupportedEncodingException e) {
            log.error("There was a problem serialising a concept. The UTF8 encoding is not supported.", e);
            throw new RuntimeException(e);
        }
    }
    
    /**
     * Transforms a string representation of a concept into an {@link Concept}.
     * 
     * @param s
     * @return
     * @throws RuntimeException
     */
    public static Concept deserialiseConcept(String s) {
        if(unmarshaller == null) init();
        try {
            Object res = unmarshaller.unmarshal(new ByteArrayInputStream(s.getBytes()));
            return (Concept) res;
        } catch(JAXBException e) {
            log.error("There was a problem deserialising a concept. JAXB threw an exception.", e);
            throw new RuntimeException(e);
        }
    }
    
    public static String serialiseLiteral(Literal l) {
        if(marshaller == null) init();
        try {
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            marshaller.marshal(l, baos);
            return baos.toString("UTF8");
        } catch(JAXBException e) {
            log.error("There was a problem serialising a concept. JAXB threw an exception.", e);
            throw new RuntimeException(e);
        } catch (UnsupportedEncodingException e) {
            log.error("There was a problem serialising a concept. The UTF8 encoding is not supported.", e);
            throw new RuntimeException(e);
        }
    }
    
    /**
     * Transforms an axiom into a {@link String} representation.
     * 
     * @param axiom
     * @return
     * @throws RuntimeException
     */
    public static String serialise(Axiom axiom) {
        if(marshaller == null) init();
        try {
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            marshaller.marshal(axiom, baos);
            return baos.toString("UTF8");
        } catch(JAXBException e) {
            log.error("There was a problem serialising an axiom. JAXB threw an exception.", e);
            throw new RuntimeException(e);
        } catch (UnsupportedEncodingException e) {
            log.error("There was a problem serialising an axiom. The UTF8 encoding is not supported.", e);
            throw new RuntimeException(e);
        }
    }
    
    /**
     * Transforms a string representation of an axiom into an {@link Axiom}.
     * 
     * @param s
     * @return
     * @throws RuntimeException
     */
    public static Axiom deserialise(String s) {
        if(unmarshaller == null) init();
        try {
            Object res = unmarshaller.unmarshal(new ByteArrayInputStream(s.getBytes()));
            return (Axiom) res;
        } catch(JAXBException e) {
            log.error("There was a problem deserialising an axiom. JAXB threw an exception.", e);
            throw new RuntimeException(e);
        }
    }

}
