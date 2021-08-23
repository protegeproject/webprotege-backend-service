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
 * 2021-08-21
 */
@WebProtegeHandler
public class SetOboTermRelationshipsCommandHandler implements CommandHandler<SetOboTermRelationshipsAction, SetOboTermRelationshipsResult> {

    private final ActionExecutor executor;

    public SetOboTermRelationshipsCommandHandler(ActionExecutor executor) {
        this.executor = executor;
    }

    @NotNull
    @Override
    public String getChannelName() {
        return SetOboTermRelationshipsAction.CHANNEL;
    }

    @Override
    public Class<SetOboTermRelationshipsAction> getRequestClass() {
        return SetOboTermRelationshipsAction.class;
    }

    @Override
    public Mono<SetOboTermRelationshipsResult> handleRequest(SetOboTermRelationshipsAction request,
                                                             ExecutionContext executionContext) {
        return executor.executeRequest(request, executionContext);
    }
}