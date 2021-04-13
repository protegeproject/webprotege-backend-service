package edu.stanford.bmir.protege.web.shared.change;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonTypeName;
import com.google.auto.value.AutoValue;
import com.google.common.annotations.GwtCompatible;
import edu.stanford.bmir.protege.web.shared.dispatch.Result;
import edu.stanford.bmir.protege.web.shared.event.EventList;
import edu.stanford.bmir.protege.web.shared.event.HasEventList;
import edu.stanford.bmir.protege.web.shared.event.ProjectEvent;
import edu.stanford.bmir.protege.web.shared.project.HasProjectId;
import edu.stanford.bmir.protege.web.shared.project.ProjectId;
import edu.stanford.bmir.protege.web.shared.revision.RevisionNumber;

import javax.annotation.Nonnull;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 19/03/15
 */
@AutoValue
@GwtCompatible(serializable = true)
@JsonTypeName("RevertRevision")
public abstract class RevertRevisionResult implements Result, HasProjectId, HasEventList<ProjectEvent<?>> {

    @JsonCreator
    @Nonnull
    public static RevertRevisionResult create(@JsonProperty("projectId") @Nonnull ProjectId projectId,
                                              @JsonProperty("revisionNumber") @Nonnull RevisionNumber revisionNumber,
                                              @JsonProperty("eventList") @Nonnull EventList<ProjectEvent<?>> eventList) {
        return new AutoValue_RevertRevisionResult(projectId, revisionNumber, eventList);
    }

    @Nonnull
    @Override
    public abstract ProjectId getProjectId();

    @Nonnull
    public abstract RevisionNumber getRevisionNumber();

    @Override
    public abstract EventList<ProjectEvent<?>> getEventList();


}
