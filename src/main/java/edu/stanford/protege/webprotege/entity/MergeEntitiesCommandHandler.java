package edu.stanford.protege.webprotege.entity;

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
public class MergeEntitiesCommandHandler implements CommandHandler<MergeEntitiesAction, MergeEntitiesResult> {

    private final ActionExecutor executor;

    public MergeEntitiesCommandHandler(ActionExecutor executor) {
        this.executor = executor;
    }

    @Nonnull
    @Override
    public String getChannelName() {
        return MergeEntitiesAction.CHANNEL;
    }

    @Override
    public Class<MergeEntitiesAction> getRequestClass() {
        return MergeEntitiesAction.class;
    }

    @Override
    public Mono<MergeEntitiesResult> handleRequest(MergeEntitiesAction request, ExecutionContext executionContext) {
        return executor.executeRequest(request, executionContext);
    }
}