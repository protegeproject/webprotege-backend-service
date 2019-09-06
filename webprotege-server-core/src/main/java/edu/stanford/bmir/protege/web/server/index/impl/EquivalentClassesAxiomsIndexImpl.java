package edu.stanford.bmir.protege.web.server.index.impl;

import edu.stanford.bmir.protege.web.server.index.AxiomsByEntityReferenceIndex;
import edu.stanford.bmir.protege.web.server.index.EquivalentClassesAxiomsIndex;
import org.semanticweb.owlapi.model.AxiomType;
import org.semanticweb.owlapi.model.OWLClass;
import org.semanticweb.owlapi.model.OWLEquivalentClassesAxiom;
import org.semanticweb.owlapi.model.OWLOntologyID;

import javax.annotation.Nonnull;
import javax.inject.Inject;
import java.util.stream.Stream;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2019-08-09
 */
public class EquivalentClassesAxiomsIndexImpl implements EquivalentClassesAxiomsIndex {

    @Nonnull
    private final AxiomsByEntityReferenceIndex axiomsByEntityReferenceIndex;

    @Inject
    public EquivalentClassesAxiomsIndexImpl(@Nonnull AxiomsByEntityReferenceIndex axiomsByEntityReferenceIndex) {
        this.axiomsByEntityReferenceIndex = checkNotNull(axiomsByEntityReferenceIndex);
    }

    @Nonnull
    @Override
    public Stream<OWLEquivalentClassesAxiom> getEquivalentClassesAxioms(@Nonnull OWLClass cls,
                                                                        @Nonnull OWLOntologyID ontologyID) {
        checkNotNull(cls);
        checkNotNull(ontologyID);
        return axiomsByEntityReferenceIndex.getReferencingAxioms(cls, ontologyID)
                                    .filter(OWLEquivalentClassesAxiom.class::isInstance)
                                    .map(OWLEquivalentClassesAxiom.class::cast);
    }
}
