/**
 * Copyright CSIRO Australian e-Health Research Centre (http://aehrc.com).
 * All rights reserved. Use is subject to license terms and conditions.
 */
package au.csiro.ontology.snomed.model;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import au.csiro.ontology.model.AbstractInfo;

/**
 * This class holds additional information for ontologies imported from SNOMED
 * RF1 and RF2 files.
 * 
 * @author Alejandro Metke
 * 
 */
public class SnomedInfo extends AbstractInfo {
    
    protected String module;
    
    protected Date effectiveTime;

    protected boolean active;

    protected boolean primitive;

    protected String fullySpecifiedName;

    protected String preferredTerm;

    protected Set<String> synonyms = new HashSet<>();

    @Override
    public String getLabel() {
        return fullySpecifiedName;
    }

    /**
     * @param module the module to set
     */
    public void setModule(String module) {
        this.module = module;
    }

    /**
     * @return the effectiveTime
     */
    public Date getEffectiveTime() {
        return effectiveTime;
    }

    /**
     * @param effectiveTime
     *            the effectiveTime to set
     */
    public void setEffectiveTime(Date effectiveTime) {
        this.effectiveTime = effectiveTime;
    }

    /**
     * @return the active
     */
    public boolean isActive() {
        return active;
    }

    /**
     * @param active
     *            the active to set
     */
    public void setActive(boolean active) {
        this.active = active;
    }

    /**
     * @return the primitive
     */
    public boolean isPrimitive() {
        return primitive;
    }

    /**
     * @param primitive
     *            the primitive to set
     */
    public void setPrimitive(boolean primitive) {
        this.primitive = primitive;
    }

    /**
     * @return the fullySpecifiedName
     */
    public String getFullySpecifiedName() {
        return fullySpecifiedName;
    }

    /**
     * @param fullySpecifiedName
     *            the fullySpecifiedName to set
     */
    public void setFullySpecifiedName(String fullySpecifiedName) {
        this.fullySpecifiedName = fullySpecifiedName;
    }

    /**
     * @return the preferredTerm
     */
    public String getPreferredTerm() {
        return preferredTerm;
    }

    /**
     * @param preferredTerm
     *            the preferredTerm to set
     */
    public void setPreferredTerm(String preferredTerm) {
        this.preferredTerm = preferredTerm;
    }

    /**
     * @return the synonyms
     */
    public Set<String> getSynonyms() {
        return synonyms;
    }
    
    
    /**
     * @return the module
     */
    public String getModule() {
        return module;
    }

}
