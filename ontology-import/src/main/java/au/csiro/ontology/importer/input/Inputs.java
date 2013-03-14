/**
 * Copyright CSIRO Australian e-Health Research Centre (http://aehrc.com).
 * All rights reserved. Use is subject to license terms and conditions.
 */
package au.csiro.ontology.importer.input;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * This class is able to process a configuration file that specifies a set of
 * input ontologies potentially in different formats.
 * 
 * @author Alejandro Metke
 *
 */
@XmlRootElement
public class Inputs {
    
    /**
     * Indicates the type of SNOMED release.
     * 
     * @author Alejandro Metke
     * 
     */
    public enum ReleaseType {
        FULL, SNAPSHOT, INCREMENTAL
    };
    
    /**
     * List of RF2 input files.
     */
    protected List<RF2Input> rf2Inputs = new ArrayList<>();
    
    /**
     * List of OWL input files.
     */
    protected List<OWLInput> owlInputs = new ArrayList<>();
    
    /**
     * Loads an {@link Inputs} object from am XML file.
     * 
     * @param file
     * @return
     * @throws JAXBException 
     */
    public static Inputs load(InputStream in) throws JAXBException {
        JAXBContext context = JAXBContext.newInstance(Inputs.class);
        Unmarshaller u = context.createUnmarshaller();
        Inputs inputs = (Inputs) u.unmarshal(in);
        return inputs;
    }
    
    /**
     * Constructor.
     * 
     * @param configFile
     */
    public Inputs() {
        
    }

    /**
     * @return the rf2Inputs
     */
    public List<RF2Input> getRf2Inputs() {
        return rf2Inputs;
    }

    /**
     * @param rf2Inputs the rf2Inputs to set
     */
    public void setRf2Inputs(List<RF2Input> rf2Inputs) {
        this.rf2Inputs = rf2Inputs;
    }

    /**
     * @return the owlInputs
     */
    public List<OWLInput> getOwlInputs() {
        return owlInputs;
    }

    /**
     * @param owlInputs the owlInputs to set
     */
    public void setOwlInputs(List<OWLInput> owlInputs) {
        this.owlInputs = owlInputs;
    }
    
}
