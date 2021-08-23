package edu.stanford.protege.webprotege.frame;

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
public class GetOntologyFramesCommandHandler implements CommandHandler<GetOntologyFramesAction, GetOntologyFramesResult> {

    private final ActionExecutor executor;

    public GetOntologyFramesCommandHandler(ActionExecutor executor) {
        this.executor = executor;
    }

    @NotNull
    @Override
    public String getChannelName() {
        return GetOntologyFramesAction.CHANNEL;
    }

    @Override
    public Class<GetOntologyFramesAction> getRequestClass() {
        return GetOntologyFramesAction.class;
    }

    @Override
    public Mono<GetOntologyFramesResult> handleRequest(GetOntologyFramesAction request,
                                                       ExecutionContext executionContext) {
        return executor.executeRequest(request, executionContext);
    }
}