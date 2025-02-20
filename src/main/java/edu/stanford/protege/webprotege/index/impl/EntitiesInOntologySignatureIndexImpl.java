package edu.stanford.protege.webprotege.index.impl;

import edu.stanford.protege.webprotege.index.EntitiesInOntologySignatureIndex;
import edu.stanford.protege.webprotege.index.OntologyAnnotationsSignatureIndex;
import edu.stanford.protege.webprotege.index.OntologyAxiomsSignatureIndex;
import org.semanticweb.owlapi.model.OWLEntity;
import org.semanticweb.owlapi.model.OWLOntologyID;

import javax.annotation.Nonnull;
import jakarta.inject.Inject;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2019-08-19
 */
public class EntitiesInOntologySignatureIndexImpl implements EntitiesInOntologySignatureIndex {

    @Nonnull
    private final OntologyAxiomsSignatureIndex ontologyAxiomsSignatureIndex;

    @Nonnull
    private final OntologyAnnotationsSignatureIndex ontologyAnnotationsSignatureIndex;

    @Inject
    public EntitiesInOntologySignatureIndexImpl(@Nonnull OntologyAxiomsSignatureIndex ontologyAxiomsSignatureIndex,
                                                @Nonnull OntologyAnnotationsSignatureIndex ontologyAnnotationsSignatureIndex) {
        this.ontologyAxiomsSignatureIndex = checkNotNull(ontologyAxiomsSignatureIndex);
        this.ontologyAnnotationsSignatureIndex = checkNotNull(ontologyAnnotationsSignatureIndex);
    }


    @Override
    public boolean containsEntityInSignature(@Nonnull OWLEntity entity, @Nonnull OWLOntologyID ontologyId) {
        checkNotNull(ontologyId);
        checkNotNull(entity);
        var inAxiomsSignature = ontologyAxiomsSignatureIndex.containsEntityInOntologyAxiomsSignature(entity, ontologyId);
        if(inAxiomsSignature) {
            return true;
        }
        if(entity.isOWLAnnotationProperty()) {
            return ontologyAnnotationsSignatureIndex.containsEntityInOntologyAnnotationsSignature(entity, ontologyId);
        }
        return false;

    }
}
