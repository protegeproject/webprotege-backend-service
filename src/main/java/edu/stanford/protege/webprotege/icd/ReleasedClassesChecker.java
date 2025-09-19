package edu.stanford.protege.webprotege.icd;

import org.semanticweb.owlapi.model.OWLClass;
import org.semanticweb.owlapi.model.OWLEntity;

import java.util.List;
import java.util.Set;

public interface ReleasedClassesChecker {
    boolean isReleased(OWLEntity entity);

    Set<OWLClass> getReleasedDescendants(OWLEntity owlEntity);

}
