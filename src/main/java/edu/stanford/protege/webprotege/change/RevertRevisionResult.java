package edu.stanford.protege.webprotege.change;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonTypeName;
import com.google.auto.value.AutoValue;
import edu.stanford.protege.webprotege.dispatch.Result;
import edu.stanford.protege.webprotege.event.EventList;
import edu.stanford.protege.webprotege.event.HasEventList;
import edu.stanford.protege.webprotege.event.ProjectEvent;
import edu.stanford.protege.webprotege.project.HasProjectId;
import edu.stanford.protege.webprotege.project.ProjectId;
import edu.stanford.protege.webprotege.revision.RevisionNumber;

import javax.annotation.Nonnull;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 19/03/15
 */
@AutoValue

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
