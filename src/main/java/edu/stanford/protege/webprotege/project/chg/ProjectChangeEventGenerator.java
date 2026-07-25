package edu.stanford.protege.webprotege.project.chg;

import edu.stanford.protege.webprotege.change.ChangeApplicationResult;
import edu.stanford.protege.webprotege.change.ChangeListGenerator;
import edu.stanford.protege.webprotege.change.HasHighLevelEvents;
import edu.stanford.protege.webprotege.change.OntologyChangedEvent;
import edu.stanford.protege.webprotege.change.SilentChangeListGenerator;
import edu.stanford.protege.webprotege.common.ChangeRequestId;
import edu.stanford.protege.webprotege.common.EventId;
import edu.stanford.protege.webprotege.common.ProjectEvent;
import edu.stanford.protege.webprotege.common.ProjectId;
import edu.stanford.protege.webprotege.common.UserId;
import edu.stanford.protege.webprotege.event.LargeNumberOfChangesEvent;
import edu.stanford.protege.webprotege.events.EventTranslatorManager;
import edu.stanford.protege.webprotege.events.EventTranslatorSessionId;
import edu.stanford.protege.webprotege.events.HighLevelProjectEventProxy;
import edu.stanford.protege.webprotege.events.outbox.EventOutbox;
import edu.stanford.protege.webprotege.project.PackagedProjectChangeEvent;
import edu.stanford.protege.webprotege.revision.Revision;
import edu.stanford.protege.webprotege.webhook.ProjectChangedWebhookInvoker;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.OptionalLong;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Turns the changes applied in a single revision into the project change events that announce them, and
 * records the resulting {@link PackagedProjectChangeEvent} in the durable outbox.
 *
 * <p>This is the sole place that decides, from the applied-change count alone, whether to announce every
 * individual change or to collapse the whole revision into a single {@link LargeNumberOfChangesEvent}
 * signal.  Extracting the decision out of {@code ChangeManager} (with its ~26 collaborators) keeps that
 * decision unit-testable in isolation.</p>
 */
public class ProjectChangeEventGenerator {

    private static final Logger LOGGER = LoggerFactory.getLogger(ProjectChangeEventGenerator.class);

    @Nonnull
    private final ProjectId projectId;

    @Nonnull
    private final ProjectChangedWebhookInvoker projectChangedWebhookInvoker;

    @Nonnull
    private final EventOutbox eventOutbox;

    /**
     * Change sets strictly larger than this are announced with a single {@link LargeNumberOfChangesEvent}
     * instead of one event per change.  See {@code webprotege.events.largeChangeThreshold} for the rationale.
     */
    private final int largeChangeThreshold;

    public ProjectChangeEventGenerator(@Nonnull ProjectId projectId,
                                       @Nonnull ProjectChangedWebhookInvoker projectChangedWebhookInvoker,
                                       @Nonnull EventOutbox eventOutbox,
                                       int largeChangeThreshold) {
        this.projectId = checkNotNull(projectId);
        this.projectChangedWebhookInvoker = checkNotNull(projectChangedWebhookInvoker);
        this.eventOutbox = checkNotNull(eventOutbox);
        this.largeChangeThreshold = largeChangeThreshold;
    }

    /**
     * Generates the high level events for a set of applied changes and records them, packaged into a single
     * {@link PackagedProjectChangeEvent}, in the durable outbox for reliable delivery.
     */
    public <R> void generateAndDispatch(ChangeRequestId changeRequestId,
                                        EventTranslatorSessionId eventTranslatorSessionId,
                                        UserId userId,
                                        ChangeListGenerator<R> changeListGenerator,
                                        ChangeApplicationResult<R> finalResult,
                                        EventTranslatorManager eventTranslatorManager,
                                        Optional<Revision> revision) {
        // A silent change list generator never announces its changes, no matter how many there are.  This is
        // checked first so that a large silent change set is not turned into a refresh signal either.
        if(changeListGenerator instanceof SilentChangeListGenerator) {
            return;
        }

        var changes = finalResult.getChangeList();

        List<ProjectEvent> eventList = new ArrayList<>();
        // Decide, from the applied-change count alone, whether to announce every individual change or to
        // collapse the whole revision into a single "large number of changes" signal.  The count is checked
        // BEFORE any per-change event object is built so that a bulk operation (for example a 100k-change CSV
        // import) never materialises a matching flood of event objects here.  Above the threshold the
        // per-change ontology events and the translated high level events are replaced by one
        // LargeNumberOfChangesEvent, which keeps the packaged payload well below Mongo's 16MB document cap and
        // the broker/browser frame limits that would otherwise silently drop an oversized bundle and leave
        // viewers stale; the client turns the signal into a "refresh?" prompt that reloads the whole view.
        if(changes.size() > largeChangeThreshold) {
            eventList.add(new LargeNumberOfChangesEvent(EventId.generate(), projectId));
            revision.ifPresent(rev ->
                    projectChangedWebhookInvoker.invoke(userId, rev.getRevisionNumber(), rev.getTimestamp()));
        }
        else {
            // Fire low-level ontology changed events.  There's an event for every change that was applied.
            for(var change : changes) {
                eventList.add(new OntologyChangedEvent(EventId.generate(), projectId, userId, change));
            }
            revision.ifPresent(rev -> {
                var highLevelEvents = new ArrayList<HighLevelProjectEventProxy>();
                eventTranslatorManager.translateOntologyChanges(eventTranslatorSessionId, changeRequestId, rev, finalResult, highLevelEvents);
                if(changeListGenerator instanceof HasHighLevelEvents) {
                    highLevelEvents.addAll(((HasHighLevelEvents) changeListGenerator).getHighLevelEvents());
                }
                highLevelEvents.stream().map(HighLevelProjectEventProxy::asProjectEvent)
                        .forEach((event) -> {
                            LOGGER.info("[ProjectManger] Dispatch high level event {}", event);
                            eventList.add(event);
                        });
                projectChangedWebhookInvoker.invoke(userId, rev.getRevisionNumber(), rev.getTimestamp());
            });
        }

        if(!eventList.isEmpty()) {
            var packagedProjectChange = new PackagedProjectChangeEvent(projectId, EventId.generate(), eventList);
            // Record the change event in the durable outbox instead of dispatching it directly.  A
            // revision-bearing change is gated on the revision's change-history write becoming durable.  An
            // insert failure propagates so that a change is never saved without a row that will announce it.
            var revisionNumber = revision.isPresent()
                    ? OptionalLong.of(revision.get().getRevisionNumber().getValue())
                    : OptionalLong.empty();
            eventOutbox.enqueue(packagedProjectChange, revisionNumber);
        }
    }
}
