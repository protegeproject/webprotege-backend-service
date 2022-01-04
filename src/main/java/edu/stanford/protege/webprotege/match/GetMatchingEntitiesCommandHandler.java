package edu.stanford.protege.webprotege.match;

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
public class GetMatchingEntitiesCommandHandler implements CommandHandler<GetMatchingEntitiesAction, GetMatchingEntitiesResult> {

    private final ActionExecutor executor;

    public GetMatchingEntitiesCommandHandler(ActionExecutor executor) {
        this.executor = executor;
    }

    @Nonnull
    @Override
    public String getChannelName() {
        return GetMatchingEntitiesAction.CHANNEL;
    }

    @Override
    public Class<GetMatchingEntitiesAction> getRequestClass() {
        return GetMatchingEntitiesAction.class;
    }

    @Override
    public Mono<GetMatchingEntitiesResult> handleRequest(GetMatchingEntitiesAction request,
                                                         ExecutionContext executionContext) {
        return executor.executeRequest(request, executionContext);
    }
}