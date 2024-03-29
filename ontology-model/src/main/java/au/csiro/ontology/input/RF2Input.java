package au.csiro.ontology.input;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import au.csiro.ontology.input.Inputs.ReleaseType;

/**
 * An set of RF2 input files.
 *
 * @author Alejandro Metke
 *
 */
public class RF2Input extends Input {
    // Terminology files
    protected Set<String> conceptsFiles = new HashSet<>();
    protected Set<String> descriptionsFiles = new HashSet<>();
    protected Set<String> identifiersFiles = new HashSet<>();
    protected Set<String> relationshipsFiles = new HashSet<>();         // deprecated
    protected Set<String> statedRelationshipsFiles = new HashSet<>();
    protected Set<String> nnfRelationshipsFiles = new HashSet<>();
    protected Set<String> nnfConcreteDomainsFiles = new HashSet<>();
    protected Set<String> textDefinitionsFiles = new HashSet<>();
    protected Set<String> owlOntologyRefsetFiles = new HashSet<>();     // deprecated
    protected Set<String> owlAxiomRefsetFiles = new HashSet<>();        // deprecated
    protected Set<String> owlExpressionRefsetFiles = new HashSet<>();

    // Reference set files
    protected Set<String> refsetDescriptorRefsetFiles = new HashSet<>();
    protected Set<String> descriptionFormatRefsetFiles = new HashSet<>();
    protected Set<String> moduleDependenciesRefsetFiles = new HashSet<>();
    protected Set<String> languageRefsetFiles = new HashSet<>();
    protected Set<String> simpleRefsetFiles = new HashSet<>();
    protected Set<String> orderedRefsetFiles = new HashSet<>();
    protected Set<String> attributeValueRefsetFiles = new HashSet<>();
    protected Set<String> simpleMapRefsetFiles = new HashSet<>();
    protected Set<String> complexMapRefsetFiles = new HashSet<>();
    protected Set<String> querySpecificationRefsetFiles = new HashSet<>();
    protected Set<String> annotationRefsetFiles = new HashSet<>();
    protected Set<String> associationRefsetFiles = new HashSet<>();
    protected Set<String> concreteDomainRefsetFiles = new HashSet<>();
    protected Set<String> attributeDomainRefsetFiles = new HashSet<>();

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

    @Override
    public void validate() {
        super.validate();

//        if (!relationshipsFiles.isEmpty()) {
//            throw new RuntimeException("relationshipsFiles is deprecated.");
//        }
        if (!owlOntologyRefsetFiles.isEmpty()) {
            owlExpressionRefsetFiles.addAll(owlOntologyRefsetFiles);
            owlOntologyRefsetFiles.clear();
        }
        if (!owlAxiomRefsetFiles.isEmpty()) {
            owlExpressionRefsetFiles.addAll(owlAxiomRefsetFiles);
            owlAxiomRefsetFiles.clear();
        }
        if (statedRelationshipsFiles.isEmpty() && owlExpressionRefsetFiles.isEmpty() && owlAxiomRefsetFiles.isEmpty() && owlOntologyRefsetFiles.isEmpty()) {
            throw new RuntimeException("No stated form of any kind declared.");
        }

        if (null == releaseType) {
            throw new RuntimeException("No releaseType.");
        }
        if (modules.size() < 1) {
            throw new RuntimeException("No modules.");
        }
    }

    /**
     * @return the conceptsFiles
     */
    public Set<String> getConceptsFiles() {
        return conceptsFiles;
    }

    /**
     * @param conceptsFiles the conceptsFiles to set
     */
    public void setConceptsFiles(Set<String> conceptsFiles) {
        this.conceptsFiles = conceptsFiles;
    }

    /**
     * @return the descriptionsFiles
     */
    public Set<String> getDescriptionsFiles() {
        return descriptionsFiles;
    }

    /**
     * @param descriptionsFiles the descriptionsFiles to set
     */
    public void setDescriptionsFiles(Set<String> descriptionsFiles) {
        this.descriptionsFiles = descriptionsFiles;
    }

    /**
     * @return the identifiersFiles
     */
    public Set<String> getIdentifiersFiles() {
        return identifiersFiles;
    }

    /**
     * @param identifiersFiles the identifiersFiles to set
     */
    public void setIdentifiersFiles(Set<String> identifiersFiles) {
        this.identifiersFiles = identifiersFiles;
    }

    /**
     * @return the relationshipsFiles
     */
    public Set<String> getNnfRelationshipsFiles() {
        return nnfRelationshipsFiles;
    }

