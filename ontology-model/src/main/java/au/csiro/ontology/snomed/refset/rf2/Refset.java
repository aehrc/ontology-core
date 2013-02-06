/**
 * Copyright CSIRO Australian e-Health Research Centre (http://aehrc.com).
 * All rights reserved. Use is subject to license terms and conditions.
 */
package au.csiro.ontology.snomed.refset.rf2;


/**
 * @author Alejandro Metke
 *
 */
public class Refset implements IRefset {
    
    protected String id;
    protected String displayName; 
    
    
    /**
     * 
     */
    public Refset(String id, String displayName) {
        this.id = id;
        this.displayName = displayName;
    }

    @Override
    public String getId() {
        return id;
    }

    @Override
    public String getDisplayName() {
        return displayName;
    }

}
