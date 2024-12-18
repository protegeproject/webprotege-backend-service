package edu.stanford.protege.webprotege.project;

import edu.stanford.protege.webprotege.api.ActionExecutor;
import edu.stanford.protege.webprotege.ipc.CommandHandler;
import edu.stanford.protege.webprotege.ipc.ExecutionContext;
import edu.stanford.protege.webprotege.ipc.WebProtegeHandler;
import org.jetbrains.annotations.NotNull;
import reactor.core.publisher.Mono;

@WebProtegeHandler
public class CreateBackupOwlFileCommandHandler implements CommandHandler<CreateBackupOwlFileAction, CreateBackupOwlFileResponse> {
    private final ActionExecutor executor;

    public CreateBackupOwlFileCommandHandler(ActionExecutor executor) {
        this.executor = executor;
    }

    @NotNull
    @Override
    public String getChannelName() {
        return CreateBackupOwlFileAction.CHANNEL;
    }

    @Override
    public Class<CreateBackupOwlFileAction> getRequestClass() {
        return CreateBackupOwlFileAction.class;
    }

    @Override
    public Mono<CreateBackupOwlFileResponse> handleRequest(CreateBackupOwlFileAction request, ExecutionContext executionContext) {
        var result = executor.execute(request, new ExecutionContext(executionContext.userId(), executionContext.jwt()));
        return Mono.just(result);
    }
}
