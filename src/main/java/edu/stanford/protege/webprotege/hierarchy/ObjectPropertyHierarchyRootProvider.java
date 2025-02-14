package edu.stanford.protege.webprotege.hierarchy;

import org.semanticweb.owlapi.model.OWLDataFactory;
import org.semanticweb.owlapi.model.OWLObjectProperty;

import jakarta.inject.Inject;
import jakarta.inject.Provider;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 02/06/15
 */
public class ObjectPropertyHierarchyRootProvider implements Provider<OWLObjectProperty> {

    private final OWLDataFactory dataFactory;

    @Inject
    public ObjectPropertyHierarchyRootProvider(OWLDataFactory dataFactory) {
        this.dataFactory = dataFactory;
    }

    @Override
    public OWLObjectProperty get() {
        return dataFactory.getOWLTopObjectProperty();
    }
}
