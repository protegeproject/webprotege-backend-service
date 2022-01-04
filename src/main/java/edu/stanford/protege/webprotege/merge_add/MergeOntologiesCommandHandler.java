package edu.stanford.protege.webprotege.merge_add;

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
public class MergeOntologiesCommandHandler implements CommandHandler<MergeOntologiesAction, MergeOntologiesResult> {

    private final ActionExecutor executor;

    public MergeOntologiesCommandHandler(ActionExecutor executor) {
        this.executor = executor;
    }

    @Nonnull
    @Override
    public String getChannelName() {
        return MergeOntologiesAction.CHANNEL;
    }

    @Override
    public Class<MergeOntologiesAction> getRequestClass() {
        return MergeOntologiesAction.class;
    }

    @Override
    public Mono<MergeOntologiesResult> handleRequest(MergeOntologiesAction request, ExecutionContext executionContext) {
        return executor.executeRequest(request, executionContext);
    }
}