package au.csiro.ontology.input;

public class OWLInput extends Input {
    protected String owlFile;

    private String owlReasonerFactory = "au.csiro.snorocket.owlapi.SnorocketReasonerFactory";

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

    protected String getOwlReasonerFactory() {
        return owlReasonerFactory;
    }

    protected void setOwlReasonerFactory(String owlReasonerFactory) {
        this.owlReasonerFactory = owlReasonerFactory;
    }

}
