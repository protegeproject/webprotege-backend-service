package edu.stanford.protege.webprotege.dispatch.validators;

import edu.stanford.protege.webprotege.dispatch.Action;
import edu.stanford.protege.webprotege.dispatch.RequestValidationResult;
import edu.stanford.protege.webprotege.dispatch.RequestValidator;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 20/02/2013
 * <p>
 *     A request validator that always validates a request as valid.
 * </p>
 */
public class NullValidator implements RequestValidator {

    private static final NullValidator instance = new NullValidator();

    @Override
    public RequestValidationResult validateAction() {
        return RequestValidationResult.getValid();
    }

    @SuppressWarnings("unchecked")
    public static <A extends Action<?>> RequestValidator get() {
        return instance;
    }
}
