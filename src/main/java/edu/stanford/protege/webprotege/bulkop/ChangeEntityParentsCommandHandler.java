package edu.stanford.protege.webprotege.bulkop;

import edu.stanford.protege.webprotege.api.ActionExecutor;
import edu.stanford.protege.webprotege.ipc.CommandHandler;
import edu.stanford.protege.webprotege.ipc.ExecutionContext;
import edu.stanford.protege.webprotege.ipc.WebProtegeHandler;
import reactor.core.publisher.Mono;

import javax.annotation.Nonnull;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2021-08-21
 */
@WebProtegeHandler
public class ChangeEntityParentsCommandHandler implements CommandHandler<ChangeEntityParentsAction, ChangeEntityParentsResult> {

    private final ActionExecutor executor;

    public ChangeEntityParentsCommandHandler(ActionExecutor executor) {
        this.executor = executor;
    }

    @Nonnull
    @Override
    public String getChannelName() {
        return ChangeEntityParentsAction.CHANNEL;
    }

    @Override
    public Class<ChangeEntityParentsAction> getRequestClass() {
        return ChangeEntityParentsAction.class;
    }

    @Override
    public Mono<ChangeEntityParentsResult> handleRequest(ChangeEntityParentsAction request,
                                                          ExecutionContext executionContext) {
        return executor.executeRequest(request, executionContext);
    }
}