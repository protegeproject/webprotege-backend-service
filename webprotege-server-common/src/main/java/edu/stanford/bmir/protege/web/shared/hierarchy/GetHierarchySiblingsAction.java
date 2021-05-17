package edu.stanford.bmir.protege.web.shared.hierarchy;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonTypeName;
import com.google.auto.value.AutoValue;
import edu.stanford.bmir.protege.web.shared.dispatch.ProjectAction;
import edu.stanford.bmir.protege.web.shared.pagination.PageRequest;
import edu.stanford.bmir.protege.web.shared.project.ProjectId;
import org.semanticweb.owlapi.model.OWLEntity;

import javax.annotation.Nonnull;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 13 Sep 2018
 */
@AutoValue

@JsonTypeName("GetHierarchySiblings")
public abstract class GetHierarchySiblingsAction implements ProjectAction<GetHierarchySiblingsResult> {

    @JsonCreator
    public static GetHierarchySiblingsAction create(@JsonProperty("projectId") ProjectId projectId,
                                                    @JsonProperty("entity") OWLEntity entity,
                                                    @JsonProperty("hierarchyId") HierarchyId hierarchyId,
                                                    @JsonProperty("pageRequest") PageRequest pageRequest) {
        return new AutoValue_GetHierarchySiblingsAction(projectId, entity, hierarchyId, pageRequest);
    }

    @Nonnull
    @Override
    public abstract ProjectId getProjectId();

    @Nonnull
    public abstract OWLEntity getEntity();

    @Nonnull
    public abstract HierarchyId getHierarchyId();

    @Nonnull
    public abstract PageRequest getPageRequest();
}
