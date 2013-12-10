package au.csiro.ontology.input;

public class OWLInput extends Input {
    protected String owlFile;

    public OWLInput() {
    }

    /**
     * @return the owlFile
     */
    public String getOwlFile() {
        return owlFile;
    }

    public void setOwlFile(String owlFile) {
        this.owlFile = owlFile;
    }
}
