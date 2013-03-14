package au.csiro.ontology.importer.input;

import java.util.ArrayList;
import java.util.List;


/**
 * Contains a module id and a list of versions that should be imported.
 * 
 * @author Alejandro Metke
 *
 */
public class ModuleInfo {
    protected String id;
    protected List<Version> versions = new ArrayList<>();
    
    public ModuleInfo() {
        
    }
    
    public ModuleInfo(String id) {
        super();
        this.id = id;
    }

    /**
     * @return the id
     */
    public String getId() {
        return id;
    }

    /**
     * @return the versions
     */
    public List<Version> getVersions() {
        return versions;
    }

    /**
     * @param id the id to set
     */
    public void setId(String id) {
        this.id = id;
    }

    /**
     * @param versions the versions to set
     */
    public void setVersions(List<Version> versions) {
        this.versions = versions;
    }
    
}