package edu.stanford.protege.webprotege.obo;

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
public class GetOboTermSynonymsCommandHandler implements CommandHandler<GetOboTermSynonymsAction, GetOboTermSynonymsResult> {

    private final ActionExecutor executor;

    public GetOboTermSynonymsCommandHandler(ActionExecutor executor) {
        this.executor = executor;
    }

    @Nonnull
    @Override
    public String getChannelName() {
        return GetOboTermSynonymsAction.CHANNEL;
    }

    @Override
    public Class<GetOboTermSynonymsAction> getRequestClass() {
        return GetOboTermSynonymsAction.class;
    }

    @Override
    public Mono<GetOboTermSynonymsResult> handleRequest(GetOboTermSynonymsAction request,
                                                        ExecutionContext executionContext) {
        return executor.executeRequest(request, executionContext);
    }
}