package au.csiro.ontology.importer.input;

import java.util.ArrayList;
import java.util.List;

import au.csiro.ontology.importer.input.Inputs.ReleaseType;

/**
 * An RF set of input files.
 * 
 * @author Alejandro Metke
 *
 */
public class RF2Input extends Input {
    // Terminology files
    protected String conceptsFile;
    protected String descriptionsFile;
    protected String identifiersFile;
    protected String relationshipsFile;
    protected String statedRelationshipsFile;
    protected String textDefinitionsFile;
    
    // Reference set files
    protected List<String> refsetDescriptorRefsetFiles = new ArrayList<>();
    protected List<String> descriptionFormatRefsetFiles = new ArrayList<>();
    protected List<String> moduleDependenciesRefsetFiles = new ArrayList<>();
    protected List<String> languageRefsetFiles = new ArrayList<>();
    protected List<String> simpleRefsetFiles = new ArrayList<>();
    protected List<String> orderedRefsetFiles = new ArrayList<>();
    protected List<String> attributeValueRefsetFiles = new ArrayList<>();
    protected List<String> simpleMapRefsetFiles = new ArrayList<>();
    protected List<String> complexMapRefsetFiles = new ArrayList<>();
    protected List<String> querySpecificationRefsetFiles = new ArrayList<>();
    protected List<String> annotationRefsetFiles = new ArrayList<>();
    protected List<String> associationRefsetFiles = new ArrayList<>();
    
    // The type of release - full, snapshot or incremental
    protected ReleaseType releaseType;
    
    // List of module information - indicates which modules should be
    // processed and includes the meta-data needed to do so
    protected List<ModuleInfo> modules = new ArrayList<>();
    
    /**
     * Constructor.
     */
    public RF2Input() {
        
    }

    /**
     * @return the conceptsFile
     */
    public String getConceptsFile() {
        return conceptsFile;
    }

    /**
     * @param conceptsFile the conceptsFile to set
     */
    public void setConceptsFile(String conceptsFile) {
        this.conceptsFile = conceptsFile;
    }

    /**
     * @return the descriptionsFile
     */
    public String getDescriptionsFile() {
        return descriptionsFile;
    }

    /**
     * @param descriptionsFile the descriptionsFile to set
     */
    public void setDescriptionsFile(String descriptionsFile) {
        this.descriptionsFile = descriptionsFile;
    }

    /**
     * @return the identifiersFile
     */
    public String getIdentifiersFile() {
        return identifiersFile;
    }

    /**
     * @param identifiersFile the identifiersFile to set
     */
    public void setIdentifiersFile(String identifiersFile) {
        this.identifiersFile = identifiersFile;
    }

    /**
     * @return the relationshipsFile
     */
    public String getRelationshipsFile() {
        return relationshipsFile;
    }

    /**
     * @param relationshipsFile the relationshipsFile to set
     */
    public void setRelationshipsFile(String relationshipsFile) {
        this.relationshipsFile = relationshipsFile;
    }

    /**
     * @return the statedRelationshipsFile
     */
    public String getStatedRelationshipsFile() {
        return statedRelationshipsFile;
    }

    /**
     * @param statedRelationshipsFile the statedRelationshipsFile to set
     */
    public void setStatedRelationshipsFile(String statedRelationshipsFile) {
        this.statedRelationshipsFile = statedRelationshipsFile;
    }

    /**
     * @return the textDefinitionsFile
     */
    public String getTextDefinitionsFile() {
        return textDefinitionsFile;
    }

    /**
     * @param textDefinitionsFile the textDefinitionsFile to set
     */
    public void setTextDefinitionsFile(String textDefinitionsFile) {
        this.textDefinitionsFile = textDefinitionsFile;
    }

    /**
     * @return the refsetDescriptorRefsetFiles
     */
    public List<String> getRefsetDescriptorRefsetFiles() {
        return refsetDescriptorRefsetFiles;
    }

    /**
     * @param refsetDescriptorRefsetFiles the refsetDescriptorRefsetFiles to set
     */
    public void setRefsetDescriptorRefsetFiles(
            List<String> refsetDescriptorRefsetFiles) {
        this.refsetDescriptorRefsetFiles = refsetDescriptorRefsetFiles;
    }

    /**
     * @return the descriptionFormatRefsetFiles
     */
    public List<String> getDescriptionFormatRefsetFiles() {
        return descriptionFormatRefsetFiles;
    }

    /**
     * @param descriptionFormatRefsetFiles the descriptionFormatRefsetFiles to set
     */
    public void setDescriptionFormatRefsetFiles(
            List<String> descriptionFormatRefsetFiles) {
        this.descriptionFormatRefsetFiles = descriptionFormatRefsetFiles;
    }