    /**
     * @param relationshipsFiles the relationshipsFiles to set
     */
    public void setNnfRelationshipsFiles(Set<String> relationshipsFiles) {
        this.nnfRelationshipsFiles = relationshipsFiles;
    }

    /**
     * @return the concreteDomainsFiles
     */
    public Set<String> getNnfConcreteDomainsFiles() {
        return nnfConcreteDomainsFiles;
    }

    /**
     * @param relationshipsFiles the relationshipsFiles to set
     */
    public void setNnfConcreteDomainsFiles(Set<String> concreteDomainsFiles) {
        this.nnfConcreteDomainsFiles = concreteDomainsFiles;
    }

    /**
     * @return the statedRelationshipsFiles
     */
    public Set<String> getStatedRelationshipsFiles() {
        return statedRelationshipsFiles;
    }

    /**
     * @param statedRelationshipsFiles the statedRelationshipsFiles to set
     */
    public void setStatedRelationshipsFiles(
            Set<String> statedRelationshipsFiles) {
        this.statedRelationshipsFiles = statedRelationshipsFiles;
    }

    /**
     * @return the textDefinitionsFiles
     */
    public Set<String> getTextDefinitionsFiles() {
        return textDefinitionsFiles;
    }

    /**
     * @param textDefinitionsFiles the textDefinitionsFiles to set
     */
    public void setTextDefinitionsFile(Set<String> textDefinitionsFiles) {
        this.textDefinitionsFiles = textDefinitionsFiles;
    }

    public Set<String> getOwlExpressionRefsetFiles() {
        return owlExpressionRefsetFiles;
    }

    public void setOwlExpressionRefsetFiles(Set<String> owlExpressionRefsetFiles) {
        this.owlExpressionRefsetFiles = owlExpressionRefsetFiles;
    }

    /**
     * @return the refsetDescriptorRefsetFiles
     */
    public Set<String> getRefsetDescriptorRefsetFiles() {
        return refsetDescriptorRefsetFiles;
    }

    /**
     * @param refsetDescriptorRefsetFiles the refsetDescriptorRefsetFiles to set
     */
    public void setRefsetDescriptorRefsetFiles(
            Set<String> refsetDescriptorRefsetFiles) {
        this.refsetDescriptorRefsetFiles = refsetDescriptorRefsetFiles;
    }

    /**
     * @return the descriptionFormatRefsetFiles
     */
    public Set<String> getDescriptionFormatRefsetFiles() {
        return descriptionFormatRefsetFiles;
    }

    /**
     * @param descriptionFormatRefsetFiles the descriptionFormatRefsetFiles to set
     */
    public void setDescriptionFormatRefsetFiles(
            Set<String> descriptionFormatRefsetFiles) {
        this.descriptionFormatRefsetFiles = descriptionFormatRefsetFiles;
    }

    /**
     * @return the moduleDependenciesRefsetFiles
     */
    public Set<String> getModuleDependenciesRefsetFiles() {
        return moduleDependenciesRefsetFiles;
    }

    /**
     * @param moduleDependenciesRefsetFiles the moduleDependenciesRefsetFiles to set
     */
    public void setModuleDependenciesRefsetFiles(
            Set<String> moduleDependenciesRefsetFiles) {
        this.moduleDependenciesRefsetFiles = moduleDependenciesRefsetFiles;
    }

    /**
     * @return the languageRefsetFiles
     */
    public Set<String> getLanguageRefsetFiles() {
        return languageRefsetFiles;
    }

    /**
     * @param languageRefsetFiles the languageRefsetFiles to set
     */
    public void setLanguageRefsetFiles(Set<String> languageRefsetFiles) {
        this.languageRefsetFiles = languageRefsetFiles;
    }

    /**
     * @return the simpleRefsetFiles
     */
    public Set<String> getSimpleRefsetFiles() {
        return simpleRefsetFiles;
    }

    /**
     * @param simpleRefsetFiles the simpleRefsetFiles to set
     */
    public void setSimpleRefsetFiles(Set<String> simpleRefsetFiles) {
        this.simpleRefsetFiles = simpleRefsetFiles;
    }

    /**
     * @return the orderedRefsetFiles
     */
    public Set<String> getOrderedRefsetFiles() {
        return orderedRefsetFiles;
    }

    /**
     * @param orderedRefsetFiles the orderedRefsetFiles to set
     */
    public void setOrderedRefsetFiles(Set<String> orderedRefsetFiles) {
        this.orderedRefsetFiles = orderedRefsetFiles;
    }

