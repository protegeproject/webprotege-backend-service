package edu.stanford.protege.webprotege.bulkop;

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
public class MoveEntitiesToParentCommandHandler implements CommandHandler<MoveEntitiesToParentAction, MoveEntitiesToParentResult> {

    private final ActionExecutor executor;

    public MoveEntitiesToParentCommandHandler(ActionExecutor executor) {
        this.executor = executor;
    }

    @NotNull
    @Override
    public String getChannelName() {
        return MoveEntitiesToParentAction.CHANNEL;
    }

    @Override
    public Class<MoveEntitiesToParentAction> getRequestClass() {
        return MoveEntitiesToParentAction.class;
    }

    @Override
    public Mono<MoveEntitiesToParentResult> handleRequest(MoveEntitiesToParentAction request,
                                                          ExecutionContext executionContext) {
        return Mono.just(executor.execute(request, executionContext));
    }
}