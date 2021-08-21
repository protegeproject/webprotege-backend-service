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
public class SetOboTermCrossProductCommandHandler implements CommandHandler<SetOboTermCrossProductAction, SetOboTermCrossProductResult> {

    private final ActionExecutor executor;

    public SetOboTermCrossProductCommandHandler(ActionExecutor executor) {
        this.executor = executor;
    }

    @NotNull
    @Override
    public String getChannelName() {
        return SetOboTermCrossProductAction.CHANNEL;
    }

    @Override
    public Class<SetOboTermCrossProductAction> getRequestClass() {
        return SetOboTermCrossProductAction.class;
    }

    @Override
    public Mono<SetOboTermCrossProductResult> handleRequest(SetOboTermCrossProductAction request,
                                                            ExecutionContext executionContext) {
        return Mono.just(executor.execute(request, executionContext));
    }
}