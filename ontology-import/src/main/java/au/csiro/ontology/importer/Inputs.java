/**
 * Copyright CSIRO Australian e-Health Research Centre (http://aehrc.com).
 * All rights reserved. Use is subject to license terms and conditions.
 */
package au.csiro.ontology.importer;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.JDOMException;
import org.jdom2.input.SAXBuilder;

/**
 * This class is able to process a configuration file that specifies a set of
 * input ontologies potentially in different formats.
 * 
 * @author Alejandro Metke
 *
 */
public class Inputs {
    
    /**
     * Indicates if the files should be loaded from an external file system or
     * the class path.
     * 
     * @author Alejandro Metke
     *
     */
    public enum InputType {
        EXTERNAL, CLASSPATH
    };
    
    /**
     * Indicates the type of SNOMED release.
     * 
     * @author Alejandro Metke
     * 
     */
    public enum ReleaseType {
        FULL, SNAPSHOT, INCREMENTAL
    };
    
    /**
     * List of input files.
     */
    protected final List<Input> inputs = new ArrayList<>();
    
    /**
     * Initialises the input utilities based on a configuration file.
     * 
     * @param configFile
     */
    public Inputs(String configFile) {
        SAXBuilder builder = new SAXBuilder();
        try {
            Document document = (Document) builder.build(configFile);
            Element rootNode = document.getRootElement();
            processXML(rootNode);
        } catch (IOException e) {
            throw new RuntimeException(e);
        } catch (JDOMException e) {
            throw new RuntimeException(e);
        }
    }
    
    protected void processXML(Element root) {
        
    }
    
    /**
     * @return the inputFiles
     */
    public List<Input> getInputs() {
        return inputs;
    }
    
    /**
     * Contains a module id and a list of versions that should be imported.
     * 
     * @author Alejandro Metke
     *
     */
    class ModuleInfo {
        protected final String id;
        protected final List<Version> versions = new ArrayList<>();
        
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
        
    }
    
    /**
     * Contains the id of a version and the meta-data associated with it.
     * 
     * @author Alejandro Metke
     *
     */
    class Version {
        protected final String id;
        protected final Map<String, String> metadata = new HashMap<>();
        
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
        
    }
    
    /**
     * Base class for inputs.
     * 
     * @author Alejandro Metke
     *
     */
    abstract class Input {
        protected InputType type;

        /**
         * @return the type
         */
        public InputType getType() {
            return type;
        }

    }
    
    /**
     * An RF set of input files.
     * 
     * @author Alejandro Metke
     *
     */
    class RF2Input extends Input {
        // Terminology files
        protected String conceptsFile;
        protected String descriptionsFile;
        protected String identifiersFile;
        protected String relationshipsFile;
        protected String statedRelationshipsFile;
        protected String textDefinitionsFile;
        
        // Reference set files
        protected List<Refset> refsetDescriptorRefsetFiles;
        protected List<Refset> descriptionFormatRefsetFiles;
        protected List<Refset> moduleDependenciesRefsetFiles;
        protected List<Refset> languageRefsetFiles;
        protected List<Refset> simpleRefsetFiles;
        protected List<Refset> orderedRefsetFiles;
        protected List<Refset> attributeValueRefsetFiles;
        protected List<Refset> simpleMapRefsetFiles;
        protected List<Refset> complexMapRefsetFiles;
        protected List<Refset> querySpecificationRefsetFiles;
        protected List<Refset> annotationRefsetFiles;
        protected List<Refset> associationRefsetFiles;
        
        // The type of release - full, snapshot or incremental
        protected ReleaseType releaseType;
        
        // List of module information - indicates which modules should be
        // processed and includes the meta-data needed to do so
        protected final List<ModuleInfo> modules = new ArrayList<>();
        
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
        public List<Refset> getRefsetDescriptorRefsetFiles() {
            return refsetDescriptorRefsetFiles;
        }

        /**
         * @param refsetDescriptorRefsetFiles the refsetDescriptorRefsetFiles to set
         */
        public void setRefsetDescriptorRefsetFiles(
                List<Refset> refsetDescriptorRefsetFiles) {
            this.refsetDescriptorRefsetFiles = refsetDescriptorRefsetFiles;
        }

        /**
         * @return the descriptionFormatRefsetFiles
         */
        public List<Refset> getDescriptionFormatRefsetFiles() {
            return descriptionFormatRefsetFiles;
        }

        /**
         * @param descriptionFormatRefsetFiles the descriptionFormatRefsetFiles to set
         */
        public void setDescriptionFormatRefsetFiles(
                List<Refset> descriptionFormatRefsetFiles) {
            this.descriptionFormatRefsetFiles = descriptionFormatRefsetFiles;
        }

        /**
         * @return the moduleDependenciesRefsetFiles
         */
        public List<Refset> getModuleDependenciesRefsetFiles() {
            return moduleDependenciesRefsetFiles;
        }

