package edu.stanford.protege.webprotege.tag;

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
public class GetProjectTagsCommandHandler implements CommandHandler<GetProjectTagsAction, GetProjectTagsResult> {

    private final ActionExecutor executor;

    public GetProjectTagsCommandHandler(ActionExecutor executor) {
        this.executor = executor;
    }

    @NotNull
    @Override
    public String getChannelName() {
        return GetProjectTagsAction.CHANNEL;
    }

    @Override
    public Class<GetProjectTagsAction> getRequestClass() {
        return GetProjectTagsAction.class;
    }

    @Override
    public Mono<GetProjectTagsResult> handleRequest(GetProjectTagsAction request, ExecutionContext executionContext) {
        return Mono.just(executor.execute(request, executionContext));
    }
}