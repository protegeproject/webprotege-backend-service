package edu.stanford.protege.webprotege.dispatch;


/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 20/01/2013
 */
public interface RequestValidator {

    RequestValidationResult validateAction();
}
