package edu.stanford.protege.webprotege.linearization;

import org.semanticweb.owlapi.model.IRI;

public interface CreateLinearizationManager {

    boolean createLinearizationFromParent(IRI newEntityIri, IRI parentEntityIri);
}
