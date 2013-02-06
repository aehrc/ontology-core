/**
 * Copyright CSIRO Australian e-Health Research Centre (http://aehrc.com).
 * All rights reserved. Use is subject to license terms and conditions.
 */
package au.csiro.ontology.snomed.refset.rf2;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

import au.csiro.ontology.util.SnomedMetadata;


/**
 * @author Alejandro Metke
 *
 */
public class LanguageRefset extends Refset implements ILanguageRefset {
    
    protected Set<String> prefSet = new HashSet<>();
    protected SnomedMetadata metadata = SnomedMetadata.INSTANCE;
    
    public LanguageRefset(String id, String displayName, 
            Collection<IRefsetMember> members) {
        super(id, displayName);
        
        String prefId = metadata.getPreferredId();
        
        for(IRefsetMember member : members) {
            ILanguageRefsetMember langMember = (ILanguageRefsetMember)member;
            String ref = langMember.getReferencedComponentId();
            if(prefId.equals(langMember.getAccceptabilityId()) && 
                    langMember.isActive()) {
                prefSet.add(ref);
            }
        }
    }

    @Override
    public boolean isPreferred(String descId) {
        return prefSet.contains(descId);
    }

}
