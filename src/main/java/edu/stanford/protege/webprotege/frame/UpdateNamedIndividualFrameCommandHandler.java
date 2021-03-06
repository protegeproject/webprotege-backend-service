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
 * 2021-08-21
 */
@WebProtegeHandler
public class UpdateNamedIndividualFrameCommandHandler implements CommandHandler<UpdateNamedIndividualFrameAction, UpdateNamedIndividualFrameResult> {

    private final ActionExecutor executor;

    public UpdateNamedIndividualFrameCommandHandler(ActionExecutor executor) {
        this.executor = executor;
    }

    @Nonnull
    @Override
    public String getChannelName() {
        return UpdateNamedIndividualFrameAction.CHANNEL;
    }

    @Override
    public Class<UpdateNamedIndividualFrameAction> getRequestClass() {
        return UpdateNamedIndividualFrameAction.class;
    }

    @Override
    public Mono<UpdateNamedIndividualFrameResult> handleRequest(UpdateNamedIndividualFrameAction request,
                                                                ExecutionContext executionContext) {
        return executor.executeRequest(request, executionContext);
    }
}