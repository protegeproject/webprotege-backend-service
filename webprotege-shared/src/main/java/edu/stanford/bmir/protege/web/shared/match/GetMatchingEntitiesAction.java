package edu.stanford.bmir.protege.web.shared.match;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonTypeName;
import com.google.auto.value.AutoValue;
import com.google.common.annotations.GwtCompatible;
import edu.stanford.bmir.protege.web.shared.dispatch.ProjectAction;
import edu.stanford.bmir.protege.web.shared.match.criteria.Criteria;
import edu.stanford.bmir.protege.web.shared.pagination.PageRequest;
import edu.stanford.bmir.protege.web.shared.project.ProjectId;

import javax.annotation.Nonnull;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 14 Jun 2018
 */
@AutoValue
@GwtCompatible(serializable = true)
@JsonTypeName("GetMatchingEntities")
public abstract class GetMatchingEntitiesAction implements ProjectAction<GetMatchingEntitiesResult> {

    @JsonCreator
    public static GetMatchingEntitiesAction create(@JsonProperty("projectId") ProjectId projectId,
                                                   @JsonProperty("criteria") Criteria criteria,
                                                   @JsonProperty("pageRequest") PageRequest pageRequest) {
        return new AutoValue_GetMatchingEntitiesAction(projectId, criteria, pageRequest);
    }

    @Nonnull
    @Override
    public abstract ProjectId getProjectId();

    @Nonnull
    public abstract Criteria getCriteria();

    @Nonnull
    public abstract PageRequest getPageRequest();
}
