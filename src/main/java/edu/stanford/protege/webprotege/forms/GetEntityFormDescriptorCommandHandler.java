package edu.stanford.protege.webprotege.forms;

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
public class GetEntityFormDescriptorCommandHandler implements CommandHandler<GetEntityFormDescriptorAction, GetEntityFormDescriptorResult> {

    private final ActionExecutor executor;

    public GetEntityFormDescriptorCommandHandler(ActionExecutor executor) {
        this.executor = executor;
    }

    @Nonnull
    @Override
    public String getChannelName() {
        return GetEntityFormDescriptorAction.CHANNEL;
    }

    @Override
    public Class<GetEntityFormDescriptorAction> getRequestClass() {
        return GetEntityFormDescriptorAction.class;
    }

    @Override
    public Mono<GetEntityFormDescriptorResult> handleRequest(GetEntityFormDescriptorAction request,
                                                             ExecutionContext executionContext) {
        return executor.executeRequest(request, executionContext);
    }
}