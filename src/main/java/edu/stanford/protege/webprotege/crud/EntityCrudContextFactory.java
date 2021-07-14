package edu.stanford.protege.webprotege.crud;

import edu.stanford.protege.webprotege.project.ProjectDetailsRepository;
import edu.stanford.protege.webprotege.project.ProjectId;
import edu.stanford.protege.webprotege.user.UserId;
import org.semanticweb.owlapi.model.OWLOntologyID;

import javax.annotation.Nonnull;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2021-07-13
 */
public class EntityCrudContextFactory {

    private final ProjectId projectId;

    private final ProjectDetailsRepository projectDetailsRepository;

    public EntityCrudContextFactory(ProjectId projectId, ProjectDetailsRepository projectDetailsRepository) {
        this.projectId = projectId;
        this.projectDetailsRepository = projectDetailsRepository;
    }

    public EntityCrudContext create(@Nonnull UserId userId,
                                    @Nonnull PrefixedNameExpander prefixedNameExpander,
                                    @Nonnull OWLOntologyID targetOntologyId) {
        return new EntityCrudContext(projectId, projectDetailsRepository, userId, prefixedNameExpander, targetOntologyId);
    }
}
