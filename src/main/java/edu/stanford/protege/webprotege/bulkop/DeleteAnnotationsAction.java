package edu.stanford.protege.webprotege.bulkop;

import com.google.common.collect.ImmutableSet;
import edu.stanford.protege.webprotege.dispatch.ProjectAction;
import edu.stanford.protege.webprotege.common.ProjectId;
import org.semanticweb.owlapi.model.OWLEntity;

import javax.annotation.Nonnull;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 26 Sep 2018
 */
@Deprecated
public class DeleteAnnotationsAction implements ProjectAction<DeleteAnnotationsResult> {

    private ProjectId projectId;

    private ImmutableSet<OWLEntity> entities;

    private AnnotationSimpleMatchingCriteria criteria;

    public DeleteAnnotationsAction(@Nonnull ProjectId projectId,
                                   @Nonnull ImmutableSet<OWLEntity> entities,
                                   @Nonnull AnnotationSimpleMatchingCriteria criteria) {
        this.projectId = checkNotNull(projectId);
        this.entities = checkNotNull(entities);
        this.criteria = checkNotNull(criteria);
    }


    private DeleteAnnotationsAction() {
    }

    @Nonnull
    @Override
    public ProjectId getProjectId() {
        return projectId;
    }

    @Nonnull
    public ImmutableSet<OWLEntity> getEntities() {
        return entities;
    }

    @Nonnull
    public AnnotationSimpleMatchingCriteria getCriteria() {
        return criteria;
    }
}
