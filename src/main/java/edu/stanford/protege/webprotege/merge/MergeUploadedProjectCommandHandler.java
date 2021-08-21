package edu.stanford.protege.webprotege.merge;

import edu.stanford.protege.webprotege.api.ActionExecutor;
import edu.stanford.protege.webprotege.ipc.CommandHandler;
import edu.stanford.protege.webprotege.ipc.ExecutionContext;
import edu.stanford.protege.webprotege.ipc.WebProtegeHandler;
import org.jetbrains.annotations.NotNull;
import reactor.core.publisher.Mono;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2021-08-21
 */
@WebProtegeHandler
public class MergeUploadedProjectCommandHandler implements CommandHandler<MergeUploadedProjectAction, MergeUploadedProjectResult> {

    private final ActionExecutor executor;

    public MergeUploadedProjectCommandHandler(ActionExecutor executor) {
        this.executor = executor;
    }

    @NotNull
    @Override
    public String getChannelName() {
        return MergeUploadedProjectAction.CHANNEL;
    }

    @Override
    public Class<MergeUploadedProjectAction> getRequestClass() {
        return MergeUploadedProjectAction.class;
    }

    @Override
    public Mono<MergeUploadedProjectResult> handleRequest(MergeUploadedProjectAction request,
                                                          ExecutionContext executionContext) {
        return Mono.just(executor.execute(request, executionContext));
    }
}