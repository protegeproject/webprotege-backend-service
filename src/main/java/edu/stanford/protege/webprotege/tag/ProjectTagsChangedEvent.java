package edu.stanford.protege.webprotege.tag;

import com.google.common.base.Objects;
import com.google.common.collect.ImmutableList;
import edu.stanford.protege.webprotege.event.ProjectEvent;
import edu.stanford.protege.webprotege.project.ProjectId;

import javax.annotation.Nonnull;
import javax.inject.Inject;
import java.util.Collection;

import static com.google.common.base.MoreObjects.toStringHelper;
import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 26 Mar 2018
 */
public class ProjectTagsChangedEvent extends ProjectEvent<ProjectTagsChangedHandler> {

    private Collection<Tag> projectTags;

    @Inject
    public ProjectTagsChangedEvent(@Nonnull ProjectId source,
                                   @Nonnull Collection<Tag> projectTags) {
        super(source);
        this.projectTags = ImmutableList.copyOf(checkNotNull(projectTags));
    }


    private ProjectTagsChangedEvent() {
    }

    /**
     * Returns an immutable list of project tags.
     */
    @Nonnull
    public Collection<Tag> getProjectTags() {
        return projectTags;
    }

    @Override
    protected void dispatch(ProjectTagsChangedHandler handler) {
        handler.handleProjectTagsChanged(this);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getProjectId(), projectTags);
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == this) {
            return true;
        }
        if (!(obj instanceof ProjectTagsChangedEvent)) {
            return false;
        }
        ProjectTagsChangedEvent other = (ProjectTagsChangedEvent) obj;
        return this.getProjectId().equals(other.getProjectId())
                && this.projectTags.equals(other.projectTags);
    }


    @Override
    public String toString() {
        return toStringHelper("ProjectTagsChangedEvent")
                .addValue(getProjectId())
                .add("tags", projectTags)
                .toString();
    }
}
