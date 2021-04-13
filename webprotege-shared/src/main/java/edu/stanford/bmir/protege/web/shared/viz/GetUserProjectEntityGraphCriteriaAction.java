package edu.stanford.bmir.protege.web.shared.viz;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonTypeName;
import com.google.auto.value.AutoValue;
import com.google.common.annotations.GwtCompatible;
import edu.stanford.bmir.protege.web.shared.annotations.GwtSerializationConstructor;
import edu.stanford.bmir.protege.web.shared.dispatch.ProjectAction;
import edu.stanford.bmir.protege.web.shared.project.ProjectId;

import javax.annotation.Nonnull;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2019-12-10
 */
@AutoValue
@GwtCompatible(serializable = true)
@JsonTypeName("GetUserProjectEntityGraphCriteria")
public abstract class GetUserProjectEntityGraphCriteriaAction implements ProjectAction<GetUserProjectEntityGraphCriteriaResult> {

    @JsonCreator
    public static GetUserProjectEntityGraphCriteriaAction create(@JsonProperty("projectId") @Nonnull ProjectId projectId) {
        return new AutoValue_GetUserProjectEntityGraphCriteriaAction(projectId);
    }

    @Nonnull
    @Override
    public abstract ProjectId getProjectId();
}
