package edu.stanford.protege.webprotege.project;

import edu.stanford.protege.webprotege.access.AccessManager;
import edu.stanford.protege.webprotege.common.BlobLocation;
import edu.stanford.protege.webprotege.common.Event;
import edu.stanford.protege.webprotege.common.EventId;
import edu.stanford.protege.webprotege.common.ProjectId;
import edu.stanford.protege.webprotege.ipc.EventDispatcher;
import edu.stanford.protege.webprotege.ipc.ExecutionContext;
import edu.stanford.protege.webprotege.storage.MinioFileDownloader;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.api.io.TempDir;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;
import org.semanticweb.binaryowl.BinaryOWLOntologyChangeLog;
import org.semanticweb.owlapi.model.OWLDataFactory;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ReplaceProjectHistoryActionHandler_DomainEventsTest {

    @Mock private AccessManager accessManager;
    @Mock private ProjectHistoryReplacer projectHistoryReplacer;
    @Mock private MinioFileDownloader minioFileDownloader;
    @Mock private EventDispatcher eventDispatcher;
    @Mock private OWLDataFactory dataFactory;
    @Mock private ReplaceProjectHistoryRequest request;
    @Mock private ExecutionContext executionContext;

    private final Executor direct = Runnable::run;

    private ReplaceProjectHistoryActionHandler newHandler() {
        return new ReplaceProjectHistoryActionHandler(
                accessManager,
                projectHistoryReplacer,
                minioFileDownloader,
                eventDispatcher,
                dataFactory,
                direct
        );
    }

    private static BlobLocation blob(String b, String n) { return new BlobLocation(b, n); }

    @Test
    void emitsSuccessDomainEvent_andPackagedEvent_onSuccess(@TempDir Path tmp) throws Exception {
        var handler = newHandler();
        var projectId = mock(ProjectId.class);
        var blobLocation = blob("bucket", "name");
        var temp = Files.createTempFile(tmp, "rev-", ".bin");

        when(request.projectId()).thenReturn(projectId);
        when(request.projectHistoryLocation()).thenReturn(blobLocation);
        when(minioFileDownloader.downloadFile(blobLocation)).thenReturn(CompletableFuture.completedFuture(temp));
        when(projectHistoryReplacer.replaceProjectHistory(temp)).thenReturn(CompletableFuture.completedFuture(null));

        try (MockedConstruction<BinaryOWLOntologyChangeLog> ignored =
                     mockConstruction(BinaryOWLOntologyChangeLog.class, (mock, ctx) ->
                             doAnswer(inv -> null).when(mock).readChanges(any(), eq(dataFactory), any()))) {

            ArgumentCaptor<Event> evtCaptor = ArgumentCaptor.forClass(Event.class);

            var resp = handler.execute(request, executionContext);
            assertThat(resp.requestStatus()).isEqualTo(ReplaceProjectHistoryRequestStatus.ACCEPTED);
            assertThat(resp.eventId()).isNotNull();

            verify(eventDispatcher, times(2)).dispatchEvent(evtCaptor.capture());

            var events = evtCaptor.getAllValues();
            assertThat(events.stream().anyMatch(e -> e instanceof ReplaceProjectHistorySucceededEvent)).isTrue();
            assertThat(events.stream().anyMatch(e -> e instanceof PackagedProjectChangeEvent)).isTrue();

            // Check correlation id is reused
            var successEvt = events.stream().filter(ReplaceProjectHistorySucceededEvent.class::isInstance)
                    .map(ReplaceProjectHistorySucceededEvent.class::cast).findFirst().orElseThrow();
            assertThat(successEvt.eventId()).isEqualTo(resp.eventId());
        }
    }

    @Test
    void emitsFailureDomainEvent_andNoPackagedEvent_onReplaceFailure(@TempDir Path tmp) throws Exception {
        var handler = newHandler();
        var projectId = mock(ProjectId.class);
        var blobLocation = blob("bucket", "name");
        var temp = Files.createTempFile(tmp, "rev-", ".bin");

        when(request.projectId()).thenReturn(projectId);
        when(request.projectHistoryLocation()).thenReturn(blobLocation);
        when(minioFileDownloader.downloadFile(blobLocation)).thenReturn(CompletableFuture.completedFuture(temp));
        when(projectHistoryReplacer.replaceProjectHistory(temp))
                .thenReturn(CompletableFuture.failedFuture(new RuntimeException("boom")));

        try (MockedConstruction<BinaryOWLOntologyChangeLog> ignored =
                     mockConstruction(BinaryOWLOntologyChangeLog.class, (mock, ctx) ->
                             doAnswer(inv -> null).when(mock).readChanges(any(), eq(dataFactory), any()))) {

            ArgumentCaptor<Event> evtCaptor = ArgumentCaptor.forClass(Event.class);

            var resp = handler.execute(request, executionContext);
            assertThat(resp.requestStatus()).isEqualTo(ReplaceProjectHistoryRequestStatus.ACCEPTED);

            verify(eventDispatcher, atLeastOnce()).dispatchEvent(evtCaptor.capture());

            var events = evtCaptor.getAllValues();
            assertThat(events.stream().anyMatch(e -> e instanceof ReplaceProjectHistoryFailedEvent)).isTrue();
            assertThat(events.stream().noneMatch(e -> e instanceof PackagedProjectChangeEvent)).isTrue();
        }
    }

    @Test
    void emitsFailureDomainEvent_onValidationFailure(@TempDir Path tmp) throws Exception {
        var handler = newHandler();
        var projectId = mock(ProjectId.class);
        var blobLocation = blob("bucket", "invalid");
        var temp = Files.createTempFile(tmp, "rev-", ".bin");

        when(request.projectId()).thenReturn(projectId);
        when(request.projectHistoryLocation()).thenReturn(blobLocation);
        when(minioFileDownloader.downloadFile(blobLocation)).thenReturn(CompletableFuture.completedFuture(temp));

        try (MockedConstruction<BinaryOWLOntologyChangeLog> ignored =
                     mockConstruction(BinaryOWLOntologyChangeLog.class, (mock, ctx) ->
                             doAnswer(inv -> { throw new RuntimeException("parse error"); })
                                     .when(mock).readChanges(any(), eq(dataFactory), any()))) {

            ArgumentCaptor<Event> evtCaptor = ArgumentCaptor.forClass(Event.class);

            var resp = handler.execute(request, executionContext);
            assertThat(resp.requestStatus()).isEqualTo(ReplaceProjectHistoryRequestStatus.ACCEPTED);

            verify(eventDispatcher, atLeastOnce()).dispatchEvent(evtCaptor.capture());

            var events = evtCaptor.getAllValues();
            assertThat(events.stream().anyMatch(e -> e instanceof ReplaceProjectHistoryFailedEvent)).isTrue();
            assertThat(events.stream().noneMatch(e -> e instanceof PackagedProjectChangeEvent)).isTrue();
        }
    }
}
