package edu.stanford.protege.webprotege.tag;

import edu.stanford.protege.webprotege.api.ActionExecutor;
import edu.stanford.protege.webprotege.ipc.CommandHandler;
import edu.stanford.protege.webprotege.ipc.ExecutionContext;
import edu.stanford.protege.webprotege.ipc.WebProtegeHandler;
import javax.annotation.Nonnull;
import reactor.core.publisher.Mono;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2021-08-20
 */
@WebProtegeHandler
public class GetEntityTagsCommandHandler implements CommandHandler<GetEntityTagsAction, GetEntityTagsResult> {

    private final ActionExecutor executor;

    public GetEntityTagsCommandHandler(ActionExecutor executor) {
        this.executor = executor;
    }

    @Nonnull
    @Override
    public String getChannelName() {
        return GetEntityTagsAction.CHANNEL;
    }

    @Override
    public Class<GetEntityTagsAction> getRequestClass() {
        return GetEntityTagsAction.class;
    }

    @Override
    public Mono<GetEntityTagsResult> handleRequest(GetEntityTagsAction request, ExecutionContext executionContext) {
        return executor.executeRequest(request, executionContext);
    }
}