package edu.stanford.protege.webprotege.download;

import edu.stanford.protege.webprotege.ipc.CommandHandler;
import edu.stanford.protege.webprotege.ipc.ExecutionContext;
import edu.stanford.protege.webprotege.ipc.WebProtegeHandler;
import edu.stanford.protege.webprotege.project.ProjectManager;
import org.jetbrains.annotations.NotNull;
import reactor.core.publisher.Mono;

import javax.annotation.Nonnull;
import java.io.BufferedOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.concurrent.CompletableFuture;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2021-09-23
 */
@WebProtegeHandler
public class CreateDownloadCommandHandler implements CommandHandler<CreateDownloadRequest, CreateDownloadResponse> {

    @Nonnull
    private final ProjectDownloaderFactory projectDownloaderFactory;

    @Nonnull
    private final ProjectManager projectManager;

    public CreateDownloadCommandHandler(@Nonnull ProjectDownloaderFactory projectDownloaderFactory, ProjectManager projectManager) {
        this.projectDownloaderFactory = projectDownloaderFactory;
        this.projectManager = projectManager;
    }

    @NotNull
    @Override
    public String getChannelName() {
        return CreateDownloadRequest.CHANNEL;
    }

    @Override
    public Class<CreateDownloadRequest> getRequestClass() {
        return CreateDownloadRequest.class;
    }

    @Override
    public Mono<CreateDownloadResponse> handleRequest(CreateDownloadRequest request,
                                                      ExecutionContext executionContext) {

        var revisionManager = projectManager.getRevisionManager(request.projectId());
        var projectDownloader = projectDownloaderFactory.create(request.projectId(),
                                        request.fileName(),
                                        request.revisionNumber(),
                                        request.downloadFormat(),
                                        revisionManager);
        var future = new CompletableFuture<CreateDownloadResponse>();
        try(var os = new BufferedOutputStream(Files.newOutputStream(Path.of("/tmp", "download.project.zip")))) {
            projectDownloader.writeProject(os);
            future.complete(new CreateDownloadResponse(new DownloadDocumentId()));
        } catch (IOException e) {
            e.printStackTrace();
        }
        return Mono.fromFuture(future);
    }
}
