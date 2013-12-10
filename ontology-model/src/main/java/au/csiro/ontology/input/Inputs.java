/**
 * Copyright CSIRO Australian e-Health Research Centre (http://aehrc.com).
 * All rights reserved. Use is subject to license terms and conditions.
 */
package au.csiro.ontology.input;

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
     * List of input files.
     */
    protected List<Input> inputs = new ArrayList<Input>();

    /**
     * Loads an {@link Inputs} object from am XML file.
     *
     * @param file
     * @return
     * @throws JAXBException
     */
    public static Inputs load(InputStream in) throws JAXBException {
        JAXBContext context = JAXBContext.newInstance(Inputs.class, RF2Input.class, OWLInput.class);
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
     * @return the inputs
     */
    public List<Input> getInputs() {
        return inputs;
    }

    /**
     * @param inputs the inputs to set
     */
    public void setInputs(List<Input> inputs) {
        this.inputs = inputs;
    }

}
