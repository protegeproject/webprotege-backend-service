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
 * 2021-08-20
 */
@WebProtegeHandler
public class ComputeProjectMergeCommandHandler implements CommandHandler<ComputeProjectMergeAction, ComputeProjectMergeResult> {

    private final ActionExecutor executor;

    public ComputeProjectMergeCommandHandler(ActionExecutor executor) {
        this.executor = executor;
    }

    @NotNull
    @Override
    public String getChannelName() {
        return ComputeProjectMergeAction.CHANNEL;
    }

    @Override
    public Class<ComputeProjectMergeAction> getRequestClass() {
        return ComputeProjectMergeAction.class;
    }

    @Override
    public Mono<ComputeProjectMergeResult> handleRequest(ComputeProjectMergeAction request,
                                                         ExecutionContext executionContext) {
        return executor.executeRequest(request, executionContext);
    }
}