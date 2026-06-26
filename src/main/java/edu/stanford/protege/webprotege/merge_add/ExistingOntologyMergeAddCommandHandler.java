package edu.stanford.protege.webprotege.merge_add;

import edu.stanford.protege.webprotege.api.ActionExecutor;
import edu.stanford.protege.webprotege.ipc.CommandHandler;
import edu.stanford.protege.webprotege.ipc.ExecutionContext;
import edu.stanford.protege.webprotege.ipc.WebProtegeHandler;
import javax.annotation.Nonnull;
import reactor.core.publisher.Mono;

@WebProtegeHandler
public class ExistingOntologyMergeAddCommandHandler implements CommandHandler<ExistingOntologyMergeAddAction, ExistingOntologyMergeAddResult> {

    private final ActionExecutor executor;

    public ExistingOntologyMergeAddCommandHandler(ActionExecutor executor) {
        this.executor = executor;
    }

    @Nonnull
    @Override
    public String getChannelName() {
        return ExistingOntologyMergeAddAction.CHANNEL;
    }

    @Override
    public Class<ExistingOntologyMergeAddAction> getRequestClass() {
        return ExistingOntologyMergeAddAction.class;
    }

    @Override
    public Mono<ExistingOntologyMergeAddResult> handleRequest(ExistingOntologyMergeAddAction request, ExecutionContext executionContext) {
        return executor.executeRequest(request, executionContext);
    }
}
