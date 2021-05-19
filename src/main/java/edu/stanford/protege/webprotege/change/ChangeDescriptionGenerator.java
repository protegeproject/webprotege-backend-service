package edu.stanford.protege.webprotege.change;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 25/02/2013
 */
public interface ChangeDescriptionGenerator<S> {

    String generateChangeDescription(ChangeApplicationResult<S> result);
}
