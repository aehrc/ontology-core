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
        protected final Map<String, String> values = new HashMap<>();
        
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
        public Map<String, String> getValues() {
            return values;
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
        protected String moduleDependenciesRefsetFile;
        protected String refsetDescriptorRefsetFile;
        protected final Map<String, String> languageRefsetFiles = new HashMap<>();
        protected final Map<String, String> simpleRefsetFiles = new HashMap<>();
        protected final Map<String, String> orderedRefsetFiles = new HashMap<>();
        protected final Map<String, String> attributeValueRefsetFiles = new HashMap<>();
        protected final Map<String, String> simpleMapRefsetFiles = new HashMap<>();
        protected final Map<String, String> complexMapRefsetFiles = new HashMap<>();
        protected final Map<String, String> querySpecificationRefsetFiles = new HashMap<>();
        protected final Map<String, String> annotationRefsetFiles = new HashMap<>();
        protected final Map<String, String> associationRefsetFiles = new HashMap<>();
        
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
         * @param conceptsFile the conceptsFile to set
         */
        public void setConceptsFile(String conceptsFile) {
            this.conceptsFile = conceptsFile;
        }

        /**
         * @param descriptionsFile the descriptionsFile to set
         */
        public void setDescriptionsFile(String descriptionsFile) {
            this.descriptionsFile = descriptionsFile;
        }

        /**
         * @param identifiersFile the identifiersFile to set
         */
        public void setIdentifiersFile(String identifiersFile) {
            this.identifiersFile = identifiersFile;
        }

        /**
         * @param relationshipsFile the relationshipsFile to set
         */
        public void setRelationshipsFile(String relationshipsFile) {
            this.relationshipsFile = relationshipsFile;
        }

        /**
         * @param statedRelationshipsFile the statedRelationshipsFile to set
         */
        public void setStatedRelationshipsFile(String statedRelationshipsFile) {
            this.statedRelationshipsFile = statedRelationshipsFile;
        }

        /**
         * @param textDefinitionsFile the textDefinitionsFile to set
         */
        public void setTextDefinitionsFile(String textDefinitionsFile) {
            this.textDefinitionsFile = textDefinitionsFile;
        }

        /**
         * @param moduleDependenciesRefsetFile the moduleDependenciesRefsetFile to set
         */
        public void setModuleDependenciesRefsetFile(String moduleDependenciesRefsetFile) {
            this.moduleDependenciesRefsetFile = moduleDependenciesRefsetFile;
        }

        /**
         * @param refsetDescriptorRefsetFile the refsetDescriptorRefsetFile to set
         */
        public void setRefsetDescriptorRefsetFile(String refsetDescriptorRefsetFile) {
            this.refsetDescriptorRefsetFile = refsetDescriptorRefsetFile;
        }

        /**
         * @param releaseType the releaseType to set
         */
        public void setReleaseType(ReleaseType releaseType) {
            this.releaseType = releaseType;
        }

        /**
         * @return the conceptsFile
         */
        public String getConceptsFile() {
            return conceptsFile;
        }

        /**
         * @return the descriptionsFile
         */
        public String getDescriptionsFile() {
            return descriptionsFile;
        }

        /**
         * @return the identifiersFile
         */
        public String getIdentifiersFile() {
            return identifiersFile;
        }

        /**
         * @return the relationshipsFile
         */
        public String getRelationshipsFile() {
            return relationshipsFile;
        }

        /**
         * @return the statedRelationshipsFile
         */
        public String getStatedRelationshipsFile() {
            return statedRelationshipsFile;
        }

        /**
         * @return the textDefinitionsFile
         */
        public String getTextDefinitionsFile() {
            return textDefinitionsFile;
        }

        /**
         * @return the moduleDependenciesRefsetFile
         */
        public String getModuleDependenciesRefsetFile() {
            return moduleDependenciesRefsetFile;
        }

        /**
         * @return the refsetDescriptorRefsetFile
         */
        public String getRefsetDescriptorRefsetFile() {
            return refsetDescriptorRefsetFile;
        }

        /**
         * @return the languageRefsetFiles
         */
        public Map<String, String> getLanguageRefsetFiles() {
            return languageRefsetFiles;
        }

        /**
         * @return the simpleRefsetFiles
         */
        public Map<String, String> getSimpleRefsetFiles() {
            return simpleRefsetFiles;
        }

        /**
         * @return the orderedRefsetFiles
         */
        public Map<String, String> getOrderedRefsetFiles() {
            return orderedRefsetFiles;
        }

        /**
         * @return the attributeValueRefsetFiles
         */
        public Map<String, String> getAttributeValueRefsetFiles() {
            return attributeValueRefsetFiles;
        }

        /**
         * @return the simpleMapRefsetFiles
         */
        public Map<String, String> getSimpleMapRefsetFiles() {
            return simpleMapRefsetFiles;
        }

        /**
         * @return the complexMapRefsetFiles
         */
        public Map<String, String> getComplexMapRefsetFiles() {
            return complexMapRefsetFiles;
        }

        /**
         * @return the querySpecificationRefsetFiles
         */
        public Map<String, String> getQuerySpecificationRefsetFiles() {
            return querySpecificationRefsetFiles;
        }

        /**
         * @return the annotationRefsetFiles
         */
        public Map<String, String> getAnnotationRefsetFiles() {
            return annotationRefsetFiles;
        }

        /**
         * @return the associationRefsetFiles
         */
        public Map<String, String> getAssociationRefsetFiles() {
            return associationRefsetFiles;
        }

        /**
         * @return the releaseType
         */
        public ReleaseType getReleaseType() {
            return releaseType;
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
    
}
