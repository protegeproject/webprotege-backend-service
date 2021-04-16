package edu.stanford.bmir.protege.web.shared.projectsettings;

import com.google.web.bindery.event.shared.Event;
import edu.stanford.bmir.protege.web.shared.event.WebProtegeEvent;

import java.util.Objects;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 25/11/14
 */
public class ProjectSettingsChangedEvent extends WebProtegeEvent<ProjectSettingsChangedHandler> {

    public static final transient Event.Type<ProjectSettingsChangedHandler> ON_PROJECT_SETTINGS_CHANGED = new Event.Type<>();

    public static Event.Type<ProjectSettingsChangedHandler> getType() {
        return ON_PROJECT_SETTINGS_CHANGED;
    }

    private ProjectSettings projectSettings;

    /**
     * For serialization purposes only
     */
    private ProjectSettingsChangedEvent() {
    }

    public ProjectSettingsChangedEvent(ProjectSettings projectSettings) {
        this.projectSettings = checkNotNull(projectSettings);
    }

    /**
     * Gets the project settings.
     * @return The project settings.  Not {@code null}.
     */
    public ProjectSettings getProjectSettings() {
        return projectSettings;
    }

    @Override
    public Event.Type<ProjectSettingsChangedHandler> getAssociatedType() {
        return ON_PROJECT_SETTINGS_CHANGED;
    }

    @Override
    protected void dispatch(ProjectSettingsChangedHandler projectSettingsChangedHandler) {
        projectSettingsChangedHandler.handleProjectSettingsChanged(this);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof ProjectSettingsChangedEvent)) {
            return false;
        }
        ProjectSettingsChangedEvent that = (ProjectSettingsChangedEvent) o;
        return Objects.equals(projectSettings, that.projectSettings);
    }

    @Override
    public int hashCode() {
        return Objects.hash(projectSettings);
    }
}