    /**
     * @return the moduleDependenciesRefsetFiles
     */
    public List<String> getModuleDependenciesRefsetFiles() {
        return moduleDependenciesRefsetFiles;
    }

    /**
     * @param moduleDependenciesRefsetFiles the moduleDependenciesRefsetFiles to set
     */
    public void setModuleDependenciesRefsetFiles(
            List<String> moduleDependenciesRefsetFiles) {
        this.moduleDependenciesRefsetFiles = moduleDependenciesRefsetFiles;
    }

    /**
     * @return the languageRefsetFiles
     */
    public List<String> getLanguageRefsetFiles() {
        return languageRefsetFiles;
    }

    /**
     * @param languageRefsetFiles the languageRefsetFiles to set
     */
    public void setLanguageRefsetFiles(List<String> languageRefsetFiles) {
        this.languageRefsetFiles = languageRefsetFiles;
    }

    /**
     * @return the simpleRefsetFiles
     */
    public List<String> getSimpleRefsetFiles() {
        return simpleRefsetFiles;
    }

    /**
     * @param simpleRefsetFiles the simpleRefsetFiles to set
     */
    public void setSimpleRefsetFiles(List<String> simpleRefsetFiles) {
        this.simpleRefsetFiles = simpleRefsetFiles;
    }

    /**
     * @return the orderedRefsetFiles
     */
    public List<String> getOrderedRefsetFiles() {
        return orderedRefsetFiles;
    }

    /**
     * @param orderedRefsetFiles the orderedRefsetFiles to set
     */
    public void setOrderedRefsetFiles(List<String> orderedRefsetFiles) {
        this.orderedRefsetFiles = orderedRefsetFiles;
    }

    /**
     * @return the attributeValueRefsetFiles
     */
    public List<String> getAttributeValueRefsetFiles() {
        return attributeValueRefsetFiles;
    }

    /**
     * @param attributeValueRefsetFiles the attributeValueRefsetFiles to set
     */
    public void setAttributeValueRefsetFiles(List<String> attributeValueRefsetFiles) {
        this.attributeValueRefsetFiles = attributeValueRefsetFiles;
    }

    /**
     * @return the simpleMapRefsetFiles
     */
    public List<String> getSimpleMapRefsetFiles() {
        return simpleMapRefsetFiles;
    }

    /**
     * @param simpleMapRefsetFiles the simpleMapRefsetFiles to set
     */
    public void setSimpleMapRefsetFiles(List<String> simpleMapRefsetFiles) {
        this.simpleMapRefsetFiles = simpleMapRefsetFiles;
    }

    /**
     * @return the complexMapRefsetFiles
     */
    public List<String> getComplexMapRefsetFiles() {
        return complexMapRefsetFiles;
    }

    /**
     * @param complexMapRefsetFiles the complexMapRefsetFiles to set
     */
    public void setComplexMapRefsetFiles(List<String> complexMapRefsetFiles) {
        this.complexMapRefsetFiles = complexMapRefsetFiles;
    }

    /**
     * @return the querySpecificationRefsetFiles
     */
    public List<String> getQuerySpecificationRefsetFiles() {
        return querySpecificationRefsetFiles;
    }

    /**
     * @param querySpecificationRefsetFiles the querySpecificationRefsetFiles to set
     */
    public void setQuerySpecificationRefsetFiles(
            List<String> querySpecificationRefsetFiles) {
        this.querySpecificationRefsetFiles = querySpecificationRefsetFiles;
    }

    /**
     * @return the annotationRefsetFiles
     */
    public List<String> getAnnotationRefsetFiles() {
        return annotationRefsetFiles;
    }

    /**
     * @param annotationRefsetFiles the annotationRefsetFiles to set
     */
    public void setAnnotationRefsetFiles(List<String> annotationRefsetFiles) {
        this.annotationRefsetFiles = annotationRefsetFiles;
    }

    /**
     * @return the associationRefsetFiles
     */
    public List<String> getAssociationRefsetFiles() {
        return associationRefsetFiles;
    }

    /**
     * @param associationRefsetFiles the associationRefsetFiles to set
     */
    public void setAssociationRefsetFiles(List<String> associationRefsetFiles) {
        this.associationRefsetFiles = associationRefsetFiles;
    }

    /**
     * @return the releaseType
     */
    public ReleaseType getReleaseType() {
        return releaseType;
    }

    /**
     * @param releaseType the releaseType to set
     */
    public void setReleaseType(ReleaseType releaseType) {
        this.releaseType = releaseType;
    }

    /**
     * @return the modules
     */
    public List<ModuleInfo> getModules() {
        return modules;
    }

    /**
     * @param modules the modules to set
     */
    public void setModules(List<ModuleInfo> modules) {
        this.modules = modules;
    }
    
}