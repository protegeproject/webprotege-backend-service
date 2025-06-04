package edu.stanford.protege.webprotege.issues;

import edu.stanford.protege.webprotege.api.ActionExecutor;
import edu.stanford.protege.webprotege.ipc.CommandHandler;
import edu.stanford.protege.webprotege.ipc.ExecutionContext;
import edu.stanford.protege.webprotege.ipc.WebProtegeHandler;
import jakarta.annotation.Nonnull;
import jakarta.inject.Inject;
import reactor.core.publisher.Mono;


@WebProtegeHandler
public class GetEntityEarliestCommentTimestampCommandHandler
        implements CommandHandler<GetEntityEarliestCommentTimestampAction, GetEntityEarliestCommentTimestampResult> {

    private final ActionExecutor executor;

    @Inject
    public GetEntityEarliestCommentTimestampCommandHandler(ActionExecutor executor) {
        this.executor = executor;
    }

    @Nonnull
    @Override
    public String getChannelName() {
        return GetEntityEarliestCommentTimestampAction.CHANNEL;
    }

    @Override
    public Class<GetEntityEarliestCommentTimestampAction> getRequestClass() {
        return GetEntityEarliestCommentTimestampAction.class;
    }

    @Override
    public Mono<GetEntityEarliestCommentTimestampResult> handleRequest(
            GetEntityEarliestCommentTimestampAction request,
            ExecutionContext executionContext) {
        return executor.executeRequest(request, executionContext);
    }
}
