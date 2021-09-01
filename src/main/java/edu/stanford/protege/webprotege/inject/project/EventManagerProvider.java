package edu.stanford.protege.webprotege.inject.project;

import edu.stanford.protege.webprotege.common.ProjectEvent;
import edu.stanford.protege.webprotege.events.EventLifeTime;
import edu.stanford.protege.webprotege.events.EventManager;
import edu.stanford.protege.webprotege.ipc.EventDispatcher;
import edu.stanford.protege.webprotege.project.ProjectDisposablesManager;
import edu.stanford.protege.webprotege.common.ProjectId;

import javax.inject.Inject;
import javax.inject.Provider;
import java.util.concurrent.TimeUnit;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 08/07/15
 */
public class EventManagerProvider implements Provider<EventManager<ProjectEvent>> {

    public static final EventLifeTime PROJECT_EVENT_LIFE_TIME = EventLifeTime.get(60, TimeUnit.SECONDS);

    private final ProjectDisposablesManager projectDisposablesManager;

    private final ProjectId projectId;

    private EventDispatcher eventDispatcher;

    @Inject
    public EventManagerProvider(ProjectDisposablesManager projectDisposablesManager, ProjectId projectId, EventDispatcher eventDispatcher) {
        this.projectDisposablesManager = checkNotNull(projectDisposablesManager);
        this.projectId = projectId;
        this.eventDispatcher = eventDispatcher;
    }

    @Override
    public EventManager<ProjectEvent> get() {
        EventManager<ProjectEvent> projectEventEventManager = new EventManager<>(PROJECT_EVENT_LIFE_TIME, projectId, eventDispatcher);
        projectDisposablesManager.register(projectEventEventManager);
        return projectEventEventManager;
    }
}