        /**
         * @param moduleDependenciesRefsetFiles the moduleDependenciesRefsetFiles to set
         */
        public void setModuleDependenciesRefsetFiles(
                List<Refset> moduleDependenciesRefsetFiles) {
            this.moduleDependenciesRefsetFiles = moduleDependenciesRefsetFiles;
        }

        /**
         * @return the languageRefsetFiles
         */
        public List<Refset> getLanguageRefsetFiles() {
            return languageRefsetFiles;
        }

        /**
         * @param languageRefsetFiles the languageRefsetFiles to set
         */
        public void setLanguageRefsetFiles(List<Refset> languageRefsetFiles) {
            this.languageRefsetFiles = languageRefsetFiles;
        }

        /**
         * @return the simpleRefsetFiles
         */
        public List<Refset> getSimpleRefsetFiles() {
            return simpleRefsetFiles;
        }

        /**
         * @param simpleRefsetFiles the simpleRefsetFiles to set
         */
        public void setSimpleRefsetFiles(List<Refset> simpleRefsetFiles) {
            this.simpleRefsetFiles = simpleRefsetFiles;
        }

        /**
         * @return the orderedRefsetFiles
         */
        public List<Refset> getOrderedRefsetFiles() {
            return orderedRefsetFiles;
        }

        /**
         * @param orderedRefsetFiles the orderedRefsetFiles to set
         */
        public void setOrderedRefsetFiles(List<Refset> orderedRefsetFiles) {
            this.orderedRefsetFiles = orderedRefsetFiles;
        }

        /**
         * @return the attributeValueRefsetFiles
         */
        public List<Refset> getAttributeValueRefsetFiles() {
            return attributeValueRefsetFiles;
        }

        /**
         * @param attributeValueRefsetFiles the attributeValueRefsetFiles to set
         */
        public void setAttributeValueRefsetFiles(List<Refset> attributeValueRefsetFiles) {
            this.attributeValueRefsetFiles = attributeValueRefsetFiles;
        }

        /**
         * @return the simpleMapRefsetFiles
         */
        public List<Refset> getSimpleMapRefsetFiles() {
            return simpleMapRefsetFiles;
        }

        /**
         * @param simpleMapRefsetFiles the simpleMapRefsetFiles to set
         */
        public void setSimpleMapRefsetFiles(List<Refset> simpleMapRefsetFiles) {
            this.simpleMapRefsetFiles = simpleMapRefsetFiles;
        }

        /**
         * @return the complexMapRefsetFiles
         */
        public List<Refset> getComplexMapRefsetFiles() {
            return complexMapRefsetFiles;
        }

        /**
         * @param complexMapRefsetFiles the complexMapRefsetFiles to set
         */
        public void setComplexMapRefsetFiles(List<Refset> complexMapRefsetFiles) {
            this.complexMapRefsetFiles = complexMapRefsetFiles;
        }

        /**
         * @return the querySpecificationRefsetFiles
         */
        public List<Refset> getQuerySpecificationRefsetFiles() {
            return querySpecificationRefsetFiles;
        }

        /**
         * @param querySpecificationRefsetFiles the querySpecificationRefsetFiles to set
         */
        public void setQuerySpecificationRefsetFiles(
                List<Refset> querySpecificationRefsetFiles) {
            this.querySpecificationRefsetFiles = querySpecificationRefsetFiles;
        }

        /**
         * @return the annotationRefsetFiles
         */
        public List<Refset> getAnnotationRefsetFiles() {
            return annotationRefsetFiles;
        }

        /**
         * @param annotationRefsetFiles the annotationRefsetFiles to set
         */
        public void setAnnotationRefsetFiles(List<Refset> annotationRefsetFiles) {
            this.annotationRefsetFiles = annotationRefsetFiles;
        }

        /**
         * @return the associationRefsetFiles
         */
        public List<Refset> getAssociationRefsetFiles() {
            return associationRefsetFiles;
        }

        /**
         * @param associationRefsetFiles the associationRefsetFiles to set
         */
        public void setAssociationRefsetFiles(List<Refset> associationRefsetFiles) {
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
        
    }
    
    class OWLInput extends Input {
        protected String owlFile;
        
        public OWLInput(String owlFile, InputType type) {
            this.type = type;
            this.owlFile = owlFile;
        }

        /**
         * @return the owlFile
         */
        public String getOwlFile() {
            return owlFile;
        }
        
    }
    
    /**
     * Represents the name and the location of a reference set.
     * 
     * @author Alejandro Metke
     *
     */
    class Refset {
        protected String name;
        protected String href;
        
        public Refset() {
            
        }

        /**
         * @return the name
         */
        public String getName() {
            return name;
        }

        /**
         * @param name the name to set
         */
        public void setName(String name) {
            this.name = name;
        }

        /**
         * @return the href
         */
        public String getHref() {
            return href;
        }

        /**
         * @param href the href to set
         */
        public void setHref(String href) {
            this.href = href;
        }
        
    }
    
}
