/**
 * Copyright CSIRO Australian e-Health Research Centre (http://aehrc.com).
 * All rights reserved. Use is subject to license terms and conditions.
 */
package au.csiro.ontology.importer;

/**
 * Exception thrown to indicate that a problem was found when importing a set
 * of axioms into our internal format.
 * 
 * @author Alejandro Metke
 *
 */
public class ImportException extends RuntimeException {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    /**
     * 
     */
    public ImportException() {
        
    }

    /**
     * @param arg0
     */
    public ImportException(String arg0) {
        super(arg0);
    }

    /**
     * @param arg0
     */
    public ImportException(Throwable arg0) {
        super(arg0);
    }

    /**
     * @param arg0
     * @param arg1
     */
    public ImportException(String arg0, Throwable arg1) {
        super(arg0, arg1);
    }

    /**
     * @param arg0
     * @param arg1
     * @param arg2
     * @param arg3
     */
    public ImportException(String arg0, Throwable arg1, boolean arg2,
            boolean arg3) {
        super(arg0, arg1, arg2, arg3);
    }

}
