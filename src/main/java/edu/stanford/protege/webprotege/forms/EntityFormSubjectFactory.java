package edu.stanford.protege.webprotege.forms;

import edu.stanford.protege.webprotege.entity.FreshEntityIri;
import org.semanticweb.owlapi.model.OWLEntity;
import org.semanticweb.owlapi.model.OWLEntityByTypeProvider;

import javax.annotation.Nonnull;
import jakarta.inject.Inject;
import java.util.UUID;

import static com.google.common.base.Preconditions.checkNotNull;
import static com.google.common.collect.ImmutableSet.toImmutableSet;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2019-11-13
 */
public class EntityFormSubjectFactory {

    private static final String EMPTY_SUPPLIED_NAME = "";

    public static final String EMPTY_LANG_TAG = "";

    @Nonnull
    private final OWLEntityByTypeProvider dataFactory;

    @Inject
    public EntityFormSubjectFactory(@Nonnull OWLEntityByTypeProvider dataFactory) {
        this.dataFactory = checkNotNull(dataFactory);
    }

    @Nonnull
    public OWLEntity createSubject(@Nonnull FormSubjectFactoryDescriptor subjectFactoryDescriptor) {
        var parentIris = subjectFactoryDescriptor.getParent()
                                .stream()
                                .map(OWLEntity::getIRI)
                                .collect(toImmutableSet());
        var entityType = subjectFactoryDescriptor.getEntityType();
        var discriminator = UUID.randomUUID().toString();
        var freshEntityIri = FreshEntityIri.get(EMPTY_SUPPLIED_NAME,
                                                EMPTY_LANG_TAG,
                                                discriminator,
                                                parentIris);
        return dataFactory.getOWLEntity(entityType, freshEntityIri.getIri());
    }
}
