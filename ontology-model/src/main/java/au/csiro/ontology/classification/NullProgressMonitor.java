/**
 * Copyright CSIRO Australian e-Health Research Centre (http://aehrc.com).
 * All rights reserved. Use is subject to license terms and conditions.
 */
package au.csiro.ontology.classification;


/**
 * Implementation of {@link IProgressMonitor} that does nothing.
 * 
 * @author Alejandro Metke
 *
 */
public class NullProgressMonitor implements IProgressMonitor {

    @Override
    public void taskStarted(String taskName) {
        
    }

    @Override
    public void taskEnded() {
        
    }

    @Override
    public void step(int value, int max) {
        
    }

    @Override
    public void taskBusy() {
        
    }

}
