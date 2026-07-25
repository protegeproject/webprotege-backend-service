package edu.stanford.protege.webprotege.project.chg;

import edu.stanford.protege.webprotege.change.ChangeApplicationResult;
import edu.stanford.protege.webprotege.change.ChangeListGenerator;
import edu.stanford.protege.webprotege.change.OntologyChange;
import edu.stanford.protege.webprotege.change.OntologyChangedEvent;
import edu.stanford.protege.webprotege.change.SilentChangeListGenerator;
import edu.stanford.protege.webprotege.common.ChangeRequestId;
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
import edu.stanford.protege.webprotege.revision.RevisionNumber;
import edu.stanford.protege.webprotege.webhook.ProjectChangedWebhookInvoker;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;

import java.util.List;
import java.util.Optional;
import java.util.OptionalLong;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.everyItem;
import static org.hamcrest.Matchers.hasItem;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.instanceOf;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.not;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.withSettings;

/**
 * Exercises the package-or-signal decision that {@link ProjectChangeEventGenerator} makes for each revision.
 */
@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
public class ProjectChangeEventGenerator_TestCase {

    private static final int THRESHOLD = 3;

    @Mock
    private ProjectChangedWebhookInvoker webhookInvoker;

    @Mock
    private EventOutbox eventOutbox;

    @Mock
    private EventTranslatorManager eventTranslatorManager;

    @Mock
    private ChangeApplicationResult<Object> finalResult;

    @Mock
    private ChangeListGenerator<Object> changeListGenerator;

    @Mock
    private Revision revision;

    private ProjectId projectId;

    private UserId userId;

    private ChangeRequestId changeRequestId;

    private EventTranslatorSessionId sessionId;

    private ProjectChangeEventGenerator generator;

    private PackagedProjectChangeEvent enqueuedEvent;

    private OptionalLong enqueuedRevisionNumber;

    @BeforeEach
    void setUp() {
        projectId = ProjectId.generate();
        userId = UserId.valueOf("the-user");
        changeRequestId = ChangeRequestId.generate();
        sessionId = EventTranslatorSessionId.create();
        when(revision.getRevisionNumber()).thenReturn(RevisionNumber.getRevisionNumber(7L));
        generator = new ProjectChangeEventGenerator(projectId, webhookInvoker, eventOutbox, THRESHOLD);
    }

    private List<OntologyChange> changesOfSize(int n) {
        return IntStream.range(0, n)
                        .mapToObj(i -> mock(OntologyChange.class))
                        .collect(Collectors.toList());
    }

    private void captureEnqueuedEvent() {
        var eventCaptor = ArgumentCaptor.forClass(PackagedProjectChangeEvent.class);
        var revisionCaptor = ArgumentCaptor.forClass(OptionalLong.class);
        verify(eventOutbox).enqueue(eventCaptor.capture(), revisionCaptor.capture());
        enqueuedEvent = eventCaptor.getValue();
        enqueuedRevisionNumber = revisionCaptor.getValue();
    }

    @Test
    void belowThreshold_dispatchesPerChangeEventsAndTranslatedHighLevelEvents() {
        when(finalResult.getChangeList()).thenReturn(changesOfSize(2));
        var highLevelEvent = mock(ProjectEvent.class);
        var proxy = mock(HighLevelProjectEventProxy.class);
        when(proxy.asProjectEvent()).thenReturn(highLevelEvent);
        doAnswer(invocation -> {
            List<HighLevelProjectEventProxy> highLevelEvents = invocation.getArgument(4);
            highLevelEvents.add(proxy);
            return null;
        }).when(eventTranslatorManager).translateOntologyChanges(any(), any(), any(), any(), anyList());

        generator.generateAndDispatch(changeRequestId, sessionId, userId, changeListGenerator, finalResult,
                                      eventTranslatorManager, Optional.of(revision));

        captureEnqueuedEvent();
        assertThat(enqueuedEvent.projectId(), is(projectId));
        // Two per-change ontology events plus the one translated high level event.
        assertThat(enqueuedEvent.projectEvents(), hasSize(3));
        assertThat(enqueuedEvent.projectEvents().stream()
                                .filter(e -> e instanceof OntologyChangedEvent)
                                .count(), is(2L));
        assertThat(enqueuedEvent.projectEvents(), hasItem(highLevelEvent));
        assertThat(enqueuedEvent.projectEvents(), everyItem(not(instanceOf(LargeNumberOfChangesEvent.class))));
        // The packaged event still rides gated on its revision's durability.
        assertThat(enqueuedRevisionNumber, is(OptionalLong.of(7L)));
        verify(webhookInvoker).invoke(eq(userId), any(RevisionNumber.class), anyLong());
    }

    @Test
    void aboveThreshold_dispatchesExactlyOneLargeChangeSignal() {
        when(finalResult.getChangeList()).thenReturn(changesOfSize(THRESHOLD + 1));

        generator.generateAndDispatch(changeRequestId, sessionId, userId, changeListGenerator, finalResult,
                                      eventTranslatorManager, Optional.of(revision));

        captureEnqueuedEvent();
        assertThat(enqueuedEvent.projectEvents(), hasSize(1));
        var onlyEvent = enqueuedEvent.projectEvents().get(0);
        assertThat(onlyEvent, is(instanceOf(LargeNumberOfChangesEvent.class)));
        assertThat(((LargeNumberOfChangesEvent) onlyEvent).projectId(), is(projectId));
        // The per-change flood must never be materialised, so the translator is not even consulted.
        verify(eventTranslatorManager, never()).translateOntologyChanges(any(), any(), any(), any(), anyList());
        // The webhook still fires in the collapsed mode, exactly as it does below the threshold.
        verify(webhookInvoker).invoke(eq(userId), any(RevisionNumber.class), anyLong());
    }

    @Test
    void atExactlyThreshold_dispatchesPerChangeEventsNotSignal() {
        when(finalResult.getChangeList()).thenReturn(changesOfSize(THRESHOLD));

        generator.generateAndDispatch(changeRequestId, sessionId, userId, changeListGenerator, finalResult,
                                      eventTranslatorManager, Optional.of(revision));

        captureEnqueuedEvent();
        // A change set at exactly the threshold is not "over" it, so it stays in the per-change mode.
        assertThat(enqueuedEvent.projectEvents(), hasSize(THRESHOLD));
        assertThat(enqueuedEvent.projectEvents(), everyItem(is(instanceOf(OntologyChangedEvent.class))));
        assertThat(enqueuedEvent.projectEvents(), everyItem(not(instanceOf(LargeNumberOfChangesEvent.class))));
    }

    @Test
    @SuppressWarnings("unchecked")
    void silentGenerator_dispatchesNothing() {
        ChangeListGenerator<Object> silentGenerator =
                mock(ChangeListGenerator.class, withSettings().extraInterfaces(SilentChangeListGenerator.class));

        generator.generateAndDispatch(changeRequestId, sessionId, userId, silentGenerator, finalResult,
                                      eventTranslatorManager, Optional.of(revision));

        verify(eventOutbox, never()).enqueue(any(), any());
        verify(webhookInvoker, never()).invoke(any(), any(), anyLong());
        verify(eventTranslatorManager, never()).translateOntologyChanges(any(), any(), any(), any(), anyList());
    }
}
