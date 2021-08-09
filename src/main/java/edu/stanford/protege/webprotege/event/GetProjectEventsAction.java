package edu.stanford.protege.webprotege.event;

import com.google.common.base.Objects;
import edu.stanford.protege.webprotege.dispatch.Action;
import edu.stanford.protege.webprotege.project.HasProjectId;
import edu.stanford.protege.webprotege.common.ProjectId;

import javax.annotation.Nonnull;

import static com.google.common.base.MoreObjects.toStringHelper;
import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 20/03/2013
 */
public class GetProjectEventsAction implements Action<GetProjectEventsResult>, HasProjectId {

    private ProjectId projectId;

    private EventTag sinceTag;

    /**
     * For serialization purposes only.
     */
    private GetProjectEventsAction() {
    }

    private GetProjectEventsAction(@Nonnull EventTag sinceTag, @Nonnull ProjectId projectId) {
        this.sinceTag = checkNotNull(sinceTag);
        this.projectId = checkNotNull(projectId);
    }

    public static GetProjectEventsAction create(@Nonnull EventTag sinceTag, @Nonnull ProjectId projectId) {
        return new GetProjectEventsAction(sinceTag, projectId);
    }

    public EventTag getSinceTag() {
        return sinceTag;
    }

    @Nonnull
    @Override
    public ProjectId getProjectId() {
        return projectId;
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(sinceTag, projectId);
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == this) {
            return true;
        }
        if (!(obj instanceof GetProjectEventsAction)) {
            return false;
        }
        GetProjectEventsAction other = (GetProjectEventsAction) obj;
        return this.sinceTag.equals(other.sinceTag)
                && this.projectId.equals(other.projectId);
    }

    @Override
    public String toString() {
        return toStringHelper("GetProjectEventsAction")
                          .addValue(projectId)
                          .add("since", sinceTag).toString();
    }
}
