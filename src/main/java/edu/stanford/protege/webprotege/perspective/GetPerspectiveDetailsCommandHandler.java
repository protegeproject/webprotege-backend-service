package edu.stanford.protege.webprotege.perspective;

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
public class GetPerspectiveDetailsCommandHandler implements CommandHandler<GetPerspectiveDetailsAction, GetPerspectiveDetailsResult> {

    private final ActionExecutor executor;

    public GetPerspectiveDetailsCommandHandler(ActionExecutor executor) {
        this.executor = executor;
    }

    @NotNull
    @Override
    public String getChannelName() {
        return GetPerspectiveDetailsAction.CHANNEL;
    }

    @Override
    public Class<GetPerspectiveDetailsAction> getRequestClass() {
        return GetPerspectiveDetailsAction.class;
    }

    @Override
    public Mono<GetPerspectiveDetailsResult> handleRequest(GetPerspectiveDetailsAction request,
                                                           ExecutionContext executionContext) {
        return Mono.just(executor.execute(request, executionContext));
    }
}