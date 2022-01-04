package edu.stanford.protege.webprotege.viz;

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
public class SetEntityGraphActiveFiltersCommandHandler implements CommandHandler<SetEntityGraphActiveFiltersAction, SetEntityGraphActiveFiltersResult> {

    private final ActionExecutor executor;

    public SetEntityGraphActiveFiltersCommandHandler(ActionExecutor executor) {
        this.executor = executor;
    }

    @Nonnull
    @Override
    public String getChannelName() {
        return SetEntityGraphActiveFiltersAction.CHANNEL;
    }

    @Override
    public Class<SetEntityGraphActiveFiltersAction> getRequestClass() {
        return SetEntityGraphActiveFiltersAction.class;
    }

    @Override
    public Mono<SetEntityGraphActiveFiltersResult> handleRequest(SetEntityGraphActiveFiltersAction request,
                                                                 ExecutionContext executionContext) {
        return executor.executeRequest(request, executionContext);
    }
}