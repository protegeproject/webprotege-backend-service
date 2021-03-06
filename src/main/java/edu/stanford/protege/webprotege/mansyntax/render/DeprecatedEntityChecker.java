package edu.stanford.protege.webprotege.mansyntax.render;

import org.semanticweb.owlapi.model.OWLEntity;

/**
* Matthew Horridge
* Stanford Center for Biomedical Informatics Research
* 27/01/15
*/
public interface DeprecatedEntityChecker {

    boolean isDeprecated(OWLEntity entity);
}
