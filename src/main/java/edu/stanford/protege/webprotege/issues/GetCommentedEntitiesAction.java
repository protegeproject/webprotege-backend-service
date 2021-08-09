package edu.stanford.protege.webprotege.issues;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonTypeName;
import com.google.auto.value.AutoValue;
import edu.stanford.protege.webprotege.dispatch.ProjectAction;
import edu.stanford.protege.webprotege.pagination.PageRequest;
import edu.stanford.protege.webprotege.common.ProjectId;

import javax.annotation.Nonnull;
import java.util.Set;


/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 7 Mar 2017
 */
@AutoValue

@JsonTypeName("GetCommentedEntities")
public abstract class GetCommentedEntitiesAction implements ProjectAction<GetCommentedEntitiesResult> {

    @JsonCreator
    public static GetCommentedEntitiesAction create(@JsonProperty("projectId") @Nonnull ProjectId projectId,
                                      @JsonProperty("userIdFilter") @Nonnull String userIdFilter,
                                      @JsonProperty("statusFilter") @Nonnull Set<Status> statusFilter,
                                      @JsonProperty("sortingKey") @Nonnull SortingKey sortingKey,
                                      @JsonProperty("pageRequest") @Nonnull PageRequest pageRequest) {
        return new AutoValue_GetCommentedEntitiesAction(projectId, sortingKey, pageRequest, userIdFilter, statusFilter);
    }

    @Nonnull
    @Override
    public abstract ProjectId getProjectId();

    @Nonnull
    public abstract SortingKey getSortingKey();

    @Nonnull
    public abstract PageRequest getPageRequest();

    @Nonnull
    public abstract String getUserIdFilter();

    @Nonnull
    public abstract Set<Status> getStatusFilter();
}
