package edu.stanford.protege.webprotege.obo;

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
public class GetOboTermRelationshipsCommandHandler implements CommandHandler<GetOboTermRelationshipsAction, GetOboTermRelationshipsResult> {

    private final ActionExecutor executor;

    public GetOboTermRelationshipsCommandHandler(ActionExecutor executor) {
        this.executor = executor;
    }

    @NotNull
    @Override
    public String getChannelName() {
        return GetOboTermRelationshipsAction.CHANNEL;
    }

    @Override
    public Class<GetOboTermRelationshipsAction> getRequestClass() {
        return GetOboTermRelationshipsAction.class;
    }

    @Override
    public Mono<GetOboTermRelationshipsResult> handleRequest(GetOboTermRelationshipsAction request,
                                                             ExecutionContext executionContext) {
        return executor.executeRequest(request, executionContext);
    }
}