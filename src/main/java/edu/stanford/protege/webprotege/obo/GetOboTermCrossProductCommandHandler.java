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
public class GetOboTermCrossProductCommandHandler implements CommandHandler<GetOboTermCrossProductAction, GetOboTermCrossProductResult> {

    private final ActionExecutor executor;

    public GetOboTermCrossProductCommandHandler(ActionExecutor executor) {
        this.executor = executor;
    }

    @NotNull
    @Override
    public String getChannelName() {
        return GetOboTermCrossProductAction.CHANNEL;
    }

    @Override
    public Class<GetOboTermCrossProductAction> getRequestClass() {
        return GetOboTermCrossProductAction.class;
    }

    @Override
    public Mono<GetOboTermCrossProductResult> handleRequest(GetOboTermCrossProductAction request,
                                                            ExecutionContext executionContext) {
        return Mono.just(executor.execute(request, executionContext));
    }
}