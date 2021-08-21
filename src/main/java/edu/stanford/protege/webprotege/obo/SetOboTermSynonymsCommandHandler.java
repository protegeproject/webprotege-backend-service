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
public class SetOboTermSynonymsCommandHandler implements CommandHandler<SetOboTermSynonymsAction, SetOboTermSynonymsResult> {

    private final ActionExecutor executor;

    public SetOboTermSynonymsCommandHandler(ActionExecutor executor) {
        this.executor = executor;
    }

    @NotNull
    @Override
    public String getChannelName() {
        return SetOboTermSynonymsAction.CHANNEL;
    }

    @Override
    public Class<SetOboTermSynonymsAction> getRequestClass() {
        return SetOboTermSynonymsAction.class;
    }

    @Override
    public Mono<SetOboTermSynonymsResult> handleRequest(SetOboTermSynonymsAction request,
                                                        ExecutionContext executionContext) {
        return Mono.just(executor.execute(request, executionContext));
    }
}