package edu.stanford.protege.webprotege.issues;

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
public class GetCommentedEntitiesCommandHandler implements CommandHandler<GetCommentedEntitiesAction, GetCommentedEntitiesResult> {

    private final ActionExecutor executor;

    public GetCommentedEntitiesCommandHandler(ActionExecutor executor) {
        this.executor = executor;
    }

    @NotNull
    @Override
    public String getChannelName() {
        return GetCommentedEntitiesAction.CHANNEL;
    }

    @Override
    public Class<GetCommentedEntitiesAction> getRequestClass() {
        return GetCommentedEntitiesAction.class;
    }

    @Override
    public Mono<GetCommentedEntitiesResult> handleRequest(GetCommentedEntitiesAction request,
                                                          ExecutionContext executionContext) {
        return executor.executeRequest(request, executionContext);
    }
}