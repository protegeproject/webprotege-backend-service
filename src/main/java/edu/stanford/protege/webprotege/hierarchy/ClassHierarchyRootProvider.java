package edu.stanford.protege.webprotege.hierarchy;

import org.semanticweb.owlapi.model.OWLClass;
import org.semanticweb.owlapi.model.OWLDataFactory;

import javax.inject.Inject;
import javax.inject.Provider;
import java.util.Set;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 02/06/15
 */
public class ClassHierarchyRootProvider implements Provider<Set<OWLClass>> {

    private final OWLDataFactory dataFactory;

    @Inject
    public ClassHierarchyRootProvider(OWLDataFactory dataFactory) {
        this.dataFactory = dataFactory;
    }

    @Override
    public Set<OWLClass> get() {
        return Set.of(dataFactory.getOWLThing());
    }
}
