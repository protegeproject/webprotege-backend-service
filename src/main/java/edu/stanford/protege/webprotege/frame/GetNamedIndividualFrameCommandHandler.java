package edu.stanford.protege.webprotege.frame;

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
public class GetNamedIndividualFrameCommandHandler implements CommandHandler<GetNamedIndividualFrameAction, GetNamedIndividualFrameResult> {

    private final ActionExecutor executor;

    public GetNamedIndividualFrameCommandHandler(ActionExecutor executor) {
        this.executor = executor;
    }

    @Nonnull
    @Override
    public String getChannelName() {
        return GetNamedIndividualFrameAction.CHANNEL;
    }

    @Override
    public Class<GetNamedIndividualFrameAction> getRequestClass() {
        return GetNamedIndividualFrameAction.class;
    }

    @Override
    public Mono<GetNamedIndividualFrameResult> handleRequest(GetNamedIndividualFrameAction request,
                                                             ExecutionContext executionContext) {
        return executor.executeRequest(request, executionContext);
    }
}