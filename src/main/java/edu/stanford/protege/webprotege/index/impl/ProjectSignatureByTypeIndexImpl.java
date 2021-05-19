package edu.stanford.protege.webprotege.index.impl;

import edu.stanford.protege.webprotege.index.DependentIndex;
import edu.stanford.protege.webprotege.index.Index;
import edu.stanford.protege.webprotege.index.ProjectSignatureByTypeIndex;
import edu.stanford.protege.webprotege.inject.ProjectSingleton;
import org.semanticweb.owlapi.model.EntityType;
import org.semanticweb.owlapi.model.OWLEntity;

import javax.annotation.Nonnull;
import javax.inject.Inject;
import java.util.Collection;
import java.util.List;
import java.util.stream.Stream;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2019-08-16
 */
@ProjectSingleton
public class ProjectSignatureByTypeIndexImpl implements ProjectSignatureByTypeIndex, DependentIndex {

    @Nonnull
    private final AxiomsByEntityReferenceIndexImpl delegate;

    @Inject
    public ProjectSignatureByTypeIndexImpl(@Nonnull AxiomsByEntityReferenceIndexImpl axiomsByEntityReferenceIndex) {
        this.delegate = checkNotNull(axiomsByEntityReferenceIndex);
    }

    @Nonnull
    @Override
    public Collection<Index> getDependencies() {
        return List.of(delegate);
    }

    @Nonnull
    @Override
    public <E extends OWLEntity> Stream<E> getSignature(@Nonnull EntityType<E> entityType) {
        checkNotNull(entityType);
        return delegate.getProjectAxiomsSignature(entityType);
    }
}
