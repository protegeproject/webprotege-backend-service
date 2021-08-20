package edu.stanford.protege.webprotege.project;

import edu.stanford.protege.webprotege.api.ActionExecutor;
import edu.stanford.protege.webprotege.ipc.CommandHandler;
import edu.stanford.protege.webprotege.ipc.ExecutionContext;
import edu.stanford.protege.webprotege.ipc.WebProtegeHandler;
import edu.stanford.protege.webprotege.project.MoveProjectsToTrashAction;
import edu.stanford.protege.webprotege.project.MoveProjectsToTrashResult;
import org.jetbrains.annotations.NotNull;
import reactor.core.publisher.Mono;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2021-08-19
 */
@WebProtegeHandler
public class MoveProjectsToTrashCommandHandler implements CommandHandler<MoveProjectsToTrashAction, MoveProjectsToTrashResult> {

    private final ActionExecutor executor;

    public MoveProjectsToTrashCommandHandler(ActionExecutor executor) {
        this.executor = executor;
    }

    @NotNull
    @Override
    public String getChannelName() {
        return MoveProjectsToTrashAction.CHANNEL;
    }

    @Override
    public Class<MoveProjectsToTrashAction> getRequestClass() {
        return MoveProjectsToTrashAction.class;
    }

    @Override
    public Mono<MoveProjectsToTrashResult> handleRequest(MoveProjectsToTrashAction request,
                                                         ExecutionContext executionContext) {
        return Mono.just(executor.execute(request, executionContext));
    }
}