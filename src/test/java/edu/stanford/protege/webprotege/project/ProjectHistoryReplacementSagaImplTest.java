package edu.stanford.protege.webprotege.project;

import edu.stanford.protege.webprotege.common.BlobLocation;
import edu.stanford.protege.webprotege.common.Event;
import edu.stanford.protege.webprotege.common.ProjectId;
import edu.stanford.protege.webprotege.ipc.EventDispatcher;
import edu.stanford.protege.webprotege.storage.MinioFileDownloader;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.api.io.TempDir;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.MockedConstruction;
import org.mockito.junit.jupiter.MockitoExtension;
import org.semanticweb.binaryowl.BinaryOWLOntologyChangeLog;
import org.semanticweb.owlapi.model.OWLDataFactory;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ProjectHistoryReplacementSagaImplTest {

    private final Executor direct = Runnable::run;

    @Mock
    private ProjectHistoryReplacer projectHistoryReplacer;

    @Mock
    private MinioFileDownloader minioFileDownloader;

    @Mock
    private EventDispatcher eventDispatcher;

    @Mock
    private OWLDataFactory dataFactory;

    private static BlobLocation blob(String b, String n) {
        return new BlobLocation(b, n);
    }

    private ProjectHistoryReplacementSagaImpl newSaga() {
        return new ProjectHistoryReplacementSagaImpl(
                projectHistoryReplacer,
                minioFileDownloader,
                eventDispatcher,
                dataFactory,
                direct
        );
    }

    @Test
    void shouldEmitStartedAndSuccessDomainEventAndPackagedEvent_onSuccess(@TempDir Path tmp) throws Exception {
        var saga = newSaga();
        var projectId = ProjectId.generate();
        var blobLocation = blob("bucket", "name");
        var temp = Files.createTempFile(tmp, "rev-", ".bin");

        var request = new ReplaceProjectHistoryRequest(projectId, blobLocation);

        when(minioFileDownloader.downloadFile(blobLocation)).thenReturn(CompletableFuture.completedFuture(temp));
        when(projectHistoryReplacer.replaceProjectHistory(eq(temp), any(ProjectHistoryReplacementOperationId.class))).thenReturn(CompletableFuture.completedFuture(null));

        try (MockedConstruction<BinaryOWLOntologyChangeLog> ignored =
                     mockConstruction(BinaryOWLOntologyChangeLog.class, (mock, ctx) ->
                             doAnswer(inv -> null).when(mock).readChanges(any(), eq(dataFactory), any()))) {

            ArgumentCaptor<Event> evtCaptor = ArgumentCaptor.forClass(Event.class);

            var resp = saga.run(request);
            assertThat(resp.requestStatus()).isEqualTo(ReplaceProjectHistoryRequestStatus.ACCEPTED);
            assertThat(resp.operationId()).isNotNull();

            verify(eventDispatcher, times(3)).dispatchEvent(evtCaptor.capture());

            var events = evtCaptor.getAllValues();
            assertThat(events.stream().anyMatch(e -> e instanceof ProjectHistoryReplacementStartedEvent)).isTrue();
            assertThat(events.stream().anyMatch(e -> e instanceof ProjectHistoryReplacementSucceededEvent)).isTrue();
            assertThat(events.stream().anyMatch(e -> e instanceof PackagedProjectChangeEvent)).isTrue();

            var successEvt = events.stream()
                    .filter(ProjectHistoryReplacementSucceededEvent.class::isInstance)
                    .map(ProjectHistoryReplacementSucceededEvent.class::cast)
                    .findFirst().orElseThrow();
            assertThat(successEvt.operationId()).isEqualTo(resp.operationId());
        }
    }

    @Test
    void shouldEmitStartedAndFailureDomainEventAndNoPackagedEvent_onReplaceFailure(@TempDir Path tmp) throws Exception {
        var saga = newSaga();
        var projectId = ProjectId.generate();
        var blobLocation = blob("bucket", "name");
        var temp = Files.createTempFile(tmp, "rev-", ".bin");

        var request = new ReplaceProjectHistoryRequest(projectId, blobLocation);

        when(minioFileDownloader.downloadFile(blobLocation)).thenReturn(CompletableFuture.completedFuture(temp));
        when(projectHistoryReplacer.replaceProjectHistory(eq(temp), any(ProjectHistoryReplacementOperationId.class)))
                .thenReturn(CompletableFuture.failedFuture(new RuntimeException("boom")));

        try (MockedConstruction<BinaryOWLOntologyChangeLog> ignored =
                     mockConstruction(BinaryOWLOntologyChangeLog.class, (mock, ctx) ->
                             doAnswer(inv -> null).when(mock).readChanges(any(), eq(dataFactory), any()))) {

            ArgumentCaptor<Event> evtCaptor = ArgumentCaptor.forClass(Event.class);

            var resp = saga.run(request);
            assertThat(resp.requestStatus()).isEqualTo(ReplaceProjectHistoryRequestStatus.ACCEPTED);

            verify(eventDispatcher, atLeast(2)).dispatchEvent(evtCaptor.capture());

            var events = evtCaptor.getAllValues();
            assertThat(events.stream().anyMatch(e -> e instanceof ProjectHistoryReplacementStartedEvent)).isTrue();
            assertThat(events.stream().anyMatch(e -> e instanceof ProjectHistoryReplacementFailedEvent)).isTrue();
            assertThat(events.stream().noneMatch(e -> e instanceof PackagedProjectChangeEvent)).isTrue();
        }
    }

    @Test
    void shouldEmitStartedAndFailureDomainEventAndNoPackagedEvent_onValidationFailure(@TempDir Path tmp) throws Exception {
        var saga = newSaga();
        var projectId = ProjectId.generate();
        var blobLocation = blob("bucket", "invalid");
        var temp = Files.createTempFile(tmp, "rev-", ".bin");

        var request = new ReplaceProjectHistoryRequest(projectId, blobLocation);

        when(minioFileDownloader.downloadFile(blobLocation)).thenReturn(CompletableFuture.completedFuture(temp));

        try (MockedConstruction<BinaryOWLOntologyChangeLog> ignored =
                     mockConstruction(BinaryOWLOntologyChangeLog.class, (mock, ctx) ->
                             doAnswer(inv -> {
                                 throw new RuntimeException("parse error");
                             }).when(mock).readChanges(any(), eq(dataFactory), any()))) {

            ArgumentCaptor<Event> evtCaptor = ArgumentCaptor.forClass(Event.class);

            var resp = saga.run(request);
            assertThat(resp.requestStatus()).isEqualTo(ReplaceProjectHistoryRequestStatus.ACCEPTED);

            verify(eventDispatcher, atLeast(2)).dispatchEvent(evtCaptor.capture());

            var events = evtCaptor.getAllValues();
            assertThat(events.stream().anyMatch(e -> e instanceof ProjectHistoryReplacementStartedEvent)).isTrue();
            assertThat(events.stream().anyMatch(e -> e instanceof ProjectHistoryReplacementFailedEvent)).isTrue();
            assertThat(events.stream().noneMatch(e -> e instanceof PackagedProjectChangeEvent)).isTrue();
        }
    }

    @Test
    void shouldRejectWhenAlreadyInProgress() throws Exception {
        var saga = newSaga();
        var projectId = ProjectId.generate();
        var blobLocation = blob("bucket", "name");
        var temp = Files.createTempFile("rev-", ".bin");

        var request = new ReplaceProjectHistoryRequest(projectId, blobLocation);

        var never = new CompletableFuture<Path>();
        when(minioFileDownloader.downloadFile(blobLocation)).thenReturn(never);

        var first = saga.run(request);
        var second = saga.run(request);

        assertThat(first.requestStatus()).isEqualTo(ReplaceProjectHistoryRequestStatus.ACCEPTED);
        assertThat(second.requestStatus()).isEqualTo(ReplaceProjectHistoryRequestStatus.REJECTED_ALREADY_IN_PROGRESS);
        assertThat(second.operationId()).isNull();
    }
}
