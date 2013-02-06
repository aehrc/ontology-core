/**
 * Copyright CSIRO Australian e-Health Research Centre (http://aehrc.com).
 * All rights reserved. Use is subject to license terms and conditions.
 */
package au.csiro.ontology.snomed.refset.rf2;

/**
 * @author Alejandro Metke
 *
 */
public class LanguageRefsetMember extends RefsetMember implements ILanguageRefsetMember {

    protected String acceptabilityId;

    public LanguageRefsetMember(String id, String effectiveTime,
            boolean active, String moduleId, String refsetId,
            String referencedComponentId, String acceptabilityId) {
        super(id, effectiveTime, active, moduleId, refsetId, referencedComponentId);
        this.acceptabilityId = acceptabilityId;
    }

    @Override
    public String getAccceptabilityId() {
        return acceptabilityId;
    }

}
