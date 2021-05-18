package edu.stanford.bmir.protege.web.server.projectsettings;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 25/11/14
 */
public interface ProjectSettingsChangedHandler {

    void handleProjectSettingsChanged(ProjectSettingsChangedEvent event);
}
