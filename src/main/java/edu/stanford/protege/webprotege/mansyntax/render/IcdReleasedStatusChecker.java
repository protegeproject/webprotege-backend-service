package edu.stanford.protege.webprotege.mansyntax.render;

import org.semanticweb.owlapi.model.OWLEntity;

public interface IcdReleasedStatusChecker {

    boolean isReleased(OWLEntity entity);
}
