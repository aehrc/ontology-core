/**
 * Copyright CSIRO Australian e-Health Research Centre (http://aehrc.com).
 * All rights reserved. Use is subject to license terms and conditions.
 */
package au.csiro.ontology.importer.input;

import java.io.StringWriter;
import java.util.HashMap;
import java.util.Map;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.Marshaller;

import au.csiro.ontology.importer.input.Input.InputType;
import au.csiro.ontology.importer.input.Inputs.ReleaseType;

/**
 * Utility class to create XML input file programmatically.
 * 
 * @author Alejandro Metke
 *
 */
public class InputsUtil {

    /**
     * @param args
     */
    public static void main(String[] args) {
        Inputs in = new Inputs();
        
        // Input for SNOMED international
        RF2Input in1 = new RF2Input();
        in.getRf2Inputs().add(in1);
        
        in1.setInputType(InputType.CLASSPATH);
        in1.setConceptsFile("/snomed_int_full_rf2/Terminology/sct2_Concept_Full_INT_20120131.txt");
        in1.setDescriptionsFile("/snomed_int_full_rf2/Terminology/sct2_Description_Full-en_INT_20120131.txt");
        in1.setRelationshipsFile("/snomed_int_full_rf2/Terminology/sct2_Relationship_Full_INT_20120131.txt");
        in1.setStatedRelationshipsFile("/snomed_int_full_rf2/Terminology/sct2_StatedRelationship_Full_INT_20120131.txt");
        in1.setTextDefinitionsFile("/snomed_int_full_rf2/Terminology/sct2_TextDefinition_Full_INT_20120131.txt");
        in1.setIdentifiersFile("/snomed_int_full_rf2/Terminology/sct2_Identifier_Full_INT_20120131.txt");
        
        in1.getRefsetDescriptorRefsetFiles().add("/snomed_int_full_rf2/Refset/Metadata/der2_cciRefset_RefsetDescriptorFull_INT_20120131.txt");
        in1.getDescriptionFormatRefsetFiles().add("/snomed_int_full_rf2/Refset/Metadata/der2_ciRefset_DescriptionTypeFull_INT_20120131.txt");
        in1.getModuleDependenciesRefsetFiles().add("/snomed_int_full_rf2/Refset/Metadata/der2_ssRefset_ModuleDependencyFull_INT_20120131.txt");
        in1.getLanguageRefsetFiles().add("/snomed_int_full_rf2/Refset/Language/der2_cRefset_LanguageFull-en_INT_20120131.txt");
        in1.getSimpleRefsetFiles().add("/snomed_int_full_rf2/Refset/Content/der2_Refset_SimpleFull_INT_20120131.txt");
        in1.getAttributeValueRefsetFiles().add("/snomed_int_full_rf2/Refset/Content/der2_cRefset_AttributeValueFull_INT_20120131.txt");
        in1.getAssociationRefsetFiles().add("/snomed_int_full_rf2/Refset/Content/der2_cRefset_AssociationReferenceFull_INT_20120131.txt");
        in1.getSimpleMapRefsetFiles().add("/snomed_int_full_rf2/Refset/CrossMap/der2_sRefset_SimpleMapFull_INT_20120131.txt");
        in1.getComplexMapRefsetFiles().add("/snomed_int_full_rf2/Refset/CrossMap/der2_iissscRefset_ComplexMapFull_INT_20120131.txt");
        
        in1.setReleaseType(ReleaseType.FULL);
        
        Map<String, String> md = new HashMap<>();
        md.put("conceptModelAttId", "410662002");
        md.put("isAId", "116680003");
        md.put("coreModuleId", "900000000000207008");
        md.put("metadataModuleId", "900000000000012004");
        md.put("conceptDefinedId", "900000000000073002");
        md.put("someId", "900000000000451002");
        md.put("allId", "900000000000452009");
        md.put("fsnId", "900000000000003001");
        md.put("synonymId", "900000000000013009");
        md.put("definitionId", "900000000000550004");
        md.put("neverGroupedIds", "123005000,127489000,272741003,411116001");
        md.put("rightIdentityIds", "363701004,127489000");
        md.put("roleGroupId", "roleGroup");
        
        ModuleInfo m900000000000207008 = new ModuleInfo("900000000000207008");
        
        Version v20120131 = new Version("20120131");
        v20120131.getMetadata().putAll(md);
        
        Version v20110731 = new Version("20110731");
        v20110731.getMetadata().putAll(md);
        
        m900000000000207008.getVersions().add(v20120131);
        m900000000000207008.getVersions().add(v20110731);
        
        in1.getModules().add(m900000000000207008);
        
        // Input for UK release
        RF2Input in2 = new RF2Input();
        in.getRf2Inputs().add(in2);
        
        in2.setInputType(InputType.CLASSPATH);
        in2.setConceptsFile("/SnomedCT_GB1000000_20121001/RF2Release/Full/Terminology/xsct2_Concept_Full_GB1000000_20121001.txt");
        in2.setDescriptionsFile("/SnomedCT_GB1000000_20121001/RF2Release/Full/Terminology/xsct2_Description_Full-en-GB_GB1000000_20121001.txt");
        in2.setRelationshipsFile("/SnomedCT_GB1000000_20121001/RF2Release/Full/Terminology/xsct2_Relationship_Full_GB1000000_20121001.txt");
        in2.setStatedRelationshipsFile("/SnomedCT_GB1000000_20121001/RF2Release/Full/Terminology/xsct2_StatedRelationship_Full_GB1000000_20121001.txt");
        in2.setTextDefinitionsFile("/SnomedCT_GB1000000_20121001/RF2Release/Full/Terminology/xsct2_TextDefinition_Full_GB1000000_20121001.txt");
        in2.setIdentifiersFile("/SnomedCT_GB1000000_20121001/RF2Release/Full/Terminology/xsct2_Identifier_Full_GB1000000_20121001.txt");
        
        // No refset descriptor in UK release!
        // Meta-data language file present in UK release but not in international release. What is it? Is it important?
        in2.getModuleDependenciesRefsetFiles().add("/SnomedCT_GB1000000_20121001/RF2Release/Full/Refset/Metadata/xder2_ssRefset_ModuleDependencyFull_GB1000000_20121001.txt");
        in2.getLanguageRefsetFiles().add("/SnomedCT_GB1000000_20121001/RF2Release/Full/Refset/Language/xder2_cRefset_LanguageFull-en-GB_GB1000000_20121001.txt");
        in2.getLanguageRefsetFiles().add("/SnomedCT_GB1000000_20121001/RF2Release/Full/Refset/Language/xder2_cRefset_UKExtensionLanguageFull-en-GB_GB1000000_20121001.txt");
        
        in2.getSimpleRefsetFiles().add("/SnomedCT_GB1000000_20121001/RF2Release/Full/Refset/Content/CarePlanning/xder2_Refset_CarePlanningSimpleFull_GB1000000_20121001.txt");
        in2.getSimpleRefsetFiles().add("/SnomedCT_GB1000000_20121001/RF2Release/Full/Refset/Content/CareRecordElement/xder2_Refset_CareRecordElementSimpleFull_GB1000000_20121001.txt");
        in2.getSimpleRefsetFiles().add("/SnomedCT_GB1000000_20121001/RF2Release/Full/Refset/Content/ClinicalMessaging/xder2_Refset_ClinicalMessagingSimpleFull_GB1000000_20121001.txt");
        in2.getSimpleRefsetFiles().add("/SnomedCT_GB1000000_20121001/RF2Release/Full/Refset/Content/DiagnosticImagingProcedure/xder2_Refset_DiagnosticImagingProcedureSimpleFull_GB1000000_20121001.txt");
        in2.getSimpleRefsetFiles().add("/SnomedCT_GB1000000_20121001/RF2Release/Full/Refset/Content/Endoscopy/xder2_Refset_EndoscopySimpleFull_GB1000000_20121001.txt");
        in2.getSimpleRefsetFiles().add("/SnomedCT_GB1000000_20121001/RF2Release/Full/Refset/Content/LinkAssertion/xder2_Refset_LinkAssertionSimpleFull_GB1000000_20121001.txt");
        in2.getSimpleRefsetFiles().add("/SnomedCT_GB1000000_20121001/RF2Release/Full/Refset/Content/NHSRealmDescription/xder2_cRefset_NHSRealmDescriptionLanguageFull_GB1000000_20121001.txt");
        in2.getSimpleRefsetFiles().add("/SnomedCT_GB1000000_20121001/RF2Release/Full/Refset/Content/OccupationalTherapy/xder2_Refset_OccupationalTherapySimpleFull_GB1000000_20121001.txt");
        in2.getSimpleRefsetFiles().add("/SnomedCT_GB1000000_20121001/RF2Release/Full/Refset/Content/PathologyBoundedCodeList/xder2_cRefset_PathologyBoundedCodeListLanguageFull_GB1000000_20121001.txt");
        in2.getSimpleRefsetFiles().add("/SnomedCT_GB1000000_20121001/RF2Release/Full/Refset/Content/PathologyBoundedCodeList/xder2_Refset_PathologyBoundedCodeListSimpleFull_GB1000000_20121001.txt");
        in2.getSimpleRefsetFiles().add("/SnomedCT_GB1000000_20121001/RF2Release/Full/Refset/Content/PathologyCatalogue/xder2_Refset_PathologyCatalogueSimpleFull_GB1000000_20121001.txt");
        in2.getSimpleRefsetFiles().add("/SnomedCT_GB1000000_20121001/RF2Release/Full/Refset/Content/PublicHealthLanguage/xder2_Refset_PublicHealthLanguageSimpleFull_GB1000000_20121001.txt");
        in2.getSimpleRefsetFiles().add("/SnomedCT_GB1000000_20121001/RF2Release/Full/Refset/Content/Renal/xder2_Refset_RenalSimpleFull_GB1000000_20121001.txt");
        in2.getSimpleRefsetFiles().add("/SnomedCT_GB1000000_20121001/RF2Release/Full/Refset/Content/SSERP/xder2_Refset_SSERPSimpleFull_GB1000000_20121001.txt");
        in2.getSimpleRefsetFiles().add("/SnomedCT_GB1000000_20121001/RF2Release/Full/Refset/Content/StandardsConsultingGroup/Religions/xder2_cRefset_ReligionsLanguageFull_GB1000000_20121001.txt");
        in2.getSimpleRefsetFiles().add("/SnomedCT_GB1000000_20121001/RF2Release/Full/Refset/Content/StandardsConsultingGroup/Religions/xder2_Refset_ReligionsSimpleFull_GB1000000_20121001.txt");
        
        in2.getAttributeValueRefsetFiles().add("/SnomedCT_GB1000000_20121001/RF2Release/Full/Refset/Content/xder2_cRefset_AssociationReferenceFull_GB1000000_20121001.txt");
        in2.getAssociationRefsetFiles().add("/SnomedCT_GB1000000_20121001/RF2Release/Full/Refset/Content/xder2_cRefset_AssociationReferenceFull_GB1000000_20121001.txt");
        
        in2.getComplexMapRefsetFiles().add("/SnomedCT_GB1000000_20121001/RF2Release/Full/Refset/CrossMap/xder2_iissscRefset_ICD10FourthEditionComplexMapFull_GB1000000_20121001.txt");
        in2.getComplexMapRefsetFiles().add("/SnomedCT_GB1000000_20121001/RF2Release/Full/Refset/CrossMap/Crossmap/xder2_iissscRefset_OPCS46ComplexMapFull_GB1000000_20121001.txt");
        
        in2.setReleaseType(ReleaseType.FULL);
        
        // SNOMED CT UK Edition reference set module
        ModuleInfo m999000031000000106 = new ModuleInfo("999000031000000106");
        Version v20121001 = new Version("20121001");
        v20121001.getMetadata().putAll(md);
        m999000031000000106.getVersions().add(v20120131);
        
        // SNOMED CT UK clinical extension reference set module
        ModuleInfo m999000021000000109 = new ModuleInfo("999000021000000109");
        v20121001 = new Version("20121001");
        v20121001.getMetadata().putAll(md);
        m999000021000000109.getVersions().add(v20120131);
        
        // SNOMED CT UK drug extension reference set module
        ModuleInfo m999000021000001108 = new ModuleInfo("999000021000001108");
        v20121001 = new Version("20121001");
        v20121001.getMetadata().putAll(md);
        m999000021000001108.getVersions().add(v20120131);
        
        in2.getModules().add(m999000031000000106);
        in2.getModules().add(m999000021000000109);
        in2.getModules().add(m999000021000001108);
        
        try {
            StringWriter writer = new StringWriter();
            JAXBContext context = JAXBContext.newInstance(Inputs.class);
            Marshaller m = context.createMarshaller();
            m.marshal(in, writer);
    
            System.out.println(writer);
        } catch(Exception e) {
            e.printStackTrace();
        }
    }

}
