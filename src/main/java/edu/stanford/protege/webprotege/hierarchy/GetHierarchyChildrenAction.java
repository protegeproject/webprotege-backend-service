package edu.stanford.protege.webprotege.hierarchy;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonTypeName;
import com.google.auto.value.AutoValue;
import edu.stanford.protege.webprotege.dispatch.ProjectAction;
import edu.stanford.protege.webprotege.pagination.PageRequest;
import edu.stanford.protege.webprotege.project.ProjectId;
import org.semanticweb.owlapi.model.OWLEntity;

import javax.annotation.Nonnull;

/**
 * Matthew Horridge Stanford Center for Biomedical Informatics Research 28 Nov 2017
 */
@AutoValue

@JsonTypeName("GetHierarchyChildren")
public abstract class GetHierarchyChildrenAction implements ProjectAction<GetHierarchyChildrenResult> {

    @JsonCreator
    public static GetHierarchyChildrenAction create(@JsonProperty("projectId") @Nonnull ProjectId projectId,
                                                    @JsonProperty("entity") @Nonnull OWLEntity entity,
                                                    @JsonProperty("hierarchyId") @Nonnull HierarchyId hierarchyId,
                                                    @JsonProperty("pageRequest") @Nonnull PageRequest pageRequest) {
        return new AutoValue_GetHierarchyChildrenAction(projectId, entity, hierarchyId, pageRequest);
    }

    public static GetHierarchyChildrenAction create(@Nonnull ProjectId projectId,
                                                    @Nonnull OWLEntity entity,
                                                    @Nonnull HierarchyId hierarchyId) {
        return create(projectId, entity, hierarchyId, PageRequest.requestFirstPage());
    }

    @Nonnull
    @Override
    public abstract ProjectId getProjectId();

    public abstract OWLEntity getEntity();

    public abstract HierarchyId getHierarchyId();

    @Nonnull
    public abstract PageRequest getPageRequest();
}
