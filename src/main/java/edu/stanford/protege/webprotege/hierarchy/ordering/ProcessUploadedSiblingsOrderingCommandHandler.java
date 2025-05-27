package edu.stanford.protege.webprotege.hierarchy.ordering;

import edu.stanford.protege.webprotege.api.ActionExecutor;
import edu.stanford.protege.webprotege.ipc.*;
import reactor.core.publisher.Mono;

import javax.annotation.Nonnull;


@WebProtegeHandler
public class ProcessUploadedSiblingsOrderingCommandHandler implements CommandHandler<ProcessUploadedSiblingsOrderingAction, ProcessUploadedSiblingsOrderingResponse> {

    private final ActionExecutor executor;

    public ProcessUploadedSiblingsOrderingCommandHandler(ActionExecutor executor) {
        this.executor = executor;
    }

    @Nonnull
    @Override
    public String getChannelName() {
        return ProcessUploadedSiblingsOrderingAction.CHANNEL;
    }

    @Override
    public Class<ProcessUploadedSiblingsOrderingAction> getRequestClass() {
        return ProcessUploadedSiblingsOrderingAction.class;
    }

    @Override
    public Mono<ProcessUploadedSiblingsOrderingResponse> handleRequest(ProcessUploadedSiblingsOrderingAction request,
                                                               ExecutionContext executionContext) {
        return executor.executeRequest(request, executionContext);
    }
}