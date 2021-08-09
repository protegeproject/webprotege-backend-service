package edu.stanford.protege.webprotege.tag;

import edu.stanford.protege.webprotege.inject.ProjectSingleton;
import edu.stanford.protege.webprotege.common.ProjectId;
import org.semanticweb.owlapi.model.OWLEntity;

import javax.annotation.Nonnull;
import java.util.Collection;
import java.util.Optional;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 25 Jun 2018
 *
 * Represents the entity tags for a given project.
 */
@ProjectSingleton
public interface EntityTagsRepository {

    void save(@Nonnull EntityTags tag);

    void addTag(@Nonnull OWLEntity entity, @Nonnull TagId tagId, @Nonnull ProjectId projectId);

    void removeTag(@Nonnull OWLEntity entity, @Nonnull TagId tagId, ProjectId projectId);

    void removeTag(@Nonnull TagId tagId, ProjectId projectId);

    @Nonnull
    Optional<EntityTags> findByEntity(@Nonnull OWLEntity entity, ProjectId projectId);

    @Nonnull
    Collection<EntityTags> findByTagId(@Nonnull TagId tagId, ProjectId projectId);

    long countTaggedEntities(ProjectId projectId);
}
