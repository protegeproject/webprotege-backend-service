package edu.stanford.protege.webprotege.issues;

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
public class SetDiscussionThreadStatusCommandHandler implements CommandHandler<SetDiscussionThreadStatusAction, SetDiscussionThreadStatusResult> {

    private final ActionExecutor executor;

    public SetDiscussionThreadStatusCommandHandler(ActionExecutor executor) {
        this.executor = executor;
    }

    @Nonnull
    @Override
    public String getChannelName() {
        return SetDiscussionThreadStatusAction.CHANNEL;
    }

    @Override
    public Class<SetDiscussionThreadStatusAction> getRequestClass() {
        return SetDiscussionThreadStatusAction.class;
    }

    @Override
    public Mono<SetDiscussionThreadStatusResult> handleRequest(SetDiscussionThreadStatusAction request,
                                                               ExecutionContext executionContext) {
        return executor.executeRequest(request, executionContext);
    }
}