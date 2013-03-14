package au.csiro.ontology.importer.input;

import java.util.HashMap;
import java.util.Map;

/**
 * Contains the id of a version and the meta-data associated with it.
 * 
 * @author Alejandro Metke
 *
 */
public class Version {
    protected String id;
    protected Map<String, String> metadata = new HashMap<>();
    
    public Version() {
        
    }
    
    public Version(String id) {
        super();
        this.id = id;
    }

    /**
     * @return the name
     */
    public String getId() {
        return id;
    }

    /**
     * @return the values
     */
    public Map<String, String> getMetadata() {
        return metadata;
    }

    /**
     * @param id the id to set
     */
    public void setId(String id) {
        this.id = id;
    }

    /**
     * @param metadata the metadata to set
     */
    public void setMetadata(Map<String, String> metadata) {
        this.metadata = metadata;
    }
    
}