    /**
     * @return the attributeValueRefsetFiles
     */
    public Set<String> getAttributeValueRefsetFiles() {
        return attributeValueRefsetFiles;
    }

    /**
     * @param attributeValueRefsetFiles the attributeValueRefsetFiles to set
     */
    public void setAttributeValueRefsetFiles(Set<String> attributeValueRefsetFiles) {
        this.attributeValueRefsetFiles = attributeValueRefsetFiles;
    }

    /**
     * @return the simpleMapRefsetFiles
     */
    public Set<String> getSimpleMapRefsetFiles() {
        return simpleMapRefsetFiles;
    }

    /**
     * @param simpleMapRefsetFiles the simpleMapRefsetFiles to set
     */
    public void setSimpleMapRefsetFiles(Set<String> simpleMapRefsetFiles) {
        this.simpleMapRefsetFiles = simpleMapRefsetFiles;
    }

    /**
     * @return the complexMapRefsetFiles
     */
    public Set<String> getComplexMapRefsetFiles() {
        return complexMapRefsetFiles;
    }

    /**
     * @param complexMapRefsetFiles the complexMapRefsetFiles to set
     */
    public void setComplexMapRefsetFiles(Set<String> complexMapRefsetFiles) {
        this.complexMapRefsetFiles = complexMapRefsetFiles;
    }

    /**
     * @return the querySpecificationRefsetFiles
     */
    public Set<String> getQuerySpecificationRefsetFiles() {
        return querySpecificationRefsetFiles;
    }

    /**
     * @param querySpecificationRefsetFiles the querySpecificationRefsetFiles to set
     */
    public void setQuerySpecificationRefsetFiles(
            Set<String> querySpecificationRefsetFiles) {
        this.querySpecificationRefsetFiles = querySpecificationRefsetFiles;
    }

    /**
     * @return the annotationRefsetFiles
     */
    public Set<String> getAnnotationRefsetFiles() {
        return annotationRefsetFiles;
    }

    /**
     * @param annotationRefsetFiles the annotationRefsetFiles to set
     */
    public void setAnnotationRefsetFiles(Set<String> annotationRefsetFiles) {
        this.annotationRefsetFiles = annotationRefsetFiles;
    }

    /**
     * @return the associationRefsetFiles
     */
    public Set<String> getAssociationRefsetFiles() {
        return associationRefsetFiles;
    }

    /**
     * @param associationRefsetFiles the associationRefsetFiles to set
     */
    public void setAssociationRefsetFiles(Set<String> associationRefsetFiles) {
        this.associationRefsetFiles = associationRefsetFiles;
    }

    /**
     * @return the concreteDomainRefsetFiles
     */
    public Set<String> getConcreteDomainRefsetFiles() {
        return concreteDomainRefsetFiles;
    }

    /**
     * @param concreteDomainRefsetFiles the concreteDomainRefsetFiles to set
     */
    public void setConcreteDomainRefsetFiles(Set<String> concreteDomainRefsetFiles) {
        this.concreteDomainRefsetFiles = concreteDomainRefsetFiles;
    }

    /**
     * @return the attributeDomainRefsetFiles
     */
    public Set<String> getAttributeDomainRefsetFiles() {
        return attributeDomainRefsetFiles;
    }

    /**
     * @param attributeDomainRefsetFiles the attributeDomainRefsetFiles to set
     */
    public void setAttributeDomainRefsetFiles(Set<String> attributeDomainRefsetFiles) {
        this.attributeDomainRefsetFiles = attributeDomainRefsetFiles;
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

    public Set<String> getRelationshipsFiles() {
        return relationshipsFiles;
    }

    public void setRelationshipsFiles(Set<String> relationshipsFiles) {
        this.relationshipsFiles = relationshipsFiles;
    }

    public Set<String> getOwlOntologyRefsetFiles() {
        return owlOntologyRefsetFiles;
    }

    public void setOwlOntologyRefsetFiles(Set<String> owlOntologyRefsetFiles) {
        this.owlOntologyRefsetFiles = owlOntologyRefsetFiles;
    }

    public Set<String> getOwlAxiomRefsetFiles() {
        return owlAxiomRefsetFiles;
    }

    public void setOwlAxiomRefsetFiles(Set<String> owlAxiomRefsetFiles) {
        this.owlAxiomRefsetFiles = owlAxiomRefsetFiles;
    }

    public void setTextDefinitionsFiles(Set<String> textDefinitionsFiles) {
        this.textDefinitionsFiles = textDefinitionsFiles;
    }

}
