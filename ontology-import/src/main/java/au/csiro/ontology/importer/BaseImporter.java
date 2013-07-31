/**
 * Copyright CSIRO Australian e-Health Research Centre (http://aehrc.com).
 * All rights reserved. Use is subject to license terms and conditions.
 */
package au.csiro.ontology.importer;

import java.util.Map;
import java.util.Set;

/**
 * @author Alejandro Metke
 *
 */
public abstract class BaseImporter implements IImporter {
    
    protected class Module {
        private final String moduleId;
        private final String moduleVersion;
        
        public Module(String moduleId, String moduleVersion) {
            super();
            this.moduleId = moduleId;
            this.moduleVersion = moduleVersion;
        }
        
        public String getModuleId() {
            return moduleId;
        }
        
        public String getModuleVersion() {
            return moduleVersion;
        }
        
    }
    
    protected class ImportEntry {
        private final String rootModuleId;
        private final String rootModuleVersion;
        private final Map<String, String> metadata;
        private final Set<Module> modules;
        
        public ImportEntry(String rootModuleId, String rootModuleVersion,
                Map<String, String> metadata, Set<Module> modules) {
            this.rootModuleId = rootModuleId;
            this.rootModuleVersion = rootModuleVersion;
            this.metadata = metadata;
            this.modules = modules;
        }
        
        public String getRootModuleId() {
            return rootModuleId;
        }
        
        public String getRootModuleVersion() {
            return rootModuleVersion;
        }
        
        public Map<String, String> getMetadata() {
            return metadata;
        }
        
        public Set<Module> getModules() {
            return modules;
        }
        
    }
    
}
