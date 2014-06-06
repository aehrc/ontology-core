package au.csiro.ontology.snomed.refset.rf2;

import java.util.Collection;

public class ValidationException extends Exception {

    private static final long serialVersionUID = 1L;

    public ValidationException(String message, Collection<String> problems) {
        super(makeMessage(message, problems));
    }

    private static String makeMessage(String message, Collection<String> problems) {
        if (null != problems) {
            for (String p: problems) {
                message = message + "\n\t" + p;
            }
        }
        return message;
    }

}
