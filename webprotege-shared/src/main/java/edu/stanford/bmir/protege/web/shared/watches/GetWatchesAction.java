package edu.stanford.bmir.protege.web.shared.watches;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonTypeName;
import com.google.auto.value.AutoValue;
import com.google.common.annotations.GwtCompatible;
import edu.stanford.bmir.protege.web.shared.HasUserId;
import edu.stanford.bmir.protege.web.shared.dispatch.ProjectAction;
import edu.stanford.bmir.protege.web.shared.project.ProjectId;
import edu.stanford.bmir.protege.web.shared.user.UserId;
import org.semanticweb.owlapi.model.OWLEntity;

import javax.annotation.Nonnull;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 20/03/2013
 */
@AutoValue
@GwtCompatible(serializable = true)
@JsonTypeName("GetWatches")
public abstract class GetWatchesAction implements ProjectAction<GetWatchesResult>, HasUserId {

    @JsonCreator
    public static GetWatchesAction create(@JsonProperty("projectId") ProjectId projectId,
                                          @JsonProperty("userId") UserId userId,
                                          @JsonProperty("entity") OWLEntity entity) {
        return new AutoValue_GetWatchesAction(projectId, userId, entity);
    }

    /**
     * Gets the {@link ProjectId}.
     * @return The {@link ProjectId}.  Not {@code null}.
     */
    @Nonnull
    @Override
    public abstract ProjectId getProjectId();

    /**
     * Gets the {@link UserId}.
     * @return The {@link UserId}.  Not {@code null}.
     */
    @Override
    public abstract UserId getUserId();

    public abstract OWLEntity getEntity();
}
