package au.csiro.ontology.importer.input;


/**
 * Base class for inputs.
 * 
 * @author Alejandro Metke
 *
 */
public abstract class Input {
    
    /**
     * Indicates if the files should be loaded from an external file system or
     * the class path.
     * 
     * @author Alejandro Metke
     *
     */
    public enum InputType {
        EXTERNAL, CLASSPATH
    };
    
    protected InputType inputType;

    /**
     * @return the type
     */
    public InputType getInputType() {
        return inputType;
    }

    /**
     * @param type the type to set
     */
    public void setInputType(InputType inputType) {
        this.inputType = inputType;
    }

}