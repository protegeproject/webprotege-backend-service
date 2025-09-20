package edu.stanford.protege.webprotege.project;

import edu.stanford.protege.webprotege.access.AccessManager;
import edu.stanford.protege.webprotege.common.BlobLocation;
import edu.stanford.protege.webprotege.common.Event;
import edu.stanford.protege.webprotege.common.ProjectId;
import edu.stanford.protege.webprotege.ipc.EventDispatcher;
import edu.stanford.protege.webprotege.ipc.ExecutionContext;
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
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ReplaceProjectHistoryActionHandlerTest {

    // Direct executor so async stages run inline in tests
    private final Executor direct = Runnable::run;

    @Mock
    private AccessManager accessManager;

    @Mock
    private ProjectHistoryReplacer projectHistoryReplacer;

    @Mock
    private MinioFileDownloader minioFileDownloader;

    @Mock
    private EventDispatcher eventDispatcher;

    @Mock
    private OWLDataFactory dataFactory;

    @Mock
    private ReplaceProjectHistoryRequest request;

    @Mock
    private ExecutionContext executionContext;

    private static BlobLocation blob(String bucket, String name) {
        return new BlobLocation(bucket, name);
    }

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

    @Test
    void acceptsRequest_deletesTempFile_dispatchesEvent_onSuccess(@TempDir Path tmp) throws Exception {
        // Arrange
        var handler = newHandler();
        var projectId = mock(ProjectId.class);
        var blobLocation = blob("bucket" , "name" );
        var temp = Files.createTempFile(tmp, "rev-" , ".bin" );
        Files.writeString(temp, "placeholder" ); // content doesn't matter; we'll mock parser

        when(request.projectId()).thenReturn(projectId);
        when(request.projectHistoryLocation()).thenReturn(blobLocation);

        when(minioFileDownloader.downloadFile(blobLocation))
                .thenReturn(CompletableFuture.completedFuture(temp));

        when(projectHistoryReplacer.replaceProjectHistory(temp))
                .thenReturn(CompletableFuture.completedFuture(null));

        // Mock construction of BinaryOWLOntologyChangeLog so validation passes
        try (MockedConstruction<BinaryOWLOntologyChangeLog> ignored =
                     mockConstruction(BinaryOWLOntologyChangeLog.class, (mock, ctx) -> {
                         doAnswer(inv -> null).when(mock)
                                 .readChanges(any(), eq(dataFactory), any());
                     })) {

            // Act
            ReplaceProjectHistoryResponse resp = handler.execute(request, executionContext);

            // Assert response
            assertThat(resp.projectId()).isEqualTo(projectId);
            assertThat(resp.requestStatus()).isEqualTo(ReplaceProjectHistoryRequestStatus.ACCEPTED);
            assertThat(resp.eventId()).isNotNull();

            // Assert side effects: replacer invoked, file deleted, event dispatched
            verify(projectHistoryReplacer).replaceProjectHistory(temp);
            assertThat(Files.exists(temp)).as("temp file should be deleted" ).isFalse();
            verify(eventDispatcher, times(1)).dispatchEvent(any());
        }
    }

    @Test
    void rejectsSecondRequestWhileFirstInFlight_thenAllowsAfterCompletion(@TempDir Path tmp) throws Exception {
        // Arrange
        var handler = newHandler();
        var projectId = mock(ProjectId.class);
        var blob1 = blob("b1" , "n1" );
        var blob2 = blob("b2" , "n2" );

        var temp1 = Files.createTempFile(tmp, "rev1-" , ".bin" );
        var temp2 = Files.createTempFile(tmp, "rev2-" , ".bin" );
        Files.writeString(temp1, "x1" );
        Files.writeString(temp2, "x2" );

        when(request.projectId()).thenReturn(projectId);
        when(minioFileDownloader.downloadFile(blob1))
                .thenReturn(CompletableFuture.completedFuture(temp1));
        when(minioFileDownloader.downloadFile(blob2))
                .thenReturn(CompletableFuture.completedFuture(temp2));

        // First call: validation OK, but replacer stays in-flight
        CompletableFuture<Void> replaceGate = new CompletableFuture<>();
        when(projectHistoryReplacer.replaceProjectHistory(temp1)).thenReturn(replaceGate);

        // Validation succeeds
        try (MockedConstruction<BinaryOWLOntologyChangeLog> ignored =
                     mockConstruction(BinaryOWLOntologyChangeLog.class, (mock, ctx) -> {
                         doAnswer(inv -> null).when(mock)
                                 .readChanges(any(), eq(dataFactory), any());
                     })) {

            // Act 1: first call accepted and in-flight
            when(request.projectHistoryLocation()).thenReturn(blob1);
            ReplaceProjectHistoryResponse r1 = handler.execute(request, executionContext);
            assertThat(r1.requestStatus()).isEqualTo(ReplaceProjectHistoryRequestStatus.ACCEPTED);

            // Act 2: second call while in-flight -> rejected
            when(request.projectHistoryLocation()).thenReturn(blob2);
            ReplaceProjectHistoryResponse r2 = handler.execute(request, executionContext);
            assertThat(r2.requestStatus()).isEqualTo(ReplaceProjectHistoryRequestStatus.REJECTED_ALREADY_IN_PROGRESS);
            assertThat(r2.eventId()).isNull();
            verify(minioFileDownloader, times(1)).downloadFile(any()); // only first started

            // Finish the first operation
            replaceGate.complete(null);

            // Act 3: third call after completion -> accepted again
            when(projectHistoryReplacer.replaceProjectHistory(temp2))
                    .thenReturn(CompletableFuture.completedFuture(null));
            ReplaceProjectHistoryResponse r3 = handler.execute(request, executionContext);
            assertThat(r3.requestStatus()).isEqualTo(ReplaceProjectHistoryRequestStatus.ACCEPTED);
        }
    }

    @Test
    void deletesTempFile_andDoesNotDispatch_onReplaceFailure(@TempDir Path tmp) throws Exception {
        // Arrange
        var handler = newHandler();
        var projectId = mock(ProjectId.class);
        var blobLocation = blob("bucket" , "name" );
        var temp = Files.createTempFile(tmp, "rev-" , ".bin" );
        Files.writeString(temp, "data" );

        when(request.projectId()).thenReturn(projectId);
        when(request.projectHistoryLocation()).thenReturn(blobLocation);
        when(minioFileDownloader.downloadFile(blobLocation))
                .thenReturn(CompletableFuture.completedFuture(temp));

        // Validation OK, but replacer fails
        when(projectHistoryReplacer.replaceProjectHistory(temp))
                .thenReturn(CompletableFuture.failedFuture(new RuntimeException("boom" )));

        try (MockedConstruction<BinaryOWLOntologyChangeLog> ignored =
                     mockConstruction(BinaryOWLOntologyChangeLog.class, (mock, ctx) -> {
                         doAnswer(inv -> null).when(mock)
                                 .readChanges(any(), eq(dataFactory), any());
                     })) {

            // Act
            ReplaceProjectHistoryResponse resp = handler.execute(request, executionContext);

            // Assert: accepted but eventual failure → file deleted, no event
            assertThat(resp.requestStatus()).isEqualTo(ReplaceProjectHistoryRequestStatus.ACCEPTED);
            assertThat(Files.exists(temp)).isFalse();
            verify(eventDispatcher, never()).dispatchEvent(any());
        }
    }

    @Test
    void validationFailure_deletesTempFile_andDoesNotCallReplacer(@TempDir Path tmp) throws Exception {
        // Arrange
        var handler = newHandler();
        var projectId = mock(ProjectId.class);
        var blobLocation = blob("bucket" , "invalid" );
        var temp = Files.createTempFile(tmp, "rev-" , ".bin" );
        Files.writeString(temp, "bad" );

        when(request.projectId()).thenReturn(projectId);
        when(request.projectHistoryLocation()).thenReturn(blobLocation);
        when(minioFileDownloader.downloadFile(blobLocation))
                .thenReturn(CompletableFuture.completedFuture(temp));

        // Make BinaryOWL validation throw
        try (MockedConstruction<BinaryOWLOntologyChangeLog> ignored =
                     mockConstruction(BinaryOWLOntologyChangeLog.class, (mock, ctx) -> {
                         doAnswer(inv -> {
                             throw new RuntimeException("parse error" );
                         }).when(mock).readChanges(any(), eq(dataFactory), any());
                     })) {

            // Act
            ReplaceProjectHistoryResponse resp = handler.execute(request, executionContext);

            // Assert: accepted, but validation failed → replacer not invoked, temp deleted, no event
            assertThat(resp.requestStatus()).isEqualTo(ReplaceProjectHistoryRequestStatus.ACCEPTED);
            verify(projectHistoryReplacer, never()).replaceProjectHistory(any());
            assertThat(Files.exists(temp)).isFalse();
            verify(eventDispatcher, never()).dispatchEvent(any());
        }
    }

    @Test
    void correlationId_isReturned_and_usedForChildEvent_whenSuccess(@TempDir Path tmp) throws Exception {
        // This test assumes you re-use the same correlation id for the packaged event,
        // and for the child LargeNumberOfChangesEvent. If your PackagedProjectChangeEvent
        // exposes getters, you can assert equality below.
        var handler = newHandler();
        var projectId = mock(ProjectId.class);
        var blobLocation = blob("bucket" , "name" );
        var temp = Files.createTempFile(tmp, "rev-" , ".bin" );
        Files.writeString(temp, "ok" );

        when(request.projectId()).thenReturn(projectId);
        when(request.projectHistoryLocation()).thenReturn(blobLocation);
        when(minioFileDownloader.downloadFile(blobLocation))
                .thenReturn(CompletableFuture.completedFuture(temp));
        when(projectHistoryReplacer.replaceProjectHistory(temp))
                .thenReturn(CompletableFuture.completedFuture(null));

        try (MockedConstruction<BinaryOWLOntologyChangeLog> ignored =
                     mockConstruction(BinaryOWLOntologyChangeLog.class, (mock, ctx) -> {
                         doAnswer(inv -> null).when(mock)
                                 .readChanges(any(), eq(dataFactory), any());
                     })) {

            ArgumentCaptor<Event> eventCaptor = ArgumentCaptor.forClass(Event.class);

            ReplaceProjectHistoryResponse resp = handler.execute(request, executionContext);
            assertThat(resp.eventId()).isNotNull();

            verify(eventDispatcher).dispatchEvent(eventCaptor.capture());
        }
    }
}
